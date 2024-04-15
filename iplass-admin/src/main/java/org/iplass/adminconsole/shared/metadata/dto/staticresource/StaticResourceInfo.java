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

package org.iplass.adminconsole.shared.metadata.dto.staticresource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.web.staticresource.definition.EntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.LocalizedStaticResourceDefinition;
import org.iplass.mtp.web.staticresource.definition.MimeTypeMappingDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;

/**
 * 静的リソースの定義。
 * resourceとして、単一のファイル、もしくはアーカイブ（zipもしくはjar）されたファイルを指定可能。
 * アーカイブとして登録した場合は、アーカイブ内のファイルを指定して取得可能となる。
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class StaticResourceInfo implements Definition {

	private static final long serialVersionUID = 6902974253290025512L;

	private String name;
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;
	private List<LocalizedStringDefinition> localizedDisplayNameList;
	private String description;

	private String binaryName;
	private String storedBinaryName;
	private FileType fileType;
	private List<LocalizedStaticResourceInfo> localizedResourceList;
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
	public String getBinaryName() {
		return binaryName;
	}
	public void setBinaryName(String binaryName) {
		this.binaryName = binaryName;
	}
	public String getStoredBinaryName() {
		return storedBinaryName;
	}
	public void setStoredBinaryName(String storedBinaryName) {
		this.storedBinaryName = storedBinaryName;
	}
	public FileType getFileType() {
		return fileType;
	}
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}
	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}
	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<LocalizedStaticResourceInfo> getLocalizedResourceList() {
		return localizedResourceList;
	}
	public void setLocalizedResourceList(List<LocalizedStaticResourceInfo> localizedResourceList) {
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
	public void setEntryPathTranslator(EntryPathTranslatorDefinition entryPathTranslator) {
		this.entryPathTranslator = entryPathTranslator;
	}

	public StaticResourceDefinition toDefinition() {
		StaticResourceDefinition def = new StaticResourceDefinition();

		def.setName(this.name);
		def.setDisplayName(this.displayName);
		def.setLocalizedDisplayNameList(this.localizedDisplayNameList);
		def.setDescription(this.description);
		def.setContentType(this.contentType);
		def.setMimeTypeMapping(this.mimeTypeMapping);
		def.setEntryTextCharset(this.entryTextCharset);
		def.setEntryPathTranslator(this.entryPathTranslator);

		if (this.localizedResourceList != null) {
			List<LocalizedStaticResourceDefinition> lsrDefList = new ArrayList<LocalizedStaticResourceDefinition>();
			for (LocalizedStaticResourceInfo lsrInfo : this.localizedResourceList) {
				lsrDefList.add(lsrInfo.toDefinition());
			}
			def.setLocalizedResourceList(lsrDefList);
		}

		return def;
	}

	public static StaticResourceInfo valueOf(StaticResourceDefinition definition) {
		StaticResourceInfo info = new StaticResourceInfo();

		info.setName(definition.getName());
		info.setDisplayName(definition.getDisplayName());
		info.setLocalizedDisplayNameList(definition.getLocalizedDisplayNameList());
		info.setDescription(definition.getDescription());
		info.setContentType(definition.getContentType());
		info.setMimeTypeMapping(definition.getMimeTypeMapping());
		info.setEntryTextCharset(definition.getEntryTextCharset());
		info.setEntryPathTranslator(definition.getEntryPathTranslator());

		return info;
	}

}
