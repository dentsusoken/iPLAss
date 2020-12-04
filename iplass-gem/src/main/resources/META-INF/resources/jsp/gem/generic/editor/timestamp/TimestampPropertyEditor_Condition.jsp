<%--
 Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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

<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.text.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.iplass.mtp.util.DateUtil"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TimestampPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!
	boolean formatCheck(String value) {
		if (value == null) return true;
		SimpleDateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateTimeFormat(), false);
		format.setLenient(false);
		try {
			format.parse(value);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	String getTimestampValue(Map<String, ArrayList<String>> searchCondMap, String key) {
		ArrayList<String> list = new ArrayList<String>();
		if (searchCondMap != null && searchCondMap.containsKey(key)) {
			list = searchCondMap.get(key);
		}
		return list.size() > 0 ? list.get(0) : null;
	}
%>
<%!
	String displayFormat(String time, TimeDispRange dispRange, boolean showWeekday) {
		if (time == null) {
			return "";
		}

		String timeFormat = "";
		if (TimeDispRange.isDispSec(dispRange)) {
			timeFormat = " " + TemplateUtil.getLocaleFormat().getOutputTimeSecFormat();
		} else if (TimeDispRange.isDispMin(dispRange)) {
			timeFormat = " " + TemplateUtil.getLocaleFormat().getOutputTimeMinFormat();
		} else if (TimeDispRange.isDispHour(dispRange)) {
			timeFormat = " " + TemplateUtil.getLocaleFormat().getOutputTimeHourFormat();
		}

		DateFormat format = null;
		if (showWeekday) {
			String dateFormat = TemplateUtil.getLocaleFormat().getOutputDateWeekdayFormat();
			//テナントのロケールと言語が違う場合、編集画面と曜日の表記が変わるため、LangLocaleを利用
			format = DateUtil.getSimpleDateFormat(dateFormat + timeFormat, true, true);
		} else {
			String dateFormat = TemplateUtil.getLocaleFormat().getOutputDateFormat();
			format = DateUtil.getSimpleDateFormat(dateFormat + timeFormat, true);
		}

		try {
			SimpleDateFormat serverFormat = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateTimeFormat(), false);
			return format.format(new Timestamp(serverFormat.parse(time).getTime()));
		} catch (ParseException e) {
			return "";
		}
	}
%>
<%
	TimestampPropertyEditor editor = (TimestampPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	Map<String, ArrayList<String>> searchCondMap = (Map<String, ArrayList<String>>)request.getAttribute(Constants.SEARCH_COND_MAP);
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	String propName = editor.getPropertyName();

	String propValueFrom = getTimestampValue(searchCondMap, Constants.SEARCH_COND_PREFIX + propName + "From");
	if (propValueFrom == null) {
		//初期値から復元(検索時に未指定の場合、ここにくる)
		if (propValue != null && propValue.length > 0 && formatCheck(propValue[0])) {
			propValueFrom = propValue[0];
		} else {
			propValueFrom = "";
		}
	}

	String propValueTo = getTimestampValue(searchCondMap, Constants.SEARCH_COND_PREFIX + propName + "To");
	if (propValueTo == null) {
		//初期値から復元(検索時に未指定の場合、ここにくる)
		if (propValue != null && propValue.length > 1 && formatCheck(propValue[1])) {
			propValueTo = propValue[1];
		} else {
			propValueTo = "";
		}
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/timestamp/TimestampPropertyAutocompletion.jsp");
	}

	boolean isUserDateTimePicker = editor.isUseDatetimePicker();
	if (TimeDispRange.NONE.equals(editor.getDispRange())) {
		isUserDateTimePicker = false;
	}

	if (editor.getDisplayType() != DateTimeDisplayType.HIDDEN) {
		//HIDDEN以外

		boolean hideFrom = editor.isSingleDayCondition() ? false : editor.isHideSearchConditionFrom();
		boolean hideTo = editor.isSingleDayCondition() ? true : editor.isHideSearchConditionTo();

		editor.setPropertyName(Constants.SEARCH_COND_PREFIX + propName + "From");
		request.setAttribute(Constants.EDITOR_PICKER_PROP_NAME, propName + "0");
		request.setAttribute(Constants.EDITOR_PICKER_PROP_VALUE, propValueFrom);

		String style = "";
		if (hideFrom) {
			style = "display: none;";
		}

		String defaultValueFrom = "";
		if (defaultValue != null && defaultValue.length > 0 && formatCheck(defaultValue[0])) {
			defaultValueFrom = defaultValue[0];
		}
		request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_VALUE, defaultValueFrom);

		String customStyle = "";
		if (editor.getDisplayType() == DateTimeDisplayType.LABEL) {
			//カスタムスタイル
			String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
			String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
			if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), null, null);
			}
			style = style + customStyle;
		}

		if ((editor.getDisplayType() != DateTimeDisplayType.LABEL) && (isUserDateTimePicker)) {
%>
<span class="timestamppicker-field" style="<c:out value="<%=style %>"/>">
<jsp:include page="TimestampTimepicker.jsp"></jsp:include>
</span>
<%
		} else if (editor.getDisplayType() == DateTimeDisplayType.LABEL) {
			String timeFromDisplayValue = displayFormat(propValueFrom, editor.getDispRange(), editor.isShowWeekday());
			String timeFromHiddenName = Constants.SEARCH_COND_PREFIX + propName + "From";
%>
<span class="timestampselect-field" style="<c:out value="<%=style %>"/>">
<c:out value="<%=timeFromDisplayValue %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=timeFromHiddenName %>"/>" value="<c:out value="<%=propValueFrom %>"/>" />
</span>
<%
		} else {
%>
<span class="timestampselect-field" style="<c:out value="<%=style %>"/>">
<jsp:include page="Timestamp.jsp"></jsp:include>
</span>
<%
		}

		if (!hideFrom && !hideTo) {
%>
&nbsp;～&nbsp;
<%
		}

		editor.setPropertyName(Constants.SEARCH_COND_PREFIX + propName + "To");
		request.setAttribute(Constants.EDITOR_PICKER_PROP_NAME, propName + "1");
		request.setAttribute(Constants.EDITOR_PICKER_PROP_VALUE, propValueTo);

		style = "";
		if (hideTo) {
			style = "display: none;";
		}
		if (editor.getDisplayType() == DateTimeDisplayType.LABEL) {
			style = style + customStyle;
		}

		String defaultValueTo = "";
		if (defaultValue != null && defaultValue.length > 1 && formatCheck(defaultValue[1])) {
			defaultValueTo = defaultValue[1];
		}
		request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_VALUE, defaultValueTo);

		request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_HOUR, "23");
		request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_MIN, "59");
		request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_SEC, "59");
		request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_MSEC, "999");

		if ((editor.getDisplayType() != DateTimeDisplayType.LABEL) && (isUserDateTimePicker)) {
%>
<span class="timestamppicker-field" style="<c:out value="<%=style %>"/>">
<jsp:include page="TimestampTimepicker.jsp"></jsp:include>
</span>
<%
		} else if (editor.getDisplayType() == DateTimeDisplayType.LABEL) {
			String timeToDisplayValue = displayFormat(propValueTo, editor.getDispRange(), editor.isShowWeekday());
			String timeToHiddenName = Constants.SEARCH_COND_PREFIX + propName + "To";
%>
<span class="timestampselect-field" style="<c:out value="<%=style %>"/>">
<c:out value="<%=timeToDisplayValue %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=timeToHiddenName %>"/>" value="<c:out value="<%=propValueTo %>"/>" />
</span>
<%
		} else {
%>
<span class="timestampselect-field" style="<c:out value="<%=style %>"/>">
<jsp:include page="Timestamp.jsp"></jsp:include>
</span>
<%
		}

		request.removeAttribute(Constants.EDITOR_PICKER_PROP_NAME);
		request.removeAttribute(Constants.EDITOR_PICKER_DEFAULT_HOUR);
		request.removeAttribute(Constants.EDITOR_PICKER_DEFAULT_MIN);
		request.removeAttribute(Constants.EDITOR_PICKER_DEFAULT_SEC);
		request.removeAttribute(Constants.EDITOR_PICKER_DEFAULT_MSEC);
		editor.setPropertyName(propName);

		if (required) {
%>
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalValidator(function() {
		var fromVal = $(":hidden[name='" + es("<%=StringUtil.escapeJavaScript(Constants.SEARCH_COND_PREFIX + propName + "From")%>") + "']").val();
		var toVal = $(":hidden[name='" + es("<%=StringUtil.escapeJavaScript(Constants.SEARCH_COND_PREFIX + propName + "To")%>") + "']").val();
		if ((typeof fromVal === "undefined" || fromVal == null || fromVal == "")
				&& (typeof toVal === "undefined" || toVal == null || toVal == "")) {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
});
</script>
<%
		}

	} else {
		//HIDDEN

		editor.setPropertyName(Constants.SEARCH_COND_PREFIX + propName + "From");
		request.setAttribute(Constants.EDITOR_PICKER_PROP_NAME, propName + "0");
		request.setAttribute(Constants.EDITOR_PICKER_PROP_VALUE, propValueFrom);
		if (isUserDateTimePicker) {
%>
<jsp:include page="TimestampTimepicker.jsp"></jsp:include>
<%
		} else {
%>
<jsp:include page="Timestamp.jsp"></jsp:include>
<%
		}

		editor.setPropertyName(Constants.SEARCH_COND_PREFIX + propName + "To");
		request.setAttribute(Constants.EDITOR_PICKER_PROP_NAME, propName + "1");
		request.setAttribute(Constants.EDITOR_PICKER_PROP_VALUE, propValueTo);
		if (isUserDateTimePicker) {
%>
<jsp:include page="TimestampTimepicker.jsp"></jsp:include>
<%
		} else {
%>
<jsp:include page="Timestamp.jsp"></jsp:include>
<%
		}

		request.removeAttribute(Constants.EDITOR_PICKER_PROP_NAME);
		request.removeAttribute(Constants.EDITOR_PICKER_PROP_VALUE);
		editor.setPropertyName(propName);
	}
%>
