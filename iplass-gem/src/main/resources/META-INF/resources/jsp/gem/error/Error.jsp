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
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.web.WebRequestConstants"%>
<%@ page import="org.iplass.mtp.ApplicationException"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
if (!response.isCommitted()) {
	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}

request.setAttribute("title", ViewUtil.getDispTenantNameWithDispChecked());
request.setAttribute("imgUrl", ViewUtil.getTenantImgUrlWithDispChecked());
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge" />
<title>${m:rs("mtp-gem-messages", "error.Error.err")}</title>
<%@include file="../layout/resource/simpleResource.jsp" %>
<%@include file="../layout/resource/skin.jsp" %>
<%@include file="../layout/resource/theme.jsp" %>
<%@include file="../layout/resource/langfont.jsp" %>
<%@include file="../layout/resource/tenant.jsp" %>
</head>
<body id="container" class="unexpected-error nomenu-layout">
<%
request.setAttribute("showNavi", false);
TemplateUtil.includeTemplate("gem/layout/header", pageContext);
Exception e = (Exception) request.getAttribute(WebRequestConstants.EXCEPTION);
if (e == null) {
	e = (Exception) request.getAttribute(WebRequestConstants.EXCEPTION);
}
%>
<div id="content">
<div id="main">
<div class="error-block">
<h2 class="hgroup-01">
<span>
<i class="far fa-circle default-icon"></i>
</span>
${m:rs("mtp-gem-messages", "error.Error.errOccurred")}
</h2>
<table class="tbl-error mb10">
<tbody>
<tr>
<td>
<%if (e instanceof ApplicationException) {%>
<%=e.getMessage()%><br />
<%} else {%>
${m:rs("mtp-gem-messages", "error.Error.retryMsg")}<br />
<%if (request.getAttribute("org.iplass.mtp.adminAddress") != null) {%>
${m:rs("mtp-gem-messages", "error.Error.contactMsg")}<br />
<span>
<a href="mailto:<%=request.getAttribute("org.iplass.mtp.adminAddress")%>"><%=request.getAttribute("org.iplass.mtp.adminAddress")%></a>
</span>
<%}%>
<%}%>
</td>
</tr>
</tbody>
</table>
<p><a href="javascript:void(0)" onclick="history.back();">${m:rs("mtp-gem-messages", "error.Error.back")}</a></p>
</div>
</div>
</div>
<%
TemplateUtil.includeTemplate("gem/layout/footer", pageContext);
%>
</body>
</html>
