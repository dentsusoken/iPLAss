/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.cache.store;

public abstract class CacheStoreFactory {
	
	private String namespace;
	private String namespacePattern;
	private int indexCount = 0;
	
	public int getIndexCount() {
		return indexCount;
	}

	public void setIndexCount(int indexCount) {
		this.indexCount = indexCount;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespacePattern() {
		return namespacePattern;
	}

	public void setNamespacePattern(String namespacePattern) {
		this.namespacePattern = namespacePattern;
	}

	public abstract CacheStore createCacheStore(String namespace);
	
	public abstract boolean canUseForLocalCache();
	
	public abstract boolean supportsIndex();
	
	public abstract CacheHandler createCacheHandler(CacheStore store);
	
	public abstract CacheStoreFactory getLowerLevel();
}
