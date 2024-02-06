/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.metaport;

/**
 * バックアップやインポートしたメタデータを保持するEntity。
 */
public interface MetaDataTagEntity {

	/** Definition名 */
	public static final String ENTITY_DEFINITION_NAME = "mtp.maintenance.MetaDataTag";

	/** MetaData(BinaryReference,XMLファイル) */
	public static final String METADATA = "metadata";

	/** 種類(格納メタデータの種類,SelectValue) */
	public static final String TYPE = "type";

//	/** ステータス(格納メタデータの状態,SelectValue) */
//	public static final String STATUS = "status";

	//種類(Select値)
	/** スナップショット(種類：Select値) */
	public static final String TYPE_SNAPSHOT = "SNAPSHOT";
	/** インポートファイル(種類：Select値) */
	public static final String TYPE_IMPORT_FILE = "IMPORTFILE";

}
