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

package org.iplass.mtp.impl.counter;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.EntityApplicationException;
import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.entity.EntityDuplicateValueException;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.keyresolver.CacheKeyResolver;
import org.iplass.mtp.impl.counter.sql.RdbTableCounterSql;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * APサーバ上にカウンター値をキャッシュするCounterService。
 * キャッシュするので、飛び番は発生する（飛び番許容しない場合[increment時にPropagation.REQUIRED]は、直接rdbを呼ぶ）。
 * また、currentの値は必ずしも正しいとは限らない。APサーバローカルの最新値であるだけ。
 * 
 * @author K.Higuchi
 *
 */
public class CachableRdbTableCounterService implements CounterService {
	private static final String COUNTER_CACHE_NAMESPACE = "mtp.counter.rdbTableCounter";
	private static Logger logger = LoggerFactory.getLogger(CachableRdbTableCounterService.class);
	
	private int cacheSize = 20;
	
	private RdbTableCounterService rdbCounter;
	
	//FIXME クラスタへリセット時の通知。SyncServerな、LRUなキャッシュを利用。
	private CacheStore cache;

	@Override
	public void init(Config config) {
		rdbCounter = new RdbTableCounterService();
		rdbCounter.init(config);
		cacheSize = config.getValue("cacheSize", Integer.TYPE, 20);
		
		CacheService cs = config.getDependentService(CacheService.class);
		cache = cs.getCache(COUNTER_CACHE_NAMESPACE);
	}

	@Override
	public void destroy() {
		rdbCounter.destroy();
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		cs.invalidate(COUNTER_CACHE_NAMESPACE);
	}

	@Override
	public long increment(int tenantId, String incrementUnitKey, long initialCount) {
		//キャッシュから取得
		CounterKey key = new CounterKey(tenantId, rdbCounter.getCounterTypeName(), incrementUnitKey);
		CacheEntry ce = cache.get(key);
		if (ce == null) {
			CacheEntry newCe = new CacheEntry(key, new Counter(key, initialCount));
			ce = cache.putIfAbsent(newCe);
			if (ce == null) {
				ce = newCe;
			}
		}
		return ((Counter) ce.getValue()).increment();
	}

	@Override
	public void resetCounter(int tenantId, String incrementUnitKey) {
		resetCounter(tenantId, incrementUnitKey, 0);
	}

	@Override
	public void resetCounter(final int tenantId, final String incrementUnitKey,
			final long currentCount) {
		//別トランザクションで実施。
		Transaction.requiresNew(t -> {
			rdbCounter.resetCounter(tenantId, incrementUnitKey, currentCount);
		});
		
		cache.remove(new CounterKey(tenantId, rdbCounter.getCounterTypeName(), incrementUnitKey));
	}

	@Override
	public void deleteCounter(final int tenantId, final String incrementUnitKey) {
		//別トランザクションで実施。
		Transaction.requiresNew(t -> {
			rdbCounter.deleteCounter(tenantId, incrementUnitKey);
		});
		
		cache.remove(new CounterKey(tenantId, rdbCounter.getCounterTypeName(), incrementUnitKey));
	}
	
	@Override
	public long current(int tenantId, String incrementUnitKey) {
		CounterKey key = new CounterKey(tenantId, rdbCounter.getCounterTypeName(), incrementUnitKey);
		CacheEntry ce = cache.get(key);
		if (ce != null) {
			return ((Counter) ce.getValue()).current();
		} else {
			return rdbCounter.current(tenantId, incrementUnitKey);
		}
	}
	
	
	private static class CounterKey implements Serializable {
		private static final long serialVersionUID = 9085282190400818284L;
		
		private final int tenantId;
		private final String typeName;
		private final String incrementUnitKey;
		
		public CounterKey(int tenantId, String typeName, String incrementUnitKey) {
			this.tenantId = tenantId;
			this.typeName = typeName;
			this.incrementUnitKey = incrementUnitKey;
		}

		public int getTenantId() {
			return tenantId;
		}
		
		public String getTypeName() {
			return typeName;
		}

		public String getIncrementUnitKey() {
			return incrementUnitKey;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((incrementUnitKey == null) ? 0 : incrementUnitKey
							.hashCode());
			result = prime * result + tenantId;
			result = prime * result
					+ ((typeName == null) ? 0 : typeName.hashCode());
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
			CounterKey other = (CounterKey) obj;
			if (incrementUnitKey == null) {
				if (other.incrementUnitKey != null)
					return false;
			} else if (!incrementUnitKey.equals(other.incrementUnitKey))
				return false;
			if (tenantId != other.tenantId)
				return false;
			if (typeName == null) {
				if (other.typeName != null)
					return false;
			} else if (!typeName.equals(other.typeName))
				return false;
			return true;
		}
	}
	
	
	private class Counter {
		
		private final CounterKey key;
		private long cachedMax;
		private long count;
		private boolean inited;
		
		private Counter(CounterKey key, long initCount) {
			this.key = key;
			this.count = initCount;
			this.cachedMax = this.count;
			inited = false;
		}
		
		public synchronized long increment() {
			if (!inited || count == cachedMax) {
				for (int i = 0; i < rdbCounter.getRetryCount(); i++) {
					try {
						loadNextCount();
						break;
					} catch (RuntimeException e) {
						if (i + 1 == rdbCounter.getRetryCount()) {
							throw e;
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("fail to increment counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + ", retry...");
							}
						}
					}
				}
			}
			count = count + 1;
			return count;
		}
		
		private void loadNextCount() {
			if (logger.isDebugEnabled()) {
				logger.debug("counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + " load to cache, size=" + cacheSize);
			}
			
			EntityApplicationException eae = null;
			Long rdbCount = null;
			try {
				rdbCount = Transaction.requiresNew(t -> {
					
					SqlExecuter<Long> executer = new SqlExecuter<Long>() {
						@Override
						public Long logic() throws SQLException {
							RdbTableCounterSql sql = rdbCounter.getCounterSql();
			
							String select = sql.currentValueSql(key.getTenantId(), rdbCounter.getCounterTypeName(), key.getIncrementUnitKey(), true, rdbCounter.getRdbAdapter());
							ResultSet rs = getStatement().executeQuery(select);
							long current = Long.MIN_VALUE;
							try {
								if (rs.next()) {
									current = rs.getLong(1);
								}
							} finally {
								rs.close();
							}
							if (current != Long.MIN_VALUE) {
								//すでにカウンター初期化済み
								String update = sql.incrementSql(key.getTenantId(), rdbCounter.getCounterTypeName(), key.getIncrementUnitKey(), cacheSize, rdbCounter.getRdbAdapter());
								int res = getStatement().executeUpdate(update);
								if (res != 1) {
									throw new SystemException("counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + " increment failed");
								}
								return current;
							} else {
								//カウンター初期化を試みる
								String create = sql.createCounterSql(key.getTenantId(), rdbCounter.getCounterTypeName(), key.getIncrementUnitKey(), count - 1 + cacheSize, rdbCounter.getRdbAdapter());
								int res = getStatement().executeUpdate(create);
								if (res != 1) {
									throw new SystemException("counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + " increment failed");
								}
								return count - 1;
							}
						}
					};
					return executer.execute(rdbCounter.getRdbAdapter(), true);
				});
				
			} catch (EntityDuplicateValueException e) {
				//別スレッド、もしくは別サーバで初期化された
				if (logger.isDebugEnabled()) {
					logger.debug("counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + " init process is failed, mybe antoher thread or server inited.retry get counter...", e);
				} else {
					logger.warn("counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + " init process is failed, mybe antoher thread or server inited.retry get counter...");
				}
				eae = e;
			} catch (EntityConcurrentUpdateException e) {
				//別スレッド、もしくは別サーバで初期化された
				if (logger.isDebugEnabled()) {
					logger.debug("counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + " init process is failed, mybe antoher thread or server inited.retry get counter...", e);
				} else {
					logger.warn("counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + " init process is failed, mybe antoher thread or server inited.retry get counter...");
				}
				eae = e;
			}
			
			//採番のみ再度実施を試みる
			if (eae != null) {
				rdbCount = Transaction.requiresNew(t -> {
					
					SqlExecuter<Long> executer = new SqlExecuter<Long>() {
						@Override
						public Long logic() throws SQLException {
							RdbTableCounterSql sql = rdbCounter.getCounterSql();
			
							String select = sql.currentValueSql(key.getTenantId(), rdbCounter.getCounterTypeName(), key.getIncrementUnitKey(), true, rdbCounter.getRdbAdapter());
							ResultSet rs = getStatement().executeQuery(select);
							long current = Long.MIN_VALUE;
							try {
								if (rs.next()) {
									current = rs.getLong(1);
								}
							} finally {
								rs.close();
							}
							if (current != Long.MIN_VALUE) {
								//すでにカウンター初期化済み
								String update = sql.incrementSql(key.getTenantId(), rdbCounter.getCounterTypeName(), key.getIncrementUnitKey(), cacheSize, rdbCounter.getRdbAdapter());
								int res = getStatement().executeUpdate(update);
								if (res != 1) {
									throw new SystemException("counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + " increment failed");
								}
								return current;
							} else {
								return null;
							}
						}
					};
					return executer.execute(rdbCounter.getRdbAdapter(), true);
				});
			}
			
			if (rdbCount == null) {
				throw new SystemException("counter:" + rdbCounter.getCounterTypeName() + "." + key.getIncrementUnitKey() + " increment failed");
			}
			
			count = rdbCount.longValue();
			cachedMax = rdbCount.longValue() + cacheSize;
			inited = true;
		}
		
		
		public synchronized long current() {
			if (inited) {
				return count;
			} else {
				return rdbCounter.current(key.getTenantId(), key.getIncrementUnitKey());
			}
		}
		
	}
	
	public static class CounterCacheKeyResolver implements CacheKeyResolver {

		@Override
		public String toString(Object cacheKey) {
			CounterKey key = (CounterKey) cacheKey;
			StringBuilder sb = new StringBuilder();
			sb.append(key.getTenantId()).append(':').append(key.getIncrementUnitKey());
			return sb.toString();
		}

		@Override
		public Object toCacheKey(String cacheKeyString) {
			
			int index = cacheKeyString.indexOf(':');
			int tenantId = Integer.parseInt(cacheKeyString.substring(0, index));
			int index2 = cacheKeyString.indexOf(index + 1, ':');
			String type = cacheKeyString.substring(index, index2);
			String incUnitKey = cacheKeyString.substring(index2 + 1);
			
			return new CounterKey(tenantId, type, incUnitKey);
		}

	}

}
