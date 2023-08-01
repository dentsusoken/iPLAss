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
package org.iplass.mtp.impl.entity.cache;

import java.util.ArrayList;
import java.util.function.Predicate;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchResult.ResultMode;
import org.iplass.mtp.entity.interceptor.EntityBulkUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityCountInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteInvocation;
import org.iplass.mtp.entity.interceptor.EntityInsertInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
import org.iplass.mtp.entity.interceptor.EntityInvocation;
import org.iplass.mtp.entity.interceptor.EntityLockByUserInvocation;
import org.iplass.mtp.entity.interceptor.EntityPurgeInvocation;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.EntityRestoreInvocation;
import org.iplass.mtp.entity.interceptor.EntityUnlockByUserInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateInvocation;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.hint.CacheHint;
import org.iplass.mtp.entity.query.hint.CacheHint.CacheScope;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.HintComment;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.interceptor.EntityCountInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityQueryInvocationImpl;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class QueryCacheInterceptor extends EntityInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(TransactionLocalQueryCacheInterceptor.class);

	private CacheService cacheService = ServiceRegistry.getRegistry().getService(CacheService.class);

	private CacheStore getCache(boolean withCreate) {
		return cacheService.getCache(EntityCacheInterceptor.QUERY_CACHE_NAMESPACE_PREFIX + ExecuteContext.getCurrentContext().getClientTenantId(), withCreate);
	}

	private void notifyUpdate(EntityInvocation<?> invocation) {
		EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
		if (eh.getMetaData().isQueryCache()) {
			CacheStore cache = getCache(true);
			synchronized(eh) {
				cache.removeByIndex(0, eh.getMetaData().getName());
			}
		}
	}

	@Override
	public String insert(EntityInsertInvocation invocation) {
		String ret = invocation.proceed();
		notifyUpdate(invocation);
		return ret;
	}

	@Override
	public void update(EntityUpdateInvocation invocation) {
		invocation.proceed();
		notifyUpdate(invocation);
	}

	@Override
	public void delete(EntityDeleteInvocation invocation) {
		invocation.proceed();
		notifyUpdate(invocation);
	}

	private CacheHint hasCacheHintAndEnableCache(Query q, EntityInvocation<?> inv) {
		EntityHandler primaryEh = ((EntityInvocationImpl<?>) inv).getEntityHandler();
		if (primaryEh != null && primaryEh.getMetaData().isQueryCache()) {
			if (q.getSelect() != null && q.getSelect().getHintComment() != null) {
				HintComment hc = q.getSelect().getHintComment();
				if (hc.getHintList() != null) {
					for (Hint h: hc.getHintList()) {
						if (h instanceof CacheHint) {
							if (((CacheHint) h).getScope() == CacheScope.GLOBAL) {
								return (CacheHint) h;
							}
						}
					}
				}
			}
		}

		return null;
	}

	private boolean isEnableCache(String[] defNameList, EntityContext ec) {
		if (defNameList == null || defNameList.length == 0) {
			return false;
		}
		for (String defName: defNameList) {
			EntityHandler eh = ec.getHandlerByName(defName);
			if (eh == null) {
				return false;
			}
			if (!eh.getMetaData().isQueryCache()) {
				return false;
			}
		}
		return true;
	}

	private String[] getUsedEntityDefs(Query q, EntityContext ec) {
		DefNameCollector collector = new DefNameCollector(ec);
		q.accept(collector);
		return collector.getDefNames();
	}

	@Override
	public void query(EntityQueryInvocation invocation) {
		if (invocation.getSearchOption().getResultMode() == ResultMode.STREAM) {
			invocation.proceed();
		} else {
			Query q = invocation.getQuery();
			CacheHint hint = hasCacheHintAndEnableCache(q, invocation);
			String[] usedEntityDefs = null;
			if (hint != null) {
				//クエリで利用しているEntityすべてキャッシュ可能になっているか確認
				EntityContext ec = EntityContext.getCurrentContext();
				String[] ueds = getUsedEntityDefs(q, ec);
				if (isEnableCache(ueds, ec)) {
					usedEntityDefs = ueds;
				} else {
					logger.warn("Cant cache query, because query references non queryCache-able Entity. eql:" + q);
				}
			}

			if (hint != null && usedEntityDefs != null) {
				Predicate<?> callback = invocation.getPredicate();
				CacheStore store = getCache(true);
				CacheEntry ce = store.get(new QueryCacheKey(q, invocation.getSearchOption().isReturnStructuredEntity()));
				if (isInvalidQueryCache(invocation, ce)) {
					//同一クエリーの検索がバックエンドに発行されないように。（結局はEntity定義単位でロックが必要となるのでEntityHandlerでロック）
					synchronized(((EntityQueryInvocationImpl) invocation).getEntityHandler()) {
						ce = store.get(new QueryCacheKey(q, invocation.getSearchOption().isReturnStructuredEntity()));
						if (isInvalidQueryCache(invocation, ce)) {
							ArrayList<Object> list = new ArrayList<>();
							invocation.setPredicate(dataModel -> {
								list.add(dataModel);
								return true;
							});

							invocation.proceed();

							QueryCache qc = new QueryCache(list.size(), list, invocation.getType(), hint.getTTL());
							q = q.copy();
							ce = new CacheEntry(new QueryCacheKey(q, invocation.getSearchOption().isReturnStructuredEntity()), qc, (Object) usedEntityDefs);
							store.put(ce, true);
						} else {
							if (logger.isTraceEnabled()) {
								logger.trace("Result list from global cache:" + q);
							}
						}
					}
				} else {
					if (logger.isTraceEnabled()) {
						logger.trace("Result list from global cache:" + q);
					}
				}

				((QueryCache) ce.getValue()).iterate(callback, q, invocation.getType(), ((EntityInvocationImpl<?>) invocation).getEntityHandler());

			} else {
				invocation.proceed();
			}
		}
	}

	private boolean isInvalidQueryCache(EntityQueryInvocation invocation, CacheEntry ce) {
		return ce == null
				|| ce.getValue() == null
				|| !((QueryCache) ce.getValue()).canIterate(invocation.getType())
				|| ((QueryCache) ce.getValue()).eol();
	}

	private boolean isInvalidCountCache(CacheEntry ce) {
		return ce == null
				|| ce.getValue() == null
				|| ((QueryCache) ce.getValue()).getCount() == null
				|| ((QueryCache) ce.getValue()).eol();
	}

	@Override
	public int count(EntityCountInvocation invocation) {
		Query q = invocation.getQuery();
		CacheHint hint = hasCacheHintAndEnableCache(q, invocation);
		String[] usedEntityDefs = null;
		if (hint != null) {
			//クエリで利用しているEntityすべてキャッシュ可能になっているか確認
			EntityContext ec = EntityContext.getCurrentContext();
			String[] ueds = getUsedEntityDefs(q, ec);
			if (isEnableCache(ueds, ec)) {
				usedEntityDefs = ueds;
			} else {
				logger.warn("Cant cache query, because query references non queryCache-able Entity. eql:" + q);
			}
		}

		if (hint != null && usedEntityDefs != null) {
			CacheStore store = getCache(true);
			CacheEntry ce = store.get(new QueryCacheKey(q, false));
			if (isInvalidCountCache(ce)) {
				//同一クエリーの検索がバックエンドに発行されないように。（結局はEntity定義単位でロックが必要となるのでEntityHandlerでロック）
				synchronized(((EntityCountInvocationImpl) invocation).getEntityHandler()) {
					ce = store.get(new QueryCacheKey(q, false));
					if (isInvalidCountCache(ce)) {
						int ret = invocation.proceed();
						QueryCache qc = new QueryCache(ret, null, invocation.getType(), hint.getTTL());
						q = q.copy();
						ce = new CacheEntry(new QueryCacheKey(q, false), qc, (Object) usedEntityDefs);
						store.put(ce, true);
					} else {
						if (logger.isTraceEnabled()) {
							logger.trace("Result list from global cache:" + q);
						}
					}
				}
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("Result list from global cache:" + q);
				}
			}

			return ((QueryCache) ce.getValue()).getCount();

		} else {
			return invocation.proceed();
		}
	}


	@Override
	public int updateAll(EntityUpdateAllInvocation invocation) {
		int ret = invocation.proceed();
		notifyUpdate(invocation);
		return ret;
	}

	@Override
	public void bulkUpdate(EntityBulkUpdateInvocation invocation) {
		invocation.proceed();
		notifyUpdate(invocation);
	}

	@Override
	public int deleteAll(EntityDeleteAllInvocation invocation) {
		int ret = invocation.proceed();
		notifyUpdate(invocation);
		return ret;
	}

	@Override
	public void purge(EntityPurgeInvocation invocation) {
		invocation.proceed();
	}

	@Override
	public Entity restore(EntityRestoreInvocation invocation) {
		Entity ret = invocation.proceed();
		notifyUpdate(invocation);
		return ret;
	}

	@Override
	public boolean lockByUser(EntityLockByUserInvocation invocation) {
		boolean ret = invocation.proceed();
		notifyUpdate(invocation);
		return ret;
	}

	@Override
	public boolean unlockByUser(EntityUnlockByUserInvocation invocation) {
		boolean ret = invocation.proceed();
		notifyUpdate(invocation);
		return ret;
	}
}
