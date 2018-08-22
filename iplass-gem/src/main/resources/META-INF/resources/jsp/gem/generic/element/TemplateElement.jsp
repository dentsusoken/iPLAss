<%--
 Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%
	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	Integer colNum = (Integer) request.getAttribute(Constants.COL_NUM);

	TemplateElement template = (TemplateElement) element;

	//列数で幅調整
	if (colNum == null || colNum < 1) {
		colNum = 1;
	}
	String cellStyle = "section-data col" + colNum;

	String title = TemplateUtil.getMultilingualString(template.getTitle(), template.getLocalizedTitleList());
%>
<th class="<c:out value="<%=cellStyle%>"/>"><c:out value="<%=title %>"/></th>
<td class="<c:out value="<%=cellStyle%>"/>">
<% TemplateUtil.includeTemplate(template.getTemplateName(), pageContext); %>
</td>
