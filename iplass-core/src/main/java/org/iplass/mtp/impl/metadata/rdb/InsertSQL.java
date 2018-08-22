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

import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


@SuppressWarnings("deprecation")
public class InsertSQL extends UpdateSqlHandler {

	private static final String CREATE_SQL;
	private static final String CREATE_SQL_USE_DEF_NAME;

	static {
		CREATE_SQL = "INSERT INTO " + ObjMetaTable.TABLE_NAME
				+ " (" + ObjMetaTable.TENANT_ID
				+ "," + ObjMetaTable.OBJ_DEF_ID
				+ "," + ObjMetaTable.OBJ_DEF_VER
				+ "," + ObjMetaTable.OBJ_DEF_PATH
				+ "," + ObjMetaTable.OBJ_DEF_DISP_NAME
				+ "," + ObjMetaTable.OBJ_DESC
				+ "," + ObjMetaTable.OBJ_META_DATA
				+ "," + ObjMetaTable.STATUS
				+ "," + ObjMetaTable.CRE_USER
				+ "," + ObjMetaTable.CRE_DATE
				+ "," + ObjMetaTable.UP_USER
				+ "," + ObjMetaTable.UP_DATE
				+ "," + ObjMetaTable.SHARABLE
				+ "," + ObjMetaTable.OVERWRITABLE
				+ ")VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, systimestamp, ?, systimestamp, ?, ?)";

		CREATE_SQL_USE_DEF_NAME = "INSERT INTO " + ObjMetaTable.TABLE_NAME
				+ " (" + ObjMetaTable.TENANT_ID
				+ "," + ObjMetaTable.OBJ_DEF_ID
				+ "," + ObjMetaTable.OBJ_DEF_VER
				+ "," + ObjMetaTable.OBJ_DEF_PATH
				+ "," + ObjMetaTable.OBJ_DEF_DISP_NAME
				+ "," + ObjMetaTable.OBJ_DESC
				+ "," + ObjMetaTable.OBJ_META_DATA
				+ "," + ObjMetaTable.STATUS
				+ "," + ObjMetaTable.CRE_USER
				+ "," + ObjMetaTable.CRE_DATE
				+ "," + ObjMetaTable.UP_USER
				+ "," + ObjMetaTable.UP_DATE
				+ "," + ObjMetaTable.SHARABLE
				+ "," + ObjMetaTable.OVERWRITABLE
				+ "," + ObjMetaTable.OBJ_DEF_NAME
				+ "," + ObjMetaTable.OBJ_DEF_TYPE
				+ ")VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, systimestamp, ?, systimestamp, ?, ?, ?, ?)";
	}

	public String createStoreSQL(RdbAdapter rdb, boolean useObjDefNameAndType) {
		if (useObjDefNameAndType) {
			return CREATE_SQL_USE_DEF_NAME.replaceAll("systimestamp", rdb.systimestamp());
		} else {
			return CREATE_SQL.replaceAll("systimestamp", rdb.systimestamp());
		}
	}

	public void setStoreParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, int version, RootMetaData metaData, String path, Blob blobMeta, boolean share, boolean dataShare, boolean permissionShare, boolean overwrite, boolean useObjDefNameAndType)
			throws SQLException {

		int num = 1;
		ps.setInt(num++, tenantId);
		ps.setString(num++, metaData.getId());
		ps.setInt(num++, version);
		ps.setString(num++, path);
		ps.setString(num++, metaData.getDisplayName());
		ps.setString(num++, metaData.getDescription());
		ps.setBlob(num++, blobMeta);
		ps.setString(num++, "V");
		String clientId = ExecuteContext.getCurrentContext().getClientId();
		ps.setString(num++, clientId);
		ps.setString(num++, clientId);
		ps.setString(num++, UpdateConfigSQL.toCharShare(share, dataShare, permissionShare));
		ps.setString(num++, UpdateConfigSQL.toCharOverwrite(overwrite));

		if (useObjDefNameAndType) {
			//obj_def_name、obj_def_typeが Not Nullのため、利用しないが登録
			ps.setString(num++, " ");
			ps.setString(num++, " ");
		}
	}

	public void setStoreParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, int version, RootMetaData metaData, String path, byte[] blobMeta, boolean share, boolean dataShare, boolean permissionShare, boolean overwrite, boolean useObjDefNameAndType)
			throws SQLException {

		int num = 1;
		ps.setInt(num++, tenantId);
		ps.setString(num++, metaData.getId());
		ps.setInt(num++, version);
		ps.setString(num++, path);
		ps.setString(num++, metaData.getDisplayName());
		ps.setString(num++, metaData.getDescription());
		ps.setBytes(num++, blobMeta);
		ps.setString(num++, "V");
		String clientId = ExecuteContext.getCurrentContext().getClientId();
		ps.setString(num++, clientId);
		ps.setString(num++, clientId);
		ps.setString(num++, UpdateConfigSQL.toCharShare(share, dataShare, permissionShare));
		ps.setString(num++, UpdateConfigSQL.toCharOverwrite(overwrite));

		if (useObjDefNameAndType) {
			//obj_def_name、obj_def_typeが Not Nullのため、利用しないが登録
			ps.setString(num++, " ");
			ps.setString(num++, " ");
		}
	}

	public void setStoreParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, int version, RootMetaData metaData, String path, InputStream blobMeta, boolean share, boolean dataShare, boolean permissionShare, boolean overwrite, boolean useObjDefNameAndType)
			throws SQLException {

		int num = 1;
		ps.setInt(num++, tenantId);
		ps.setString(num++, metaData.getId());
		ps.setInt(num++, version);
		ps.setString(num++, path);
		ps.setString(num++, metaData.getDisplayName());
		ps.setString(num++, metaData.getDescription());
		ps.setBinaryStream(num++, blobMeta);
		ps.setString(num++, "V");
		String clientId = ExecuteContext.getCurrentContext().getClientId();
		ps.setString(num++, clientId);
		ps.setString(num++, clientId);
		ps.setString(num++, UpdateConfigSQL.toCharShare(share, dataShare, permissionShare));
		ps.setString(num++, UpdateConfigSQL.toCharOverwrite(overwrite));

		if (useObjDefNameAndType) {
			//obj_def_name、obj_def_typeが Not Nullのため、利用しないが登録
			ps.setString(num++, " ");
			ps.setString(num++, " ");
		}
	}
}
