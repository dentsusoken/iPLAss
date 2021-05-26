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
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="org.iplass.mtp.spi.ServiceRegistry"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.FloatPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.IntegerPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.PropertyEditor" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%@ page import="org.iplass.gem.GemConfigService"%>

<%!
	String convertNumber(String value, PropertyEditor editor) {
		if (value == null) return "";
		if (editor instanceof IntegerPropertyEditor) {
			try {
				return "" + Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return "";
			}
		} else if (editor instanceof FloatPropertyEditor) {
			try {
				return "" + Double.parseDouble(value);
			} catch (NumberFormatException e) {
				return "";
			}
		}
		return "";
	}
	Number convertStringToNumber(String value, PropertyEditor editor) {
		if (value == null) return null;
		if (editor instanceof IntegerPropertyEditor) {
			try {
				return (Number)Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return null;
			}
		} else if (editor instanceof FloatPropertyEditor) {
			try {
				return (Number)Double.parseDouble(value);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}
	String format(String valueStr, NumberPropertyEditor editor) {
		if (valueStr == null) return "";
		Number value = convertStringToNumber(valueStr, editor);
		if (value == null) return "";

		GemConfigService gemConfig = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		String format = editor.getNumberFormat();
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

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String propName = Constants.SEARCH_COND_PREFIX + editor.getPropertyName();
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	String value = "";
	if (propValue != null && propValue.length > 0) {
		value = convertNumber(propValue[0], editor);
	}

	String valueTo = "";
	if (editor.isSearchInRange()) {
		if (propValue != null && propValue.length > 1) {
			valueTo = convertNumber(propValue[1], editor);
		}
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/number/NumberPropertyAutocompletion.jsp");
	}
	if (editor.getDisplayType() != NumberDisplayType.HIDDEN) {
		//HIDDEN以外

		String strDefault = "";
		if (defaultValue != null && defaultValue.length > 0) {
			strDefault = convertNumber(defaultValue[0], editor);
		}

		//カスタムスタイル
		String customStyle = "";
		if (editor.getDisplayType() != NumberDisplayType.LABEL) {
			if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
			}
		} else {
			if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), null, null);
			}
		}
		Map<String, List<String>> searchCondMap = (Map<String, List<String>>)request.getAttribute(Constants.SEARCH_COND_MAP);
		if (editor.getDisplayType() == NumberDisplayType.LABEL) {
			String[] _strDefault = ViewUtil.getSearchCondValue(searchCondMap,  Constants.SEARCH_COND_PREFIX + editor.getPropertyName());
			strDefault = _strDefault != null && _strDefault.length > 0 ? _strDefault[0] : strDefault;
			String str = format(strDefault, editor);
%>
<span  style="<c:out value="<%=customStyle%>"/>">
<c:out value="<%=str %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strDefault %>"/>" />
</span>
<%
		} else {
%>
<input type="text" class="form-size-04 inpbr" style="<c:out value="<%=customStyle%>"/>" value="<%=value %>" name="<c:out value="<%=propName %>"/>" onblur="numcheck(this)" />
<%
		}
		String strDefaultTo = "";
		if (editor.isSearchInRange()) {

			if (defaultValue != null && defaultValue.length > 1) {
				strDefaultTo = convertNumber(defaultValue[1], editor);
			}
%>
&nbsp;～&nbsp;
<%
			if (editor.getDisplayType() == NumberDisplayType.LABEL) {
				String[] _strDefaultTo = ViewUtil.getSearchCondValue(searchCondMap,  Constants.SEARCH_COND_PREFIX + editor.getPropertyName() + "To");
				strDefaultTo = _strDefaultTo != null && _strDefaultTo.length > 0 ? _strDefaultTo[0] : strDefaultTo;
				String strTo = format(strDefaultTo, editor);
%>
<span  style="<c:out value="<%=customStyle%>"/>">
<c:out value="<%=strTo %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propName %>"/>To" value="<c:out value="<%=strDefaultTo %>"/>" />
</span>
<%
			} else {
%>
<input type="text" class="form-size-04 inpbr" style="<c:out value="<%=customStyle%>"/>" value="<%=valueTo %>" name="<c:out value="<%=propName %>"/>To" onblur="numcheck(this)" />
<%
			}
		}
%>
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val("<%=strDefault %>");
		$(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>To") + "']").val("<%=strDefaultTo %>");
	});
<%
		if (required) {
			if (!editor.isSearchInRange()) {
				//Fromのみ
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}

		return true;
	});
<%
			} else {
				//範囲
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		var valTo = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>To") + "']").val();
		if ((typeof val === "undefined" || val == null || val == "") && (typeof valTo === "undefined" || valTo == null || valTo == "")) {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}

		return true;
	});
<%
			}
		}
%>
});
</script>
<%
	} else {
		//HIDDEN
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=value %>"/>"/>
<%
		if (editor.isSearchInRange()) {
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>To" value="<c:out value="<%=valueTo %>"/>"/>
<%
		}

	}
%>