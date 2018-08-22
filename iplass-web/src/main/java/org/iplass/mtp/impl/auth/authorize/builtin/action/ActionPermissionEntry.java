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

package org.iplass.mtp.impl.auth.authorize.builtin.action;

import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.web.actionmapping.permission.ActionParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ActionPermissionEntry {

	private static final Logger logger = LoggerFactory.getLogger(ActionPermissionEntry.class);

	private static final String SCRIPT_PREFIX = "ActionPermissionEntry_parameterCond";

	private final String role;
	private final Script parameterCondition;
	private final boolean isScriptError;

	ActionPermissionEntry(String role, String oid, String parameterConditionExp) {
		this.role = role;
		boolean localIsScriptError = false;
		Script localParameterCondition = null;
		if (parameterConditionExp != null) {
			ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			try {
				localParameterCondition = se.createScript(parameterConditionExp, SCRIPT_PREFIX + "_" + oid);
			} catch (RuntimeException e) {
				logger.error("parameter condition expression is invalid, so set permission to Not Allowed: " + parameterConditionExp, e);
				localIsScriptError = true;
			}
		}

		isScriptError = localIsScriptError;
		parameterCondition = localParameterCondition;
	}

	public boolean hasParam() {
		return parameterCondition != null;
	}

	public String getRole() {
		return role;
	}

	public boolean isPermit(AuthContextHolder authContextHolder, String actionName, ActionParameter param, TenantAuthorizeContext tenantContext) {
		if (isScriptError) {
			return false;
		}

		if (parameterCondition == null) {
			return true;
		}

		ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
		ScriptContext sc = se.newScriptContext();
		sc.setAttribute("user", authContextHolder.newUserBinding(tenantContext));
		sc.setAttribute("session", SessionBinding.newSessionBinding());
		sc.setAttribute("action", actionName);
		sc.setAttribute("parameter", new ActionParameterBinding(param));
		Boolean ret;
		try {
			ret = (Boolean) parameterCondition.eval(sc);
		} catch (RuntimeException e) {
			logger.error("can not eval condition expression. so return not Allowed:" + e.getMessage(), e);
			return false;
		}
		if (ret != null && ret.booleanValue()) {
			return true;
		}
		return false;
	}

}
