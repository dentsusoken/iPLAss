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
<%@ page import="java.text.ParseException"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="org.iplass.mtp.util.DateUtil"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TimestampPropertyEditor" %>
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
	String getTimestampValue(String searchCond, String key) {
		if (searchCond != null && searchCond.indexOf(key) > -1) {
			String[] split = searchCond.split("&");
			if (split != null && split.length > 0) {
				for (String tmp : split) {
					String[] kv = tmp.split("=");
					if (kv != null && kv.length > 1 && key.equals(kv[0])) {
						return kv[1];
					}
				}
			}
		}
		return null;
	}
%>
<%
	TimestampPropertyEditor editor = (TimestampPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	String searchCond = request.getParameter(Constants.SEARCH_COND);
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	boolean hideFrom = editor.isSingleDayCondition() ? false : editor.isHideSearchConditionFrom();
	boolean hideTo = editor.isSingleDayCondition() ? true : editor.isHideSearchConditionTo();

	String propName = editor.getPropertyName();
	editor.setPropertyName(Constants.SEARCH_COND_PREFIX + propName + "From");
	request.setAttribute(Constants.EDITOR_PICKER_PROP_NAME, propName + "0");

	String style = "";
	if (hideFrom) {
		style = "display: none;";
	}

	String defaultValueFrom = "";
	if (defaultValue != null && defaultValue.length > 0 && formatCheck(defaultValue[0])) {
		defaultValueFrom = defaultValue[0];
	}

	String propValueFrom = getTimestampValue(searchCond, Constants.SEARCH_COND_PREFIX + propName + "From");
	if (propValueFrom == null) {
		//初期値から復元(検索時に未指定の場合、ここにくる)
		if (propValue != null && propValue.length > 0 && formatCheck(propValue[0])) {
			propValueFrom = propValue[0];
		} else {
			propValueFrom = "";
		}
	}
	request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_VALUE, defaultValueFrom);
	request.setAttribute(Constants.EDITOR_PICKER_PROP_VALUE, propValueFrom);

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/timestamp/TimestampPropertyAutocompletion.jsp");
	}

	boolean isUserDateTimePicker = editor.isUseDatetimePicker();

	if (TimeDispRange.NONE.equals(editor.getDispRange())) {
		isUserDateTimePicker = false;
	}

	if (isUserDateTimePicker) {
%>
<span class="timestamppicker-field" style="<c:out value="<%=style %>"/>">
<jsp:include page="TimestampTimepicker.jsp"></jsp:include>
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
	if (!hideFrom && !hideTo) {
%>
&nbsp;～&nbsp;
<%
	}

	editor.setPropertyName(Constants.SEARCH_COND_PREFIX + propName + "To");
	request.setAttribute(Constants.EDITOR_PICKER_PROP_NAME, propName + "1");

	style = "";
	if (hideTo) {
		style = "display: none;";
	}

	String defaultValueTo = "";
	if (defaultValue != null && defaultValue.length > 1 && formatCheck(defaultValue[1])) {
		defaultValueTo = defaultValue[1];
	}

	String propValueTo = getTimestampValue(searchCond, Constants.SEARCH_COND_PREFIX + propName + "To");
	if (propValueTo == null) {
		//初期値から復元(検索時に未指定の場合、ここにくる)
		if (propValue != null && propValue.length > 1 && formatCheck(propValue[1])) {
			propValueTo = propValue[1];
		} else {
			propValueTo = "";
		}
	}
	request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_VALUE, defaultValueTo);
	request.setAttribute(Constants.EDITOR_PICKER_PROP_VALUE, propValueTo);
	request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_HOUR, "23");
	request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_MIN, "59");
	request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_SEC, "59");
	request.setAttribute(Constants.EDITOR_PICKER_DEFAULT_MSEC, "999");

	if (isUserDateTimePicker) {
%>
<span class="timestamppicker-field" style="<c:out value="<%=style %>"/>">
<jsp:include page="TimestampTimepicker.jsp"></jsp:include>
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
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
});
</script>
<%
	}
%>
