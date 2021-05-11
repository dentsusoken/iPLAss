/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;

/**
 * Userエンティティの一意キーを検索するDefaultUnmodifiableUniqueKeyResolver。
 *
 * @author Y.Suzuki
 *
 */
public class DefaultUnmodifiableUniqueKeyResolver implements UnmodifiableUniqueKeyResolver {
	
	private AuthService authService;
	
	EntityManager entityManager;
	
	private String unmodifiableUniqueKeyProperty = Entity.OID;
	
	@Override
	public String getUnmodifiableUniqueKeyProperty() {
		return unmodifiableUniqueKeyProperty;
	}
	public void setUnmodifiableUniqueKeyProperty(
			String unmodifiableUniqueKeyProperty) {
		this.unmodifiableUniqueKeyProperty = unmodifiableUniqueKeyProperty;
	}

	public String resolveUnmodifiableUniqueKey(String accountId) {
		return authService.doSecuredAction(AuthContextHolder.getAuthContext().privilegedAuthContextHolder(), () -> {
			Query q = new Query()
					.select(new EntityField(Entity.OID))
					.from(User.DEFINITION_NAME)
					.where(new Equals(new EntityField(User.ACCOUNT_ID), new Literal(accountId)));

			return ((User) entityManager.searchEntity(q).getFirst()).getOid();
		});
	}

	@Override
	public void inited(AuthService service, AuthenticationProvider provider) {
		this.authService = service;
		this.entityManager = ManagerLocator.getInstance().getManager(EntityManager.class);
	}

}
