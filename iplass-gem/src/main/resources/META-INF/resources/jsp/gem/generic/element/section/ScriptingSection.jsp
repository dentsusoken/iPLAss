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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.*" %>
<%@ page import="org.iplass.mtp.view.generic.element.section.*" %>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.Constants" %>

<%
	EntityDefinition ed = (EntityDefinition) request.getAttribute(Constants.ENTITY_DEFINITION);
	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	ScriptingSection section = (ScriptingSection) element;
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	if ((type == OutputType.EDIT && section.isHideDetail())
			|| (type == OutputType.VIEW && section.isHideView())) return;

	String id = "";
	if (section.getId() != null) id = section.getId();
%>
<div id="<c:out value="<%=id %>"/>">
<%
	EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	evm.executeTemplate(ed.getName(), section.getKey(), request, response, application, pageContext);
%>
</div>
