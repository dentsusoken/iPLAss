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

package org.iplass.mtp.web.template.definition;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.definition.LocalizedStringDefinition;

@XmlRootElement
public class JspTemplateDefinition extends TemplateDefinition {

	private static final long serialVersionUID = -6397493149115396048L;

	private String path;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedPathList;

	/**
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path セットする path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedPathList() {
		return localizedPathList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedPathList(List<LocalizedStringDefinition> localizedPathList) {
		this.localizedPathList = localizedPathList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedPath(LocalizedStringDefinition localizedPath) {
		if (localizedPathList == null) {
			localizedPathList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedPathList.add(localizedPath);
	}

}
