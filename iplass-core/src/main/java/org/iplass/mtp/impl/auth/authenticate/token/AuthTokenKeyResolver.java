/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.token;

import org.iplass.mtp.impl.cache.store.keyresolver.CacheKeyResolver;

public class AuthTokenKeyResolver implements CacheKeyResolver {

	@Override
	public String toString(Object cacheKey) {
		AuthTokenKey key = (AuthTokenKey) cacheKey;
		StringBuilder sb = new StringBuilder();
		sb.append(key.getTenantId()).append(':').append(key.getType()).append(':').append(key.getSeries());
		return sb.toString();
	}

	@Override
	public Object toCacheKey(String cacheKeyString) {
		int index = cacheKeyString.indexOf(':');
		int tenantId = Integer.parseInt(cacheKeyString.substring(0, index));
		int index2 = cacheKeyString.indexOf(':', index + 1);
		String type = cacheKeyString.substring(index + 1, index2);
		String series = cacheKeyString.substring(index2 + 1);

		return new AuthTokenKey(tenantId, type, series);
	}

}