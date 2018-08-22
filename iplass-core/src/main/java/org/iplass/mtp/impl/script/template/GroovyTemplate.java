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

package org.iplass.mtp.impl.script.template;

import java.io.IOException;
import java.io.Writer;

import org.iplass.mtp.impl.script.Script;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.Writable;

public class GroovyTemplate {

	private Script script;
	private Closure<?> templateClosure;
	
	public GroovyTemplate(Script script) {
		this.script = script;
		GroovyObject object = script.createInstanceAs(GroovyObject.class, null);
		templateClosure = (Closure<?>) object.invokeMethod("getTemplate", null);
		templateClosure.setResolveStrategy(Closure.DELEGATE_FIRST);
	}
	
	public void doTemplate(GroovyTemplateBinding binding) throws IOException {
		Closure<?> template = (Closure<?>) templateClosure.clone();
		template.setDelegate(binding);
		GroovyTemplateContext.init(binding);
		try {
			((Writable) template).writeTo((Writer) binding.getVariable("out"));
		} finally {
			GroovyTemplateContext.fin();
		}
	}
}
