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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.view.generic.editor.PropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%
	PropertyEditor editor = (PropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	String propName = editor.getPropertyName();

	if (OutputType.EDIT == type) {
		//詳細編集
%>
<jsp:include page="./entityselect/EntitySelectPropertyEditor_Edit.jsp"></jsp:include>
<jsp:include page="../editor/ErrorMessage.jsp">
	<jsp:param value="<%=propName %>" name="propName" />
</jsp:include>
<%
	} else if (OutputType.VIEW == type) {
		//詳細表示
%>
<jsp:include page="./entityselect/EntitySelectPropertyEditor_View.jsp"></jsp:include>
<%
	} else if (OutputType.SEARCHCONDITION == type) {
		//検索条件
%>
<jsp:include page="./entityselect/EntitySelectPropertyEditor_Condition.jsp"></jsp:include>
<%
	} else {
		//検索結果
%>
<jsp:include page="./entityselect/EntitySelectPropertyEditor_View.jsp"></jsp:include>
<%
	}

	request.removeAttribute(Constants.EDITOR_EDITOR);
	request.removeAttribute(Constants.EDITOR_PROP_VALUE);
	request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	request.removeAttribute(Constants.EDITOR_DISPLAY_LABEL);
%>
