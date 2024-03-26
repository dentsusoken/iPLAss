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
String refPropName = (String) request.getAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME);

Object value = request.getAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA);
Entity rootEntity = value instanceof Entity ? (Entity)value : null;

StringBuilder sb = new StringBuilder();
for (AutocompletionProperty prop : setting.getProperties()) {
	if (sb.length() > 0) sb.append(",");
	if (prop.isNestProperty()) {
		//NestTableの場合、先頭がreferenece名かをチェック
		sb.append("[name^='").append(refPropName + "\\[").append("']");
		sb.append("[name$='.").append(prop.getPropertyName()).append("']");
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
		var $this = $(this);
		var prefix = null;
		if ($this.parents(".tbl-reference").length > 0) {
			var name = $this.attr("name");
			if (name.indexOf(".") != -1) {
				prefix = name.substr(0, name.indexOf(".") + 1);
			}
		}

		var sourceVales = {};

<%	for (AutocompletionProperty prop : setting.getProperties()) {
		String acPropName = prop.getPropertyName();
		if (prop.isNestProperty()) { 
%>
		if (prefix != null) {
			sourceVales[prefix + "<%=acPropName%>"] = $("[name='" + prefix + "<%=acPropName%>']").map(function() {return $(this).val();}).get();
		} else {
			sourceVales["<%=refPropName%>.<%=acPropName%>"] = $("[name*='.<%=acPropName%>']").map(function() {return $(this).val();}).get();
		}
<% 		} else { %>
		sourceVales["<%=acPropName%>"] = $("[name='<%=acPropName%>']").map(function() {return $(this).val();}).get();
<% 		}
	} 
%>
<%=	js.getJavascript()%>
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
		var $this = $(this);
		var prefix = null;
		if ($this.parents(".tbl-reference").length > 0) {
			var name = $this.attr("name");
			if (name.indexOf(".") != -1) {
				prefix = name.substr(0, name.indexOf(".") + 1);
			}
		}

		var pValue = {};
<% 	for (AutocompletionProperty prop : setting.getProperties()) {
		String acPropName = prop.getPropertyName();
		if (prop.isNestProperty()) { 
%>
		if (prefix != null) {
			pValue[prefix + "<%=acPropName%>"] = $("[name='" + prefix + "<%=acPropName%>']").map(function() {return $(this).val();}).get();
		} else {
			pValue["<%=refPropName%>.<%=acPropName%>"] = $("[name*='.<%=acPropName%>']").map(function() {return $(this).val();}).get();
		}
<% 		} else { %>
		pValue["<%=acPropName%>"] = $("[name='<%=acPropName%>']").map(function() {return $(this).val();}).get();
<% 		}
	}

	if (rootEntity == null) { 
%>
		var entity = null;
<% 	} else { %>
		var entity = {oid: "<%=rootEntity.getOid()%>", version: "<%=rootEntity.getVersion()%>"};
<%	} %>

		if (prefix != null) {
			var refIndex = prefix.substr(0, prefix.indexOf("]")).substr(prefix.indexOf("[") + 1);
			pValue["refIndex"] = refIndex;
			var propName = prefix + "<%=propName%>";
			var _propName = prefix.replace("[" + refIndex + "]", "") + "<%=propName%>";
			var cValue = $("[name='" + propName + "']").map(function() {return $(this).val();}).get();
			getAutocompletionValue("<%=GetAutocompletionValueCommand.WEBAPI_NAME%>", "<%=defName%>", "<%=viewName%>", "<%=Constants.VIEW_TYPE_DETAIL%>", _propName, "<%=key%>", null, pValue, cValue, entity, function(value) {
<jsp:include page="<%=path %>" />
			});
		} else {
			$("[id^='id_tr_<%=refPropName%>']:visible").each(function() {
				var id = $(this).attr("id");
				var refIndex = id.substr("id_tr_<%=refPropName%>".length);
				pValue["refIndex"] = refIndex;
				var propName = "<%=refPropName%>[" + refIndex + "].<%=propName%>";
				var cValue = $("[name='" + propName + "']").map(function() {return $(this).val();}).get();
				getAutocompletionValue("<%=GetAutocompletionValueCommand.WEBAPI_NAME%>", "<%=defName%>", "<%=viewName%>", "<%=Constants.VIEW_TYPE_DETAIL%>", "<%=refPropName%>.<%=propName%>", "<%=key%>", null, pValue, cValue, entity, function(value) {
<jsp:include page="<%=path %>" />
				});
			});

		}
	});
});
</script>
<%
	request.removeAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE);
}
%>
