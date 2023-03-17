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
<%@ page import="org.iplass.mtp.view.generic.editor.DecimalPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%
DecimalPropertyEditor editor = (DecimalPropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
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
	if (editor.getDisplayType() == NumberDisplayType.LABEL) {
%>

var newContent = '';

if (multiplicity == 1) {
	if (!value) {
		newContent = "" + '<input type="hidden" name="' + propName + '" value="">';
	} else {
		newContent = value.label + '<input type="hidden" name="' + propName + '" value="' + value.value + '">';
	}
	$("[name='data-label-" + propName + "']").html(newContent);
} else {
	var dataLabelEle = $("[name='data-label-" + propName + "'] li");
	for (var i = 0; i < value.length; i++) {
		if (dataLabelEle[i] != null) dataLabelEle[i].remove();
		if (!value[i]) {
			continue;
		}
		newContent = newContent  + '<li>' + value[i].label
			+ '<input type="hidden" name="' + propName + '" value="' + value[i].value + '"> </li>';
	}
	$(newContent).prependTo($("[name='data-label-" + propName + "']"));
}
<% 
	} else if(editor.getDisplayType() == NumberDisplayType.HIDDEN) {
%>

if (multiplicity == 1) {
	$('[name=' + propName + ']').val(value);
} else {
	var propLength = $('[name=' + propName + ']').length;
	var newContent = '';
	for (i =  0; i < value.length; i++) {
		var hiddenValue = value[i] ? value[i] : "";
		if (i > propLength - 1) {
			newContent = newContent + '<input type="hidden" name="' + propName + '" value="' + hiddenValue + '">';
			continue;
		}
		$("[name='" + propName + "']:eq(" + i + ")").val(hiddenValue);
	}

	// 項目数が増える場合に追加する
	if (propLength && value.length > propLength) {
		$("[name='" + propName + "']:eq(" + (propLength - 1) + ")").after($(newContent));
	// 項目に値が無い場合は新規に追加する
	} else if (!propLength) {
		$(".hidden-input-area:first").append($(newContent));
	}
}
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

		} else  {
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
if (value.length > 0) {
	$("[name='sc_" + propName + "']").val(value[0]);
}
<%
	if (editor.isSearchInRange()) {
%>
if (value.length > 1) {
	$("[name='sc_" + propName + "To']").val(value[1]);
}
<%
	}
}
%>