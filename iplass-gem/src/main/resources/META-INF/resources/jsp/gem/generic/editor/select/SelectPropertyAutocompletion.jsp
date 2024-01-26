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
	//編集画面
%>
const multiplicity = <%=multiplicity%>;
const inputLength = $("[name='" + propName + "']").length;
value = normalizedDetailAutoCompletionValue(value, multiplicity, inputLength, "");
<%
	if (editor.getDisplayType() == SelectDisplayType.SELECT) {
%>
$("[name='" + propName + "']").val(value);
<%
	} else if (editor.getDisplayType() == SelectDisplayType.LABEL) {
		List<EditorValue> autocompletionEditorValues = (List<EditorValue>) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR_VALUES);
		//表示ラベルと値をセット
%>
var editorValueMap = new Map();
<c:forEach items="<%=autocompletionEditorValues%>" var="editorValue">
	editorValueMap.set('${editorValue.value}', {label:'${editorValue.label}', style:'${editorValue.style}'});
</c:forEach>
renderDetailAutoCompletionLabelTypeEditorValueFormat(value, multiplicity, propName, editorValueMap);
<%
	} else if(editor.getDisplayType() == SelectDisplayType.HIDDEN) {
		List<EditorValue> autocompletionEditorValues = (List<EditorValue>) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR_VALUES);
%>
var editorValueMap = new Map();
<c:forEach items="<%=autocompletionEditorValues%>" var="editorValue">
	editorValueMap.set('${editorValue.value}', {label:'${editorValue.label}', style:'${editorValue.style}'});
</c:forEach>
renderDetailAutoCompletionHiddenType(value, multiplicity, propName, 
	function(value) {
		if (value && editorValueMap.has(String(value))) {
			return value;
		}
		return "";
	}
);
<%
	} else {
		if (multiplicity == 1) {
			//radio
%>
// クリック状態を解除する
$("[name='" + propName + "']:checked").each(function() {
	$(this).click();
	return false;
});

$("[name='" + propName + "'][value='" + value + "']").click();
<%
		} else  {
			//checkbox
%>
// クリック状態を解除する
$("[name='" + propName + "']:checked").each(function() {
	$(this).click();
});

for (var i = 0; i < value.length; i++) {
	$("[name='" + propName + "'][value='" + value[i] + "']").click();
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
// クリック状態を解除する
$("[name='sc_" + propName + "']:checked").each(function() {
	$(this).click();
});

for (var i = 0; i < value.length; i++) {
	$("[name='sc_" + propName + "'][value='" + value[i] + "']").click();
}
<%
	} else if (editor.getDisplayType() == SelectDisplayType.RADIO) {
%>
// クリック状態を解除する
$("[name='sc_" + propName + "']:checked").each(function() {
	$(this).click();
	return false;
});

if (!value[0]) {
	// 未指定をクリック
	$("[name='sc_" + propName + "']:last").click();
} else {
	$("[name='sc_" + propName + "'][value='" + value[0] + "']").click();
}
<%
	} else if (editor.getDisplayType() == SelectDisplayType.SELECT) {
%>
$("[name='sc_" + propName + "']").val(value[0]);
<%
	}
}
%>