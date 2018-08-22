/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch.sql;

public interface DeleteLogTable {

	public static final String TABLE_NAME = "DELETE_LOG";

	public static final String TENANT_ID = "TENANT_ID";
	public static final String OBJ_DEF_ID = "OBJ_DEF_ID";
	public static final String OBJ_ID = "OBJ_ID";
	public static final String OBJ_VER = "OBJ_VER";
	public static final String STATUS = "STATUS";
	public static final String CRE_DATE = "CRE_DATE";
	public static final String UP_DATE = "UP_DATE";

	public enum Status {

		DELETE("D"),
		RESTORE("R"),
		EMPTY("");

		private String code;
		Status(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}

		public static Status codeOf(String code) {
			for (Status status : values()) {
				if (code.equals(status.code())) {
					return status;
				}
			}
			return Status.EMPTY;
		}
	}

}
