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

package org.iplass.mtp.impl.view.menu;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.view.menu.MetaMenu.MetaMenuHandler;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.menu.ActionMenuAction;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuAction;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuItemManager;
import org.iplass.mtp.view.menu.UrlMenuAction;
import org.iplass.mtp.view.menu.UrlMenuItem;

public class MenuItemManagerImpl extends AbstractTypedDefinitionManager<MenuItem> implements MenuItemManager {

	private MenuItemService service = ServiceRegistry.getRegistry().getService(MenuItemService.class);

	@Deprecated
	@Override
	public MenuItem getMenuItem(String name) {
		return get(name);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult createMenuItem(MenuItem menuItem) {
		return create(menuItem);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult updateMenuItem(MenuItem menuItem) {
		return update(menuItem);
	}

	@Deprecated
	@Override
	public DefinitionModifyResult deleteMenuItem(String name) {
		return remove(name);
	}

	@Override
	public EntityMenuAction customizeEntityMenu(EntityMenuItem menuItem, String defaultAction) {
		EntityMenuAction ret = new EntityMenuAction(menuItem);
		ret.setActionName(defaultAction);

		MetaMenuHandler handler = service.getRuntimeByName(menuItem.getName());
		if (handler != null && handler.getCompiledCustomizeScript() != null) {
			ExecuteContext ec = ExecuteContext.getCurrentContext();
			ScriptEngine scriptEngine = ec.getTenantContext().getScriptEngine();
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute("menu", ret);

			handler.getCompiledCustomizeScript().eval(sc);
		}

		return ret;
	}

	@Override
	public ActionMenuAction customizeActionMenu(ActionMenuItem menuItem) {
		ActionMenuAction ret = new ActionMenuAction(menuItem);

		MetaMenuHandler handler = service.getRuntimeByName(menuItem.getName());
		if (handler != null && handler.getCompiledCustomizeScript() != null) {
			ExecuteContext ec = ExecuteContext.getCurrentContext();
			ScriptEngine scriptEngine = ec.getTenantContext().getScriptEngine();
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute("menu", ret);

			handler.getCompiledCustomizeScript().eval(sc);
		}

		return ret;
	}

	public UrlMenuAction customizeUrlMenu(UrlMenuItem menuItem) {
		UrlMenuAction ret = new UrlMenuAction(menuItem);

		MetaMenuHandler handler = service.getRuntimeByName(menuItem.getName());
		if (handler != null && handler.getCompiledCustomizeScript() != null) {
			ExecuteContext ec = ExecuteContext.getCurrentContext();
			ScriptEngine scriptEngine = ec.getTenantContext().getScriptEngine();
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute("menu", ret);

			handler.getCompiledCustomizeScript().eval(sc);
		}

		return ret;
	}

	@Override
	public Class<MenuItem> getDefinitionType() {
		return MenuItem.class;
	}

	@Override
	protected RootMetaData newInstance(MenuItem definition) {
		return MetaMenu.createInstance(definition);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
