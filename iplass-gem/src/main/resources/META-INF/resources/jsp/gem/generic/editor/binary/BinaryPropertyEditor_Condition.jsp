<%--
 Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.BinaryPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.BinaryPropertyEditor.BinaryDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.PropertyEditor" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	BinaryPropertyEditor editor = (BinaryPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

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
		value = propValue[0];
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/binary/BinaryPropertyAutocompletion.jsp");
	}

	if (editor.getDisplayType() != BinaryDisplayType.HIDDEN) {
		//HIDDEN以外

		String strDefault = "";
		if (defaultValue != null && defaultValue.length > 0) {
			strDefault = defaultValue[0];
		}

		//カスタムスタイル
		String customStyle = "";
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
		}

		if (editor.getDisplayType() == BinaryDisplayType.LABEL) {
%>
<c:out value="<%=strDefault %>"/>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strDefault %>"/>" />
<%
		} else {
%>
<input type="text" class="form-size-04 inpbr" style="<c:out value="<%=customStyle%>"/>" value="<%=value %>" name="<c:out value="<%=propName %>"/>" />
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
