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

package org.iplass.mtp.impl.web.actionmapping.cache;

import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantResource;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataContextListener;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping.ActionMappingRuntime;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaCacheCriteria.CacheCriteriaRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.actionmapping.definition.cache.RelatedEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActionMappingのキャッシュ
 *
 * @author K.Higuchi
 *
 */
public class ContentCacheContext implements TenantResource {

	public static final String ACTION_CONTENT_CACHE_NAMESPACE = "mtp.action.content";

	public static final String HOLE_ENTITY = "*";

	private static Logger logger = LoggerFactory.getLogger(ContentCacheContext.class);

	public static final ContentCacheContext getContentCacheContext() {
		return ExecuteContext.getCurrentContext().getTenantContext().getResource(ContentCacheContext.class);
	}

	//pk: actionName;key
	//index 0: actionName
	//index 1: entityName;oid / entityName;*
	//index 2: entityName
	private CacheStore contentCacheStore;

	//Action名＋パラメータをキーにした1件取得（pk）
	//Action名をキーにした削除
	//Entity名＋OIDをキーにした削除
	//Entity名をキーにした削除
	//indexとしては、3つ
	
	private MetaDataContext metaDataContext;
	private MetaDataContextListener metaDatalistener = new MetaDataContextListener() {

		@Override
		public void updated(String path, String pathBefore) {
			if (path.startsWith(ActionMappingService.ACTION_MAPPING_META_PATH)) {
				String actionName = path.substring(ActionMappingService.ACTION_MAPPING_META_PATH.length());
				invalidateByActionName(actionName);
			} else if (path.startsWith(EntityService.ENTITY_META_PATH)) {
				String entityName = path.substring(EntityService.ENTITY_META_PATH.length()).replace('/', '.');
				invalidateByEntityName(entityName);
			}
			if (pathBefore != null) {
				if (pathBefore.startsWith(ActionMappingService.ACTION_MAPPING_META_PATH)) {
					String actionName = pathBefore.substring(ActionMappingService.ACTION_MAPPING_META_PATH.length());
					invalidateByActionName(actionName);
				} else if (pathBefore.startsWith(EntityService.ENTITY_META_PATH)) {
					String entityName = pathBefore.substring(EntityService.ENTITY_META_PATH.length()).replace('/', '.');
					invalidateByEntityName(entityName);
				}
			}
		}

		@Override
		public void removed(String path) {
			if (path.startsWith(ActionMappingService.ACTION_MAPPING_META_PATH)) {
				String actionName = path.substring(ActionMappingService.ACTION_MAPPING_META_PATH.length());
				invalidateByActionName(actionName);
			} else if (path.startsWith(EntityService.ENTITY_META_PATH)) {
				String entityName = path.substring(EntityService.ENTITY_META_PATH.length());
				invalidateByEntityName(entityName);
			}
		}

		@Override
		public void created(String path) {
		}
	};

	public ContentCacheContext() {
	}
	
	@Override
	public void init(TenantContext tenantContext) {
		int tenantId = tenantContext.getTenantId();
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		contentCacheStore = cs.getCache(ACTION_CONTENT_CACHE_NAMESPACE + "/" + tenantId);
		metaDataContext = tenantContext.getMetaDataContext();
		metaDataContext.addMetaDataContextListener(metaDatalistener);
	}

	@Override
	public void destory() {
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		cs.invalidate(contentCacheStore.getNamespace());
		metaDataContext.removeMetaDataContextListener(metaDatalistener);
		if (logger.isTraceEnabled()) {
			logger.trace("invalidate all action cache.");
		}
	}

	public ContentCache get(String actionName, String lang, String key) {
		String cacheKey = genKey(actionName, lang, key);
		CacheEntry ce = contentCacheStore.get(cacheKey);
		if (ce != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("hits action cache:" + cacheKey);
			}

			ContentCache cc = (ContentCache) ce.getValue();
			if (cc.getExpires() < System.currentTimeMillis()) {
				invalidate(cacheKey);
				return null;
			}

			return (ContentCache) ce.getValue();
		} else {
			return null;
		}
	}

	public void put(String actionName, String lang, String key, ContentCache contentCache) {
		String cacheKey = genKey(actionName, lang, key);

		if (logger.isTraceEnabled()) {
			logger.trace("put action cache:" + cacheKey);
		}
		
		String[] entityNameAndOidArray = null;
		if (contentCache.getRelatedEntityNameAndOid() != null) {
			entityNameAndOidArray = contentCache.getRelatedEntityNameAndOid().toArray(new String[contentCache.getRelatedEntityNameAndOid().size()]);
		}
		String[] entityNameArray = null;
		if (contentCache.getRelatedEntityName() != null) {
			entityNameArray = contentCache.getRelatedEntityName().toArray(new String[contentCache.getRelatedEntityName().size()]);
		}

		contentCacheStore.put(new CacheEntry(cacheKey, contentCache, contentCache.getCreationTime(), new Object[]{actionName, entityNameAndOidArray, entityNameArray}), true);
	}

	private ActionMappingRuntime getAction(WebRequestStack stack) {
		if (stack == null) {
			return null;
		}
		ActionMappingRuntime amr = (ActionMappingRuntime) stack.getAttribute(CachableHttpServletResponse.ACTION_RUNTIME_NAME);
		if (amr == null) {
			return getAction(stack.getPrevStack());
		} else {
			return amr;
		}
	}

	public boolean isRecordEntity(String entityName) {
		WebRequestStack stack = WebRequestStack.getCurrent();
		if (stack != null && stack.getResponse() instanceof CachableHttpServletResponse) {
			CachableHttpServletResponse res = (CachableHttpServletResponse) stack.getResponse();
			ContentCache cc = res.getCurrentContentCache();
			if (cc != null) {
				CacheCriteriaRuntime ccr = getAction(stack).getCacheCriteria();
				RelatedEntityType type = ccr.checkRelatedEntity(entityName);
				if (type != null) {
					return true;
				}
			}
		}
		return false;
	}

	public void record(String entityName, String oid, Long expires) {

		WebRequestStack stack = WebRequestStack.getCurrent();
		if (stack != null && stack.getResponse() instanceof CachableHttpServletResponse) {
			CachableHttpServletResponse res = (CachableHttpServletResponse) stack.getResponse();
			ContentCache cc = res.getCurrentContentCache();
			if (cc != null) {
				CacheCriteriaRuntime ccr = getAction(stack).getCacheCriteria();
				RelatedEntityType type = ccr.checkRelatedEntity(entityName);
				if (type != null) {
					if (type == RelatedEntityType.WHOLE) {
						cc.addRelatedEntity(entityName, HOLE_ENTITY);
					} else if (!HOLE_ENTITY.equals(oid)) {
						cc.addRelatedEntity(entityName, oid);
					}
				}

				//時間ベースバージョン管理の場合、有効期間をexpireに設定。
				if (expires != null) {
					if (cc.getExpires() > expires.longValue()) {
						cc.setExpires(expires.longValue());
					}
				}
			}
		}
	}

	public void recordForce(RelatedEntityType type, String entityName, String oid) {
		WebRequestStack stack = WebRequestStack.getCurrent();
		if (stack != null && stack.getResponse() instanceof CachableHttpServletResponse) {
			CachableHttpServletResponse res = (CachableHttpServletResponse) stack.getResponse();
			ContentCache cc = res.getCurrentContentCache();
			if (cc != null) {
				if (type == RelatedEntityType.WHOLE) {
					cc.addRelatedEntity(entityName, HOLE_ENTITY);
				} else if (type == RelatedEntityType.SPECIFIC_ID && oid != null) {
					cc.addRelatedEntity(entityName, oid);
				}
			}
		}
	}

	public void setCacheExpires(long expires) {
		WebRequestStack stack = WebRequestStack.getCurrent();
		if (stack != null && stack.getResponse() instanceof CachableHttpServletResponse) {
			CachableHttpServletResponse res = (CachableHttpServletResponse) stack.getResponse();
			ContentCache cc = res.getCurrentContentCache();
			if (cc != null) {
				//時間ベースバージョン管理の場合、有効期間をexpireに設定。
				if (cc.getExpires() > expires) {
					cc.setExpires(expires);
				}
			}
		}
	}
	
	private String genKey(String actionName, String lang, String key) {
		return actionName + ";" + lang + ";" + key;
	}

	public void invalidate(String actionName, String lang, String key) {
		invalidate(genKey(actionName, lang, key));
	}

	private void invalidate(String cacheKey) {
		contentCacheStore.remove(cacheKey);
		if (logger.isTraceEnabled()) {
			logger.trace("invalidate action cache:" + cacheKey);
		}
	}
	
	public void invalidateByActionName(String actionName) {
		contentCacheStore.removeByIndex(0, actionName);
		if (logger.isTraceEnabled()) {
			logger.trace("invalidate action cache:" + actionName);
		}
	}

	public void invalidateByEntityName(String entityName) {
		contentCacheStore.removeByIndex(2, entityName);
		if (logger.isTraceEnabled()) {
			logger.trace("invalidate action cache by entityName:" + entityName);
		}
	}

	public void invalidateByEntityNameAndOid(String entityName, String oid) {
		contentCacheStore.removeByIndex(1, entityName + ";" + oid);
		if (logger.isTraceEnabled()) {
			logger.trace("invalidate action cache by entityName:" + entityName + ";" + oid);
		}
	}
	
	public void invalidateAllEntry() {
		contentCacheStore.removeAll();
		if (logger.isTraceEnabled()) {
			logger.trace("invalidate all action cache.");
		}
	}

}
