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
<%@ page import="java.text.DateFormat" %>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DatePropertyEditor" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	String displayFormat(Date date, boolean showWeekday) {
		if (date == null) return "";

		if (showWeekday) {
			//テナントのロケールと言語が違う場合、編集画面と曜日の表記が変わるため、LangLocaleを利用
			DateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateWeekdayFormat(), false, true);
			return format.format(date);
		} else {
			DateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), false);
			return format.format(date);
		}
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

	boolean isMultiple = pd.getMultiplicity() != 1;
	if (isMultiple) {
		//複数
%>
<ul class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
		Date[] array = propValue instanceof Date[] ? (Date[]) propValue : null;
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				String str = displayFormat(array[i], editor.isShowWeekday());
%>
<li>
<c:out value="<%=str %>" />
<%
				if (outputHidden) {
					str = format(array[i]);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" />
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
		String str = displayFormat(date, editor.isShowWeekday());
%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>" data-show-weekday="<c:out value="<%=editor.isShowWeekday() %>"/>">
<c:out value="<%=str %>" />
<%
		if (outputHidden) {
			str = format(date);
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" />
<%
		}
%>
</span>
<%
	}
%>
