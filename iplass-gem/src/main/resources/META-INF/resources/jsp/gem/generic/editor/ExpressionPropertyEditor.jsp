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
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.ExpressionProperty"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor" %>
<%@ page import="org.iplass.gem.command.Constants" %>

<%!
String format(String format, Object value) {
	if (value == null) return "";
	String str = "";
	if (value instanceof String) {
		if (format != null) {
			try {
				BigDecimal decimal = new BigDecimal((String) value);
				str = new DecimalFormat(format).format(decimal);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			str = (String) value;
		}
	} else if (value instanceof Number) {
		if (format != null) {
			str = new DecimalFormat(format).format(value);
		} else {
			str = value.toString();
		}
	} else {
		str = value.toString();
	}
	return str;
}
%>

<%
	ExpressionPropertyEditor editor = (ExpressionPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);
	Object defaultValue = request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	if (pd == null || !(pd instanceof ExpressionProperty)) {
		//定義がExpressionPropertyでなければ表示不可
		return;
	}

	if (editor.getEditor() != null) {
		String path = EntityViewUtil.getJspPath(editor.getEditor(), ViewConst.DESIGN_TYPE_GEM);
		if (path != null) {
			//ExpressionのResultTypeにあわせたeditorで表示
			editor.getEditor().setPropertyName(editor.getPropertyName());
			request.setAttribute(Constants.EDITOR_EDITOR, editor.getEditor());
			if (OutputType.EDIT == type || OutputType.BULKEDIT == type) {
				String editorTypeName = editor.getPropertyName() + "_editorType";
				String editorType = editor.getEditor().getClass().getSimpleName();
%>
<input type="hidden" name="<c:out value="<%=editorTypeName %>"/>" value="<c:out value="<%=editorType %>"/>" >
<%

				//Expressionは表示のみなので、編集モードの場合は表示で出力
				request.setAttribute(Constants.OUTPUT_TYPE, OutputType.VIEW);
				request.setAttribute(Constants.OUTPUT_HIDDEN, true);
			}
%>
<jsp:include page="<%=path %>" />
<%
			if (OutputType.EDIT == type || OutputType.BULKEDIT == type) {
				//後続のために戻しておく
				request.setAttribute(Constants.OUTPUT_TYPE, type);
				request.removeAttribute(Constants.OUTPUT_HIDDEN);
			}
			request.removeAttribute(Constants.EDITOR_EDITOR);
			request.removeAttribute(Constants.EDITOR_PROP_VALUE);
			request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
			return;
		}
	}

	String propName = editor.getPropertyName();

	boolean isMultiple = pd.getMultiplicity() != 1;

	if (OutputType.EDIT == type || OutputType.VIEW == type || OutputType.BULKEDIT == type) {
		//詳細編集 or 詳細表示 or 一括更新編集

		//カスタムスタイル
		String customStyle = "";
		if (type == OutputType.VIEW) {
			if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), entity, propValue);
			}
		} else if (type == OutputType.EDIT) {
			if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
			}
		}

		if (isMultiple) {
%>
<ul class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
			Object[] array = (Object[]) propValue;
			if (array != null) {
				for (Object obj : array) {
					String hValue = obj != null ? obj.toString() : "";
%>
<li>
<c:out value="<%=format(editor.getNumberFormat(), obj) %>"/>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hValue %>"/>">
</li>
<%
				}
			}
%>
</ul>
<%
		} else {
			String hValue = propValue != null ? propValue.toString() : "";

%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>">
<c:out value="<%=format(editor.getNumberFormat(), propValue) %>"/>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hValue %>"/>">
</span>
<%
		}
	} else if (OutputType.SEARCHCONDITION == type) {
		//検索条件
		propName = Constants.SEARCH_COND_PREFIX + propName;

		String[] _propValue = (String[]) propValue;
		String str = "";
		if (_propValue != null && _propValue.length > 0) {
			str = _propValue[0];
		}

		String[] _defaultValue = (String[]) defaultValue;
		String strDefault = "";
		if (_defaultValue != null && _defaultValue.length > 0) {
			strDefault = _defaultValue[0];
		}

		//カスタムスタイル
		String customStyle = "";
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
		}
%>
<input type="text" class="form-size-04 inpbr" style="<c:out value="<%=customStyle%>"/>" value="<c:out value="<%=str%>"/>" name="<c:out value="<%=propName %>"/>" />

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val("<%=StringUtil.escapeJavaScript(strDefault) %>");
	});
<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (typeof val === "undefined" || val == null || val == "") {
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
<%
	} else {
		//検索結果
		if (isMultiple) {
%>
<ul>
<%
			Object[] array = (Object[]) propValue;
			if (array != null) {
				for (Object obj : array) {
%>
<li><c:out value="<%=format(editor.getNumberFormat(), obj) %>"/></li>
<%
				}
			}
%>
</ul>
<%
		} else {
%>
<c:out value="<%=format(editor.getNumberFormat(), propValue) %>"/>
<%
		}
	}
	request.removeAttribute(Constants.EDITOR_EDITOR);
	request.removeAttribute(Constants.EDITOR_PROP_VALUE);
	request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
%>
