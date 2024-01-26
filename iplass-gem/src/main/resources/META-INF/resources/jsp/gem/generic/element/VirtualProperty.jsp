<%--
 Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.

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
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.entity.*"%>
<%@ page import="org.iplass.mtp.entity.definition.*"%>
<%@ page import="org.iplass.mtp.entity.definition.validations.*"%>
<%@ page import="org.iplass.mtp.util.*" %>
<%@ page import="org.iplass.mtp.view.generic.*" %>
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.view.generic.element.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>

<%
	Element element = (Element) request.getAttribute(Constants.ELEMENT);
	Object value = request.getAttribute(Constants.ENTITY_DATA);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);
	EntityDefinition ed = (EntityDefinition) request.getAttribute(Constants.ENTITY_DEFINITION);
	String viewName = (String) request.getAttribute(Constants.VIEW_NAME);
	if (viewName == null) viewName = "";	
	Integer colNum = (Integer) request.getAttribute(Constants.COL_NUM);
	Entity rootEntity = (Entity) request.getAttribute(Constants.ROOT_ENTITY);

	VirtualPropertyItem property = (VirtualPropertyItem) element;
	String propName = property.getPropertyName();
	PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(property);

	String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList());

	boolean required = false;
	RequiredDisplayType rdType = property.getRequiredDisplayType();
	if (rdType == null) rdType = RequiredDisplayType.DEFAULT;
	if (rdType == RequiredDisplayType.DISPLAY) {//DEFAULTの場合は非表示(PropertyDefinition無いので)
		required = true;
	}

	property.getEditor().setPropertyName(property.getPropertyName());

	Entity entity = value instanceof Entity ? (Entity) value : null;
	Object propValue = null;
	if (entity != null) {
		propValue = entity.getValue(property.getPropertyName());
	}

	//列数で幅調整
	if (colNum == null || colNum < 1) {
		colNum = 1;
	}
	String cellStyle = "section-data col" + colNum;

	String style = cellStyle;
	if (StringUtil.isNotBlank(property.getStyle())) {
		style = cellStyle + " " + property.getStyle();
	}

	String tooltip = "";
	if (StringUtil.isNotBlank(property.getTooltip())) {
		tooltip = TemplateUtil.getMultilingualString(property.getTooltip(), property.getLocalizedTooltipList());
	}

	String description = "";
	if (StringUtil.isNotBlank(property.getDescription())) {
		description = TemplateUtil.getMultilingualString(property.getDescription(), property.getLocalizedDescriptionList());
	}

	boolean showDesc = OutputType.EDIT == type && description != null && description.length() > 0;
	
	if (!property.getEditor().isHide()) {
		//非表示ではない場合
%>

<th id="id_th_<c:out value="<%=propName %>"/>" class="<c:out value="<%=style %>"/>">
<%-- XSS対応-メタの設定のため対応なし(displayLabel) --%>
<%=displayLabel %>
<%
		if (OutputType.EDIT == type && required) {
%>
<span class="ico-required ml10 vm">${m:rs("mtp-gem-messages", "generic.element.property.Property.required")}</span>
<%
		}
		if (OutputType.EDIT == type && tooltip != null && tooltip.length() > 0) {
%>
<%-- XSS対応-メタの設定のため対応なし(tooltip) --%>
<span class="ml05"><img src="${m:esc(skinImagePath)}/icon-help-01.png" alt="" class="vm tp"  title="<%=tooltip %>" /></span>
<%
		}
%>
</th>
<td id="id_td_<c:out value="<%=propName %>"/>" class="<c:out value="<%=style %>"/>">
<%
		if (showDesc) {
%>
<p class="mb05">
<%
		}
	}

	String path =  EntityViewUtil.getJspPath(property.getEditor(), ViewConst.DESIGN_TYPE_GEM);
	if (path != null) {
		request.setAttribute(Constants.EDITOR_EDITOR, property.getEditor());
		request.setAttribute(Constants.EDITOR_PROP_VALUE, propValue);
		request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, pd);
		request.setAttribute(Constants.IS_VIRTUAL, true);
		if (OutputType.EDIT == type) {
			request.setAttribute(Constants.AUTOCOMPLETION_SETTING, property.getAutocompletionSetting());
			request.setAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA, rootEntity);
			request.setAttribute(Constants.EDITOR_REQUIRED, required);
		}
%>
<jsp:include page="<%=path %>" />
<%
		request.removeAttribute(Constants.EDITOR_EDITOR);
		request.removeAttribute(Constants.EDITOR_PROP_VALUE);
		request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
		request.removeAttribute(Constants.IS_VIRTUAL);
		if (OutputType.EDIT == type) {
			request.removeAttribute(Constants.EDITOR_REQUIRED);
		}
	}

	if (showDesc) {
%>
</p>
<%-- XSS対応-メタの設定のため対応なし(description) --%>
<p class="explanation"><%=description %></p>
<%
	}
	if ((OutputType.EDIT == type) && property.getAutocompletionSetting() != null) {
		request.setAttribute(Constants.AUTOCOMPLETION_DEF_NAME, ed.getName());
		request.setAttribute(Constants.AUTOCOMPLETION_VIEW_NAME, viewName);
		request.setAttribute(Constants.AUTOCOMPLETION_PROP_NAME, propName);
		request.setAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY, pd.getMultiplicity());
		String autocompletionPath = "/jsp/gem/generic/common/Autocompletion.jsp";
%>
<jsp:include page="<%=autocompletionPath%>" />
<%
		request.removeAttribute(Constants.AUTOCOMPLETION_SETTING);
		request.removeAttribute(Constants.AUTOCOMPLETION_ROOT_ENTITY_DATA);
		request.removeAttribute(Constants.AUTOCOMPLETION_DEF_NAME);
		request.removeAttribute(Constants.AUTOCOMPLETION_VIEW_NAME);
		request.removeAttribute(Constants.AUTOCOMPLETION_PROP_NAME);
		request.removeAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
		request.removeAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH);
	}
	if (OutputType.EDIT == type) {
		request.removeAttribute(Constants.EDITOR_REQUIRED);
	}
%>
</td>
