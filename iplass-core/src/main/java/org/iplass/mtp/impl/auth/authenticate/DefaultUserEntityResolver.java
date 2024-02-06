/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.hint.CacheHint;
import org.iplass.mtp.entity.query.hint.CacheHint.CacheScope;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.builder.EntityBuilder;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

/**
 * Userエンティティを検索するUserEntityResolver。
 *
 * @author K.Higuchi
 *
 */
public class DefaultUserEntityResolver implements UserEntityResolver {

	public static List<String> DEFAULT_EAGER_LOAD_REFERENCE_PROPERTY
			= Collections.unmodifiableList(Arrays.asList(new String[]{"rank", "groups"}));

	private AuthService authService;

	private boolean enableCache = true;
	private String unmodifiableUniqueKeyProperty = Entity.OID;
	private List<String> eagerLoadReferenceProperty;
	private String filterCondition;

	private boolean byLoad = false;
	private Condition filterConditionNode;

	public boolean isEnableCache() {
		return enableCache;
	}
	public void setEnableCache(boolean enableCache) {
		this.enableCache = enableCache;
	}
	public boolean isByLoad() {
		return byLoad;
	}
	public void setByLoad(boolean byLoad) {
		this.byLoad = byLoad;
	}
	@Override
	public String getUnmodifiableUniqueKeyProperty() {
		return unmodifiableUniqueKeyProperty;
	}
	public void setUnmodifiableUniqueKeyProperty(
			String unmodifiableUniqueKeyProperty) {
		this.unmodifiableUniqueKeyProperty = unmodifiableUniqueKeyProperty;
	}
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
		//cache
		//FIXME EQLの結果キャッシュをできるようにcache
//		ExecuteContext ec = ExecuteContext.getCurrentContext();
//		if (enableCache) {
//			if (ec != null) {
//				User cached = (User) ec.getAttribute(CACHE_KEY);
//				if (cached != null) {
//					String uniqueKey = cached.getValue(unmodifiableUniqueKeyProperty);
//					if (uniqueKey != null) {
//						if (uniqueKey.equals(account.getUnmodifiableUniqueKey())) {
//							return cached;
//						}
//					}
//				}
//			}
//		}

		User user = searchUser(account.getUnmodifiableUniqueKey());
//		if (enableCache) {
//			ec.setAttribute(CACHE_KEY, user, false);
//		}
		return user;
	}

	public User searchUser(final String key) {

		return authService.doSecuredAction(AuthContextHolder.getAuthContext().privilegedAuthContextHolder(), () ->{
							if (byLoad) {
								return searchUserByLoad(key);
							} else {
								return searchUserByOneEQL(key);
							}
						});
	}

	private User searchUserByOneEQL(String key) {
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

			if (enableCache) {
				q.select().addHint(new CacheHint(CacheScope.TRANSACTION));
			}

			q.from(User.DEFINITION_NAME);

			Condition c = new Equals(unmodifiableUniqueKeyProperty, key);
			if (filterConditionNode != null) {
				c = new And(c, (Condition) filterConditionNode.copy());
			}
			q.where(c);

			final EntityBuilder eb = new EntityBuilder(userEh, ec, q.getSelect().getSelectValues());
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
			//ユーザーエンティティの状態が不正
			throw new EntityRuntimeException("failed to search " + User.DEFINITION_NAME + ".", e);
		}

	}

	private User searchUserByLoad(String key) {
		try {
			final String[] oid = new String[1];
			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			if (!unmodifiableUniqueKeyProperty.equals(Entity.OID)) {
				Query q = new Query().select(Entity.OID).from(User.DEFINITION_NAME);

				Condition c = new Equals(unmodifiableUniqueKeyProperty, key);
				if (filterConditionNode != null) {
					c = new And(c, (Condition) filterConditionNode.copy());
				}
				q.where(c);

				em.search(q, new Predicate<Object[]>() {
					@Override
					public boolean test(Object[] data) {
						oid[0] = (String) data[0];
						return false;//1件のはず
					}
				});
			} else {
				oid[0] = key;
			}
			if (oid[0] != null) {
				User user;
				if (eagerLoadReferenceProperty == null) {
					user = (User) em.load(oid[0], User.DEFINITION_NAME, new LoadOption(false, false));
				} else {
					user = (User) em.load(oid[0], User.DEFINITION_NAME, new LoadOption(eagerLoadReferenceProperty));
				}

				//TODO 現状、1段のみ対応、、、
				if (eagerLoadReferenceProperty != null) {
					for (String pName: eagerLoadReferenceProperty) {
						Object ref = user.getValue(pName);
						if (ref != null) {
							if (ref instanceof Entity) {
								Entity refE = (Entity) ref;
								user.setValue(pName, em.load(refE.getOid(), refE.getDefinitionName()));
							} else if (ref instanceof Entity[]) {
								Entity[] refEs = (Entity[]) ref;
								for (int i = 0; i < refEs.length; i++) {
									refEs[i] = em.load(refEs[i].getOid(), refEs[i].getDefinitionName());
									user.setValue(pName, refEs);
								}
							}
						}
					}
				}

				return user;
			}
			return null;
		} catch (Exception e) {
			//ユーザーエンティティの状態が不正
			throw new EntityRuntimeException("failed to search " + User.DEFINITION_NAME + ".", e);
		}

	}

	@Override
	public void inited(AuthService service, AuthenticationProvider provider) {
		this.authService = service;
		if (filterCondition != null) {
			filterConditionNode = Condition.newCondition(filterCondition);
		}
	}

}
