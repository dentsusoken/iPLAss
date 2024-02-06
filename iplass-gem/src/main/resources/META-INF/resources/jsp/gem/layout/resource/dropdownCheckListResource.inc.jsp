<%--
 Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.

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
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>

<script type="text/javascript" src="${m:esc(staticContentPath)}/scripts/gem/plugin/dropdownCheckList/js/ui.dropdownchecklist-1.4-min.js?cv=<%=TemplateUtil.getAPIVersion()%>"></script>
<link rel="stylesheet" type="text/css" media="screen, print" href="${m:esc(staticContentPath)}/scripts/gem/plugin/dropdownCheckList/css/ui.dropdownchecklist.standalone.css?cv=<%=TemplateUtil.getAPIVersion()%>" />
<link rel="stylesheet" type="text/css" media="screen, print" href="${m:esc(staticContentPath)}/scripts/gem/plugin/dropdownCheckList/css/ui.dropdownchecklist.themeroller.css?cv=<%=TemplateUtil.getAPIVersion()%>" />
