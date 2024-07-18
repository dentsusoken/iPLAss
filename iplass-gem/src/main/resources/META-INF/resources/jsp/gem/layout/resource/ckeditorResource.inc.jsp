<%--
 Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
 
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

<script type="text/javascript" src="${m:esc(staticContentPath)}/webjars/ckeditor/4.22.1/full/ckeditor.js?cv=<%=TemplateUtil.getAPIVersion()%>"></script>
<script type="text/javascript" src="${m:esc(staticContentPath)}/webjars/ckeditor/4.22.1/full/adapters/jquery.js?cv=<%=TemplateUtil.getAPIVersion()%>"></script>
<script type="text/javascript">
CKEDITOR.config.customConfig = "${m:esc(staticContentPath)}/scripts/gem/plugin/ckeditor/mtpconfig.js?cv=<%=TemplateUtil.getAPIVersion()%>";
</script>
