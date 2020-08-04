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

<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.web.WebRequestConstants"%>
<%@ page import="org.iplass.gem.command.auth.AuthCommandConstants"%>
<%@ page import="org.iplass.gem.command.auth.UpdateExpirePasswordCommand"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
String errorMessage = null;
Exception e = (Exception) request.getAttribute(AuthCommandConstants.RESULT_ERROR);
if (e == null) {
	e = (Exception) request.getAttribute(WebRequestConstants.EXCEPTION);
}
if (e != null) {
	errorMessage = e.getMessage();
}

request.setAttribute("title", ViewUtil.getDispTenantNameWithDispChecked());
request.setAttribute("imgUrl", ViewUtil.getTenantImgUrlWithDispChecked());

String largeImageUrl = ViewUtil.getTenantLargeImgUrl();
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge" />
<title>${m:esc(title)}</title>

<%@include file="../layout/resource/simpleResource.jsp" %>
<%@include file="../layout/resource/skin.jsp" %>
<%@include file="../layout/resource/theme.jsp" %>
<%@include file="../layout/resource/langfont.jsp" %>
<%@include file="../layout/resource/tenant.jsp" %>
<script type="text/javascript">
<!--
function changePassword() {
	document.passwordForm.submit();
}
//-->
</script>

<%if (StringUtil.isNotBlank(largeImageUrl)){%>
<style>
#login #main #header {
background: none;
height: auto;
}
</style>
<%}%>

</head>

<body id="login">
<div id="container">

<%
request.setAttribute("showNavi", false);
TemplateUtil.includeTemplate("gem/layout/header", pageContext);
%>

<div id="content">
<div id="main" class="expire">

<%if (StringUtil.isNotBlank(largeImageUrl)){%>
<img src="<%=largeImageUrl%>" class="login-logo" />
<%}%>

<h2 class="hgroup-01">${m:rs("mtp-gem-messages", "auth.Expire.passChng")}</h2>

<%if (errorMessage != null) {%>
<div class="error">
<span class="error"><%= errorMessage %></span>
</div>
<%}%>

<form name="passwordForm" method="post" action="${m:tcPath()}/<%=UpdateExpirePasswordCommand.ACTION_UPDATE_EXP_PASSWORD%>">
<table class="tbl-login-01">
<tr>
<th style="width:22em;">${m:rs("mtp-gem-messages", "auth.Expire.curPass")}</th>
<td><input type="password" name="<%=AuthCommandConstants.PARAM_PASSWORD%>" value="" /></td>
</tr>
<tr>
<th style="width:22em;">${m:rs("mtp-gem-messages", "auth.Expire.newPass")}</th>
<td><input type="password" name="<%=AuthCommandConstants.PARAM_NEW_PASSWORD%>" value="" /></td>
</tr>
<tr>
<th style="width:22em;">${m:rs("mtp-gem-messages", "auth.Expire.cnfrmNewPass")}</th>
<td><input type="password" name="<%=AuthCommandConstants.PARAM_CONFIRM_PASSWORD%>" value="" /></td>
</tr>
</table>

<p class="nav-login-01">
<input type="button" value="${m:rs('mtp-gem-messages', 'auth.Expire.change')}" class="gr-btn" onclick="changePassword();return false;" />
</p>

${m:outputToken('FORM_XHTML', true)}
</form>
</div><!-- main -->
</div><!-- content -->

<%
TemplateUtil.includeTemplate("gem/layout/footer", pageContext);
%>
</div><!-- container -->
</body>
</html>
