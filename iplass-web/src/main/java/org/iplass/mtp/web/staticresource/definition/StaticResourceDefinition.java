/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.web.staticresource.definition;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.definition.binary.BinaryDefinition;

/**
 * 静的リソースの定義。
 * resourceとして、単一のファイル、もしくはアーカイブ（zipもしくはjar）されたファイルを指定可能。
 * アーカイブとして登録した場合は、アーカイブ内のファイルを指定して取得可能となる。
 * 
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class StaticResourceDefinition implements Definition {
	private static final long serialVersionUID = 7919419226190191428L;
	
	private String name;
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;
	private List<LocalizedStringDefinition> localizedDisplayNameList;
	private String description;

	private BinaryDefinition resource;
	private List<LocalizedStaticResourceDefinition> localizedResourceList;
	private String contentType;
	
	private List<MimeTypeMappingDefinition> mimeTypeMapping;
	private String entryTextCharset;
	private EntryPathTranslatorDefinition entryPathTranslator;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}
	public void setLocalizedDisplayNameList(
			List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BinaryDefinition getResource() {
		return resource;
	}
	public void setResource(BinaryDefinition resource) {
		this.resource = resource;
	}
	public List<LocalizedStaticResourceDefinition> getLocalizedResourceList() {
		return localizedResourceList;
	}
	public void setLocalizedResourceList(
			List<LocalizedStaticResourceDefinition> localizedResourceList) {
		this.localizedResourceList = localizedResourceList;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public List<MimeTypeMappingDefinition> getMimeTypeMapping() {
		return mimeTypeMapping;
	}
	public void setMimeTypeMapping(List<MimeTypeMappingDefinition> mimeTypeMapping) {
		this.mimeTypeMapping = mimeTypeMapping;
	}
	public String getEntryTextCharset() {
		return entryTextCharset;
	}
	public void setEntryTextCharset(String entryTextCharset) {
		this.entryTextCharset = entryTextCharset;
	}
	public EntryPathTranslatorDefinition getEntryPathTranslator() {
		return entryPathTranslator;
	}
	public void setEntryPathTranslator(
			EntryPathTranslatorDefinition entryPathTranslator) {
		this.entryPathTranslator = entryPathTranslator;
	}

}
