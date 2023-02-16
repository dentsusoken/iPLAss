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
String parentDefName = StringUtil.escapeJavaScript((String) request.getAttribute(Constants.AUTOCOMPLETION_DEF_NAME));
if (parentDefName == null) parentDefName = "";
String parentViewName = StringUtil.escapeJavaScript((String) request.getAttribute(Constants.AUTOCOMPLETION_VIEW_NAME));
if (parentViewName == null) parentViewName = "";

Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);
String entityOid = null;
if (rootEntity != null && rootEntity.getOid() != null) entityOid = rootEntity.getOid();
Long entityVersion = null;
if (rootEntity != null && rootEntity.getVersion() != null) entityVersion = rootEntity.getVersion();
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
%>
function appendIfHasMore(propName, value) {
	<%-- 他の自動補完機能に合わせて、既存データで取得したデータの件数以上のものを捨てないので、配列の最後に保存します。 --%>
	$("[name='" + propName +"']:gt(" + (value.length - 1) +")").each(function(index) {
		var key = $(this).val();
		var tmp = keySplit(key);
<%
	if (editor.getDisplayType() == ReferenceDisplayType.LINK
		|| editor.getDisplayType() == ReferenceDisplayType.TREE
		|| editor.getDisplayType() == ReferenceDisplayType.UNIQUE) {
%>
		var label = "<%=editor.getDisplayLabelItem() == null ? "name" : editor.getDisplayLabelItem()%>";
		var linkId = propName + "_" + tmp.oid;
		tmp[label] = $("[data-linkId='" + linkId +"']").text();
<%
	}

	if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE) {
%>
		var unique = "<%=editor.getUniqueItem() %>";
		tmp[unique] = $(this).parent().children("span.unique-key").children("input[type='text']").val();
<%
	}
%>
		value.push(tmp);
	});
}
<%
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
if (value.length > 0 && value[0] != null) {
	appendIfHasMore(propName, value);
}
var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
<%
	if (editor.getDisplayType() == ReferenceDisplayType.LINK) {
%>
var callback = function() {
	toggleRefInsertBtn("ul_" + _propName, multiplicity, "ins_btn_" + _propName);
};

if (value.length > 0 && value[0] != null) {
	if (value.length > 0) {
		$("#ul_" + _propName).children().remove();
	}
	for (var i = 0; i < value.length; i++) {
		var key = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
		var label = <%=editor.getDisplayLabelItem() == null ? "value[i].name" : "value[i]." + editor.getDisplayLabelItem() %>;
		addReference("li_" + propName + key, "<%=viewAction%>", "<%=defName%>", key, label, propName, "ul_" + _propName, <%=refEdit%>, callback, "<%=parentDefName%>", "<%=parentViewName%>", "<%=viewType%>", null, "<%=entityOid%>", "<%=entityVersion%>");
	}
	toggleRefInsertBtn("ul_" + _propName, multiplicity, "ins_btn_" + _propName);
} else {
	$("#ul_" + propName + " li:first").remove();
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.CHECKBOX) {
%>
if (value.length > 0 && value[0] != null) {
	
	for (var i = 0; i < value.length; i++) {
		var key = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
		$("[name='" + propName + "'][value='" + key + "']").click();
	}
} else {
	// 自動補完の値が空の場合、未選択とする
	$("[name='" + propName + "']").prop('checked', false);
	$("[name='" + propName + "']").prev('span').removeClass('checked');
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.SELECT) {
%>
if (value.length > 0 && value[0] != null) {
	var oid = [];
	for (var i = 0; i < value.length; i++) {
		var key = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
		oid.push(key);
	}
	$("[name='" + propName + "']").val(oid);
} else {
	// 自動補完の値が空の場合、未選択とする
	$("[name='" + propName + "']").val("");
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
%>
for (var i = 0; i < value.length; i++) {
	$("select[name='" + propName + "']").refCombo({
		reset:true,
		oid:value[i] == null ? "" : value[i].oid
	});
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
%>
var callback = function() {
	toggleRefInsertBtn("ul_" + _propName, multiplicity, "ins_btn_" + _propName);
};

if (value.length > 0 && value[0] != null) {
	if (value.length > 0) {
		$("#ul_" + _propName).children().remove();
	}
	for (var i = 0; i < value.length; i++) {
		var key = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
		var label = <%=editor.getDisplayLabelItem() == null ? "value[i].name" : "value[i]." + editor.getDisplayLabelItem() %>;
		addReference("li_" + propName + key, "<%=viewAction%>", "<%=defName%>", key, label, propName, "ul_" + _propName, <%=refEdit%>, callback, "<%=parentDefName%>", "<%=parentViewName%>", "<%=viewType%>", null, "<%=entityOid%>", "<%=entityVersion%>");
	}
	toggleRefInsertBtn("ul_" + _propName, multiplicity, "ins_btn_" + _propName);
} else {
	$("#ul_" + propName + " li:first").remove();
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE) {
%>
var callback = function() {
	<%-- Dummy行が存在するので、 multiplicity + 1 --%>
	toggleRefInsertBtn("ul_" + _propName, multiplicity + 1, "id_addBtn_" + _propName);
};

if (value.length > 0 && value[0] != null) {

	if (value.length > 0) {
		$("#ul_" + _propName).children(":visible").remove();
	}
	for (var i = 0; i < value.length; i++) {
		var key = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
		var label = <%=editor.getDisplayLabelItem() == null ? "value[i].name" : "value[i]." + editor.getDisplayLabelItem() %>;
		var unique = <%="value[i]." + editor.getUniqueItem() %>;
		<%-- Dummy行が存在するので、 multiplicity + 1 --%>
		addUniqueReference("<%=viewAction %>", key, label, unique, "<%=defName %>", propName, multiplicity + 1, "ul_" + _propName, "id_li_" + _propName + "Dummmy", <%=refEdit%>, "id_count_" + _propName, callback ,"<%=parentDefName%>", "<%=parentViewName%>", "<%=viewType%>", null, "<%=entityOid%>", "<%=entityVersion%>");
	}
	<%-- Dummy行が存在するので、 multiplicity + 1 --%>
	toggleRefInsertBtn("ul_" + _propName, multiplicity + 1, "id_addBtn_" + _propName);
} else {
	$("#ul_" + propName + " li:first").remove();
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.LABEL) {
%>
var newContent = '';

// 自動補完の値が空の場合
if (!value || (value.length == 1 && !value[0])) {
	newContent = "" + ' <input type="hidden" name="' + propName + '" value="">';
	$("[name='" + propName + "']:first").parent().html(newContent);
} else {
	for (var i = 0; i < value.length; i++) {
		var liId = "li_" + propName + i;
		var label = <%=editor.getDisplayLabelItem() == null ? "value[i].name" : "value[i]." + editor.getDisplayLabelItem() %>;
		var _value = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
		newContent = newContent + '<li id="' + liId + '" >' + label
				+ '<input type="hidden" name="' + propName + '" value="' + _value + '">' + '</li>';
	}
	
	if (value.length > 0) {
		$("[name='data-label-" + propName + "']").html(newContent);
	}
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.HIDDEN) {
%>

var propLength = $('[name=' + propName + ']').length;
var newContent = '';
for (i =  0; i < value.length; i++) {
	var hiddenValue = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
	if (i > propLength - 1) {
		newContent = newContent + '<input type="hidden" name="' + propName + '" value="' + hiddenValue + '">';
		continue;
	}
	$("[name='" + propName + "']:eq(" + i + ")").val(hiddenValue);
}

// 項目数が増える場合に追加する
if (propLength && value.length > propLength) {
	$("[name='" + propName + "']:eq(" + (propLength - 1) + ")").after($(newContent));
// 項目に値が無い場合は新規に追加する
} else if (!propLength) {
	$(".hidden-input-area:first").append($(newContent));
}

<%

	}
} else {
	// 検索画面
%>
if (!(value instanceof Array)) {
	value = [value];
}
if (value.length > 0) {
	appendIfHasMore("sc_" + propName, value);
}
var _propName = propName.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/\./g, "\\.");
<%
	if (editor.getDisplayType() == ReferenceDisplayType.SELECT) {
%>
var oid = [];
if (value.length > 0 && value[0] != null) {
	for (var i = 0; i < value.length; i++) {
		oid.push(value[i].oid);
	}
	$("[name='sc_" + propName + "']").val(oid);
} else {
	// 自動補完の値が空の場合、未選択とする
	$("[name='sc_" + propName + "']").val("");
}

<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.CHECKBOX) {
%>
if (value.length > 0 && value[0] != null) {
	for (var i = 0; i < value.length; i++) {
		$("[name='sc_" + propName + "'][value='" + value[i].oid + "']").click();
	}
} else {
	// 自動補完の値が空の場合、未選択とする
	$("[name='sc_" + propName + "']").prop('checked', false);
	$("[name='sc_" + propName + "']").prev('span').removeClass('checked');
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
%>
for (var i = 0; i < value.length; i++) {
	$("select[name='sc_" + propName + "']").refCombo({
		reset:true,
		oid:value[i] == null ? "" : value[i].oid
	});
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.LINK) {

		if (editor.isSingleSelect()) {
%>
value = value.length > 0 ? value.slice(0, 1) : [];
<%
		}
%>
if (value.length > 0 && value[0] != null) {

	if (value.length > 0) {
		$("#ul_sc_" + _propName).children().remove();
	}
	for (var i = 0; i < value.length; i++) {
		var key = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
		var label = <%=editor.getDisplayLabelItem() == null ? "value[i].name" : "value[i]." + editor.getDisplayLabelItem() %>;
		addReference("li_sc_" + propName + key, "<%=viewAction%>", "<%=defName%>", key, label, "sc_" + propName, "ul_sc_" + _propName, false);
	}
} else {
	$("#ul_sc_" + propName + " li:first").remove();
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
%>
if (value.length > 0 && value[0] != null) {
	if (value.length > 0) {
		$("#ul_sc_" + _propName).children().remove();
	}
	for (var i = 0; i < value.length; i++) {
		var key = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
		var label = <%=editor.getDisplayLabelItem() == null ? "value[i].name" : "value[i]." + editor.getDisplayLabelItem() %>;
		addReference("li_sc_" + propName + key, "<%=viewAction%>", "<%=defName%>", key, label, "sc_" + propName, "ul_sc_" + _propName, false);
	}
} else {
	$("#ul_sc_" + propName + " li:first").remove();
}
<%
	} else if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE) {

		if (editor.isSingleSelect()) {
%>
value = value.length > 0 ? value.slice(0, 1) : [];
<%
		}
%>
if (value.length > 0 && value[0] != null) {
	if (value.length > 0) {
		$("#ul_sc_" + _propName).children(":visible").remove();
	}
	for (var i = 0; i < value.length; i++) {
		var key = value[i].oid + "_" + (value[i].version ? value[i].version : "0");
		var label = <%=editor.getDisplayLabelItem() == null ? "value[i].name" : "value[i]." + editor.getDisplayLabelItem() %>;
		var unique = <%="value[i]." + editor.getUniqueItem() %>;
		<%-- Dummy行が存在するので、 multiplicity + 1 --%>
		addUniqueReference("<%=viewAction %>", key, label, unique, "<%=defName %>", "sc_" + propName, -1, "ul_sc_" + _propName, "id_li_sc_" + _propName + "Dummmy", false, "id_count_sc_" + _propName);
	}
} else {
	$("#ul_sc_" + propName + " li:first").remove();
}
<%
	}
}
%>