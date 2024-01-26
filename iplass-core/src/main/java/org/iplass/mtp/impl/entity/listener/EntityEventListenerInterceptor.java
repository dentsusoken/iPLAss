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

package org.iplass.mtp.impl.entity.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.ValidateResult;
import org.iplass.mtp.entity.interceptor.EntityDeleteInvocation;
import org.iplass.mtp.entity.interceptor.EntityInsertInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
import org.iplass.mtp.entity.interceptor.EntityLoadInvocation;
import org.iplass.mtp.entity.interceptor.EntityPurgeInvocation;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.EntityRestoreInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityValidateInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEventListener.EventListenerRuntime;
import org.iplass.mtp.impl.entity.interceptor.EntityInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityLoadInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityValidateInvocationImpl;

public class EntityEventListenerInterceptor extends EntityInterceptorAdapter {

	@Override
	public ValidateResult validate(EntityValidateInvocation invocation) {
		EntityValidateInvocationImpl invImpl = (EntityValidateInvocationImpl) invocation;
		EntityHandler eh = invImpl.getEntityHandler();
		EntityEventContext eeContext = null;
		if (eh.getEventListenerHandlers() != null && eh.getEventListenerHandlers().size() > 0) {
			eeContext = new EntityEventContext();
			if (invocation.getValidatePropertyList() != null) {
				eeContext.setAttribute(EntityEventContext.VALIDATE_PROPERTIES, new ArrayList<String>(invocation.getValidatePropertyList()));
			}
			if (invImpl.getUpdateOption() != null) {
				eeContext.setAttribute(EntityEventContext.UPDATE_OPTION, invImpl.getUpdateOption());
			}
			if (invImpl.getInsertOption() != null) {
				eeContext.setAttribute(EntityEventContext.INSERT_OPTION, invImpl.getInsertOption());
			}
			for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
				eeh.handleBeforeValidate(invocation.getEntity(), eeContext);
			}
		}
		return invocation.proceed();
	}

	@Override
	public String insert(EntityInsertInvocation invocation) {
		if (invocation.getInsertOption().isNotifyListeners()) {
			EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
			EntityEventContext eeContext = null;
			if (eh.getEventListenerHandlers() != null && eh.getEventListenerHandlers().size() > 0) {
				eeContext = new EntityEventContext();
				eeContext.setAttribute(EntityEventContext.INSERT_OPTION, invocation.getInsertOption());

				for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
					if (!eeh.handleBeforeInsert(invocation.getEntity(), eeContext)) {
						//do nothing
						return null;
					}
				}
			}

			String oid = invocation.proceed();

			if (eeContext != null) {//実行順は先頭から
				for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
					eeh.handleAfterInsert(invocation.getEntity(), eeContext);
				}
			}

			return oid;
		} else {
			return invocation.proceed();
		}
	}

	private boolean loadWithMappedBy(List<EventListenerRuntime> elList) {
		for (EventListenerRuntime elr: elList) {
			if (!elr.getMetaData().isWithoutMappedByReference()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void update(EntityUpdateInvocation invocation) {
		if (invocation.getUpdateOption().isNotifyListeners()) {
			EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
			EntityEventContext eeContext = null;
			if (eh.getEventListenerHandlers() != null && eh.getEventListenerHandlers().size() > 0) {
				eeContext = new EntityEventContext();
				eeContext.setAttribute(EntityEventContext.UPDATE_OPTION, invocation.getUpdateOption());
				Entity beforeUpdate = null;
				if (loadWithMappedBy(eh.getEventListenerHandlers())) {
					beforeUpdate = new EntityLoadInvocationImpl(invocation.getEntity().getOid(), invocation.getEntity().getVersion(), null, false, eh.getService().getInterceptors(), eh).proceed();
				} else {
					beforeUpdate = new EntityLoadInvocationImpl(invocation.getEntity().getOid(), invocation.getEntity().getVersion(), new LoadOption(true, false), false, eh.getService().getInterceptors(), eh).proceed();
				}
				eeContext.setAttribute(EntityEventContext.BEFORE_UPDATE_ENTITY, beforeUpdate);
				for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
					if (!eeh.handleBeforeUpdate(invocation.getEntity(), eeContext)) {
						//do nothing
						return;
					}
				}
			}

			invocation.proceed();

			if (eeContext != null) {//実行順は先頭から
				for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
					eeh.handleAfterUpdate(invocation.getEntity(), eeContext);
				}
			}
		} else {
			invocation.proceed();
		}
	}

	@Override
	public void delete(EntityDeleteInvocation invocation) {
		if (invocation.getDeleteOption().isNotifyListeners()) {
			EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
			EntityEventContext eeContext = null;
			if (eh.getEventListenerHandlers() != null && eh.getEventListenerHandlers().size() > 0) {
				eeContext = new EntityEventContext();
				eeContext.setAttribute(EntityEventContext.DELETE_OPTION, invocation.getDeleteOption());
				for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
					if (!eeh.handleBeforeDelete(invocation.getEntity(), eeContext)) {
						//do nothing
						return;
					}
				}
			}

			invocation.proceed();

			if (eeContext != null) {
				for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
					eeh.handleAfterDelete(invocation.getEntity(), eeContext);
				}
				if (invocation.getDeleteOption().isPurge()) {
					for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
						eeh.handleAfterPurge(invocation.getEntity());
					}
				}
			}
		} else {
			invocation.proceed();
		}
	}

	@Override
	public Entity load(EntityLoadInvocation invocation) {
		Entity entity = invocation.proceed();
		if (entity != null) {
			if (invocation.getLoadOption() == null || invocation.getLoadOption().isNotifyListeners()) {
				EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
				if (eh.getEventListenerHandlers() != null && eh.getEventListenerHandlers().size() > 0) {
					for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
						eeh.handleOnLoad(entity);
					}
				}
			}
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void query(final EntityQueryInvocation invocation) {
		if (invocation.getType() == InvocationType.SEARCH_ENTITY) {
			if (invocation.getSearchOption().isNotifyListeners()) {
				final EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
				if (eh.getEventListenerHandlers() != null && eh.getEventListenerHandlers().size() > 0) {
					final Predicate<Entity> actual = ((Predicate<Entity>) invocation.getPredicate());
					Predicate<Entity> wrapper = new Predicate<Entity>() {
						@Override
						public boolean test(Entity entity) {

							for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
								eeh.handleOnLoad(entity);
							}
							return actual.test(entity);
						}
					};
					invocation.setPredicate(wrapper);
				}
			}
		}
		invocation.proceed();
	}

	@Override
	public Entity restore(EntityRestoreInvocation invocation) {
		Entity entity = invocation.proceed();
		EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
		if (eh.getEventListenerHandlers() != null && eh.getEventListenerHandlers().size() > 0) {
			for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
				eeh.handleAfterRestore(entity);
			}
		}
		return entity;
	}

	@Override
	public void purge(EntityPurgeInvocation invocation) {
		EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
		Entity entity = null;
		if (eh.getEventListenerHandlers() != null && eh.getEventListenerHandlers().size() > 0) {
			Entity ee = new GenericEntity();
			eh.getRecycleBin(
					new Predicate<Entity>() {
						@Override
						public boolean test(Entity dataModel) {
							ee.setOid(dataModel.getOid());
							ee.setName(dataModel.getName());
							ee.setRecycleBinId(dataModel.getRecycleBinId());
							ee.setUpdateDate(dataModel.getUpdateDate());
							ee.setUpdateBy(dataModel.getUpdateBy());
							return false;
						}
					}, invocation.getRecycleBinId());
			entity = ee;
		}
		invocation.proceed();
		if (eh.getEventListenerHandlers() != null && eh.getEventListenerHandlers().size() > 0) {
			for (EventListenerRuntime eeh: eh.getEventListenerHandlers()) {
				eeh.handleAfterPurge(entity);
			}
		}
	}
}
