/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjRefTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class ReferenceDeleteSql extends UpdateSqlHandler {

	public String toSql(int tenantId, EntityHandler eh, String oid, Long version, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjRefTable.OBJ_ID + "='").append(rdb.sanitize(oid)).append("'");
		if (version != null) {
			sb.append(" AND " + ObjRefTable.OBJ_VER + "=").append(version);
		} else {
			sb.append(" AND " + ObjRefTable.OBJ_VER + "=0");
		}

		return sb.toString();
	}

	public String deleteByOidAndVersion(int tenantId, EntityHandler eh, String refDefId, String oid, Long version, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjRefTable.REF_DEF_ID + "='").append(rdb.sanitize(refDefId));
		sb.append("' AND " + ObjRefTable.OBJ_ID + "='").append(rdb.sanitize(oid)).append("'");
		if (version != null) {
			sb.append(" AND " + ObjRefTable.OBJ_VER + "=").append(version);
		} else {
			sb.append(" AND " + ObjRefTable.OBJ_VER + "=0");
		}

		return sb.toString();
	}

	public String deleteByTempTable(int tenantId, EntityHandler eh, RdbAdapter rdb, EntityContext context) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId()));
		if (rdb.isSupportRowValueConstructor()) {
			sb.append("' AND (" + ObjRefTable.OBJ_ID + "," + ObjRefTable.OBJ_VER + ") IN("
					+ "SELECT " + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + " FROM " + rdb.getTemplaryTablePrefix() + ObjStoreTable.TABLE_NAME_TMP + ")");
		} else {
			String objRefTable = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF();
			sb.append("' AND EXISTS (");
			sb.append("SELECT 1 FROM ").append(rdb.getTemplaryTablePrefix()).append(ObjStoreTable.TABLE_NAME_TMP ).append(" TMP");
			sb.append(" WHERE ").append("TMP.").append(ObjStoreTable.OBJ_ID ).append("=").append(objRefTable).append(".").append(ObjStoreTable.OBJ_ID);
			sb.append(" AND ").append("TMP.").append(ObjStoreTable.OBJ_VER ).append("=").append(objRefTable).append(".").append(ObjStoreTable.OBJ_VER);
			sb.append(")");
		}
		return sb.toString();
	}

//	public String toSqlByCond(int tenantId, EntityHandler eh, DeleteCondition cond, boolean useIndex, RdbAdapter rdb, EntityContext context) {
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
//		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId()));
//		sb.append("' AND (" + ObjRefTable.OBJ_ID + "," + ObjRefTable.OBJ_VER + ") IN(");
//
//		Query q = new Query();
//		q.selectDistinct(Entity.OID, Entity.VERSION)
//			.from(eh.getMetaData().getName())
//			.setWhere(cond.getWhere());
//		SqlQueryContext qc = new SqlQueryContext(eh, context, rdb);
//		qc.setUseIndexTable(useIndex);
//		SqlConverter conv = new SqlConverter(qc, false);
//		q.accept(conv);
//
//		sb.append(rdb.tableAlias(qc.toSelectSql()));
//
//		sb.append(")");
//		return sb.toString();
//	}

//	public String deleteAll(int tenantId, EntityHandler eh, RdbAdapter rdb) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
//		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
//
//		return sb.toString();
//	}

//	public String deleteAllByTargetDefId(int tenantId, EntityHandler eh, RdbAdapter rdb) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
//		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjRefTable.TARGET_OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
//
//		return sb.toString();
//	}

	public String deleteAll(int tenantId, String defId, String tableNamePostfix, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(MetaGRdbEntityStore.makeObjRefTableName(tableNamePostfix));
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(defId)).append("'");

		return sb.toString();
	}

	public String deleteAllByTargetDefId(int tenantId, String targetDefId, String tableNamePostfix, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(MetaGRdbEntityStore.makeObjRefTableName(tableNamePostfix));
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.TARGET_OBJ_DEF_ID + "='").append(rdb.sanitize(targetDefId)).append("'");

		return sb.toString();
	}

	public String deleteForDefrag(int tenantId, EntityHandler eh, List<String> usedRefPropertyIds, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");

		//利用されているReferencePropertyを除外する
		if (!usedRefPropertyIds.isEmpty()) {
			sb.append(" AND " + ObjRefTable.REF_DEF_ID + " NOT IN (");
			boolean isFirst = true;
			for (String refProperty : usedRefPropertyIds) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(",");
				}
				sb.append("'").append(rdb.sanitize(refProperty)).append("'");
			}
			sb.append(")");
		}

		return sb.toString();
	}

}
