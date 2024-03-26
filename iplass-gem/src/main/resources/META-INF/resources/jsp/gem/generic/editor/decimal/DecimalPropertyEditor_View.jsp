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
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DecimalPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.GemConfigService"%>
<%@ page import="org.iplass.mtp.spi.ServiceRegistry"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	String format(String format, BigDecimal value) {
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
	DecimalPropertyEditor editor = (DecimalPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
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

		//カスタム表示スタイル
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
<ul name="data-label-<c:out value="<%=propName %>"/>"  class="data-label<c:out value="<%=clsComma %>"/>" style="<c:out value="<%=customStyle %>"/>">
<%
			BigDecimal[] array = propValue instanceof BigDecimal[] ? (BigDecimal[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					String str = format(editor.getNumberFormat(), array[i]);
%>
<li>
<c:out value="<%=str %>"/>
<%
					if (outputHidden) {
						String hiddenValue = array[i] instanceof BigDecimal ? ((BigDecimal)array[i]).toPlainString() : "";
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hiddenValue %>"/>" />
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
			BigDecimal target = propValue instanceof BigDecimal ? (BigDecimal) propValue : null;
			String str = format(editor.getNumberFormat(), target);
%>
<span name="data-label-<c:out value="<%=propName %>"/>"  class="data-label<c:out value="<%=clsComma %>"/>" style="<c:out value="<%=customStyle %>"/>">
<c:out value="<%=str %>"/>
<%
			if (outputHidden) {
				String hiddenValue = propValue instanceof BigDecimal ? ((BigDecimal)propValue).toPlainString() : "";
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hiddenValue %>"/>" />
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
			BigDecimal[] array = propValue instanceof BigDecimal[] ? (BigDecimal[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					String hiddenValue = array[i] instanceof BigDecimal ? ((BigDecimal)array[i]).toPlainString() : "";
%>
<li>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hiddenValue %>"/>" />
</li>
<%
				}
			}
%>
</ul>
<%
		} else {
			//単一
			String hiddenValue = propValue instanceof BigDecimal ? ((BigDecimal)propValue).toPlainString() : "";
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hiddenValue %>"/>" />
<%
		}
	}
%>
