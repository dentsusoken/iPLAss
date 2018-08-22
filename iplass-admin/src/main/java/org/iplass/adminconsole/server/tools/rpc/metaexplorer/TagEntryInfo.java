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

package org.iplass.adminconsole.server.tools.rpc.metaexplorer;

import java.io.Serializable;

import org.iplass.mtp.impl.tools.metaport.XMLEntryInfo;

public class TagEntryInfo implements Serializable {

	private static final long serialVersionUID = 6058717527885871786L;

	private String tagName;

	private XMLEntryInfo entryInfo;

	public TagEntryInfo() {
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public XMLEntryInfo getEntryInfo() {
		return entryInfo;
	}

	public void setEntryInfo(XMLEntryInfo entryInfo) {
		this.entryInfo = entryInfo;
	}

}
