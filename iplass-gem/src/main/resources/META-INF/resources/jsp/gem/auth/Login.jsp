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
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.impl.core.ExecuteContext"%>
<%@ page import="org.iplass.mtp.message.MessageManager"%>
<%@ page import="org.iplass.mtp.message.MessageItem"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.tenant.TenantAuthInfo"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.web.WebRequestConstants"%>
<%@ page import="org.iplass.gem.command.auth.AuthCommandConstants"%>
<%@ page import="org.iplass.gem.command.auth.LoginCommand"%>
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
String orgUrl = (String) request.getAttribute(WebRequestConstants.REDIRECT_PATH);

request.setAttribute("title", ViewUtil.getDispTenantNameWithDispChecked());
request.setAttribute("imgUrl", ViewUtil.getTenantImgUrlWithDispChecked());

String largeImageUrl = ViewUtil.getTenantLargeImgUrl();

boolean isRememberMe = ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantAuthInfo.class).isUseRememberMe();
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

<%if (isRememberMe) {%>
<script type="text/javascript">
<!--
$(function() {
	if (getCookie("rememberMeFlag")=="1") {
		$("#rememberMe").trigger("click");
	}
	$("#rememberMe").on("change", function(){
	    if ($(this).is(':checked')) {
	    	setCookie("rememberMeFlag", "1", 3650);
	    } else {
	    	setCookie("rememberMeFlag", "0", -1);
	    }
	});
	document.loginForm.<%=AuthCommandConstants.PARAM_USER_ID%>.focus();
});
//-->
</script>
<%} else {%>
<script type="text/javascript">
<!--
$(function() {
	document.loginForm.<%=AuthCommandConstants.PARAM_USER_ID%>.focus();
});
//-->
</script>
<%}%>

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
<div id="main">

<%if (StringUtil.isNotBlank(largeImageUrl)){%>
<img src="<%=largeImageUrl%>" class="login-logo" />
<%}%>

<h2>${m:rs("mtp-gem-messages", "auth.Login.login")}</h2>

<%if (errorMessage != null) {%>
<div class="error">
<span class="error"><c:out value="<%= errorMessage %>"/></span>
</div>
<%}%>

<form name="loginForm" method="post" action="${m:tcPath()}/<%=LoginCommand.ACTION_LOGIN%>">
<table class="tbl-login-01">
<tr>
<th>${m:rs("mtp-gem-messages", "auth.Login.id")}</th>
<td><input type="text" name="<%=AuthCommandConstants.PARAM_USER_ID%>" value="" /></td>
</tr>
<tr>
<th>${m:rs("mtp-gem-messages", "auth.Login.pass")}</th>
<td><input type="password" name="<%=AuthCommandConstants.PARAM_PASSWORD%>" value="" /></td>
</tr>
<%if (isRememberMe) {%>
<tr>
<th></th>
<td><label><input type="checkbox" id="rememberMe" name="<%=AuthCommandConstants.PARAM_REMEMBER_ME%>" value="1" />${m:rs("mtp-gem-messages", "auth.Login.rememberMe")}</label></td>
</tr>
<%}%>
</table>

<p class="nav-login-01">
<input type="submit" class="gr-btn" value="${m:rs('mtp-gem-messages', 'auth.Login.login')}" />
</p>

<%if (orgUrl != null) {%>
<input type="hidden" name="<%=AuthCommandConstants.PARAM_BACK_URL%>" value="<c:out value="<%=orgUrl%>"/>" />
<%}%>

${m:outputToken('FORM_XHTML', true)}
</form>
<%@include file="./webAuthn.inc.jsp"%>
</div><!-- main -->
</div><!-- content -->

<%
TemplateUtil.includeTemplate("gem/layout/footer", pageContext);
%>
</div><!-- container -->
</body>
</html>
