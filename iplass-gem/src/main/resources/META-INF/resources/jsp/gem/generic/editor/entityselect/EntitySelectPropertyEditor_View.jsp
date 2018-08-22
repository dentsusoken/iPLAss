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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.definition.DefinitionSummary"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition" %>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TemplatePropertyEditor" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	List<DefinitionSummary> getValues(List<DefinitionSummary> list, Object value, PropertyDefinition pd) {
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
	List<DefinitionSummary> definitionNames = manager.definitionSummaryList();

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
		List<DefinitionSummary> values = getValues(definitionNames, propValue, pd);
%>
<ul class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
		for (DefinitionSummary tmp : values) {
			String label = tmp.getDisplayName() != null ? tmp.getDisplayName() : tmp.getName();
			String displayName = TemplateUtil.getMultilingualString(label, manager.get(tmp.getName()).getLocalizedDisplayNameList());
%>
<li>
<c:out value="<%=displayName %>" />
</li>
<%
		}
%>
</ul>
<%
	} else {
		DefinitionSummary dn = getValue(definitionNames, (String) propValue);
		String label = dn != null ? dn.getDisplayName() != null ? dn.getDisplayName() : dn.getName() : "";
		String displayName = TemplateUtil.getMultilingualString(label, manager.get(dn.getName()).getLocalizedDisplayNameList());
%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>">
<c:out value="<%=displayName %>"/>
</span>
<%
	}
%>
