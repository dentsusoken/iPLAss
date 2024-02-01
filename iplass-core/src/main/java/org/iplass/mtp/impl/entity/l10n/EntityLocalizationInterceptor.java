/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.entity.l10n;

import java.util.List;
import java.util.function.Predicate;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.interceptor.EntityCountInvocation;
import org.iplass.mtp.entity.interceptor.EntityInsertInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
import org.iplass.mtp.entity.interceptor.EntityLoadInvocation;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.interceptor.EntityInvocationImpl;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.properties.extend.AutoNumberType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityLocalizationInterceptor extends EntityInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(EntityLocalizationInterceptor.class);

	@Override
	public String insert(EntityInsertInvocation invocation) {
		EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
		if (eh.getDataLocalizationStrategyRuntime() != null) {
			if (invocation.getInsertOption() != null && invocation.getInsertOption().isLocalized()) {

				Entity forRet = invocation.getEntity();
				Entity e = ((GenericEntity) forRet).copy();
				InsertOption op = invocation.getInsertOption().copy();
				eh.getDataLocalizationStrategyRuntime().handleEntityForInsert(e, op);

				if (logger.isDebugEnabled()) {
					logger.debug("translate to Local:from=" + forRet + ", to=" + e);
				}

				invocation.setEntity(e);
				invocation.setInsertOption(op);

				String oid = invocation.proceed();

				forRet.setOid(e.getOid());
				forRet.setVersion(e.getVersion());

				//auto numberを通知
				List<PropertyHandler> pList = eh.getPropertyListByPropertyType(AutoNumberType.class, EntityContext.getCurrentContext());
				if (pList != null) {
					for (PropertyHandler ph: pList) {
						forRet.setValue(ph.getName(), e.getValue(ph.getName()));
					}
				}

				return oid;
			} else {
				return invocation.proceed();
			}
		} else {
			return invocation.proceed();
		}

	}

	@Override
	public void update(EntityUpdateInvocation invocation) {
		EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
		if (eh.getDataLocalizationStrategyRuntime() != null) {
			if (invocation.getUpdateOption() != null && invocation.getUpdateOption().isLocalized()) {
				Entity forRet = invocation.getEntity();
				Entity e = ((GenericEntity) forRet).copy();
				UpdateOption op = invocation.getUpdateOption().copy();
				eh.getDataLocalizationStrategyRuntime().handleEntityForUpdate(e, op);

				if (logger.isDebugEnabled()) {
					logger.debug("translate to Local:from=" + forRet + ", to=" + e);
				}

				invocation.setEntity(e);
				invocation.setUpdateOption(op);

				invocation.proceed();

				//バージョンを通知
				forRet.setVersion(e.getVersion());
			} else {
				invocation.proceed();
			}
		} else {
			invocation.proceed();
		}
	}

	@Override
	public Entity load(EntityLoadInvocation invocation) {
		EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
		if (eh.getDataLocalizationStrategyRuntime() != null) {
			if (invocation.getLoadOption() != null && invocation.getLoadOption().isLocalized()) {

				Entity e = invocation.proceed();
				Entity transed = eh.getDataLocalizationStrategyRuntime().handleEntityForLoad(e);

				if (logger.isDebugEnabled()) {
					logger.debug("translate to Local:from=" + e + ", to=" + transed);
				}

				return transed;
			} else {
				return invocation.proceed();
			}
		} else {
			return invocation.proceed();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void query(EntityQueryInvocation invocation) {
		EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
		if (invocation.getQuery().isLocalized()) {
			QueryLocalizer ql = new QueryLocalizer(eh, EntityContext.getCurrentContext());
			Query q = (Query) invocation.getQuery().accept(ql);

			if (logger.isDebugEnabled()) {
				logger.debug("translate to Local:from=" + invocation.getQuery() + ", to=" + q);
			}

			if (invocation.getType() == InvocationType.SEARCH_ENTITY) {
				SearchResultAdapter callback = new SearchResultAdapter(invocation.getQuery(), q, (Predicate<Entity>) invocation.getPredicate());
				invocation.setPredicate(callback);
			}

			invocation.setQuery(q);
			invocation.proceed();
		} else {
			invocation.proceed();
		}
	}

	@Override
	public int count(EntityCountInvocation invocation) {
		EntityHandler eh = ((EntityInvocationImpl<?>) invocation).getEntityHandler();
		if (invocation.getQuery().isLocalized()) {
			QueryLocalizer ql = new QueryLocalizer(eh, EntityContext.getCurrentContext());
			Query q = (Query) invocation.getQuery().accept(ql);

			if (logger.isDebugEnabled()) {
				logger.debug("translate to Local:from=" + invocation.getQuery() + ", to=" + q);
			}

			invocation.setQuery(q);
			return invocation.proceed();
		} else {
			return invocation.proceed();
		}
	}

}
