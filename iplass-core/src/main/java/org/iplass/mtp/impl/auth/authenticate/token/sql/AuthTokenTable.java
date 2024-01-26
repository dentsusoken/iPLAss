/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.token.sql;

/**
 * テーブル T_ATOKEN 定義
 *
 * @author SEKIGUCHI Naoya
 */
public interface AuthTokenTable {
	/** テーブル名 */
	String TABLE_NAME = "T_ATOKEN";

	/** テナントID */
	String TENANT_ID = "TENANT_ID";
	/** T_TYPE */
	String T_TYPE = "T_TYPE";
	/** U_KEY */
	String U_KEY = "U_KEY";
	/** SERIES */
	String SERIES = "SERIES";
	/** TOKEN */
	String TOKEN = "TOKEN";
	/** POL_NAME */
	String POL_NAME = "POL_NAME";
	/** S_DATE */
	String S_DATE = "S_DATE";
	/** T_INFO */
	String T_INFO = "T_INFO";
}
