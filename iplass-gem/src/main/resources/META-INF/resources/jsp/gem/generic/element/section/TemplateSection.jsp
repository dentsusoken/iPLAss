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

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.section.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%
	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	TemplateSection section = (TemplateSection) element;
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	if ((type == OutputType.EDIT && section.isHideDetail())
			|| (type == OutputType.VIEW && section.isHideView())) return;

	String id = "";
	if (section.getId() != null) id = "id=\"" + StringUtil.escapeHtml(section.getId()) + "\"";

	String style = "";
	if (StringUtil.isNotBlank(section.getStyle())) {
		style = section.getStyle();
	}
	
	String styleAttr = ViewUtil.buildHeightStyleAttr(section.getSectionHeight());
%>
<div <%=id %> class="template-section <c:out value="<%=style %>"/>" style="<%= styleAttr %>">
<%
	TemplateUtil.includeTemplate(section.getTemplateName(), pageContext);
%>
</div>
