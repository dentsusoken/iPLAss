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

package org.iplass.mtp.impl.metadata.rdb;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataEntryThinWrapper;
import org.iplass.mtp.impl.metadata.MetaDataRepositoryKind;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SelectSQL extends QuerySqlHandler {
	private static Logger logger = LoggerFactory.getLogger(SelectSQL.class);

	private static final String META_DATA_ENTRY_INFO_SELECT =
			"SELECT " + ObjMetaTable.OBJ_DEF_ID
			+ " ," + ObjMetaTable.OBJ_DEF_PATH
			+ " ," + ObjMetaTable.OBJ_DEF_DISP_NAME
			+ " ," + ObjMetaTable.OBJ_DESC
			+ " ," + ObjMetaTable.SHARABLE
			+ " ," + ObjMetaTable.OVERWRITABLE
			+ " ," + ObjMetaTable.OBJ_DEF_VER
			+ " ," + ObjMetaTable.STATUS
			+ " ," + ObjMetaTable.CRE_DATE
			+ " ," + ObjMetaTable.UP_DATE
			+ " ," + ObjMetaTable.CRE_USER
			+ " ," + ObjMetaTable.UP_USER
			+ " FROM " + ObjMetaTable.TABLE_NAME;

	private static final String META_DATA_LOAD_SELECT =
			"SELECT " + ObjMetaTable.OBJ_DEF_VER
			+ " ," + ObjMetaTable.OBJ_DEF_PATH
			+ " ," + ObjMetaTable.OBJ_META_DATA
			+ " ," + ObjMetaTable.SHARABLE
			+ " ," + ObjMetaTable.OVERWRITABLE
			+ " ," + ObjMetaTable.OBJ_DEF_VER
			+ " FROM " + ObjMetaTable.TABLE_NAME;

	public String createGetMetadataInfoSQL(RdbAdapter rdb, boolean onlyValid) {
		if (onlyValid) {
			return META_DATA_ENTRY_INFO_SELECT
					+ " WHERE " + ObjMetaTable.TENANT_ID + "=? "
					+ " AND " + ObjMetaTable.OBJ_DEF_ID + "=? "
					+ " AND " + ObjMetaTable.STATUS + "='V' "
					+ " ORDER BY " + ObjMetaTable.OBJ_DEF_VER + " DESC";
		} else {
			return META_DATA_ENTRY_INFO_SELECT
					+ " WHERE " + ObjMetaTable.TENANT_ID + "=? "
					+ " AND " + ObjMetaTable.OBJ_DEF_ID + "=? "
					+ " ORDER BY " + ObjMetaTable.OBJ_DEF_VER + " DESC ";
		}
	}

	public void setGetMetadataInfoParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String metaDataId) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, metaDataId);
	}

	public String createNodeListSQL(RdbAdapter rdb, String prefixPath, boolean withInvalid) {
		
		StringBuilder sb = new StringBuilder();
		
		if (withInvalid) {
			sb.append(META_DATA_ENTRY_INFO_SELECT
					+ " a"
					+ " WHERE " + ObjMetaTable.TENANT_ID + "=? "
					+ " AND " + ObjMetaTable.OBJ_DEF_VER + "=(SELECT MAX(" + ObjMetaTable.OBJ_DEF_VER + ") FROM " + ObjMetaTable.TABLE_NAME
					+ " WHERE " + ObjMetaTable.TENANT_ID + "=a." + ObjMetaTable.TENANT_ID
					+ " AND " + ObjMetaTable.OBJ_DEF_ID + "=a." + ObjMetaTable.OBJ_DEF_ID + ")");
		} else {
			sb.append(META_DATA_ENTRY_INFO_SELECT
					+ " WHERE " + ObjMetaTable.TENANT_ID + "=? "
					+ " AND " + ObjMetaTable.STATUS + "='V'");
		}

		if (prefixPath != null) {
			sb.append(" AND " + ObjMetaTable.OBJ_DEF_PATH + " LIKE ? ").append(rdb.escape());
		}
		return sb.toString();
	}

	public void setNodeListParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String prefixPath) throws SQLException {
		int num = 1;
		ps.setInt(num++, tenantId);
		if (prefixPath != null) {
			ps.setString(num++, rdb.sanitizeForLike(prefixPath) + "%");
		}
	}

	public List<MetaDataEntryInfo> createNodeListResultData(ResultSet rs, RdbAdapter rdb) throws SQLException {
		List<MetaDataEntryInfo> result = null;
		while(rs.next()) {
			if(result == null) {
				result = new ArrayList<MetaDataEntryInfo>();
			}
			result.add(createMetaDataEntryInfo(rs, rdb));
		}
		return result == null ? Collections.<MetaDataEntryInfo>emptyList() : result;
	}

	public MetaDataEntryInfo createMetaDataEntryInfo(ResultSet rs, RdbAdapter rdb) throws SQLException {
		MetaDataEntryInfo node = new MetaDataEntryInfo();
		int num = 1;
		node.setId(rs.getString(num++));
		node.setPath(rs.getString(num++));
		node.setDisplayName(rs.getString(num++));
		node.setDescription(rs.getString(num++));
		node.setRepository(MetaDataRepositoryKind.RDB.getDisplayName());
		boolean[] booleanShare = UpdateConfigSQL.toBooleanShare(rs.getString(num++));
		node.setSharable(booleanShare[0]);
		node.setDataSharable(booleanShare[1]);
		node.setPermissionSharable(booleanShare[2]);
		node.setOverwritable(UpdateConfigSQL.toBooleanOverwrite(rs.getString(num++)));
		node.setVersion(rs.getInt(num++));
		String stateStr = rs.getString(num++);
		if ("V".equals(stateStr)) {
			node.setState(State.VALID);
		} else {
			node.setState(State.INVALID);
		}
		node.setCreateDate(rs.getTimestamp(num++, rdb.rdbCalendar()));
		node.setUpdateDate(rs.getTimestamp(num++, rdb.rdbCalendar()));
		node.setCreateUser(rs.getString(num++));
		node.setUpdateUser(rs.getString(num++));

		return node;
	}

	public String createLoadSQL(int version) {
		StringBuilder sb = new StringBuilder();
		sb.append(META_DATA_LOAD_SELECT
			+ " WHERE " + ObjMetaTable.TENANT_ID + " = ? "
			+ " AND " + ObjMetaTable.OBJ_DEF_PATH + " = ? "
		);
		if (version < 0) {
			sb.append(" AND " + ObjMetaTable.STATUS + " = 'V' ");
		} else {
			sb.append(" AND " + ObjMetaTable.OBJ_DEF_VER + " = ? ");
		}
		sb.append(" ORDER BY " + ObjMetaTable.UP_DATE + " DESC");
		return sb.toString();
	}

	public void setLoadParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String path, int version) throws SQLException {
		int num = 1;
		ps.setInt(num++, tenantId);
		ps.setString(num++, path);
		if(version >= 0) {
			ps.setInt(num++, version);
		}
	}

	public MetaDataEntry createLoadResultData(RdbAdapter rdb, ResultSet rs, JAXBContext context) throws SQLException, JAXBException {
		if(rs.next()) {
			Blob xmlData = null;
			InputStream is = null;
			try {
				if (rdb.isSupportBlobType()) {
					xmlData = rs.getBlob(3);
				}
				MetaDataEntry instance = new MetaDataEntry();
				Unmarshaller unmarshaller = context.createUnmarshaller();
				if (rdb.isSupportBlobType()) {
					is = xmlData.getBinaryStream();
				} else {
					is = rs.getBinaryStream(3);
				}
				MetaDataEntryThinWrapper meta = (MetaDataEntryThinWrapper) unmarshaller.unmarshal(is);

				instance.setMetaData(meta.getMetaData());
				instance.setVersion(rs.getInt(1));
				instance.setState(State.VALID);//TODO 暫定実装
				instance.setPath(rs.getString(2));
				boolean[] booleanShare = UpdateConfigSQL.toBooleanShare(rs.getString(4));
				instance.setSharable(booleanShare[0]);
				instance.setDataSharable(booleanShare[1]);
				instance.setPermissionSharable(booleanShare[2]);
				instance.setOverwritable(UpdateConfigSQL.toBooleanOverwrite(rs.getString(5)));
				instance.setVersion(rs.getInt(6));

				return instance;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logger.warn("can not close inputstream resource:" + is, e);
					}
				}
				if (xmlData != null) {
					xmlData.free();
				}
			}
		}
		return null;
	}

	public String createLoadByIdSQL(int version) {
		StringBuilder sb = new StringBuilder();
		sb.append(META_DATA_LOAD_SELECT
				+ " WHERE " + ObjMetaTable.TENANT_ID + " = ? "
				+ " AND " + ObjMetaTable.OBJ_DEF_ID + " = ? ");
		if(version < 0) {
			sb.append(" AND " + ObjMetaTable.STATUS + " = 'V' ");
		} else {
			sb.append(" AND " + ObjMetaTable.OBJ_DEF_VER + " = ? ");
		}
		return sb.toString();
	}

	public void setLoadByIdParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String id, int version) throws SQLException {
		int num = 1;
		ps.setInt(num++, tenantId);
		ps.setString(num++, id);
		if(version >= 0) {
			ps.setInt(num++, version);
		}
	}

	public String createMaxVersionSQL() {
		return "SELECT MAX(" + ObjMetaTable.OBJ_DEF_VER + ") "
			+ " FROM " + ObjMetaTable.TABLE_NAME
			+ " WHERE " + ObjMetaTable.TENANT_ID + " = ? "
			+ " AND " + ObjMetaTable.OBJ_DEF_ID + " = ? ";
	}

	public void setMaxVersionParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, RootMetaData metaData) throws SQLException {
		int num = 1;
		ps.setInt(num++, tenantId);
		ps.setString(num++, metaData.getId());
	}

	public int getMaxVersionResultData(ResultSet rs) throws SQLException {
		if(rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}

	public String createDataCountSQL(boolean validOnly) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(" + ObjMetaTable.OBJ_DEF_ID + ") "
			+ " FROM " + ObjMetaTable.TABLE_NAME
			+ " WHERE " + ObjMetaTable.TENANT_ID + " = ? "
			+ " AND " + ObjMetaTable.OBJ_DEF_ID + " = ? ");
		if (validOnly) {
			sb.append(" AND " + ObjMetaTable.STATUS + " = 'V' ");
		}
		return sb.toString();
	}

	public void setDataCountParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, RootMetaData metaData) throws SQLException {
		int num = 1;
		ps.setInt(num++, tenantId);
		ps.setString(num++, metaData.getId());
	}

	public int getDataCountResultData(ResultSet rs) throws SQLException {
		if(rs.next()) {
			return rs.getInt(1);
		}
		return 0;
	}

	public String createTenantIdListSQL() {
		return "SELECT " + ObjMetaTable.TENANT_ID
				+ " FROM " + ObjMetaTable.TABLE_NAME
				+ " WHERE " + ObjMetaTable.OBJ_DEF_ID + " = ? "
				+ " AND " + ObjMetaTable.STATUS + " = 'V'";
	}

	public void setTenantIdListParameter(RdbAdapter rdb, PreparedStatement ps, String metaDataId) throws SQLException {
		int num = 1;
		ps.setString(num++, metaDataId);
	}

	public List<Integer> getTenantIdListResultData(ResultSet rs) throws SQLException {
		List<Integer> list = new ArrayList<Integer>();
		while (rs.next()) {
			list.add(rs.getInt(1));
		}
		return list;
	}

	public String createMetaHistorySQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(META_DATA_ENTRY_INFO_SELECT
				+ " WHERE " + ObjMetaTable.TENANT_ID + " = ? "
				+ " AND " + ObjMetaTable.OBJ_DEF_ID + " = ? "
		 		+ " ORDER BY " + ObjMetaTable.OBJ_DEF_VER + " DESC ");
		return sb.toString();
	}

	public void setMetaHistoryParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String path) throws SQLException {
		int num = 1;
		ps.setInt(num++, tenantId);
		ps.setString(num++, path);
	}

}
