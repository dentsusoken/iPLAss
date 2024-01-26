/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.cache.store.event;

import org.iplass.mtp.impl.cache.store.CacheEntry;

public final class CacheUpdateEvent extends CacheEvent {
	
	private final CacheEntry previousEntry;
	private final CacheEntry entry;

	public CacheUpdateEvent(CacheEntry previousEntry, CacheEntry entry) {
		super(CacheEventType.UPDATE);
		this.entry = entry;
		this.previousEntry = previousEntry;
	}

	public CacheEntry getEntry() {
		return entry;
	}

	public CacheEntry getPreviousEntry() {
		return previousEntry;
	}

	@Override
	public String toString() {
		return "CacheUpdateEvent [type=" + type + ", previousEntry="
				+ previousEntry + ", entry=" + entry + "]";
	}

}
