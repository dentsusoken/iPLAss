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

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchResult.ResultMode;
import org.iplass.mtp.entity.interceptor.EntityBulkUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityCountInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteInvocation;
import org.iplass.mtp.entity.interceptor.EntityInsertInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
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
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.interceptor.EntityInvocationImpl;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TransactionLocalQueryCacheInterceptor extends EntityInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(TransactionLocalQueryCacheInterceptor.class);

	private QueryCacheInterceptor queryCacheInterceptor;

	TransactionLocalQueryCacheInterceptor() {
		queryCacheInterceptor = new QueryCacheInterceptor();
	}

	private CacheStore getCache(boolean withCreate) {
		Transaction t = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {

			CacheStore cache = (CacheStore) t.getAttribute(EntityCacheInterceptor.LOCAL_QUERY_CACHE_NAMESPACE);

			if (withCreate && cache == null) {
				cache = ServiceRegistry.getRegistry().getService(CacheService.class).createLocalCache(EntityCacheInterceptor.LOCAL_QUERY_CACHE_NAMESPACE);
				t.setAttribute(EntityCacheInterceptor.LOCAL_QUERY_CACHE_NAMESPACE, cache);
			}
			return cache;
		}
		return null;
	}

	private void notifyUpdate(String defName) {
		CacheStore cache = getCache(false);
		if (cache != null) {
			cache.removeByIndex(0, defName);
		}
	}

	@Override
	public String insert(EntityInsertInvocation invocation) {
		String ret = queryCacheInterceptor.insert(invocation);
		notifyUpdate(invocation.getEntity().getDefinitionName());
		return ret;
	}

	@Override
	public void update(EntityUpdateInvocation invocation) {
		queryCacheInterceptor.update(invocation);
		notifyUpdate(invocation.getEntity().getDefinitionName());
	}

	@Override
	public void delete(EntityDeleteInvocation invocation) {
		queryCacheInterceptor.delete(invocation);
		notifyUpdate(invocation.getEntity().getDefinitionName());
	}

	private boolean hasCacheTransactionHint(Query q) {
		if (q.getSelect() != null && q.getSelect().getHintComment() != null) {
			HintComment hc = q.getSelect().getHintComment();
			if (hc.getHintList() != null) {
				for (Hint h: hc.getHintList()) {
					if (h instanceof CacheHint) {
						if (((CacheHint) h).getScope() == CacheScope.TRANSACTION) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private String[] getUsedEntityDefs(Query q) {
		DefNameCollector collector = new DefNameCollector(EntityContext.getCurrentContext());
		q.accept(collector);
		return collector.getDefNames();
	}

	@Override
	public void query(EntityQueryInvocation invocation) {
		if (invocation.getSearchOption().getResultMode() == ResultMode.STREAM) {
			queryCacheInterceptor.query(invocation);
		} else {
			Query q = invocation.getQuery();
			CacheStore store = null;
			if (hasCacheTransactionHint(q) && (store = getCache(true)) != null) {
				Predicate<?> callback = invocation.getPredicate();
				CacheEntry ce = store.get(q);
				if (ce == null
						|| ce.getValue() == null
						|| !((QueryCache) ce.getValue()).canIterate(invocation.getType())) {
					ArrayList<Object> list = new ArrayList<>();
					invocation.setPredicate(dataModel -> {
						list.add(dataModel);
						return true;
					});
					queryCacheInterceptor.query(invocation);
					QueryCache qc = new QueryCache(list.size(), list, invocation.getType());
					q = q.copy();
					ce = new CacheEntry(q, qc, (Object) getUsedEntityDefs(q));
					store.put(ce, true);
				} else {
					if (logger.isTraceEnabled()) {
						logger.trace("Result list from transaction local cache:" + q);
					}
				}

				((QueryCache) ce.getValue()).iterate(callback, q, invocation.getType(), ((EntityInvocationImpl<?>) invocation).getEntityHandler());

			} else {
				queryCacheInterceptor.query(invocation);
			}
		}
	}

	@Override
	public int count(EntityCountInvocation invocation) {
		Query q = invocation.getQuery();
		CacheStore store = null;
		if (hasCacheTransactionHint(q) && (store = getCache(true)) != null) {
			CacheEntry ce = store.get(q);
			if (ce == null
					|| ce.getValue() == null
					|| ((QueryCache) ce.getValue()).getCount() == null) {
				int ret = queryCacheInterceptor.count(invocation);
				QueryCache qc = new QueryCache(ret, null, invocation.getType());
				q = q.copy();
				ce = new CacheEntry(q, qc, (Object) getUsedEntityDefs(q));
				store.put(ce, true);
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("Count from transaction local cache:" + q);
				}
			}

			return ((QueryCache) ce.getValue()).getCount();

		} else {
			return queryCacheInterceptor.count(invocation);
		}
	}

	@Override
	public int updateAll(EntityUpdateAllInvocation invocation) {
		int ret = queryCacheInterceptor.updateAll(invocation);
		notifyUpdate(invocation.getUpdateCondition().getDefinitionName());
		return ret;
	}

	@Override
	public void bulkUpdate(EntityBulkUpdateInvocation invocation) {
		queryCacheInterceptor.bulkUpdate(invocation);
		notifyUpdate(invocation.getBulkUpdatable().getDefinitionName());
	}

	@Override
	public int deleteAll(EntityDeleteAllInvocation invocation) {
		int ret = queryCacheInterceptor.deleteAll(invocation);
		notifyUpdate(invocation.getDeleteCondition().getDefinitionName());
		return ret;
	}

	@Override
	public void purge(EntityPurgeInvocation invocation) {
		queryCacheInterceptor.purge(invocation);
	}

	@Override
	public Entity restore(EntityRestoreInvocation invocation) {
		Entity ret = queryCacheInterceptor.restore(invocation);
		notifyUpdate(invocation.getEntityDefinition().getName());
		return ret;
	}

	@Override
	public boolean lockByUser(EntityLockByUserInvocation invocation) {
		boolean ret = queryCacheInterceptor.lockByUser(invocation);
		notifyUpdate(invocation.getEntityDefinition().getName());
		return ret;
	}

	@Override
	public boolean unlockByUser(EntityUnlockByUserInvocation invocation) {
		boolean ret = queryCacheInterceptor.unlockByUser(invocation);
		notifyUpdate(invocation.getEntityDefinition().getName());
		return ret;
	}

}
