/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.iplass.gem.command.generic.search.SearchViewCommand;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuItemVisitor;
import org.iplass.mtp.view.menu.MenuTree;
import org.iplass.mtp.view.menu.NodeMenuItem;
import org.iplass.mtp.view.menu.UrlMenuItem;
import org.iplass.mtp.web.actionmapping.permission.ActionParameter;
import org.iplass.mtp.web.actionmapping.permission.ActionPermission;

public class MenuAuthVisitor implements MenuItemVisitor<Boolean> {

	private AuthContext authContext;

	public MenuAuthVisitor(AuthContext authContext) {
		this.authContext = authContext;
	}

	@Override
	public Boolean visit(MenuTree menuTree) {
		menuTree.setMenuItems(acceptChild(menuTree.getMenuItems()));
		return true;
	}

	@Override
	public Boolean visitNoMenu(MenuTree menuTree) {
		return true;
	}

	@Override
	public Boolean visit(NodeMenuItem nodeMenuItem) {
		// 権限チェックをする。（現状は、対応する権限が無い)
		nodeMenuItem.setChilds(acceptChild(nodeMenuItem.getChilds()));
		return nodeMenuItem.getChilds() != null;
	}

	@Override
	public Boolean visit(ActionMenuItem actionMenuItem) {
		String actionName = actionMenuItem.getActionName();
		String param = actionMenuItem.getParameter();

		if(!checkActionPermission(actionName, new HashMap<String, String>(), param)) {
			return false;
		}

		actionMenuItem.setChilds(acceptChild(actionMenuItem.getChilds()));

		return true;
	}

	@Override
	public Boolean visit(EntityMenuItem entityMenuItem) {
		//Entity権限のチェック
		String entityName = entityMenuItem.getEntityDefinitionName();
		if (!authContext.checkPermission(new EntityPermission(entityName, EntityPermission.Action.REFERENCE))) {
			return false;
		}

		// Action権限のチェックをする。
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(Constants.DEF_NAME, entityName);
		paramMap.put(Constants.VIEW_NAME, entityMenuItem.getViewName());
		String param = entityMenuItem.getParameter();

		if(!checkActionPermission(SearchViewCommand.SEARCH_ACTION_NAME, paramMap, param)) {
			return false;
		}

		entityMenuItem.setChilds(acceptChild(entityMenuItem.getChilds()));

		return true;
	}

	@Override
	public Boolean visit(UrlMenuItem urlMenuItem) {
		// 権限チェック無し
		return true;
	}

	private List<MenuItem> acceptChild(List<MenuItem> childs) {
		if(childs == null || childs.isEmpty()) {
			// 子供が存在しないので終了
			return null;
		}
		// 子供の権限が無い場合は除外
		List<MenuItem> validChilds = childs.stream()
				.filter(child -> child.accept(this)).collect(Collectors.toList());

		if (validChilds.isEmpty()) {
			return null;
		} else {
			return validChilds;
		}
	}

	private Boolean checkActionPermission(String actionName, Map<String, String> paramMap, String param) {
		// 権限チェックのパラメータにマッピングする。
		if(StringUtil.isNotEmpty(param)) {
			paramMap.putAll(
					Arrays.stream(param.split("&"))
						.map(value -> value.split("="))
						.filter(value -> value.length == 2)
						.collect(Collectors.toMap(value -> value[0], value -> value[1])));
		}

		if (authContext.checkPermission(new ActionPermission(actionName, new MenuActionParameter(paramMap)))) {
			return true;
		} else {
			return false;
		}
	}

	private static class MenuActionParameter implements ActionParameter {

		private Map<String, String> paramValue;

		public MenuActionParameter(Map<String, String> paramValue) {
			this.paramValue = paramValue;
		}

		@Override
		public Object getValue(String name) {
			return paramValue.get(name);
		}
	}

}
