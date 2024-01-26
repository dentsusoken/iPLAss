<%--
 Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
 
 Unless you have purchased a commercial license,
 the following license terms apply:
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%
	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	Integer colNum = (Integer) request.getAttribute(Constants.COL_NUM);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	TemplateElement template = (TemplateElement) element;

	//列数で幅調整
	if (colNum == null || colNum < 1) {
		colNum = 1;
	}
	String cellStyle = "section-data col" + colNum;
	if (StringUtil.isNotBlank(template.getStyle())) {
		cellStyle += " " + template.getStyle();
	}

	String title = TemplateUtil.getMultilingualString(template.getTitle(), template.getLocalizedTitleList());
	
	boolean required = template.getRequiredDisplayType() == RequiredDisplayType.DISPLAY;

	String tooltip = "";
	if (StringUtil.isNotBlank(template.getTooltip())) {
		tooltip = TemplateUtil.getMultilingualString(template.getTooltip(), template.getLocalizedTooltipList());
	}
%>
<th class="<c:out value="<%=cellStyle%>"/>"><c:out value="<%=title %>"/>
<%
	if (OutputType.EDIT == type) {
		 if (required) {
%>
<span class="ico-required ml10 vm">${m:rs("mtp-gem-messages", "generic.element.property.Property.required")}</span>
<%
		}

		if (StringUtil.isNotEmpty(tooltip)) {
%>
<%-- XSS対応-メタの設定のため対応なし(tooltip) --%>
<span class="ml05"><img src="${m:esc(skinImagePath)}/icon-help-01.png" alt="" class="vm tp"  title="<%=tooltip %>" /></span>
<%
		}
	}
%>
</th>
<td class="<c:out value="<%=cellStyle%>"/>">
<% TemplateUtil.includeTemplate(template.getTemplateName(), pageContext); %>
</td>
