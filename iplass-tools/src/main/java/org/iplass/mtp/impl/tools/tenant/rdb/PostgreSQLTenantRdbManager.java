/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.tenant.rdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.tools.tenant.PartitionCreateParameter;
import org.iplass.mtp.impl.tools.tenant.PartitionInfo;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;

public class PostgreSQLTenantRdbManager extends DefaultTenantRdbManager {

	private static final String SQL_TENANT_EXISTS = "select 1 where exists (select * from t_tenant where url = ?)";
	private static final String POSTGRESQL_EXIST_TABLE_SQL = "select count(*) from information_schema.tables where table_name = ?";

	private RdbAdapter adapter;

	public PostgreSQLTenantRdbManager(RdbAdapter adapter) {
		super(adapter);
		this.adapter = adapter;
	}

	@Override
	public boolean existsURL(String url) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {
				//検索SQL実行
				PreparedStatement statement = getPreparedStatement(SQL_TENANT_EXISTS);
				String key = !url.startsWith("/") ? "/" + url : url;
				statement.setString(1, key);
				ResultSet rs = statement.executeQuery();
				try {
					return rs.next();
				} finally {
					rs.close();
				}
			}
		};
		return exec.execute(adapter, true);
	}

	@Override
	public boolean isSupportPartition() {
		return false;
	}
	@Override
	public List<PartitionInfo> getPartitionInfo() {
		return null;
	}
	@Override
	public boolean createPartition(PartitionCreateParameter param, LogHandler logHandler) {
		return false;
	}

	@Override
	public boolean dropPartition(PartitionDeleteParameter param, LogHandler logHandler) {
		return false;
	}

	@Override
	protected boolean isExistsTable(String tableName) {

		SqlExecuter<Boolean> exec = createCheckExistSqlExecuter(POSTGRESQL_EXIST_TABLE_SQL, tableName);
		return exec.execute(adapter, true);
	}

	private SqlExecuter<Boolean> createCheckExistSqlExecuter(final String sql, final String tableName) {
		return new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {

				PreparedStatement ps = getPreparedStatement(sql);
				ps.setString(1, tableName);

				ResultSet rs = ps.executeQuery();
				int count = 0;
				try {
					if(rs.next()) {
						count = rs.getInt(1);
					}
				} finally {
					rs.close();
				}
				if (count <= 0) {
					return false;
				}
				return true;
			}
		};
	}

}
