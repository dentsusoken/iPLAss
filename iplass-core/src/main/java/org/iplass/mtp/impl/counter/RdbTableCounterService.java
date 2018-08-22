/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.mtp.impl.counter;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.counter.sql.RdbTableCounterSql;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdbTableCounterService implements CounterService {
	private static Logger logger = LoggerFactory.getLogger(RdbTableCounterService.class);

	private boolean separateTransaction;
	private int retryCount;
	private String counterTypeName;

	private RdbTableCounterSql sql;
	private RdbAdapter rdb;

	public String getCounterTypeName() {
		return counterTypeName;
	}

	public void setCounterTypeName(String counterTypeName) {
		this.counterTypeName = counterTypeName;
	}

	protected RdbAdapter getRdbAdapter() {
		return rdb;
	}

	protected RdbTableCounterSql getCounterSql() {
		return sql;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public boolean isSeparateTransaction() {
		return separateTransaction;
	}

	@Override
	public void init(Config config) {
		rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();
		separateTransaction = config.getValue("separateTransaction", Boolean.TYPE, false);
		retryCount = config.getValue("retryCount", Integer.TYPE, 3);
		sql = rdb.getUpdateSqlCreator(RdbTableCounterSql.class);

		counterTypeName = config.getValue("counterTypeName");
		if (counterTypeName == null) {
			counterTypeName = "defaultCounter";
		}
	}

	@Override
	public void destroy() {
	}

	private Long incrementInternal(final int tenantId, final String incrementUnitKey) {
		SqlExecuter<Long> executer = new SqlExecuter<Long>() {
			@Override
			public Long logic() throws SQLException {

				String select = sql.currentValueSql(tenantId, counterTypeName, incrementUnitKey, true, rdb);
				ResultSet rs = getStatement().executeQuery(select);
				long current = Long.MIN_VALUE;
				try {
					if (rs.next()) {
						current = rs.getLong(1);
					}
				} finally {
					rs.close();
				}
				if (current != Long.MIN_VALUE) {
					String update = sql.incrementSql(tenantId, counterTypeName, incrementUnitKey, 1, rdb);
					int res = getStatement().executeUpdate(update);
					if (res != 1) {
						throw new SystemException("counter:" + counterTypeName + "." + incrementUnitKey + " increment failed");
					}
					return current + 1L;
				} else {
					return null;
				}
			}
		};
		return executer.execute(rdb, true);
	}

	private Long incrementOrInit(final int tenantId, final String incrementUnitKey, final long initialCount) {
		Long v = incrementInternal(tenantId, incrementUnitKey);
		if (v == null) {
			resetCounter(tenantId, incrementUnitKey, initialCount - 1);
			if (logger.isDebugEnabled()) {
				logger.debug("init Counter:tenandId=" + tenantId + ", key=" + incrementUnitKey + ", startsWith=" + initialCount);
			}
			v = incrementInternal(tenantId, incrementUnitKey);
		}
		return v;
	}

	@Override
	public long increment(final int tenantId, final String incrementUnitKey, final long initialCount) {
		Propagation p = separateTransaction ? Propagation.REQUIRES_NEW: Propagation.REQUIRED;
		Long v = null;
		RuntimeException exp = null;
		for (int i = 0; i < retryCount; i++) {
			try {
				v = Transaction.with(p, t -> {
					return incrementOrInit(tenantId, incrementUnitKey, initialCount);
				});
			} catch (RuntimeException e) {
				exp = e;
			}
			if (v != null) {
				return v.longValue();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("fail to increment counter:" + counterTypeName + "." + incrementUnitKey + ", retry...");
			}
		}

		if (exp != null) {
			throw exp;
		}

		throw new SystemException("counter:" + counterTypeName + "." + incrementUnitKey + " increment failed. created counter row can't view... May be Transaction Isolation Level is not READ_COMITTED...");
	}

	@Override
	public void resetCounter(int tenantId, String incrementUnitKey) {
		resetCounter(tenantId, incrementUnitKey, 0);
	}

	@Override
	public void resetCounter(final int tenantId, final String incrementUnitKey, final long currentCount) {
		Transaction.required(t -> {
			SqlExecuter<Void> executer = new SqlExecuter<Void>() {
				@Override
				public Void logic() throws SQLException {

					String select = sql.currentValueSql(tenantId, counterTypeName, incrementUnitKey, true, rdb);
					ResultSet rs = getStatement().executeQuery(select);
					Long current = null;
					try {
						if (rs.next()) {
							current = rs.getLong(1);
						}
					} finally {
						rs.close();
					}

					if (current != null) {
						String delete = sql.deleteCounterSql(tenantId, counterTypeName, incrementUnitKey, rdb);
						getStatement().addBatch(delete);
					}
					String create = sql.createCounterSql(tenantId, counterTypeName, incrementUnitKey, currentCount, rdb);
					getStatement().addBatch(create);
					int res[] = getStatement().executeBatch();
					if (res[res.length - 1] != 1) {
						throw new SystemException("counter:" + counterTypeName + "." + incrementUnitKey + " reset failed");
					}
					return null;
				}
			};
			return executer.execute(rdb, true);
		});
	}

	@Override
	public void deleteCounter(final int tenantId, final String incrementUnitKey) {
		Transaction.required(t -> {
			SqlExecuter<Void> executer = new SqlExecuter<Void>() {
				@Override
				public Void logic() throws SQLException {

					String select = sql.currentValueSql(tenantId, counterTypeName, incrementUnitKey, true, rdb);
					ResultSet rs = getStatement().executeQuery(select);
					Long current = null;
					try {
						if (rs.next()) {
							current = rs.getLong(1);
						}
					} finally {
						rs.close();
					}
					if (current != null) {
						String delete = sql.deleteCounterSql(tenantId, counterTypeName, incrementUnitKey, rdb);
						//削除はエラーにしない
//						int res = getStatement().executeUpdate(delete);
						getStatement().executeUpdate(delete);
					}
					return null;
				}
			};
			return executer.execute(rdb, true);
		});
	}

	@Override
	public long current(final int tenantId, final String incrementUnitKey) {
		SqlExecuter<Long> executer = new SqlExecuter<Long>() {
			@Override
			public Long logic() throws SQLException {

				String select = sql.currentValueSql(tenantId, counterTypeName, incrementUnitKey, false, rdb);
				ResultSet rs = getStatement().executeQuery(select);
				Long current = null;
				try {
					if (rs.next()) {
						current = rs.getLong(1);
					} else {
						return Long.valueOf(-1L);
					}
				} finally {
					rs.close();
				}
				return current.longValue();
			}
		};
		return executer.execute(rdb, true);
	}

}
