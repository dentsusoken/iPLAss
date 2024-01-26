/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.report;

import java.util.Map;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.jxls.expression.ExpressionEvaluator;

/**
 * JXLSの式評価をGroovyベースで実行する為のクラス
 * 
 * @author Y.Ishida
 *
 */
public class JxlsGroovyEvaluator implements ExpressionEvaluator {
	
	private JxlsCompiledScriptCacheStore cacheStore;
	private ScriptEngine se;
	
	public JxlsGroovyEvaluator(JxlsCompiledScriptCacheStore cacheStore) {
		this.cacheStore = cacheStore;
		TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
		se = tc.getScriptEngine();
	}
		
	@Override
	public Object evaluate(String expression, Map<String, Object> context) {
		Script script = cacheStore.getScript(expression);
		
		ScriptContext scriptContext = se.newScriptContext();
		if (context != null) {
			setAttribute(scriptContext, context);
		}
		return script.eval(scriptContext);
	}

	private void setAttribute(ScriptContext scriptContext, Map<String, Object> context) {
		for (Map.Entry<String, Object> entry : context.entrySet()) {
			scriptContext.setAttribute(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Object evaluate(Map<String, Object> context) {
		return null;
	}

	@Override
	public String getExpression() {
		return null;
	}
	

}
