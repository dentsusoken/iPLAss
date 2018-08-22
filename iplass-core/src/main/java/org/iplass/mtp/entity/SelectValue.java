/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity;

import java.io.Serializable;

/**
 * propertyが、SelectProperty型の場合、値として実際に返却されるクラス。
 *
 * @see org.iplass.mtp.entity.definition.properties.SelectProperty
 *
 * @author K.Higuchi
 *
 */
public class SelectValue implements Comparable<SelectValue>, Serializable {
	private static final long serialVersionUID = 1593232287446031856L;

	private String value;
	private String displayName;

	public SelectValue() {
	}

	public SelectValue(String value) {
		this.value = value;
	}

	public SelectValue(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return "{\"value\":\"" + value + "\"" + ",\"displayName\":\"" + displayName + "\"}";
	}


	public SelectValue copy() {
		return new SelectValue(value, displayName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		SelectValue other = (SelectValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int compareTo(SelectValue o) {
		return value.compareTo(o.getValue());
	}

}
