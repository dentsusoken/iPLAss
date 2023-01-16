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

<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="java.util.Locale"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.GemConfigService"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.mtp.spi.ServiceRegistry"%>

<%!
	String format(String format, Number value) {
		if (value == null) return "";

		GemConfigService gemConfig = ServiceRegistry.getRegistry().getService(GemConfigService.class);

		String str = null;
		try {
			DecimalFormat df = new DecimalFormat();
			if (gemConfig.isFormatNumberWithComma()) {
				//カンマでフォーマットする場合は指定のフォーマットがある場合だけフォーマット適用
				if (format != null) {
					df.applyPattern(format);
					str = df.format(value);
				} else{
					NumberFormat nf = NumberFormat.getInstance(TemplateUtil.getLocale());
					str = nf.format(value);
				}
			} else {
				//カンマでフォーマットしない場合はフォーマットがない場合に数値のみのフォーマットを適用
				if (format == null) format = "#.###";
				df.applyPattern(format);
				str = df.format(value);
			}
		} catch (NumberFormatException e) {
			str = "";
		}
		return str;
	}
%>

<%
	NumberPropertyEditor editor = (NumberPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
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

	if (editor.getDisplayType() != NumberDisplayType.HIDDEN) {

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

		String clsComma = "";
		if (editor.isShowComma()) {
			clsComma = " commaLabel";
		}
		
		if (isMultiple) {
			//複数
%>
<ul name="data-label-<c:out value="<%=propName %>"/>" class="data-label<c:out value="<%=clsComma %>"/>" style="<c:out value="<%=customStyle %>"/>">
<%
			Number[] array = propValue instanceof Number[] ? (Number[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					Number tmp = array[i];
					String str = format(editor.getNumberFormat(), tmp);
%>
<li>
<c:out value="<%=str %>"/>
<%
					String hiddenValue = "";
					if (tmp instanceof Double) hiddenValue = "" + ((Double)tmp).doubleValue();
					else if (tmp instanceof Long) hiddenValue = "" + ((Long)tmp).longValue();
%>
</li>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hiddenValue %>"/>" />

<%
				}
			}
%>
</ul>
<%
		} else {
			//単数
			String str = propValue instanceof Number ? format(editor.getNumberFormat(), (Number) propValue) : "";
%>
<span name="data-label-<c:out value="<%=propName %>"/>" class="data-label<c:out value="<%=clsComma %>"/>" style="<c:out value="<%=customStyle %>"/>">
<c:out value="<%=str %>"/>
<%
			String hiddenValue = "";
			if (propValue instanceof Double) hiddenValue = "" + ((Double)propValue).doubleValue();
			else if (propValue instanceof Long) hiddenValue = "" + ((Long)propValue).longValue();
%>
</span>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hiddenValue %>"/>" />

<%
		}
	} else {
		//HIDDEN
		
		if (isMultiple) {
			//複数
			Number[] array = propValue instanceof Number[] ? (Number[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					Number tmp = array[i];
					String hiddenValue = "";
					if (tmp instanceof Double) hiddenValue = "" + ((Double)tmp).doubleValue();
					else if (tmp instanceof Long) hiddenValue = "" + ((Long)tmp).longValue();
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hiddenValue %>"/>" />
<%
				}
			}
		} else {
			//単数
			String hiddenValue = "";
			if (propValue instanceof Double) hiddenValue = "" + ((Double)propValue).doubleValue();
			else if (propValue instanceof Long) hiddenValue = "" + ((Long)propValue).longValue();
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hiddenValue %>"/>" />
<%
		}
	}
%>
