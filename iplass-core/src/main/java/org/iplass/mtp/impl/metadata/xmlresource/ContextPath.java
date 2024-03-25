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

package org.iplass.mtp.impl.metadata.xmlresource;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class ContextPath {

	private String name;
	private List<ContextPath> contextPath;
	private List<XmlResourceMetaDataEntryThinWrapper> entry;

	public ContextPath() {
	}

	public ContextPath(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public List<ContextPath> getContextPath() {
		return contextPath;
	}

	public void setContextPath(List<ContextPath> contextPath) {
		this.contextPath = contextPath;
	}

	public List<XmlResourceMetaDataEntryThinWrapper> getEntry() {
		return entry;
	}

	@XmlElement(name="metaDataEntry")
	public void setEntry(List<XmlResourceMetaDataEntryThinWrapper> entry) {
		this.entry = entry;
	}

}
