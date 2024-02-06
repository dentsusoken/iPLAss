/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.view.treeview;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.treeview.TreeView;
import org.iplass.mtp.view.treeview.TreeViewManager;

/**
 * ツリービュー定義を管理するクラス
 * @author lis3wg
 */
public class TreeViewManagerImpl extends AbstractTypedDefinitionManager<TreeView> implements TreeViewManager {

	/** サービス */
	private TreeViewService service = ServiceRegistry.getRegistry().getService(TreeViewService.class);

	@Deprecated
	@Override
	public TreeView getTreeViewByName(String name) {
		return get(name);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult createTreeView(TreeView treeView) {
		return create(treeView);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult updateTreeView(TreeView treeView) {
		return update(treeView);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult removeTreeView(String name) {
		return remove(name);
	}

	@Override
	public Class<TreeView> getDefinitionType() {
		return TreeView.class;
	}

	@Override
	protected RootMetaData newInstance(TreeView definition) {
		return new MetaTreeView();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
