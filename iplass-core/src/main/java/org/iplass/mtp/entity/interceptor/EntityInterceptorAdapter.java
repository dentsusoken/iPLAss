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

package org.iplass.mtp.entity.interceptor;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateResult;

/**
 * EntityInterceptorのアダプタ。
 * InvocationType毎に別メソッドとして、デフォルト実装（何もせず後続処理呼び出し）を提供。
 * 
 * @author K.Higuchi
 *
 */
public class EntityInterceptorAdapter implements EntityInterceptor {
	
	@Override
	public final Object intercept(EntityInvocation<?> invocation) {
		switch (invocation.getType()) {
		case COUNT:
			return count((EntityCountInvocation) invocation);
		case DELETE:
			delete((EntityDeleteInvocation) invocation);
			return null;
		case DELETE_ALL:
			return deleteAll((EntityDeleteAllInvocation) invocation);
		case INSERT:
			return insert((EntityInsertInvocation) invocation);
		case LOAD:
			return load((EntityLoadInvocation) invocation);
		case SEARCH:
		case SEARCH_ENTITY:
			query((EntityQueryInvocation) invocation);
			return null;
		case UPDATE:
			update((EntityUpdateInvocation) invocation);
			return null;
		case UPDATE_ALL:
			return updateAll((EntityUpdateAllInvocation) invocation);
		case BULK_UPDATE:
			bulkUpdate((EntityBulkUpdateInvocation) invocation);
			return null;
		case VALIDATE:
			return validate((EntityValidateInvocation) invocation);
		case GET_RECYCLE_BIN:
			getRecycleBin((EntityGetRecycleBinInvocation) invocation);
			return null;
		case PURGE:
			purge((EntityPurgeInvocation) invocation);
			return null;
		case RESTORE:
			return restore((EntityRestoreInvocation) invocation);
		case LOCK_BY_USER:
			return lockByUser((EntityLockByUserInvocation) invocation);
		case UNLOCK_BY_USER:
			return unlockByUser((EntityUnlockByUserInvocation) invocation);
		case NORMALIZE:
			normalize((EntityNormalizeInvocation) invocation);
			return null;
		default:
			throw new IllegalArgumentException("not support method");
		}
	}

	/**
	 * {@link InvocationType#VALIDATE}の際、呼び出される。
	 * 
	 * @param invocation
	 * @return
	 */
	public ValidateResult validate(EntityValidateInvocation invocation) {
		return invocation.proceed();
	}

	/**
	 * {@link InvocationType#INSERT}の際、呼び出される。
	 * 
	 * @param invocation
	 * @return
	 */
	public String insert(EntityInsertInvocation invocation) {
		return invocation.proceed();
	}

	/**
	 * {@link InvocationType#UPDATE}の際、呼び出される。
	 * 
	 * @param invocation
	 */
	public void update(EntityUpdateInvocation invocation) {
		invocation.proceed();
	}

	/**
	 * {@link InvocationType#DELETE}の際、呼び出される。
	 * 
	 * @param invocation
	 */
	public void delete(EntityDeleteInvocation invocation) {
		invocation.proceed();
	}

	/**
	 * {@link InvocationType#LOAD}の際、呼び出される。
	 * 
	 * @param invocation
	 * @return
	 */
	public Entity load(EntityLoadInvocation invocation) {
		return invocation.proceed();
	}

	/**
	 * {@link InvocationType#SEARCH}、{@link InvocationType#SEARCH_ENTITY}の際、呼び出される。
	 * 
	 * @param invocation
	 */
	public void query(EntityQueryInvocation invocation) {
		invocation.proceed();
	}

	/**
	 * {@link InvocationType#COUNT}の際、呼び出される。
	 * 
	 * @param invocation
	 * @return
	 */
	public int count(EntityCountInvocation invocation) {
		return invocation.proceed();
	}

	/**
	 * {@link InvocationType#UPDATE_ALL}の際、呼び出される。
	 * 
	 * @param invocation
	 * @return
	 */
	public int updateAll(EntityUpdateAllInvocation invocation) {
		return invocation.proceed();
	}

	/**
	 * {@link InvocationType#BULK_UPDATE}の際、呼び出される。
	 * 
	 * @param invocation
	 * @return
	 */
	public void bulkUpdate(EntityBulkUpdateInvocation invocation) {
		invocation.proceed();
	}
	
	/**
	 * {@link InvocationType#DELETE_ALL}の際、呼び出される。
	 * 
	 * @param invocation
	 * @return
	 */
	public int deleteAll(EntityDeleteAllInvocation invocation) {
		return invocation.proceed();
	}
	
	/**
	 * {@link InvocationType#GET_RECYCLE_BIN}の際、呼び出される。
	 * 
	 * @param invocation
	 */
	public void getRecycleBin(EntityGetRecycleBinInvocation invocation) {
		invocation.proceed();
	}

	/**
	 * {@link InvocationType#PURGE}の際、呼び出される。
	 * 
	 * @param invocation
	 */
	public void purge(EntityPurgeInvocation invocation) {
		invocation.proceed();
	}

	/**
	 * {@link InvocationType#RESTORE}の際、呼び出される。
	 * 
	 * @param invocation
	 * @return Entity
	 */
	public Entity restore(EntityRestoreInvocation invocation) {
		return invocation.proceed();
	}

	public boolean lockByUser(EntityLockByUserInvocation invocation) {
		return invocation.proceed();
	}

	public boolean unlockByUser(EntityUnlockByUserInvocation invocation) {
		return invocation.proceed();
	}

	public void normalize(EntityNormalizeInvocation invocation) {
		invocation.proceed();
	}

}
