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

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="org.iplass.mtp.entity.Entity" %>
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
Integer refSectionIndex = (Integer) request.getAttribute(Constants.AUTOCOMPLETION_REF_SECTION_INDEX);
String refPropName = (String) request.getAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME);
String refIndexedPropName = "";
if (refPropName != null) {
	if (refSectionIndex != null) {
		refIndexedPropName = refPropName + "[" + refSectionIndex + "].";
	} else {
		refIndexedPropName = refPropName + ".";
	}
	refPropName = refPropName + ".";
} else {
	refPropName = "";
}

Object value = request.getAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA);
Entity rootEntity = value instanceof Entity ? (Entity)value : null;

StringBuilder sb = new StringBuilder();
for (AutocompletionProperty prop : setting.getProperties()) {
	if (sb.length() > 0) sb.append(",");
	if (prop.isNestProperty()) {
		sb.append("[name='").append(refIndexedPropName).append(prop.getPropertyName()).append("']");
	} else {
		sb.append("[name='").append(prop.getPropertyName()).append("']");
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
		<% String acPropName = prop.isNestProperty() ? refIndexedPropName + prop.getPropertyName() : prop.getPropertyName(); %>
		sourceVales["<%=acPropName%>"] = $("[name='<%=acPropName%>']").map(function() {return $(this).val();}).get();
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

	request.setAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE, Constants.VIEW_TYPE_DETAIL);
	String index = refSectionIndex != null ? refSectionIndex.toString() : "null";
	String key = StringUtil.escapeJavaScript(ws.getRuntimeKey());
%>
<script>
$(function() {
	$(document).on("change", "<%=selector%>", function() {
		var pValue = {};
		<% for (AutocompletionProperty prop : setting.getProperties()) { %>
		<% String acPropName = prop.isNestProperty() ? refIndexedPropName + prop.getPropertyName() : prop.getPropertyName(); %>
		pValue["<%=acPropName%>"] = $("[name='<%=acPropName%>']").map(function() {return $(this).val();}).get();
		<% } %>
		var propName = "<%=refIndexedPropName + propName%>";
		var cValue = $("[name='" + propName + "']").map(function() {return $(this).val();}).get();
<% if (rootEntity == null) { %>
		var entity = null;
<% } else { %>
		var entity = {oid: "<%=rootEntity.getOid()%>", version: "<%=rootEntity.getVersion()%>"};
<% } %>
		getAutocompletionValue("<%=GetAutocompletionValueCommand.WEBAPI_NAME%>", "<%=defName%>", "<%=viewName%>", "<%=Constants.VIEW_TYPE_DETAIL%>", "<%=refPropName + propName%>", "<%=key%>", <%=index%>, pValue, cValue, entity, function(value) {
<jsp:include page="<%=path %>" />
		});
	});
});
</script>
<%
	request.removeAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE);
}
%>
