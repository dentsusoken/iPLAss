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

import java.sql.Timestamp;
import java.util.List;

import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjRefTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class RecycleBinSql extends UpdateSqlHandler {

	public String copyDataToRB(int tenantId, EntityHandler eh,
			Long rbid, String oid, String userId, RdbAdapter rdb, StorageSpaceMap ssmap) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE_RB());
		sb.append("(" + ObjStoreTable.RB_ID + "," + ObjStoreTable.RB_DATE + "," + ObjStoreTable.RB_USER + ",");
		appendAllDataCols(sb, ssmap, eh);
		sb.append(") SELECT ");
		sb.append(rbid).append(",");
		sb.append(rdb.systimestamp()).append(",");
		sb.append("'").append(rdb.sanitize(userId)).append("',");
		appendAllDataCols(sb, ssmap, eh);
		sb.append(" FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjStoreTable.OBJ_ID + "='").append(rdb.sanitize(oid)).append("'");

		return sb.toString();
	}

	private void appendAllDataCols(StringBuilder sb, StorageSpaceMap ssmap, EntityHandler eh) {
		sb.append(ObjStoreTable.TENANT_ID).append(',');
		sb.append(ObjStoreTable.OBJ_DEF_ID).append(',');
		sb.append(ObjStoreTable.PG_NO).append(',');
		sb.append(ObjStoreTable.OBJ_ID).append(',');
		sb.append(ObjStoreTable.OBJ_VER).append(',');
		sb.append(ObjStoreTable.OBJ_DEF_VER).append(',');
		sb.append(ObjStoreTable.STATUS).append(',');
		sb.append(ObjStoreTable.OBJ_NAME).append(',');
		sb.append(ObjStoreTable.OBJ_DESC).append(',');
		sb.append(ObjStoreTable.CRE_DATE).append(',');
		sb.append(ObjStoreTable.UP_DATE).append(',');
		sb.append(ObjStoreTable.S_DATE).append(',');
		sb.append(ObjStoreTable.E_DATE).append(',');
		sb.append(ObjStoreTable.LOCK_USER).append(',');
		sb.append(ObjStoreTable.CRE_USER).append(',');
		sb.append(ObjStoreTable.UP_USER);

		addCol(sb, ObjStoreTable.VALUE_STR_PREFIX, null, ssmap.getVarcharColumns());
		addCol(sb, ObjStoreTable.VALUE_NUM_PREFIX, null, ssmap.getDecimalColumns());
		addCol(sb, ObjStoreTable.VALUE_TS_PREFIX, null, ssmap.getTimestampColumns());
		addCol(sb, ObjStoreTable.VALUE_DBL_PREFIX, null, ssmap.getDoubleColumns());
		addCol(sb, ObjStoreTable.INDEX_STR_PREFIX, null, ssmap.getIndexedVarcharColumns());
		addCol(sb, ObjStoreTable.INDEX_STR_PREFIX, ObjStoreTable.INDEX_TD_POSTFIX, ssmap.getIndexedVarcharColumns());
		addCol(sb, ObjStoreTable.INDEX_NUM_PREFIX, null, ssmap.getIndexedDecimalColumns());
		addCol(sb, ObjStoreTable.INDEX_NUM_PREFIX, ObjStoreTable.INDEX_TD_POSTFIX, ssmap.getIndexedDecimalColumns());
		addCol(sb, ObjStoreTable.INDEX_TS_PREFIX, null, ssmap.getIndexedTimestampColumns());
		addCol(sb, ObjStoreTable.INDEX_TS_PREFIX, ObjStoreTable.INDEX_TD_POSTFIX, ssmap.getIndexedTimestampColumns());
		addCol(sb, ObjStoreTable.INDEX_DBL_PREFIX, null, ssmap.getUniqueIndexedDoubleColumns());
		addCol(sb, ObjStoreTable.INDEX_DBL_PREFIX, ObjStoreTable.INDEX_TD_POSTFIX, ssmap.getUniqueIndexedDoubleColumns());
		addCol(sb, ObjStoreTable.UNIQUE_STR_PREFIX, null, ssmap.getUniqueIndexedVarcharColumns());
		addCol(sb, ObjStoreTable.UNIQUE_STR_PREFIX, ObjStoreTable.INDEX_TD_POSTFIX, ssmap.getUniqueIndexedVarcharColumns());
		addCol(sb, ObjStoreTable.UNIQUE_NUM_PREFIX, null, ssmap.getUniqueIndexedDecimalColumns());
		addCol(sb, ObjStoreTable.UNIQUE_NUM_PREFIX, ObjStoreTable.INDEX_TD_POSTFIX, ssmap.getUniqueIndexedDecimalColumns());
		addCol(sb, ObjStoreTable.UNIQUE_TS_PREFIX, null, ssmap.getUniqueIndexedTimestampColumns());
		addCol(sb, ObjStoreTable.UNIQUE_TS_PREFIX, ObjStoreTable.INDEX_TD_POSTFIX, ssmap.getUniqueIndexedTimestampColumns());
		addCol(sb, ObjStoreTable.UNIQUE_DBL_PREFIX, null, ssmap.getUniqueIndexedDoubleColumns());
		addCol(sb, ObjStoreTable.UNIQUE_DBL_PREFIX, ObjStoreTable.INDEX_TD_POSTFIX, ssmap.getUniqueIndexedDoubleColumns());

		for (PropertyHandler p: eh.getDeclaredPropertyList()) {
			GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) p.getStoreSpecProperty();
			if (col != null && col.isNative()) {
				List<GRdbPropertyStoreHandler> cols = col.asList();
				for (GRdbPropertyStoreHandler c: cols) {
					sb.append(',');
					sb.append(c.getMetaData().getColumnName());
				}
			}
		}
	}

	private void addCol(StringBuilder sb, String prefix, String postFix, int count) {
		for (int i = 1; i <= count; i++) {
			sb.append(',');
			sb.append(prefix).append(i);
			if (postFix != null) {
				sb.append(postFix);
			}
		}
	}

	public String copyDataFromRB(int tenantId, EntityHandler eh,
			Long rbid, RdbAdapter rdb, EntityContext context, StorageSpaceMap ssmap) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append("(");
		appendAllDataCols(sb, ssmap, eh);
		sb.append(") SELECT ");
		appendAllDataCols(sb, ssmap, eh);
		sb.append(" FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE_RB());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjStoreTable.RB_ID + "=").append(rbid);
		return sb.toString();
	}

	public String deleteDataRB(int tenantId, EntityHandler eh, Long rbid, Timestamp ts, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE_RB());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
		if (rbid != null) {
			sb.append(" AND " + ObjStoreTable.RB_ID + "=").append(rbid);
		}
		if (ts != null) {
			sb.append(" AND " + ObjStoreTable.RB_DATE + "<").append(rdb.toTimeStampExpression(ts));
		}
		return sb.toString();
	}

	public String deleteRefRB(int tenantId, EntityHandler eh, Long rbid, Timestamp ts, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF_RB());
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
		if (rbid != null) {
			sb.append(" AND " + ObjRefTable.RB_ID + "=").append(rbid);
		}
		if (ts != null) {
			sb.append(" AND " + ObjRefTable.RB_ID + " IN (SELECT ").append(ObjStoreTable.RB_ID + " FROM ");
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE_RB());
			sb.append(" WHERE ");
			sb.append(ObjStoreTable.TENANT_ID + "=").append(tenantId);
			sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
			if (ts != null) {
				sb.append(" AND " + ObjStoreTable.RB_DATE + "<").append(rdb.toTimeStampExpression(ts));
			}
			sb.append(")");
		}
		return sb.toString();
	}

//	public String deleteRefRBByTargetDefId(int tenantId, EntityHandler eh, RdbAdapter rdb) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF_RB());
//		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjRefTable.TARGET_OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
//		return sb.toString();
//	}

	public String copyRefToRB(int tenantId, long rbid, EntityHandler eh, String oid, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF_RB());
		sb.append("(").append(ObjRefTable.RB_ID).append(",");
		appendAllRefCols(sb);
		sb.append(") SELECT ");
		sb.append(rbid).append(",");
		appendAllRefCols(sb);
		sb.append(" FROM ").append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjRefTable.OBJ_ID + "='").append(rdb.sanitize(oid)).append("'");

		return sb.toString();
	}

	private void appendAllRefCols(StringBuilder sb) {
		sb.append(ObjRefTable.TENANT_ID
				+ "," + ObjRefTable.OBJ_DEF_ID
				+ "," + ObjRefTable.REF_DEF_ID
				+ "," + ObjRefTable.OBJ_ID
				+ "," + ObjRefTable.OBJ_VER
				+ "," + ObjRefTable.TARGET_OBJ_DEF_ID
				+ "," + ObjRefTable.TARGET_OBJ_ID
				+ "," + ObjRefTable.TARGET_OBJ_VER);
	}

	public String copyRefFromRB(int tenantId, long rbid, EntityHandler eh, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
		sb.append("(");
		appendAllRefCols(sb);
		sb.append(") SELECT ");
		appendAllRefCols(sb);
		sb.append(" FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF_RB());
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjRefTable.RB_ID + "=").append(rbid);

		return sb.toString();
	}


	public String lockData(int tenantId, EntityHandler eh, Long rbid, Timestamp ts, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ ObjStoreTable.TENANT_ID
				+ "," + ObjStoreTable.OBJ_DEF_ID
				+ "," + ObjStoreTable.RB_ID
				+ "," + ObjStoreTable.OBJ_ID
				+ "," + ObjStoreTable.OBJ_VER
				+ " FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE_RB());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
		if (rbid != null) {
			sb.append(" AND " + ObjStoreTable.RB_ID + "=").append(rbid);
		}
		if (ts != null) {
			sb.append(" AND " + ObjStoreTable.RB_DATE + "<").append(rdb.toTimeStampExpression(ts));
		}

		return rdb.createRowLockSql(sb.toString());
	}


	public String searchRB(int tenantId, EntityHandler eh, Long rbid, RdbAdapter rdb) {
		return searchRB(tenantId, eh, rbid, null, rdb);
	}

	public String searchRB(int tenantId, EntityHandler eh, Long rbid, Timestamp ts, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ ObjStoreTable.TENANT_ID
				+ "," + ObjStoreTable.OBJ_DEF_ID
				+ "," + ObjStoreTable.RB_ID
				+ "," + ObjStoreTable.OBJ_ID
				+ "," + ObjStoreTable.OBJ_NAME
				+ "," + ObjStoreTable.RB_DATE
				+ "," + ObjStoreTable.RB_USER
				+ " FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE_RB());
		sb.append(" A WHERE ");
		sb.append(ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
		if (rbid != null) {
			sb.append(" AND " + ObjStoreTable.RB_ID + "=").append(rbid);
		}
		if (ts != null) {
			sb.append(" AND " + ObjStoreTable.RB_DATE + "<").append(rdb.toTimeStampExpression(ts));
		}
		sb.append(" AND " + ObjStoreTable.PG_NO + "=0");
		sb.append(" AND " + ObjStoreTable.OBJ_VER
				+ "=(SELECT "
				+ "MAX(" + ObjStoreTable.OBJ_VER + ")"
				+ " FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE_RB());
		sb.append(" WHERE "
				+ ObjStoreTable.TENANT_ID + "=A." + ObjStoreTable.TENANT_ID
				+ " AND " + ObjStoreTable.OBJ_DEF_ID + "=A." + ObjStoreTable.OBJ_DEF_ID
				+ " AND " + ObjStoreTable.RB_ID + "=A." + ObjStoreTable.RB_ID
				+ " AND " + ObjStoreTable.OBJ_ID + "=A." + ObjStoreTable.OBJ_ID
				+ ")");
		return sb.toString();
	}

	public String count(String sql) {
		return "SELECT COUNT(*) FROM (" + sql + ") CT";
	}

	public String deleteDataRB(int tenantId, String defId, String tableNamePostfix, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(MetaGRdbEntityStore.makeObjRbTableName(tableNamePostfix));
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdb.sanitize(defId)).append("'");
		return sb.toString();

	}

	public String deleteRefRB(int tenantId, String defId, String tableNamePostfix, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(MetaGRdbEntityStore.makeObjRefRbTableName(tableNamePostfix));
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(defId)).append("'");
		return sb.toString();
	}

	public String deleteRefRBByTargetDefId(int tenantId, String targetDefId, String tableNamePostfix, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(MetaGRdbEntityStore.makeObjRefRbTableName(tableNamePostfix));
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.TARGET_OBJ_DEF_ID + "='").append(rdb.sanitize(targetDefId)).append("'");
		return sb.toString();
	}

//	public String toDataUpdateRbSqlForDefrag(int tenantId, EntityHandler eh, List<String> unUseColNames, RdbAdapter rdbAdaptor) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("UPDATE ");
//		sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_DATA_RB());
//
//		sb.append(" SET ");
//		for (String colName : unUseColNames) {
//			sb.append(colName + " = null,");
//		}
//		sb.deleteCharAt(sb.length() - 1);
//
//		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId()) + "'");
//
//		//バージョンは指定しない
//
//		return sb.toString();
//	}

	public String deleteForDefragRB(int tenantId, EntityHandler eh, List<String> refPropertyIds, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF_RB());
		sb.append(" WHERE " + ObjRefTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");

		//利用されているReferencePropertyを除外する
		if (!refPropertyIds.isEmpty()) {
			sb.append(" AND " + ObjRefTable.REF_DEF_ID + " NOT IN (");
			boolean isFirst = true;
			for (String refProperty : refPropertyIds) {
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
