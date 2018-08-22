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

package org.iplass.mtp.impl.auth.authorize.builtin.group;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.Group;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.cache.LoadingAdapter;

public class GroupCacheLogic implements LoadingAdapter<String, GroupContext> {

	private final TenantAuthorizeContext authorizeContext;
	private EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

	public GroupCacheLogic(TenantAuthorizeContext authorizeContext) {
		this.authorizeContext = authorizeContext;
	}

	@Override
	public GroupContext load(final String key) {

		return AuthContext.doPrivileged(() -> {
			Query q = new Query()
					.select(Entity.OID, Group.CODE, Group.PARENT + "." + Group.CODE, Group.CHILDREN + "." + Group.CODE)
					.from(Group.DEFINITION_NAME)
					.where(new Equals(Group.CODE, key));

			final String[] codeAndParentCode = {null, null, null};
			final ArrayList<String> childCodes = new ArrayList<String>();
			em.search(q, new Predicate<Object[]>() {
				@Override
				public boolean test(Object[] dataModel) {
					codeAndParentCode[0] = (String) dataModel[0];
					codeAndParentCode[1] = (String) dataModel[1];
					codeAndParentCode[2] = (String) dataModel[2];
					if (dataModel[3] != null) {
						childCodes.add((String) dataModel[3]);
					}
					return true;
				}
			});

			if (codeAndParentCode[0] == null) {
				return null;
			}
			return new GroupContext(codeAndParentCode[0], codeAndParentCode[1], codeAndParentCode[2], childCodes.toArray(new String[childCodes.size()]), authorizeContext);
		});
	}

	@Override
	public String getKey(GroupContext val) {
		return val.getGroupCode();
	}

	@Override
	public List<GroupContext> loadByIndex(int indexType, Object indexVal) {
		return null;
	}
	@Override
	public long getVersion(GroupContext value) {
		return 0;
	}
	@Override
	public Object getIndexVal(int indexType, GroupContext value) {
		return null;
	}

}
