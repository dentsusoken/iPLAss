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

package org.iplass.mtp.impl.entity;

import org.iplass.mtp.impl.metadata.MetaData;

public class MetaEntityMapping implements MetaData {
	private static final long serialVersionUID = 3653948484919058222L;
	
	private String mappingClass;
	
	public MetaEntityMapping() {
	}
	
	public MetaEntityMapping(String mappingClass) {
		this.mappingClass = mappingClass;
	}

	public String getMappingClass() {
		return mappingClass;
	}

	public void setMappingClass(String mappingClass) {
		this.mappingClass = mappingClass;
	}

	@Override
	public MetaEntityMapping copy() {
		return new MetaEntityMapping(mappingClass);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mappingClass == null) ? 0 : mappingClass.hashCode());
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
		MetaEntityMapping other = (MetaEntityMapping) obj;
		if (mappingClass == null) {
			if (other.mappingClass != null)
				return false;
		} else if (!mappingClass.equals(other.mappingClass))
			return false;
		return true;
	}

}
