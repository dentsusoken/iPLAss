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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.impl.util.ConvertUtil"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.GenericEntity"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.view.generic.FormView" %>
<%@ page import="org.iplass.mtp.view.generic.EntityViewManager"%>
<%@ page import="org.iplass.mtp.view.generic.editor.PropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.JoinPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData" %>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%!
	ReferencePropertyEditor getRefEditor(String defName, String viewName, String propName, String viewType, Integer refSectionIndex, Entity entity) {
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		PropertyEditor editor = evm.getPropertyEditor(defName, viewType, viewName, propName, refSectionIndex, entity);
		if (editor instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor rpe = (ReferencePropertyEditor) editor;
			return rpe;
		} else if (editor instanceof JoinPropertyEditor) {
			JoinPropertyEditor jpe = (JoinPropertyEditor) editor;
			if (jpe.getEditor() instanceof ReferencePropertyEditor) {
				return ((ReferencePropertyEditor) jpe.getEditor());
			}
		}

		return null;
	}

	String getDisplayLabelItem(String defName, String viewName, String propName, String viewType, Integer refSectionIndex, Entity entity) {
		FormView form = ViewUtil.getFormView(defName, viewName, viewType);
		if (form == null) return null;
		
		ReferencePropertyEditor editor = getRefEditor(defName, viewName, propName, viewType, refSectionIndex, entity);
		if (editor != null) return editor.getDisplayLabelItem();

		return null;
	}

	String getUniqueItem(String defName, String viewName, String propName, String viewType, Integer refSectionIndex, Entity entity) {
		FormView form = ViewUtil.getFormView(defName, viewName, viewType);
		if (form == null) return null;

		ReferencePropertyEditor editor = getRefEditor(defName, viewName, propName, viewType, refSectionIndex, entity);
		if (editor != null) return editor.getUniqueItem();

		return null;
	}
%>
<%
	//データ取得
	DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);
	String modalTarget = request.getParameter(Constants.MODAL_TARGET);
	//参照先エンティティ登録用
	String parentDefName = request.getParameter(Constants.PARENT_DEFNAME);
	String parentViewName = request.getParameter(Constants.PARENT_VIEWNAME);
	String parentPropName = request.getParameter(Constants.PARENT_PROPNAME);
	String viewType = request.getParameter(Constants.VIEW_TYPE);
	String refSectionIndex = request.getParameter(Constants.REF_SECTION_INDEX);
	String entityOid = request.getParameter(Constants.DISPLAY_SCRIPT_ENTITY_OID);
	String entityVersion = request.getParameter(Constants.DISPLAY_SCRIPT_ENTITY_VERSION);

	if (modalTarget == null) modalTarget = "";
	else modalTarget = StringUtil.escapeHtml(modalTarget);

	Integer _refSectionIndex = null;
	if (StringUtil.isNotBlank(refSectionIndex)) {
		_refSectionIndex = new Integer(refSectionIndex);
	}

	String title = ViewUtil.getDispTenantName();

	Entity parent = null;
	if (StringUtil.isNotBlank(parentDefName) && StringUtil.isNotBlank(entityOid) && StringUtil.isNotBlank(entityVersion)) {
		parent = new GenericEntity(parentDefName);
		parent.setOid(entityOid);
		parent.setVersion(Long.valueOf(entityVersion));
	}

	String dispPropLabel = null;
	if (StringUtil.isNotBlank(parentDefName) && StringUtil.isNotBlank(parentPropName)) {
		dispPropLabel = getDisplayLabelItem(parentDefName, parentViewName, parentPropName, viewType, _refSectionIndex, parent);
	}
	if (dispPropLabel == null) {
		dispPropLabel = Entity.NAME;
	}
	
	String uniqueItem = null;
	if (StringUtil.isNotBlank(parentDefName) && StringUtil.isNotBlank(parentPropName)) {
		uniqueItem = getUniqueItem(parentDefName, parentViewName, parentPropName, viewType, _refSectionIndex, parent);
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge" />
<title><c:out value="<%= title %>"/></title>
<%@include file="../../../layout/resource/simpleResource.jsp" %>
<%@include file="../../../layout/resource/skin.jsp" %>
<%@include file="../../../layout/resource/theme.jsp" %>
<%@include file="../../../layout/resource/langfont.jsp" %>
<%@include file="../../../layout/resource/tenant.jsp" %>
<script type="text/javascript">
var key = "<%=modalTarget%>";
var modalTarget = key != "" ? key : null;
$(function() {
	var entity = {
<% if (uniqueItem != null) {%>
		uniqueValue: "<%=ConvertUtil.convertToString(data.getEntity().getValue(uniqueItem))%>",
<% } %>
		oid:"<%=StringUtil.escapeJavaScript(data.getEntity().getOid())%>",
		version:"<%=data.getEntity().getVersion()%>",
		name:"<%=StringUtil.escapeJavaScript((String)data.getEntity().getValue(dispPropLabel))%>"
	};
	var func = null;
	var windowManager = document.rootDocument.scriptContext["windowManager"];
	if (modalTarget && windowManager && windowManager[document.targetName]) {
		var win = windowManager[modalTarget];
		func = win.scriptContext["editReferenceCallback"];
	} else {
		func = parent.document.scriptContext["editReferenceCallback"];
	}
	if (func && $.isFunction(func)) {
		func.call(this, entity);
	}
});
</script>
</head>
<body class="modal-body">
</body>
</html>
