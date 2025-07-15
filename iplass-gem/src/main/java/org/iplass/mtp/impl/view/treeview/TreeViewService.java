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

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.view.treeview.MetaTreeView.TreeViewHandler;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.view.treeview.TreeView;
import org.iplass.mtp.view.treeview.TreeViewManager;

/**
 * ツリービュー定義を扱うサービス
 * @author lis3wg
 */
public class TreeViewService extends AbstractTypedMetaDataService<MetaTreeView, TreeViewHandler> implements Service {

	/** メタデータのパス */
	public static final String META_PATH = "/view/treeview/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<TreeView, MetaTreeView> {
		public TypeMap() {
			super(getFixedPath(), MetaTreeView.class, TreeView.class);
		}
		@Override
		public TypedDefinitionManager<TreeView> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(TreeViewManager.class);
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return DefinitionNameChecker.getPathSlashDefinitionNameChecker();
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(Config config) {
	}

	public static String getFixedPath() {
		return META_PATH;
	}

	@Override
	public Class<MetaTreeView> getMetaDataType() {
		return MetaTreeView.class;
	}

	@Override
	public Class<TreeViewHandler> getRuntimeType() {
		return TreeViewHandler.class;
	}
}
