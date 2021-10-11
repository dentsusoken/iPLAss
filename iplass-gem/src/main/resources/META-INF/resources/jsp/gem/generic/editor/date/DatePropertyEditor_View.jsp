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
<%@ page import="java.sql.Date" %>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.impl.core.ExecuteContext"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DatePropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimeFormatSetting"%>
<%@ page import="org.iplass.mtp.view.generic.editor.LocalizedDateTimeFormatSetting"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%!String displayFormat(Date date, String datetimeFormatPattern, String datetimeLocale, boolean showWeekday) {
		if (date == null) return "";
		DateFormat format = null;

		if (datetimeFormatPattern != null) {
			//フォーマットの指定がある場合、指定されたフォーマットで表記する
			format = ViewUtil.getDateTimeFormat(datetimeFormatPattern, datetimeLocale);
		} else if (showWeekday) {
			//テナントのロケールと言語が違う場合、編集画面と曜日の表記が変わるため、LangLocaleを利用
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateWeekdayFormat(), false, true);
		} else {
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), false);
		}
		return format.format(date);
	}

	String format(Date date) {
		DateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateFormat(), false);
		return date != null ? format.format(date) : "";
	}

%>

<%
	DatePropertyEditor editor = (DatePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	Boolean outputHidden = (Boolean) request.getAttribute(Constants.OUTPUT_HIDDEN);
	if (outputHidden == null) outputHidden = false;

	String propName = editor.getPropertyName();

	boolean isMultiple = pd.getMultiplicity() != 1;

	if (editor.getDisplayType() != DateTimeDisplayType.HIDDEN) {

		//カスタムスタイル
		String customStyle = "";
		if (type == OutputType.VIEW) {
			if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), entity, propValue);
			}
		} else if (type == OutputType.EDIT) {
			//入力不可の場合
			if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
			}
		}

	DateTimeFormatSetting formatInfo = ViewUtil.setFormatInfo(editor.getLocalizedDatetimeFormatList(), editor.getDatetimeFormat());

		if (isMultiple) {
			//複数
%>
<ul class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
			Date[] array = propValue instanceof Date[] ? (Date[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
%>
<li>
<c:out value="<%=displayFormat(array[i], formatInfo.getDatetimeFormat(), formatInfo.getDatetimeLocale(), editor.isShowWeekday()) %>" />
<%
					if (outputHidden) {
						String strHidden = format(array[i]);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<%
					}
%>
</li>
<%
				}
			}
%>
</ul>
<%
		} else {
			//単一
			Date date = propValue instanceof Date ? (Date) propValue : null;
%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>" data-show-weekday="<c:out value="<%=editor.isShowWeekday() %>"/>">
<c:out value="<%=displayFormat(date, formatInfo.getDatetimeFormat(), formatInfo.getDatetimeLocale(), editor.isShowWeekday()) %>" />
<%
			if (outputHidden) {
				String strHidden = format(date);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<%
			}
%>
</span>
<%
		}

	} else {
		//HIDDEN
		if (isMultiple) {
			//複数
			Date[] array = propValue instanceof Date[] ? (Date[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					String strHidden = format(array[i]);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<%
				}
			}
		} else {
			//単一
			Date date = propValue instanceof Date ? (Date) propValue : null;
			String strHidden = format(date);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<%
		}
	}
%>
