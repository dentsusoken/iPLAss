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
package org.iplass.mtp.impl.metadata.xmlfile;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.metadata.MetaDataEntryThinWrapper;
import org.iplass.mtp.impl.metadata.RootMetaData;

@XmlRootElement(name = "metaDataEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class VersioningMetaDataEntryThinWrapper  extends MetaDataEntryThinWrapper {
	@XmlAttribute(required = false)
	private Integer version;
	
	@XmlAttribute(required=false)
	private boolean overwritable = true;
	
	@XmlAttribute(required = false)
	private boolean sharable = false;

	@XmlAttribute(required = false)
	private boolean dataSharable = false;
	
	@XmlAttribute(required = false)
	private boolean permissionSharable = false;
	
	public VersioningMetaDataEntryThinWrapper() {
		super();
	}
	
	public VersioningMetaDataEntryThinWrapper(RootMetaData metaData) {	
		super(metaData);
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public boolean isPermissionSharable() {
		return permissionSharable;
	}

	public void setPermissionSharable(boolean permissionSharable) {
		this.permissionSharable = permissionSharable;
	}
	
	public boolean isDataSharable() {
		return dataSharable;
	}

	public void setDataSharable(boolean dataSharable) {
		this.dataSharable = dataSharable;
	}

	public boolean isSharable() {
		return sharable;
	}

	public void setSharable(boolean sharable) {
		this.sharable = sharable;
	}

	public boolean isOverwritable() {
		return overwritable;
	}

	public void setOverwritable(boolean overwritable) {
		this.overwritable = overwritable;
	}
}
