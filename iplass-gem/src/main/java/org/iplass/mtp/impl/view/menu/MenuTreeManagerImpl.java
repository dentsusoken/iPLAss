/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.menu.MenuTree;
import org.iplass.mtp.view.menu.MenuTreeManager;

public class MenuTreeManagerImpl extends AbstractTypedDefinitionManager<MenuTree> implements MenuTreeManager {

	private MenuTreeService service = ServiceRegistry.getRegistry().getService(MenuTreeService.class);

	@Deprecated
	@Override
	public MenuTree getMenuTree(String name) {
		return get(name);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult createMenuTree(MenuTree menuTree) {
		return create(menuTree);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult updateMenuTree(MenuTree menuTree) {
		return update(menuTree);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult deleteMenuTree(String name) {
		return remove(name);
	}

	@Override
	public Class<MenuTree> getDefinitionType() {
		return MenuTree.class;
	}

	@Override
	protected RootMetaData newInstance(MenuTree definition) {
		return new MetaTreeMenu();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
