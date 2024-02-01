/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.tools.batch;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.mysql.MysqlRdbAdaptor;
import org.iplass.mtp.impl.rdb.oracle.OracleRdbAdapter;
import org.iplass.mtp.impl.rdb.postgresql.PostgreSQLRdbAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.SqlServerRdbAdapter;

/**
 * 設定情報
 */
public class ConfigSetting {

	private final String configFileName;

	private final RdbAdapter adapter;
	private final String connectionInfo;

	public ConfigSetting(String configFileName, RdbAdapter adapter, String connectionInfo) {
		this.configFileName = configFileName;
		this.adapter = adapter;
		this.connectionInfo = connectionInfo;
	}

	public String getConfigFileName() {
		return configFileName;
	}

	public String getRdbAdapterName() {
		return (adapter != null ? adapter.getClass().getSimpleName() : "");
	}

	public String getConnectionInfo() {
		return connectionInfo;
	}

	public boolean isOracle() {
		return (adapter != null && adapter instanceof OracleRdbAdapter);
	}
	public boolean isMySQL() {
		return (adapter != null && adapter instanceof MysqlRdbAdaptor);
	}
	public boolean isPostgreSQL() {
		return (adapter != null && adapter instanceof PostgreSQLRdbAdapter);
	}
	public boolean isSQLServer() {
		return (adapter != null && adapter instanceof SqlServerRdbAdapter);
	}

}
