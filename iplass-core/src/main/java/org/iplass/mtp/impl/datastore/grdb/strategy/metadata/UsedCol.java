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
package org.iplass.mtp.impl.datastore.grdb.strategy.metadata;

import org.iplass.mtp.impl.datastore.grdb.ColumnPosition;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.RawColIndexType;
import org.iplass.mtp.impl.datastore.grdb.RawColType;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;

public class UsedCol implements Comparable<UsedCol> {
	
	private final RawColType rawColType;
	private final RawColIndexType rawColIndexType;
	private final ColumnPosition position;
	
	private final MetaGRdbPropertyStore col;
	private final MetaPrimitiveProperty property;
	
	public UsedCol(RawColType rawColType, RawColIndexType rawColIndexType, ColumnPosition position) {
		this.rawColType = rawColType;
		this.rawColIndexType = rawColIndexType;
		this.position = position;
		this.col = null;
		this.property = null;
	}
	
	public UsedCol(RawColType rawColType, RawColIndexType rawColIndexType, ColumnPosition position, MetaGRdbPropertyStore col,
			MetaPrimitiveProperty property) {
		this.rawColType = rawColType;
		this.rawColIndexType = rawColIndexType;
		this.position = position;
		this.col = col;
		this.property = property;
	}
	
	public RawColType getRawColType() {
		return rawColType;
	}

	public RawColIndexType getRawColIndexType() {
		return rawColIndexType;
	}

	public ColumnPosition getPosition() {
		return position;
	}

	public MetaGRdbPropertyStore getCol() {
		return col;
	}

	public MetaPrimitiveProperty getProperty() {
		return property;
	}

	@Override
	public int compareTo(UsedCol o) {
		int ret = rawColType.ordinal() - o.rawColType.ordinal();
		if (ret != 0) {
			return ret;
		}
		ret = rawColIndexType.ordinal() - o.rawColIndexType.ordinal();
		if (ret != 0) {
			return ret;
		}
		return -position.compareTo(o.position);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result
				+ ((rawColIndexType == null) ? 0 : rawColIndexType.hashCode());
		result = prime * result
				+ ((rawColType == null) ? 0 : rawColType.hashCode());
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
		UsedCol other = (UsedCol) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (rawColIndexType != other.rawColIndexType)
			return false;
		if (rawColType != other.rawColType)
			return false;
		return true;
	}
	
}
