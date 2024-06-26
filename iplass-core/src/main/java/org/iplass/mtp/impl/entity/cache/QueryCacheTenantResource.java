/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantResource;
import org.iplass.mtp.spi.ServiceRegistry;

public class QueryCacheTenantResource implements TenantResource {
	private int tenantId;

	@Override
	public void init(TenantContext tenantContext) {
		this.tenantId = tenantContext.getTenantId();
	}

	@Override
	public void destory() {
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		cs.invalidate(EntityCacheInterceptor.QUERY_CACHE_NAMESPACE_PREFIX + tenantId);
		cs.invalidate(EntityCacheInterceptor.KEEP_QUERY_CACHE_NAMESPACE_PREFIX + tenantId);
	}

}
