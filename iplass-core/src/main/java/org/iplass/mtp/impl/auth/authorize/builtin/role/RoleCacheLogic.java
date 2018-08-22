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

package org.iplass.mtp.impl.auth.authorize.builtin.role;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.cache.LoadingAdapter;

public class RoleCacheLogic implements LoadingAdapter<String, RoleContext> {

	public static final String ROLE_DEF_NAME = "mtp.auth.Role";
	public static final String ROLE_CODE = "code";
	public static final String ROLE_PRIORITY = "priority";
	public static final String ROLE_CONDITION = "condition";

	public static final String ROLE_CONDITION_DEF_NAME = "mtp.auth.RoleCondition";
	public static final String ROLE_CONDITION_EXPRESSION = "expression";
	public static final String ROLE_CONDITION_ROLE = "role";

	private final TenantAuthorizeContext authorizeContext;
	private EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

	public RoleCacheLogic(TenantAuthorizeContext authorizeContext) {
		this.authorizeContext = authorizeContext;
	}

	@Override
	public RoleContext load(final String key) {

		return AuthContext.doPrivileged(() -> {
			Query q = new Query()
					.select(ROLE_CODE, ROLE_PRIORITY, ROLE_CONDITION + "." + ROLE_CONDITION_EXPRESSION)
					.from(ROLE_DEF_NAME)
					.where(new Equals(ROLE_CODE, key));

			final ArrayList<String> expressionList = new ArrayList<String>();
			final boolean[] isFind = {false};
			final long[] priority = {0};
			em.search(q, new Predicate<Object[]>() {

				@Override
				public boolean test(Object[] dataModel) {
					isFind[0] = true;
					if (dataModel[1] != null) {
						priority[0] = ((Long) dataModel[1]).longValue();
					}
					String exp = (String) dataModel[2];
					if (exp != null && exp.trim().length() != 0) {
						expressionList.add(exp);
					}
					return true;
				}
			});

			if (!isFind[0]) {
				return null;
			}

			return new RoleContext(key, priority[0], expressionList.toArray(new String[expressionList.size()]), authorizeContext);
		});
	}

	@Override
	public String getKey(RoleContext val) {
		return val.getRoleCode();
	}

	@Override
	public List<RoleContext> loadByIndex(int indexType, Object indexVal) {
		return null;
	}
	@Override
	public long getVersion(RoleContext value) {
		return 0;
	}
	@Override
	public Object getIndexVal(int indexType, RoleContext value) {
		return null;
	}

}
