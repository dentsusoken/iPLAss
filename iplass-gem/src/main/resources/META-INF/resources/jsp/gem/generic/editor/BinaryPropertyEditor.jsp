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

<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.BinaryProperty"%>
<%@ page import="org.iplass.mtp.view.generic.editor.BinaryPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%
	BinaryPropertyEditor editor = (BinaryPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);

	if (pd == null || !(pd instanceof BinaryProperty)) {
		//定義がBinaryPropertyでなければ表示不可
		return;
	}

	String propName = editor.getPropertyName();

	//タイプ毎に出力内容かえる
	if (OutputType.EDIT == type) {
		//詳細編集
%>
<jsp:include page="./binary/BinaryPropertyEditor_Edit.jsp"></jsp:include>
<jsp:include page="ErrorMessage.jsp">
	<jsp:param value="<%=propName %>" name="propName" />
</jsp:include>
<%
	} else if (OutputType.VIEW == type) {
		//詳細表示
%>
<jsp:include page="./binary/BinaryPropertyEditor_View.jsp"></jsp:include>
<%
	} else if (OutputType.SEARCHCONDITION == type) {
		//検索条件
%>
<jsp:include page="./binary/BinaryPropertyEditor_Condition.jsp"></jsp:include>
<%
	} else if (OutputType.SEARCHRESULT == type) {
		//詳細表示
%>
<jsp:include page="./binary/BinaryPropertyEditor_View.jsp"></jsp:include>
<%
	}
	request.removeAttribute("editor");
	request.removeAttribute("propValue");
	request.removeAttribute("propertyDefinition");
%>
