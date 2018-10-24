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
import java.util.Map;
import java.util.Set;

import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class RdbSequenceCounterService implements CounterService {

	private Map<String, String> sequenceNameMap;

	private RdbAdapter rdb;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();
		sequenceNameMap = config.getValue("sequenceNameMap", Map.class);
		if (sequenceNameMap == null) {
			throw new ServiceConfigrationException("sequenceNameMap must specify.");
		}
	}

	public Map<String, String> getSequenceNameMap() {
		return sequenceNameMap;
	}

	@Override
	public void destroy() {
	}

	@Override
	public long increment(final int tenantId, final String incrementUnitKey, long initialCount) {
		SqlExecuter<Long> exec = new SqlExecuter<Long>() {
			@Override
			public Long logic() throws SQLException {
				String sequenceName = sequenceNameMap.get(incrementUnitKey);
				String seqSql = rdb.seqNextSelectSql(sequenceName, tenantId, incrementUnitKey);
				ResultSet rs = getStatement().executeQuery(seqSql);
				try {
					rs.next();
					return rs.getLong(1);
				} finally {
					rs.close();
				}
			}
		};
		return exec.execute(rdb, true);
	}

	@Override
	public void resetCounter(int tenantId, String incrementUnitKey) {
		throw new UnsupportedOperationException("sequence counter not supported online sequence generation");
	}

	@Override
	public void resetCounter(int tenantId, String incrementUnitKey, long currentCount) {
		throw new UnsupportedOperationException("sequence counter not supported online sequence generation");
	}

	@Override
	public void deleteCounter(int tenantId, String incrementUnitKey) {
		throw new UnsupportedOperationException("sequence counter not supported online sequence generation");
	}

	@Override
	public long current(int tenantId, String incrementUnitKey) {
		throw new UnsupportedOperationException("sequence counter's current() method is unsupported");
	}

	@Override
	public Set<String> keySet(int tenantId, String prefixIncrementUnitKey) {
		throw new UnsupportedOperationException("sequence counter's keySet() method is unsupported");
	}

}
