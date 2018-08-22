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

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@page import="org.iplass.mtp.util.StringUtil"%>
<%@page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@page import="org.iplass.mtp.view.generic.editor.BooleanPropertyEditor" %>
<%@page import="org.iplass.mtp.view.generic.editor.BooleanPropertyEditor.BooleanDisplayType" %>
<%@page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@page import="org.iplass.gem.command.Constants" %>
<%@page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@page import="org.iplass.gem.command.ViewUtil" %>
<%
	BooleanPropertyEditor editor = (BooleanPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	String propName = Constants.SEARCH_COND_PREFIX + editor.getPropertyName();
	String trueLabel = TemplateUtil.getMultilingualString(editor.getTrueLabel(), editor.getLocalizedTrueLabelList());
	if (StringUtil.isEmpty(trueLabel)) {
		trueLabel = TemplateUtil.getMultilingualString(
				GemResourceBundleUtil.resourceString("generic.editor.boolean.BooleanPropertyEditor_Condition.enable"),
				GemResourceBundleUtil.resourceList("generic.editor.boolean.BooleanPropertyEditor_Condition.enable"));
	}
	String falseLabel = TemplateUtil.getMultilingualString(editor.getFalseLabel(), editor.getLocalizedFalseLabelList());
	if (StringUtil.isEmpty(falseLabel)) {
		falseLabel = TemplateUtil.getMultilingualString(
				GemResourceBundleUtil.resourceString("generic.editor.boolean.BooleanPropertyEditor_Condition.invalid"),
				GemResourceBundleUtil.resourceList("generic.editor.boolean.BooleanPropertyEditor_Condition.invalid"));
	}

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.boolean.BooleanPropertyEditor_Condition.pleaseSelect");
	}

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
	}

	String matchStr = editor.getDisplayType() == BooleanDisplayType.SELECT ? " selected" : " checked";
	String checked1 = "";
	String checked2 = "";
	String checked3 = matchStr;
	if (propValue != null && propValue.length > 0 && "true".equals(propValue[0])) {
		checked1 = matchStr;
		checked3 = "";
	}
	if (propValue != null && propValue.length > 0 && "false".equals(propValue[0])) {
		checked2 = matchStr;
		checked3 = "";
	}
	String defaultCheckValue = "";
	if (defaultValue != null && defaultValue.length > 0) {
		if ("true".equals(defaultValue[0])) {
			defaultCheckValue = "true";
		} else if ("false".equals(defaultValue[0])) {
			defaultCheckValue = "false";
		}
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/boolean/BooleanPropertyAutocompletion.jsp");
	}

	if (editor.getDisplayType() == BooleanDisplayType.SELECT) {
%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>" size="1">
<option value="" <%=checked3 %>><%=pleaseSelectLabel %></option>
<option value="true" <%=checked1 %>><c:out value="<%=trueLabel %>"/></option>
<option value="false" <%=checked2 %>><c:out value="<%=falseLabel %>"/></option>
</select>
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val("<%=StringUtil.escapeJavaScript(defaultCheckValue) %>");
	});
<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
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
%>
<ul class="list-radio-01">
<li>
<label for="select-radio-<c:out value="<%=propName %>"/>01" style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=trueLabel %>"/>" >
<input id="select-radio-<c:out value="<%=propName %>"/>01" name="<c:out value="<%=propName %>"/>" class="radio" type="radio" value="true" <%=checked1 %>><c:out value="<%=trueLabel %>"/>
</label>
</li>
<li>
<label for="select-radio-<c:out value="<%=propName %>"/>02" style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=falseLabel %>"/>" >
<input id="select-radio-<c:out value="<%=propName %>"/>02" name="<c:out value="<%=propName %>"/>" class="radio" type="radio" value="false"<%=checked2 %>><c:out value="<%=falseLabel %>"/>
</label>
</li>
<li>
<label for="select-radio-<c:out value="<%=propName %>"/>03" style="<c:out value="<%=customStyle%>"/>" title="${m:rs('mtp-gem-messages', 'generic.editor.boolean.BooleanPropertyEditor_Condition.unspecified')}" >
<input id="select-radio-<c:out value="<%=propName %>"/>03" name="<c:out value="<%=propName %>"/>" class="radio" type="radio" value="" <%=checked3 %>>${m:rs("mtp-gem-messages", "generic.editor.boolean.BooleanPropertyEditor_Condition.unspecified")}
</label>
</li>
</ul>

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$(":radio[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val(["<%=defaultCheckValue %>"]);
		$(":radio[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "'][value='<%=StringUtil.escapeJavaScript(defaultCheckValue) %>']").trigger("click");
	});
<%
	if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":radio[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']:checked").val();
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
	}
%>
