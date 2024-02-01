/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

class FineGrainedLockIndex {
	private ConcurrentMap<Object, IndexValue> indexMap;
	private int shardSize;
	private boolean fair;
	
	public FineGrainedLockIndex(int shardSize, boolean fair) {
		indexMap = new ConcurrentHashMap<>();
		this.shardSize = shardSize;
		this.fair = fair;
	}
	
	public IndexValue getIndexValue(Object value, boolean createIfNone) {
		if (createIfNone) {
			return indexMap.computeIfAbsent(value, k -> new IndexValue(shardSize, fair));
		} else {
			return indexMap.get(value);
		}
	}
	
	public int shardIndex(Object key) {
		if (shardSize == 1) {
			return 0;
		} else {
			return Math.abs(key.hashCode() % shardSize);
		}
	}
	
	public void destroy() {
		indexMap.clear();
	}
	
	static class IndexValue {
		private final IndexValueShard[] shards;
		
		public IndexValue(int shardSize, boolean fair) {
			shards = new IndexValueShard[shardSize];
			for (int i = 0; i < shardSize; i++) {
				shards[i] = new IndexValueShard(fair);
			}
		}
		
		public void add(int index, Object keyRef) {
			shards[index].add(keyRef);
		}
		
		public void remove(int index, Object keyRef) {
			shards[index].remove(keyRef);
		}
		
		public ReentrantLock writeLock(int index) {
			return shards[index].lock;
		}
		
		public int size() {
			if (shards.length == 1) {
				Set<Object> r = shards[0].refs;
				if (r == null) {
					return 0;
				} else {
					return r.size();
				}
			} else {
				int ret = 0;
				Set<Object> r = null;
				for (int i = 0; i < shards.length; i++) {
					r = shards[i].refs;
					if (r != null) {
						ret += r.size();
					}
				}
				return ret;
			}
		}
		
		public List<Object> refs() {
			//NullKeyのみであったらNullKeyを返却
			NullKey nullRef = null;
			List<Object> refsAll = new ArrayList<>();
			Set<Object> r = null;
			for (int i = 0; i < shards.length; i++) {
				r = shards[i].refs;
				if (r != null) {
					if (r.size() > 0) {
						for (Object k: r) {
							if (k instanceof NullKey) {
								nullRef = (NullKey) k;
							} else {
								refsAll.add(k);
							}
						}
					}
				}
			}
			if (refsAll.isEmpty() && nullRef != null) {
				refsAll.add(nullRef);
			}
			return refsAll;
		}
		
		public Object firstRef() {
			//NullKeyのみであったらNullKeyを返却
			NullKey nullRef = null;
			Set<Object> r = null;
			for (int i = 0; i < shards.length; i++) {
				r = shards[i].refs;
				if (r != null) {
					if (r.size() > 0) {
						for (Object k: r) {
							if (k instanceof NullKey) {
								nullRef = (NullKey) k;
							} else {
								return k;
							}
						}
					}
				}
			}
			if (nullRef != null) {
				return nullRef;
			} else {
				return null;
			}
		}
	}
	
	private static class IndexValueShard {
		private final ReentrantLock lock;
		private volatile Set<Object> refs;
		
		IndexValueShard(boolean fair) {
			lock = new ReentrantLock(fair);
		}
		
		void add(Object keyRef) {
			Set<Object> r = refs;
			if (r == null) {
				r = new HashSet<>();
			} else {
				HashSet<Object> newRefs = new HashSet<>();
				for (Object ref: r) {
					if (!(ref instanceof NullKey)) {
						newRefs.add(ref);
					}
				}
				r = newRefs;
			}
			r.add(keyRef);
			refs = r;
		}
		
		void remove(Object keyRef) {
			Set<Object> r = refs;
			if (r != null) {
				r = new HashSet<>(r);
				r.remove(keyRef);
				if (r.size() == 0) {
					r = null;
				} else if (r.size() == 1) {
					if (r.iterator().next() instanceof NullKey) {
						r = null;
					}
				}
			}
			refs = r;
		}
	}
}
