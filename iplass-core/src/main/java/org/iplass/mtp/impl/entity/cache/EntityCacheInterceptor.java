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

package org.iplass.mtp.impl.entity.cache;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateResult;
import org.iplass.mtp.entity.interceptor.EntityBulkUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityCountInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteInvocation;
import org.iplass.mtp.entity.interceptor.EntityInsertInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
import org.iplass.mtp.entity.interceptor.EntityLoadInvocation;
import org.iplass.mtp.entity.interceptor.EntityLockByUserInvocation;
import org.iplass.mtp.entity.interceptor.EntityPurgeInvocation;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.EntityRestoreInvocation;
import org.iplass.mtp.entity.interceptor.EntityUnlockByUserInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityValidateInvocation;


/**
 * EntityのキャッシュInterceptor。
 *
 * @author K.Higuchi
 *
 */
public class EntityCacheInterceptor extends EntityInterceptorAdapter {
	public static final String QUERY_CACHE_NAMESPACE_PREFIX = "mtp.entity.queryCache/";
	public static final String KEEP_QUERY_CACHE_NAMESPACE_PREFIX = "mtp.entity.keepQueryCache/";
	public static final String LOCAL_QUERY_CACHE_NAMESPACE = "mtp.entity.transactionLocalQueryCache";
	public static final String LOCAL_LOAD_CACHE_NAMESPACE = "mtp.entity.transactionLocalCache";

	TransactionLocalLoadCacheInterceptor transactionLocalLoadCacheInterceptor;
	
	public EntityCacheInterceptor() {
		transactionLocalLoadCacheInterceptor = new TransactionLocalLoadCacheInterceptor();
	}

	@Override
	public ValidateResult validate(EntityValidateInvocation invocation) {
		return transactionLocalLoadCacheInterceptor.validate(invocation);
	}

	@Override
	public String insert(EntityInsertInvocation invocation) {
		return transactionLocalLoadCacheInterceptor.insert(invocation);
	}

	@Override
	public void update(EntityUpdateInvocation invocation) {
		transactionLocalLoadCacheInterceptor.update(invocation);
	}

	@Override
	public void delete(EntityDeleteInvocation invocation) {
		transactionLocalLoadCacheInterceptor.delete(invocation);
	}

	@Override
	public Entity load(EntityLoadInvocation invocation) {
		return transactionLocalLoadCacheInterceptor.load(invocation);
	}

	@Override
	public void query(EntityQueryInvocation invocation) {
		transactionLocalLoadCacheInterceptor.query(invocation);
	}

	@Override
	public int count(EntityCountInvocation invocation) {
		return transactionLocalLoadCacheInterceptor.count(invocation);
	}

	@Override
	public int updateAll(EntityUpdateAllInvocation invocation) {
		return transactionLocalLoadCacheInterceptor.updateAll(invocation);
	}

	@Override
	public int deleteAll(EntityDeleteAllInvocation invocation) {
		return transactionLocalLoadCacheInterceptor.deleteAll(invocation);
	}

	@Override
	public void purge(EntityPurgeInvocation invocation) {
		transactionLocalLoadCacheInterceptor.purge(invocation);
	}

	@Override
	public Entity restore(EntityRestoreInvocation invocation) {
		return transactionLocalLoadCacheInterceptor.restore(invocation);
	}

	@Override
	public boolean lockByUser(EntityLockByUserInvocation invocation) {
		return transactionLocalLoadCacheInterceptor.lockByUser(invocation);
	}

	@Override
	public boolean unlockByUser(EntityUnlockByUserInvocation invocation) {
		return transactionLocalLoadCacheInterceptor.unlockByUser(invocation);
	}

	@Override
	public void bulkUpdate(EntityBulkUpdateInvocation invocation) {
		transactionLocalLoadCacheInterceptor.bulkUpdate(invocation);
	}
	
}
