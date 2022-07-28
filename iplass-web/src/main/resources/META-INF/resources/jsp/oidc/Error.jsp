<%--
 Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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
<%@page import="org.iplass.mtp.web.template.ELFunctions"%>
<%@page import="org.iplass.mtp.ApplicationException"%>
<%@page import="org.iplass.mtp.impl.core.ExecuteContext"%>
<%@page import="org.iplass.mtp.tenant.Tenant"%>
<%@page import="org.iplass.mtp.tenant.web.TenantWebInfo"%>
<%@page import="org.iplass.mtp.web.WebRequestConstants"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%@page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@page import="org.iplass.mtp.command.RequestContext"%>
<%@page import="org.iplass.mtp.impl.web.WebResourceBundleUtil"%>
<%@page import="org.iplass.mtp.impl.web.WebUtil"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge" />
<title>OpenID Connect failed.</title>
</head>
<body>
<%
RequestContext context = TemplateUtil.getRequestContext();
Exception e = (Exception) context.getAttribute(WebRequestConstants.EXCEPTION);
String errorMessage = null;
if (e != null && e instanceof ApplicationException) {
	errorMessage = e.getMessage();
} else {
	errorMessage = WebResourceBundleUtil.resourceString("oidc.Error.failed");
}
Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
String home = null;
String menuUrl = WebUtil.getTenantWebInfo(tenant).getHomeUrl();
if (menuUrl != null && menuUrl.length() != 0) {
	home = TemplateUtil.getTenantContextPath() + menuUrl;
} else {
	home = TemplateUtil.getTenantContextPath() + "/";
}
%>
<h1>OpenID Connect failed</h1>
<p><%=ELFunctions.esc(errorMessage)%><p>
<p><a href="<%=home%>">HOME</a></p>
</body>
</html>