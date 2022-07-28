/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.oidc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.oidc.AutoUserProvisioningHandler;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.hint.CacheHint;
import org.iplass.mtp.entity.query.hint.CacheHint.CacheScope;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.subquery.ScalarSubQuery;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.UserEntityResolver;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.builder.EntityBuilder;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * OIDCAuthenticationProvider用のUserEntityResolver。
 * OpenIDConnect定義に基づきユーザを解決。
 *
 * @author K.Higuchi
 *
 */
public class OIDCUserEntityResolver implements UserEntityResolver {

	private AuthService authService;
	private OpenIdConnectService oidcService;

	private List<String> eagerLoadReferenceProperty;
	private String filterCondition;

	private Condition filterConditionNode;

	public List<String> getEagerLoadReferenceProperty() {
		return eagerLoadReferenceProperty;
	}
	public void setEagerLoadReferenceProperty(
			List<String> eagerLoadReferenceProperty) {
		this.eagerLoadReferenceProperty = eagerLoadReferenceProperty;
	}

	public String getFilterCondition() {
		return filterCondition;
	}
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	@Override
	public User searchUser(AccountHandle account) {
		OIDCAccountHandle ah = (OIDCAccountHandle) account;
		OpenIdConnectRuntime oidcr = oidcService.getRuntimeByName(ah.getOpenIdConnectDefinitionName());

		User user = searchUser(ah);
		if (user == null && oidcr.getMetaData().isEnableTransientUser()) {
			user = temporaryUser(ah, oidcr);
		}
		return user;
	}

	private User temporaryUser(OIDCAccountHandle ah, OpenIdConnectRuntime oidcr) {
		User user;
		if (oidcr.getAutoUserProvisioningHandler() != null) {
			user = oidcr.getAutoUserProvisioningHandler().transientUser(ah.getSubjectId(), ah.getSubjectName(), ah.getAttributeMap());
		} else {
			user = new AutoUserProvisioningHandler() {
				public void updateUser(User user, String subjectId, String subjectName, Map<String, Object> attributes) {
				}
				public String createUser(String subjectId, String subjectName, Map<String, Object> attributes) {
					return null;
				}
			}.transientUser(ah.getSubjectId(), ah.getSubjectName(), ah.getAttributeMap());
		}
		
		if (user.getAccountPolicy() == null) {
			//resolve authPolicy
			AuthenticationPolicyService authPolicyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
			List<MetaDataEntryInfo> list = authPolicyService.list();
			for (MetaDataEntryInfo mi: list) {
				AuthenticationPolicyRuntime apr = authPolicyService.getRuntimeById(mi.getId());
				if (oidcr.isAllowedOnPolicy(apr)) {
					user.setAccountPolicy(apr.getMetaData().getName());
					break;
				}
			}
		}
		return user;
	}

	private User searchUser(OIDCAccountHandle ah) {
		return authService.doSecuredAction(AuthContextHolder.getAuthContext().privilegedAuthContextHolder(),
				() -> searchUserByOneEQL((String) ah.getAttributeMap().get(OIDCAccountHandle.SUBJECT_ID_WITH_DEFINITION_NAME)));
	}

	private User searchUserByOneEQL(String uniqueKeyOfOpenIdProviderAccount) {
		try {
			EntityContext ec = EntityContext.getCurrentContext();

			ArrayList<ValueExpression> selectVals = new ArrayList<>();
			EntityHandler userEh = ec.getHandlerByName(User.DEFINITION_NAME);
			for (PropertyHandler ph: userEh.getPropertyList(ec)) {
				if (ph instanceof PrimitivePropertyHandler) {
					selectVals.add(new EntityField(ph.getName()));
				}
			}

			if (eagerLoadReferenceProperty != null) {
				for (String refName: eagerLoadReferenceProperty) {
					EntityHandler refEh = ((ReferencePropertyHandler) userEh.getPropertyCascade(refName, ec)).getReferenceEntityHandler(ec);
					for (PropertyHandler ph: refEh.getPropertyList(ec)) {
						if (ph instanceof PrimitivePropertyHandler) {
							selectVals.add(new EntityField(refName + "." + ph.getName()));
						}
					}
				}
			}
			Query q = new Query();
			q.setSelect(new Select(false, selectVals));
			q.select().addHint(new CacheHint(CacheScope.TRANSACTION));

			q.from(User.DEFINITION_NAME);

			Query subq = new Query().select(OpenIdProviderAccountEntityEventListener.USER_OID)
					.from(OpenIdProviderAccountEntityEventListener.DEFINITION_NAME)
					.where(new Equals(OpenIdProviderAccountEntityEventListener.UNIQUE_KEY, uniqueKeyOfOpenIdProviderAccount));
			
			Condition c = new Equals(Entity.OID, new ScalarSubQuery(subq));
			if (filterConditionNode != null) {
				c = new And(c, (Condition) filterConditionNode.copy());
			}
			q.where(c);

			String[] props = new String[q.getSelect().getSelectValues().size()];
			for (int i = 0; i < q.getSelect().getSelectValues().size(); i++) {
				props[i] = q.getSelect().getSelectValues().get(i).toString();
			}
			final EntityBuilder eb = new EntityBuilder(userEh, ec, props);
			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			em.search(q, new Predicate<Object[]>() {
				@Override
				public boolean test(Object[] data) {
					eb.handle(data, null);
					return true;
				}
			});

			eb.finished();

			Collection<Entity> result = eb.getCollection();
			if (!result.isEmpty()) {
				return (User) result.iterator().next();
			}

			return null;
		} catch (Exception e) {
			//ユーザエンティティの状態が不正
			throw new EntityRuntimeException("failed to search " + User.DEFINITION_NAME + ".", e);
		}
	}

	@Override
	public void inited(AuthService service, AuthenticationProvider provider) {
		this.authService = service;
		oidcService = ServiceRegistry.getRegistry().getService(OpenIdConnectService.class);
		if (filterCondition != null) {
			filterConditionNode = Condition.newCondition(filterCondition);
		}
	}
	@Override
	public String getUnmodifiableUniqueKeyProperty() {
		return User.OID;
	}

}
