/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.detail.handler;

import java.util.ArrayList;
import java.util.function.Predicate;

import org.iplass.gem.command.generic.detail.DetailFormViewData;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.auth.EntityAuthContext;
import org.iplass.mtp.impl.entity.interceptor.EntityQueryInvocationImpl;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * 詳細・編集画面のボタンをEntityの参照可能範囲設定で制御する
 *
 */
public class CheckPermissionLimitConditionOfButtonHandler extends DetailFormViewAdapter {

	@Override
	protected void onShowDetailView(ShowDetailLayoutViewEvent event) {

		DetailFormViewData detailFormViewData = event.getDetailFormViewData();
		AuthContextHolder user = AuthContextHolder.getAuthContext();
		EntityHandler handler = ServiceRegistry.getRegistry().getService(EntityService.class).getRuntimeByName(event.getEntityName());

		if (detailFormViewData.getEntity() != null && detailFormViewData.getEntity().getOid() != null && !user.isPrivilegedExecution()) {
			//更新権限チェック
			checkUpdatePermission(handler, detailFormViewData, user);

			//削除権限チェック(編集ボタンの制御で参照)
			checkDeletePermission(handler, detailFormViewData, user);
		}

	}

	@Override
	protected void onShowEditView(ShowDetailLayoutViewEvent event) {

		DetailFormViewData detailFormViewData = event.getDetailFormViewData();
		AuthContextHolder user = AuthContextHolder.getAuthContext();
		EntityHandler handler = ServiceRegistry.getRegistry().getService(EntityService.class).getRuntimeByName(event.getEntityName());

		if (detailFormViewData.getEntity() != null && detailFormViewData.getEntity().getOid() != null && !user.isPrivilegedExecution()) {
			//更新権限チェック
			checkUpdatePermission(handler, detailFormViewData, user);

			//削除権限チェック
			checkDeletePermission(handler, detailFormViewData, user);
		}

	}

	private void checkUpdatePermission(EntityHandler handler, DetailFormViewData detailFormViewData, AuthContextHolder user) {
		EntityPermission permission = new EntityPermission(handler.getMetaData().getName(), EntityPermission.Action.UPDATE);
		if (!detailFormViewData.isCanUpdate()) {
			//編集不可
			return;
		}

		//更新可能なデータ範囲か？
		detailFormViewData.setCanUpdate(checkLimitCondition(handler, detailFormViewData, permission, user));
	}

	private void checkDeletePermission(EntityHandler handler, DetailFormViewData detailFormViewData, AuthContextHolder user) {
		EntityPermission permission = new EntityPermission(handler.getMetaData().getName(), EntityPermission.Action.DELETE);
		if (!detailFormViewData.isCanDelete()) {
			//削除不可
			return;
		}

		//削除可能なデータ範囲か？
		detailFormViewData.setCanDelete(checkLimitCondition(handler, detailFormViewData, permission, user));
	}

	private boolean checkLimitCondition(final EntityHandler handler, DetailFormViewData detailFormViewData, EntityPermission permission, AuthContextHolder user) {
		EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(permission);
		if (eac.hasLimitCondition(permission, user)) {
			boolean versionSpecified = handler.isVersioned() && detailFormViewData.getEntity().getVersion() != null;
			Query q = new Query().select(Entity.OID).from(handler.getMetaData().getName());
			if (versionSpecified) {
				q.select().add(Entity.VERSION);
			}

			Condition cond;
			if (versionSpecified) {
				cond = new And(new Equals(Entity.OID, detailFormViewData.getEntity().getOid()), new Equals(Entity.VERSION, detailFormViewData.getEntity().getVersion()));
			} else {
				cond = new Equals(Entity.OID, detailFormViewData.getEntity().getOid());
			}

			q.where(cond);

			final Query transQuery = eac.modifyQuery(q, permission.getAction(), user);

			final ArrayList<String> oids = new ArrayList<>(1);
			AuthContext.doPrivileged(() -> {
				new EntityQueryInvocationImpl(transQuery,
						new SearchOption().unnotifyListeners(),
						new Predicate<Object[]>() {
							@Override
							public boolean test(Object[] dataModel) {
								if (versionSpecified) {
									oids.add(((String) dataModel[0]) + "." + dataModel[1]);
								} else {
									oids.add((String) dataModel[0]);
								}
								return true;
							}
						},
						InvocationType.SEARCH,
						handler.getService().getInterceptors(),
						handler)
				.proceed();
			});

			Entity entity = detailFormViewData.getEntity();
			String ct;
			if (versionSpecified) {
				ct = entity.getOid() + "." + entity.getVersion();
			} else {
				ct = entity.getOid();
			}
			if (!oids.contains(ct)) {
				return false;
			}
		}
		return true;
	}

}
