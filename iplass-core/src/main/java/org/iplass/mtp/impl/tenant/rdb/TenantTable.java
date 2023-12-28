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
package org.iplass.mtp.impl.tenant.rdb;

/**
 * テーブル T_TENANT 定義
 *
 * @author SEKIGUCHI Naoya
 */
public interface TenantTable {
	/** テーブル名 */
	String TABLE_NAME = "T_TENANT";

	/** ID */
	String ID = "ID";
	/** テナント名 */
	String NAME = "NAME";
	/** 詳細 */
	String DESCRIPTION = "DESCRIPTION";
	/** ホスト名 */
	String HOST_NAME = "HOST_NAME";
	/** URL */
	String URL = "URL";
	/** 有効日時（FROM） */
	String YUKO_DATE_FROM = "YUKO_DATE_FROM";
	/** 有効日時（TO）*/
	String YUKO_DATE_TO = "YUKO_DATE_TO";
	/** 作成ユーザー */
	String CRE_USER = "CRE_USER";
	/** 作成日時 */
	String CRE_DATE = "CRE_DATE";
	/** 更新ユーザー */
	String UP_USER = "UP_USER";
	/** 更新日時 */
	String UP_DATE = "UP_DATE";
}
