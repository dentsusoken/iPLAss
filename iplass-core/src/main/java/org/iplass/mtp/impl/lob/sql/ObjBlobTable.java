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

public interface ObjBlobTable {
	
	public static final String TABLE_NAME = "OBJ_BLOB";
	public static final String TABLE_NAME_RB = "OBJ_BLOB_RB";
	
	public static final String TENANT_ID = "TENANT_ID";
	public static final String LOB_ID = "LOB_ID";
	public static final String LOB_NAME = "LOB_NAME";
	public static final String LOB_TYPE = "LOB_TYPE";
	public static final String LOB_STAT = "LOB_STAT";
	public static final String LOB_DATA_ID ="LOB_DATA_ID";
	public static final String CRE_DATE = "CRE_DATE"; 
	public static final String UP_DATE = "UP_DATE";
	public static final String CRE_USER = "CRE_USER";
	public static final String UP_USER = "UP_USER";
	public static final String SESS_ID = "SESS_ID";
	public static final String OBJ_DEF_ID = "OBJ_DEF_ID";
	public static final String PROP_DEF_ID = "PROP_DEF_ID";
	public static final String OBJ_ID = "OBJ_ID";
	public static final String OBJ_VER = "OBJ_VER";
	
	//ごみ箱のみ項目
	public static final String RB_ID = "RB_ID";

	public static final String[] COLS = {
		TENANT_ID,LOB_ID,LOB_NAME,LOB_TYPE,LOB_STAT,LOB_DATA_ID,CRE_DATE,UP_DATE,CRE_USER,UP_USER,SESS_ID,OBJ_DEF_ID,PROP_DEF_ID,OBJ_ID,OBJ_VER
	};
	
	public static final String[] COLS_RB = {
		TENANT_ID,RB_ID,LOB_ID
	};
	
//	public static final String[] LOB_ID_COL = {"LOB_ID"};
	
	public static final String SEQ_LOB_ID = "SEQ_LOB_ID";
	
}
