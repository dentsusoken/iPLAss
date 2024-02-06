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
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	String title = ViewUtil.getDispTenantName();

	String topViewListOffset = (String) request.getAttribute(Constants.TOPVIEW_LIST_OFFSET);
	if (topViewListOffset == null){topViewListOffset = "";}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge" />
<title><c:out value="<%= title %>"/></title>
<%@include file="../layout/resource/simpleResource.jsp" %>
<%@include file="../layout/resource/skin.jsp" %>
<%@include file="../layout/resource/theme.jsp" %>
<%@include file="../layout/resource/langfont.jsp" %>
<%@include file="../layout/resource/tenant.jsp" %>
<script type="text/javascript">
$(function() {
	submitForm(contextPath + "/${m:escJs(backPath)}", {
		<%=Constants.DEF_NAME%>:"${m:escJs(defName)}",
		<%=Constants.SEARCH_COND%>:$(":hidden[name='searchCond']").val(),
		<%=Constants.TOPVIEW_LIST_OFFSET%>:"<%=StringUtil.escapeJavaScript(topViewListOffset)%>"
	});
});
</script>
</head>
<body>
<input type="hidden" id="searchCond" name="searchCond" value="${m:esc(searchCond)}">
</body>