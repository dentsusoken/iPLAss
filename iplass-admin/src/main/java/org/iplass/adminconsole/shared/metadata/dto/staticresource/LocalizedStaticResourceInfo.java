/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.web.staticresource.definition.LocalizedStaticResourceDefinition;

public class LocalizedStaticResourceInfo implements Definition {

	private static final long serialVersionUID = -3051863152423865170L;

	private String name;
	private String localeName;
	private String binaryName;
	private String storedBinaryName;
	private FileType fileType;

	public LocalizedStaticResourceInfo() {
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocaleName() {
		return localeName;
	}
	public void setLocaleName(String localeName) {
		this.localeName = localeName;
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

	public LocalizedStaticResourceDefinition toDefinition() {
		LocalizedStaticResourceDefinition def = new LocalizedStaticResourceDefinition();
		def.setLocaleName(this.localeName);
		return def;
	}

	public static LocalizedStaticResourceInfo valueOf(LocalizedStaticResourceDefinition definition) {
		LocalizedStaticResourceInfo info = new LocalizedStaticResourceInfo();
		info.setLocaleName(definition.getLocaleName());
		return info;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public void setDisplayName(String displayName) {
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void setDescription(String description) {
	}

}
