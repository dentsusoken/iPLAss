/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.grdb;

import java.io.Serializable;

public class ColumnPosition implements Serializable, Comparable<ColumnPosition> {
	private static final long serialVersionUID = 3974568368172318817L;

	private int pageNo;
	private int columnNo;
	
	public ColumnPosition() {
	}
	
	public ColumnPosition(int pageNo, int columnNo) {
		super();
		this.pageNo = pageNo;
		this.columnNo = columnNo;
	}

	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getColumnNo() {
		return columnNo;
	}
	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnNo;
		result = prime * result + pageNo;
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
		ColumnPosition other = (ColumnPosition) obj;
		if (columnNo != other.columnNo)
			return false;
		if (pageNo != other.pageNo)
			return false;
		return true;
	}

	@Override
	public int compareTo(ColumnPosition o) {
		if (this == o) {
			return 0;
		}
		if (this.pageNo < o.pageNo) {
			return -1;
		}
		if (this.pageNo > o.pageNo) {
			return 1;
		}
		if (this.columnNo < o.columnNo) {
			return -1;
		}
		if (this.columnNo > o.columnNo) {
			return 1;
		}
		return 0;
	}

}
