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
		if (value != null) {
			value = [value];
		}
	}

	if (value != null) {
		var propLength = $("[name='" + propName + "']").length;
		if (value.length < propLength) {
			value = value.concat(new Array(propLength - value.length));
		}
	} else {
		// nullの場合は1件のみクリアとする
		value = new Array(1);
	}
}

<%
	if (editor.getDisplayType() == SelectDisplayType.SELECT) {
%>
$("[name='" + propName + "']").val(value);
<%
	} else if (editor.getDisplayType() == SelectDisplayType.LABEL) {
		List<EditorValue> autocompletionEditorValues = (List<EditorValue>) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR_VALUES);
%>
// 表示ラベルと値をセット
var editorMap = new Map();
<c:forEach items="<%=autocompletionEditorValues%>" var="editorValue">
	editorMap.set('${editorValue.value}', {label:'${editorValue.label}', style:'${editorValue.style}'});
</c:forEach>

var newContent = '';

if (multiplicity == 1) {
	if (value) {
		newContent = '<li class="' + editorMap.get(String(value)).style + '">' + editorMap.get(String(value)).label 
			+ '<input type="hidden" name="' + propName + '" value="' + value + '"> </li>';
	}
} else {
	if (value) {
		for (i =  0; i < value.length; i++) {
			if (value[i] == null || value[i].length == 0) {
				continue;	
			}
			newContent = newContent + '<li class="' + editorMap.get(String(value[i])).style + '">' + editorMap.get(String(value[i])).label
				+ '<input type="hidden" name="' + propName + '" value="' + value[i] + '"> </li>';
		}
	}
}
$("[name='data-label-" + propName + "']").html(newContent);

<%
	} else if(editor.getDisplayType() == SelectDisplayType.HIDDEN) {
%>
if (multiplicity == 1) {
	// プロパティ値が無い場合はタグ追加
	if (!$('[name=' + propName + ']').length) {
		$(".hidden-input-area:first").append('<input type="hidden" name="' + propName + '" value="">');
	}
	$('[name=' + propName + ']').val(value);
	
} else {
	$('[name=' + propName + ']').remove();
	var newContent = '';
	for (i =  0; i < value.length; i++) {
		if (value[i]) {
			newContent = newContent + '<input type="hidden" name="' + propName + '" value="' + value[i] + '">';
		}
	}

	if (newContent.length > 0) {
		$(".hidden-input-area:first").append($(newContent));
	}
}
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

var clickValue = !value ? "" : value; 
$("[name='" + propName + "'][value='" + clickValue + "']").click();
<%
		} else  {
			//checkbox
%>
// クリック状態を解除する
$("[name='" + propName + "']:checked").each(function() {
	$(this).click();
});

for (var i = 0; i < value.length; i++) {
	var clickValue = !value[i] ? "" : value[i]; 
	$("[name='" + propName + "'][value='" + clickValue + "']").click();
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
	var clickValue = !value[i] ? "" : value[i]; 
	$("[name='sc_" + propName + "'][value='" + clickValue + "']").click();
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