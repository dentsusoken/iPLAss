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

package org.iplass.mtp.impl.entity.auth;

import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.permission.EntityPropertyPermission;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.authorize.AuthorizationContext;

public interface EntityAuthContext extends AuthorizationContext {

//	public boolean isPermit(EntityPermission.Action action, AuthContextHolder user);
//	public boolean isPermit(String propertyName, EntityPropertyPermission.Action action, AuthContextHolder user);
	public Condition addLimitingCondition(Condition orignal, EntityPermission.Action action, AuthContextHolder user);
	public Query modifyQuery(Query orignal, EntityPermission.Action action, AuthContextHolder user);
	public Query modifyQuery(Query orignal, EntityPermission.Action action, EntityPropertyPermission.Action propAction, AuthContextHolder user);
	public boolean hasLimitCondition(EntityPermission permission, AuthContextHolder user);

}
