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

package org.iplass.mtp.view.menu;

public class ActionMenuAction {

	/** Action実行時に追加されるパラメータ */
	private String parameter;

	/** 実行するActionの名前 */
	private String actionName;

	public ActionMenuAction() {
	}

	public ActionMenuAction(ActionMenuItem menuItem) {
		setActionName(menuItem.getActionName());
		setParameter(menuItem.getParameter());
	}

	/**
	 * Action実行時に追加されるパラメータを取得します。
	 * @return Action実行時に追加されるパラメータ
	 */
	public String getParameter() {
	    return parameter;
	}

	/**
	 * Action実行時に追加されるパラメータを設定します。
	 * @param parameter Action実行時に追加されるパラメータ
	 */
	public void setParameter(String parameter) {
	    this.parameter = parameter;
	}

	/**
	 * 実行するActionの名前を取得します。
	 * @return 実行するActionの名前
	 */
	public String getActionName() {
	    return actionName;
	}

	/**
	 * 実行するActionの名前を設定します。
	 * @param actionName 実行するActionの名前
	 */
	public void setActionName(String actionName) {
	    this.actionName = actionName;
	}


}
