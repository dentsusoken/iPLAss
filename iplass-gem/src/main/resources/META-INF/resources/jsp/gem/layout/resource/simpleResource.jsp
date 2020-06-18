<%--
 Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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

<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%
	request.setAttribute("staticContentPath", TemplateUtil.getStaticContentPath());
	request.setAttribute("apiVersion", TemplateUtil.getAPIVersion());
	request.setAttribute("skinName", ViewUtil.getSkin().getPageSkinName());
	request.setAttribute("skinImagePath", ViewUtil.getSkinImagePath());
	request.setAttribute("themeName", ViewUtil.getTheme().getThemeName());
	request.setAttribute("themeImagePath", ViewUtil.getThemeImagePath());

	String language = TemplateUtil.getLanguage();
	if (StringUtil.isEmpty(language)) {
		language = "ja";
	}
	request.setAttribute("language", language);
%>
<script>
contentPath = "${staticContentPath}";
contextPath = "${m:tcPath()}";
sysdate = "<%=DateUtil.getSimpleDateFormat("yyyyMMddHHmmss", true).format(TemplateUtil.getCurrentTimestamp())%>";
scriptContext = {};
document.scriptContext = scriptContext;
scriptContext.gem = {};
scriptContext.gem.showPulldownPleaseSelectLabel = <%=ViewUtil.isShowPulldownPleaseSelectLabel()%>;
dType="/gem";
</script>

<%@include file="./locale.jsp" %>

<%-- For compatibility before 3.0.12  --%>
<script>
scriptContext.locale.showPulldownPleaseSelectLabel = <%=ViewUtil.isShowPulldownPleaseSelectLabel()%>;
</script>

<script src="${staticContentPath}/webjars/jquery/3.5.1/jquery.min.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jquery-ui/1.12.1/jquery-ui.min.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/functions.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/common.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/webapi.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/plugin/fixHeight.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/momentjs/2.18.1/min/moment-with-locales.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/font-awesome/5.0.9/svg-with-js/js/fontawesome-all.min.js?cv=${apiVersion}"></script>

<link rel="stylesheet" href="${staticContentPath}/webjars/jquery-ui/1.12.1/themes/base/jquery-ui.min.css?cv=${apiVersion}" />

<%@include file="./datepickerResource.inc.jsp" %> 
