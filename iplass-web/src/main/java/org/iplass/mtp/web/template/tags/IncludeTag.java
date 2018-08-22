/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.web.template.tags;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.iplass.mtp.web.template.TemplateUtil;

/**
 * <p>
 * 別Action、TemplateをincludeするためのJSPタグです。
 * </p>
 * <p>
 * {@link TemplateUtil#include(String, PageContext)}もしくは、{@link TemplateUtil#includeTemplate(String, PageContext)}を呼び出します。
 * </p>
 * 
 * <p>
 * 利用例：Actionをinclude<br>
 * <pre>
 * &lt;%{@literal @}page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;
 * &lt;%{@literal @}taglib prefix="m" uri="http://iplass.org/tags/mtp"%&gt;
 *
 * :
 *
 * &lt;m:include action="your/action/path" /&gt;
 * </pre>
 * </p>
 * 
 * <p>
 * 利用例：Templateをinclude<br>
 * <pre>
 * &lt;%{@literal @}page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;
 * &lt;%{@literal @}taglib prefix="m" uri="http://iplass.org/tags/mtp"%&gt;
 *
 * :
 *
 * &lt;m:include template="your/template/path" /&gt;
 * </pre>
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class IncludeTag extends SimpleTagSupport {
	
	private String action;
	private String template;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
		this.template = null;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
		this.action = null;
	}
	@Override
	public void doTag() throws JspException, IOException {
		try {
			if (action != null) {
				TemplateUtil.include(action, (PageContext) getJspContext());
			} else if (template != null) {
				TemplateUtil.includeTemplate(template, (PageContext) getJspContext());
			} else {
				throw new IllegalArgumentException("action or template must specified.");
			}
		} catch (ServletException e) {
			throw new JspException(e);
		}
	}

}
