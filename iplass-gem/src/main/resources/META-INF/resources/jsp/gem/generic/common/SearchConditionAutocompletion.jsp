<%--
 Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
 
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
<%@ page import="org.iplass.mtp.view.generic.common.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.generic.common.GetAutocompletionValueCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
AutocompletionSetting setting = (AutocompletionSetting) request.getAttribute(Constants.AUTOCOMPLETION_SETTING);
String defName = StringUtil.escapeJavaScript((String) request.getAttribute(Constants.AUTOCOMPLETION_DEF_NAME));
String viewName = StringUtil.escapeJavaScript((String) request.getAttribute(Constants.AUTOCOMPLETION_VIEW_NAME));
if (viewName == null) viewName = "";
String propName = StringUtil.escapeJavaScript((String) request.getAttribute(Constants.AUTOCOMPLETION_PROP_NAME));
if (setting == null) return;
String refPropName = (String) request.getAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME);
if (refPropName != null) {
	refPropName = refPropName + ".";
} else {
	refPropName = "";
}

StringBuilder sb = new StringBuilder();
for (AutocompletionProperty prop : setting.getProperties()) {
	if (sb.length() > 0) sb.append(",");
	if (prop.isNestProperty()) {
		sb.append("[name='").append(Constants.SEARCH_COND_PREFIX).append(refPropName).append(prop.getPropertyName()).append("']");
	} else {
		sb.append("[name='").append(Constants.SEARCH_COND_PREFIX).append(prop.getPropertyName()).append("']");
	}

}
String selector = sb.toString();

if (setting instanceof JavascriptAutocompletionSetting) {
	JavascriptAutocompletionSetting js = (JavascriptAutocompletionSetting) setting;
%>
<script>
$(function() {
	$(document).on("change", "<%=selector%>", function() {
		var sourceVales = {};
		<% for (AutocompletionProperty prop : setting.getProperties()) { %>
		<% String acPropName = Constants.SEARCH_COND_PREFIX + prop.getPropertyName(); %>
		sourceVales["<%=prop.getPropertyName()%>"] = $("[name='<%=acPropName%>']").map(function() {return $(this).val();}).get();
		<% } %>
		<%=js.getJavascript()%>
	});
});
</script>
<%
} else if (setting instanceof WebApiAutocompletionSetting) {
	WebApiAutocompletionSetting ws = (WebApiAutocompletionSetting) setting;
	if (ws.getRuntimeKey() == null) return;
	String path = (String) request.getAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH);
	if (path == null) return; //返却値を型に合わせた形に整形するためのjspパス、無ければ何もしない

	String key = StringUtil.escapeJavaScript(ws.getRuntimeKey());
	request.setAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE, Constants.VIEW_TYPE_SEARCH);
%>
<script>
$(function() {
	$(document).on("change", "<%=selector%>", function() {
		var pValue = {};
		<% for (AutocompletionProperty prop : setting.getProperties()) { %>
		<% String acPropName = Constants.SEARCH_COND_PREFIX + prop.getPropertyName(); %>
		pValue["<%=prop.getPropertyName()%>"] = $("[name='<%=acPropName%>']").map(function() {return $(this).val();}).get();
		<% } %>
		var propName = "<%=propName%>";
		var cValue = $("[name='" + propName + "']").map(function() {return $(this).val();}).get();
		getAutocompletionValue("<%=GetAutocompletionValueCommand.WEBAPI_NAME%>", "<%=defName%>", "<%=viewName%>", "<%=Constants.VIEW_TYPE_SEARCH%>", propName, "<%=key%>", null, pValue, cValue, null, function(value) {
<jsp:include page="<%=path %>" />
		});
	});
});
</script>
<%
	request.removeAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE);
}
%>
