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

package org.iplass.mtp.impl.script;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.GroovyClassLoader;

public class GroovyScriptEngine implements ScriptEngine {
	
	public static final String UTILITY_CLASS_META_PATH = "/utilityClass/";

	private static Logger logger = LoggerFactory.getLogger(GroovyScriptEngine.class);

	private final GroovyClassLoader sharedClassLoader;

	public GroovyScriptEngine(GroovyClassLoader classLoader, GroovyScriptService service) {
		this(classLoader, service, false);
	}

	public GroovyScriptEngine(GroovyClassLoader classLoader, GroovyScriptService service, boolean vanilla) {
		//ResourceLoaderを取り換える前にやっとかないと、ループしてしまう。
//		if (service.getInitScript() != null) {
//			GroovyScript expandScript = new GroovyScript(classLoader, service.getInitScript(), "___initScript___");
//			expandScript.eval(newScriptContext());
//		}
		
		if (!vanilla) {
			classLoader.setResourceLoader(new UtilitiyClassResourceLoader(classLoader.getResourceLoader(), service));
		}
		this.sharedClassLoader = classLoader;
		//Entity、UserBinding、ActionParameterBindingの拡張。Groovy上で扱いやすいように
		
		
		logger.debug("created GroovyScriptEngine");
	}
	
	@Override
	public Script createScript(String script, String name) {
		return createScript(script, name, false);
	}

	@Override
	public Script createScript(String script, String name, boolean sharedClass) {

		//英数以外だと、エラーとなるので_に置換する
        name = name.replaceAll("[^\\w\\d]", "_");

        // 先頭が数字だとエラーなるので必ず頭に_を追加する
        name = "_".concat(name);

		if (logger.isDebugEnabled()) {
			logger.debug("create Script:" + name + " ...");
		}
		if (sharedClass) {
			return new GroovyScript(sharedClassLoader, script, name);
		} else {
			return new GroovyScript(new GroovyClassLoader(sharedClassLoader), script, name);
		}
	}

	public ScriptContext newScriptContext() {
		return new GroovyScriptContext();
	}
	
	@Override
	public ScriptContext newScriptContext(PrintWriter out) {
		return new GroovyScriptContext(out);
	}
	
	public GroovyClassLoader getSharedClassLoader() {
		return sharedClassLoader;
	}

	@Override
	public void invalidate() {
		sharedClassLoader.clearCache();
		if (sharedClassLoader.getResourceLoader() instanceof UtilitiyClassResourceLoader) {
			((UtilitiyClassResourceLoader) sharedClassLoader.getResourceLoader()).clearClassNameList();
		}
	}

	
}
