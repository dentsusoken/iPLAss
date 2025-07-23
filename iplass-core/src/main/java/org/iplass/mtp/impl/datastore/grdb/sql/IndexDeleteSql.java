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

import java.util.List;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjIndexTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class IndexDeleteSql extends UpdateSqlHandler {

	private static final String TMP_TABLE_ALIAS = "tt";

//	public String toSql(int tenantId, EntityHandler eh, String colName, BaseRdbTypeAdapter typeAdapter, IndexType type, RdbAdapter rdb) {
//		return deleteByColName(tenantId, eh.getMetaData().getId(), ((MetaGRdbEntityStore) eh.getEntityStoreRuntime().getMetaData()).getTableNamePostfix(), colName, typeAdapter, type, rdb);
//	}

	public String deleteByColName(int tenantId, String defId, String tableNamePostfix, int pageNo, String colName, BaseRdbTypeAdapter typeAdapter, IndexType type, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(MetaGRdbEntityStore.makeIndexTableName(typeAdapter.getColOfIndex(), tableNamePostfix));
		} else {
			sb.append(MetaGRdbEntityStore.makeUniqueIndexTableName(typeAdapter.getColOfIndex(), tableNamePostfix));
		}

		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(defId));
		sb.append("' AND " + ObjIndexTable.COL_NAME + "='").append(rdb.sanitize(MetaGRdbPropertyStore.makeExternalIndexColName(pageNo, colName))).append("'");

		return sb.toString();
	}

	//FIXME INDEXテーブルを索引構成表にして、oidでの索引を追加

	/**
	 * oid,version指定によるテーブル単位での一括削除。
	 */
	public String toSqlDelByOid(int tenantId, EntityHandler eh, String colOfIndex, IndexType type, String oid, Long version, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colOfIndex));
		} else {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colOfIndex));
		}

		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjIndexTable.OBJ_ID + "='").append(rdb.sanitize(oid)).append("'");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(" AND " + ObjIndexTable.OBJ_VER + "=");
			if (version != null) {
				sb.append(version);
			} else {
				sb.append("0");
			}
		}

		return sb.toString();
	}


	/**
	 * oidリスト指定による、テーブル単位の一括削除。
	 */
//	public String toSqlDelByOidList(int tenantId, EntityHandler eh, String colOfIndex, IndexType type, List<String[]> oidList, RdbAdapter rdb) {
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		if (type == IndexType.UNIQUE) {
//			sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colOfIndex));
//		} else {
//			sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colOfIndex));
//		}
//
//		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
//		if (type == IndexType.NON_UNIQUE) {
//			sb.append(" AND (" + ObjIndexTable.OBJ_ID + "," + ObjIndexTable.OBJ_VER + ") IN(");
//			for (int i = 0; i < oidList.size(); i++) {
//				if (i != 0) {
//					sb.append(",");
//				}
//				String[] oid = oidList.get(i);
//				sb.append("('").append(rdb.sanitize(oid[0])).append("',");
//				sb.append(oid[1]);
//				sb.append(")");
//			}
//			sb.append(")");
//		} else {
//			sb.append(" AND " + ObjIndexTable.OBJ_ID + " IN(");
//			for (int i = 0; i < oidList.size(); i++) {
//				if (i != 0) {
//					sb.append(",");
//				}
//				sb.append("'").append(rdb.sanitize(oidList.get(i)[0])).append("'");
//			}
//			sb.append(")");
//		}
//
//		return sb.toString();
//	}

	/**
	 * 一時表結合による一括削除
	 */
	public String deleteByTempTable(int tenantId, EntityHandler eh, String colOfIndex, IndexType type, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colOfIndex));
		} else {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colOfIndex));
		}

		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
		if (type == IndexType.NON_UNIQUE) {
			if (rdb.isSupportRowValueConstructor()) {
				sb.append(" AND (" + ObjIndexTable.OBJ_ID + "," + ObjIndexTable.OBJ_VER + ") IN("
						+ "SELECT " + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + " FROM " + rdb.getTemplaryTablePrefix() + ObjStoreTable.TABLE_NAME_TMP + ")");
			} else {
				String objIndexTable = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colOfIndex);
				sb.append(" AND EXISTS (SELECT 1 FROM ").append(rdb.getTemplaryTablePrefix() + ObjStoreTable.TABLE_NAME_TMP).append(" ").append(TMP_TABLE_ALIAS);
				sb.append(" WHERE ").append(TMP_TABLE_ALIAS).append(".").append(ObjIndexTable.OBJ_ID ).append("=").append(objIndexTable).append(".").append(ObjIndexTable.OBJ_ID);
				sb.append(" AND ").append(TMP_TABLE_ALIAS).append(".").append(ObjIndexTable.OBJ_VER ).append("=").append(objIndexTable).append(".").append(ObjIndexTable.OBJ_VER);
				sb.append(")");
			}
		} else {
			sb.append(" AND " + ObjIndexTable.OBJ_ID + " IN("
					+ "SELECT " + ObjStoreTable.OBJ_ID + " FROM " + rdb.getTemplaryTablePrefix() + ObjStoreTable.TABLE_NAME_TMP + ")");
		}

		return sb.toString();
	}

	/**
	 * 条件指定による、テーブル単位の一括削除。
	 */
//	public String toSqlDelByCond(int tenantId, EntityHandler eh, String colOfIndex, IndexType type, DeleteCondition cond, RdbAdapter rdb, EntityContext context) {
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		if (type == IndexType.NON_UNIQUE) {
//			sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colOfIndex));
//		} else {
//			sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colOfIndex));
//		}
//
//		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
//		if (type == IndexType.NON_UNIQUE) {
//			sb.append(" AND (" + ObjIndexTable.OBJ_ID + "," + ObjIndexTable.OBJ_VER + ") IN(");
//			Query q = new Query();
//			q.selectDistinct(Entity.OID, Entity.VERSION)
//				.from(eh.getMetaData().getName())
//				.setWhere(cond.getWhere());
//			SqlQueryContext qc = new SqlQueryContext(eh, context, rdb);
//			SqlConverter conv = new SqlConverter(qc, false);
//			q.accept(conv);
//
//			sb.append(rdb.tableAlias(qc.toSelectSql()));
//			sb.append(")");
//		} else {
//			sb.append(" AND " + ObjIndexTable.OBJ_ID + " IN(");
//			Query q = new Query();
//			q.select(true, Entity.OID)
//				.from(eh.getMetaData().getName())
//				.setWhere(cond.getWhere());
//			SqlQueryContext qc = new SqlQueryContext(eh, context, rdb);
//			SqlConverter conv = new SqlConverter(qc, false);
//			q.accept(conv);
//
//			sb.append(rdb.tableAlias(qc.toSelectSql()));
//			sb.append(")");
//		}
//
//		return sb.toString();
//	}

	public String deleteByOidAndVersion(int tenantId, GRdbPropertyStoreHandler colDef, String oid, Long version, RdbAdapter rdb) {

		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		String objDefId = eh.getMetaData().getId();
		String colName = colDef.getExternalIndexColName();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex()));
		} else {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex()));
		}

		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(objDefId));
		sb.append("' AND " + ObjIndexTable.COL_NAME + "='").append(rdb.sanitize(colName));
		sb.append("' AND " + ObjIndexTable.OBJ_ID + "='").append(rdb.sanitize(oid)).append("'");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(" AND " + ObjIndexTable.OBJ_VER + "=");
			if (version != null) {
				sb.append(version);
			} else {
				sb.append("0");
			}
		}
		return sb.toString();
	}

//	public String toSqlDelByOidList(int tenantId, RHCPropertyStoreHandler colDef, List<String[]> oidList, RdbAdapter rdb) {
//		EntityHandler eh = colDef.getPropertyRuntime().getParent();
//		String objDefId = eh.getMetaData().getId();
//		String colName = colDef.getMetaData().getColumnNames()[0];
//		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		if (type == IndexType.NON_UNIQUE) {
//			sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex()));
//		} else {
//			sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex()));
//		}
//
//		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(objDefId));
//		sb.append("' AND " + ObjIndexTable.COL_NAME + "='").append(rdb.sanitize(colName)).append("'");
//		if (type == IndexType.NON_UNIQUE) {
//			sb.append(" AND (" + ObjIndexTable.OBJ_ID + "," + ObjIndexTable.OBJ_VER + ") IN(");
//			for (int i = 0; i < oidList.size(); i++) {
//				if (i != 0) {
//					sb.append(",");
//				}
//				String[] oid = oidList.get(i);
//				sb.append("('").append(rdb.sanitize(oid[0])).append("',");
//				sb.append(oid[1]);
//				sb.append(")");
//			}
//			sb.append(")");
//		} else {
//			sb.append(" AND " + ObjIndexTable.OBJ_ID + " IN(");
//			for (int i = 0; i < oidList.size(); i++) {
//				if (i != 0) {
//					sb.append(",");
//				}
//				sb.append("'").append(rdb.sanitize(oidList.get(i)[0])).append("'");
//			}
//			sb.append(")");
//		}
//
//		return sb.toString();
//	}

//	public String deleteAll(int tenantId, EntityHandler eh, String colTypePostFix, IndexType type, RdbAdapter rdb) {
//		String defId = eh.getMetaData().getId();
//		String tableNamePostfix = ((MetaGRdbEntityStore) eh.getEntityStoreRuntime().getMetaData()).getTableNamePostfix();
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("DELETE FROM ");
//		if (type == IndexType.NON_UNIQUE) {
//			sb.append(MetaGRdbEntityStore.makeIndexTableName(colTypePostFix, tableNamePostfix));
//		} else {
//			sb.append(MetaGRdbEntityStore.makeUniqueIndexTableName(colTypePostFix, tableNamePostfix));
//		}
//
//		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(defId)).append("'");
//
//		return sb.toString();
//	}

	public String deleteAll(int tenantId, String defId, String tableNamePostfix, String colTypePostFix, IndexType type, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(MetaGRdbEntityStore.makeIndexTableName(colTypePostFix, tableNamePostfix));
		} else {
			sb.append(MetaGRdbEntityStore.makeUniqueIndexTableName(colTypePostFix, tableNamePostfix));
		}

		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(defId)).append("'");

		return sb.toString();
	}

	public String deleteForDefrag(int tenantId, EntityHandler eh, String colTypePostFix, IndexType type, List<String> usedColNames, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colTypePostFix));
		} else {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colTypePostFix));
		}

		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
		if (usedColNames != null && usedColNames.size() > 0) {
			sb.append(" AND " + ObjIndexTable.COL_NAME + " NOT IN (");
			for (int i = 0; i< usedColNames.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append("'").append(rdb.sanitize(usedColNames.get(i))).append("'");
			}
			sb.append(")");
		}

		return sb.toString();
	}


}
