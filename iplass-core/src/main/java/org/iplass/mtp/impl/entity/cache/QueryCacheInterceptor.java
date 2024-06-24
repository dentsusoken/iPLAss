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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SearchOption;
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
import org.iplass.mtp.entity.interceptor.InvocationType;
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
import org.iplass.mtp.impl.entity.interceptor.EntityInvocationImpl;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class QueryCacheInterceptor extends EntityInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(TransactionLocalQueryCacheInterceptor.class);

	private CacheService cacheService = ServiceRegistry.getRegistry().getService(CacheService.class);
	
	private CacheStore getCache(boolean withCreate, CacheScope scope) {
		switch(scope) {
		case GLOBAL_KEEP:
		case GLOBAL_RELOAD:
			//KEEP、RELOADの場合、INDEXを利用しないのでCacheStoreを分ける
			return cacheService.getCache(EntityCacheInterceptor.KEEP_QUERY_CACHE_NAMESPACE_PREFIX
					+ ExecuteContext.getCurrentContext().getClientTenantId(), withCreate);
		default:
			return cacheService.getCache(EntityCacheInterceptor.QUERY_CACHE_NAMESPACE_PREFIX
					+ ExecuteContext.getCurrentContext().getClientTenantId(), withCreate);
		}
	}

	private void notifyUpdate(EntityInvocation<?> invocation) {
		EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
		if (eh.getMetaData().isQueryCache()) {
			CacheStore cache = getCache(true, CacheScope.GLOBAL);
			cache.removeByIndex(0, eh.getMetaData().getName());
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
							CacheScope cs = ((CacheHint) h).getScope();
							if (cs == CacheScope.GLOBAL || cs == CacheScope.GLOBAL_KEEP || cs == CacheScope.GLOBAL_RELOAD) {
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
			final String[] usedEntityDefs;
			if (hint != null) {
				//クエリで利用しているEntityすべてキャッシュ可能になっているか確認
				EntityContext ec = EntityContext.getCurrentContext();
				String[] ueds = getUsedEntityDefs(q, ec);
				if (isEnableCache(ueds, ec)) {
					usedEntityDefs = ueds;
				} else {
					usedEntityDefs = null;
					logger.warn("Cant cache query, because query references non queryCache-able Entity. eql:" + q);
				}
			} else {
				usedEntityDefs = null;
			}

			if (hint != null && usedEntityDefs != null) {
				Predicate<?> callback = invocation.getPredicate();
				CacheStore store = getCache(true, hint.getScope());
				QueryCacheKey key = new QueryCacheKey(q, invocation.getSearchOption().isReturnStructuredEntity(), false);
				CacheEntry ce = store.get(key);
				if (isInvalidQueryCache(invocation, ce)) {
					if (hint.getScope() == CacheScope.GLOBAL_RELOAD) {
						ce = store.computeIfAbsentWithAutoReload(key, (k, v) -> {
							if (AuthContext.getCurrentContext().isPrivileged()) {
								return reloadQueryCache(k);
							} else {
								return AuthContext.doPrivileged(() -> {
									return reloadQueryCache(k);
								});
							}
						});
						//countOnlyのキャッシュをクリア
						store.remove(new QueryCacheKey(q, invocation.getSearchOption().isReturnStructuredEntity(), true));
					} else {
						ce = store.compute(key, (k, v) -> {
							return loadQueryCacheIfInvalid(invocation, q, hint, usedEntityDefs, v);
						});
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

	private CacheEntry reloadQueryCache(Object k) {
			Query reloadQuery = ((QueryCacheKey) k).query.copy();
			HintComment hc = reloadQuery.getSelect().getHintComment();
			CacheHint ch = null;
			for (Hint h : hc.getHintList()) {
				if (h instanceof CacheHint) {
					ch = (CacheHint) h;
					break;
				}
			}
			hc.getHintList().removeIf(h -> h instanceof CacheHint);
			
			reloadQuery.localized(false);
			SearchOption so = new SearchOption();
			so.setNotifyListeners(false);
			so.setReturnStructuredEntity(((QueryCacheKey) k).returnStructuredEntity);
			
			EntityManager em = ManagerLocator.manager(EntityManager.class);
			ArrayList<Object> list = new ArrayList<>();
			em.search(reloadQuery,so, row -> {
				list.add(row);
				return true;
			});
			QueryCache qc = new QueryCache(list.size(), list, InvocationType.SEARCH, -1);
			CacheEntry newCe = new CacheEntry(k, qc, (Object[]) null);
			newCe.setTimeToLive(TimeUnit.SECONDS.toMillis(ch.getTTL()));
			return newCe;
	}

	private CacheEntry loadQueryCacheIfInvalid(EntityQueryInvocation invocation, Query q, CacheHint hint,
			final String[] usedEntityDefs, CacheEntry v) {
		if (isInvalidQueryCache(invocation, v)) {
			ArrayList<Object> list = new ArrayList<>();
			invocation.setPredicate(dataModel -> {
				list.add(dataModel);
				return true;
			});

			invocation.proceed();

			QueryCache qc = new QueryCache(list.size(), list, invocation.getType(), hint.getTTL());
			CacheEntry newCe = new CacheEntry(new QueryCacheKey(q.copy(), invocation.getSearchOption().isReturnStructuredEntity(), false), qc, (Object) usedEntityDefs);
			if (hint.getTTL() > 0) {
				newCe.setTimeToLive(TimeUnit.SECONDS.toMillis(hint.getTTL()));
			}
			return newCe;
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Result list from global cache:" + q);
			}
			return v;
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
		final String[] usedEntityDefs;
		if (hint != null) {
			//クエリで利用しているEntityすべてキャッシュ可能になっているか確認
			EntityContext ec = EntityContext.getCurrentContext();
			String[] ueds = getUsedEntityDefs(q, ec);
			if (isEnableCache(ueds, ec)) {
				usedEntityDefs = ueds;
			} else {
				usedEntityDefs = null;
				logger.warn("Cant cache query, because query references non queryCache-able Entity. eql:" + q);
			}
		} else {
			usedEntityDefs = null;
		}

		if (hint != null && usedEntityDefs != null) {
			CacheStore store = getCache(true, hint.getScope());
			QueryCacheKey key = new QueryCacheKey(q, false, false);
			CacheEntry ce = store.get(key);
			//CacheScope.GLOBAL_RELOADの場合、countOnlyでキャッシュされている可能性も考慮
			if (ce == null && hint.getScope() == CacheScope.GLOBAL_RELOAD) {
				key = new QueryCacheKey(q, false, true);
				ce = store.get(key);
			}
			
			if (isInvalidCountCache(ce)) {
				if (hint.getScope() == CacheScope.GLOBAL_RELOAD) {
					ce = store.computeIfAbsentWithAutoReload(key, (k, v) -> {
						if (AuthContext.getCurrentContext().isPrivileged()) {
							return reloadCountCache(k);
						} else {
							return AuthContext.doPrivileged(() -> {
								return reloadCountCache(k);
							});
						}
					});
				} else {
					ce = store.compute(key, (k, v) -> {
						return loadCountCacheIfInvalid(invocation, q, hint, usedEntityDefs, v);
					});
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

	private CacheEntry reloadCountCache(Object k) {
		Query reloadQuery = ((QueryCacheKey) k).query.copy();
		HintComment hc = reloadQuery.getSelect().getHintComment();
		CacheHint ch = null;
		for (Hint h : hc.getHintList()) {
			if (h instanceof CacheHint) {
				ch = (CacheHint) h;
				break;
			}
		}
		hc.getHintList().removeIf(h -> h instanceof CacheHint);
		
		reloadQuery.localized(false);
		EntityManager em = ManagerLocator.manager(EntityManager.class);
		int count = em.count(reloadQuery);
		
		QueryCache qc = new QueryCache(count, null, InvocationType.COUNT, -1);
		CacheEntry newCe = new CacheEntry(k, qc, (Object[]) null);
		newCe.setTimeToLive(TimeUnit.SECONDS.toMillis(ch.getTTL()));
		return newCe;
	}

	private CacheEntry loadCountCacheIfInvalid(EntityCountInvocation invocation, Query q, CacheHint hint,
			final String[] usedEntityDefs, CacheEntry v) {
		if (isInvalidCountCache(v)) {
			int ret = invocation.proceed();
			QueryCache qc = new QueryCache(ret, null, invocation.getType(), hint.getTTL());
			CacheEntry newCe = new CacheEntry(new QueryCacheKey(q.copy(), false, false), qc, (Object) usedEntityDefs);
			if (hint.getTTL() > 0) {
				newCe.setTimeToLive(TimeUnit.SECONDS.toMillis(hint.getTTL()));
			}
			return newCe;
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Result list from global cache:" + q);
			}
			return v;
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
