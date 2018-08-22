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

package org.iplass.mtp.impl.datastore.grdb.sql.table;

public interface ObjStoreTable {
	
	public static final String TABLE_NAME = "OBJ_STORE";
	public static final String TABLE_NAME_RB = "OBJ_STORE_RB";
	public static final String TABLE_NAME_TMP = "OBJ_STORE_TMP";
	
	public static final String TENANT_ID = "TENANT_ID";
	public static final String OBJ_DEF_ID = "OBJ_DEF_ID";
	public static final String PG_NO = "PG_NO";
	public static final String OBJ_ID = "OBJ_ID";
	public static final String OBJ_VER = "OBJ_VER";
	public static final String OBJ_DEF_VER = "OBJ_DEF_VER";
	public static final String STATUS = "STATUS";
//	public static final String DEL_FLG = "DEL_FLG";
	public static final String OBJ_NAME = "OBJ_NAME";
	public static final String OBJ_DESC = "OBJ_DESC";
	public static final String CRE_DATE = "CRE_DATE";
	public static final String UP_DATE = "UP_DATE";
	public static final String S_DATE = "S_DATE";
	public static final String E_DATE = "E_DATE";
	public static final String LOCK_USER = "LOCK_USER";
	public static final String CRE_USER = "CRE_USER";
	public static final String UP_USER = "UP_USER";
	
	
	//ごみ箱のみ項目
	public static final String RB_ID = "RB_ID";
	public static final String RB_DATE = "RB_DATE";
	public static final String RB_USER = "RB_USER";
	
//	public static final String VALUE_PREFIX = "V_";
	
//	public static final int VALUE_COL_COUNT = 256;
//	public static final int VALUE_COL_COUNT = 100;
	
	public static final String VALUE_STR_PREFIX = "STR_";
	public static final String VALUE_NUM_PREFIX = "NUM_";
	public static final String VALUE_TS_PREFIX = "TS_";
	public static final String VALUE_DBL_PREFIX = "DBL_";

	public static final String INDEX_STR_PREFIX = "ISTR_";
	public static final String INDEX_NUM_PREFIX = "INUM_";
	public static final String INDEX_TS_PREFIX = "ITS_";
	public static final String INDEX_DBL_PREFIX = "IDBL_";
	
	public static final String UNIQUE_STR_PREFIX = "USTR_";
	public static final String UNIQUE_NUM_PREFIX = "UNUM_";
	public static final String UNIQUE_TS_PREFIX = "UTS_";
	public static final String UNIQUE_DBL_PREFIX = "UDBL_";
	
	public static final String INDEX_TD_POSTFIX = "_TD";
	
	public static final String INDEX_TD_SEPARATOR = ":";

	//シーケンス名
	public static final String SEQ_OBJ_ID = "SEQ_OBJ_ID";
	public static final String SEQ_RB_ID = "SEQ_RB_ID";
	
}
