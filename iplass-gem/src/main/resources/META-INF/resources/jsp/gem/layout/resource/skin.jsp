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
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	String skinLanguage = (String) request.getAttribute("language");
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
<%if ("en".equals(skinLanguage)) {%>
<style>
#header #user-nav .search-text {
height: 26px;
}
</style>
<%}%>
<%for (String cssPath : cssPathList) {%>
<link rel="stylesheet" href="${staticContentPath}<%=cssPath%>?cv=${apiVersion}" />
<%}%>
