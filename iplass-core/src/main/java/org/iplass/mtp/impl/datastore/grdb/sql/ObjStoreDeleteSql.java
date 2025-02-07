/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.grdb.sql;

import java.sql.Timestamp;

import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlConverter;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlQueryContext;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class ObjStoreDeleteSql extends UpdateSqlHandler {

	private static final String TMP_TABLE_ALIAS = "tt";

	public String deleteMainPageByOid(int tenantId, EntityHandler eh, String objId, Long version, boolean withOptimisticLock, Timestamp timestamp, RdbAdapter adapter) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='");
		sb.append(adapter.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjStoreTable.OBJ_ID + "='");
		sb.append(adapter.sanitize(objId));
		sb.append("' AND " + ObjStoreTable.OBJ_VER + "=");
		if (version != null) {
			sb.append(version);
		} else {
			sb.append("0");
		}
		sb.append(" AND " + ObjStoreTable.PG_NO + "=0");
		if (withOptimisticLock) {
			if (timestamp == null) {
				throw new NullPointerException("specify optimistic lock, but timestamp is null.");
			}
			sb.append(" AND " + ObjStoreTable.UP_DATE + "=");
			sb.append(adapter.toTimeStampExpression(timestamp));
		}

		return sb.toString();
	}

	public String deleteByOid(int tenantId, EntityHandler eh, String objId, Long version, RdbAdapter adapter) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='");
		sb.append(adapter.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjStoreTable.OBJ_ID + "='");
		sb.append(adapter.sanitize(objId));
		sb.append("' AND " + ObjStoreTable.OBJ_VER + "=");
		if (version != null) {
			sb.append(version);
		} else {
			sb.append("0");
		}
		return sb.toString();
	}

	public String deleteByCondition(int tenantId, EntityHandler eh,
			DeleteCondition cond, RdbAdapter rdb,
			EntityContext entityContext) {
		StringBuilder sb = new StringBuilder();
		String objStoreTableName = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE();
		if (rdb.isNeedMultiTableTrick()) {
			sb.append("DELETE ");
			sb.append(objStoreTableName);
			sb.append(" FROM ");
		} else {
			sb.append("DELETE FROM ");
		}
		sb.append(objStoreTableName);
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='");
		sb.append(rdb.sanitize(eh.getMetaData().getId()));
		if (rdb.isSupportRowValueConstructor()) {
			sb.append("' AND (" + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + ") in(");
		} else {
			sb.append("' AND EXISTS (");
		}

		//TODO joinがない場合は、inのサブクエリでなく、直接条件指定する

		Query q = new Query();
		q.select(Entity.OID, Entity.VERSION)
			.from(eh.getMetaData().getName())
			.setWhere(cond.getWhere());
		SqlQueryContext qc = new SqlQueryContext(eh, entityContext, rdb);
		SqlConverter conv = new SqlConverter(qc, false);
		q.accept(conv);

		if (rdb.isSupportRowValueConstructor()) {
			sb.append(rdb.tableAlias(qc.toSelectSql()));
		} else {
			String prefix = qc.getPrefix();
			String objStoreTable = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE();

			StringBuilder sbJoin = new StringBuilder();
			if (prefix != null) {
				sbJoin.append(prefix).append(".");
			}
			sbJoin.append(ObjStoreTable.OBJ_ID).append("=").append(objStoreTable).append(".").append(ObjStoreTable.OBJ_ID);
			sbJoin.append(" AND ");
			if (prefix != null) {
				sbJoin.append(prefix).append(".");
			}
			sbJoin.append(ObjStoreTable.OBJ_VER).append("=").append(objStoreTable).append(".").append(ObjStoreTable.OBJ_VER);

			sb.append(rdb.tableAlias(qc.toSelectSql(sbJoin.toString())));
		}
		sb.append(")");

		return sb.toString();
	}

//	public String toSqlByDelFlag(int tenantId, EntityHandler eh,
//			RdbAdapter rdb,
//			EntityContext entityContext) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
//		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=");
//		sb.append(tenantId);
//		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='");
//		sb.append(rdb.sanitize(eh.getMetaData().getId()));
//		sb.append("' AND " + ObjStoreTable.DEL_FLG + "='D'");
//
//		return sb.toString();
//	}

	public String deleteByTempTable(int tenantId, EntityHandler eh,
			RdbAdapter rdb,
			EntityContext entityContext) {
		StringBuilder sb = new StringBuilder();
		String objStoreTableName = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE();
		if (rdb.isNeedMultiTableTrick()) {
			sb.append("DELETE ");
			sb.append(objStoreTableName);
			sb.append(" FROM ");
		} else {
			sb.append("DELETE FROM ");
		}
		sb.append(objStoreTableName);
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='");
		sb.append(rdb.sanitize(eh.getMetaData().getId())).append("'");
		if (rdb.isSupportRowValueConstructor()) {
			sb.append(" AND (" + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + ") IN("
					+ "SELECT " + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + " FROM " + rdb.getTemplaryTablePrefix() + ObjStoreTable.TABLE_NAME_TMP + ")");
		} else {
			String objStoreTable = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE();
			sb.append(" AND EXISTS (");
			sb.append("SELECT 1 FROM ").append(rdb.getTemplaryTablePrefix()).append(ObjStoreTable.TABLE_NAME_TMP ).append(" ").append(TMP_TABLE_ALIAS);
			sb.append(" WHERE ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_ID ).append("=").append(objStoreTable).append(".").append(ObjStoreTable.OBJ_ID);
			sb.append(" AND ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_VER ).append("=").append(objStoreTable).append(".").append(ObjStoreTable.OBJ_VER);
			sb.append(")");
		}

		return sb.toString();
	}

//	public String deleteAllData(int tenantId, EntityHandler eh, RdbAdapter rdb) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
//		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=");
//		sb.append(tenantId);
//		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='");
//		sb.append(rdb.sanitize(eh.getMetaData().getId())).append("'");
//
//		return sb.toString();
//	}

	public String deleteAllDataByDefId(int tenantId, String defId, String tableNamePostfix, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(MetaGRdbEntityStore.makeObjStoreName(tableNamePostfix));
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='");
		sb.append(rdb.sanitize(defId)).append("'");

		return sb.toString();
	}

	public String countByOidWithoutTargetVersion(int tenantId, EntityHandler eh, String oid, Long version, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
		sb.append(" AND " + ObjStoreTable.OBJ_ID + "='").append(rdb.sanitize(oid)).append("'");
		sb.append(" AND " + ObjStoreTable.OBJ_VER + "!=").append(version);
		sb.append(" AND " + ObjStoreTable.PG_NO + "=0");
		return sb.toString();
	}
}
