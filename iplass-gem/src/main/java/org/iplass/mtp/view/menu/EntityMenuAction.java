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

package org.iplass.mtp.view.menu;

public class EntityMenuAction {

	/** アクション名 */
	private String actionName;

	/** Entity定義名 */
	private String defName;

	/** View名 */
	private String viewName;

	/** 画面表示時に検索を実行 */
	private boolean executeSearch;

	/** 実行時に追加されるパラメータ */
	private String parameter;

	public EntityMenuAction() {
	}

	public EntityMenuAction(EntityMenuItem menuItem) {
		setDefName(menuItem.getEntityDefinitionName());
		setExecuteSearch(menuItem.isExecuteSearch());
		setParameter(menuItem.getParameter());
		setViewName(menuItem.getViewName());
	}

	/**
	 * アクション名を取得します。
	 * @return アクション名
	 */
	public String getActionName() {
	    return actionName;
	}

	/**
	 * アクション名を設定します。
	 * @param actionName アクション名
	 */
	public void setActionName(String actionName) {
	    this.actionName = actionName;
	}

	/**
	 * Entity定義名を取得します。
	 * @return Entity定義名
	 */
	public String getDefName() {
	    return defName;
	}

	/**
	 * Entity定義名を設定します。
	 * @param defName Entity定義名
	 */
	public void setDefName(String defName) {
	    this.defName = defName;
	}

	/**
	 * View名を取得します。
	 * @return View名
	 */
	public String getViewName() {
	    return viewName;
	}

	/**
	 * View名を設定します。
	 * @param viewName View名
	 */
	public void setViewName(String viewName) {
	    this.viewName = viewName;
	}

	/**
	 * 画面表示時に検索を実行を取得します。
	 * @return 画面表示時に検索を実行
	 */
	public boolean isExecuteSearch() {
	    return executeSearch;
	}

	/**
	 * 画面表示時に検索を実行を設定します。
	 * @param executeSearch 画面表示時に検索を実行
	 */
	public void setExecuteSearch(boolean executeSearch) {
	    this.executeSearch = executeSearch;
	}

	/**
	 * 実行時に追加されるパラメータを取得します。
	 * @return 実行時に追加されるパラメータ
	 */
	public String getParameter() {
	    return parameter;
	}

	/**
	 * 実行時に追加されるパラメータを設定します。
	 * @param parameter 実行時に追加されるパラメータ
	 */
	public void setParameter(String parameter) {
	    this.parameter = parameter;
	}
}
