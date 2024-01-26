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

package org.iplass.mtp.impl.auth.authorize.builtin.webapi;

import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.webapi.permission.WebApiParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WebApiPermissionEntry {

	private static final Logger logger = LoggerFactory.getLogger(WebApiPermissionEntry.class);

	private static final String SCRIPT_PREFIX = "WebApiPermissionEntry_parameterCond";

	private String role;
	private Script parameterCondition;
	private boolean isScriptError = false;

	WebApiPermissionEntry(String role, String oid, String parameterConditionExp) {
		this.role = role;
		if (parameterConditionExp != null) {
			ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			try {
				parameterCondition = se.createScript(parameterConditionExp, SCRIPT_PREFIX + "_" + oid);
			} catch (RuntimeException e) {
				logger.error("parameter condition expression is invalid, so set permission to Not Allowed: " + parameterConditionExp, e);
				isScriptError = true;
			}
		}
	}

	public boolean hasParam() {
		return parameterCondition != null;
	}

	public String getRole() {
		return role;
	}

	public boolean isPermit(AuthContextHolder userContext, String webApiName, WebApiParameter param, TenantAuthorizeContext authContext) {
		if (isScriptError) {
			return false;
		}

		if (parameterCondition == null) {
			return true;
		}

		ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
		ScriptContext sc = se.newScriptContext();
		sc.setAttribute("user", userContext.newUserBinding(authContext));
		sc.setAttribute("session", SessionBinding.newSessionBinding());
		sc.setAttribute("webApi", webApiName);
		sc.setAttribute("parameter", new WebApiParameterBinding(param));
		sc.setAttribute("request", RequestContextBinding.newRequestContextBinding());
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
