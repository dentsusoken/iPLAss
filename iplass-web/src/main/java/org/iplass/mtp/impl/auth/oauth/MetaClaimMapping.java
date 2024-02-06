/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.oauth.definition.ClaimMappingDefinition;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaClaimMapping implements MetaData {
	private static final long serialVersionUID = -3286338403698162013L;

	private String claimName;
	private String userPropertyName;
	private String customValueScript;
	
	public MetaClaimMapping() {
	}
	
	public MetaClaimMapping(String claimName, String userPropertyName) {
		this.claimName = claimName;
		this.userPropertyName = userPropertyName;
	}
	
	public String getClaimName() {
		return claimName;
	}
	public void setClaimName(String claimName) {
		this.claimName = claimName;
	}
	public String getUserPropertyName() {
		return userPropertyName;
	}
	public void setUserPropertyName(String userPropertyName) {
		this.userPropertyName = userPropertyName;
	}
	public String getCustomValueScript() {
		return customValueScript;
	}
	public void setCustomValueScript(String customValueScript) {
		this.customValueScript = customValueScript;
	}

	@Override
	public MetaClaimMapping copy() {
		return ObjectUtil.deepCopy(this);
	}
	public void applyConfig(ClaimMappingDefinition cmd) {
		claimName = cmd.getClaimName();
		userPropertyName = cmd.getUserPropertyName();
		customValueScript = cmd.getCustomValueScript();
	}
	public ClaimMappingDefinition currentConfig() {
		ClaimMappingDefinition def = new ClaimMappingDefinition();
		def.setClaimName(claimName);
		def.setUserPropertyName(userPropertyName);
		def.setCustomValueScript(customValueScript);
		return def;
	}
	
	public ClaimMappingRuntime createRuntime(String defName, String scopeName) {
		return new ClaimMappingRuntime(defName, scopeName);
	}
	
	public class ClaimMappingRuntime {
		private ScriptEngine se;
		private Script s;
		
		private ClaimMappingRuntime(String defName, String scopeName) {
			if (customValueScript != null) {
				TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
				se = tc.getScriptEngine();
				s = se.createScript(customValueScript, "CustomValueScript_" + defName + "_" + scopeName + "_" + claimName);
			}
		}
		
		public Object value(User user) {
			if (userPropertyName != null) {
				return user.getValue(userPropertyName);
			} else {
				ScriptContext sc = se.newScriptContext();
				sc.setAttribute("user", user);
				return s.eval(sc);
			}
		}
		
		public String name() {
			return claimName;
		}
	}
}
