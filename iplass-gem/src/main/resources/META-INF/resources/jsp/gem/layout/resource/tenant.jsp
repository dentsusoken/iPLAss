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

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%@ page import="org.iplass.mtp.tenant.Tenant"%>
<%@ page import="org.iplass.mtp.tenant.gem.TenantGemInfo"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>

<%
	Tenant tenant = TemplateUtil.getTenant();
	TenantGemInfo gemInfo = ViewUtil.getTenantGemInfo(tenant);
	String javascriptFilePath = gemInfo.getJavascriptFilePath();
	if (StringUtil.isNotEmpty(javascriptFilePath)) {
%>
<script src="<%=TemplateUtil.getResourceContentPath(javascriptFilePath)%>"></script>
<%
	}
	String stylesheetFilePath = gemInfo.getStylesheetFilePath();
	if (StringUtil.isNotEmpty(stylesheetFilePath)) {
%>
<link rel="stylesheet" href="<%=TemplateUtil.getResourceContentPath(stylesheetFilePath)%>" />
<%
	}

	String iconUrl = gemInfo.getIconUrl();
	if (StringUtil.isNotEmpty(iconUrl)) {
		String type = "";
		if (iconUrl.endsWith(".png")) {
			type = "image/png";
		} else if (iconUrl.endsWith(".gif")) {
			type = "image/gif";
		}
%>
<link rel="icon" href="<%=TemplateUtil.getResourceContentPath(iconUrl)%>" type="<%=type%>" />
<%
	}
%>
