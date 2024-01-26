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
package org.iplass.mtp.impl.auth.oauth.introspectors;

import java.util.Map;

import org.iplass.mtp.auth.oauth.definition.CustomTokenIntrospectorDefinition;
import org.iplass.mtp.auth.oauth.definition.introspectors.ScriptingCustomTokenIntrospectorDefinition;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.oauth.MetaCustomTokenIntrospector;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;

public class MetaScriptingCustomTokenIntrospector extends MetaCustomTokenIntrospector {
	private static final long serialVersionUID = 7094142390799872996L;

	private static final String REQUEST_BINDING_NAME = "request";
	private static final String RESPONSE_BINDING_NAME = "response";
	private static final String RESOURCE_OWNER_BINDING_NAME = "resourceOwner";

	private String script;
	
	public MetaScriptingCustomTokenIntrospector() {
	}
	
	public MetaScriptingCustomTokenIntrospector(String script) {
		this.script = script;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public void applyConfig(CustomTokenIntrospectorDefinition def) {
		script = ((ScriptingCustomTokenIntrospectorDefinition) def).getScript();
	}

	@Override
	public CustomTokenIntrospectorDefinition currentConfig() {
		ScriptingCustomTokenIntrospectorDefinition def = new ScriptingCustomTokenIntrospectorDefinition();
		def.setScript(script);
		return def;
	}

	@Override
	public CustomTokenIntrospectorRuntime createRuntime(String metaId, int index) {
		return new ScriptingCustomTokenIntrospectorRuntime(metaId, index);
	}
	
	public class ScriptingCustomTokenIntrospectorRuntime extends CustomTokenIntrospectorRuntime {
		
		private static final String SCRIPT_PREFIX = "ScriptingCustomTokenIntrospector";

		private Script scriptRuntime;

		private ScriptingCustomTokenIntrospectorRuntime(String metaId, int index) {
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptRuntime = tc.getScriptEngine().createScript(script, SCRIPT_PREFIX + "_" + metaId + "_" + index);
		}

		@Override
		public boolean handle(Map<String, Object> response, RequestContext request, AccessToken token) {
			
			ExecuteContext ex = ExecuteContext.getCurrentContext();
			TenantContext tc = ex.getTenantContext();
			ScriptEngine scriptEngine = tc.getScriptEngine();
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute(REQUEST_BINDING_NAME, new RequestContextBinding(request));
			sc.setAttribute(RESPONSE_BINDING_NAME, response);
			sc.setAttribute(RESOURCE_OWNER_BINDING_NAME, token.getUser());
			
			Boolean ret = (Boolean) scriptRuntime.eval(sc);
			if (ret != null && ret.booleanValue()) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public MetaCustomTokenIntrospector getMetaData() {
			return MetaScriptingCustomTokenIntrospector.this;
		}
		
	}


}
