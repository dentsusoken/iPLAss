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

public interface LobStoreTable {

	public static final String TABLE_NAME = "LOB_STORE";

	public static final String TENANT_ID = "TENANT_ID";
	public static final String LOB_DATA_ID = "LOB_DATA_ID";
	public static final String CRE_DATE = "CRE_DATE";
	public static final String B_DATA = "B_DATA";
	public static final String REF_COUNT = "REF_COUNT";
	public static final String LOB_SIZE = "LOB_SIZE";

	public static final String[] COLS = {
		TENANT_ID,LOB_DATA_ID,CRE_DATE,B_DATA,REF_COUNT,LOB_SIZE
	};

	public static final String[] LOB_ID_COL = {"LOB_ID"};
}
