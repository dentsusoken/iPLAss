package org.iplass.mtp.impl.report;

import java.util.Map;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptContext;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.jxls.expression.ExpressionEvaluator;

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
