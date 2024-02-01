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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
	Element element = (Element) request.getAttribute(Constants.ELEMENT);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	Integer colNum = (Integer) request.getAttribute(Constants.COL_NUM);

	Link link = (Link) element;
	String target = "";
	if (link.isDispNewWindow()) {
		target = "target=\"_blank\"";
	}

	//列数で幅調整
	if (colNum == null || colNum < 1) {
		colNum = 1;
	}
	String cellStyle = "section-data col" + colNum;

	String title = TemplateUtil.getMultilingualString(link.getTitle(), link.getLocalizedTitleList());
	String label = TemplateUtil.getMultilingualString(link.getDisplayLabel(), link.getLocalizedDisplayLabelList());

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(link.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, link.getInputCustomStyleScriptKey(), entity, null);
	}
%>
<th class="<c:out value="<%=cellStyle%>"/>"><c:out value="<%=title %>"/></th>
<td class="<c:out value="<%=cellStyle%>"/>">
<a href="<c:out value="<%=link.getUrl() %>"/>" style="<c:out value="<%=customStyle%>"/>" <%=target %>><c:out value="<%=label %>"/></a>
</td>
