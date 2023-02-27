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
<%@ page import="org.iplass.mtp.view.generic.editor.TimestampPropertyEditor" %>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>

<%
TimestampPropertyEditor editor = (TimestampPropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
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
	if (value == null) value = "";
} else {
	if (value instanceof Array) {
		if (value.length > multiplicity) {
			value = value.slice(0, multiplicity);
		}
	} else {
		value = [value];
	}

	// 空配列の場合、全件クリアとするため設定
	if (value.length == 0) {
		value = new Array($("[name='" + propName + "']").length);
	}

	for (var i = 0; i < value.length; i++) {
		if (value[i] == null) value[i] = "";
	}
}
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
		hiddenValue = value[i] ? value[i] : "";
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
		if (editor.isUseDatetimePicker()) {
			// datetimepicker
			if (multiplicity == 1) {
%>
var $datetime = $("#datetime_" + propName);
var timeFormat = $datetime.attr("data-timeformat");
var range = "NONE";
if (timeFormat == "HH:mm:ss") {
	range = "SEC";
} else if (timeFormat == "HH:mm") {
	range = "MIN";
} else if (timeFormat == "HH") {
	range = "HOUR";
}

$datetime.val(convertToLocaleDatetimeString(value, dateUtil.getServerDatetimeFormat(), range)).trigger("blur");
$("#i_" + propName).val(value);
<%
			} else  {
				//フィールドあるか、戻り値のサイズ、クリックして追加
%>
for (var i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	var $datetime = $("#datetime_" + propName + i);
	var timeFormat = $datetime.attr("data-timeformat");
	var range = "NONE";
	if (timeFormat == "HH:mm:ss") {
		range = "SEC";
	} else if (timeFormat == "HH:mm") {
		range = "MIN";
	} else if (timeFormat == "HH") {
		range = "HOUR";
	}
	var formatDateString = value[i].length > 0 ? convertToLocaleDatetimeString(value[i], dateUtil.getServerDatetimeFormat(), range) : "";

	$datetime.val(convertToLocaleDatetimeString(value[i], dateUtil.getServerDatetimeFormat(), range)).trigger("blur");
	$("#i_" + propName + i).val(value[i]);
}
<%
			}
		} else {
			// datepicker + pulldown
			if (multiplicity == 1) {
%>
// pulldownではない項目はエラー回避のため定義
var formatDateString = value.length > 0 ? dateUtil.newFormatString(value, dateUtil.getServerDatetimeFormat(), dateUtil.getInputDateFormat()) : "";
var formatMsString = value.length > 0 ? dateUtil.newFormatString(value, dateUtil.getServerDatetimeFormat(), "SSS") : "";

$("#d_" + propName).val(formatDateString).trigger("blur");
$("#h_" + propName).val(dateUtil.newFormatString(value, dateUtil.getServerDatetimeFormat(), dateUtil.getInputHourFormat()));
$("#m_" + propName).val(dateUtil.newFormatString(value, dateUtil.getServerDatetimeFormat(), dateUtil.getInputMinFormat()));
$("#s_" + propName).val(dateUtil.newFormatString(value, dateUtil.getServerDatetimeFormat(), dateUtil.getInputSecFormat()));
$("#ms_" + propName).val(formatMsString);
$("#i_" + propName).val(value);
<%
			} else  {
			//フィールドあるか、戻り値のサイズ、クリックして追加
%>
for (var i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	// pulldownではない項目はエラー回避のため定義
	var formatDateString = value[i].length > 0 ? dateUtil.newFormatString(value[i], dateUtil.getServerDatetimeFormat(), dateUtil.getInputDateFormat()) : "";
	var formatMsString = value[i].length > 0 ? dateUtil.newFormatString(value[i], dateUtil.getServerDatetimeFormat(), "SSS") : "";

	$("#d_" + propName + i).val(formatDateString).trigger("blur");
	$("#h_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerDatetimeFormat(), dateUtil.getInputHourFormat()));
	$("#m_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerDatetimeFormat(), dateUtil.getInputMinFormat()));
	$("#s_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerDatetimeFormat(), dateUtil.getInputSecFormat()));
	$("#ms_" + propName + i).val(formatMsString);
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

	if (editor.isUseDatetimePicker()) {
		// datetimepicker
		if (!hideFrom) {
%>
if (value.length > 0) {
	var $datetimeFrom = $("#datetime_" + propName + "0");
	var timeFormatFrom = $datetimeFrom.attr("data-timeformat");
	var rangeFrom = "NONE";
	if (timeFormatFrom == "HH:mm:ss") {
		rangeFrom = "SEC";
	} else if (timeFormatFrom == "HH:mm") {
		rangeFrom = "MIN";
	} else if (timeFormatFrom == "HH") {
		rangeFrom = "HOUR";
	}

	$datetimeFrom.val(convertToLocaleDatetimeString(value[0], dateUtil.getServerDatetimeFormat(), rangeFrom)).trigger("blur");
	$("#i_" + propName + "0").val(value[0]);
}
<%
		}
		if (!hideTo) {
%>
if (value.length > 1) {
	var $datetimeTo = $("#datetime_" + propName + "1");
	var timeFormatTo = $datetimeTo.attr("data-timeformat");
	var rangeTo = "NONE";
	if (timeFormatTo == "HH:mm:ss") {
		rangeTo = "SEC";
	} else if (timeFormatTo == "HH:mm") {
		rangeTo = "MIN";
	} else if (timeFormatTo == "HH") {
		rangeTo = "HOUR";
	}

	$datetimeTo.val(convertToLocaleDatetimeString(value[1], dateUtil.getServerDatetimeFormat(), rangeTo)).trigger("blur");
	$("#i_" + propName + "1").val(value[1]);
}
<%
		}
	} else {
		// datepicker + pulldown
		if (!hideFrom) {
%>
if (value.length > 0) {
	// pulldownではない項目はエラー回避のため定義
	var formatDateFromString = value[0].length > 0 ? dateUtil.newFormatString(value[0], dateUtil.getServerDatetimeFormat(), dateUtil.getInputDateFormat()) : "";
	var formatMsString = value[0].length > 0 ? dateUtil.newFormatString(value[0], dateUtil.getServerDatetimeFormat(), "SSS") : "";
	
	$("#d_" + propName + "0").val(formatDateFromString).trigger("blur");
	$("#h_" + propName + "0").val(dateUtil.newFormatString(value[0], dateUtil.getServerDatetimeFormat(), dateUtil.getInputHourFormat()));
	$("#m_" + propName + "0").val(dateUtil.newFormatString(value[0], dateUtil.getServerDatetimeFormat(), dateUtil.getInputMinFormat()));
	$("#s_" + propName + "0").val(dateUtil.newFormatString(value[0], dateUtil.getServerDatetimeFormat(), dateUtil.getInputSecFormat()));
	$("#ms_" + propName + "0").val(formatMsString);
	$("#i_" + propName + "0").val(value[0]);
}
<%
		}
		if (!hideTo) {
%>
if (value.length > 1) {
	// pulldownではない項目はエラー回避のため定義
	var formatDateToString = value[1].length > 0 ? dateUtil.newFormatString(value[1], dateUtil.getServerDatetimeFormat(), dateUtil.getInputDateFormat()) : "";
	var formatMsString = value[1].length > 0 ? dateUtil.newFormatString(value[1], dateUtil.getServerDatetimeFormat(), "SSS") : "";

	$("#d_" + propName + "1").val(formatDateToString).trigger("blur");
	$("#h_" + propName + "1").val(dateUtil.newFormatString(value[1], dateUtil.getServerDatetimeFormat(), dateUtil.getInputHourFormat()));
	$("#m_" + propName + "1").val(dateUtil.newFormatString(value[1], dateUtil.getServerDatetimeFormat(), dateUtil.getInputMinFormat()));
	$("#s_" + propName + "1").val(dateUtil.newFormatString(value[1], dateUtil.getServerDatetimeFormat(), dateUtil.getInputSecFormat()));
	$("#ms_" + propName + "1").val(formatMsString);
	$("#i_" + propName + "1").val(value[1]);
}
<%
		}
	}
}
%>