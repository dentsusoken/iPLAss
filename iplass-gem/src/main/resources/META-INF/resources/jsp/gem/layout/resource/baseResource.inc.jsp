<%--
 Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.

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

<script src="${staticContentPath}/webjars/free-jqgrid/4.15.5/dist/jquery.jqgrid.min.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/free-jqgrid/4.15.5/dist/i18n/min/grid.locale-${language}.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jqtree/1.4.9/tree.jquery.js?cv=${apiVersion}"></script>

<link rel="stylesheet" href="${staticContentPath}/webjars/free-jqgrid/4.15.5/dist/css/ui.jqgrid.min.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/webjars/free-jqgrid/4.15.5/dist/plugins/css/ui.multiselect.min.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/webjars/jqtree/1.4.9/jqtree.css?cv=${apiVersion}" />

<%@include file="./viewerjsResource.inc.jsp"%>