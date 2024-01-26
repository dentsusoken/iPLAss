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
package org.iplass.mtp.impl.auth.authenticate.builtin.sql;

/**
 * テーブル T_ACCOUNT 定義
 *
 * @author SEKIGUCHI Naoya
 */
public interface AccountTable {
	/** テーブル名 */
	String TABLE_NAME = "T_ACCOUNT";

	/** テナントID */
	String TENANT_ID = "TENANT_ID";
	/** アカウントID */
	String ACCOUNT_ID = "ACCOUNT_ID";
	/** パスワード */
	String PASSWORD = "PASSWORD";
	/** SALT */
	String SALT = "SALT";
	/** OID */
	String OID = "OID";
	/** 最終ログイン日時 */
	String LAST_LOGIN_ON = "LAST_LOGIN_ON";
	/** 最終パスワード更新日時 */
	String LAST_PASSWORD_CHANGE = "LAST_PASSWORD_CHANGE";
	/** ログインエラー回数 */
	String LOGIN_ERR_CNT = "LOGIN_ERR_CNT";
	/** ログインエラー日時 */
	String LOGIN_ERR_DATE = "LOGIN_ERR_DATE";
	/** POL_NAME */
	String POL_NAME = "POL_NAME";
	/** 作成ユーザー */
	String CRE_USER = "CRE_USER";
	/** 作成日時 */
	String CRE_DATE = "CRE_DATE";
	/** 更新ユーザー */
	String UP_USER = "UP_USER";
	/** 更新日時 */
	String UP_DATE = "UP_DATE";
}
