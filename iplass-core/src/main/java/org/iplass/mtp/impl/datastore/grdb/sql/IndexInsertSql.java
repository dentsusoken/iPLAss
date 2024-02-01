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

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjIndexTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter.ValueHandleLogic;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class IndexInsertSql extends UpdateSqlHandler {

	private static final String TMP_TABLE_ALIAS = "tt";

	public String searchByOid(int tenantId, final GRdbPropertyStoreHandler colDef, String oid, RdbAdapter rdbAdaptor) {
		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		String objDefId = eh.getMetaData().getId();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT " + ObjIndexTable.VAL + " FROM ");
//		sb.append("SELECT 1 FROM ");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex()));
		} else {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex()));
		}
		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(objDefId)).append("'");
		sb.append(" AND " + ObjIndexTable.OBJ_ID + "='").append(rdbAdaptor.sanitize(oid)).append("'");
		sb.append(" AND " + ObjIndexTable.COL_NAME + "='").append(rdbAdaptor.sanitize(colDef.getExternalIndexColName())).append("'");

		return sb.toString();
	}

	public String insert(int tenantId, final GRdbPropertyStoreHandler colDef, String oid, Long version, final Object value, final RdbAdapter rdbAdaptor) {

		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		String objDefId = eh.getMetaData().getId();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();

		final StringBuilder sb = new StringBuilder();
		String indexTableName;
		if (type == IndexType.NON_UNIQUE) {
			indexTableName = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
		} else {
			indexTableName = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
		}
		appendInsertInto(sb, type, indexTableName);

		sb.append(" VALUES(");
		sb.append(tenantId).append(",'");
		sb.append(rdbAdaptor.sanitize(objDefId)).append("','");
		sb.append(rdbAdaptor.sanitize(colDef.getExternalIndexColName())).append("','");
		sb.append(rdbAdaptor.sanitize(oid)).append("'");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(",").append(version);
		}
		sb.append(",");
		if (value != null) {
			colDef.getSingleColumnRdbTypeAdapter().appendToTypedCol(sb, rdbAdaptor, new ValueHandleLogic() {
				@Override
				public void run() {
					colDef.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(value, sb, rdbAdaptor);
				}
			});
		} else {
			sb.append("null");
		}
		sb.append(")");

		return sb.toString();
	}

	private void appendInsertInto(StringBuilder sb, IndexType type, String indexTableName) {
		sb.append("INSERT INTO ");
		sb.append(indexTableName);
		sb.append("(" + ObjIndexTable.TENANT_ID
				+ "," + ObjIndexTable.OBJ_DEF_ID
				+ "," + ObjIndexTable.COL_NAME
				+ "," + ObjIndexTable.OBJ_ID);
		if (type == IndexType.NON_UNIQUE) {
			sb.append("," + ObjIndexTable.OBJ_VER);
		}
		sb.append("," + ObjIndexTable.VAL).append(")");
	}

	public String insertByTempTable(int tenantId, final GRdbPropertyStoreHandler colDef, final RdbAdapter rdbAdaptor) {
		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		String objDefId = eh.getMetaData().getId();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();

		final StringBuilder sb = new StringBuilder();
		String indexTableName;
		if (type == IndexType.NON_UNIQUE) {
			indexTableName = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
		} else {
			indexTableName = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
		}
		appendInsertInto(sb, type, indexTableName);

		StringBuilder sbCond = new StringBuilder();
		if (rdbAdaptor.isSupportRowValueConstructor()) {
			sbCond.append("(").append(ObjStoreTable.OBJ_ID).append(",").append(ObjStoreTable.OBJ_VER).append(") IN(")
				.append("SELECT ").append(ObjStoreTable.OBJ_ID).append(",").append(ObjStoreTable.OBJ_VER).append(" FROM ").append(rdbAdaptor.getTemplaryTablePrefix()).append(ObjStoreTable.TABLE_NAME_TMP).append(")");
		} else {
			String objStoreTable = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE();
			sbCond.append("EXISTS (SELECT 1 FROM ").append(rdbAdaptor.getTemplaryTablePrefix()).append(ObjStoreTable.TABLE_NAME_TMP).append(" ").append(TMP_TABLE_ALIAS)
				.append(" WHERE ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_ID).append("=").append(objStoreTable).append(".").append(ObjStoreTable.OBJ_ID)
				.append(" AND ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_VER).append("=").append(objStoreTable).append(".").append(ObjStoreTable.OBJ_VER);
		}
		appendSelect(sb, tenantId, objDefId, ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE(),
				type, colDef.getExternalIndexColName(), colDef.getMetaData().getPageNo(), colDef.getMetaData().getColumnName(), sbCond.toString(), rdbAdaptor);

		return sb.toString();
	}

	private void appendSelect(StringBuilder sb, int tenantId, String objDefId, String objStoreTableName, IndexType type, String extIndexColName, int pageNo, String colName, CharSequence cond, RdbAdapter rdbAdaptor) {
		sb.append(" SELECT ");
		sb.append(tenantId).append(",'");
		sb.append(rdbAdaptor.sanitize(objDefId)).append("','");
		sb.append(rdbAdaptor.sanitize(extIndexColName)).append("',");
		sb.append(ObjStoreTable.OBJ_ID + ",");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(ObjStoreTable.OBJ_VER + ",");
			sb.append(colName);
		} else {
			sb.append("MAX(").append(colName).append(")");
		}
		sb.append(" FROM ");
		sb.append(objStoreTableName);
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(objDefId)).append("'");
		if (cond != null) {
			sb.append(" AND ").append(cond);
		}
		sb.append(" AND " + ObjStoreTable.PG_NO + "=").append(pageNo);
		if (type == IndexType.UNIQUE_WITHOUT_NULL || type == IndexType.NON_UNIQUE) {
			sb.append(" AND ").append(colName).append(" IS NOT NULL");
		}

		if (type != IndexType.NON_UNIQUE) {
			sb.append(" GROUP BY " + ObjStoreTable.TENANT_ID
				+ "," + ObjStoreTable.OBJ_DEF_ID
				+ "," + ObjIndexTable.OBJ_ID);
		}
	}

	public String insertAll(int tenantId, String defId, String tablePostfix, final int pageNo, final String colName, IndexType type, final BaseRdbTypeAdapter colAdapter, final RdbAdapter rdbAdaptor) {

		String objTableName = MetaGRdbEntityStore.makeObjStoreName(tablePostfix);
		final StringBuilder sb = new StringBuilder();
		String indexTableName;
		if (type == IndexType.NON_UNIQUE) {
			indexTableName = MetaGRdbEntityStore.makeIndexTableName(colAdapter.getColOfIndex(), tablePostfix);
		} else {
			indexTableName = MetaGRdbEntityStore.makeUniqueIndexTableName(colAdapter.getColOfIndex(), tablePostfix);
		}
		String extIndexColName = MetaGRdbPropertyStore.makeExternalIndexColName(pageNo, colName);

		appendInsertInto(sb, type, indexTableName);
		appendSelect(sb, tenantId, defId, objTableName, type, extIndexColName, pageNo, colName, null, rdbAdaptor);

		return sb.toString();
	}

	public String insertByOid(int tenantId, final GRdbPropertyStoreHandler colDef, String oid, final RdbAdapter rdbAdaptor) {
		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		String objDefId = eh.getMetaData().getId();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();

		final StringBuilder sb = new StringBuilder();
		String indexTableName;
		if (type == IndexType.NON_UNIQUE) {
			indexTableName = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
		} else {
			indexTableName = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
		}
		appendInsertInto(sb, type, indexTableName);

		appendSelect(sb, tenantId, objDefId, ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE(),
				type, colDef.getExternalIndexColName(), colDef.getMetaData().getPageNo(), colDef.getMetaData().getColumnName(),
				ObjIndexTable.OBJ_ID + "='" + rdbAdaptor.sanitize(oid) + "'",
				rdbAdaptor);

		return sb.toString();
	}
}
