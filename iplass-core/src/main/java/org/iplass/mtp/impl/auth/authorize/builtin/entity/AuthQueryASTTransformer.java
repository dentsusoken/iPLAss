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

package org.iplass.mtp.impl.auth.authorize.builtin.entity;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.permission.EntityPropertyPermission;
import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformerSupport;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.Refer;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Paren;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.auth.authorize.AuthorizationProvider;
import org.iplass.mtp.impl.auth.authorize.builtin.BuiltinAuthorizationProvider;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.auth.EntityAuthContext;
import org.iplass.mtp.impl.entity.auth.EntityQueryAuthContextHolder;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.entity.versioning.VersionedQueryNormalizer;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * Queryにセキュリティ制約を付け加えるASTTransformer。
 *
 * @author K.Higuchi
 *
 */
class AuthQueryASTTransformer extends ASTTransformerSupport {

	private TenantAuthorizeContext tenantAuthContext;
	private BuiltinEntityAuthContext entityAuthContext;
	private EntityPermission.Action action;
	private EntityPropertyPermission.Action propAction;
	private AuthContextHolder user;
	private EntityHandler eh;
	private EntityContext entityContext;

	private AuthQueryASTTransformer parent;

	private HashMap<String, PermissionInfo> permissionCheckReslt = new HashMap<String, AuthQueryASTTransformer.PermissionInfo>();
	private EntityAuthContextHandler eacHandler;
	
	//バージョンカラムが指定されていた場合の挙動を制御するため
	//TODO このあたりをSecurityで意識しなければならないのはちょっといただけない、、、
	private boolean isVersionSpecify;
	private boolean isConditonNode;


	private class PermissionInfo {
		boolean isPermit;
		boolean isReference;
		Condition limitCondition;

		PermissionInfo(boolean isPermit, boolean isReference) {
			this.isPermit = isPermit;
			this.isReference = isReference;
		}

		public String toString() {
			return String.valueOf(isPermit);
		}
	}

	// 権限条件は、select * from A where (a.x = 'hoge') and oid in (select distinct oid from A where 権限条件)

	AuthQueryASTTransformer(TenantAuthorizeContext tenantAuthContext,
			BuiltinEntityAuthContext entityAuthContext, EntityPermission.Action action,
			AuthContextHolder user, EntityHandler eh, EntityContext entityContext, AuthQueryASTTransformer parent) {

		this(tenantAuthContext, entityAuthContext, action, null, user, eh, entityContext, parent);
	}

	AuthQueryASTTransformer(TenantAuthorizeContext tenantAuthContext,
			BuiltinEntityAuthContext entityAuthContext,
			EntityPermission.Action action, EntityPropertyPermission.Action propAction,
			AuthContextHolder user, EntityHandler eh, EntityContext entityContext, AuthQueryASTTransformer parent) {
		this.tenantAuthContext = tenantAuthContext;
		this.entityAuthContext = entityAuthContext;
		this.action = action;
		this.user = user;
		this.eh = eh;
		this.entityContext = entityContext;
		this.parent = parent;

		this.propAction = propAction;
		if (propAction == null) {
			this.propAction = toPropertyAction(action);
		}

		isVersionSpecify = false;
		isConditonNode = false;

	}

	public Query transform(Query q) {
		return (Query) q.accept(this);
	}

	private EntityPropertyPermission.Action toPropertyAction(EntityPermission.Action action) {
		switch (action) {
		case CREATE:
			return EntityPropertyPermission.Action.CREATE;
		case REFERENCE:
			return EntityPropertyPermission.Action.REFERENCE;
		case UPDATE:
			return EntityPropertyPermission.Action.UPDATE;
		default:
			return null;
		}
	}

	private boolean isPermit(String propName) {
		PermissionInfo p = permissionCheckReslt.get(propName);
		if (p == null) {
			int index = propName.indexOf('.');
			while (p == null) {
				if (index > -1) {
					//まだ.階層途中。
					//ReferenceProperty
					String refPropName = propName.substring(0, index);
					if (!isPermit(refPropName)) {
						p = new PermissionInfo(false, true);
					} else {
						PropertyHandler ph = eh.getPropertyCascade(refPropName, entityContext);
						if (ph instanceof ReferencePropertyHandler) {
							ReferencePropertyHandler rph = (ReferencePropertyHandler) ph;
							EntityHandler reh = rph.getReferenceEntityHandler(entityContext);
							EntityPermission ep = new EntityPermission(reh.getMetaData().getName(), action);
							if (!user.checkPermission(ep)) {
								p = new PermissionInfo(false, true);
							} else {
								PermissionInfo refPI = new PermissionInfo(true, true);
								EntityAuthContext refEac = (EntityAuthContext) user.getAuthorizationContext(ep);
								if (refEac instanceof BuiltinEntityAuthContext) {
									refPI.limitCondition = ((BuiltinEntityAuthContext) refEac).addLimitingCondition(null, action, user);
								}
								permissionCheckReslt.put(refPropName, refPI);
							}
						}
					}
				} else {
					//最下層
					int dotIndex = propName.indexOf('.');
					if (dotIndex < 0) {
						//自身のPrimitiveProperty
						p = new PermissionInfo(entityAuthContext.isPermit(propName, propAction, user), false);
					} else {
						//参照先の権限を確認
						String ref = propName.substring(0, dotIndex);
						String prop = propName.substring(dotIndex + 1);
						ReferencePropertyHandler rph = (ReferencePropertyHandler) eh.getPropertyCascade(ref, entityContext);
						if (rph == null) {
							throw new NullPointerException(" reference:" + ref + " of " + eh.getMetaData().getName() + " not found.");
						}
						EntityHandler reh = rph.getReferenceEntityHandler(entityContext);
						if (user.checkPermission(new EntityPropertyPermission(reh.getMetaData().getName(), prop, propAction))) {
							p = new PermissionInfo(true, false);
						} else {
							p = new PermissionInfo(false, false);
						}
					}
				}
				index = propName.indexOf('.', index + 1);
			}

			permissionCheckReslt.put(propName, p);
		}
		return p.isPermit;
	}

	private int unnestCount(String propName) {
		for (int i = 0; i < propName.length(); i++) {
			if (propName.charAt(i) != '.') {
				return i;
			}
		}
		return 0;
	}

	@Override
	public ASTNode visit(EntityField entityField) {
		
		String propName = entityField.getPropertyName();
		int unnestCount = unnestCount(propName);
		AuthQueryASTTransformer target = this;
		if (unnestCount > 0) {
			propName = propName.substring(unnestCount);
			for (int i = 0; i < unnestCount; i++) {
				target = target.parent;
				if (target == null) {
					throw new QueryException("can't correlate outer query on :" + entityField);
				}
			}
		}
		
		//バージョン制御用のカラムを条件に指定している場合は、oid指定するサブクエリーにversionedを付ける
		if (target.isConditonNode && VersionedQueryNormalizer.isVersionProperty(propName)) {
			target.isVersionSpecify = true;
		}

		//削除の場合は、対応するプロパティ権限はないので
		if (target.propAction == null || target.isPermit(propName) || propName.equalsIgnoreCase(SubQuery.THIS)) {
			return super.visit(entityField);
		} else {
			return new Literal(null);
		}
	}

	@Override
	public ASTNode visit(SubQuery subQuery) {

		Query subQ = subQuery.getQuery();
		Condition on = subQuery.getOn();
		EntityHandler subEh = entityContext.getHandlerByName(subQ.getFrom().getEntityName());
		if (subEh == null) {
			throw new QueryException("subQuery's Entity not defined:" + subQuery);
		}
		
		BuiltinEntityAuthContext subEac = (BuiltinEntityAuthContext) user.getAuthorizationContext(new EntityPermission(subQ.getFrom().getEntityName(), action));
		TenantAuthorizeContext subTac = subEac.getTenantAuthContext();

		AuthQueryASTTransformer subTrans = new AuthQueryASTTransformer(
				subTac,
				subEac,
				action,
				propAction,
				user,
				subEh,
				entityContext,
				this);

		if (on != null) {
			on = (Condition) on.accept(subTrans);
		}

		subQ = subTrans.transform(subQ);

		return new SubQuery(subQ, on);
	}

	@Override
	public ASTNode visit(Query query) {
		Query q = (Query) super.visit(query);

		HasReferencePropertyChecker queryHasRefChecker = new HasReferencePropertyChecker();
		q.accept(queryHasRefChecker);
		
		EntityQueryAuthContextHolder eqaContext = EntityQueryAuthContextHolder.getContext();

		//referの設定（参照先のEntityで制限条件がある場合で、かつwithPermissionConditionがtrueの場合）
		if (queryHasRefChecker.hasReferenceProperty()) {
			if (permissionCheckReslt != null) {
				for (final Map.Entry<String, PermissionInfo> e: permissionCheckReslt.entrySet()) {
					PermissionInfo pInfo = e.getValue();
					if (pInfo.isReference) {
						if (pInfo.limitCondition != null) {
							ReferencePropertyHandler rph = (ReferencePropertyHandler) eh.getPropertyCascade(e.getKey(), entityContext);
							EntityHandler refEh = rph.getReferenceEntityHandler(entityContext);
							if (refEh != null) {
								Refer r = q.refer(e.getKey());
								if (!eqaContext.isWithoutConditionReferenceName(e.getKey())) {
									HasReferencePropertyChecker refChecker = new HasReferencePropertyChecker();
									pInfo.limitCondition.accept(refChecker);
									if (refChecker.hasReferenceProperty()) {
										pInfo.limitCondition = toOidInCondition(refEh.getMetaData().getName(), pInfo.limitCondition, e.getKey());
									}

									//参照先として条件構築
									QueryVisitorSupport qvs = new QueryVisitorSupport() {
										@Override
										public boolean visit(EntityField entityField) {
											entityField.setPropertyName(e.getKey() + "." + entityField.getPropertyName());
											return super.visit(entityField);
										}

										@Override
										public boolean visit(
												SubQuery scalarSubQuery) {
											//一番外側の条件だけ変更すればよいので、
											//ScalerSubQuery内は、変換しない。
											//セキュリティ条件では外側のサブクエリと相関で結合はできない仕様なので
											return false;
										}
									};
									pInfo.limitCondition.accept(qvs);

									if (r.getCondition() == null) {
										r.setCondition(pInfo.limitCondition);
									} else {
										r.setCondition(new And(r.getCondition(), pInfo.limitCondition));
									}
								}
							}
						}
					}
				}
			}
		}

		Condition cond = null;
		//admin（かつ、他テナントユーザでない）は全権限保持
		UserBinding userBind = user.newUserBinding(tenantAuthContext);
		if (!userBind.isGrantAllPermissions() && !eqaContext.isWithoutConditionReferenceName(EntityQueryAuthContextHolder.REFERENCE_THIS)) {
			cond = entityAuthContext.addLimitingCondition(null, action, user);
		}

		//限定条件なし
		if (cond == null) {
			return q;
		}

		HasReferencePropertyChecker hasRefChecker = new HasReferencePropertyChecker();
		cond.accept(hasRefChecker);

		//メインのEntityに対する条件設定
		// 参照プロパティに対しての限定条件がある場合、
		//権限条件は、select * from A where (a.x = 'hoge') and (oid,version) in (select distinct oid,version from A where 権限条件)
		if (hasRefChecker.hasReferenceProperty()) {
			cond = toOidInCondition(eh.getMetaData().getName(), cond, SubQuery.THIS);
		}

		if (q.getWhere() == null || q.getWhere().getCondition() == null) {
			q.where(cond);
		} else {
			And and = new And();
			and.addExpression(new Paren(q.getWhere().getCondition()));
			and.addExpression(new Paren(cond));
			q.where(and);
		}


		return q;
	}

	@Override
	public ASTNode visit(Where where) {
		isConditonNode = true;
		ASTNode node = super.visit(where);
		isConditonNode = false;
		return node;
	}


	private Condition toOidInCondition(String fromEntityName, Condition cond, String refName) {
		Query q = new Query();
		q.setSelect(new Select(true, new ValueExpression[]{new EntityField(Entity.OID), new EntityField(Entity.VERSION)}));
		q.from(fromEntityName);
		q.where(cond);

		//条件文でバージョンカラムが指定されている場合は、Inのサブクエリーでは全バージョンを対象にする
		if (isVersionSpecify) {
			q.setVersiond(true);
		}
		
		SubQuery sq = new SubQuery(q);
		AuthorizationProvider azp = ServiceRegistry.getRegistry().getService(AuthService.class).getAuthorizationProvider();
		if (azp instanceof BuiltinAuthorizationProvider) {
			if (eacHandler == null) {
				eacHandler = (EntityAuthContextHandler) ((BuiltinAuthorizationProvider) azp).getAuthorizationContextHandler(EntityPermission.class);
			}
			if (eacHandler.isUseCorrelatedSubqueryOnEntityLimitCondition()) {
				sq.on(refName, SubQuery.THIS);
			}
		}

		In in = new In(new String[]{Entity.OID, Entity.VERSION}, sq);
		return in;
	}
}
