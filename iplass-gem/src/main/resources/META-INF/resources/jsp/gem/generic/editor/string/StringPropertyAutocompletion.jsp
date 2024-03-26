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
<%@ page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
StringPropertyEditor editor = (StringPropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
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
	if (editor.getDisplayType() == StringDisplayType.TEXT
		|| editor.getDisplayType() == StringDisplayType.TEXTAREA
		|| editor.getDisplayType() == StringDisplayType.PASSWORD) {
		if (multiplicity == 1) {
%>
$("[name='" + propName + "']").val(value);
<%
		} else {
			//フィールドあるか、戻り値のサイズ、クリックして追加
%>
for (var i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	$("[name='" + propName + "']:eq(" + i + ")").val(value[i]);
}
<%
		}
	} else if (editor.getDisplayType() == StringDisplayType.LABEL) {
%>
renderDetailAutoCompletionLabelType(value, multiplicity, propName,
	function(value) {
		if (!value) {
			return ""
		}
		return value.replaceAll("\r\n", "<br>").replaceAll("\n", "<br>").replaceAll("\r", "<br>").replaceAll(" ", "&nbsp;");
	},
	function(value) {
		if (!value) {
			return "";
		}
		return value;
	}
);
<%
	} else if (editor.getDisplayType() == StringDisplayType.SELECT) {
%>
$("[name='" + propName + "']").val(value);
<%
	} else if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
		if (multiplicity == 1) {
%>
CKEDITOR.instances[propName].setData(value);
<%
		} else {
			//フィールドあるか、戻り値のサイズ、クリックして追加
%>
for (var i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	var id = $("[name='" + propName + "']:eq(" + i + ")").attr("id");
	CKEDITOR.instances[id].setData(value[i]);
}
<%
		}
	} else if (editor.getDisplayType() == StringDisplayType.HIDDEN) {
%>
renderDetailAutoCompletionHiddenType(value, multiplicity, propName);
<%
	}
} else {
	// 検索画面
%>
if (value instanceof Array) {
	if (value.length > 0) {
		value = value[0];
	} else {
		value = "";
	}
}
$("[name='sc_" + propName + "']").val(value);
<%
}
%>