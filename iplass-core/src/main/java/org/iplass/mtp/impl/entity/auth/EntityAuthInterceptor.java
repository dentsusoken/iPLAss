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

package org.iplass.mtp.impl.entity.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.NoPermissionException;
import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteTargetVersion;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.entity.SearchOption;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateCondition.UpdateValue;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.interceptor.EntityBulkUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityCountInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityDeleteInvocation;
import org.iplass.mtp.entity.interceptor.EntityGetRecycleBinInvocation;
import org.iplass.mtp.entity.interceptor.EntityInsertInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptorAdapter;
import org.iplass.mtp.entity.interceptor.EntityLoadInvocation;
import org.iplass.mtp.entity.interceptor.EntityPurgeInvocation;
import org.iplass.mtp.entity.interceptor.EntityQueryInvocation;
import org.iplass.mtp.entity.interceptor.EntityRestoreInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityUpdateInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.permission.EntityPropertyPermission;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.RowValueList;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.interceptor.EntityBulkUpdateInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityCountInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityDeleteAllInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityDeleteInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityGetRecycleBinInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityInsertInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityPurgeInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityQueryInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityRestoreInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityUpdateAllInvocationImpl;
import org.iplass.mtp.impl.entity.interceptor.EntityUpdateInvocationImpl;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityAuthInterceptor extends EntityInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(EntityAuthInterceptor.class);

	@Override
	public String insert(EntityInsertInvocation invocation) {
		//1.当該EntityのINSERT権限があるかチェック→権限がない場合エラー
		//2.当該Entityの項目が、ISNERT可能項目かどうかチェック→//TODO INSERT不可項目の場合はエラー
		//3.当該Entityに設定されている参照先のEntityが参照可能なEntityかチェック→権限ない場合エラー
		//4.当該Entityが登録可能なデータ範囲かどうかチェック→権限ない場合エラー

		EntityInsertInvocationImpl inv = (EntityInsertInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged insert call. shrot cut auth check.");
			}
			return invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.CREATE);
			if (!user.checkPermission(perm)) {
//				throw new NoPermissionException(invocation.getEntityDefinition().getDisplayName() + "の" + EntityPermission.Action.CREATE.getDisplayName() + "権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noRegist", eh.getLocalizedDisplayName()));
			}

			//項目レベルでのチェック
			EntityContext ectx = EntityContext.getCurrentContext();
			for (PropertyHandler ph: eh.getPropertyList(ectx)) {
				Object value = inv.getEntity().getValue(ph.getName());

				if (value != null) {
					//項目レベルセキュリティのチェック
					if (!user.checkPermission(new EntityPropertyPermission(eh.getMetaData().getName(), ph.getName(), EntityPropertyPermission.Action.CREATE))) {
//						throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の項目：" + ph.getMetaData().getDisplayName() + "の" + EntityPropertyPermission.Action.CREATE.getDisplayName() + "権限がありません");
						throw new NoPermissionException(
								resourceString("impl.entity.auth.EntityAuthInterceptor.noRegistProperty", eh.getLocalizedDisplayName(), ph.getLocalizedDisplayName()));
					}

					//参照先のEntityに対して、参照権限が設定されているかチェック
					if (ph instanceof ReferencePropertyHandler) {
						ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
						//逆参照ではない場合
						if (rph.getMetaData().getMappedByPropertyMetaDataId() == null) {
							EntityHandler reh = rph.getReferenceEntityHandler(ectx);
							EntityPermission rerPerm = new EntityPermission(reh.getMetaData().getName(), EntityPermission.Action.REFERENCE);
							if (!user.checkPermission(rerPerm)) {
//								throw new NoPermissionException(reh.getMetaData().getDisplayName() + "の" + EntityPermission.Action.REFERENCE.getDisplayName() + "権限がありません");
								throw new NoPermissionException(
										resourceString("impl.entity.auth.EntityAuthInterceptor.noReference", reh.getLocalizedDisplayName()));
							}
							//限定条件が設定されている場合は、実際にデータ検索して、検索可能な範囲かチェック
							checkLimitCondition(reh, value, rerPerm, user, false);
						}
					}

				}
			}

			String oid = invocation.proceed();

			//登録可能なデータ範囲か？
			Object key;
			if (eh.isVersioned() && invocation.getInsertOption().isVersionSpecified()) {
				key = new Object[] {oid, inv.getEntity().getVersion()};
			} else {
				key = oid;
			}
			checkLimitCondition(eh, key, perm, user, eh.isVersioned());

			return oid;
		}

	}

	private void checkLimitCondition(final EntityHandler eh, Object value, EntityPermission permission, AuthContextHolder user, boolean versionSpecified) {
		//withVersion=trueの場合は、versioned queryを発行しバージョンまで一致するかチェック
		
		EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(permission);
		if (eac.hasLimitCondition(permission, user)) {
			Query q = new Query().select(Entity.OID).from(eh.getMetaData().getName());
			if (versionSpecified) {
				q.select().add(Entity.VERSION);
			}
			Condition cond = null;
			if (value instanceof Entity[]) {
				Entity[] eList = (Entity[]) value;
				List<ValueExpression> oids = new ArrayList<ValueExpression>();
				for (Entity e: eList) {
					if (versionSpecified) {
						Long ver = e.getVersion();
						if (ver == null) {
							ver = 0L;
						}
						oids.add(new RowValueList(new Literal(e.getOid()), new Literal(ver)));
					} else {
						oids.add(new Literal(e.getOid()));
					}
				}
				if (oids.size() == 0) {
					//ブランクの配列であった場合など
					return;
				}
				In in;
				if (versionSpecified) {
					in = new In(new String[] {Entity.OID, Entity.VERSION});
				} else {
					in = new In(Entity.OID);
				}
				in.setValue(oids);
				cond = in;
			} else if (value instanceof Entity) {
				if (versionSpecified) {
					Long ver = ((Entity) value).getVersion();
					if (ver == null) {
						ver = 0L;
					}
					cond = new And(new Equals(Entity.OID, ((Entity) value).getOid()), new Equals(Entity.VERSION, ver));
				} else {
					cond = new Equals(Entity.OID, ((Entity) value).getOid());
				}
			} else {
				if (versionSpecified) {
					String oid;
					Long ver;
					if (value instanceof Object[]) {
						oid = (String) ((Object[]) value)[0];
						ver = (Long) ((Object[]) value)[1];
						if (ver == null) {
							ver = 0L;
						}
					} else {
						oid = (String) value;
						ver = 0L;
					}
					cond = new And(new Equals(Entity.OID, oid), new Equals(Entity.VERSION, ver));
				} else {
					cond = new Equals(Entity.OID, (String) value);
				}
			}

			q.where(cond);

			final Query transQuery = eac.modifyQuery(q, permission.getAction(), user);

			final ArrayList<String> oids = new ArrayList<String>();
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
						eh.getService().getInterceptors(),
						eh).proceed();
			});


			if (value instanceof Entity[]) {
				for (Entity e: (Entity[]) value) {
					String ct;
					if (versionSpecified) {
						Long ver = e.getVersion();
						if (ver == null) {
							ver = 0L;
						}
						ct = e.getOid() + "." + ver;
					} else {
						ct = e.getOid();
					}
					if (!oids.contains(ct)) {
						throw new NoPermissionException(getPermissionExceptionMessage(eh, permission.getAction()));
					}
				}
			} else if (value instanceof Entity) {
				String ct;
				if (versionSpecified) {
					Long ver = ((Entity) value).getVersion();
					if (ver == null) {
						ver = 0L;
					}
					ct = ((Entity) value).getOid() + "." + ver;
				} else {
					ct = ((Entity) value).getOid();
				}
				if (!oids.contains(ct)) {
					throw new NoPermissionException(getPermissionExceptionMessage(eh, permission.getAction()));
				}
			} else {
				String ct;
				if (versionSpecified) {
					String oid;
					Long ver;
					if (value instanceof Object[]) {
						oid = (String) ((Object[]) value)[0];
						ver = (Long) ((Object[]) value)[1];
						if (ver == null) {
							ver = 0L;
						}
					} else {
						oid = (String) value;
						ver = 0L;
					}
					ct = oid + "." + ver;
				} else {
					ct = (String) value;
				}
				if (!oids.contains(ct)) {
					throw new NoPermissionException(getPermissionExceptionMessage(eh, permission.getAction()));
				}
			}
		}
	}

	private String getPermissionExceptionMessage(EntityHandler entityDefinition, EntityPermission.Action action) {
		String bundleKey = null;
		if (EntityPermission.Action.CREATE.equals(action)) {
			bundleKey = "impl.entity.auth.EntityAuthInterceptor.noRegist";
		} else if (EntityPermission.Action.DELETE.equals(action)) {
			bundleKey = "impl.entity.auth.EntityAuthInterceptor.noDelete";
		} else if (EntityPermission.Action.REFERENCE.equals(action)) {
			bundleKey = "impl.entity.auth.EntityAuthInterceptor.noReference";
		} else if (EntityPermission.Action.UPDATE.equals(action)) {
			bundleKey = "impl.entity.auth.EntityAuthInterceptor.noUpdate";
		} else {
			return null;
		}
		return resourceString(bundleKey, entityDefinition.getLocalizedDisplayName());
	}

	@Override
	public void update(EntityUpdateInvocation invocation) {
		//1.当該EntityのUPDATE権限があるかチェック→権限ない場合エラー
		//2.当該Entityが登録可能なデータ範囲かどうかチェック→権限ない場合はエラー
		//3.UpdateOptionの更新項目をチェック→権限ない場合はエラー
		//4.当該Entityに設定されている参照先のEntityが参照可能なEntityかチェック→参照権限ない場合エラー

		EntityUpdateInvocationImpl inv = (EntityUpdateInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged update call. shrot cut auth check.");
			}
			invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.UPDATE);
			if (!user.checkPermission(perm)) {
//				throw new NoPermissionException(invocation.getEntityDefinition().getDisplayName() + "の" + EntityPermission.Action.UPDATE.getDisplayName() + "権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noUpdate", eh.getLocalizedDisplayName()));
			}

			//更新可能なデータ範囲か？
			checkLimitCondition(eh, inv.getEntity(), perm, user, eh.isVersioned() && TargetVersion.CURRENT_VALID != inv.getUpdateOption().getTargetVersion());

			//項目レベルでのチェック
			UpdateOption updateOption = invocation.getUpdateOption();
			EntityContext ectx = EntityContext.getCurrentContext();
			for (String pName: updateOption.getUpdateProperties()) {
				PropertyHandler ph = eh.getProperty(pName, ectx);
				if (ph != null) {
					if (!user.checkPermission(new EntityPropertyPermission(eh.getMetaData().getName(), ph.getName(), EntityPropertyPermission.Action.UPDATE))) {
//						throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の項目：" + ph.getMetaData().getDisplayName() + "の" + EntityPropertyPermission.Action.UPDATE.getDisplayName() + "権限がありません");
						throw new NoPermissionException(
								resourceString("impl.entity.auth.EntityAuthInterceptor.noUpdateProperty", eh.getLocalizedDisplayName(), ph.getLocalizedDisplayName()));
					}
					//参照先のEntityに対して、参照権限が設定されているかチェック
					if (ph instanceof ReferencePropertyHandler) {
						Object value = inv.getEntity().getValue(pName);
						if (value != null) {
							ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
							//逆参照ではない場合
							if (rph.getMetaData().getMappedByPropertyMetaDataId() == null) {
								EntityHandler reh = rph.getReferenceEntityHandler(ectx);
								EntityPermission refPerm = new EntityPermission(reh.getMetaData().getName(), EntityPermission.Action.REFERENCE);
								if (!user.checkPermission(refPerm)) {
//									throw new NoPermissionException(reh.getMetaData().getDisplayName() + "の" + EntityPermission.Action.REFERENCE.getDisplayName() + "権限がありません");
									throw new NoPermissionException(
											resourceString("impl.entity.auth.EntityAuthInterceptor.noReference", reh.getLocalizedDisplayName()));
								}
								//限定条件が設定されている場合は、実際にデータ検索して、検索可能な範囲かチェック
								checkLimitCondition(reh, value, refPerm, user, false);
							}
						}
					}
				}
			}

			invocation.proceed();
		}

	}

	@Override
	public void delete(EntityDeleteInvocation invocation) {
		//1.当該EntityのDELETE権限があるかチェック→権限ない場合エラー
		//2.当該Entityが削除可能なデータ範囲かどうかチェック→権限ない場合はエラー

		EntityDeleteInvocationImpl inv = (EntityDeleteInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged delete call. shrot cut auth check.");
			}
			invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.DELETE);
			if (!user.checkPermission(perm)) {
//				throw new NoPermissionException(invocation.getEntityDefinition().getDisplayName() + "の" + EntityPermission.Action.DELETE.getDisplayName() + "権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noDelete", eh.getLocalizedDisplayName()));
			}

			//更新可能なデータ範囲か？
			checkLimitCondition(eh, inv.getEntity(), perm, user, eh.isVersioned() && DeleteTargetVersion.SPECIFIC == inv.getDeleteOption().getTargetVersion());

			invocation.proceed();
		}
	}

	@Override
	public Entity load(EntityLoadInvocation invocation) {
		//1.当該EntityのREFERENCE権限があるかチェック→権限ない場合nullリターン
		//2.当該Entityが参照可能な範囲のデータかチェック→権限ない場合はnullリターン
		//3.当該Entityの項目が参照可能かチェック→権限ない場合、当該項目をnullにしてリターン

		//最終的には、queryが実行されるのでそこでチェックされる前提でスルー
		Entity e = invocation.proceed();

		return e;
	}

	@Override
	public void query(EntityQueryInvocation invocation) {
		//当該カラムを含む検索の場合、行を制限する
		//1.対象Entityの参照権限があるかチェック→権限ない場合0件リターン
		//2.クエリーにEntityのデータ参照範囲の限定条件を付与
		//3.当該Entityの参照項目の参照権限が付与されているかチェック→ない場合は当該項目をnullでリターン
		//4.検索条件に参照権限が付与されていない項目を指定されていた場合は、当該結果を0件で返却するようにする

		EntityQueryInvocationImpl inv = (EntityQueryInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged query call. shrot cut auth check.");
			}
			invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.REFERENCE);
			if (!user.checkPermission(perm)) {
//				throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の" + EntityPermission.Action.REFERENCE.getDisplayName() + "権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noReference", eh.getLocalizedDisplayName()));
			}


			//項目のマスク（参照不可の項目をnullに）と、限定条件の付与
			//query時に対象とするEntityActionが指定されていれば、そのActionを利用(PropertyActionはREFERENCE)
			EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(perm);
			EntityPermission.Action action = EntityQueryAuthContextHolder.getContext().getQueryAction();
			inv.setQuery(eac.modifyQuery(inv.getQuery(), action, EntityPropertyPermission.Action.REFERENCE, user));

			invocation.proceed();
		}
	}

	@Override
	public int count(EntityCountInvocation invocation) {
		//当該カラムを含む検索の場合、行を制限する
		//1.対象Entityの参照権限があるかチェック→権限ない場合0件リターン
		//2.クエリーにEntityのデータ参照範囲の限定条件を付与
		//3.当該Entityの参照項目の参照権限が付与されているかチェック→ない場合は当該項目をnullでリターン
		//4.検索条件に参照権限が付与されていない項目を指定されていた場合は、当該結果を0件で返却するようにする

		EntityCountInvocationImpl inv = (EntityCountInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged count call. shrot cut auth check.");
			}
			return invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.REFERENCE);
			if (!user.checkPermission(perm)) {
//				throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の" + EntityPermission.Action.REFERENCE.getDisplayName() + "権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noReference", eh.getLocalizedDisplayName()));
			}

			//項目のマスク（参照不可の項目をnullに）と、限定条件の付与
			//query時に対象とするEntityActionが指定されていれば、そのActionを利用(PropertyActionはREFERENCE)
			EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(perm);
			EntityPermission.Action action = EntityQueryAuthContextHolder.getContext().getQueryAction();
			inv.setQuery(eac.modifyQuery(inv.getQuery(), action, EntityPropertyPermission.Action.REFERENCE, user));

			return invocation.proceed();
		}
	}

	@Override
	public int updateAll(EntityUpdateAllInvocation invocation) {
		//1.対象Entityの更新権限があるかチェック→権限ない場合エラー
		//2.当該Entityの更新項目に権限があるかチェック→権限ない場合エラー
		//3.更新クエリーにEntityのデータ更新範囲の限定条件を付与

		EntityUpdateAllInvocationImpl inv = (EntityUpdateAllInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged updateAll call. shrot cut auth check.");
			}
			return invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.UPDATE);
			if (!user.checkPermission(perm)) {
//				throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の" + EntityPermission.Action.UPDATE.getDisplayName() + "権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noUpdate", eh.getLocalizedDisplayName()));
			}

			//項目レベルでのチェック
			UpdateCondition updateCond = invocation.getUpdateCondition();
			EntityContext ectx = EntityContext.getCurrentContext();
			for (UpdateValue uv: updateCond.getValues()) {
				PropertyHandler ph = eh.getProperty(uv.getEntityField(), ectx);

				// メッセージで利用するdisplayNameは全てDefinitionから取得する
				if (!user.checkPermission(new EntityPropertyPermission(eh.getMetaData().getName(), ph.getName(), EntityPropertyPermission.Action.UPDATE))) {
//					throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の項目：" + ph.getMetaData().getDisplayName() + "の" + EntityPropertyPermission.Action.UPDATE.getDisplayName() + "権限がありません");
					throw new NoPermissionException(
							resourceString("impl.entity.auth.EntityAuthInterceptor.noUpdateProperty", eh.getLocalizedDisplayName(), ph.getLocalizedDisplayName()));
				}
			}

			//限定条件の付与
			EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(perm);
			if (eac.hasLimitCondition(perm, user)) {
				//コピー
				updateCond = new UpdateCondition(updateCond.getDefinitionName(), updateCond.getValues(), updateCond.getWhere());

				Condition cond = null;
				if (updateCond.getWhere() != null) {
					cond = updateCond.getWhere().getCondition();
				}
				cond = eac.addLimitingCondition(cond, EntityPermission.Action.UPDATE, user);
				updateCond.setWhere(new Where(cond));
				inv.setUpdateCondition(updateCond);
			}

			return invocation.proceed();
		}
	}

	@Override
	public int deleteAll(EntityDeleteAllInvocation invocation) {
		//1.対象Entityの削除権限があるかチェック→権限ない場合0件リターン
		//2.クエリーにEntityのデータ削除範囲の限定条件を付与
		EntityDeleteAllInvocationImpl inv = (EntityDeleteAllInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged deleteAll call. shrot cut auth check.");
			}
			return invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.DELETE);
			if (!user.checkPermission(perm)) {
//				throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の" + EntityPermission.Action.DELETE.getDisplayName() + "権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noDelete", eh.getLocalizedDisplayName()));
			}

			//限定条件の付与
			EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(perm);
			if (eac.hasLimitCondition(perm, user)) {
				//コピー
				DeleteCondition org = inv.getDeleteCondition();
				DeleteCondition delCond = new DeleteCondition(org.getDefinitionName(), org.getWhere());

				Condition cond = null;
				if (delCond.getWhere() != null) {
					cond = delCond.getWhere().getCondition();
				}
				cond = eac.addLimitingCondition(cond, EntityPermission.Action.DELETE, user);
				delCond.setWhere(new Where(cond));
				inv.setDeleteCondition(delCond);
			}
			return invocation.proceed();
		}
	}
	
	@Override
	public void getRecycleBin(EntityGetRecycleBinInvocation invocation) {
		EntityGetRecycleBinInvocationImpl inv = (EntityGetRecycleBinInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged restore call. shrot cut auth check.");
			}
			invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.DELETE);
			if (!user.checkPermission(perm)) {
				//削除権限がない場合は、参照できない
				return;
			}
			EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(perm);
			if (eac.hasLimitCondition(perm, user)) {
				//自分が削除したEntityに限定
				String clientId = ExecuteContext.getCurrentContext().getClientId();
				Predicate<Entity> cb = inv.getCallback();
				inv.setCallback(e -> {
					if (clientId.equals(e.getUpdateBy())) {
						return cb.test(e);
					} else {
						return true;
					}
				});
			}

			invocation.proceed();
		}
	}

	@Override
	public void purge(EntityPurgeInvocation invocation) {
		//1.対象Entityの削除権限があるかチェック→権限ない場合エラー
		//  ※限定条件がある場合は、削除不可とする
		EntityPurgeInvocationImpl inv = (EntityPurgeInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged purge call. shrot cut auth check.");
			}
			invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.DELETE);
			if (!user.checkPermission(perm)) {
//				throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の" + EntityPermission.Action.DELETE.getDisplayName() + "権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noDelete", eh.getLocalizedDisplayName()));
			}
			EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(perm);
			if (eac.hasLimitCondition(perm, user)) {
//				throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の全データ範囲に対する" + EntityPermission.Action.DELETE.getDisplayName() + "権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noDeleteAll", eh.getLocalizedDisplayName()));
			}

			invocation.proceed();
		}
	}

	@Override
	public Entity restore(EntityRestoreInvocation invocation) {
		//1.対象Entityの削除権限があるかチェック→権限ない場合エラー
		//  ※限定条件がある場合は、リストア不可とする
		EntityRestoreInvocationImpl inv = (EntityRestoreInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged restore call. shrot cut auth check.");
			}
			return invocation.proceed();
		} else {
			EntityPermission perm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.DELETE);
			if (!user.checkPermission(perm)) {
//				throw new NoPermissionException(eh.getMetaData().getDisplayName() + "の" + EntityPermission.Action.DELETE.getDisplayName() + "（復活）権限がありません");
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noUndo", eh.getLocalizedDisplayName()));
			}
			EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(perm);
			if (eac.hasLimitCondition(perm, user)) {
				//自身が削除したデータのみ復活可能
				Entity[] ret = new Entity[1];
				eh.getRecycleBin(e -> {
					ret[0] = e;
					return false;
				}, inv.getRecycleBinId());
				if (ret[0] == null) {
					throw new EntityConcurrentUpdateException(resourceString("impl.core.EntityHandler.alreadyRestored"));
				}
				
				String clientId = ExecuteContext.getCurrentContext().getClientId();
				if (!clientId.equals(ret[0].getUpdateBy())) {
					throw new NoPermissionException(
							resourceString("impl.entity.auth.EntityAuthInterceptor.noUndoAll", eh.getLocalizedDisplayName()));
				}
			}

			return invocation.proceed();
		}
	}

	@Override
	public void bulkUpdate(EntityBulkUpdateInvocation invocation) {

		//限定条件がある場合は、bulkUpdate不可とする
		EntityBulkUpdateInvocationImpl inv = (EntityBulkUpdateInvocationImpl) invocation;
		EntityHandler eh = inv.getEntityHandler();
		AuthContextHolder user = AuthContextHolder.getAuthContext();

		//セキュリティチェックを大幅ショートカットするため、ここで特権チェック
		if (user.isPrivilegedExecution()) {
			if (logger.isDebugEnabled()) {
				logger.debug("privileged bulkUpdate call. shrot cut auth check.");
			}
			invocation.proceed();
		} else {
			EntityPermission delPerm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.DELETE);
			if (!user.checkPermission(delPerm)) {
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noDelete", eh.getLocalizedDisplayName()));
			}
			EntityPermission crePerm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.CREATE);
			if (!user.checkPermission(crePerm)) {
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noRegist", eh.getLocalizedDisplayName()));
			}
			EntityPermission updPerm = new EntityPermission(eh.getMetaData().getName(), EntityPermission.Action.UPDATE);
			if (!user.checkPermission(updPerm)) {
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noUpdate", eh.getLocalizedDisplayName()));
			}
			EntityAuthContext eac = (EntityAuthContext) user.getAuthorizationContext(updPerm);
			if (eac.hasLimitCondition(delPerm, user)) {
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noDelete", eh.getLocalizedDisplayName()));
			}
			if (eac.hasLimitCondition(updPerm, user)) {
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noUpdate", eh.getLocalizedDisplayName()));
			}
			if (eac.hasLimitCondition(crePerm, user)) {
				throw new NoPermissionException(
						resourceString("impl.entity.auth.EntityAuthInterceptor.noRegist", eh.getLocalizedDisplayName()));
			}

			invocation.proceed();
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
