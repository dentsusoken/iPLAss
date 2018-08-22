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

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	String _language = TemplateUtil.getLanguage();
	if (StringUtil.isEmpty(_language)) {
		_language = "ja";
	}
	String languageFonts = TemplateUtil.getLanguageFonts(_language);

	String skinName = (String) request.getAttribute("skinName");
	List<String> cssPathList = ViewUtil.getCssPathList(skinName);
%>
<script>
scriptContext.skinName = "${skinName}";
</script>
<script src="${staticContentPath}/scripts/gem/skin/${skinName}/design.js?cv=${apiVersion}"></script>
<link rel="stylesheet" href="${staticContentPath}/styles/gem/skin/${skinName}/base.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/styles/gem/skin/${skinName}/structure.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/styles/gem/skin/${skinName}/module.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/styles/gem/skin/${skinName}/clear.css?cv=${apiVersion}" />
<%if (StringUtil.isNotBlank(languageFonts)) {%>
<script>
scriptContext.languageFonts = '<%=languageFonts%>';
</script>
<style>
body, input, textarea, .ui-widget,
.ui-dialog label, .ui-dialog span, .ui-dialog input, .ui-dialog textarea, .ui-dialog button, .ui-dialog select,
ul.context-menu-list li.context-menu-item span {
font-family: <%=languageFonts%>;
}
</style>
<%}%>
<%if ("en".equals(_language)) {%>
<style>
#header #user-nav .search-text {
height: 26px;
}
</style>
<%}%>
<%for (String cssPath : cssPathList) {%>
<link rel="stylesheet" href="${staticContentPath}<%=cssPath%>?cv=${apiVersion}" />
<%}%>
