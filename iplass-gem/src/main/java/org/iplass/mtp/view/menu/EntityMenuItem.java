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
 * Entity表示用メニューアイテム定義
 */
@XmlRootElement
public class EntityMenuItem extends MenuItem {

	private static final long serialVersionUID = 5211264967375867544L;

	/** 実行時に追加されるパラメータ */
	private String parameter;

	/** メニューに表示するEntity定義名 */
	private String entityDefinitionName;

	/** View名 */
	private String viewName;

	/** 画面表示時に検索を実行 */
	private boolean executeSearch;

	public EntityMenuItem() {
	}

	public EntityMenuItem(String defName, String displayName) {
		setName(defName);
		setDisplayName(displayName);
		this.setEntityDefinitionName(defName);
	}

	@Override
	public <R> R accept(MenuItemVisitor<R> menuItemVisitor) {
		return menuItemVisitor.visit(this);
	}

	/**
	 * 実行時に追加されるパラメータを返します。
	 *
	 * @return Action実行時に追加されるパラメータ
	 */
	public String getParameter() {
	    return parameter;
	}

	/**
	 * 実行時に追加されるパラメータを設定します。
	 *
	 * @param parameter Action実行時に追加されるパラメータ
	 */
	public void setParameter(String parameter) {
	    this.parameter = parameter;
	}

	/**
	 * メニューに表示するEntity定義名を返します。
	 *
	 * @return メニューに表示するEntity定義名
	 */
	public String getEntityDefinitionName() {
	    return entityDefinitionName;
	}

	/**
	 * メニューに表示するEntity定義名を設定します。
	 *
	 * @param entityDefinitionName メニューに表示するEntity定義名
	 */
	public void setEntityDefinitionName(String entityDefinitionName) {
	    this.entityDefinitionName = entityDefinitionName;
	}

	/**
	 * 表示するView名を返します。
	 *
	 * @return View名
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * 表示するView名を設定します。
	 *
	 * @param viewName View名
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * 検索画面表示時に検索を実行するかをを返します。
	 *
	 * @return 検索を実行するか
	 */
	public boolean isExecuteSearch() {
		return executeSearch;
	}

	/**
	 * 検索画面表示時に検索を実行するかをを返します。
	 *
	 * @param executeSearch 検索を実行するか
	 */
	public void setExecuteSearch(boolean executeSearch) {
		this.executeSearch = executeSearch;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result + ((entityDefinitionName == null) ? 0 : entityDefinitionName.hashCode());
		result = prime * result + ((viewName == null) ? 0 : viewName.hashCode());
		result = prime * result + (executeSearch ? 1231 : 1237);
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
		EntityMenuItem other = (EntityMenuItem) obj;
		if (parameter == null) {
			if (other.parameter != null) {
				return false;
			}
		} else if (!parameter.equals(other.parameter)) {
			return false;
		}
		if (entityDefinitionName == null) {
			if (other.entityDefinitionName != null) {
				return false;
			}
		} else if (!entityDefinitionName.equals(other.entityDefinitionName)) {
			return false;
		}
		if (viewName == null) {
			if (other.viewName != null) {
				return false;
			}
		} else if (!viewName.equals(other.viewName)) {
			return false;
		}
		if (executeSearch != other.executeSearch) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EntityMenuItem [");
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
		builder.append(", entityDefinitionName=");
		builder.append(entityDefinitionName);
		builder.append(", viewName=");
		builder.append(viewName);
		builder.append(", executeSearch=");
		builder.append(executeSearch);
		builder.append("]");
		return builder.toString();
	}

}
