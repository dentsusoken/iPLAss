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

package org.iplass.mtp.web.actionmapping.permission;

import java.util.Map;

import org.iplass.mtp.auth.Permission;

/**
 * Actionの実行権限。
 * action名（=urlのパス）とその際のパラメータ値で権限を表現。
 * 
 * @author K.Higuchi
 *
 */
public class ActionPermission extends Permission {
	
	private final String actionName;
	private final ActionParameter parameter;
	private final boolean externalResource;
	
	public ActionPermission(String actionName) {
		this(actionName, (ActionParameter) null, false);
	}
	
	public ActionPermission(String actionName, boolean externalResource) {
		this(actionName, (ActionParameter) null, externalResource);
	}
	
	public ActionPermission(String actionName, Map<String, Object> parameter) {
		this(actionName, new MapActionParameter(parameter), false);
	}

	public ActionPermission(String actionName, Map<String, Object> parameter, boolean externalResource) {
		this(actionName, new MapActionParameter(parameter), externalResource);
	}

	public ActionPermission(String actionName, ActionParameter parameter) {
		this(actionName, parameter, false);
	}

	public ActionPermission(String actionName, ActionParameter parameter, boolean externalResource) {
		this.actionName = actionName;
		this.parameter = parameter;
		this.externalResource = externalResource;
	}
	
	/**
	 * 実際にはActionとして定義されていないリソース（たとえば静的ファイルなど）の権限を定義して、
	 * その権限チェックを行いたい場合は、このフラグをtrueにセット。
	 * このフラグがtrueにセットされた場合は、当該pathはActionを表さないものとして、
	 * Action権限のテナント共有設定などのAction固有の設定は参照しないように動作する。
	 * 
	 * @return 
	 */
	public final boolean isExternalResource() {
		return externalResource;
	}

	public final String getActionName() {
		return actionName;
	}

	public final ActionParameter getParameter() {
		return parameter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + (externalResource ? 1231 : 1237);
		result = prime * result
				+ ((parameter == null) ? 0 : parameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionPermission other = (ActionPermission) obj;
		if (actionName == null) {
			if (other.actionName != null)
				return false;
		} else if (!actionName.equals(other.actionName))
			return false;
		if (externalResource != other.externalResource)
			return false;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActionPermission [actionName=" + actionName + ", parameter=" + parameter + ", externalResource="
				+ externalResource + "]";
	}

}
