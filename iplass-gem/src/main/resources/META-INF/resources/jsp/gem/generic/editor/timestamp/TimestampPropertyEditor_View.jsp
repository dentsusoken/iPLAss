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
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Locale"%>
<%@ page import="org.iplass.mtp.impl.core.ExecuteContext"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimeFormatProperty" %>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TimestampPropertyEditor" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	String displayFormat(Timestamp time, TimeDispRange dispRange, String datetimeFormatPattern, String datetimeLocale, boolean showWeekday) {
		if (time == null) {
			return "";
		}
		DateFormat format = null;

		if (datetimeFormatPattern != null) {
			//フォーマットの指定がある場合、指定されたフォーマットで表記する
			format = getSimpleDateFormat(datetimeFormatPattern, datetimeLocale);
		} else {
			String timeFormat = "";
			if (TimeDispRange.isDispSec(dispRange)) {
				timeFormat = " " + TemplateUtil.getLocaleFormat().getOutputTimeSecFormat();
			} else if (TimeDispRange.isDispMin(dispRange)) {
				timeFormat = " " + TemplateUtil.getLocaleFormat().getOutputTimeMinFormat();
			} else if (TimeDispRange.isDispHour(dispRange)) {
				timeFormat = " " + TemplateUtil.getLocaleFormat().getOutputTimeHourFormat();
			}

			if (showWeekday) {
				String dateFormat = TemplateUtil.getLocaleFormat().getOutputDateWeekdayFormat();
				//テナントのロケールと言語が違う場合、編集画面と曜日の表記が変わるため、LangLocaleを利用
				format = DateUtil.getSimpleDateFormat(dateFormat + timeFormat, true, true);
			} else {
				String dateFormat = TemplateUtil.getLocaleFormat().getOutputDateFormat();
				format = DateUtil.getSimpleDateFormat(dateFormat + timeFormat, true);
			}
		}
		return format.format(time);
	}

	String format(Timestamp time) {
		SimpleDateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateTimeFormat(), true);
		return time != null ? format.format(time) : "";
	}

	DateFormat getSimpleDateFormat(String pattern, String datetimeLocale) {
		//指定されたフォーマットで、ロケール設定がない場合はデフォルトで指定されているロケールのものを使用する
		Locale locale = ExecuteContext.getCurrentContext().getLocale();
		if (datetimeLocale != null) {
			String[] localeValue = datetimeLocale.split("_", 0);
			if (localeValue.length <= 1) {
				locale = new Locale(localeValue[0]);
			} else if (localeValue.length <= 2) {
				locale = new Locale(localeValue[0], localeValue[1]);
			} else if (localeValue.length <= 3) {
				locale = new Locale(localeValue[0], localeValue[1], localeValue[2]);
			}
		}

		DateFormat format = new SimpleDateFormat(pattern, locale);

		return format;
	}
%>

<%
	TimestampPropertyEditor editor = (TimestampPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
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

		String datetimeFormatPattern = null;
		String datetimeLocale = null;
		if(editor.getDatetimeFormatList() != null && editor.getDatetimeFormatList().size() > 0){
			boolean defaultFlag = true;
			for(DateTimeFormatProperty dfp : editor.getDatetimeFormatList()){
				if(dfp.getDatetimeLocale() == null){
					break;
				}
				String[] langInfo = dfp.getDatetimeLocale().split("_", 0);
				String localLang = langInfo[0];
				String tenantLang = ExecuteContext.getCurrentContext().getLanguage();
				if((tenantLang.equals(localLang))){
					datetimeFormatPattern = dfp.getDatetimeFormat();
					datetimeLocale = dfp.getDatetimeLocale();
					defaultFlag = false;
					break;
				}
			}
			if(defaultFlag){
				datetimeFormatPattern = editor.getDatetimeFormatList().get(0).getDatetimeFormat();
				datetimeLocale = editor.getDatetimeFormatList().get(0).getDatetimeLocale();
			}
		}

		if (isMultiple) {
			//複数
%>
<ul class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
			Timestamp[] array = propValue instanceof Timestamp[] ? (Timestamp[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					Timestamp t = array[i];
%>
<li>
<c:out value="<%=displayFormat(t, editor.getDispRange(), datetimeFormatPattern , datetimeLocale, editor.isShowWeekday()) %>"/>
<%
					if (outputHidden) {
						String strHidden = format(t);
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
			Timestamp t = propValue instanceof Timestamp ? (Timestamp) propValue : null;
%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>" data-time-range="<c:out value="<%=editor.getDispRange() %>"/>" data-show-weekday="<c:out value="<%=editor.isShowWeekday() %>"/>">
<c:out value="<%=displayFormat(t, editor.getDispRange(), datetimeFormatPattern, datetimeLocale, editor.isShowWeekday()) %>"/>
<%
			if (outputHidden) {
				String strHidden = format(t);
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
			Timestamp[] array = propValue instanceof Timestamp[] ? (Timestamp[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					Timestamp t = array[i];
					String strHidden = format(t);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<%
				}
			}
		} else {
			//単一
			Timestamp t = propValue instanceof Timestamp ? (Timestamp) propValue : null;
			String strHidden = format(t);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<%
		}
	}
%>
