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

package org.iplass.mtp.impl.entity.cache;

import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.cache.store.keyresolver.CacheKeyResolver;

public class QueryCacheKeyResolver implements CacheKeyResolver {
	static final String FLAG_RETURN_STRUCTURED_ENTITY = "RSE:";
	static final String FLAG_COUNT_ONLY = "CO:";

	@Override
	public String toString(Object cacheKey) {
		QueryCacheKey k = (QueryCacheKey) cacheKey;
		StringBuilder sb = new StringBuilder();
		if (k.countOnly) {
			sb.append(FLAG_COUNT_ONLY);
		}
		if (k.returnStructuredEntity) {
			sb.append(FLAG_RETURN_STRUCTURED_ENTITY);
		}
		sb.append(k.query.toString());
		return sb.toString();
	}

	@Override
	public Object toCacheKey(String cacheKeyString) {
		boolean countOnly = false;
		boolean returnStructuredEntity = false;
		if (cacheKeyString.startsWith(FLAG_COUNT_ONLY)) {
			countOnly = true;
			cacheKeyString = cacheKeyString.substring(FLAG_COUNT_ONLY.length());
		}
		if (cacheKeyString.startsWith(FLAG_RETURN_STRUCTURED_ENTITY)) {
			returnStructuredEntity = true;
			cacheKeyString = cacheKeyString.substring(FLAG_RETURN_STRUCTURED_ENTITY.length());
		}
		return new QueryCacheKey(Query.newQuery(cacheKeyString), returnStructuredEntity, countOnly);
	}

}
