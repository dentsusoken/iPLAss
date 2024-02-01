/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.impl.view.menu.MetaMenu.MetaMenuHandler;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuItemManager;

public class MenuItemService extends AbstractTypedMetaDataService<MetaMenu, MetaMenuHandler> implements Service {

	public static final String MENU_ITEM_PATH = "/view/menu/item/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<MenuItem, MetaMenu> {
		public TypeMap() {
			super(getFixedPath(), MetaMenu.class, MenuItem.class);
		}
		@Override
		public TypedDefinitionManager<MenuItem> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(MenuItemManager.class);
		}
	}

	public MenuItemService() {
	}

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	public static String getFixedPath() {
		return MENU_ITEM_PATH;
	}

	@Override
	public Class<MetaMenu> getMetaDataType() {
		return MetaMenu.class;
	}

	@Override
	public Class<MetaMenuHandler> getRuntimeType() {
		return MetaMenuHandler.class;
	}
}
