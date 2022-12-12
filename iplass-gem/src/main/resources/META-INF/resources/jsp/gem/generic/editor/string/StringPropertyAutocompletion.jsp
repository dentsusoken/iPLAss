<%--
 Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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
	// 詳細画面
%>
var multiplicity = <%=multiplicity%>;
if (multiplicity == 1) {
	if (value instanceof Array) {
		value = value.length > 0 ? value[0] : "";
	}
} else {
	if (value instanceof Array) {
		if (value.length > multiplicity) {
			value = value.slice(0, multiplicity);
		}
	} else {
		value = [value];
	}
}

<%
	if (editor.getDisplayType() == StringDisplayType.TEXT
		|| editor.getDisplayType() == StringDisplayType.TEXTAREA
		|| editor.getDisplayType() == StringDisplayType.PASSWORD
		|| editor.getDisplayType() == StringDisplayType.HIDDEN
		|| editor.getDisplayType() == StringDisplayType.LABEL) {
		if (multiplicity == 1) {
			// ラベル表示の場合はラベルに値を設定
			if (editor.getDisplayType() == StringDisplayType.LABEL) {
%>
var labelValue = document.getElementsByName("data-label-" + propName).item(0);
labelValue.innerHTML = value.replaceAll('\r\n', '<BR>').replaceAll('\n', '<BR>').replaceAll('\r', '<BR>').replaceAll(' ', '&nbsp;');
<%
			}
%>
$("[name='" + propName + "']").val(value);
<%
		} else  {
			
			// ラベル表示の場合はhtml書き換え
			if (editor.getDisplayType() == StringDisplayType.LABEL) {
%>
var newContent = '';
for (i =  0; i < value.length; i++) {
newContent = newContent + '<li>' + value[i].replaceAll('\r\n', '<BR>').replaceAll('\n', '<BR>').replaceAll('\r', '<BR>').replaceAll(' ', '&nbsp;')
			+ '<input type="hidden" name="' + propName + '" value="' + value[i] + '"> </li>';
}
document.getElementsByName("data-label-" + propName).item(0).innerHTML = newContent;
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
		}
	} else if (editor.getDisplayType() == StringDisplayType.SELECT) {
%>
$("[name='" + propName + "']").val(value);
<%
	} else if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
		if (multiplicity == 1) {
%>
CKEDITOR.instances[propName].setData(value);
<%
		} else  {
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