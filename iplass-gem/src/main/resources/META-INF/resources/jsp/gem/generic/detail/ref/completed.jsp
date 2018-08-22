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

<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData" %>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<!DOCTYPE html>
<%
	//データ取得
	DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);
	String modalTarget = request.getParameter(Constants.MODAL_TARGET);

	if (modalTarget == null) modalTarget = "";
	else modalTarget = StringUtil.escapeHtml(modalTarget);

	String title = ViewUtil.getDispTenantName();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><c:out value="<%= title %>"/></title>
<%@include file="../../../layout/resource/simpleResource.jsp" %>
<%@include file="../../../layout/resource/skin.jsp" %>
<%@include file="../../../layout/resource/theme.jsp" %>
<%@include file="../../../layout/resource/tenant.jsp" %>
<script type="text/javascript">
var key = "<%=modalTarget%>";
var modalTarget = key != "" ? key : null;
$(function() {
	var entity = {
		oid:"<%=StringUtil.escapeJavaScript(data.getEntity().getOid())%>",
		version:"<%=data.getEntity().getVersion()%>",
		name:"<%=StringUtil.escapeJavaScript(data.getEntity().getName())%>"
	};
	var func = null;
	var windowManager = document.rootWindow.scriptContext["windowManager"];
	if (modalTarget && windowManager && windowManager[document.targetName]) {
		var win = windowManager[modalTarget];
		func = win.scriptContext["editReferenceCallback"];
	} else {
		func = parent.document.scriptContext["editReferenceCallback"];
	}
	if (func && $.isFunction(func)) {
		func.call(this, entity);
	}
});
</script>
</head>
<body class="modal-body">
</body>
</html>
