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
<%@ page import="org.iplass.mtp.view.generic.editor.BooleanPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.BooleanPropertyEditor.BooleanDisplayType"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
BooleanPropertyEditor editor = (BooleanPropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
String viewType = StringUtil.escapeJavaScript((String) request.getAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE));
Integer multiplicity = (Integer) request.getAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
if (multiplicity == null) multiplicity = 1;
//呼び出し元は/common/Autocompletion.jsp、以降はWebApiの結果を反映する部分のJavascript、結果の変数はvalue
if (Constants.VIEW_TYPE_DETAIL.equals(viewType)) {
	//詳細画面
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
	if (editor.getDisplayType() == BooleanDisplayType.SELECT) {
%>
$("[name='" + propName + "']").val(value);
<%
	} else if (editor.getDisplayType() == BooleanDisplayType.RADIO) {
		if (multiplicity == 1) {
%>
$("[name='" + propName + "'][value='" + value + "']").click();
<%
		} else  {
%>
for (var i = 0; i < value.length; i++) {
	$("[name='" + propName + i + "'][value='" + value[i] + "']").click();
}
<%
		}
	} else if (editor.getDisplayType() == BooleanDisplayType.CHECKBOX) {
		if (multiplicity == 1) {
%>
var isChecked = $("[name='" + propName + "']").is(":checked");
if (value === true || value === "true") {
	if (!isChecked) {
		$("[name='" + propName + "']").click();
	}
} else {
	if (isChecked) {
		$("[name='" + propName + "']").click();
	}
}

<%
		} else {
%>
for (var i = 0; i < value.length; i++) {
	var isChecked = $("[name='" + propName + i + "']").is(":checked");
	if (value[i] === true || value[i] === "true") {
		if (!isChecked) {
			$("[name='" + propName + i + "']").click();
		}
	} else {
		if (isChecked) {
			$("[name='" + propName + i + "']").click();
		}
	}
}
<%
		}
	} else if (editor.getDisplayType() == BooleanDisplayType.LABEL) {
		String[] autocompletionBooleanLabel = (String[]) request.getAttribute("autocompletionBooleanLabel");
		String trueLabel = autocompletionBooleanLabel[0];
		String falseLabel = autocompletionBooleanLabel[1];
%>
var labelValue = document.getElementsByName("data-label-" + propName).item(0);
var newContent = '';

if (multiplicity == 1) {
	var booleanLabel = (value === true || value === "true") ? "<%=trueLabel %>" : "<%=falseLabel %>";
	newContent = booleanLabel + '<input type="hidden" name="' + propName + '" value="' + value + '">';
} else {

	for (i =  0; i < value.length; i++) {
		var booleanLabel = (value[i] === true || value[i] === "true") ? "<%=trueLabel %>" : "<%=falseLabel %>";
		newContent = newContent + '<li>' + booleanLabel + '</li>'
			+ '<input type="hidden" name="' + propName + '" value="' + value[i] + '">';
	}
}
document.getElementsByName("data-label-" + propName).item(0).innerHTML = newContent;

<%
	}
} else {
	//検索画面
%>
if (value instanceof Array) {
	value = value.length > 0 ? value[0] : "";
}
<%
	if (editor.getDisplayType() == BooleanDisplayType.SELECT) {
%>
$("[name='sc_" + propName + "']").val(value);
<%
	} else {
%>
$("[name='sc_" + propName + "'][value='" + value + "']").click();
<%
	}
}
%>