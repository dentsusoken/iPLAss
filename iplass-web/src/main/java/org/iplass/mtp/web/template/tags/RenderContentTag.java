/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
 * LayoutアクションのJSPにおいて、実際のコンテンツを表示する箇所を指定するJSPタグです。
 * </p>
* <p>
 * {@link TemplateUtil#renderContent(PageContext)}を呼び出します。
 * </p>
 * <p>
 * 利用例：<br>
 * <pre>
 * &lt;%{@literal @}page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;
 * &lt;%{@literal @}taglib prefix="m" uri="http://iplass.org/tags/mtp"%&gt;
 *
 * :
 * &lt;div id="main"&gt;
 * 
 * &lt;!-- ここにメインコンテンツが出力される --&gt;
 * &lt;m:renderContent /&gt;
 * 
 * &lt;/div&gt;
 *
 * </pre>
 * </p>
 * 
 * 
 * @author K.Higuchi
 *
 */
public class RenderContentTag extends SimpleTagSupport {
	
	@Override
	public void doTag() throws JspException, IOException {
		try {
			TemplateUtil.renderContent((PageContext) getJspContext());
		} catch (ServletException e) {
			throw new JspException(e);
		}
	}

}
