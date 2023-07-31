/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.LinkedHashMap;
import java.util.TreeSet;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.builtin.FineGrainedLockIndex.IndexValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FineGrainedLockState implements AutoCloseable {
	private static Logger logger = LoggerFactory.getLogger(FineGrainedLockState.class);
	
	private TargetIndex[] targetIndex;
	
	private CacheEntry newEntry;
	private CacheEntry oldEntry;
	
	private class TargetIndex {
		LinkedHashMap<Object, IndexValue> indexValueMap;
		int shardIndex;
	}
	
	FineGrainedLockState(CacheEntry newEntry, CacheEntry oldEntry, FineGrainedLockIndex[] fineGrainedLockIndex) {
		this.newEntry = newEntry;
		this.oldEntry = oldEntry;
		targetIndex = new TargetIndex[fineGrainedLockIndex.length];
		for (int i = 0; i < targetIndex.length; i++) {
			targetIndex[i] = new TargetIndex();
			TreeSet<Object> sortedIndexValues = new TreeSet<>();
			targetIndex[i].indexValueMap = new LinkedHashMap<>();
			Object key = (newEntry != null) ? newEntry.getKey(): oldEntry.getKey();
			targetIndex[i].shardIndex = fineGrainedLockIndex[i].shardIndex(key);
			
			if (newEntry != null) {
				addToSortedIndexValues(sortedIndexValues, newEntry.getIndexValue(i));
			}
			if (oldEntry != null) {
				addToSortedIndexValues(sortedIndexValues, oldEntry.getIndexValue(i));
			}
			
			for (Object ival: sortedIndexValues) {
				targetIndex[i].indexValueMap.put(ival, fineGrainedLockIndex[i].getIndexValue(ival, true));
			}
			
			for (IndexValue iv: targetIndex[i].indexValueMap.values()) {
				iv.writeLock(targetIndex[i].shardIndex).lock();
			}
		}

	}
	
	private void addToSortedIndexValues(TreeSet<Object> sortedIndexValues, Object ival) {
		if (ival instanceof Object[]) {
			for (Object o: (Object[]) ival) {
				sortedIndexValues.add(o);
			}
		} else {
			if (ival != null) {
				sortedIndexValues.add(ival);
			}
		}
	}
	
	public void maintain() {
		for (int i = 0; i < targetIndex.length; i++) {
			if (oldEntry != null) {
				Object ival = oldEntry.getIndexValue(i);
				if (ival instanceof Object[]) {
					for (Object o: (Object[]) ival) {
						IndexValue iv = targetIndex[i].indexValueMap.get(o);
						iv.remove(targetIndex[i].shardIndex, oldEntry.getKey());
					}
				} else {
					if (ival != null) {
						IndexValue iv = targetIndex[i].indexValueMap.get(ival);
						iv.remove(targetIndex[i].shardIndex, oldEntry.getKey());
					}
				}
			}
			if (newEntry != null) {
				Object ival = newEntry.getIndexValue(i);
				if (ival instanceof Object[]) {
					for (Object o: (Object[]) ival) {
						IndexValue iv = targetIndex[i].indexValueMap.get(o);
						iv.add(targetIndex[i].shardIndex, newEntry.getKey());
					}
				} else {
					if (ival != null) {
						IndexValue iv = targetIndex[i].indexValueMap.get(ival);
						iv.add(targetIndex[i].shardIndex, newEntry.getKey());
					}
				}
			}
		}
	}

	@Override
	public void close() {
		for (int i = 0; i < targetIndex.length; i++) {
			for (IndexValue iv: targetIndex[i].indexValueMap.values()) {
				try {
					iv.writeLock(targetIndex[i].shardIndex).unlock();
				} catch (IllegalMonitorStateException e) {
					logger.warn("Illegal Lock State in FineGrainedLock." + e, e);
				}
			}
		}
	}

}
