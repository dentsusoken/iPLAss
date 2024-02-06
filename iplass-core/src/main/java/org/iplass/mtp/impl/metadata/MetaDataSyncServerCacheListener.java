/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata;

import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.builtin.SyncServerCacheEventListener;
import org.iplass.mtp.spi.ServiceRegistry;

public class MetaDataSyncServerCacheListener implements
		SyncServerCacheEventListener {

	@Override
	public void removeAll(String namespace) {
//		String listCacheNs = namespace.replace(MetaDataContext.METADATA_CACHE_NAMESPACE, MetaDataContext.METADATA_LIST_CACHE_NAMESPACE);
//		CacheStore listCache = ServiceRegistry.getRegistry().getService(CacheService.class).getCache(listCacheNs, false);
//		if (listCache != null) {
//			listCache.removeAll();
//		}
		
		String defListCacheNs = namespace.replace(MetaDataContext.METADATA_CACHE_NAMESPACE, MetaDataContext.METADATA_DEF_LIST_CACHE_NAMESPACE);
		CacheStore defListCache = ServiceRegistry.getRegistry().getService(CacheService.class).getCache(defListCacheNs, false);
		if (defListCache != null) {
			defListCache.removeAll();
		}
	}
	
	@Override
	public void markDirty(String namespace, CacheEntry entry) {
//		String listCacheNs = namespace.replace(MetaDataContext.METADATA_CACHE_NAMESPACE, MetaDataContext.METADATA_LIST_CACHE_NAMESPACE);
//		CacheStore listCache = ServiceRegistry.getRegistry().getService(CacheService.class).getCache(listCacheNs, false);
		String defListCacheNs = namespace.replace(MetaDataContext.METADATA_CACHE_NAMESPACE, MetaDataContext.METADATA_DEF_LIST_CACHE_NAMESPACE);
		CacheStore defListCache = ServiceRegistry.getRegistry().getService(CacheService.class).getCache(defListCacheNs, false);
		String listPath = (String) entry.getIndexValue(0);
		if (listPath != null) {
			if (!listPath.endsWith("/")) {
				listPath = listPath + "/";
			}
			while(listPath.length() != 0) {
				listPath = listPath.substring(0, listPath.lastIndexOf('/'));
				String pathWithSlash = listPath + "/";
//				if (listCache != null) {
//					listCache.invalidate(new CacheEntry(pathWithSlash, null));
//				}
				if (defListCache != null) {
					defListCache.remove(pathWithSlash);
				}
			}
			
		}
	}

}
