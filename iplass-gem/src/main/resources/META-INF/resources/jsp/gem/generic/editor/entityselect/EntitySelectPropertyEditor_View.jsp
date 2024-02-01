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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition" %>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TemplatePropertyEditor" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	List<String> getLabels(Object value, PropertyDefinition pd, EntityDefinitionManager manager) {
		List<String> labels = new ArrayList<>();
		if (pd.getMultiplicity() != 1){
			String[] array = value instanceof String[] ? (String[]) value : null;
			if (array != null) {
				for (String defName : array) {
					labels.add(getLabel(defName, manager));
				}
			}
		} else {
			String defName = value instanceof String ? (String) value : null;
			if (defName != null) {
				labels.add(getLabel(defName, manager));
			}
		}
		return labels;
	}
	String getLabel(String defName, EntityDefinitionManager manager) {
		if (defName == null) return "";
		
		EntityDefinition ed = manager.get(defName);
		if (ed != null) {
			String label = ed.getDisplayName() != null ? ed.getDisplayName() : ed.getName();
			return TemplateUtil.getMultilingualString(label, ed.getLocalizedDisplayNameList());
		} else {
			return "(" + defName + ")";
		}
	}
%>

<%
	TemplatePropertyEditor editor = (TemplatePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);

	String propName = editor.getPropertyName();

	EntityDefinitionManager manager = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);

	//カスタムスタイル
	String customStyle = "";
	if (type == OutputType.VIEW) {
		if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), entity, propValue);
		}
	} else if (type == OutputType.EDIT) {
		//入力不可の場合
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
		}
	}

	boolean isMultiple = pd.getMultiplicity() != 1;
	if (isMultiple) {
		List<String> labels = getLabels(propValue, pd, manager);
%>
<ul class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
		for (String label : labels) {
%>
<li>
<c:out value="<%=label %>" />
</li>
<%
		}
%>
</ul>
<%
	} else {
		String label = getLabel((String)propValue, manager);
%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>">
<c:out value="<%=label %>"/>
</span>
<%
	}
%>
