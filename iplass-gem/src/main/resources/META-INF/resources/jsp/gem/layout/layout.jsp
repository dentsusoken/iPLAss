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
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge" />
<%
request.setAttribute(ViewConst.DESIGN_TYPE, ViewConst.DESIGN_TYPE_GEM);

String title = ViewUtil.getDispTenantName();
%>
<title><c:out value="<%= title %>"/></title>

<jsp:include page="./resource/resource.jsp" />
<jsp:include page="./resource/skin.jsp" />
<jsp:include page="./resource/theme.jsp" />
<jsp:include page="./resource/langfont.jsp" />
<jsp:include page="./resource/tenant.jsp" />
</head>
<body class="${skinName}">
<div id="dialog_parent"></div>

<div id="container">

<%
request.setAttribute("showNavi", true);
%>
<m:include template="gem/layout/header" />

<div id="content">
<m:include template="gem/layout/navi" />

<div id="split-block">
<p id="split-btn"></p>
</div><!--split-block-->

<div id="content-inner">
<div id="main">
<div id="main-inner">
<m:renderContent />
</div><!-- main-inner -->
</div><!-- main -->
</div><!-- content-inner -->
</div><!-- content -->

<m:include template="gem/layout/footer" />

<%
String frameName = "modalFrame-" + Math.random() * 1000 + "-" + new Date().getTime();
%>
<div class="modal-dialogs">
<div class="modal-dialog" id="modal-dialog-root">
<div class="modal-wrap">
</div>
<div class="modal-inner">
<h2 class="hgroup-01"><span id="modal-title"></span></h2>
<p class="modal-maximize" id="modal-maximize-root">${m:rs("mtp-gem-messages", "layout.layout.maximize")}</p>
<p class="modal-restore" id="modal-restore-root">${m:rs("mtp-gem-messages", "layout.layout.restore")}</p>
<p class="modal-close" id="modal-close-root">${m:rs("mtp-gem-messages", "layout.layout.close")}</p>
<iframe name="<%=frameName%>" src="about:blank">
</iframe>
</div><!--modal-inner-->
</div><!--modal-dialog-->
</div><!--modal-dialogs-->

<div class="tooltip-wrap">
<div class="tooltip">
<p class="tooltxt"></p>
<span class="tool-icon"></span>
</div><!--tooltip-->
</div><!--tooltip-wrap-->

</div><!-- container  -->
</body>
</html>
