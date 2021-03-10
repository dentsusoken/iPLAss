/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.auditlog;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.interceptor.EntityBulkUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityCountInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteInvocation;
import org.iplass.mtp.entity.interceptor.EntityInsertInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
import org.iplass.mtp.entity.interceptor.EntityPurgeInvocation;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.EntityRestoreInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateInvocation;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.interceptor.EntityDeleteInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityLoadInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityUpdateInvocationImpl;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;

public class AuditLogInterceptor extends EntityInterceptorAdapter implements ServiceInitListener<EntityService> {
	
	private AuditLoggingService historyLoggingService;
	
	@Override
	public int count(EntityCountInvocation invocation) {
		historyLoggingService.logQuery(invocation.getQuery(), true);
		return super.count(invocation);
	}

	@Override
	public void query(EntityQueryInvocation invocation) {
		historyLoggingService.logQuery(invocation.getQuery(), false);
		super.query(invocation);
	}

	@Override
	public String insert(EntityInsertInvocation invocation) {
		String ret = invocation.proceed();
		historyLoggingService.logInsert(invocation.getEntity());
		return ret;
	}

	@Override
	public void update(EntityUpdateInvocation invocation) {
		EntityHandler eh = ((EntityUpdateInvocationImpl) invocation).getEntityHandler();
		Entity beforeUpdate = null;
		if (historyLoggingService.isLogBeforeEntity(eh.getMetaData().getName())) {
			beforeUpdate = new EntityLoadInvocationImpl(invocation.getEntity().getOid(), invocation.getEntity().getVersion(), new LoadOption(true, false), false, eh.getService().getInterceptors(), eh).proceed();
		}
		invocation.proceed();
		historyLoggingService.logUpdate(beforeUpdate, invocation.getEntity(), invocation.getUpdateOption());
	}

	@Override
	public void delete(EntityDeleteInvocation invocation) {
		Entity entity = null;
		EntityHandler eh = ((EntityDeleteInvocationImpl) invocation).getEntityHandler();
		if (historyLoggingService.isLogBeforeEntity(eh.getMetaData().getName())) {
			//削除前の状態のEntityの情報がほしいので、ロードする。
			entity = new EntityLoadInvocationImpl(invocation.getEntity().getOid(), invocation.getEntity().getVersion(), new LoadOption(true, false), false, eh.getService().getInterceptors(), eh).proceed();
		}
		invocation.proceed();
		if (entity == null) {
			entity = invocation.getEntity();
		}
		historyLoggingService.logDelete(entity, invocation.getDeleteOption());
	}

	@Override
	public int updateAll(EntityUpdateAllInvocation invocation) {
		Integer ret = invocation.proceed();
		historyLoggingService.logUpdateAll(invocation.getUpdateCondition());
		return ret;
	}

	@Override
	public int deleteAll(EntityDeleteAllInvocation invocation) {
		Integer ret = invocation.proceed();
		historyLoggingService.logDeleteAll(invocation.getDeleteCondition());
		return ret;
	}

	@Override
	public void purge(EntityPurgeInvocation invocation) {
		invocation.proceed();
		historyLoggingService.logPurge(invocation.getRecycleBinId());
	}

	@Override
	public Entity restore(EntityRestoreInvocation invocation) {
		Entity e = invocation.proceed();
		historyLoggingService.logRestore(e.getOid(), e.getDefinitionName(), invocation.getRecycleBinId());
		return e;
	}

	@Override
	public void bulkUpdate(EntityBulkUpdateInvocation invocation) {
		invocation.proceed();
		historyLoggingService.logBulkUpdate(invocation.getBulkUpdatable().getDefinitionName());
	}

	@Override
	public void inited(EntityService service, Config config) {
		historyLoggingService = config.getDependentService(AuditLoggingService.class);
	}

	@Override
	public void destroyed() {
	}

}
