/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.cache.store.builtin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionListener;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * トランザクションが有効な間はバックエンドのCacheStoreに反映を遅延するCacheStoreFactory。
 * 更新処理が他のスレッドなどと競合した場合は、バックエンドのキャッシュ内容をクリアしてしまうので、
 * 当CacheStoreに格納されるデータはバックエンドに永続ストアがあり、その補助的なキャッシュとしての利用を想定。
 * 勝手にキャッシュを消されては問題の場合は、このクラスの利用は不可。
 * 
 * @author K.Higuchi
 *
 */
public class TransactionLocalCacheStoreFactory extends AbstractBuiltinCacheStoreFactory {
	private static final String CACHE_PREFIX = "org.iplass.mtp.cache.";
	private static final NullCacheStore NULL_CACHE = new NullCacheStore("", null);

	private static Logger logger = LoggerFactory.getLogger(TransactionLocalCacheStoreFactory.class);

	private int initialCapacity = 16;
	private float loadFactor = 0.75f;

	private CacheStoreFactory backendStore;

	public CacheStoreFactory getBackendStore() {
		return backendStore;
	}

	public void setBackendStore(CacheStoreFactory backendStore) {
		backendStore.setIndexCount(getIndexCount());
		this.backendStore = backendStore;
	}

	@Override
	public void setIndexCount(int indexCount) {
		super.setIndexCount(indexCount);
		if (backendStore != null) {
			backendStore.setIndexCount(indexCount);
		}
	}

	public int getInitialCapacity() {
		return initialCapacity;
	}

	public void setInitialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
	}

	public float getLoadFactor() {
		return loadFactor;
	}

	public void setLoadFactor(float loadFactor) {
		this.loadFactor = loadFactor;
	}

	@Override
	public CacheStore createCacheStore(String namespace) {
		return new TransactionLocalCacheStore(namespace);
	}

	@Override
	public boolean canUseForLocalCache() {
		return false;
	}

	@Override
	public boolean supportsIndex() {
		return true;
	}

	public class TransactionLocalCacheStore implements CacheStore {

		private final String namespace;
		private final CacheStore backendCacheStore;

		TransactionLocalCacheStore(String namespace) {
			this.namespace = namespace;
			if (backendStore != null) {
				CacheStore cs = backendStore.createCacheStore(namespace);
				backendCacheStore = cs;
			} else {
				backendCacheStore = null;
			}
		}

		public CacheStore getBackendCacheStore() {
			return backendCacheStore;
		}

		//FIXME そもそも、doModifyは、呼び出し側のTransacitonで実施してよいかな。TransactionListennerのafterでdoFinish
		//FIXME CacheEntryのバージョン番号は更新時は、以前のやつより+1以上になるようにする
		//FIXME put操作も、事前の値取得を行う。putIfAbsent、replaceを用いる。
		//FIXME Load時のputはdirtyじゃないので、トランザクションの反映を待たずに、即共有キャッシュへput

		public void reloadFromBackendStore(Object key) {
			getStore().remove(key);
			get(key);
		}

//		public boolean isNegativeCachedOnLocalStore(Object key) {
//			return getStore().get(key) instanceof NullCacheEntry;
//		}

//		public boolean isNegativeCachedOnLocalStore(int indexKey, Object indexValue) {
//			return getNullIndexList().isNull(indexKey, indexValue);
//		}

		private CacheStore getStore() {
			Transaction t = Transaction.getCurrent();
			if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
				CacheStore store = (CacheStore) t.getAttribute(CACHE_PREFIX + namespace);
				if (store == null) {
					store = new MapBaseCacheStore(namespace, new HashMap<Object, CacheEntry>(initialCapacity, loadFactor), getIndexCount(), -1, TransactionLocalCacheStoreFactory.this);
					t.setAttribute(CACHE_PREFIX + namespace, store);
				}
				return store;
			} else {
				return NULL_CACHE;
			}
		}

		@Override
		public CacheEntry put(final CacheEntry entry, final boolean isClean) {
			if (logger.isTraceEnabled()) {
				logger.trace("put local cache!key=" + entry.getKey() + ",version=" + entry.getVersion() + ",value=" + entry.getValue() + ",clean=" + isClean);
			}
			final CacheEntry localPrevious = getStore().put(entry, isClean);
			
			if (backendCacheStore == null) {
				return localPrevious;
			}
			

			if (isClean) {
				//dirtyでない場合は、そのままバックエンドにput
				CacheEntry backendPrevious = backendCacheStore.put(entry, isClean);
				if (localPrevious != null) {
					return localPrevious;
				} else {
					return backendPrevious;
				}
				
			} else {
				//dirtyの場合は、トランザクションコミットのタイミングで更新
				final CacheEntry previous;
				if (entry.getKey() != null) {
					if (localPrevious == null) {
						previous = backendCacheStore.get(entry.getKey());
					} else {
						if (localPrevious instanceof RemovedCacheEntry) {
							previous = null;
						} else {
							previous = localPrevious;
						}
					}
				} else {
					previous = null;
				}
				addOrRunTransactionListener(new TransactionListener() {
					@Override
					public void afterRollback(Transaction t) {
					}

					@Override
					public void afterCommit(Transaction t) {
						if (logger.isTraceEnabled()) {
							logger.trace("put shared cache!key=" + entry.getKey() + ",version=" + entry.getVersion() + ",value=" + entry.getValue());
						}
						boolean isConflict = true;
						try {
							if (previous == null) {
								//新規
								CacheEntry sheredPrevious = backendCacheStore.putIfAbsent(entry);
								if (sheredPrevious == null) {
									isConflict = false;
								} else {
									if (logger.isDebugEnabled()) {
										logger.debug("maybe another Thread Updated newly version:" + sheredPrevious.getVersion() + ". so markInvalid CacheEntry:" + entry.getKey() + " version:" + entry.getVersion());
									}
								}
							} else {
								//更新
								if (backendCacheStore.replace(previous, entry)) {
									isConflict = false;
								} else {
									boolean isLoop = true;
									while (isLoop) {
										CacheEntry current = backendCacheStore.get(entry.getKey());
										if (current == null) {
											//削除されたのかどうかわからないので、conflict
											if (logger.isDebugEnabled()) {
												logger.debug("CacheEntry:" + entry.getKey() + " not found. maybe removed. so markInvalid.");
											}
											isLoop = false;
//										} else if (current.getVersion() < entry.getVersion()) {
//											//更新しようとしている方が新しいバージョンなので
//											if (backendCacheStore.replace(current, entry)) {
//												isLoop = false;
//												isConflict = false;
//											}
										} else {
											//別スレッドがさらに新しいバージョンで更新済み
											if (logger.isDebugEnabled()) {
												logger.debug("maybe another Thread Updated newly version:" + current.getVersion() + ". so markInvalid CacheEntry:" + entry.getKey() + " version:" + entry.getVersion());
											}
											isLoop = false;
//											isConflict = false;
										}
									}
								}
							}

						} finally {
							if (isConflict) {
								//状態があやしいので、無効化する
								try {
									backendCacheStore.remove(entry.getKey());
								} catch (RuntimeException e) {
									logger.error("cache may be illegal state... cause:" + e, e);
								}
							}
						}
					}
				});
				
				return previous;
			}
		}

		@Override
		public CacheEntry putIfAbsent(final CacheEntry entry) {
			final CacheEntry target = getStore().get(entry.getKey());
			if (target != null && !(target instanceof RemovedCacheEntry)) {
				return target;
			}
			if (logger.isTraceEnabled()) {
				logger.trace("put local cache!key=" + entry.getKey() + ",version=" + entry.getVersion() + ",value=" + entry.getValue());
			}
			getStore().put(entry, false);
			
			final CacheEntry backendPrevious = backendCacheStore.get(entry.getKey());
			if (backendPrevious != null && !(target instanceof RemovedCacheEntry)) {
				return backendPrevious;
			}
			
			//ここまでくるパターンは、
			//1. backendが本当にnull
			//2. backendがnullでないが、ローカルトランザクション上削除済み

			//dirtyの場合は、トランザクションコミットのタイミングで更新
			addOrRunTransactionListener(new TransactionListener() {
				@Override
				public void afterRollback(Transaction t) {
				}

				@Override
				public void afterCommit(Transaction t) {

					if (logger.isTraceEnabled()) {
						logger.trace("put shared cache!key=" + entry.getKey() + ",version=" + entry.getVersion() + ",value=" + entry.getValue());
					}
					boolean isConflict = true;
					try {
						if (backendPrevious == null) {
							//1. backendが本当にnull
							CacheEntry sheredPrevious = backendCacheStore.putIfAbsent(entry);
							if (sheredPrevious == null) {
								isConflict = false;
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("maybe another Thread Updated newly version:" + sheredPrevious.getVersion() + ".  so markInvalid CacheEntry:" + entry.getKey() + " version:" + entry.getVersion());
								}
							}
						} else {
							//2. backendがnullでないが、ローカルトランザクション上削除済み
							if (backendCacheStore.replace(backendPrevious, entry)) {
								isConflict = false;
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("maybe another Thread Updated newly version. expected previous version is " + backendPrevious.getVersion() + " but no match.  so markInvalid CacheEntry:" + entry.getKey() + " version:" + entry.getVersion());
								}
							}
						}
					} finally {
						if (isConflict) {
							//状態があやしいので、無効化する
							try {
								backendCacheStore.remove(entry.getKey());
							} catch (RuntimeException e) {
								logger.error("cache may be illegal state... cause:" + e, e);
							}
						}
					}
				}
			});
			return null;
		}

		@Override
		public CacheEntry get(Object key) {
			CacheStore localCache = getStore();
			CacheEntry entry = localCache.get(key);
			if (entry != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("hit local cache!key=" + entry.getKey() + ",version=" + entry.getVersion() + ",value=" + entry.getValue());
				}

//				if (entry instanceof RemovedCacheEntry) {
//					return null;
//				} else {
					return entry;
//				}
			}
			if (backendCacheStore == null) {
				return null;
			}

			CacheEntry sharedEntry = backendCacheStore.get(key);
			if (sharedEntry != null) {
				localCache.put(sharedEntry, true);
				return sharedEntry;
			} else {
				return null;
			}
		}

		@Override
		public CacheEntry remove(final Object key) {
			if (logger.isTraceEnabled()) {
				logger.trace("remove local cache!key=" + key);
			}
			CacheEntry sharedToRemove;
			if (backendCacheStore != null) {
				sharedToRemove = backendCacheStore.get(key);
			} else {
				sharedToRemove = null;
			}

			CacheStore localCache = getStore();
			CacheEntry removed = localCache.remove(key);
//			if (removed instanceof RemovedCacheEntry) {
//				removed = null;
//			}

			//TransactionLocal中、削除としてマーク
			if (sharedToRemove != null) {
				localCache.put(new RemovedCacheEntry(sharedToRemove), true);
			}

			if (backendCacheStore == null) {
				return removed;
			}
			
			final CacheEntry previous;
			if (key != null) {
				if (removed != null) {
					if (removed instanceof RemovedCacheEntry) {
						previous = null;
					} else {
						previous = sharedToRemove;
					}
				} else {
					previous = sharedToRemove;
				}
			} else {
				previous = null;
			}

			addOrRunTransactionListener(new TransactionListener() {
				@Override
				public void afterRollback(Transaction t) {
				}

				@Override
				public void afterCommit(Transaction t) {

					if (logger.isTraceEnabled()) {
						logger.trace("remove from shared cache!key=" + key);
					}
					boolean isConflict = true;
					try {
						if (previous != null) {
							if (!backendCacheStore.remove(previous)) {
								boolean isLoop = true;
								while (isLoop) {
									CacheEntry current = backendCacheStore.get(key);
									if (current == null) {
										if (logger.isDebugEnabled()) {
											logger.debug("CacheEntry:" + key + " not found. maybe already removed by another thread.");
										}
										isLoop = false;
										isConflict = false;
//									} else if (current.getVersion() < previous.getVersion()) {
//										if (backendCacheStore.remove(current)) {
//											isLoop = false;
//											isConflict = false;
//										}
									} else {
										//別スレッドがさらに新しいバージョンで更新済み
										if (logger.isDebugEnabled()) {
											logger.debug("maybe another Thread Updated newly version:" + current.getVersion() + ". so markInvalid CacheEntry:" + previous.getKey() + " version:" + previous.getVersion());
										}
										isLoop = false;
//										isConflict = false;
									}
								}
							} else {
								isConflict = false;
							}
						} else {
							backendCacheStore.remove(key);
							isConflict = false;
						}
					} finally {
						if (isConflict) {
							//状態があやしいので、無効化する
							try {
								backendCacheStore.remove(previous.getKey());
							} catch (RuntimeException e) {
								logger.error("cache may be illegal state... cause:" + e, e);
							}
						}
					}
				}
			});

			return sharedToRemove;
		}

		@Override
		public boolean remove(CacheEntry entry) {

			CacheEntry compareTarget = get(entry.getKey());
			if (compareTarget == null ||
					!compareTarget.equals(entry)) {
				return false;
			}

			remove(entry.getKey());
			return true;
		}

		@Override
		public CacheEntry replace(final CacheEntry entry) {
			CacheEntry target = getStore().get(entry.getKey());
			if (target instanceof RemovedCacheEntry) {
				return null;
			}
			
			CacheEntry backendPrevious = backendCacheStore.get(entry.getKey());
			if (backendPrevious == null) {
				return null;
			}
			if (target == null) {
				target = backendPrevious;
			}

			if (logger.isTraceEnabled()) {
				logger.trace("put local cache!key=" + entry.getKey() + ",version=" + entry.getVersion() + ",value=" + entry.getValue());
			}
			getStore().put(entry, false);
			
			final CacheEntry previous = target;
			
			addOrRunTransactionListener(new TransactionListener() {
				@Override
				public void afterRollback(Transaction t) {
				}

				@Override
				public void afterCommit(Transaction t) {

					if (logger.isTraceEnabled()) {
						logger.trace("put shared cache!key=" + entry.getKey() + ",version=" + entry.getVersion() + ",value=" + entry.getValue());
					}
					boolean isConflict = true;
					try {
						if (previous != null) {
							//replace
							if (backendCacheStore.replace(previous, entry)) {
								isConflict = false;
							} else {
								boolean isLoop = true;
								while (isLoop) {
									CacheEntry current = backendCacheStore.get(entry.getKey());
									if (current == null) {
										//削除されたのかどうかわからないので、conflict
										if (logger.isDebugEnabled()) {
											logger.debug("CacheEntry:" + entry.getKey() + " not found. maybe removed. so markInvalid.");
										}
										isLoop = false;
//									} else if (current.getVersion() < entry.getVersion()) {
//										//更新しようとしている方が新しいバージョンなので
//										if (backendCacheStore.replace(current, entry)) {
//											isLoop = false;
//											isConflict = false;
//										}
									} else {
										//別スレッドがさらに新しいバージョンで更新済み
										if (logger.isDebugEnabled()) {
											logger.debug("maybe another Thread Updated newly version:" + current.getVersion() + ".  so markInvalid CacheEntry:" + entry.getKey() + " version:" + entry.getVersion());
										}
										isLoop = false;
//										isConflict = false;
									}
								}
							}
						}

					} finally {
						if (isConflict) {
							//状態があやしいので、無効化する
							try {
								backendCacheStore.remove(entry.getKey());
							} catch (RuntimeException e) {
								logger.error("cache may be illegal state... cause:" + e, e);
							}
						}
					}
				}
			});
			return target;
		}

		@Override
		public boolean replace(final CacheEntry oldEntry, final CacheEntry newEntry) {
			if (!oldEntry.getKey().equals(newEntry.getKey())) {
				throw new IllegalArgumentException("new key must equals old key.");
			}
			CacheEntry target = getStore().get(oldEntry.getKey());
			if (target instanceof RemovedCacheEntry
					|| !oldEntry.equals(target)) {
				return false;
			}
			
			if (target == null) {
				target = backendCacheStore.get(newEntry.getKey());
			}
			if (target == null || !oldEntry.equals(target)) {
				return false;
			}
			
			if (logger.isTraceEnabled()) {
				logger.trace("put local cache!key=" + newEntry.getKey() + ",version=" + newEntry.getVersion() + ",value=" + newEntry.getValue());
			}
			getStore().put(newEntry, false);

			//dirtyの場合は、トランザクションコミットのタイミングで更新
			final CacheEntry previous = target;
			
			addOrRunTransactionListener(new TransactionListener() {
				@Override
				public void afterRollback(Transaction t) {
				}

				@Override
				public void afterCommit(Transaction t) {

					if (logger.isTraceEnabled()) {
						logger.trace("put shared cache!key=" + newEntry.getKey() + ",version=" + newEntry.getVersion() + ",value=" + newEntry.getValue());
					}
					boolean isConflict = true;
					try {
						if (previous != null) {
							//replace
							if (backendCacheStore.replace(previous, newEntry)) {
								isConflict = false;
							} else {
								boolean isLoop = true;
								while (isLoop) {
									CacheEntry current = backendCacheStore.get(newEntry.getKey());
									if (current == null) {
										//削除されたのかどうかわからないので、conflict
										if (logger.isDebugEnabled()) {
											logger.debug("CacheEntry:" + newEntry.getKey() + " not found. maybe removed. so markInvalid.");
										}
										isLoop = false;
//									} else if (current.getVersion() < newEntry.getVersion()) {
//										//更新しようとしている方が新しいバージョンなので
//										if (backendCacheStore.replace(current, newEntry)) {
//											isLoop = false;
//											isConflict = false;
//										}
									} else {
										//別スレッドがさらに新しいバージョンで更新済み
										if (logger.isDebugEnabled()) {
											logger.debug("maybe another Thread Updated newly version:" + current.getVersion() + ". so markInvalid CacheEntry:" + newEntry.getKey() + " version:" + newEntry.getVersion());
										}
										isLoop = false;
//										isConflict = false;
									}
								}
							}
						}

					} finally {
						if (isConflict) {
							//状態があやしいので、無効化する
							try {
								backendCacheStore.remove(newEntry.getKey());
							} catch (RuntimeException e) {
								logger.error("cache may be illegal state... cause:" + e, e);
							}
						}
					}
				}
			});
			return true;
		}

		@Override
		public void removeAll() {
			for (Object key: keySet()) {
				remove(key);
			}
		}

		@Override
		public List<Object> keySet() {
			ArrayList<Object> ret = new ArrayList<Object>();

			if (backendCacheStore != null) {
				ret.addAll(backendCacheStore.keySet());
			}

			CacheStore localStore = getStore();
			List<Object> localKeySet = localStore.keySet();
			for (Object k: localKeySet) {
				CacheEntry ce = localStore.get(k);
				if (ce != null) {
					if (!ret.contains(k)) {
						ret.add(k);
					}
				}
			}
			return ret;
		}

		@Override
		public CacheEntry getByIndex(int indexKey, Object indexValue) {

			CacheStore localCache = getStore();
			List<CacheEntry> entries = localCache.getListByIndex(indexKey, indexValue);
			if (entries != null && entries.size() > 0) {
				CacheEntry entry = null;
				for (CacheEntry ent : entries) {
					entry = ent;
					if (!(ent instanceof RemovedCacheEntry)) {
						break;
					}
				}

				if (logger.isTraceEnabled()) {
					logger.trace("hit local cache!indexVal(" + indexKey + ")=" + indexValue + ",key=" + entry.getKey() + ",version=" + entry.getVersion() + ",value=" + entry.getValue() + ",is Remove=" + (entry instanceof RemovedCacheEntry));
				}
				return entry;
			}
			if (backendCacheStore == null) {
				return null;
			}

			CacheEntry sharedEntry = backendCacheStore.getByIndex(indexKey, indexValue);
			if (sharedEntry != null) {
				//indexの値がtoransactonLocalで更新されている可能性あり
				CacheEntry localEntry = localCache.get(sharedEntry.getKey());
				if (localEntry != null &&
						((localEntry instanceof RemovedCacheEntry) ||
						!indexValue.equals(localEntry.getIndexValue(indexKey)))) {
					return null;
				} else {
					localCache.put(sharedEntry, true);
					return sharedEntry;
				}
			} else {
				return null;
			}
		}

		@Override
		public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {

			CacheStore localCache = getStore();
			List<CacheEntry> localList = localCache.getListByIndex(indexKey, indexValue);
			if (backendCacheStore == null) {
				return localList;
			}

			List<CacheEntry> ret = new ArrayList<CacheEntry>();
			List<CacheEntry> sharedList = backendCacheStore.getListByIndex(indexKey, indexValue);

			for (CacheEntry sce: sharedList) {
				CacheEntry lce = get(sce.getKey());
				if (lce != null && !(lce instanceof RemovedCacheEntry) && indexValue.equals(lce.getIndexValue(indexKey))) {
					ret.add(lce);
				}
			}

			//新規に追加されているものを追加
			for (CacheEntry lce: localList) {
//				if (!(lce instanceof RemovedCacheEntry)) {
					if (!ret.contains(lce)) {
						
						ret.add(lce);
					}
//				}
			}
			return ret;
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		@Override
		public void addCacheEventListenner(CacheEventListener listener) {
			if (backendCacheStore != null) {
				backendCacheStore.addCacheEventListenner(listener);
			}
		}

		@Override
		public void removeCacheEventListenner(CacheEventListener listener) {
			if (backendCacheStore != null) {
				backendCacheStore.removeCacheEventListenner(listener);
			}
		}
		
		@Override
		public List<CacheEventListener> getListeners() {
			if (backendCacheStore == null) {
				return Collections.emptyList();
			} else {
				return backendCacheStore.getListeners();
			}
		}

		private void addOrRunTransactionListener(TransactionListener listener) {
			Transaction t = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
			if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
				t.addTransactionListener(listener);
			} else {
				listener.afterCommit(null);
			}
		}

		@Override
		public CacheStoreFactory getFactory() {
			return TransactionLocalCacheStoreFactory.this;
		}

//		@Override
//		public void invalidate(final CacheEntry entry) {
//			if (logger.isTraceEnabled()) {
//				logger.trace("invalidate local cache!key=" + entry.getKey());
//			}
//			final CacheEntry sharedToRemove;
//			if (backendCacheStore != null) {
//				sharedToRemove = backendCacheStore.get(entry.getKey());
//			} else {
//				sharedToRemove = null;
//			}
//
//			CacheStore localCache = getStore();
//			CacheEntry removed = localCache.remove(entry.getKey());
//			if (removed instanceof RemovedCacheEntry) {
//				removed = null;
//			}
//
//			if (sharedToRemove != null) {
//				localCache.put(new RemovedCacheEntry(sharedToRemove), true);
//			} else {
//				localCache.put(new RemovedCacheEntry(entry.getKey(), null), true);
//			}
//
//			if (backendCacheStore == null) {
//				return;
//			}
//
//			addOrRunTransactionListener(new TransactionListener() {
//				@Override
//				public void afterRollback(Transaction t) {
//				}
//
//				@Override
//				public void afterCommit(Transaction t) {
//
//					if (logger.isTraceEnabled()) {
//						logger.trace("invalidate shared cache!key=" + entry.getKey());
//					}
//					backendCacheStore.invalidate(entry);
//				}
//			});
//		}

		@Override
		public List<CacheEntry> removeByIndex(final int indexKey, final Object indexValue) {

			List<CacheEntry> list = getListByIndex(indexKey, indexValue);
			if (list != null) {
				CacheStore localCache = getStore();
				for (CacheEntry e: list) {
					localCache.put(new RemovedCacheEntry(e), false);
				}
			}

			addOrRunTransactionListener(new TransactionListener() {
				@Override
				public void afterRollback(Transaction t) {
				}

				@Override
				public void afterCommit(Transaction t) {

					if (logger.isTraceEnabled()) {
						logger.trace("removeByIndex shared cache!indexKey=" + indexKey + ", indexValue=" + indexValue);
					}
					backendCacheStore.removeByIndex(indexKey, indexValue);
				}
			});
			return list;
		}
		

		@Override
		public Integer getSize() {
			return backendCacheStore.getSize();
		}

		@Override
		public String trace() {
			StringBuilder builder = new StringBuilder();
			builder.append("-----------------------------------");
			builder.append("\nCacheStore Info");
			builder.append("\nCacheStore:" + this);
			builder.append("\n\tnamespace:" + namespace);
			builder.append("\n\tindexCount:" + getIndexCount());
			CacheStore localCache = getStore();
			builder.append("\n\t===================================");
			builder.append("\n\tlocalCache:" + localCache);
			builder.append("\n" + localCache.trace());
			builder.append("\n\t===================================");
			builder.append("\n\tbackendCacheStore:" + backendCacheStore);
			builder.append("\n" + backendCacheStore.trace());
			builder.append("\n-----------------------------------");

			return builder.toString();
		}

		@Override
		public void destroy() {
			if (backendCacheStore != null) {
				backendCacheStore.destroy();
			}
		}

	}

	@Override
	public CacheHandler createCacheHandler(CacheStore store) {
		return new SimpleLocalCacheHandler(store, getConcurrencyLevelOfCacheHandler());
	}
	
	public static final class RemovedCacheEntry extends CacheEntry {
		private static final long serialVersionUID = 8582988909194271518L;

		public RemovedCacheEntry(Object key, Object[] indexValues) {
			super(key, null, Long.MIN_VALUE, indexValues);
		}

		public RemovedCacheEntry(CacheEntry actual) {
			super(actual.getKey(), null, actual.getVersion() + 1, actual.getIndexValues());
		}
	}

	@Override
	public CacheStoreFactory getLowerLevel() {
		return backendStore;
	}

}
