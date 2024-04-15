<%--
 Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.

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
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.sql.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.Map" %>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.impl.core.ExecuteContext"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DatePropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimeFormatSetting"%>
<%@ page import="org.iplass.mtp.view.generic.editor.LocalizedDateTimeFormatSetting"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%!
	boolean formatCheck(String value) {
		if (value == null) return true;
// 		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateFormat(), false);
		format.setLenient(false);
		try {
			format.parse(value);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
%>
<%!
	String displayFormat(String date, String datetimeFormatPattern, String datetimeLocale, boolean showWeekday) {
		if (date == null) return "";

		try {
			if(datetimeFormatPattern != null){
				DateFormat format = ViewUtil.getDateTimeFormat(datetimeFormatPattern, datetimeLocale);
				return format.format(format);
			} else {
				SimpleDateFormat serverFormat = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateFormat(), false);
				if (showWeekday) {
					//テナントのロケールと言語が違う場合、編集画面と曜日の表記が変わるため、LangLocaleを利用
					SimpleDateFormat clientFormat = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateWeekdayFormat(), false, true);
					return clientFormat.format(new Date(serverFormat.parse(date).getTime()));
				} else {
					SimpleDateFormat clientFormat = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), false);
					return clientFormat.format(new Date(serverFormat.parse(date).getTime()));
				}
			}
		} catch (ParseException e) {
			return "";
		}
	}
%>
<%

	DatePropertyEditor editor = (DatePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	Map<String, List<String>> searchCondMap = (Map<String, List<String>>)request.getAttribute(Constants.SEARCH_COND_MAP);

	String propNameFrom = Constants.SEARCH_COND_PREFIX + editor.getPropertyName() + "From";
	//直接searchCondから取得(hidden対応)
	String propValueFrom = "";
	String[] propValueFromArray = ViewUtil.getSearchCondValue(searchCondMap, propNameFrom);
	if (propValueFromArray != null && propValueFromArray.length > 0) {
		propValueFrom = propValueFromArray[0];
	} else {
		//初期値から復元(検索時に未指定の場合、ここにくる)
		if (propValue != null && propValue.length > 0 && formatCheck(propValue[0])) {
			propValueFrom = propValue[0];
		} else {
			propValueFrom = "";
		}
	}

	String propNameTo = Constants.SEARCH_COND_PREFIX + editor.getPropertyName() + "To";
	String propValueTo = "";
	String[] propValueToArray = ViewUtil.getSearchCondValue(searchCondMap, propNameTo);
	if (propValueToArray != null && propValueToArray.length > 0) {
		propValueTo = propValueToArray[0];
	} else {
		//初期値から復元(検索時に未指定の場合、ここにくる)
		if (propValue != null && propValue.length > 1 && formatCheck(propValue[1])) {
			propValueTo = propValue[1];
		} else {
			propValueTo = "";
		}
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/date/DatePropertyAutocompletion.jsp");
	}
	if (editor.getDisplayType() != DateTimeDisplayType.HIDDEN) {
		//HIDDEN以外

		boolean hideFrom = editor.isSingleDayCondition() ? false : editor.isHideSearchConditionFrom();
		boolean hideTo = editor.isSingleDayCondition() ? true : editor.isHideSearchConditionTo();

		//カスタムスタイル
		String customStyle = "";
		if (editor.getDisplayType() != DateTimeDisplayType.LABEL) {
			if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
			}
		} else {
			if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), null, null);
			}
		}

		String defaultValueFrom = "";
		if (defaultValue != null && defaultValue.length > 0 && formatCheck(defaultValue[0])) {
			defaultValueFrom = defaultValue[0];
		}

		String onchange = "dateChange('" + StringUtil.escapeJavaScript(propNameFrom) + "')";

		String fromDisp = "";
		if (hideFrom) {
			fromDisp = "display: none;";
		}

		DateTimeFormatSetting formatInfo = ViewUtil.getFormatInfo(editor.getLocalizedDatetimeFormatList(), editor.getDatetimeFormat());

		if (editor.getDisplayType() == DateTimeDisplayType.LABEL) {
			String dateFromDisplayLabel = displayFormat(propValueFrom, formatInfo.getDatetimeFormat(), formatInfo.getDatetimeLocale(), editor.isShowWeekday());
			fromDisp = fromDisp + customStyle;
%>
<span style="<c:out value="<%=fromDisp %>"/>">
<c:out value="<%=dateFromDisplayLabel %>" />
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propNameFrom %>"/>" value="<c:out value="<%=propValueFrom %>"/>" />
</span>
<%
		} else {
%>
<span style="<c:out value="<%=fromDisp %>"/>">
<%-- XSS対応-メタの設定のため対応なし(onchange) --%>
<input type="text" id="d_<c:out value="<%=propNameFrom %>"/>" class="datepicker inpbr" style="<c:out value="<%=customStyle%>"/>" value="" onchange="<%=onchange %>" data-showButtonPanel="<%=!editor.isHideButtonPanel()%>" data-showWeekday=<%=editor.isShowWeekday()%> data-suppress-alert="true" />
<input type="hidden" id="i_<c:out value="<%=propNameFrom%>"/>" name="<c:out value="<%=propNameFrom%>"/>" value="<c:out value="<%=propValueFrom%>"/>" />
</span>
<%
		}
		if (!editor.isSingleDayCondition()) {
			if ((!editor.isHideSearchConditionFrom() && !editor.isHideSearchConditionTo())
					|| !editor.isHideSearchConditionRangeSymbol()) {
%>
<span class="range-symbol">&nbsp;${m:rs('mtp-gem-messages', 'generic.editor.common.rangeSymbol')}&nbsp;</span>
<%
			}
		}

		String defaultValueTo = "";
		if (defaultValue != null && defaultValue.length > 1 && formatCheck(defaultValue[1])) {
			defaultValueTo = defaultValue[1];
		}

		onchange = "dateChange('" + propNameTo + "')";

		String toDisp = "";
		if (hideTo) {
			toDisp = "display: none;";
		}
		if (editor.getDisplayType() == DateTimeDisplayType.LABEL) {
			String dateToDisplayLabel = displayFormat(propValueTo, formatInfo.getDatetimeFormat(), formatInfo.getDatetimeLocale(), editor.isShowWeekday());
			toDisp = toDisp + customStyle;
%>
<span style="<c:out value="<%=toDisp%>"/>">
<c:out value="<%=dateToDisplayLabel %>" />
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propNameTo %>"/>" value="<c:out value="<%=propValueTo %>"/>" />
</span>
<%
		} else {
%>
<span style="<c:out value="<%=toDisp%>"/>">
<%-- XSS対応-メタの設定のため対応なし(onchange) --%>
<input type="text" id="d_<c:out value="<%=propNameTo%>"/>" class="datepicker inpbr" style="<c:out value="<%=customStyle%>"/>" value=""  onchange="<%=onchange%>" data-showButtonPanel="<%=!editor.isHideButtonPanel()%>" data-showWeekday=<%=editor.isShowWeekday()%> data-suppress-alert="true" />
<input type="hidden" id="i_<c:out value="<%=propNameTo %>"/>" name="<c:out value="<%=propNameTo %>"/>" value="<c:out value="<%=propValueTo %>"/>" />
</span>
<%
		}
%>

<script type="text/javascript">
$(function() {
	var from = convertToLocaleDateString("<%=StringUtil.escapeJavaScript(propValueFrom)%>");
	var to = convertToLocaleDateString("<%=StringUtil.escapeJavaScript(propValueTo)%>");

	$("#d_" + es("<%=StringUtil.escapeJavaScript(propNameFrom)%>")).val(from).trigger("blur");
	$("#d_" + es("<%=StringUtil.escapeJavaScript(propNameTo)%>")).val(to).trigger("blur");

	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		var defaultFrom = convertToLocaleDateString("<%=StringUtil.escapeJavaScript(defaultValueFrom)%>");
		var defaultTo = convertToLocaleDateString("<%=StringUtil.escapeJavaScript(defaultValueTo)%>");

		var $from = $("#d_" + es("<%=StringUtil.escapeJavaScript(propNameFrom)%>"));
		$from.val(defaultFrom);
		$("#i_" + es("<%=StringUtil.escapeJavaScript(propNameFrom)%>")).val("<%=StringUtil.escapeJavaScript(defaultValueFrom)%>");
		var $to = $("#d_" + es("<%=StringUtil.escapeJavaScript(propNameTo)%>"));
		$to.val(defaultTo);
		$("#i_" + es("<%=StringUtil.escapeJavaScript(propNameTo)%>")).val("<%=StringUtil.escapeJavaScript(defaultValueTo)%>");

		var $parent = $from.closest(".property-data");
		$from.removeClass("validate-error");
		$to.removeClass("validate-error");
		$(".format-error", $parent).remove();
	});

<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var fromVal = $("#d_" + es("<%=StringUtil.escapeJavaScript(propNameFrom)%>")).val();
		var toVal = $("#d_" + es("<%=StringUtil.escapeJavaScript(propNameTo)%>")).val();
		if ((typeof fromVal === "undefined" || fromVal == null || fromVal == "")
				&& (typeof toVal === "undefined" || toVal == null || toVal == "")) {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
<%
		}
	//フォーマットチェック
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var fromVal = $("#d_" + es("<%=StringUtil.escapeJavaScript(propNameFrom)%>")).val();
		var toVal = $("#d_" + es("<%=StringUtil.escapeJavaScript(propNameTo)%>")).val();
		if (typeof fromVal !== "undefined" && fromVal != null && fromVal !== "") {
			try {
				validateDate(fromVal, dateUtil.getInputDateFormat(), "");
			} catch (e) {
				alert(messageFormat(scriptContext.gem.locale.common.dateFormatErrorMsg, "<%=StringUtil.escapeJavaScript(displayLabel)%>", dateUtil.getInputDateFormat()))
				return false;
			}
		}
		if (typeof toVal !== "undefined" && toVal != null && toVal !== "") {
			try {
				validateDate(toVal, dateUtil.getInputDateFormat(), "");
			} catch (e) {
				alert(messageFormat(scriptContext.gem.locale.common.dateFormatErrorMsg, "<%=StringUtil.escapeJavaScript(displayLabel)%>", dateUtil.getInputDateFormat()))
				return false;
			}
		}
		return true;
	});
});
</script>
<%
	} else {
		//HIDDEN
%>
<input type="hidden" id="i_<c:out value="<%=propNameFrom%>"/>" name="<c:out value="<%=propNameFrom%>"/>" value="<c:out value="<%=propValueFrom%>"/>" />
<input type="hidden" id="i_<c:out value="<%=propNameTo %>"/>" name="<c:out value="<%=propNameTo %>"/>" value="<c:out value="<%=propValueTo %>"/>" />
<%
	}
%>
