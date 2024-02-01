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

	@Override
	public String toString(Object cacheKey) {
		QueryCacheKey k = (QueryCacheKey) cacheKey;
		if (k.returnStructuredEntity) {
			StringBuilder sb = new StringBuilder();
			sb.append(FLAG_RETURN_STRUCTURED_ENTITY).append(k.query.toString());
			return sb.toString();
		} else {
			return k.query.toString();
		}
	}

	@Override
	public Object toCacheKey(String cacheKeyString) {
		if (cacheKeyString.startsWith(FLAG_RETURN_STRUCTURED_ENTITY)) {
			Query q =  Query.newQuery(cacheKeyString.substring(FLAG_RETURN_STRUCTURED_ENTITY.length()));
			return new QueryCacheKey(q, true);
		} else {
			return new QueryCacheKey(Query.newQuery(cacheKeyString), false);
		}
	}

}
