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

/**
 *
 */
package org.iplass.mtp.impl.view.menu;

import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;

public final class MetaEntityMenu extends MetaMenu {

	private static final long serialVersionUID = -7349512062871654338L;

	/** MetaEntityのID */
	private String definitionId;

	/** 実行時に追加されるパラメータ */
	private String parameter;

	/** View名 */
	private String viewName;

	/** 画面表示時に検索を実行 */
	private boolean executeSearch;

	@Override
	public MetaEntityMenuHandler createRuntime(MetaDataConfig metaDataConfig) {
		return new MetaEntityMenuHandler();
	}

	@Override
	public void applyConfig(MenuItem definition) {
		fillFrom(definition);
		EntityMenuItem def = (EntityMenuItem) definition;
		parameter = def.getParameter();
		viewName = def.getViewName();
		executeSearch = def.isExecuteSearch();

		//name - id 変換
		EntityService ehs = ServiceRegistry.getRegistry().getService(EntityService.class);
		EntityHandler target = ehs.getRuntimeByName(def.getEntityDefinitionName());
		if (target == null) {
			definitionId = null;
		} else {
			definitionId = target.getMetaData().getId();
		}
	}

	@Override
	public MenuItem currentConfig() {
		//MenuItem保存のタイミングによって未指定状態になる可能性があるためチェック
		if (getDefinitionId() == null) {
			return null;
		}
		EntityService ehs = ServiceRegistry.getRegistry().getService(EntityService.class);
		EntityHandler entityHandler = ehs.getRuntimeById(getDefinitionId());
		if(entityHandler == null) {
			return null;
		}
		MetaEntity entity = entityHandler.getMetaData();
		if(entity == null) {
			return null;
		}

		EntityMenuItem definition = new EntityMenuItem();
		fillTo(definition);
		definition.setParameter(parameter);
		definition.setEntityDefinitionName(entity.getName());
		definition.setViewName(viewName);
		definition.setExecuteSearch(executeSearch);
		return definition;
	}


	public class MetaEntityMenuHandler extends MetaMenuHandler {
		@Override
		public MetaEntityMenu getMetaData() {
			return MetaEntityMenu.this;
		}
	}

	/**
	 * メニューで参照するEntityのIDを返します。
	 *
	 * @return EntityのID
	 */
	public String getDefinitionId() {
	    return definitionId;
	}

	/**
	 * メニューで参照するEntityのIDを設定します。
	 *
	 * @param definitionId EntityのID
	 */
	public void setDefinitionId(String definitionId) {
	    this.definitionId = definitionId;
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
	 * @param parameter Action実行時に追加されるパラメータ
	 */
	public void setParameter(String parameter) {
	    this.parameter = parameter;
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
		result = prime * result + ((definitionId == null) ? 0 : definitionId.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
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
		MetaEntityMenu other = (MetaEntityMenu) obj;
		if (definitionId == null) {
			if (other.definitionId != null) {
				return false;
			}
		} else if (!definitionId.equals(other.definitionId)) {
			return false;
		}
		if (parameter == null) {
			if (other.parameter != null) {
				return false;
			}
		} else if (!parameter.equals(other.parameter)) {
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
		builder.append("MetaEntityMenu [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append(", description=");
		builder.append(description);
		builder.append(", definitionId=");
		builder.append(definitionId);
		builder.append(", viewName=");
		builder.append(viewName);
		builder.append(", executeSearch=");
		builder.append(executeSearch);
		builder.append(", parameter=");
		builder.append(parameter);
		builder.append(", icon=");
		builder.append(icon);
		builder.append("]");
		return builder.toString();
	}

}
