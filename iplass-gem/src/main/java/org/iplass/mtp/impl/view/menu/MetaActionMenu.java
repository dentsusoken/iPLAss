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

import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.MenuItem;

public final class MetaActionMenu extends MetaMenu {

	private static final long serialVersionUID = 4468943776970244262L;

	/** MetaActionMappingのID */
	String definitionId;

	/** 実行時に追加されるパラメータ */
	private String parameter;

	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new MetaActionMenuHandler();
	}

	@Override
	public void applyConfig(MenuItem definition) {
		fillFrom(definition);
		ActionMenuItem def = (ActionMenuItem) definition;
		parameter = def.getParameter();

		//name - id 変換
		ActionMappingService service = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
		ActionMappingRuntime target = service.getByPathHierarchy(def.getActionName());
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
		ActionMappingService service = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
		ActionMappingRuntime target = service.getRuntimeById(getDefinitionId());
		if(target == null) {
			return null;
		}
		MetaActionMapping mam = target.getMetaData();
		if(mam == null) {
			return null;
		}
		ActionMenuItem definition = new ActionMenuItem();
		fillTo(definition);
		definition.setParameter(parameter);
		definition.setActionName(mam.getName());
		return definition;
	}

	public class MetaActionMenuHandler extends MetaMenuHandler {

		@Override
		public MetaActionMenu getMetaData() {
			return MetaActionMenu.this;
		}
	}

	/**
	 * メニューで参照するActionのIDを取得します。
	 *
	 * @return メニューで参照するActionのID
	 */
	public String getDefinitionId() {
	    return definitionId;
	}

	/**
	 * メニューで参照するActionのIDを設定します。
	 *
	 * @param definitionId メニューで参照するActionのID
	 */
	public void setDefinitionId(String definitionId) {
	    this.definitionId = definitionId;
	}

	/**
	 * Action実行時に追加されるパラメータを取得します。
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((definitionId == null) ? 0 : definitionId.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
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
		MetaActionMenu other = (MetaActionMenu) obj;
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
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetaActionMenu [parameter=");
		builder.append(parameter);
		builder.append(", id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append(", description=");
		builder.append(description);
		builder.append(", definitionId=");
		builder.append(definitionId);
		builder.append(", icon=");
		builder.append(icon);
		builder.append("]");
		return builder.toString();
	}

}
