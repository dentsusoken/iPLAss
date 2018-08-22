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
<%@ page import="org.iplass.gem.command.Constants"%>

<%
TimePropertyEditor editor = (TimePropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
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
	if (editor.isUseTimePicker()) {
		if (multiplicity == 1) {
%>
var $time = $("#time_" + propName);
var timeFormat = $time.attr("data-timeformat");

$time.val(dateUtil.newFormatString(value, dateUtil.getServerTimeFormat(), timeFormat));
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
	$time.val(dateUtil.newFormatString(value[i], dateUtil.getServerTimeFormat(), timeFormat));
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
$("#ms_" + propName).val(dateUtil.newFormatString(value, dateUtil.getServerTimeFormat(), "SSS"));
$("#i_" + propName).val(value);
<%
		} else  {
			//フィールドあるか、戻り値のサイズ、クリックして追加
%>
for (var i = 0; i < value.length; i++) {
	if ($("[name='" + propName + "']:eq(" + i + ")").length == 0) {
		$("#id_addBtn_" + propName).click();
	}
	$("#h_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerTimeFormat(), dateUtil.getInputHourFormat()));
	$("#m_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerTimeFormat(), dateUtil.getInputMinFormat()));
	$("#s_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerTimeFormat(), dateUtil.getInputSecFormat()));
	$("#ms_" + propName + i).val(dateUtil.newFormatString(value[i], dateUtil.getServerTimeFormat(), "SSS"));
	$("#i_" + propName + i).val(value[i]);
}
<%
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

	$timeFrom.val(dateUtil.newFormatString(value[0], dateUtil.getServerTimeFormat(), timeFormatFrom));
	$("#i_" + propName + "0").val(value[0]);
}
<%
		}
		if (!hideTo) {
%>
if (value.length > 1) {
	var $timeTo = $("#time_" + propName + "1");
	var timeFormatTo = $timeTo.attr("data-timeformat");

	$timeTo.val(dateUtil.newFormatString(value[1], dateUtil.getServerTimeFormat(), timeFormatTo));
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
	$("#ms_" + propName + "0").val(dateUtil.newFormatString(value[0], dateUtil.getServerTimeFormat(), "SSS"));
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
	$("#ms_" + propName + "1").val(dateUtil.newFormatString(value[1], dateUtil.getServerTimeFormat(), "SSS"));
	$("#i_" + propName + "1").val(value[1]);
}
<%
		}
	}
}
%>