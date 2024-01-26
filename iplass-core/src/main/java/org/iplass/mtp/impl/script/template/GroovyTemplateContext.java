/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.script.template;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.LoggerPrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroovyTemplateContext {
	private static Logger sysout = LoggerFactory.getLogger("mtp.script.template.out");
	
	
	public static GroovyTemplateContext getContext() {
		return (GroovyTemplateContext) ExecuteContext.getCurrentContext().getAttribute(GroovyTemplateContext.class.getName());
	}
	
	static void init(GroovyTemplateBinding bindings) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		GroovyTemplateContext stack = (GroovyTemplateContext) ec.getAttribute(GroovyTemplateContext.class.getName());
		ec.setAttribute(GroovyTemplateContext.class.getName(), new GroovyTemplateContext(bindings, stack), false);
	}
	
	static void fin() {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		GroovyTemplateContext ctx = (GroovyTemplateContext) ec.getAttribute(GroovyTemplateContext.class.getName());
		if (ctx != null) {
			ec.setAttribute(GroovyTemplateContext.class.getName(), ctx.stack, false);
		}
	}
	
	private GroovyTemplateContext stack;
	
	private LoggerPrintWriter lpw;
	private GroovyTemplateBinding bindings;
	
	GroovyTemplateContext(GroovyTemplateBinding bindings, GroovyTemplateContext stack) {
		this.bindings = bindings;
		this.stack = stack;
		if (stack != null) {
			lpw = stack.lpw;
		}
	}
	
	public GroovyTemplateBinding bindings() {
		return bindings;
	}
	
	public LoggerPrintWriter loggerPrintWriter() {
		if (lpw == null) {
			lpw = new LoggerPrintWriter(sysout);
		}
		return lpw;
	}

}
