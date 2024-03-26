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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.definition.DefinitionSummary"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TemplatePropertyEditor" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%!
	DefinitionSummary getValue(List<DefinitionSummary> list, String value) {
		if (value == null) return null;
		for (DefinitionSummary tmp : list) {
			if (value.equals(tmp.getName())) {
				return tmp;
			}
		}
		return null;
	}%>
<%
	TemplatePropertyEditor editor = (TemplatePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);
	Integer colNum = (Integer) request.getAttribute(Constants.COL_NUM);

	Boolean isVirtual = (Boolean) request.getAttribute(Constants.IS_VIRTUAL);
	if (isVirtual == null) isVirtual = false;

	AuthenticationPolicyDefinitionManager manager = ManagerLocator.getInstance().getManager(AuthenticationPolicyDefinitionManager.class);
	List<DefinitionSummary> definitionNames = manager.definitionSummaryList();

	boolean isInsert = Constants.EXEC_TYPE_INSERT.equals(execType);
	String propName = editor.getPropertyName();
	AuthContext auth = AuthContext.getCurrentContext();
	boolean isEditable = true;
	if (isVirtual) {
		isEditable = true;//仮想プロパティは権限チェック要らない
	} else {
		if(isInsert) {
			isEditable = auth.checkPermission(new EntityPropertyPermission(defName, pd.getName(), EntityPropertyPermission.Action.CREATE));
		} else {
			isEditable = auth.checkPermission(new EntityPropertyPermission(defName, pd.getName(), EntityPropertyPermission.Action.UPDATE));
		}
	}
	boolean updatable = ((pd == null || pd.isUpdatable()) || isInsert) && isEditable;

	boolean isMultiple = pd.getMultiplicity() != 1;

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.authpolicyselect.AuthenticationPolicySelectPropertyEditor_Edit.pleaseSelect");
	}

	//詳細編集
	String str = propValue instanceof String ? (String) propValue : "";
	if (updatable) {
%>
<select name="<c:out value="<%=propName %>"/>" size="1" class="form-size-02 inpbr">
<option value=""><%= pleaseSelectLabel %></option>
<%
		for (DefinitionSummary def : definitionNames) {
			String selected = str.equals(def.getName()) ? " selected" : "";
			String label = def.getDisplayName() != null ? def.getDisplayName() : def.getName();
			String displayName = TemplateUtil.getMultilingualString(label, manager.get(def.getName()).getLocalizedDisplayNameList());
%>
<option value="<c:out value="<%=def.getName() %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=displayName %>" /></option>
<%
		}
%>
</select>
<%
	} else {
		DefinitionSummary def = getValue(definitionNames, str);
		String label = def != null ? def.getDisplayName() != null ? def.getDisplayName() : def.getName() : "";
		String displayName = TemplateUtil.getMultilingualString(label, manager.get(def.getName()).getLocalizedDisplayNameList());
%>
<c:out value="<%=displayName %>"/>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=propValue %>"/>" />
<%
	}
%>
