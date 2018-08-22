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

<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.detail.DetailViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%
ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.AUTOCOMPLETION_EDITOR);
String viewType = StringUtil.escapeJavaScript((String) request.getAttribute(Constants.AUTOCOMPLETION_VIEW_TYPE));
Integer multiplicity = (Integer) request.getAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
if (multiplicity == null) multiplicity = 1;
//呼び出し元は/common/Autocompletion.jsp、以降はWebApiの結果を反映する部分のJavascript、結果の変数はvalue

String contextPath = TemplateUtil.getTenantContextPath();
String defName = editor.getObjectName();
String urlPath = ViewUtil.getParamMappingPath(defName, editor.getViewName());
String viewAction = "";
if (StringUtil.isNotBlank(editor.getViewrefActionName())) {
	viewAction = contextPath + "/" + editor.getViewrefActionName() + urlPath;
} else {
	viewAction = contextPath + "/" + DetailViewCommand.REF_VIEW_ACTION_NAME + urlPath;
}

if (Constants.VIEW_TYPE_DETAIL.equals(viewType)) {
	// 詳細画面
	boolean refEdit = editor.isEditableReference();
%>
var multiplicity = <%=multiplicity%>;
if (multiplicity == 1) {
	if (value instanceof Array) {
		value = value.length > 0 ? value.slice(0, 1) : [];
	} else {
		value = [value];
	}
} else if (multiplicity == -1) {
	if (!(value instanceof Array)) {
		value = [value];
	}
} else {
	if (value instanceof Array) {
		if (value.length > multiplicity) {
			value = value.slice(0, multiplicity);
		}
	} else {
		value = [value];
	}
}
<%
	if (editor.getDisplayType() == ReferenceDisplayType.LINK) {
%>
for (var i = 0; i < value.length; i++) {
	var key = value[i].oid + "_" + (value[i].version ? value[i].varsion : "0");
	addReference("li_" + propName + key, "<%=viewAction%>", "<%=defName%>", key, value[i].name, propName, "ul_" + propName, <%=refEdit%>);
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.CHECKBOX) {
%>
for (var i = 0; i < value.length; i++) {
	var key = value[i].oid + "_" + (value[i].version ? value[i].varsion : "0");
	$("[name='" + propName + "'][value='" + key + "']").click();
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.SELECT) {
%>
var oid = [];
for (var i = 0; i < value.length; i++) {
	var key = value[i].oid + "_" + (value[i].version ? value[i].varsion : "0");
	oid.push(key);
}
$("[name='" + propName + "']").val(oid);
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
%>
for (var i = 0; i < value.length; i++) {
	$("select[name='" + propName + "']").refCombo({
		reset:true,
		oid:value[i].oid
	});
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
%>
for (var i = 0; i < value.length; i++) {
	var key = value[i].oid + "_" + (value[i].version ? value[i].varsion : "0");
	addReference("li_" + propName + key, "<%=viewAction%>", "<%=defName%>", key, value[i].name, propName, "ul_" + propName, <%=refEdit%>);
}
<%
	}
} else {
	// 検索画面
%>
if (!(value instanceof Array)) {
	value = [value];
}
<%
	if (editor.getDisplayType() == ReferenceDisplayType.SELECT) {
%>
var oid = [];
for (var i = 0; i < value.length; i++) {
	oid.push(value[i].oid);
}
$("[name='sc_" + propName + "']").val(oid);
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.CHECKBOX) {
%>
for (var i = 0; i < value.length; i++) {
	$("[name='sc_" + propName + "'][value='" + value[i].oid + "']").click();
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
%>
for (var i = 0; i < value.length; i++) {
	$("select[name='sc_" + propName + "']").refCombo({
		reset:true,
		oid:value[i].oid
	});
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.LINK) {
%>
for (var i = 0; i < value.length; i++) {
	var key = value[i].oid + "_" + (value[i].version ? value[i].varsion : "0");
	addReference("li_sc_" + propName + key, "<%=viewAction%>", "<%=defName%>", key, value[i].name, "sc_" + propName, "ul_sc_" + propName, false);
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
%>
for (var i = 0; i < value.length; i++) {
	var key = value[i].oid + "_" + (value[i].version ? value[i].varsion : "0");
	addReference("li_sc_" + propName + key, "<%=viewAction%>", "<%=defName%>", key, value[i].name, "sc_" + propName, "ul_sc_" + propName, false);
}
<%
	}
}
%>