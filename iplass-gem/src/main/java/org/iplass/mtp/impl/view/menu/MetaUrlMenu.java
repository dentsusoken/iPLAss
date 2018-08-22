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

package org.iplass.mtp.impl.view.menu;

import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.UrlMenuItem;

public class MetaUrlMenu extends MetaMenu {

	private static final long serialVersionUID = -296845078963049764L;

	/** 遷移先URL */
	private String url;

	/** パラメータ */
	private String parameter;

	/** 新しいページで表示 */
	private boolean showNewPage;

	/**
	 * 遷移先URLを取得します。
	 * @return 遷移先URL
	 */
	public String getUrl() {
	    return url;
	}

	/**
	 * 遷移先URLを設定します。
	 * @param url 遷移先URL
	 */
	public void setUrl(String url) {
	    this.url = url;
	}

	/**
	 * パラメータを取得します。
	 * @return パラメータ
	 */
	public String getParameter() {
	    return parameter;
	}

	/**
	 * パラメータを設定します。
	 * @param parameter パラメータ
	 */
	public void setParameter(String parameter) {
	    this.parameter = parameter;
	}

	/**
	 * 新しいページで表示を取得します。
	 * @return 新しいページで表示
	 */
	public boolean isShowNewPage() {
	    return showNewPage;
	}

	/**
	 * 新しいページで表示を設定します。
	 * @param showNewPage 新しいページで表示
	 */
	public void setShowNewPage(boolean showNewPage) {
	    this.showNewPage = showNewPage;
	}

	@Override
	public void applyConfig(MenuItem definition) {
		fillFrom(definition);
		UrlMenuItem def = (UrlMenuItem) definition;
		url = def.getUrl();
		parameter = def.getParameter();
		showNewPage = def.isShowNewPage();
	}

	@Override
	public MenuItem currentConfig() {
		UrlMenuItem definition = new UrlMenuItem();
		fillTo(definition);
		definition.setUrl(url);
		definition.setParameter(parameter);
		definition.setShowNewPage(showNewPage);
		return definition;
	}

	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new MetaUrlMenuHandler();
	}

	public class MetaUrlMenuHandler extends MetaMenuHandler {

		@Override
		public MetaUrlMenu getMetaData() {
			return MetaUrlMenu.this;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result + (showNewPage ? 1231 : 1237);
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
		MetaUrlMenu other = (MetaUrlMenu) obj;
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		if (parameter == null) {
			if (other.parameter != null) {
				return false;
			}
		} else if (!parameter.equals(other.parameter)) {
			return false;
		}
		if (showNewPage != other.showNewPage) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UrlMenuItem [");
		builder.append("name=");
		builder.append(getName());
		builder.append(", description=");
		builder.append(getDescription());
		builder.append(", definitionId=");
		builder.append(getDisplayName());
		builder.append(", icon=");
		builder.append(getIcon());
		builder.append(", url=");
		builder.append(url);
		builder.append(", parameter=");
		builder.append(parameter);
		builder.append(", showNewPage=");
		builder.append(showNewPage);
		builder.append("]");
		return builder.toString();
	}

}
