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

package org.iplass.mtp.impl.datastore.grdb.sql.table;

public interface ObjIndexTable {
	
	public static final String TABLE_INDEX_PREFIX_NAME = "OBJ_INDEX_";
	public static final String TABLE_UNIQUE_PREFIX_NAME = "OBJ_UNIQUE_";

	public static final String TENANT_ID = "TENANT_ID";
	public static final String OBJ_DEF_ID = "OBJ_DEF_ID";
	public static final String COL_NAME = "COL_NAME";
	public static final String OBJ_ID = "OBJ_ID";
	public static final String OBJ_VER = "OBJ_VER";
	public static final String VAL = "VAL";
	
	public static final String PG_COL_SEPARATOR = ":";

	public static final String[] COLS_INDEX = {
		TENANT_ID,OBJ_DEF_ID,COL_NAME,OBJ_ID,OBJ_VER,VAL
	};

	public static final String[] COLS_UNIQUE = {
		TENANT_ID,OBJ_DEF_ID,COL_NAME,OBJ_ID,VAL
	};

}
