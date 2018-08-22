<%--
 Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinition"%>
<%@ page import="org.iplass.mtp.view.top.TopViewDefinitionManager"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<div id="snav">
<m:include template="gem/menu/menu" />
<%
	String roleName = (String) request.getAttribute(Constants.ROLE_NAME);
	if (roleName == null || roleName.isEmpty()) roleName = "DEFAULT";

	TopViewDefinitionManager tm = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
	TopViewDefinition definition = tm.get(roleName);
	if (definition != null && definition.getWidgets().size() > 0) {
%>
<div id="widget" class="tab-panel">
<%
		tm.loadWidgets(request, response, application, pageContext);
%>
</div>
<%
	}
%>
</div><!--snav-->
