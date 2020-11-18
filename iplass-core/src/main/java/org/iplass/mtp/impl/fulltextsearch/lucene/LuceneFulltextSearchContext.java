/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch.lucene;

import java.io.IOException;
import java.util.List;

import org.iplass.mtp.entity.fulltextsearch.FulltextSearchRuntimeException;
import org.iplass.mtp.impl.cache.CacheController;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.LoadingAdapter;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.event.CacheCreateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.cache.store.event.CacheInvalidateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheRemoveEvent;
import org.iplass.mtp.impl.cache.store.event.CacheUpdateEvent;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneFulltextSearchContext {
	private static Logger logger = LoggerFactory.getLogger(LuceneFulltextSearchContext.class);
	
	public static final String CACHE_NAMESPACE = "mtp.fulltextsearch.lucene.dir";

	private LuceneFulltextSearchService service;
	private CacheController<String, IndexDir> dirs;
	private int tenantId;
	
	public LuceneFulltextSearchContext(LuceneFulltextSearchService service, TenantContext tc) {
		this.service = service;
		this.tenantId = tc.getTenantId();
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		dirs = new CacheController<>(cs.getCache(CACHE_NAMESPACE + "/" + tenantId), false, 0, new IndexDirLoadingAdapter(), false, true);
		dirs.getStore().addCacheEventListenner(new IndexCacheEventListener());
	}
	
	public IndexDir getIndexDir(String defId) {
		return dirs.get(defId);
	}
	
	private void closeDir(CacheEntry ce) {
		if (ce != null) {
			IndexDir dir = (IndexDir) ce.getValue();
			if (dir != null) {
				try {
					dir.close();
				} catch (IOException e) {
					logger.warn("can not close Lucene index directory:" + dir.getDefId() + ", maybe resource leak...", e);
				}
			}
		}
	}

	public void destroy() {
		dirs.maintenance(cc -> {
			dirs.clearAll();
			dirs.invalidateCacheStore();
		});
	}
	
	public void refreshAll() {
		dirs.maintenance(cc -> {
			FulltextSearchRuntimeException ex = null;
			for (Object key: cc.getStore().keySet()) {
				CacheEntry ce = cc.getStore().get(key);
				if (ce.getValue() != null) {
					try {
						((IndexDir) ce.getValue()).refresh();
					} catch (Exception e) {
						if (ex == null) {
							ex = new FulltextSearchRuntimeException("Error occurred when refreshing searcher.", e);
						} else {
							ex.addSuppressed(e);
						}
					}
				}
			}
			if (ex != null) {
				throw ex;
			}
		});
	}
	
	private class IndexDirLoadingAdapter implements LoadingAdapter<String, IndexDir> {
		@Override
		public IndexDir load(String key) {
			return service.newIndexDir(tenantId, key);
		}

		@Override
		public List<IndexDir> loadByIndex(int indexType, Object indexVal) {
			return null;
		}

		@Override
		public long getVersion(IndexDir value) {
			return 0;
		}

		@Override
		public Object getIndexVal(int indexType, IndexDir value) {
			return null;
		}

		@Override
		public String getKey(IndexDir val) {
			return val.getDefId();
		}
		
	}
	
	private class IndexCacheEventListener implements CacheEventListener {
		@Override
		public void updated(CacheUpdateEvent event) {
		}
		
		@Override
		public void removed(CacheRemoveEvent event) {
			closeDir(event.getEntry());
		}
		
		@Override
		public void invalidated(CacheInvalidateEvent event) {
			closeDir(event.getEntry());
		}
		
		@Override
		public void created(CacheCreateEvent event) {
		}
	}

}
