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
<%@ page import="java.sql.Date"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DatePropertyEditor" %>
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

	String getDateValue(String searchCond, String key) {
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

	DatePropertyEditor editor = (DatePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	boolean hideFrom = editor.isSingleDayCondition() ? false : editor.isHideSearchConditionFrom();
	boolean hideTo = editor.isSingleDayCondition() ? true : editor.isHideSearchConditionTo();

	String searchCond = request.getParameter(Constants.SEARCH_COND);
	String propNameFrom = Constants.SEARCH_COND_PREFIX + editor.getPropertyName() + "From";

	String defaultValueFrom = "";
	if (defaultValue != null && defaultValue.length > 0 && formatCheck(defaultValue[0])) {
		defaultValueFrom = defaultValue[0];
	}

	//直接searchCondから取得(hidden対応)
	String propValueFrom = getDateValue(searchCond, propNameFrom);
	if (propValueFrom == null) {
		//初期値から復元(検索時に未指定の場合、ここにくる)
		if (propValue != null && propValue.length > 0 && formatCheck(propValue[0])) {
			propValueFrom = propValue[0];
		} else {
			propValueFrom = "";
		}
	}

	String onchange = "dateChange('" + StringUtil.escapeJavaScript(propNameFrom) + "')";

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/date/DatePropertyAutocompletion.jsp");
	}

	String fromDisp = "";
	if (hideFrom) {
		fromDisp = "display: none;";
	}
%>
<span style="<c:out value="<%=fromDisp %>"/>">
<%-- XSS対応-メタの設定のため対応なし(onchange) --%>
<input type="text" id="d_<c:out value="<%=propNameFrom %>"/>" class="datepicker inpbr" style="<c:out value="<%=customStyle%>"/>" value="" onchange="<%=onchange %>" data-showButtonPanel="<%=!editor.isHideButtonPanel()%>" data-showWeekday=<%=editor.isShowWeekday()%> />
<input type="hidden" id="i_<c:out value="<%=propNameFrom%>"/>" name="<c:out value="<%=propNameFrom%>"/>" value="<c:out value="<%=propValueFrom%>"/>" />
</span>
<%
	if (!hideFrom && !hideTo) {
%>
&nbsp;～&nbsp;
<%
	}
	String propNameTo = Constants.SEARCH_COND_PREFIX + editor.getPropertyName() + "To";

	String defaultValueTo = "";
	if (defaultValue != null && defaultValue.length > 1 && formatCheck(defaultValue[1])) {
		defaultValueTo = defaultValue[1];
	}

	//直接searchCondから取得(hidden対応)
	String propValueTo = getDateValue(searchCond, propNameTo);
	if (propValueTo == null) {
		//初期値から復元(検索時に未指定の場合、ここにくる)
		if (propValue != null && propValue.length > 1 && formatCheck(propValue[1])) {
			propValueTo = propValue[1];
		} else {
			propValueTo = "";
		}
	}
	onchange = "dateChange('" + propNameTo + "')";

	String toDisp = "";
	if (hideTo) {
		toDisp = "display: none;";
	}
%>
<span style="<c:out value="<%=toDisp%>"/>">
<%-- XSS対応-メタの設定のため対応なし(onchange) --%>
<input type="text" id="d_<c:out value="<%=propNameTo%>"/>" class="datepicker inpbr" style="<c:out value="<%=customStyle%>"/>" value=""  onchange="<%=onchange%>" data-showButtonPanel="<%=!editor.isHideButtonPanel()%>" data-showWeekday=<%=editor.isShowWeekday()%> />
<input type="hidden" id="i_<c:out value="<%=propNameTo %>"/>" name="<c:out value="<%=propNameTo %>"/>" value="<c:out value="<%=propValueTo %>"/>" />
</span>

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

		$("#d_" + es("<%=StringUtil.escapeJavaScript(propNameFrom)%>")).val(defaultFrom);
		$("#i_" + es("<%=StringUtil.escapeJavaScript(propNameFrom)%>")).val("<%=StringUtil.escapeJavaScript(defaultValueFrom)%>");
		$("#d_" + es("<%=StringUtil.escapeJavaScript(propNameTo)%>")).val(defaultTo);
		$("#i_" + es("<%=StringUtil.escapeJavaScript(propNameTo)%>")).val("<%=StringUtil.escapeJavaScript(defaultValueTo)%>");
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
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
<%
	}
%>
});
</script>

