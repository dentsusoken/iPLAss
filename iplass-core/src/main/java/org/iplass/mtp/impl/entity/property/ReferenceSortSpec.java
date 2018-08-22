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

package org.iplass.mtp.impl.entity.property;

import org.iplass.mtp.impl.metadata.MetaData;

public class ReferenceSortSpec implements MetaData {
	private static final long serialVersionUID = -5734891785217631412L;
	
	public enum SortType {
		ASC,DESC
	}
	
	public ReferenceSortSpec() {
	}
	public ReferenceSortSpec(String sortPropertyMetaDataId, SortType sortType) {
		this.sortPropertyMetaDataId = sortPropertyMetaDataId;
		this.sortType = sortType;
	}
	
	private String sortPropertyMetaDataId;
	private SortType sortType;
	
	public String getSortPropertyMetaDataId() {
		return sortPropertyMetaDataId;
	}
	public void setSortPropertyMetaDataId(String sortPropertyMetaDataId) {
		this.sortPropertyMetaDataId = sortPropertyMetaDataId;
	}
	public SortType getSortType() {
		return sortType;
	}
	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}

	@Override
	public ReferenceSortSpec copy() {
		return new ReferenceSortSpec(sortPropertyMetaDataId, sortType);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((sortPropertyMetaDataId == null) ? 0
						: sortPropertyMetaDataId.hashCode());
		result = prime * result
				+ ((sortType == null) ? 0 : sortType.hashCode());
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
		ReferenceSortSpec other = (ReferenceSortSpec) obj;
		if (sortPropertyMetaDataId == null) {
			if (other.sortPropertyMetaDataId != null)
				return false;
		} else if (!sortPropertyMetaDataId.equals(other.sortPropertyMetaDataId))
			return false;
		if (sortType == null) {
			if (other.sortType != null)
				return false;
		} else if (!sortType.equals(other.sortType))
			return false;
		return true;
	}
	
	

}
