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

package org.iplass.mtp.impl.lob.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.iplass.mtp.impl.lob.Lob;
import org.iplass.mtp.impl.lob.LobDao;
import org.iplass.mtp.impl.lob.lobstore.LobStore;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class BlobSearchSql extends QuerySqlHandler {

	public String toSql(RdbAdapter rdb, int tenantId, long lobId,
			String sessionId, String defId, String propId, String oid,
			Long version, boolean withLock) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ ObjBlobTable.TENANT_ID
				+ "," + ObjBlobTable.LOB_ID
				+ "," + ObjBlobTable.LOB_NAME
				+ "," + ObjBlobTable.LOB_TYPE
				+ "," + ObjBlobTable.LOB_STAT
				+ "," + ObjBlobTable.LOB_DATA_ID
				+ "," + ObjBlobTable.SESS_ID
				+ "," + ObjBlobTable.OBJ_DEF_ID
				+ "," + ObjBlobTable.PROP_DEF_ID
				+ "," + ObjBlobTable.OBJ_ID
				+ "," + ObjBlobTable.OBJ_VER
				+ " FROM "
				+ ObjBlobTable.TABLE_NAME
				+ " WHERE "
				+ ObjBlobTable.TENANT_ID
				+ "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjBlobTable.LOB_ID + "=");
		sb.append(lobId);
		if (sessionId != null) {
			sb.append(" AND " + ObjBlobTable.SESS_ID + "=");
			sb.append("'").append(rdb.sanitize(sessionId)).append("'");
		}
		if (defId != null) {
			sb.append(" AND " + ObjBlobTable.OBJ_DEF_ID + "=");
			sb.append("'").append(rdb.sanitize(defId)).append("'");
		}
		if (propId != null) {
			sb.append(" AND " + ObjBlobTable.PROP_DEF_ID + "=");
			sb.append("'").append(rdb.sanitize(propId)).append("'");
		}
		if (oid != null) {
			sb.append(" AND " + ObjBlobTable.OBJ_ID + "=");
			sb.append("'").append(rdb.sanitize(oid)).append("'");
		}
		if (version != null) {
			sb.append(" AND " + ObjBlobTable.OBJ_VER + "=");
			sb.append(version);
		}

		return withLock ? rdb.createRowLockSql(sb.toString()) : sb.toString();
	}

	public String toSearchSql(RdbAdapter rdb, int tenantId, long[] lobId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ ObjBlobTable.TENANT_ID
				+ "," + ObjBlobTable.LOB_ID
				+ "," + ObjBlobTable.LOB_NAME
				+ "," + ObjBlobTable.LOB_TYPE
				+ "," + ObjBlobTable.LOB_STAT
				+ "," + ObjBlobTable.LOB_DATA_ID
				+ "," + ObjBlobTable.SESS_ID
				+ "," + ObjBlobTable.OBJ_DEF_ID
				+ "," + ObjBlobTable.PROP_DEF_ID
				+ "," + ObjBlobTable.OBJ_ID
				+ "," + ObjBlobTable.OBJ_VER
				+ " FROM " + ObjBlobTable.TABLE_NAME
				+ " WHERE " + ObjBlobTable.TENANT_ID + "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjBlobTable.LOB_ID + " IN (");
		for (int i = 0; i < lobId.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(lobId[i]);
		}
		sb.append(")");
		return sb.toString();
	}

	public String toSqlByDefId(RdbAdapter rdb, int tenantId, String defId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ ObjBlobTable.TENANT_ID
				+ "," + ObjBlobTable.LOB_ID
//				+ "," + ObjBlobTable.LOB_NAME
//				+ "," + ObjBlobTable.LOB_TYPE
//				+ "," + ObjBlobTable.LOB_STAT
				+ "," + ObjBlobTable.LOB_DATA_ID
//				+ "," + ObjBlobTable.SESS_ID
				+ "," + ObjBlobTable.OBJ_DEF_ID
//				+ "," + ObjBlobTable.PROP_DEF_ID
//				+ "," + ObjBlobTable.OBJ_ID
//				+ "," + ObjBlobTable.OBJ_VER
				+ " FROM "
				+ ObjBlobTable.TABLE_NAME
				+ " WHERE "
				+ ObjBlobTable.TENANT_ID
				+ "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjBlobTable.OBJ_DEF_ID + "=");
		sb.append("'").append(rdb.sanitize(defId)).append("'");

		return sb.toString();
	}

	public String toSqlForCleanTemporary(RdbAdapter rdb, int day, int tenantId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ ObjBlobTable.TENANT_ID
				+ "," + ObjBlobTable.LOB_ID
				+ "," + ObjBlobTable.LOB_NAME
				+ "," + ObjBlobTable.LOB_TYPE
				+ "," + ObjBlobTable.LOB_STAT
				+ "," + ObjBlobTable.LOB_DATA_ID
				+ "," + ObjBlobTable.SESS_ID
				+ "," + ObjBlobTable.OBJ_DEF_ID
				+ "," + ObjBlobTable.PROP_DEF_ID
				+ "," + ObjBlobTable.OBJ_ID
				+ "," + ObjBlobTable.OBJ_VER
				+ " FROM "
				+ ObjBlobTable.TABLE_NAME);
		sb.append(" WHERE " + ObjBlobTable.LOB_STAT + "='" + Lob.STATE_TEMP + "'");
		sb.append(" AND " + ObjBlobTable.TENANT_ID + "=");
		sb.append(tenantId);
		if (day < 0) {
			sb.append(" AND " + ObjBlobTable.UP_DATE + "<").append(rdb.addDate(rdb.systimestamp(), day));
		}
		return sb.toString();
	}

	public Lob toBinaryData(ResultSet rs, LobStore lobStore, LobDao dao, boolean manageLobSizeOnRdb) throws SQLException {
		return new Lob(
				rs.getInt(ObjBlobTable.TENANT_ID),
				rs.getLong(ObjBlobTable.LOB_ID),
				rs.getString(ObjBlobTable.LOB_NAME),
				rs.getString(ObjBlobTable.LOB_TYPE),
				rs.getString(ObjBlobTable.OBJ_DEF_ID),
				rs.getString(ObjBlobTable.PROP_DEF_ID),
				rs.getString(ObjBlobTable.OBJ_ID),
				rs.getLong(ObjBlobTable.OBJ_VER),
				rs.getString(ObjBlobTable.SESS_ID),
				rs.getString(ObjBlobTable.LOB_STAT),
				rs.getLong(ObjBlobTable.LOB_DATA_ID),
				lobStore,
				dao,
				manageLobSizeOnRdb);
	}

	/**
	 * Entity定義として参照していないLobデータ情報を取得するためのSQL定義
	 *
	 * @param rdb
	 * @param tenantId
	 * @param defId
	 * @param validPropertyIds
	 * @return
	 */
	public String toSqlForDefrag(RdbAdapter rdb, int tenantId, String defId, List<String> validPropertyIds) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ ObjBlobTable.TENANT_ID
				+ "," + ObjBlobTable.LOB_ID
				+ "," + ObjBlobTable.LOB_DATA_ID
				+ "," + ObjBlobTable.OBJ_DEF_ID
				+ "," + ObjBlobTable.PROP_DEF_ID
				+ " FROM "
				+ ObjBlobTable.TABLE_NAME
				+ " WHERE "
				+ ObjBlobTable.TENANT_ID
				+ "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjBlobTable.OBJ_DEF_ID + "=");
		sb.append("'").append(rdb.sanitize(defId)).append("'");

		//利用されているPropertyを除外する
		if (!validPropertyIds.isEmpty()) {
			StringBuilder notin = new StringBuilder();
			for (String propertyId : validPropertyIds) {
				notin.append("'" + rdb.sanitize(propertyId) + "',");
			}
			notin.deleteCharAt(notin.length() - 1);
			sb.append(" AND " + ObjBlobTable.PROP_DEF_ID + " NOT IN (").append(notin.toString()).append(")");
		}

		return sb.toString();
	}

	/**
	 * テナントの全Lobデータ情報を取得するためのSQL定義
	 * @param rdb
	 * @param tenantId
	 * @return
	 */
	public String toSqlForMigrate(RdbAdapter rdb, int tenantId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ ObjBlobTable.TENANT_ID
				+ "," + ObjBlobTable.LOB_ID
				+ "," + ObjBlobTable.LOB_NAME
				+ "," + ObjBlobTable.LOB_TYPE
				+ "," + ObjBlobTable.LOB_STAT
				+ "," + ObjBlobTable.LOB_DATA_ID
				+ "," + ObjBlobTable.SESS_ID
				+ "," + ObjBlobTable.OBJ_DEF_ID
				+ "," + ObjBlobTable.PROP_DEF_ID
				+ "," + ObjBlobTable.OBJ_ID
				+ "," + ObjBlobTable.OBJ_VER
				+ " FROM " + ObjBlobTable.TABLE_NAME
				+ " WHERE " + ObjBlobTable.TENANT_ID + "=");
		sb.append(tenantId);
		return sb.toString();
	}

}
