/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.menu;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.view.menu.MetaTreeMenu.MetaTreeMenuHandler;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.view.menu.MenuTree;
import org.iplass.mtp.view.menu.MenuTreeManager;

public class MenuTreeService extends AbstractTypedMetaDataService<MetaTreeMenu, MetaTreeMenuHandler> implements Service {

	public static final String MENU_TREE_PATH = "/view/menu/tree/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<MenuTree, MetaTreeMenu> {
		public TypeMap() {
			super(getFixedPath(), MetaTreeMenu.class, MenuTree.class);
		}
		@Override
		public TypedDefinitionManager<MenuTree> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(MenuTreeManager.class);
		}
	}

	public MenuTreeService() {
	}

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	public static String getFixedPath() {
		return MENU_TREE_PATH;
	}

	@Override
	public Class<MetaTreeMenu> getMetaDataType() {
		return MetaTreeMenu.class;
	}

	@Override
	public Class<MetaTreeMenuHandler> getRuntimeType() {
		return MetaTreeMenuHandler.class;
	}

}
