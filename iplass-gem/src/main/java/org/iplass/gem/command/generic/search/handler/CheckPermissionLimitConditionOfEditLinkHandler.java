/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.search.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.gem.command.common.SearchResultData;
import org.iplass.gem.command.common.SearchResultRow;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.RowValueList;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.auth.EntityAuthContext;
import org.iplass.mtp.impl.entity.interceptor.EntityQueryInvocationImpl;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * 検索結果の編集リンクをEntityの参照可能範囲設定で制御する
 *
 */
public class CheckPermissionLimitConditionOfEditLinkHandler extends SearchFormViewAdapter {

	@Override
	protected void onCreateSearchResult(CreateSearchResultEvent event) {

		//検索結果が0件の場合は対象外
		if (event.getResultData().getRows().isEmpty()) {
			return;
		}

		AuthContextHolder user = AuthContextHolder.getAuthContext();
		EntityHandler handler = ServiceRegistry.getRegistry().getService(EntityService.class).getRuntimeByName(event.getEntityName());

		if (!user.isPrivilegedExecution()) {
			//更新権限チェック
			checkUpdatePermission(handler, event.getResultData(), user);

			//削除権限チェック
			checkDeletePermission(handler, event.getResultData(), user);
		}

	}

	private void checkUpdatePermission(EntityHandler handler, SearchResultData resultData, AuthContextHolder user) {
		EntityPermission permission = new EntityPermission(handler.getMetaData().getName(), EntityPermission.Action.UPDATE);
		if (!user.checkPermission(permission)) {
			//全部編集不可
			resultData.getRows().forEach(row -> {
				row.getResponse().put(SearchResultRow.CAN_EDIT, String.valueOf(false));
			});
			return;
		}

		//更新可能なデータ範囲か？
		checkLimitCondition(handler, resultData, permission, user, SearchResultRow.CAN_EDIT);
	}

	private void checkDeletePermission(EntityHandler handler, SearchResultData resultData, AuthContextHolder user) {
		EntityPermission permission = new EntityPermission(handler.getMetaData().getName(), EntityPermission.Action.DELETE);
		if (!user.checkPermission(permission)) {
			//全部削除不可
			resultData.getRows().forEach(row -> {
				row.getResponse().put(SearchResultRow.CAN_DELETE, String.valueOf(false));
			});
			return;
		}

		//削除可能なデータ範囲か？
		checkLimitCondition(handler, resultData, permission, user, SearchResultRow.CAN_DELETE);
	}

	private void checkLimitCondition(final EntityHandler handler, SearchResultData resultData, EntityPermission permission, AuthContextHolder user, String responseKey) {
		EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(permission);
		if (eac.hasLimitCondition(permission, user)) {
			boolean versionSpecified = handler.isVersioned();
			Query q = new Query().select(Entity.OID).from(handler.getMetaData().getName());
			if (versionSpecified) {
				q.select().add(Entity.VERSION);
			}

			Condition cond = null;

			List<SearchResultRow> rows = resultData.getRows();

			if (rows.size() > 1) {
				final List<ValueExpression> oids = new ArrayList<>();
				resultData.getRows().forEach(row -> {
					if (versionSpecified) {
						oids.add(new RowValueList(new Literal(row.getEntity().getOid()), new Literal(row.getEntity().getVersion())));
					} else {
						oids.add(new Literal(row.getEntity().getOid()));
					}
				});
				In in;
				if (versionSpecified) {
					in = new In(new String[] {Entity.OID, Entity.VERSION});
				} else {
					in = new In(Entity.OID);
				}
				in.setValue(oids);
				cond = in;
			} else {
				if (versionSpecified) {
					cond = new And(new Equals(Entity.OID, rows.get(0).getEntity().getOid()), new Equals(Entity.VERSION, rows.get(0).getEntity().getVersion()));
				} else {
					cond = new Equals(Entity.OID, rows.get(0).getEntity().getOid());
				}
			}

			q.where(cond);

			final Query transQuery = eac.modifyQuery(q, permission.getAction(), user);

			final ArrayList<String> oids = new ArrayList<>();
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

			resultData.getRows().forEach(row -> {
				Entity entity = row.getEntity();
				String ct;
				if (versionSpecified) {
					ct = entity.getOid() + "." + entity.getVersion();
				} else {
					ct = entity.getOid();
				}
				if (!oids.contains(ct)) {
					row.getResponse().put(responseKey, String.valueOf(false));
				}
			});
		}
	}
}
