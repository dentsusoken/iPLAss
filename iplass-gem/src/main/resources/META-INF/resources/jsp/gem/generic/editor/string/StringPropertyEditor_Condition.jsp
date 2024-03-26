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

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.ExpressionProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.StringProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinitionType"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.EditorValue" %>
<%@ page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>
<%
	StringPropertyEditor editor = (StringPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String propName = Constants.SEARCH_COND_PREFIX + editor.getPropertyName();
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);

	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);

	String value = "";
	if (propValue != null && propValue.length > 0) {
		value = propValue[0];
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/string/StringPropertyAutocompletion.jsp");
	}

	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);

	if (editor.getDisplayType() != StringDisplayType.HIDDEN) {
		//HIDDEN以外

		Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
		if (required == null) required = false;

		String strDefault = "";
		if (defaultValue != null && defaultValue.length > 0) {
			strDefault = defaultValue[0];
		}

		//カスタムスタイル
		String customStyle = "";
		if (editor.getDisplayType() != StringDisplayType.LABEL) {
			if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
			}
		} else {
			if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), null, null);
			}
		}

		if (pd instanceof StringProperty
				|| (pd instanceof ExpressionProperty
						&& ((ExpressionProperty)pd).getResultType() == PropertyDefinitionType.STRING)) {
			//String型のみ、LongTextは検索条件に含めない
			if (editor.getDisplayType() == StringDisplayType.SELECT) {
				String pleaseSelectLabel = "";
				if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
					pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.string.StringPropertyEditor_Condition.pleaseSelect");
				}
%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>">
<option value=""><%= pleaseSelectLabel %></option>
<%
				for (EditorValue tmp : editor.getValues()) {
					String label = EntityViewUtil.getStringPropertySelectTypeLabel(tmp);
					String optStyle = tmp.getStyle() != null ? tmp.getStyle() : "";
					String selected = "";
					if (value.equals(tmp.getValue())) selected = " selected";
%>
<option class="<c:out value="<%=optStyle %>"/>" value="<c:out value="<%=tmp.getValue() %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=label %>" /></option>
<%
				}
%>
</select>

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val("<%=StringUtil.escapeJavaScript(strDefault) %>");
	});
<%
				if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
			} else if (editor.getDisplayType() == StringDisplayType.LABEL) {
				Map<String, List<String>> searchCondMap = (Map<String, List<String>>)request.getAttribute(Constants.SEARCH_COND_MAP);
				String[] _strDefault = ViewUtil.getSearchCondValue(searchCondMap,  Constants.SEARCH_COND_PREFIX + editor.getPropertyName());
				strDefault = _strDefault != null && _strDefault.length > 0 ? _strDefault[0] : strDefault;
				String labelstr = StringUtil.escapeXml10(strDefault, true);
				labelstr = labelstr.replaceAll("\r\n", "<BR>").replaceAll("\n", "<BR>").replaceAll("\r", "<BR>").replaceAll(" ", "&nbsp;");
%>
<span style="<c:out value="<%=customStyle%>"/>">
<c:out value="<%=labelstr %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strDefault %>"/>" />
</span>
<%
			} else {
				//SELECT以外
%>
<input type="text" class="form-size-04 inpbr" style="<c:out value="<%=customStyle%>"/>" value="<c:out value="<%=value %>"/>" name="<c:out value="<%=propName %>"/>" />

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
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
			}
		} else {
			if (editor.getDisplayType() == StringDisplayType.LABEL) {
				Map<String, List<String>> searchCondMap = (Map<String, List<String>>)request.getAttribute(Constants.SEARCH_COND_MAP);
				String[] _strDefault = ViewUtil.getSearchCondValue(searchCondMap,  Constants.SEARCH_COND_PREFIX + editor.getPropertyName());
				strDefault = _strDefault != null && _strDefault.length > 0 ? _strDefault[0] : strDefault;
				String labelstr = StringUtil.escapeXml10(strDefault, true);
				labelstr = labelstr.replaceAll("\r\n", "<BR>").replaceAll("\n", "<BR>").replaceAll("\r", "<BR>").replaceAll(" ", "&nbsp;");
%>
<span style="<c:out value="<%=customStyle%>"/>">
<c:out value="<%=labelstr %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strDefault %>"/>" />
</span>
<%
			} else {
			//LongTextはテキストボックスのみ
%>
<input type="text" class="form-size-04 inpbr" style="<c:out value="<%=customStyle%>"/>" value="<c:out value="<%=value %>"/>" name="<c:out value="<%=propName %>"/>" />
<%
			}
%>
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
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
		}
	} else {
		//HIDDEN
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=value %>"/>"/>
<%
	}
%>

