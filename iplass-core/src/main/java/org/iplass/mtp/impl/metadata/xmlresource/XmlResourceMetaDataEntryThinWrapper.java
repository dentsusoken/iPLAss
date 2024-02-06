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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.metadata.MetaDataEntryThinWrapper;
import org.iplass.mtp.impl.metadata.RootMetaData;

/**
 * <p>XMLリソース用メタデータエントリ定義のラッパークラスです。</p>
 *
 * <p>{@link MetaDataEntryThinWrapper}に対して、name属性を追加しています。</br>
 * このname属性にフルパスを指定することにより、解析時に<code>&lt;contextPath&gt;</code>で指定されるパス定義を上書きします。</p>
 *
 *
 * @author lis70i
 * @see XmlResourceMetaDataStore
 *
 */
@XmlRootElement(name="metaDataEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlResourceMetaDataEntryThinWrapper extends
		MetaDataEntryThinWrapper {

	@XmlAttribute(required=false)
	private String name;
	
	@XmlAttribute(required=false)
	private boolean overwritable = true;
	
	@XmlAttribute(required=false)
	private boolean sharable = true;

	@XmlAttribute(required=false)
	private boolean dataSharable = false;
	
	@XmlAttribute(required=false)
	private boolean permissionSharable = false;
	
	public XmlResourceMetaDataEntryThinWrapper() {
		super();
	}

	public XmlResourceMetaDataEntryThinWrapper(RootMetaData metaData) {
		super(metaData);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
