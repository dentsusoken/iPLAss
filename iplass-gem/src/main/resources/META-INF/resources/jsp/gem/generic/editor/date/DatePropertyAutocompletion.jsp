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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DatePropertyEditor" %>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>

<%
DatePropertyEditor editor = (DatePropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
String viewType = StringUtil.escapeJavaScript((String) request.getAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE));
Integer multiplicity = (Integer) request.getAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
if (multiplicity == null) multiplicity = 1;
//呼び出し元は/common/Autocompletion.jsp、以降はWebApiの結果を反映する部分のJavascript、結果の変数はvalue
if (Constants.VIEW_TYPE_DETAIL.equals(viewType)) {
	//編集画面
%>
const multiplicity = <%=multiplicity%>;
const inputLength = $("[name='" + propName + "']").length;
value = normalizedDetailAutoCompletionValue(value, multiplicity, inputLength, "");
<%
	if (editor.getDisplayType() == DateTimeDisplayType.LABEL) {
%>
renderDetailAutoCompletionLabelTypeLabelFormat(value, multiplicity, propName);
<%
	} else if(editor.getDisplayType() == DateTimeDisplayType.HIDDEN) {
%>
renderDetailAutoCompletionHiddenType(value, multiplicity, propName);
<%
	} else {
		if (multiplicity == 1) {
%>
$("#d_" + propName).val(convertToLocaleDateString(value)).trigger("blur");
$("#i_" + propName).val(value);
<%
		} else  {
		//フィールドあるか、戻り値のサイズ、クリックして追加
%>
for (var i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	$("#d_" + propName + i).val(convertToLocaleDateString(value[i])).trigger("blur");
	$("#i_" + propName + i).val(value[i]);
}
<%
		}
	}
} else {
	//検索画面
%>
if (value instanceof Array) {
	if (value.length > 2) {
		value = value.slice(0, 2);
	}
} else {
	value = [value];
}

for (var i = 0; i < value.length; i++) {
	if (value[i] == null) value[i] = "";
}
<%
	boolean hideFrom = editor.isSingleDayCondition() ? false : editor.isHideSearchConditionFrom();
	boolean hideTo = editor.isSingleDayCondition() ? true : editor.isHideSearchConditionTo();

	if (!hideFrom) {
%>
if (value.length > 0) {
	$("#d_sc_" + propName + "From").val(convertToLocaleDateString(value[0])).trigger("blur");
	$("#i_sc_" + propName + "From").val(value[0]);
}
<%
	}
	if (!hideTo) {
%>
if (value.length > 1) {
	$("#d_sc_" + propName + "To").val(convertToLocaleDateString(value[1])).trigger("blur");
	$("#i_sc_" + propName + "To").val(value[1]);
}
<%
	}
}
%>