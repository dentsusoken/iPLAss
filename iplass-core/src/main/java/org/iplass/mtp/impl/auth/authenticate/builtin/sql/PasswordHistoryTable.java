/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
 * テーブル T_PASS_HI 定義
 *
 * @author SEKIGUCHI Naoya
 */
public interface PasswordHistoryTable {
	/** テーブル名 */
	String TABLE_NAME = "T_PASS_HI";

	/** テナントID */
	String TENANT_ID = "TENANT_ID";
	/** アカウントID */
	String ACCOUNT_ID = "ACCOUNT_ID";
	/** パスワード */
	String PASSWORD = "PASSWORD";
	/** SALT */
	String SALT = "SALT";
	/** 更新日時 */
	String UP_DATE = "UP_DATE";
}
