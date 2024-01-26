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
package org.iplass.mtp.impl.web.template.groovy;

import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.script.template.GTmplBase;
import org.iplass.mtp.impl.script.template.GroovyTemplateContext;
import org.iplass.mtp.web.template.TemplateUtil;
import org.iplass.mtp.web.template.TemplateUtil.TokenOutputType;

import groovy.lang.Closure;

public class WebGTmplBase extends GTmplBase {
	
	protected String esc(Object value) {
		return escHtml(value);
	}
	protected String token() {
		return TemplateUtil.outputToken(TokenOutputType.VALUE, true);
	}
	protected String fixToken() {
		return TemplateUtil.outputToken(TokenOutputType.VALUE, false);
	}
	protected String outputToken(Object tokenOutputType, boolean createNew) {
		if (tokenOutputType instanceof TokenOutputType) {
			return TemplateUtil.outputToken((TokenOutputType) tokenOutputType, createNew);
		}
		return TemplateUtil.outputToken(TokenOutputType.valueOf(tokenOutputType.toString()), createNew);
	}
	protected RequestContext rc() {
		return TemplateUtil.getRequestContext();
	}
	protected String tcPath() {
		return TemplateUtil.getTenantContextPath();
	}
	
	protected void bind(Map<String, Object> params, Closure<Void> inner) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		BindContext parent = (BindContext) ec.getAttribute(BindContext.class.getName());
		BindContext bindContext = new BindContext(params, parent);
		ec.setAttribute(BindContext.class.getName(), bindContext, false);
		try {
			inner.setResolveStrategy(Closure.DELEGATE_FIRST);
			if (bindContext.getBinding() != null) {
				inner.setDelegate(bindContext.getBinding());
			}
			inner.call();
		} finally {
			ec.setAttribute(BindContext.class.getName(), parent, false);
		}
	}
	
	protected void errors() {
		errors(Collections.emptyMap(), null);
	}
	
	protected void errors(Closure<Void> inner) {
		errors(Collections.emptyMap(), inner);
	}
	
	protected void errors(Map<String, Object> params) {
		errors(params, null);
	}
	
	protected void errors(Map<String, Object> params, Closure<Void> inner) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		BindContext bindContext = (BindContext) ec.getAttribute(BindContext.class.getName());
		ErrorsContext errors = new ErrorsContext(params, bindContext);
		if (errors.hasError()) {
			if (inner != null) {
				if (errors.getBinding() != null) {
					inner.setDelegate(errors.getBinding());
				}
				inner.call();
			} else {
				errors.write((Writer) GroovyTemplateContext.getContext().bindings().getVariable("out"));
			}
		}
	}
	
}
