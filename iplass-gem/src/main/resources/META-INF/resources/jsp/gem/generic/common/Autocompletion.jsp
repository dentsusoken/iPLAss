<%--
 Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 
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

Object value = request.getAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA);
Entity rootEntity = value instanceof Entity ? (Entity)value : null;

StringBuilder sb = new StringBuilder();
for (AutocompletionProperty prop : setting.getProperties()) {
	if (sb.length() > 0) sb.append(",");
	if (prop.getPropertyName().indexOf(".") != -1) {
		int indexOfDot = prop.getPropertyName().indexOf(".");
		String refPropName = prop.getPropertyName().substring(0, indexOfDot);
		String nestPropName = prop.getPropertyName().substring(indexOfDot + 1, prop.getPropertyName().length());
		if (prop.getReferencePropertyIndex() != null) {
			sb.append("[name='").append(refPropName).append("[").append(prop.getReferencePropertyIndex()).append("].").append(nestPropName).append("']");
		} else {
			sb.append("[name^='").append(refPropName).append("']").append("[name*='").append(nestPropName).append("']");
		}
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
<%
	for (AutocompletionProperty prop : setting.getProperties()) {
		String acPropName = prop.getPropertyName();
		if (prop.getPropertyName().indexOf(".") != -1) {
			//NestTable、ReferenceSection
			int indexOfDot = prop.getPropertyName().indexOf(".");
			String refPropName = prop.getPropertyName().substring(0, indexOfDot);
			String nestPropName = prop.getPropertyName().substring(indexOfDot + 1, prop.getPropertyName().length());
			if (prop.getReferencePropertyIndex() != null) {
				//特定の行、セクション
				String _propName = refPropName + "[" + prop.getReferencePropertyIndex() + "]." + nestPropName;
%>
		sourceVales["<%=_propName%>"] = $("[name='<%=_propName%>']").map(function() {return $(this).val();}).get();
<%
			} else {
				//全行、全セクション
				String _propName = "[name^='" + refPropName + "][name*='" + nestPropName + "']:visible";
%>
		$("<%=_propName%>").each(function() {
			var name = $(this).attr("name");
			sourceVales[name] = $("[name='" + name + "']").map(function() {return $(this).val();}).get();
		});
<%
			}
		} else {
%>
		sourceVales["<%=acPropName%>"] = $("[name='<%=acPropName%>']").map(function() {return $(this).val();}).get();
<%
		}
	}
%>
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
	String key = StringUtil.escapeJavaScript(ws.getRuntimeKey());
%>
<script>
$(function() {
	$(document).on("change", "<%=selector%>", function() {
		var pValue = {};
<%
	for (AutocompletionProperty prop : setting.getProperties()) {
		String acPropName = prop.getPropertyName();
		if (prop.getPropertyName().indexOf(".") != -1) {
			//NestTable、ReferenceSection
			int indexOfDot = prop.getPropertyName().indexOf(".");
			String refPropName = prop.getPropertyName().substring(0, indexOfDot);
			String nestPropName = prop.getPropertyName().substring(indexOfDot + 1, prop.getPropertyName().length());
			if (prop.getReferencePropertyIndex() != null) {
				//特定の行、セクション
				String _propName = refPropName + "[" + prop.getReferencePropertyIndex() + "]." + nestPropName;
%>
		pValue["<%=_propName%>"] = $("[name='<%=_propName%>']").map(function() {return $(this).val();}).get();
<%
			} else {
				//全行、全セクション
				String _propName = "[name^='" + refPropName + "'][name*='" + nestPropName + "']:visible";
%>
		$("<%=_propName%>").each(function() {
			var name = $(this).attr("name");
			pValue[name] = $("[name='" + name + "']").map(function() {return $(this).val();}).get();
		});
<%
			}
		} else {
%>
		pValue["<%=acPropName%>"] = $("[name='<%=acPropName%>']").map(function() {return $(this).val();}).get();
<%
		}
	}
%>
		var name = $(this).attr("name");
		if (name.indexOf("]") != -1) {
			var rowIndex = name.substr(0, name.indexOf("]")).substr(name.indexOf("[") + 1);
			pValue["refIndex"] = rowIndex;
		}
		var propName = "<%=propName%>";
		var cValue = $("[name='" + propName + "']").map(function() {return $(this).val();}).get();
<%	if (rootEntity == null) { %>
		var entity = null;
<% } else { %>
		var entity = {oid: "<%=rootEntity.getOid()%>", version: "<%=rootEntity.getVersion()%>"};
<% } %>
		getAutocompletionValue("<%=GetAutocompletionValueCommand.WEBAPI_NAME%>", "<%=defName%>", "<%=viewName%>", "<%=Constants.VIEW_TYPE_DETAIL%>", propName, "<%=key%>", null, pValue, cValue, entity, function(value) {
<jsp:include page="<%=path %>" />
		});
	});
});
</script>
<%
	request.removeAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE);
}
%>
