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

package org.iplass.mtp.impl.core.config;

import java.util.Collections;
import java.util.Map;

import org.iplass.mtp.spi.ObjectBuilder;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyScriptObjectBuilder<T> implements ObjectBuilder<T> {
	private Binding binding;
	private String script;
	
	public GroovyScriptObjectBuilder(String script) {
		this.script = script;
		binding = new Binding();
		binding.setVariable("properties", Collections.emptyMap());
		binding.setVariable("args", Collections.emptyMap());
	}
	
	@Override
	public void setName(String name) {
		binding.setVariable("name", name);
	}

	@Override
	public void setValue(String value) {
		binding.setVariable("value", value);
	}

	@Override
	public void setClassName(String className) {
		binding.setVariable("className", className);
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		binding.setVariable("properties", properties);
	}

	@Override
	public void setArgs(Map<String, Object> args) {
		binding.setVariable("args", args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T build() {
		GroovyShell gshell = new GroovyShell(binding);
		return (T) gshell.evaluate(script);
	}

}
