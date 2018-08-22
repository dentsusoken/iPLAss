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

package org.iplass.mtp.impl.web.actionmapping.cache;

import java.io.Serializable;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EntityContentMap implements Serializable {
	private static final long serialVersionUID = -3806165452908206540L;
	
	private final String entityName;//*指定もある。
	private final String oid;
	
	//バージョン単位ではキャッシュしない。oid単位でのキャッシュ
	
	private long invalidationTime;
	private HashSet<String> contentCacheKeys;
	
	/**
	 * 使う方で、lockを適切に扱うように。
	 */
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public EntityContentMap(String entityName, String oid) {
		this.entityName = entityName;
		this.oid = oid;
	}
	
	public long getInvalidationTime() {
		return invalidationTime;
	}

	public void setInvalidationTime(long invalidationTime) {
		this.invalidationTime = invalidationTime;
	}
	
	public ReentrantReadWriteLock getLock() {
		return lock;
	}
	
	public HashSet<String> getContentCacheKeys() {
		return contentCacheKeys;
	}
	
	public void addContentCacheKeys(String contentCacheKey) {
		if (contentCacheKeys == null) {
			contentCacheKeys = new HashSet<String>();
		}
		contentCacheKeys.add(contentCacheKey);
	}
	
	public void setContentCacheKeys(HashSet<String> contentCacheKeys) {
		this.contentCacheKeys = contentCacheKeys;
	}


	public String getEntityName() {
		return entityName;
	}
	public String getOid() {
		return oid;
	}
	
}
