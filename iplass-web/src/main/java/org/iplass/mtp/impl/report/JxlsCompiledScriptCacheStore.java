/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
	
	/**
	 * 引数で指定の式言語(expression)にてキャッシュの値を取得。
	 * キャッシュにない場合、コンパイルされたScriptをキャッシュする。
	 * @param expression
	 * @return
	 */
	public Script getScript (String expression) {
		Script script = jxlsCompiledScriptCache.get(expression);
		
		if (script == null) {
			script = se.createScript(expression, SCRIPT_PREFIX + "_" + GroovyTemplateCompiler.randomName());
			jxlsCompiledScriptCache.put(expression, script);
		}
		
		return script;
	}
	
	
}
