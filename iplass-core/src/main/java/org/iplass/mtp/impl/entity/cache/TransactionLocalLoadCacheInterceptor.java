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
import java.util.HashSet;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.interceptor.EntityBulkUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityCountInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteInvocation;
import org.iplass.mtp.entity.interceptor.EntityInsertInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
import org.iplass.mtp.entity.interceptor.EntityInvocation;
import org.iplass.mtp.entity.interceptor.EntityLoadInvocation;
import org.iplass.mtp.entity.interceptor.EntityLockByUserInvocation;
import org.iplass.mtp.entity.interceptor.EntityPurgeInvocation;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.EntityRestoreInvocation;
import org.iplass.mtp.entity.interceptor.EntityUnlockByUserInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateInvocation;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.builtin.NullCacheStore;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.auth.EntityQueryAuthContextHolder;
import org.iplass.mtp.impl.entity.interceptor.EntityLoadInvocationImpl;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class TransactionLocalLoadCacheInterceptor extends EntityInterceptorAdapter {

	private static final NullCacheStore NULL_CACHE = new NullCacheStore(EntityCacheInterceptor.LOCAL_LOAD_CACHE_NAMESPACE, null);

	private static Logger logger = LoggerFactory.getLogger(TransactionLocalLoadCacheInterceptor.class);

	private TransactionLocalQueryCacheInterceptor transactionLocalQueryCacheInterceptor;

	TransactionLocalLoadCacheInterceptor() {
		transactionLocalQueryCacheInterceptor = new TransactionLocalQueryCacheInterceptor();
	}

	@Override
	public String insert(EntityInsertInvocation invocation) {
		String res = transactionLocalQueryCacheInterceptor.insert(invocation);
		//ネガティブキャッシュしているので
		CacheStore cache = getCache();
		cache.remove(crateCacheKey(invocation, invocation.getEntity().getOid(), invocation.getEntity().getVersion()));
		if (invocation.getEntity().getVersion() != null) {//バージョン未指定時のキャッシュも削除
			cache.remove(crateCacheKey(invocation, invocation.getEntity().getOid(), null));
		}
		return res;
	}

	private boolean isNameUpdate(EntityUpdateInvocation invocation) {
		if (invocation.getUpdateOption().getUpdateProperties().contains(Entity.NAME)) {
			return true;
		}
		//namePropertyが指定されている場合
		String nameProperty = invocation.getEntityDefinition().getNamePropertyName();
		if (nameProperty != null
				&& invocation.getUpdateOption().getUpdateProperties().contains(nameProperty)) {
			return true;
		}

		return false;
	}

	@Override
	public void update(EntityUpdateInvocation invocation) {
		transactionLocalQueryCacheInterceptor.update(invocation);
		CacheStore cache = getCache();
		CacheKey updateKey = crateCacheKey(invocation, invocation.getEntity().getOid(), invocation.getEntity().getVersion());
		cache.remove(updateKey);
		if (invocation.getEntity().getVersion() != null) {
			//バージョン未指定時のキャッシュも削除
			cache.remove(crateCacheKey(invocation, invocation.getEntity().getOid(), null));
		}

		//このEntityを参照しているEntityがあった場合、nameを更新した際、当該のEntityのnameを更新する必要あり
		if (isNameUpdate(invocation)) {
			ArrayList<CacheKey> keyList = new ArrayList<CacheKey>();
			for (Object o: cache.keySet()) {
				CacheKey key = (CacheKey) o;
				CacheEntry val = cache.get(key);
				EntityCacheEntry ce = null;
				if (val != null) {
					ce = (EntityCacheEntry) val.getValue();
				}
				if (ce != null && ce.isRef(updateKey)) {
					keyList.add(key);
				}
			}
			if (keyList.size() > 0) {
				for(CacheKey key: keyList) {
					cache.remove(key);
				}
			}
		}
	}

	@Override
	public void delete(EntityDeleteInvocation invocation) {
		transactionLocalQueryCacheInterceptor.delete(invocation);
		CacheStore cache = getCache();
		cache.remove(crateCacheKey(invocation, invocation.getEntity().getOid(), invocation.getEntity().getVersion()));
		if (invocation.getEntity().getVersion() != null) {//バージョン未指定時のキャッシュも削除
			cache.remove(crateCacheKey(invocation, invocation.getEntity().getOid(), null));
		}
	}

	@Override
	public boolean lockByUser(EntityLockByUserInvocation invocation) {
		boolean res = transactionLocalQueryCacheInterceptor.lockByUser(invocation);
		if (res) {
			CacheStore cache = getCache();
			ArrayList<CacheKey> keyList = new ArrayList<CacheKey>();
			for (Object o: cache.keySet()) {
				CacheKey key = (CacheKey) o;
				if (key.getTenantId() == ExecuteContext.getCurrentContext().getClientTenantId()
						&& key.getDefitionName().equals(invocation.getEntityDefinition().getName())
						&& key.getOid().equals(invocation.getOid())) {
					keyList.add(key);
				}
			}
			if (keyList.size() > 0) {
				for(CacheKey key: keyList) {
					cache.remove(key);
				}
			}
//
//			setLockByUser(invocation.getEntityDefinition().getName(), invocation.getOid(), invocation.getUserId());
		}
		return res;
	}

//	private void setLockByUser(String defName, String oid, String userId) {
//		Transaction t = ServiceLocator.getInstance().getTransactionManager().currentTransaction();
//		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
//
//			HashMap<String, String> cache = (HashMap<String, String>) t.getAttribute(LOCK_CACHE_KEY);
//			if (cache == null) {
//				cache = new HashMap<String, String>();
//				t.setAttribute(LOCK_CACHE_KEY, cache);
//			}
//			cache.put(defName + "-" + oid, userId);
//		}
//	}

	@Override
	public boolean unlockByUser(EntityUnlockByUserInvocation invocation) {
		boolean res = transactionLocalQueryCacheInterceptor.unlockByUser(invocation);
		if (res) {
			CacheStore cache = getCache();
			ArrayList<CacheKey> keyList = new ArrayList<CacheKey>();
			for (Object o: cache.keySet()) {
				CacheKey key = (CacheKey) o;
				if (key.getTenantId() == ExecuteContext.getCurrentContext().getClientTenantId()
						&& key.getDefitionName().equals(invocation.getEntityDefinition().getName())
						&& key.getOid().equals(invocation.getOid())) {
					keyList.add(key);
				}
			}
			if (keyList.size() > 0) {
				for(CacheKey key: keyList) {
					cache.remove(key);
				}
			}
//
//			setLockByUser(invocation.getEntityDefinition().getName(), invocation.getOid(), null);
		}
		return res;
	}


//	private void checkAndSetLockedByUser(Entity entity) {
//		Transaction t = ServiceLocator.getInstance().getTransactionManager().currentTransaction();
//		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
//			HashMap<String, String> cache = (HashMap<String, String>) t.getAttribute(LOCK_CACHE_KEY);
//			if (cache != null) {
//				String key = entity.getDefinitionName() + "-" + entity.getOid();
//				if (cache.containsKey(key)) {
//					entity.setLockedBy(cache.get(key));
//				}
//			}
//		}
//	}

	@Override
	public Entity load(EntityLoadInvocation invocation) {
		EntityHandler eh = ((EntityLoadInvocationImpl) invocation).getEntityHandler();

		CacheStore cache = getCache();
		CacheKey key = crateCacheKey(invocation, invocation.getOid(), invocation.getVersion());
		CacheEntry val = cache.get(key);
		EntityCacheEntry ce = null;
		
		//check AuthContext
		EntityPermission.Action entityPermissionAction = EntityQueryAuthContextHolder.getContext().getQueryAction();
		AuthContextHolder authContext = AuthContextHolder.getAuthContext();
		if (val != null) {
			EntityCacheEntry ceForCheck = (EntityCacheEntry) val.getValue();
			if (ceForCheck.authContext != authContext
					|| ceForCheck.entityPermissionAction != entityPermissionAction) {
				cache.remove(key);
				val = null;
			}
		}
		
		if (val != null) {
			ce = (EntityCacheEntry) val.getValue();
			if (ce.entity == null) {
				if (logger.isTraceEnabled()) {
					logger.trace("hit entity cache null:definition=" + invocation.getEntityDefinition().getName() + ", oid=" + invocation.getOid());
				}
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("hit entity cache:definition=" + invocation.getEntityDefinition().getName() + ", oid=" + invocation.getOid());
				}

				//ロックの場合でまだロックしてない場合は、実際に検索する
				if (invocation.withLock() && !ce.withLock) {
					if (logger.isTraceEnabled()) {
						logger.trace("lock needed, so load entire store:definition=" + invocation.getEntityDefinition().getName() + ", oid=" + invocation.getOid());
					}
					Entity e = invocation.proceed();
					if (e == null) {
						ce = nullEntry(entityPermissionAction, authContext);
					} else {
						ce = createEntityCacheEntry(e, invocation, null, entityPermissionAction, authContext);
					}
					putToCache(cache, key, ce, eh);
				} else {
					if (ce.needLoad(invocation.getLoadOption(), invocation.getEntityDefinition())) {
						if (logger.isTraceEnabled()) {
							logger.trace("additional refProperty needed, so load entire store:definition=" + invocation.getEntityDefinition().getName() + ", oid=" + invocation.getOid());
						}
						Entity e = invocation.proceed();
						if (e == null) {
							ce = nullEntry(entityPermissionAction, authContext);
						} else {
							ce = createEntityCacheEntry(e, invocation, ce, entityPermissionAction, authContext);
						}
						putToCache(cache, key, ce, eh);
					}
				}
			}
		} else {
			Entity e = invocation.proceed();
			if (e == null) {
				ce = nullEntry(entityPermissionAction, authContext);
				return null;
			} else {
				ce = createEntityCacheEntry(e, invocation, null, entityPermissionAction, authContext);
			}
			putToCache(cache, key, ce, eh);
		}

		GenericEntity entity = (GenericEntity) ce.getEntity();
		if (entity == null) {
			return null;
		} else {
			Entity toReturn = entity.deepCopy();
//			checkAndSetLockedByUser(toReturn);
			return toReturn;
		}
	}

	private void putToCache(CacheStore cache, CacheKey key, EntityCacheEntry ce, EntityHandler eh) {
		cache.put(new CacheEntry(key, ce), true);
		if (!eh.isVersioned()) {
			CacheKey noVersionKey = new CacheKey(key.getTenantId(), key.getDefitionName(), key.getOid(), null);
			cache.put(new CacheEntry(noVersionKey, ce), true);
		}

	}
	
	private EntityCacheEntry nullEntry(EntityPermission.Action entityPermissionAction, AuthContextHolder authContext) {
		return new EntityCacheEntry(null, true, null, false, null, entityPermissionAction, authContext);
	}

	private EntityCacheEntry createEntityCacheEntry(Entity e, EntityLoadInvocation invocation, EntityCacheEntry prev, EntityPermission.Action entityPermissionAction, AuthContextHolder authContext) {

		LoadOption option = invocation.getLoadOption();
		boolean all = false;
		HashSet<String> loadRefNames = null;
		EntityDefinition def = invocation.getEntityDefinition();
		HashSet<String> defRefNames = new HashSet<String>();

		//全プロパティ取得済みかチェック
		if (option == null
				|| option.getLoadReferences() == null && option.isWithReference() && option.isWithMappedByReference()) {
			all = true;
		} else {
			if (option.getLoadReferences() != null) {
				loadRefNames = new HashSet<String>(option.getLoadReferences());
			} else {
				loadRefNames = new HashSet<String>();
			}
			for (PropertyDefinition pd: def.getPropertyList()) {
				if (pd instanceof ReferenceProperty) {
					defRefNames.add(pd.getName());
					if (option.getLoadReferences() == null
							&& option.isWithReference()) {
						if (((ReferenceProperty) pd).getMappedBy() != null) {
							if (option.isWithMappedByReference()) {
								loadRefNames.add(pd.getName());
							}
						} else {
							loadRefNames.add(pd.getName());
						}
					}
				}
			}
			if (defRefNames.size() == 0
					|| loadRefNames.containsAll(defRefNames)) {
				all = true;
			}

			if (!all && prev != null) {
				//既存とマージ
				if (e.getUpdateDate() != null
						&& e.getUpdateDate().equals(prev.getEntity().getUpdateDate())) {

					for (PropertyDefinition pd: def.getPropertyList()) {
						if (pd instanceof ReferenceProperty) {
							if (!loadRefNames.contains(pd.getName())) {
								e.setValue(pd.getName(), prev.getEntity().getValue(pd.getName()));
							}
						}
					}

					loadRefNames.addAll(prev.refNames);
					if (loadRefNames.containsAll(defRefNames)) {
						all = true;
					}
				}
			}
		}
		
		return new EntityCacheEntry(e, all, loadRefNames, invocation.withLock(), def, entityPermissionAction, authContext);
	}

	@Override
	public void bulkUpdate(EntityBulkUpdateInvocation invocation) {
		transactionLocalQueryCacheInterceptor.bulkUpdate(invocation);
		getCache().removeAll();//とりあえず、、
	}

	@Override
	public int updateAll(EntityUpdateAllInvocation invocation) {
		Integer res = transactionLocalQueryCacheInterceptor.updateAll(invocation);
		getCache().removeAll();//とりあえず、、
		return res.intValue();
	}

	@Override
	public int deleteAll(EntityDeleteAllInvocation invocation) {
		Integer res = transactionLocalQueryCacheInterceptor.deleteAll(invocation);
		getCache().removeAll();//とりあえず、、
		return res.intValue();
	}

	private CacheKey crateCacheKey(EntityInvocation<?> invocation, String oid, Long version) {
		return new CacheKey(ExecuteContext.getCurrentContext().getClientTenantId(), invocation.getEntityDefinition().getName(), oid, version);
	}

	private CacheStore getCache() {
		Transaction t = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
		if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {

			CacheStore cache = (CacheStore) t.getAttribute(EntityCacheInterceptor.LOCAL_LOAD_CACHE_NAMESPACE);
			if (cache == null) {
				cache = ServiceRegistry.getRegistry().getService(CacheService.class).createLocalCache(EntityCacheInterceptor.LOCAL_LOAD_CACHE_NAMESPACE);
				t.setAttribute(EntityCacheInterceptor.LOCAL_LOAD_CACHE_NAMESPACE, cache);
			}
			return cache;
		}
		return NULL_CACHE;
	}

	@Override
	public void query(EntityQueryInvocation invocation) {
		transactionLocalQueryCacheInterceptor.query(invocation);
	}

	@Override
	public int count(EntityCountInvocation invocation) {
		return transactionLocalQueryCacheInterceptor.count(invocation);
	}

	@Override
	public void purge(EntityPurgeInvocation invocation) {
		transactionLocalQueryCacheInterceptor.purge(invocation);
	}

	@Override
	public Entity restore(EntityRestoreInvocation invocation) {
		return transactionLocalQueryCacheInterceptor.restore(invocation);
	}

	private static class EntityCacheEntry {
		final Entity entity;
		final boolean all;
		final HashSet<String> refNames;
		final boolean withLock;
		final List<Entity> refEntity;
		
		final EntityPermission.Action entityPermissionAction;
		final AuthContextHolder authContext;

		private EntityCacheEntry(Entity entity, boolean all, HashSet<String> refNames, boolean withLock, EntityDefinition ed, EntityPermission.Action entityPermissionAction, AuthContextHolder authContext) {
			this.entity = entity;
			this.all = all;
			this.refNames = refNames;
			this.withLock = withLock;
			List<Entity> newRefEntity = null;
			if (ed != null) {
				for (PropertyDefinition pd: ed.getPropertyList()) {
					if (pd instanceof ReferenceProperty) {
						Object o = entity.getValue(pd.getName());
						if (o != null) {
							if (newRefEntity == null) {
								newRefEntity = new ArrayList<Entity>();
							}
							if (o instanceof Entity) {
								newRefEntity.add((Entity) o);
							} else if (o instanceof Entity[]) {
								for (Entity e: (Entity[]) o) {
									newRefEntity.add(e);
								}
							}
						}
					}
				}
			}
			refEntity = newRefEntity;
			this.entityPermissionAction = entityPermissionAction;
			this.authContext = authContext;
		}

		public boolean isRef(CacheKey updateKey) {

			if (refEntity != null) {
				for (Entity e: refEntity) {
					if (updateKey.getDefitionName().equals(e.getDefinitionName())
							&& updateKey.getOid().equals(e.getOid())) {
						return true;
					}
				}
			}
			return false;
		}

		boolean needLoad(LoadOption option, EntityDefinition def) {
			if (all) {
				return false;
			}

			if (option != null && option.getLoadReferences() != null) {
				if (refNames != null && refNames.containsAll(option.getLoadReferences())) {
					return false;
				}
			} else {
				ArrayList<String> refList = new ArrayList<String>();
				for (PropertyDefinition pd: def.getPropertyList()) {
					if (pd instanceof ReferenceProperty) {
						if (option == null) {
							refList.add(pd.getName());
						} else if (option.isWithReference()) {
							if (((ReferenceProperty) pd).getMappedBy() != null) {
								if (option.isWithMappedByReference()) {
									refList.add(pd.getName());
								}
							} else {
								refList.add(pd.getName());
							}
						}
					}
				}
				if (refList.size() == 0) {
					return false;
				} else {
					if (refNames != null && refNames.containsAll(refList)) {
						return false;
					}
				}
			}

			return true;

		}

		Entity getEntity() {
			return entity;
		}

	}

	private static class CacheKey {

		private int tenantId;
		private String defitionName;
		private String oid;
		private Long version;

		private CacheKey(int tenantId, String defitionName, String oid, Long version) {
			super();
			this.tenantId = tenantId;
			this.defitionName = defitionName;
			this.oid = oid;
			this.version = version;
		}

		public int getTenantId() {
			return tenantId;
		}
		public String getDefitionName() {
			return defitionName;
		}
		public String getOid() {
			return oid;
		}
		public Long getVersion() {
			return version;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((defitionName == null) ? 0 : defitionName.hashCode());
			result = prime * result + ((oid == null) ? 0 : oid.hashCode());
			result = prime * result + tenantId;
			result = prime * result
					+ ((version == null) ? 0 : version.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheKey other = (CacheKey) obj;
			if (defitionName == null) {
				if (other.defitionName != null)
					return false;
			} else if (!defitionName.equals(other.defitionName))
				return false;
			if (oid == null) {
				if (other.oid != null)
					return false;
			} else if (!oid.equals(other.oid))
				return false;
			if (tenantId != other.tenantId)
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}


	}

}
