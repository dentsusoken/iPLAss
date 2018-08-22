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

public interface MenuTreeManager extends TypedDefinitionManager<MenuTree> {

	/**
	 * メニューツリーを返します
	 *
	 * @param name メニューツリー名
	 * @return メニューツリー
	 * @deprecated {@link #get(String)} を使用してください。
	 */
	@Deprecated
	public MenuTree getMenuTree(String name);

	/**
	 * メニューツリーを新規に作成します
	 *
	 * @param menuTree 作成するメニューツリー
	 * @deprecated {@link #create(MenuTree)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult createMenuTree(MenuTree menuTree);

	/**
	 * メニューツリーを更新します
	 *
	 * @param menuTree 更新するメニューツリー
	 * @deprecated {@link #update(MenuTree)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult updateMenuTree(MenuTree menuTree);

	/**
	 * メニューツリーを削除します
	 *
	 * @param name 削除するメニューツリー名
	 * @deprecated {@link #remove(String)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult deleteMenuTree(String name);
}
