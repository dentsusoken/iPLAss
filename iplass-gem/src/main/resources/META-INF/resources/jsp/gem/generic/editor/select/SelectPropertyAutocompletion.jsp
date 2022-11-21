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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.iplass.mtp.entity.SelectValue" %>
<%@ page import="org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition" %>
<%@ page import="org.iplass.mtp.view.generic.editor.EditorValue" %>

<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
SelectPropertyEditor editor = (SelectPropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
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

var labelValue = document.getElementsByName("data-label-" + propName).item(0);

<%
	if (editor.getDisplayType() == SelectDisplayType.SELECT) {
%>
$("[name='" + propName + "']").val(value);
<%
	// ラベル表示の場合はhtmlを書き換え
	} else if (editor.getDisplayType() == SelectDisplayType.LABEL) {
		List<EditorValue> autocompletionEditorValues = (List<EditorValue>) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR_VALUES);
		Map<String, EditorValue> editorMap = new HashMap<>();
		for (int i = 0; i < autocompletionEditorValues.size(); i++) {
			String[] editorValue = {autocompletionEditorValues.get(i).getLabel(), autocompletionEditorValues.get(i).getStyle()};
			editorMap.put(autocompletionEditorValues.get(i).getValue(), autocompletionEditorValues.get(i)); 
		}

%>
var editorMap = new Map();
<c:forEach items="<%=editorMap%>" var="editorValue" varStatus="loop">
var tmp = {};
tmp['label'] = '${editorValue.value.label}';
tmp['style'] = '${editorValue.value.style}';
editorMap.set('${editorValue.key}', tmp);
</c:forEach>
var newContent = '';

if (multiplicity == 1) {
	newContent = '<li class="' + editorMap.get(String(value)).style + '">' + editorMap.get(String(value)).label 
		+ '<input type="hidden" name="' + propName + '" value="' + value + '"> </li>';
	
} else {
	for (i =  0; i < value.length; i++) {
		newContent = newContent + '<li class="' + editorMap.get(String(value[i])).style + '">' + editorMap.get(String(value[i])).label
			+ '<input type="hidden" name="' + propName + '" value="' + value[i] + '"> </li>';
	}
}
document.getElementsByName("data-label-" + propName).item(0).innerHTML = newContent;

<%

	} else {
		if (multiplicity == 1) {
			//radio
%>
$("[name='" + propName + "'][value='" + value + "']").click();
<%
		} else  {
			//checkbox
%>
for (var i = 0; i < value.length; i++) {
	var isChecked = $("[name='" + propName + "'][value='" + value[i] + "']").is(":checked");
	if (!isChecked) {
		$("[name='" + propName + "'][value='" + value[i] + "']").click();
	}
}
<%
		}
	}
} else {
	// 検索画面
%>
if (!(value instanceof Array)) {
	value = [value];
}
<%
	if (editor.getDisplayType() == SelectDisplayType.CHECKBOX) {
%>
for (var i = 0; i < value.length; i++) {
	var isChecked = $("[name='sc_" + propName + "'][value='" + value[i] + "']").is(":checked");
	if (!isChecked) {
		$("[name='sc_" + propName + "'][value='" + value[i] + "']").click();
	}
}
<%
	} else if (editor.getDisplayType() == SelectDisplayType.RADIO) {
%>
$("[name='sc_" + propName + "'][value='" + value[0] + "']").click();
<%
	} else if (editor.getDisplayType() == SelectDisplayType.SELECT) {
%>
$("[name='sc_" + propName + "']").val(value[0]);
<%
	}
}
%>