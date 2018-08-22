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

package org.iplass.mtp.impl.metadata.rdb;

public interface ObjMetaTable {

	public static final String TABLE_NAME = "OBJ_META";

	public static final String TENANT_ID = "TENANT_ID";
	public static final String OBJ_DEF_ID = "OBJ_DEF_ID";
	public static final String OBJ_DEF_VER = "OBJ_DEF_VER";
	public static final String OBJ_DEF_PATH = "OBJ_DEF_PATH";
	public static final String OBJ_DEF_DISP_NAME = "OBJ_DEF_DISP_NAME";
	public static final String OBJ_DESC = "OBJ_DESC";
	public static final String OBJ_META_DATA = "OBJ_META_DATA";
	public static final String STATUS = "STATUS";
	public static final String SHARABLE = "SHARABLE";
	public static final String OVERWRITABLE = "OVERWRITABLE";
	public static final String CRE_USER = "CRE_USER";
	public static final String CRE_DATE = "CRE_DATE";
	public static final String UP_USER = "UP_USER";
	public static final String UP_DATE = "UP_DATE";

	/** 1.x系との共有利用用(2.0から未使用) */
	@Deprecated
	public static final String OBJ_DEF_NAME = "OBJ_DEF_NAME";
	@Deprecated
	public static final String OBJ_DEF_TYPE = "OBJ_DEF_TYPE";

}
