/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.cache.store;

import java.io.Serializable;
import java.util.Arrays;

public class CacheEntry implements Serializable {
	private static final long serialVersionUID = 6317374211009297155L;

	private final Object key;
	private final Object value;
	private final long version;//楽観ロック用
	private final Object[] indexValues;//equals,hashCodeに含めない。
	private final long creationTime;
	private Long timeToLive;//equals,hashCodeに含めない。-1は無制限の意味。msで指定。

	public CacheEntry(Object key, Object value, long version, long creationTime, Object... indexValues) {
		this.key = key;
		this.value = value;
		this.version = version;
		this.creationTime = creationTime;
		this.indexValues = indexValues;
	}
	
	public CacheEntry(Object key, Object value, long version, Object... indexValues) {
		this.key = key;
		this.value = value;
		this.version = version;
		this.indexValues = indexValues;
		this.creationTime = System.currentTimeMillis();
	}

	public CacheEntry(Object key, Object value, Object... indexValues) {
		this.key = key;
		this.value = value;
		this.version = System.currentTimeMillis();
		this.indexValues = indexValues;
		this.creationTime = this.version;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public long getVersion() {
		return version;
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public Object getIndexValue(int indexKey) {
		if (indexValues == null) {
			return null;
		}
		if (indexValues.length <= indexKey) {
			return null;
		}
		return indexValues[indexKey];
	}

	public Object[] getIndexValues() {
		return indexValues;
	}

	public long getExpirationTime() {
		if (timeToLive == null || timeToLive < 0L) {
			return Long.MAX_VALUE;
		}
		long exp = creationTime + timeToLive;
		if (exp < 0) {
			//桁あふれ
			return Long.MAX_VALUE;
		}
		return exp;
	}

	public Long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(Long timeToLive) {
		this.timeToLive = timeToLive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (creationTime ^ (creationTime >>> 32));
//		result = prime * result + Arrays.deepHashCode(indexValues);
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + (int) (version ^ (version >>> 32));
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
		CacheEntry other = (CacheEntry) obj;
		if (creationTime != other.creationTime)
			return false;
//		if (!Arrays.deepEquals(indexValues, other.indexValues))
//			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CacheEntry [key=" + key + ", value=" + value + ", version=" + version + ", indexValues="
				+ Arrays.deepToString(indexValues) + ", creationTime=" + creationTime + ", timeToLive=" + timeToLive
				+ "]";
	}

}
