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

package org.iplass.mtp.view.menu;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.definition.TypedDefinitionManager;

public interface MenuItemManager extends TypedDefinitionManager<MenuItem> {
	/**
	 * メニューアイテムを返します
	 *
	 * @param name メニューアイテム名
	 * @return メニューアイテム
	 * @deprecated {@link #get(String)} を使用してください。
	 */
	@Deprecated
	public MenuItem getMenuItem(String name);

	/**
	 * メニューアイテムを作成します
	 *
	 * @param menuItem 作成するメニューアイテム
	 * @deprecated {@link #create(MenuItem)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult createMenuItem(MenuItem menuItem);

	/**
	 * メニューアイテムを更新します
	 *
	 * @param menuItem 更新するメニューアイテム
	 * @deprecated {@link #update(MenuItem)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult updateMenuItem(MenuItem menuItem);

	/**
	 * メニューアイテムを削除します
	 *
	 * @param name 削除するメニューアイテム名
	 * @deprecated {@link #remove(String)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult deleteMenuItem(String name);

	/**
	 * EntityMenuItemにカスタマイズ設定を行います。
	 *
	 * @param menuItem メニューアイテム
	 * @param defaultAction デフォルトのアクション
	 * @return カスタマイズ結果
	 */
	public EntityMenuAction customizeEntityMenu(EntityMenuItem menuItem, String defaultAction);

	/**
	 * ActionMenuItemにカスタマイズ設定を行います。
	 *
	 * @param menuItem メニューアイテム
	 * @return カスタマイズ結果
	 */
	public ActionMenuAction customizeActionMenu(ActionMenuItem menuItem);

	/**
	 * UrlMenuItemにカスタマイズ設定を行います。
	 *
	 * @param menuItem メニューアイテム
	 * @return カスタマイズ結果
	 */
	public UrlMenuAction customizeUrlMenu(UrlMenuItem menuItem);
}
