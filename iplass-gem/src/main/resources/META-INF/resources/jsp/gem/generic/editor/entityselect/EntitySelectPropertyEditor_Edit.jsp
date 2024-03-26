<%--
 Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
 
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
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.definition.DefinitionSummary"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TemplatePropertyEditor" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%!List<DefinitionSummary> getValues(List<DefinitionSummary> list, Object value, PropertyDefinition pd) {
		List<DefinitionSummary> values = new ArrayList<DefinitionSummary>();
		if (pd.getMultiplicity() != 1){
			String[] array = value instanceof String[] ? (String[]) value : null;
			if (array != null) {
				for (String tmp : array) {
					DefinitionSummary ev = getValue(list, tmp);
					if (ev != null) {
						values.add(ev);
					}
				}
			}
		} else {
			String tmp = value instanceof String ? (String) value : null;
			DefinitionSummary ev = getValue(list, tmp);
			if (ev != null) values.add(ev);
		}
		return values;
	}
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

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);
	Integer colNum = (Integer) request.getAttribute(Constants.COL_NUM);
	Boolean nest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST);
	Boolean nestDummyRow = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST_DUMMY_ROW);

	Boolean isVirtual = (Boolean) request.getAttribute(Constants.IS_VIRTUAL);
	if (isVirtual == null) isVirtual = false;

	EntityDefinitionManager manager = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
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
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.entityselect.EntitySelectPropertyEditor_Edit.pleaseSelect");
	}

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
	}

	//詳細編集
	int length = 0;
	if (isMultiple) {
		//複数
		String[] array = propValue instanceof String[] ? (String[]) propValue : null;
		if (updatable) {
			List<String> values = new ArrayList<String>();
			if (array != null && array.length > 0) values.addAll(Arrays.asList(array));
%>
<select name="<c:out value="<%=propName %>"/>" size="5" class="form-size-12 inpbr" style="<c:out value="<%=customStyle%>"/>" multiple>
<%
			for (DefinitionSummary tmp : definitionNames) {
				String selected = values.contains(tmp.getName()) ? " selected" : "";
				String label = tmp.getDisplayName() != null ? tmp.getDisplayName() : tmp.getName();
				String displayName = TemplateUtil.getMultilingualString(label, manager.get(tmp.getName()).getLocalizedDisplayNameList());
%>
<option value="<c:out value="<%=tmp.getName() %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=displayName %>" /></option>
<%
			}
%>
</select>
<%
		} else {
			List<DefinitionSummary> names = getValues(definitionNames, propValue, pd);
%>
<ul style="<c:out value="<%=customStyle%>"/>" >
<%
			for (DefinitionSummary tmp : names) {
				String label = tmp.getDisplayName() != null ? tmp.getDisplayName() : tmp.getName();
				String displayName = TemplateUtil.getMultilingualString(label, manager.get(tmp.getName()).getLocalizedDisplayNameList());
%>
<li>
<c:out value="<%=label %>" />
<input type="hidden" name="<c:out value="<%=propName%>"/>" value="<c:out value="<%=displayName%>"/>" />
</li>
<%
			}
%>
</ul>
<%
		}
	} else {
		//単一
		String str = propValue instanceof String ? (String) propValue : "";
		if (updatable) {
%>
<select name="<c:out value="<%=propName %>"/>" size="1" class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>">
<option value=""><%= pleaseSelectLabel %></option>
<%
			for (DefinitionSummary tmp : definitionNames) {
				String selected = str.equals(tmp.getName()) ? " selected" : "";
				String label = tmp.getDisplayName() != null ? tmp.getDisplayName() : tmp.getName();
				String displayName = TemplateUtil.getMultilingualString(label, manager.get(tmp.getName()).getLocalizedDisplayNameList());
%>
<option value="<c:out value="<%=tmp.getName() %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=displayName %>" /></option>
<%
			}
%>
</select>
<%
		} else {
			DefinitionSummary dn = getValue(definitionNames, str);
			String label = dn != null ? dn.getDisplayName() != null ? dn.getDisplayName() : dn.getName() : "";
			String displayName = TemplateUtil.getMultilingualString(label, manager.get(dn.getName()).getLocalizedDisplayNameList());
			String val = dn != null ? dn.getName() != null ? dn.getName() : "" : "";

			if (StringUtil.isNotEmpty(customStyle)) {
%>
<span style="<c:out value="<%=customStyle %>"/>">
<%
			}
%>
<c:out value="<%=displayName %>"/>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=propValue %>"/>" />
<%
			if (StringUtil.isNotEmpty(customStyle)) {
%>
</span>
<%
			}
		}
	}
%>
