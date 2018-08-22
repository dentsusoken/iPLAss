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

import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleContext {

	private static final Logger logger = LoggerFactory.getLogger(RoleContext.class);

	private static final String SCRIPT_PREFIX = "RoleContext_roleCondition";

	private String roleCode;
	private long rolePriolitiy;
	private Script[] roleCondition;
	private TenantAuthorizeContext tenantAuthContext;

	public RoleContext(String roleCode, long rolePriolitiy, String[] roleConditionExp, TenantAuthorizeContext tenantAuthContext) {
		this.roleCode = roleCode;
		this.tenantAuthContext = tenantAuthContext;
		this.rolePriolitiy = rolePriolitiy;

		if (roleConditionExp != null) {
			roleCondition = new Script[roleConditionExp.length];
			ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			for (int i = 0; i < roleConditionExp.length; i++) {
				try {
					roleCondition[i] = se.createScript(roleConditionExp[i], SCRIPT_PREFIX + "_" + roleCode + "_" + i);
				} catch (RuntimeException e) {
					logger.error("role condition expression is invalid, so ignore this expression: " + roleConditionExp[i], e);
				}
			}
		}
	}

	public String getRoleCode() {
		return roleCode;
	}

	//ロール条件

	//Groovyで書くと、、
	// user.accountId='admin' && user.memberOf('jimukyoku') && user.rank.level > 5
	// user.accountId='admin' && user.groupCode.any{it.matches('hoge.*')} && user.rank.level > 5
	public boolean userInRole(AuthContextHolder userAuthContext) {

		//admin（かつ、他テナントユーザでない）は全権限保持
		UserBinding user = userAuthContext.newUserBinding(tenantAuthContext);
		if (user.isGrantAllPermissions()) {
			return true;
		}

		if (roleCondition != null) {
			ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			ScriptContext sc = se.newScriptContext();
			sc.setAttribute("user", user);
			sc.setAttribute("session", SessionBinding.newSessionBinding());
			sc.setAttribute("request", RequestContextBinding.newRequestContextBinding());

			for (Script s: roleCondition) {
				if (s != null) {
					Boolean ret;
					try {
						ret = (Boolean) s.eval(sc);
					} catch (RuntimeException e) {
						logger.error("can not eval condition expression. so return false:role=" + roleCode  + ":"+ e.getMessage(), e);
						return false;
					}
					if (ret != null && ret.booleanValue()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public long getPriority() {
		return rolePriolitiy;
	}

}
