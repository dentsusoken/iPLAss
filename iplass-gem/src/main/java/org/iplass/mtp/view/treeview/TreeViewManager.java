/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.treeview;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.definition.TypedDefinitionManager;

/**
 * ツリービュー定義を管理するクラスのインターフェース
 * @author lis3wg
 */
public interface TreeViewManager extends TypedDefinitionManager<TreeView> {

	/**
	 * 指定のTreeViewを取得する。
	 * @param name TreeViewの名前
	 * @return TreeView TreeView
	 * @deprecated {@link #get(String)} を使用してください。
	 */
	@Deprecated
	public TreeView getTreeViewByName(String name);

	/**
	 * TreeViewを新規に作成する。
	 * @param treeView TreeView
	 * @deprecated {@link #create(TreeView)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult createTreeView(TreeView treeView);

	/**
	 * TreeViewを更新する。
	 * @param treeView TreeView
	 * @deprecated {@link #update(TreeView)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult updateTreeView(TreeView treeView);

	/**
	 * TreeViewを削除する。
	 * @param name TreeViewの名前
	 * @deprecated {@link #remove(String)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult removeTreeView(String name);
}
