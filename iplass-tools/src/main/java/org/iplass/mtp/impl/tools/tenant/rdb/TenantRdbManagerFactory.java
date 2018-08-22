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

import java.util.List;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.mysql.MysqlRdbAdaptor;
import org.iplass.mtp.impl.rdb.oracle.OracleRdbAdapter;
import org.iplass.mtp.impl.rdb.postgresql.PostgreSQLRdbAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.SqlServerRdbAdapter;
import org.iplass.mtp.impl.tools.tenant.PartitionCreateParameter;
import org.iplass.mtp.impl.tools.tenant.PartitionInfo;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;

public class TenantRdbManagerFactory {

	public TenantRdbManager createManager(RdbAdapter adapter) {

		if (adapter instanceof OracleRdbAdapter) {
			return new OracleTenantRdbManager(adapter);
		} else if (adapter instanceof MysqlRdbAdaptor) {
			return new MySQLTenantRdbManager(adapter);
		} else if (adapter instanceof SqlServerRdbAdapter) {
			return new SqlServerTenantRdbManager(adapter);
		} else if (adapter instanceof PostgreSQLRdbAdapter) {
			return new PostgreSQLTenantRdbManager(adapter);
		} else {
			return new EmptyTenantRdbManager(adapter);
		}
	}

	private static class EmptyTenantRdbManager extends DefaultTenantRdbManager {

		public EmptyTenantRdbManager(RdbAdapter adapter) {
			super(adapter);
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
			return false;
		}

	}
}
