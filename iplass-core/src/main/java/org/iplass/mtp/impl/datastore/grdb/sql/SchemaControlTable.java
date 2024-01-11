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
package org.iplass.mtp.impl.datastore.grdb.sql;

/**
 * テーブル SCHEMA_CTRL 定義
 *
 * @see org.iplass.mtp.impl.datastore.grdb.strategy.metadata.LockStatus
 * @author SEKIGUCHI Naoya
 */
public interface SchemaControlTable {
	/** テーブル名 */
	String TABLE_NAME = "SCHEMA_CTRL";

	/** テナントID */
	String TENANT_ID = "TENANT_ID";
	/** OBJ_DEF_ID */
	String OBJ_DEF_ID = "OBJ_DEF_ID";
	/** OBJ_DEF_VER */
	String OBJ_DEF_VER = "OBJ_DEF_VER";
	/** LOCK_STATUS */
	String LOCK_STATUS = "LOCK_STATUS";
	/** CR_DATA_VER */
	String CR_DATA_VER = "CR_DATA_VER";
}
