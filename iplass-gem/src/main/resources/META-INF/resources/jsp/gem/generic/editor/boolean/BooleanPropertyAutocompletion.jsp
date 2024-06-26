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
	//編集画面
	//Booleanは入力領域の数ではなくて、多重度で埋める
%>
const multiplicity = <%=multiplicity%>;
value = normalizedDetailAutoCompletionValue(value, multiplicity, multiplicity, null);
<%
	if (editor.getDisplayType() == BooleanDisplayType.SELECT) {
		// booleanやnullをそのままセットすると選択されない（stringなら平気）ため、配列化して対応
%>
if (multiplicity == 1) {
	value = [value];
}
$("[name='" + propName + "']").val(value);
<%
	} else if (editor.getDisplayType() == BooleanDisplayType.RADIO) {
		if (multiplicity == 1) {
			// クリック状態を解除して、選択
%>
$("[name='" + propName + "']:checked").each(function() {
	$(this).click();
});
$("[name='" + propName + "'][value='" + value + "']").click();
<%
		} else {
			// クリック状態を解除して、選択
%>
for (let i = 0; i < value.length; i++) {
	$("[name='" + propName + i + "']:checked").each(function() {
		$(this).click();
		return false;
	});
	$("[name='" + propName + i + "'][value='" + value[i] + "']").click();
}
<%
		}
	} else if (editor.getDisplayType() == BooleanDisplayType.CHECKBOX) {
		if (multiplicity == 1) {
			// クリック状態を解除して、trueの場合のみ選択
%>
$("[name='" + propName + "']:checked").each(function() {
	$(this).click();
});
if (value === true || value === "true") {
	$("[name='" + propName + "'][value='" + value + "']").click();
}
<%
		} else {
			// クリック状態を解除して、trueの場合のみ選択
%>
for (let i = 0; i < value.length; i++) {
	$("[name='" + propName + i + "']:checked").each(function() {
		$(this).click();
	});
	if (value[i] === true || value[i] === "true") {
		$("[name='" + propName + i + "'][value='" + value[i] + "']").click();
	}
}
<%
		}
	} else if (editor.getDisplayType() == BooleanDisplayType.LABEL) {
		String[] autocompletionBooleanLabel = (String[]) request.getAttribute("autocompletionBooleanLabel");
		String trueLabel = autocompletionBooleanLabel[0];
		String falseLabel = autocompletionBooleanLabel[1];
%>
renderDetailAutoCompletionLabelType(value, multiplicity, propName,
	function(value) {
		if (!((value === true || value === "true") || (value === false || value === "false"))) {
			return ""
		}
		return (value === true || value === "true") ? "<%=trueLabel %>" : "<%=falseLabel %>";
	},
	function(value) {
		if (!((value === true || value === "true") || (value === false || value === "false"))) {
			return "";
		}
		return value;
	}
);
<%
	} else if (editor.getDisplayType() == BooleanDisplayType.HIDDEN) {
%>
renderDetailAutoCompletionHiddenType(value, multiplicity, propName, 
	function(value) {
		if (!((value === true || value === "true") || (value === false || value === "false"))) {
			return "";
		}
		return value;
	}
);
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
		//booleanをそのままセットすると選択されない、かつnullも対応のため変換する
%>
value = [value];
$("[name='sc_" + propName + "']").val(value);
<%
	} else {
%>
if ((value === true || value === "true")
	|| (value === false || value === "false")) {
	$("[name='sc_" + propName + "'][value='" + value + "']").click();
} else {
	$("[name='sc_" + propName + "']:last").click();
}
<%
	}
}
%>