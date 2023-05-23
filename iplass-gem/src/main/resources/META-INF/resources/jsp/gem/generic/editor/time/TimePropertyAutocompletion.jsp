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
<%@ page import="org.iplass.mtp.view.generic.editor.TimePropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
TimePropertyEditor editor = (TimePropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
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
	} else if(editor.getDisplayType() == DateTimeDisplayType.HIDDEN) {
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
	} else {
%>

<%
		if (editor.isUseTimePicker()) {
			if (multiplicity == 1) {
%>
var $time = $("#time_" + propName);
var timeFormat = $time.attr("data-timeformat");
var formatString = value.length > 0 ? dateUtil.newFormatString(value, dateUtil.getServerTimeFormat(), timeFormat) : "";

$time.val(formatString);
$("#i_" + propName).val(value);
<%
			} else  {
			//フィールドあるか、戻り値のサイズ、クリックして追加
%>
for (var i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	var $time = $("#time_" + propName + i);
	var timeFormat = $time.attr("data-timeformat");
	var formatString = value[i].length > 0 ? dateUtil.newFormatString(value[i], dateUtil.getServerTimeFormat(), timeFormat) : "";
	$time.val(formatString);
	$("#i_" + propName + i).val(value[i]);
}
<%
			}
		} else {
			if (multiplicity == 1) {
%>
$("#h_" + propName).val(dateUtil.newFormatString(value, dateUtil.getServerTimeFormat(), dateUtil.getInputHourFormat()));
$("#m_" + propName).val(dateUtil.newFormatString(value, dateUtil.getServerTimeFormat(), dateUtil.getInputMinFormat()));
$("#s_" + propName).val(dateUtil.newFormatString(value, dateUtil.getServerTimeFormat(), dateUtil.getInputSecFormat()));
$("#i_" + propName).val(value);
<%
			} else {
			//フィールドあるか、戻り値のサイズ、クリックして追加
%>
for (var i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	$("#h_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerTimeFormat(), dateUtil.getInputHourFormat()));
	$("#m_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerTimeFormat(), dateUtil.getInputMinFormat()));
	$("#s_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerTimeFormat(), dateUtil.getInputSecFormat()));
	$("#i_" + propName + i).val(value[i]);
}
<%
			}
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
	if (editor.isUseTimePicker()) {
		// datetimepicker
		if (!hideFrom) {
%>
if (value.length > 0) {
	var $timeFrom = $("#time_" + propName + "0");
	var timeFormatFrom = $timeFrom.attr("data-timeformat");

	var formatTimeFromString = value[0].length > 0 ? dateUtil.newFormatString(value[0], dateUtil.getServerTimeFormat(), timeFormatFrom) : "";
	$timeFrom.val(formatTimeFromString);
	$("#i_" + propName + "0").val(value[0]);
}
<%
		}
		if (!hideTo) {
%>
if (value.length > 1) {
	var $timeTo = $("#time_" + propName + "1");
	var timeFormatTo = $timeTo.attr("data-timeformat");

	var formatTimeToString = value[1].length > 0 ? dateUtil.newFormatString(value[1], dateUtil.getServerTimeFormat(), timeFormatTo) : "";
	$timeTo.val(formatTimeToString);
	$("#i_" + propName + "1").val(value[1]);
}
<%
		}
	} else {
		// pulldown
		if (!hideFrom) {
%>
if (value.length > 0) {
	$("#h_" + propName + "0").val(dateUtil.newFormatString(value[0], dateUtil.getServerTimeFormat(), dateUtil.getInputHourFormat()));
	$("#m_" + propName + "0").val(dateUtil.newFormatString(value[0], dateUtil.getServerTimeFormat(), dateUtil.getInputMinFormat()));
	$("#s_" + propName + "0").val(dateUtil.newFormatString(value[0], dateUtil.getServerTimeFormat(), dateUtil.getInputSecFormat()));
	$("#i_" + propName + "0").val(value[0]);
}
<%
		}
		if (!hideTo) {
%>
if (value.length > 1) {
	$("#h_" + propName + "1").val(dateUtil.newFormatString(value[1], dateUtil.getServerTimeFormat(), dateUtil.getInputHourFormat()));
	$("#m_" + propName + "1").val(dateUtil.newFormatString(value[1], dateUtil.getServerTimeFormat(), dateUtil.getInputMinFormat()));
	$("#s_" + propName + "1").val(dateUtil.newFormatString(value[1], dateUtil.getServerTimeFormat(), dateUtil.getInputSecFormat()));
	$("#i_" + propName + "1").val(value[1]);
}
<%
		}
	}
}
%>