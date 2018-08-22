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

package org.iplass.mtp.impl.cache.store.keyresolver;

public class IntegerCacheKeyResolver implements CacheKeyResolver {

	@Override
	public String toString(Object cacheKey) {
		if (cacheKey == null) {
			return null;
		}
		return cacheKey.toString();
	}

	@Override
	public Object toCacheKey(String cacheKeyString) {
		if (cacheKeyString == null) {
			return null;
		}
		return Integer.parseInt(cacheKeyString);
	}

}
