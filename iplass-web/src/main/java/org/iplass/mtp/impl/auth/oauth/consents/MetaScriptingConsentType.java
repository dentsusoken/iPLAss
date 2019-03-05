/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth.consents;

import java.util.List;

import org.apache.commons.collections4.list.UnmodifiableList;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.oauth.definition.ClientType;
import org.iplass.mtp.auth.oauth.definition.ConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.ScriptingConsentTypeDefinition;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.oauth.MetaConsentType;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.command.RequestContextBinding;
import org.iplass.mtp.impl.command.SessionBinding;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;

/**
 * 承認画面表示有無をカスタムロジックで設定するMetaConsentType。
 * 
 * @author K.Higuchi
 *
 */
public class MetaScriptingConsentType extends MetaConsentType {
	private static final long serialVersionUID = 7627301483098333747L;

	private static final String AUTH_CONTEXT_BINDING_NAME = "auth";
	private static final String USER_BINDING_NAME = "user";
	private static final String REQUEST_BINDING_NAME = "request";
	private static final String SESSION_BINDING_NAME = "session";
	private static final String REQUIRED_SCOPES_BINDING_NAME = "requiredScopes";
	private static final String GRANTED_SCOPES_BINDING_NAME = "grantedScopes";
	
	private String script;
	
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public ScriptingConsentTypeRuntime createRuntime(String metaId, ClientType ct) {
		return new ScriptingConsentTypeRuntime(metaId, ct);
	}
	
	public class ScriptingConsentTypeRuntime extends ConsentTypeRuntime {
		
		
		private static final String SCRIPT_PREFIX = "ScriptingConsentType_script";

		private Script scriptRuntime;

		public ScriptingConsentTypeRuntime(String metaId, ClientType ct) {
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptRuntime = tc.getScriptEngine().createScript(script, SCRIPT_PREFIX + "_" + metaId + "_" + ct);
		}

		@Override
		public boolean needConsent(RequestContext request, List<String> scopes, AccessToken currentToken) {
			
			ExecuteContext ex = ExecuteContext.getCurrentContext();
			TenantContext tc = ex.getTenantContext();
			ScriptEngine scriptEngine = tc.getScriptEngine();
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute(REQUEST_BINDING_NAME, RequestContextBinding.newRequestContextBinding());
			sc.setAttribute(SESSION_BINDING_NAME, SessionBinding.newSessionBinding());
			sc.setAttribute(USER_BINDING_NAME, AuthContextHolder.getAuthContext().newUserBinding());
			sc.setAttribute(AUTH_CONTEXT_BINDING_NAME, AuthContext.getCurrentContext());
			sc.setAttribute(REQUIRED_SCOPES_BINDING_NAME, new UnmodifiableList<>(scopes));
			if (currentToken != null) {
				sc.setAttribute(GRANTED_SCOPES_BINDING_NAME, new UnmodifiableList<>(currentToken.getGrantedScopes()));
			}
			
			Boolean ret = (Boolean) scriptRuntime.eval(sc);
			if (ret != null && ret.booleanValue()) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public void applyConfig(ConsentTypeDefinition consentType) {
		script = ((ScriptingConsentTypeDefinition) consentType).getScript();
	}

	@Override
	public ConsentTypeDefinition currentConfig() {
		ScriptingConsentTypeDefinition def = new ScriptingConsentTypeDefinition();
		def.setScript(script);
		return def;
	}

}
