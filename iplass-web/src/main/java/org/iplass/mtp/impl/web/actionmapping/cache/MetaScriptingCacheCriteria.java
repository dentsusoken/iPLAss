/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.actionmapping.cache;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.ScriptingCacheCriteriaDefinition;

/**
 * キャッシュの一致判定ロジックをスクリプトで記述するCacheCriteria。
 *
 * @author K.Higuchi
 *
 */
public class MetaScriptingCacheCriteria extends MetaCacheCriteria {
	private static final long serialVersionUID = -8948760695898972341L;

	private static final String REQUEST_BINDING_NAME = "request";

	/** キャッシュのキー比較に利用する文字列を返却するよう実装する。 */
	private String script;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public void applyConfig(CacheCriteriaDefinition definition) {
		fillFrom(definition);
		ScriptingCacheCriteriaDefinition def = (ScriptingCacheCriteriaDefinition)definition;
		script = def.getScript();
	}

	@Override
	public CacheCriteriaDefinition currentConfig() {
		ScriptingCacheCriteriaDefinition definition = new ScriptingCacheCriteriaDefinition();
		fillTo(definition);
		definition.setScript(script);
		return definition;
	}

	@Override
	public ScriptingCacheCriteriaRuntime createRuntime(MetaActionMapping actionMapping) {
		return new ScriptingCacheCriteriaRuntime(actionMapping);
	}

	public class ScriptingCacheCriteriaRuntime extends CacheCriteriaRuntime {

		private static final String SCRIPT_PREFIX = "ScriptingCacheCriteriaRuntime_script";

		private Script scriptRuntime;

		public ScriptingCacheCriteriaRuntime(MetaActionMapping actionMapping) {
			TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
			scriptRuntime = tc.getScriptEngine().createScript(script, SCRIPT_PREFIX + "_" + actionMapping.getName());
		}

		@Override
		public String createContentCacheKey(RequestContext request) {
			ExecuteContext ex = ExecuteContext.getCurrentContext();
			TenantContext tc = ex.getTenantContext();
			ScriptEngine scriptEngine = tc.getScriptEngine();
			ScriptContext sc = scriptEngine.newScriptContext();
			sc.setAttribute(REQUEST_BINDING_NAME, request);
			sc.setAttribute("user", AuthContextHolder.getAuthContext().newUserBinding());
			sc.setAttribute("auth", AuthContext.getCurrentContext());
			return (String) scriptRuntime.eval(sc);
		}

	}

}
