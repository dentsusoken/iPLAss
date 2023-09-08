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
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
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

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/number/NumberPropertyAutocompletion.jsp");
	}
	if (editor.getDisplayType() != NumberDisplayType.HIDDEN) {
		//HIDDEN以外

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
		String tmpCls = "";
		if (editor.isShowComma()) {
			tmpCls += " commaField";
		}

		if (!editor.isSearchInRange()) {
			// 単一検索

			String strDefault = "";
			if (defaultValue != null && defaultValue.length > 0) {
				strDefault = convertNumber(defaultValue[0], editor);
			}

			if (editor.getDisplayType() == NumberDisplayType.LABEL) {
				String[] _strDefault = ViewUtil.getSearchCondValue(searchCondMap,  Constants.SEARCH_COND_PREFIX + editor.getPropertyName());
				strDefault = _strDefault != null && _strDefault.length > 0 ? _strDefault[0] : strDefault;
				String formatValue = format(strDefault, editor);
%>
<span  style="<c:out value="<%=customStyle%>"/>">
<c:out value="<%=formatValue %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strDefault %>"/>" />
</span>
<%
			} else {
				String inputValue = "";
				if (propValue != null && propValue.length > 0) {
					inputValue = convertNumber(propValue[0], editor);
				}
%>
<span>
<input type="text" class="form-size-04 inpbr <c:out value="<%=tmpCls %>"/>" style="<c:out value="<%=customStyle%>"/>" 
	value="<%=inputValue %>" name="<c:out value="<%=propName %>"/>" onblur="numcheck(this, true)" />
</span>
<%
			}
%>
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		var $input = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']");
		$input.val("<%=strDefault %>");

		var $parent = $input.closest(".property-data");
		$input.removeClass("validate-error");
		$(".format-error", $parent).remove();

<%
			if (editor.isShowComma()) {
%>
		$(".commaField.dummyField", $parent).remove();
		$(".commaField", $parent).show();
<%
			}
%>
	});
<%
			if (required) {
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
			}

			//フォーマットチェック
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (typeof val !== "undefined" && val !== null && val !== "" && isNaN(val)) {
			alert(scriptContext.gem.locale.common.numFormatErrorMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
});
</script>
<%
		} else {
			// 範囲検索

			String dispStyleFrom = editor.isHideSearchConditionFrom() ? "display: none;" : "";

			String strDefaultFrom = "";
			if (defaultValue != null && defaultValue.length > 0) {
				strDefaultFrom = convertNumber(defaultValue[0], editor);
			}
			if (editor.getDisplayType() == NumberDisplayType.LABEL) {
				String[] _strDefaultFrom = ViewUtil.getSearchCondValue(searchCondMap,  Constants.SEARCH_COND_PREFIX + editor.getPropertyName() + "From");
				strDefaultFrom = _strDefaultFrom != null && _strDefaultFrom.length > 0 ? _strDefaultFrom[0] : strDefaultFrom;
				String formatValue = format(strDefaultFrom, editor);
%>
<span style="<c:out value="<%=dispStyleFrom + customStyle%>"/>">
<c:out value="<%=formatValue %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propName %>"/>From" value="<c:out value="<%=strDefaultFrom %>"/>" />
</span>
<%
			} else {
				String inputValueFrom = "";
				if (propValue != null && propValue.length > 0) {
					inputValueFrom = convertNumber(propValue[0], editor);
				}
%>
<span style="<c:out value="<%=dispStyleFrom%>"/>">
<input type="text" class="form-size-04 inpbr <c:out value="<%=tmpCls %>"/>" style="<c:out value="<%=customStyle%>"/>" 
	value="<%=inputValueFrom %>" name="<c:out value="<%=propName %>"/>From" onblur="numcheck(this, true)" />
</span>
<%
			}

			if ((!editor.isHideSearchConditionFrom() && !editor.isHideSearchConditionTo())
					|| !editor.isHideSearchConditionRangeSymbol()) {
%>
<span class="range-symbol">&nbsp;${m:rs('mtp-gem-messages', 'generic.editor.common.rangeSymbol')}&nbsp;</span>
<%
			}

			String dispStyleTo = editor.isHideSearchConditionTo() ? "display: none;" : "";

			String strDefaultTo = "";
			if (defaultValue != null && defaultValue.length > 1) {
				strDefaultTo = convertNumber(defaultValue[1], editor);
			}
			if (editor.getDisplayType() == NumberDisplayType.LABEL) {
				String[] _strDefaultTo = ViewUtil.getSearchCondValue(searchCondMap,  Constants.SEARCH_COND_PREFIX + editor.getPropertyName() + "To");
				strDefaultTo = _strDefaultTo != null && _strDefaultTo.length > 0 ? _strDefaultTo[0] : strDefaultTo;
				String formatValue = format(strDefaultTo, editor);
%>
<span  style="<c:out value="<%=dispStyleTo + customStyle%>"/>">
<c:out value="<%=formatValue %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propName %>"/>To" value="<c:out value="<%=strDefaultTo %>"/>" />
</span>
<%
			} else {
				String inputValueTo = "";
				if (propValue != null && propValue.length > 1) {
					inputValueTo = convertNumber(propValue[1], editor);
				}
%>
<span style="<c:out value="<%=dispStyleTo%>"/>">
<input type="text" class="form-size-04 inpbr <c:out value="<%=tmpCls %>"/>" style="<c:out value="<%=customStyle%>"/>" 
	value="<%=inputValueTo %>" name="<c:out value="<%=propName %>"/>To" onblur="numcheck(this, true)" />
</span>
<%
			}
%>
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		var $from = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>From") + "']");
		$from.val("<%=strDefaultFrom %>");
		var $to = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>To") + "']");
		$to.val("<%=strDefaultTo %>");

		var $parent = $from.closest(".property-data");
		$from.removeClass("validate-error");
		$to.removeClass("validate-error");
		$(".format-error", $parent).remove();

<%
			if (editor.isShowComma()) {
%>
		$(".commaField.dummyField", $parent).remove();
		$(".commaField", $parent).show();
<%
			}
%>
	});
<%
			if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var valFrom = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>From") + "']").val();
		var valTo = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>To") + "']").val();
		if ((typeof valFrom === "undefined" || valFrom == null || valFrom == "") 
				&& (typeof valTo === "undefined" || valTo == null || valTo == "")) {
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
		var valFrom = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>From") + "']").val();
		var valTo = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>To") + "']").val();
		if ((typeof valFrom !== "undefined" && valFrom !== null && valFrom !== "" && isNaN(valFrom)) 
				|| (typeof valTo !== "undefined" && valTo != null && valTo !== "" && isNaN(valTo))) {
			alert(scriptContext.gem.locale.common.numFormatErrorMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}

		return true;
	});
});
</script>
<%
		}
	} else {
		//HIDDEN
		if (!editor.isSearchInRange()) {
			String inputValue = "";
			if (propValue != null && propValue.length > 0) {
				inputValue = convertNumber(propValue[0], editor);
			}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=inputValue %>"/>"/>
<%
		} else {
			String inputValueFrom = "";
			if (propValue != null && propValue.length > 0) {
				inputValueFrom = convertNumber(propValue[0], editor);
			}
			String inputValueTo = "";
			if (propValue != null && propValue.length > 1) {
				inputValueTo = convertNumber(propValue[1], editor);
			}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>From" value="<c:out value="<%=inputValueFrom %>"/>"/>
<input type="hidden" name="<c:out value="<%=propName %>"/>To" value="<c:out value="<%=inputValueTo %>"/>"/>
<%
		}

	}
%>
