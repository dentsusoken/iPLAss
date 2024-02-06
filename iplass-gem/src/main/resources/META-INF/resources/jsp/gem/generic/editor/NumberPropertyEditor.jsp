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

<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinitionType"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ExpressionProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.FloatProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.IntegerProperty"%>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.EntityViewRuntimeException"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%
	NumberPropertyEditor editor = (NumberPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);

	String propName = editor.getPropertyName();
	if (pd == null || !(pd instanceof FloatProperty || pd instanceof IntegerProperty)) {
		//定義がFloatPropertyかIntegerPropertyか、Expression(resultType=Float or Integer)でなければ表示不可
		if (pd instanceof ExpressionProperty) {
			ExpressionProperty ep = (ExpressionProperty) pd;
			if (ep.getResultType() != PropertyDefinitionType.FLOAT && ep.getResultType() != PropertyDefinitionType.INTEGER) {
				throw new EntityViewRuntimeException(propName + " 's editor is unsupport "
						+ (pd != null ? pd.getClass().getSimpleName() : "(unknown)") + " type." );
			}
		} else {
			throw new EntityViewRuntimeException(propName + " 's editor is unsupport "
					+ (pd != null ? pd.getClass().getSimpleName() : "(unknown)") + " type." );
		}
	}

	//タイプ毎に出力内容かえる
	if (OutputType.EDIT == type || OutputType.BULK == type) {
		//詳細編集 or 一括更新編集
%>
<jsp:include page="./number/NumberPropertyEditor_Edit.jsp"></jsp:include>
<jsp:include page="ErrorMessage.jsp">
	<jsp:param value="<%=propName %>" name="propName" />
</jsp:include>
<%
	} else if (OutputType.VIEW == type) {
		//詳細表示
%>
<jsp:include page="./number/NumberPropertyEditor_View.jsp"></jsp:include>
<%
	} else if (OutputType.SEARCHCONDITION == type) {
		//検索条件
%>
<jsp:include page="./number/NumberPropertyEditor_Condition.jsp"></jsp:include>
<%
	} else {
		//検索結果
%>
<jsp:include page="./number/NumberPropertyEditor_View.jsp"></jsp:include>
<%
	}

	request.removeAttribute(Constants.EDITOR_EDITOR);
	request.removeAttribute(Constants.EDITOR_PROP_VALUE);
	request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	request.removeAttribute(Constants.EDITOR_DISPLAY_LABEL);
%>
