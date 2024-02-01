/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.i18n.MetaLocalizedString;

public abstract class BaseRootMetaData implements RootMetaData {

	private static final long serialVersionUID = -3871101646411745713L;

	/** ID */
	protected String id;

	/** 名前 */
	protected String name;

	/** 表示名 */
	protected String displayName;

	/** 表示名(多言語) */
	protected List<MetaLocalizedString> localizedDisplayNameList = new ArrayList<MetaLocalizedString>();

	/** 説明 */
	protected String description;

	final public String getId() {
		return id;
	}

	final public void setId(String id) {
		this.id = id;
	}

	final public String getName() {
		return name;
	}

	final public void setName(String name) {
		this.name = name;
	}

	final public String getDisplayName() {
		return displayName;
	}

	final public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 表示名の多言語情報を取得します。
	 * @return 表示名の多言語情報
	 */
	final public List<MetaLocalizedString> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	/**
	 * 表示名の多言語情報を設定します。
	 * @param localizedDisplayNameList 表示名の多言語情報
	 */
	final public void setLocalizedDisplayNameList(List<MetaLocalizedString> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	final public String getDescription() {
		return description;
	}

	final public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((localizedDisplayNameList == null) ? 0
						: localizedDisplayNameList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseRootMetaData other = (BaseRootMetaData) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (localizedDisplayNameList == null) {
			if (other.localizedDisplayNameList != null)
				return false;
		} else if (!localizedDisplayNameList
				.equals(other.localizedDisplayNameList))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
