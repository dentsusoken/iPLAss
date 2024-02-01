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

package org.iplass.mtp.view.menu;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action実行用メニューアイテム定義
 */
@XmlRootElement
public class ActionMenuItem extends MenuItem {

	private static final long serialVersionUID = 4953543417129531970L;

	/** Action実行時に追加されるパラメータ */
	private String parameter;

	/** 実行するActionの名前 */
	private String actionName;

	/**
	 * Action実行時に追加されるパラメータを返します。
	 *
	 * @return Action実行時に追加されるパラメータ
	 */
	public String getParameter() {
	    return parameter;
	}

	/**
	 * Action実行時に追加されるパラメータを設定します。
	 *
	 * @param parameter Action実行時に追加されるパラメータ
	 */
	public void setParameter(String parameter) {
	    this.parameter = parameter;
	}

	/**
	 * 実行するActionの名前を返します。
	 *
	 * @return 実行するActionの名前
	 */
	public String getActionName() {
	    return actionName;
	}

	/**
	 * 実行するActionの名前を設定します。
	 *
	 * @param actionName 実行するActionの名前
	 */
	public void setActionName(String actionName) {
	    this.actionName = actionName;
	}

	@Override
	public <R> R accept(MenuItemVisitor<R> menuItemVisitor) {
		return menuItemVisitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ActionMenuItem other = (ActionMenuItem) obj;
		if (parameter == null) {
			if (other.parameter != null) {
				return false;
			}
		} else if (!parameter.equals(other.parameter)) {
			return false;
		}
		if (actionName == null) {
			if (other.actionName != null) {
				return false;
			}
		} else if (!actionName.equals(other.actionName)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActionMenuItem [");
		builder.append("name=");
		builder.append(getName());
		builder.append(", description=");
		builder.append(getDescription());
		builder.append(", definitionId=");
		builder.append(getDisplayName());
		builder.append(", imageUrl=");
		builder.append(getImageUrl());
		builder.append(", parameter=");
		builder.append(parameter);
		builder.append(", actionName=");
		builder.append(actionName);
		builder.append("]");
		return builder.toString();
	}
}
