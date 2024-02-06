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

package org.iplass.mtp.impl.entity;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.transaction.TransactionStatus;





public class EntityContext {
	
	//TODO MetaDataContextができたので、存在意義が薄れてきている、、、StoreStrategyへ渡す際のインタフェースとしてのみ利用がよい
	
	private int localTenantId;
	private int sharedTenantId;
	private EntityService service;
//	private Map<String, EntityHandler> transactionLocalCacheById;
//	private Map<String, EntityHandler> transactionLocalCacheByName;
	
//	private Timestamp currentTimestamp;
	
	public EntityContext(int localTenantId, int sharedTenantId, EntityService service) {
		this.localTenantId = localTenantId;
		this.sharedTenantId = sharedTenantId;
		this.service = service;
//		transactionLocalCacheById = new HashMap<String, EntityHandler>();
//		transactionLocalCacheByName = new HashMap<String, EntityHandler>();
	}
	
	public int getLocalTenantId() {
		return localTenantId;
	}
	
	public int getSharedTenantId() {
		return sharedTenantId;
	}
	
	public int getTenantId(EntityHandler eh) {
		if (eh.isUseSharedData()) {
			return sharedTenantId;
		} else {
			return localTenantId;
		}
	}
	
	public EntityHandler getHandlerByName(String name) {
		return service.getRuntimeByName(name);
	}
	
	public EntityHandler getHandlerById(String id) {
		return service.getRuntimeById(id);
	}
	
	//TODO このメソッドはできれば消したい。。。
	public void refreshTransactionLocalCache(String id) {
		
		MetaDataContext.getContext().refreshTransactionLocalCache(id);
	}

	public static EntityContext getCurrentContext() {
		
		TenantContext tenantContext = ExecuteContext.getCurrentContext().getTenantContext();
	
		//TODO EntityContextは、スレッドに紐付くものでなく、トランザクション×テナント毎に紐付くもの（ということで良いか？）
		TransactionManager tm = ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager();
		if (tm != null) {
			Transaction t = tm.currentTransaction();
			if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
	
				String key = EntityContext.class.getName() + ":" + tenantContext.getTenantId();
	
				EntityContext metaContext = (EntityContext) t.getAttribute(key);
				if (metaContext == null) {
					int sharedTenantId = ServiceRegistry.getRegistry().getService(TenantContextService.class).getSharedTenantId();
					metaContext = new EntityContext(tenantContext.getTenantId(), sharedTenantId, ServiceRegistry.getRegistry().getService(EntityService.class));
					t.setAttribute(key, metaContext);
				}
				return metaContext;
			}
		}
	
		int sharedTenantId = ServiceRegistry.getRegistry().getService(TenantContextService.class).getSharedTenantId();
		return new EntityContext(tenantContext.getTenantId(), sharedTenantId, ServiceRegistry.getRegistry().getService(EntityService.class));
	}
}
