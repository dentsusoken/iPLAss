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

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.Binding;

public class GroovyScriptContext implements ScriptContext {
	
	private static Logger logger = LoggerFactory.getLogger("mtp.script.out");
	
	private Binding binding;
	private PrintWriter out;
	
	public GroovyScriptContext() {
		binding = new Binding();
		setOut(new LoggerPrintWriter(logger));
	}

	public GroovyScriptContext(PrintWriter out) {
		binding = new Binding();
		setOut(out);
	}
	
	public Object getAttribute(String name) {
		return binding.getVariable(name);
	}

	public void setAttribute(String name, Object value) {
		binding.setVariable(name, value);
	}

	public void setOut(PrintWriter out) {
		this.out = out;
		binding.setVariable("out", out);
	}

	public PrintWriter getOut() {
		return out;
	}
	
	public Binding getBinding() {
		return binding;
	}
	
}
