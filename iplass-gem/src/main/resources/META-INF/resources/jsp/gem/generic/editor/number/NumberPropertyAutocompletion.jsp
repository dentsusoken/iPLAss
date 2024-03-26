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

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
NumberPropertyEditor editor = (NumberPropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
String viewType = StringUtil.escapeJavaScript((String) request.getAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE));
Integer multiplicity = (Integer) request.getAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
if (multiplicity == null) multiplicity = 1;
//呼び出し元は/common/Autocompletion.jsp、以降はWebApiの結果を反映する部分のJavascript、結果の変数はvalue
if (Constants.VIEW_TYPE_DETAIL.equals(viewType)) {
	//編集画面
%>
const multiplicity = <%=multiplicity%>;
const inputLength = $("[name='" + propName + "']").length;
value = normalizedDetailAutoCompletionValue(value, multiplicity, inputLength, null);
<%
	if (editor.getDisplayType() == NumberDisplayType.LABEL) {
%>
renderDetailAutoCompletionLabelTypeLabelFormat(value, multiplicity, propName);
<%
	} else if(editor.getDisplayType() == NumberDisplayType.HIDDEN) {
%>
renderDetailAutoCompletionHiddenType(value, multiplicity, propName);
<% 
	} else if (editor.getDisplayType() == NumberDisplayType.TEXT) {

		if (multiplicity == 1) {
%>
$("[name='" + propName + "']").val(value);
<%
			if (editor.isShowComma()) {
%>
$("[name='" + propName + "']").prev().trigger("focus");
<%
			}
		} else {
			//フィールドあるか、戻り値のサイズ、クリックして追加
%>
for (var i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	$("[name='" + propName + "']:eq(" + i + ")").val(value[i]);
<%
			if (editor.isShowComma()) {
%>
	$("[name='" + propName + "']:eq(" + i + ")").prev().trigger("focus");
<%
			}
%>
}
<%
		}
	}
} else {
	// 検索画面
%>
if (value instanceof Array) {
	if (value.length > 2) {
		value = value.slice(0, 2);
	}
} else {
	value = [value];
}
<%
	if (!editor.isSearchInRange()) {
		// 単一検索
%>
if (value.length > 0) {
	$("[name='sc_" + propName + "']").val(value[0]);
}
<%
	} else {
		// 範囲検索
		if (!editor.isHideSearchConditionFrom()) {
%>
if (value.length > 0) {
	$("[name='sc_" + propName + "From']").val(value[0]);
}
<%
		}
		if (!editor.isHideSearchConditionTo()) {
%>
if (value.length > 1) {
	$("[name='sc_" + propName + "To']").val(value[1]);
}
<%
		}
	}
}
%>