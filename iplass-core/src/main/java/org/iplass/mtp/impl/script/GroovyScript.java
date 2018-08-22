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

package org.iplass.mtp.impl.script;

import org.codehaus.groovy.GroovyBugError;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;


public class GroovyScript implements Script {

	static final String CODE_BASE = "/groovy/script";

//	private GroovyClassLoader classLoader;
	private Class<?> cls;

	public GroovyScript(GroovyClassLoader classLoader, String script, String name) {
		try {
//			this.classLoader = classLoader;
			GroovyCodeSource codeSource = new GroovyCodeSource(script, name + ".groovy", CODE_BASE);
			codeSource.setCachable(false);
			cls = classLoader.parseClass(codeSource);
		} catch (GroovyBugError | NoClassDefFoundError e) {
			throw new ScriptRuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T createInstanceAs(Class<T> type, ScriptContext context) {

		//TODO ScriptContextの必要性の検討。標準出力を入れ替えるのは無理。あと使うとしたら、static変数への代入。

		try {
			if (type.isAssignableFrom(cls)) {
				//TODO newInstance()ではなく、cloneで可能？
				return (T) cls.newInstance();
			} else {
				throw new ScriptRuntimeException(cls.getName() + " is not implimentation class or subclass of " + type);
			}
		} catch (InstantiationException e) {
			throw new ScriptRuntimeException("can not instantiate " + cls.getName(), e);
		} catch (IllegalAccessException e) {
			throw new ScriptRuntimeException("can not instantiate " + cls.getName(), e);
		}
	}

	public Object eval(ScriptContext context) {
		try {
			if (groovy.lang.Script.class.isAssignableFrom(cls)) {
				//TODO newInstance()ではなく、cloneで可能？
				groovy.lang.Script gScript = (groovy.lang.Script) cls.newInstance();
				gScript.setBinding(((GroovyScriptContext) context).getBinding());
				return gScript.run();
			} else {
				//TODO main(String[])を呼ぶ？
				throw new ScriptRuntimeException(cls.getName() + " is not Script.");
			}
		} catch (InstantiationException e) {
			throw new ScriptRuntimeException("can not instantiate " + cls.getName(), e);
		} catch (IllegalAccessException e) {
			throw new ScriptRuntimeException("can not instantiate " + cls.getName(), e);
		}
	}

	public boolean isInstantiateAs(Class<?> type) {
		return type.isAssignableFrom(cls);
	}

	@Override
	protected void finalize() throws Throwable {
		//FIXME GroovyClassLoaderから自身の削除（sharedClassLoaderで、Scriptの場合だけか？）
		super.finalize();
	}



}
