<%--
 Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.

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
<%@ page import="org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinitionManager"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.PropertyEditor" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.definition.DefinitionSummary"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%
	PropertyEditor editor = (PropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);
	String propName = Constants.SEARCH_COND_PREFIX + editor.getPropertyName();
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	String strValue = "";
	if (propValue != null && propValue.length > 0) {
		strValue = propValue[0];
	}
	String strDefault = "";
	if (defaultValue != null && defaultValue.length > 0) {
		strDefault = defaultValue[0];
	}

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.authpolicyselect.AuthenticationPolicySelectPropertyEditor_Condition.pleaseSelect");
	}

	AuthenticationPolicyDefinitionManager manager = ManagerLocator.getInstance().getManager(AuthenticationPolicyDefinitionManager.class);
	List<DefinitionSummary> definitionNames = manager.definitionSummaryList();
%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr">
<option value=""><%= pleaseSelectLabel %></option>
<%
	for (DefinitionSummary def : definitionNames) {
		String label = def.getDisplayName() != null ? def.getDisplayName() : def.getName();
		String displayName = TemplateUtil.getMultilingualString(label, manager.get(def.getName()).getLocalizedDisplayNameList());
		String selected = strValue.equals(def.getName()) ? " selected" : "";
%>
<option value="<c:out value="<%=def.getName() %>"/>" <%=selected %>><c:out value="<%=displayName %>" /></option>
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
