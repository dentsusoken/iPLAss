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

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;

@XmlSeeAlso(value = {
		JspTemplateDefinition.class,
		GroovyTemplateDefinition.class,
		HtmlTemplateDefinition.class,
		BinaryTemplateDefinition.class,
		ReportTemplateDefinition.class
		})
public abstract class TemplateDefinition implements Definition {

	private static final long serialVersionUID = 4432940487717913835L;

	/** 定義名 */
	private String name;

	/** 表示名 */
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;

	private List<LocalizedStringDefinition> localizedDisplayNameList;

	/** 説明 */
	private String description;

	/** ContentType */
	private String contentType;

	/** LayoutAction(ActionMappingのName) */
	private String layoutActionName;

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName セットする displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	public void addLocalizedDisplayName(LocalizedStringDefinition localizedDisplayName) {
		if (localizedDisplayNameList == null) {
			localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
		}
		localizedDisplayNameList.add(localizedDisplayName);
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description セットする description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType セットする contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return layoutActionName
	 */
	public String getLayoutActionName() {
		return layoutActionName;
	}

	/**
	 * @param layoutActionName セットする layoutActionName
	 */
	public void setLayoutActionName(String layoutActionName) {
		this.layoutActionName = layoutActionName;
	}

}
