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

package org.iplass.adminconsole.shared.metadata.dto;

/**
 * MetaData関連の共通定数定義
 */
public interface MetaDataConstants {

	public static final String META_CATEGORY_SECURITY						= "Security";
	public static final String META_CATEGORY_DATA_MODEL				= "Data Model";
	public static final String META_CATEGORY_VIEW_COMPONENTS			= "View Components";
	public static final String META_CATEGORY_CUSTOMIZATION			= "Customization";
	public static final String META_CATEGORY_NOTIFICATION			= "Notification";

	/** 名前の正規表現(パスにスラッシュを利用) */
	public static final String NAME_REG_EXP_PATH_SLASH = "^[0-9a-zA-Z_]+(/[0-9a-zA-Z_-]+)*$";
	/** 名前の正規表現(パスにスラッシュを利用、名前にピリオド含む) */
	public static final String NAME_REG_EXP_PATH_SLASH_NAME_PERIOD = "^[0-9a-zA-Z_]+(/[0-9a-zA-Z_-]+)*(\\.[0-9a-zA-Z_-]+)*$";
	/** 名前の正規表現(パスにピリオドを利用) */
	public static final String NAME_REG_EXP_PATH_PERIOD = "^[0-9a-zA-Z_]+(\\.[0-9a-zA-Z_-]+)*$";

	/** Entity名の正規表現(英数、アンダスコア、パスにピリオド、先頭の数字不可、マイナス不可) */
	public static final String ENTITY_NAME_REG_EXP_PATH_PERIOD = "^[a-zA-Z_][0-9a-zA-Z_]*(\\.[a-zA-Z_][0-9a-zA-Z_]*)*$";
	/** Entityプロパティ名の正規表現(英数、アンダスコア、先頭の数字不可、マイナス不可) */
	public static final String ENTITY_PROPERTY_NAME_REG_EXP_PATH_PERIOD = "^[a-zA-Z_]+([0-9a-zA-Z_]+)*$";

	/** 標準FormItemの幅 */
	public static final int DEFAULT_FORM_ITEM_WIDTH = 300;

	/** ノードアイコン(Item Local) */
	public static final String NODE_ICON_LOCAL_ITEM = "page.png";
	/** ノードアイコン(Item Shared) */
	public static final String NODE_ICON_SHARED_ITEM = "page_lightning.png";
	/** ノードアイコン(Item SharedOverrite) */
	public static final String NODE_ICON_SHARED_OVERWRITE_ITEM = "page_red.png";
	/** ノードアイコン(Item Not OverWritable) */
	public static final String NODE_ICON_LOCKED_ITEM = "page_link.png";

	/** コンテキストメニューアイコン(追加) */
	public static final String CONTEXT_MENU_ICON_ADD = "page_add.png";
	/** コンテキストメニューアイコン(名前変更) */
	public static final String CONTEXT_MENU_ICON_RENAME = "page_edit.png";
	/** コンテキストメニューアイコン(削除) */
	public static final String CONTEXT_MENU_ICON_DEL = "page_delete.png";
	/** コンテキストメニューアイコン(コピー) */
	public static final String CONTEXT_MENU_ICON_COPY = "page_copy.png";
	/** コンテキストメニューアイコン(リフレッシュ) */
	public static final String CONTEXT_MENU_ICON_REFRESH = "page_refresh.png";


	/** ロールEntity名 */
	public static final String ENTITY_NAME_ROLE = "mtp.auth.Role";

	/** ロール条件Entity名 */
	public static final String ENTITY_NAME_ROLE_CONDITION = "mtp.auth.RoleCondition";

	/** MetaDataのDrag and Dropのタイプ */
	public static final String METADATA_DRAG_DROP_TYPE = "MetaDataItem";

}
