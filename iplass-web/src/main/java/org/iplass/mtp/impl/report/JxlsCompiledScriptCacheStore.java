package org.iplass.mtp.impl.report;

import java.util.concurrent.ConcurrentHashMap;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;

public class JxlsCompiledScriptCacheStore {
	
	private ScriptEngine se;
	private ConcurrentHashMap<String, Script> jxlsCompiledScriptCache;
	private static final String SCRIPT_PREFIX = "JxlsGroovyEvaluator_script";
	
	public JxlsCompiledScriptCacheStore() {
		TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
		se = tc.getScriptEngine();
		jxlsCompiledScriptCache = new ConcurrentHashMap<String, Script>();
	}
	
	public Script getScript (String expression) {
		Script script = jxlsCompiledScriptCache.get(expression);
		
		if (script == null) {
			script = se.createScript(expression, SCRIPT_PREFIX + "_" + GroovyTemplateCompiler.randomName());
			jxlsCompiledScriptCache.put(expression, script);
		}
		
		return script;
	}
	
	
}
