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

package org.iplass.mtp.impl.entity.contentcache;

import java.sql.Timestamp;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.interceptor.EntityBulkUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityCountInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteInvocation;
import org.iplass.mtp.entity.interceptor.EntityInsertInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
import org.iplass.mtp.entity.interceptor.EntityLoadInvocation;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.EntityRestoreInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.web.actionmapping.cache.ContentCacheContext;

public class ContentCacheInterceptor extends EntityInterceptorAdapter {

	@Override
	public String insert(EntityInsertInvocation invocation) {
		String ret = super.insert(invocation);
		ContentCacheContext.getContentCacheContext().invalidateByEntityNameAndOid(invocation.getEntityDefinition().getName(), ret);
		ContentCacheContext.getContentCacheContext().invalidateByEntityNameAndOid(invocation.getEntityDefinition().getName(), ContentCacheContext.HOLE_ENTITY);
		return ret;
	}

	@Override
	public void update(EntityUpdateInvocation invocation) {
		super.update(invocation);
		ContentCacheContext.getContentCacheContext().invalidateByEntityNameAndOid(invocation.getEntityDefinition().getName(), invocation.getEntity().getOid());
		ContentCacheContext.getContentCacheContext().invalidateByEntityNameAndOid(invocation.getEntityDefinition().getName(), ContentCacheContext.HOLE_ENTITY);
	}

	@Override
	public void delete(EntityDeleteInvocation invocation) {
		super.delete(invocation);
		ContentCacheContext.getContentCacheContext().invalidateByEntityNameAndOid(invocation.getEntityDefinition().getName(), invocation.getEntity().getOid());
		ContentCacheContext.getContentCacheContext().invalidateByEntityNameAndOid(invocation.getEntityDefinition().getName(), ContentCacheContext.HOLE_ENTITY);
	}

	@Override
	public int updateAll(EntityUpdateAllInvocation invocation) {
		int ret = super.updateAll(invocation);
		ContentCacheContext.getContentCacheContext().invalidateByEntityName(invocation.getUpdateCondition().getDefinitionName());
		return ret;
	}

	@Override
	public void bulkUpdate(EntityBulkUpdateInvocation invocation) {
		super.bulkUpdate(invocation);
		ContentCacheContext.getContentCacheContext().invalidateByEntityName(invocation.getBulkUpdatable().getDefinitionName());
	}

	@Override
	public int deleteAll(EntityDeleteAllInvocation invocation) {
		int ret = super.deleteAll(invocation);
		ContentCacheContext.getContentCacheContext().invalidateByEntityName(invocation.getDeleteCondition().getDefinitionName());
		return ret;
	}

	@Override
	public Entity load(EntityLoadInvocation invocation) {
		Entity ret = super.load(invocation);
		if (invocation.getEntityDefinition().getVersionControlType() == VersionControlType.TIMEBASE) {
			Long expires = null;
			if (ret != null && ret.getEndDate() != null) {
				expires = ret.getEndDate().getTime() + 1;//ミリ秒以下を切り上げ
			}
			ContentCacheContext.getContentCacheContext().record(invocation.getEntityDefinition().getName(), invocation.getOid(), expires);
		} else {
			ContentCacheContext.getContentCacheContext().record(invocation.getEntityDefinition().getName(), invocation.getOid(), null);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void query(final EntityQueryInvocation invocation) {
		final ContentCacheContext ccc = ContentCacheContext.getContentCacheContext();
		boolean isRecordEntity = ccc.isRecordEntity(invocation.getEntityDefinition().getName());
		if (isRecordEntity) {
			final boolean isTimebase = invocation.getEntityDefinition().getVersionControlType() == VersionControlType.TIMEBASE;
			final boolean isSearchEntity = invocation.getType() == InvocationType.SEARCH_ENTITY;
			final Predicate<Object> real = (Predicate<Object>) invocation.getPredicate();
			final int[] indexes = new int[]{-1, -1};//Object[]の場合のoid、endDateのindex
			if (!isSearchEntity) {
				List<ValueExpression> vl = invocation.getQuery().getSelect().getSelectValues();
				for (int i = 0; i < vl.size(); i++) {
					ValueExpression v = vl.get(i);
					if (v instanceof EntityField) {
						String ef = ((EntityField) v).getPropertyName();
						if (Entity.OID.equals(ef)) {
							indexes[0] = i;
						} else if (Entity.END_DATE.equals(ef)) {
							indexes[1] = i;
						}
					}
				}
			}
			invocation.setPredicate(new Predicate<Object>() {

				@Override
				public boolean test(Object dataModel) {

					boolean res = real.test(dataModel);

					if (isSearchEntity) {
						Entity e = (Entity) dataModel;
						if (e.getOid() != null) {
							Long expires = null;
							if (isTimebase && e.getEndDate() != null) {
								expires = e.getEndDate().getTime() + 1;//ミリ秒以下を切り上げ
							}
							ccc.record(invocation.getEntityDefinition().getName(), e.getOid(), expires);
						}
					} else {
						Object[] o = (Object[]) dataModel;
						String oid = null;
						Long expires = null;
						if (indexes[0] != -1) {
							oid = (String) o[indexes[0]];
						}
						if (indexes[1] != -1 && o[indexes[1]] != null) {
							expires = ((Timestamp) o[indexes[1]]).getTime() + 1;
						}
						if (oid != null) {
							ccc.record(invocation.getEntityDefinition().getName(), oid, expires);
						}
					}

					return res;
				}
			});

		}
		super.query(invocation);
		if (isRecordEntity) {
			ccc.record(invocation.getEntityDefinition().getName(), ContentCacheContext.HOLE_ENTITY, null);
		}
	}

	@Override
	public int count(EntityCountInvocation invocation) {
		int ret = super.count(invocation);
		ContentCacheContext.getContentCacheContext().record(invocation.getEntityDefinition().getName(), ContentCacheContext.HOLE_ENTITY, null);
		return ret;
	}

	@Override
	public Entity restore(EntityRestoreInvocation invocation) {
		Entity ret = super.restore(invocation);
		ContentCacheContext.getContentCacheContext().invalidateByEntityNameAndOid(invocation.getEntityDefinition().getName(), ret.getOid());
		ContentCacheContext.getContentCacheContext().invalidateByEntityNameAndOid(invocation.getEntityDefinition().getName(), ContentCacheContext.HOLE_ENTITY);
		return ret;
	}

}
