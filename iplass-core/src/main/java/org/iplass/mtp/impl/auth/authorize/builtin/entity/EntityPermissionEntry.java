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

package org.iplass.mtp.impl.auth.authorize.builtin.entity;

import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.auth.authorize.builtin.AuthLimitConditionBinding;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.query.prepared.PreparedQueryTemplate;
import org.iplass.mtp.impl.query.prepared.PreparedQueryTemplateFactory;

class EntityPermissionEntry {
	
	private String role;
	private PreparedQueryTemplate condition;
	private boolean isPermit;

	EntityPermissionEntry(String role, String conditionExpression, boolean isPermit) {
		this.role = role;
		if (conditionExpression != null) {
			condition = PreparedQueryTemplateFactory.createPreparedQueryTemplate(conditionExpression);
		}
		this.isPermit = isPermit;
	}
	
	public String getRole() {
		return role;
	}
	
	public Condition getCondition(UserBinding user, TenantAuthorizeContext authContext) {
		if (condition == null) {
			return null;
		}
		
		AuthLimitConditionBinding bind = new AuthLimitConditionBinding(
				ExecuteContext.getCurrentContext().getCurrentTimestamp(),
				user,
				SessionBinding.newSessionBinding(),
				authContext);
		
		return condition.condition(bind);
	}
	
	public boolean hasLimitCondition() {
		return condition != null;
	}
	
	public boolean isPermit() {
		return isPermit;
	}
	

}
