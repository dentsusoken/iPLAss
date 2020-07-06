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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge" />
<%
request.setAttribute(Constants.DIALOG_MODE, true);

String title = ViewUtil.getDispTenantName();
%>
<title><c:out value="<%= title %>"/></title>
<%@include file="./resource/resource.jsp" %>
<%@include file="./resource/skin.jsp" %>
<%@include file="./resource/theme.jsp" %>
<%@include file="./resource/langfont.jsp" %>
<%@include file="./resource/tenant.jsp" %>
<style>
html {
overflow-x: hidden;
}
</style>
</head>
<body class="modal-body ${skinName}">
<div id="dialog_parent"></div>
<div id="container">
<div id="content-inner">
<div id="main">
<div id="main-inner">
<m:renderContent />
</div><!-- main-inner -->
</div><!-- main -->
</div><!-- content-inner -->

<div class="tooltip-wrap">
<div class="tooltip">
<p class="tooltxt"></p>
<span class="tool-icon"></span>
</div><!--tooltip-->
</div><!--tooltip-wrap-->

</div><!-- container  -->
</body>
</html>
