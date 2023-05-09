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

package org.iplass.mtp.impl.rdb.connection;

import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.transaction.LocalTransaction;
import org.iplass.mtp.impl.transaction.LocalTransactionManager;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public abstract class AbstractConnectionFactory extends ConnectionFactory {
	
	public static final String CLIENT_INFO_THREAD_NAME = "thread";

	private static final Logger logger = LoggerFactory.getLogger(AbstractConnectionFactory.class);
	private int warnLogThreshold;
	private boolean warnLogBefore;
	private boolean countSqlExecution;
	private TransactionIsolationLevel transactionIsolationLevel;
	
	private Map<String, Object> clientInfoMap;
	private int clientInfoMaxLength;

	private boolean isDefault;

	@Override
	public Connection getConnection() {
		return getConnection(null);
	}

	@Override
	public Connection getConnection(Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		if (isDefault) {
			TransactionService ts = ServiceRegistry.getRegistry().getService(TransactionService.class);
			int warnLogThreshold = getWarnLogThreshold();

			TransactionManager tm = ts.getTransacitonManager();
			Transaction ct = tm.currentTransaction();
			ResourceHolder rh = ResourceHolder.getResourceHolder();
			//LocalTransactionの場合
			if (ct instanceof LocalTransaction) {
				LocalTransaction t = (LocalTransaction) ct;
				if (t.getStatus() == TransactionStatus.ACTIVE) {
					if (t.getCon() == null) {
						try {
							if (rh == null || rh.isInUse() || (isCreateConnectionIfReadOnlyTransaction() && t.isReadOnly())) {
								// ResourceHolder使ってない場合/既にResourceHolder利用されている場合/ReadOnlyの場合に新規にコネクション作成するかつ、ReadOnlyの場合は物理Connection
								t.setCon(new LocalTransactionConnectionWrapper(
										getPhysicalConnection(afterGetPhysicalConnectionHandler), true, null, warnLogThreshold, warnLogBefore, countSqlExecution));
							} else {
								t.setCon(new LocalTransactionConnectionWrapper(
										getHoldingConnection(rh, afterGetPhysicalConnectionHandler), true, rh, warnLogThreshold, warnLogBefore, countSqlExecution));
							}
						} catch (SQLException e) {
							throw new ConnectionException(e);
						}
					}
					return t.getCon();
				} else {
					if (rh == null || rh.isInUse()) {
						return new LocalTransactionConnectionWrapper(
								getPhysicalConnection(afterGetPhysicalConnectionHandler), false, null, warnLogThreshold, warnLogBefore, countSqlExecution);
					} else {
						//未使用のResourceHolderのコネクション
						return new LocalTransactionConnectionWrapper(
								getHoldingConnection(rh, afterGetPhysicalConnectionHandler), false, rh, warnLogThreshold, warnLogBefore, countSqlExecution);
					}
				}
			}

			//LocalTransactionだけど、トランザクション起動されていない場合
			if (tm instanceof LocalTransactionManager) {
				if (rh == null || rh.isInUse()) {
					return new LocalTransactionConnectionWrapper(
							getPhysicalConnection(afterGetPhysicalConnectionHandler), false, null, warnLogThreshold, warnLogBefore, countSqlExecution);
				} else {
					return new LocalTransactionConnectionWrapper(
							getHoldingConnection(rh, afterGetPhysicalConnectionHandler), false, rh, warnLogThreshold, warnLogBefore, countSqlExecution);
				}
			}
		}
		//デフォルトのConnectionFactoryでない場合、、LocalTransactionでない場合、そのまま素直に生成
		//TODO トランザクション管理
		return new LocalTransactionConnectionWrapper(
				getPhysicalConnection(afterGetPhysicalConnectionHandler), false, null, warnLogThreshold, warnLogBefore, countSqlExecution);
	}

	Connection getHoldingConnection(ResourceHolder rh, Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		Connection con = rh.getConnection(this, afterGetPhysicalConnectionHandler);
		if (logger.isDebugEnabled()) {
			logger.debug("getConnection from ResourceHolder:" + con);
		}
		setClientInfo(con);
		return con;
	}
	
	private void setClientInfo(Connection con) {
		if (clientInfoMap != null && !clientInfoMap.isEmpty()) {
			try {
				for (Map.Entry<String, Object> e: clientInfoMap.entrySet()) {
					CharSequence val = clientInfoValue(e.getValue());
					con.setClientInfo(e.getKey(), substrClientInfoValue(val));
				}
			} catch (SQLClientInfoException e) {
				throw new ConnectionException("Can not setClientInfo.", e);
			}
		}
	}
	
	private CharSequence clientInfoValue(Object name) {
		if (name instanceof List) {
			StringBuilder sb = new StringBuilder();
			List<String> names = (List<String>) name;
			for (int i = 0; i < names.size(); i++) {
				if (i != 0) {
					sb.append("-");
				}
				sb.append(clientInfoValue(names.get(i)));
			}
			return sb;
		} else {
			String n = (String) name;
			if (CLIENT_INFO_THREAD_NAME.equals(n)) {
				return Thread.currentThread().getName();
			} else if ( n != null && !n.isEmpty()) {
				String val = MDC.get(n);
				if (val == null) {
					val = "";
				}
				return val;
			} else {
				return "";
			}
		}
	}
	
	private String substrClientInfoValue(CharSequence val) {
		if (clientInfoMaxLength >= 0) {
			if (val.length() > clientInfoMaxLength) {
				val = val.subSequence(val.length() - clientInfoMaxLength, val.length());
			}
		}
		return val.toString();
	}

	Connection getPhysicalConnection(Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		Connection con = getConnectionInternal();
		if (logger.isDebugEnabled()) {
			logger.debug("create physical connection:" + con);
		}
		initPhysicalConnection(con, afterGetPhysicalConnectionHandler);
		return con;
	}
	
	protected void initPhysicalConnection(Connection con, Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		setClientInfo(con);
		if (transactionIsolationLevel != null) {
			try {
				con.setTransactionIsolation(transactionIsolationLevel.sqlIntValue());
			} catch (SQLException e) {
				throw new ConnectionException("Can not setTransactionIsolation level.", e);
			}
		}
		if (afterGetPhysicalConnectionHandler != null) {
			con = afterGetPhysicalConnectionHandler.apply(con);
		}
	}

	protected abstract Connection getConnectionInternal();

	@Override
	public void init(Config config) {

		String serviceName = config.getServiceName();
		if (serviceName.equals(ConnectionFactory.class.getName())) {
			isDefault = true;
		}

		warnLogThreshold = config.getValue("warnLogThreshold", Integer.TYPE, 0);
		warnLogBefore = config.getValue("warnLogBefore", Boolean.TYPE, true);
		countSqlExecution = config.getValue("countSqlExecution", Boolean.TYPE, true);
		
		transactionIsolationLevel = config.getValue("transactionIsolationLevel", TransactionIsolationLevel.class);
		
		clientInfoMap = config.getValue("clientInfoMap", Map.class);
		clientInfoMaxLength = config.getValue("clientInfoMaxLength", Integer.TYPE, -1);
		
	}

	@Override
	public boolean isWarnLogBefore() {
		return warnLogBefore;
	}

	@Override
	public int getWarnLogThreshold() {
		return warnLogThreshold;
	}

	@Override
	public TransactionIsolationLevel getTransactionIsolationLevel() {
		return transactionIsolationLevel;
	}
	
	@Override
	public boolean isCountSqlExecution() {
		return countSqlExecution;
	}
	
	@Override
	public AtomicInteger getCounterOfSqlExecution() {
		if (countSqlExecution) {
			ExecuteContext ec = ExecuteContext.getCurrentContext();
			AtomicInteger sqlCount = (AtomicInteger) ec.getAttribute(ConnectionFactory.SQL_COUNT_KEY);
			if (sqlCount == null) {
				sqlCount = new AtomicInteger();
				ec.setAttribute(ConnectionFactory.SQL_COUNT_KEY, sqlCount, true);
			}
			return sqlCount;
		} else {
			return null;
		}
	}

	/**
	 * 読み取り専用トランザクションの場合に、新規にコネクションを作成するか。
	 * 新規にコネクションを作成する場合は、true を返却する。
	 * 
	 * @return 新規にコネクションを作成する場合は true 
	 */
	protected boolean isCreateConnectionIfReadOnlyTransaction() {
		return false;
	}
}
