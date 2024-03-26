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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.sql.Time" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Locale"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.impl.core.ExecuteContext"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimeFormatSetting"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TimePropertyEditor" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%!
	String displayFormat(Time time, String datetimeFoematPattern, String datetimeLocale, TimeDispRange dispRange) {
		if (time == null) {
			return "";
		}

		DateFormat format = null;
		if(datetimeFoematPattern != null){
			format = ViewUtil.getDateTimeFormat(datetimeFoematPattern, datetimeLocale);
		} else if (TimeDispRange.isDispSec(dispRange)) {
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputTimeSecFormat(), false);
		} else if (TimeDispRange.isDispMin(dispRange)) {
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputTimeMinFormat(), false);
		} else if (TimeDispRange.isDispHour(dispRange)) {
			format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputTimeHourFormat(), false);
		} else {
			return "";
		}

		String value = format.format(time);

		return value;
	}
	String format(Time time) {
		SimpleDateFormat format = DateUtil.getSimpleDateFormat("HHmmssSSS", false);
		return time != null ? format.format(time) : "";
	}

%>
<%
	TimePropertyEditor editor = (TimePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
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

		DateTimeFormatSetting formatInfo = ViewUtil.getFormatInfo(editor.getLocalizedDatetimeFormatList(), editor.getDatetimeFormat());

		if (isMultiple) {
			//複数
%>
<ul name="data-label-<c:out value="<%=propName %>"/>" class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
			Time[] array = propValue instanceof Time[] ? (Time[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					Time t = array[i];
%>
<li>
<c:out value="<%=displayFormat(t, formatInfo.getDatetimeFormat(), formatInfo.getDatetimeLocale(), editor.getDispRange()) %>"/>
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
			Time t = propValue instanceof Time ? (Time) propValue : null;
%>
<span name="data-label-<c:out value="<%=propName %>"/>" class="data-label" style="<c:out value="<%=customStyle %>"/>" data-time-range="<c:out value="<%=editor.getDispRange() %>"/>">
<c:out value="<%=displayFormat(t, formatInfo.getDatetimeFormat(), formatInfo.getDatetimeLocale(), editor.getDispRange()) %>"/>
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
%>
<ul name="data-hidden-<c:out value="<%=propName %>"/>">
<%
			Time[] array = propValue instanceof Time[] ? (Time[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					Time t = array[i];
					String strHidden = format(t);
%>
<li>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
</li>
<%
				}
			}
%>
</ul>
<%
		} else {
			//単一
			Time t = propValue instanceof Time ? (Time) propValue : null;
			String strHidden = format(t);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<%
		}
	}
%>
