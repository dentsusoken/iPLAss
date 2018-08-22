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
package org.iplass.mtp.impl.metadata.xmlfile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.metadata.MetaDataEntryThinWrapper;
import org.iplass.mtp.impl.metadata.RootMetaData;

@XmlRootElement(name = "metaDataEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlFileMetaDataEntryThinWrapper extends MetaDataEntryThinWrapper {

	@XmlAttribute(required = false)
	private Integer version;

	public XmlFileMetaDataEntryThinWrapper() {
		super();
	}
	
	public XmlFileMetaDataEntryThinWrapper(RootMetaData metaData) {	
		super(metaData);
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}	
}
