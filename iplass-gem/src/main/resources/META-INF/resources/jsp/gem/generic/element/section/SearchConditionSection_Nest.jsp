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

<%@ page import="java.util.HashMap" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.NestProperty"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.RangePropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.element.property.PropertyItem"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%!
String checkDefaultValue(HashMap<String, Object> defaultSearchCond, String searchCond, String key, String expect, String retStr) {
	if (StringUtil.isNotBlank(searchCond)) return "";
	if (!defaultSearchCond.containsKey(key)) return "";
	Object value = defaultSearchCond.get(key);
	if (value instanceof Object[] && ((Object[]) value).length > 0) {
		value = ((Object[]) value)[0];
	}
	if (expect.equals(value)) return retStr;
	return "";
}
%>
<%
	ReferenceProperty rp = (ReferenceProperty) request.getAttribute(Constants.EDITOR_REF_NEST_PROPERTY);
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_REF_NEST_EDITOR);
	String propName = (String) request.getAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
	String condPropName = (String) request.getAttribute(Constants.COND_PROP_NAME);
	@SuppressWarnings("unchecked") HashMap<String, Object> defaultSearchCond = (HashMap<String, Object>) request.getAttribute(Constants.DEFAULT_SEARCH_COND);

	PropertyItem item = (PropertyItem) request.getAttribute(Constants.PROPERTY_ITEM);
	request.removeAttribute(Constants.PROPERTY_ITEM);//消しておかないと下位の項目で取れてしまう
	NestProperty nest = (NestProperty) request.getAttribute(Constants.EDITOR_REF_NEST_PROPERTY_ITEM);
	request.removeAttribute(Constants.EDITOR_REF_NEST_PROPERTY_ITEM);

	String searchCond = request.getParameter(Constants.SEARCH_COND);
	if (searchCond == null) searchCond = "";

	String displayLabel = null;
	String optClass = null;
	if (item != null) {
		displayLabel = TemplateUtil.getMultilingualString(item.getDisplayLabel(), item.getLocalizedDisplayLabelList(), rp.getDisplayName(), rp.getLocalizedDisplayNameList());
		optClass = item.isRequiredDetail() ? "required" : "";
	} else if (nest != null) {
		displayLabel = TemplateUtil.getMultilingualString(nest.getDisplayLabel(), nest.getLocalizedDisplayLabelList(), rp.getDisplayName(), rp.getLocalizedDisplayNameList());
		optClass = nest.isRequiredDetail() ? "required" : "";
	}

	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	EntityDefinition ed = edm.get(rp.getObjectDefinitionName());

	boolean showNest = false;
	if (editor.getDisplayType() == ReferenceDisplayType.SELECT
			|| editor.getDisplayType() == ReferenceDisplayType.CHECKBOX
			|| editor.getDisplayType() == ReferenceDisplayType.REFCOMBO
			|| (editor.getDisplayType() == ReferenceDisplayType.LINK && editor.isUseSearchDialog())) {
		//選択型かそれ以外は今まで通り
%>
<option value="<c:out value="<%=propName%>"/>" class="<c:out value="<%=optClass%>" />"><c:out value="<%=displayLabel%>" /></option>
<%
	} else {
		if (editor.getNestProperties().isEmpty()) {
			//ネストがなければ名前
%>
<option value="<c:out value="<%=propName%>"/>" class="<c:out value="<%=optClass%>" />"><c:out value="<%=displayLabel%>" /></option>
<%
		} else {
			showNest = true;
			if (editor.isUseNestConditionWithProperty()) {
				//ネストと同時に名前も
%>
<option value="<c:out value="<%=propName%>"/>" class="<c:out value="<%=optClass%>" />"><c:out value="<%=displayLabel%>" /></option>
<%
			}
		}
	}

	//ネストを表示
	if (showNest || editor.isUseNestConditionWithProperty()) {
		for (NestProperty np : editor.getNestProperties()) {
			String npName = propName + "." + np.getPropertyName();
			PropertyDefinition pd = ed.getProperty(np.getPropertyName());
			if (pd instanceof ReferenceProperty
					&& np.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) np.getEditor()).getNestProperties().isEmpty()) {
				request.setAttribute(Constants.EDITOR_REF_NEST_PROPERTY_ITEM, np);
				request.setAttribute(Constants.EDITOR_REF_NEST_PROPERTY, pd);
				request.setAttribute(Constants.EDITOR_REF_NEST_EDITOR, np.getEditor());
				request.setAttribute(Constants.EDITOR_REF_NEST_PROP_NAME, npName);
%>
<jsp:include page="SearchConditionSection_Nest.jsp" />
<%
				request.removeAttribute(Constants.EDITOR_REF_NEST_PROPERTY_ITEM);
				request.removeAttribute(Constants.EDITOR_REF_NEST_PROPERTY);
				request.removeAttribute(Constants.EDITOR_REF_NEST_EDITOR);
				request.removeAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
			} else {
				displayLabel = TemplateUtil.getMultilingualString(np.getDisplayLabel(), np.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList());
				optClass = np.isRequiredDetail() ? "required" : "";
				String selected = checkDefaultValue(defaultSearchCond, searchCond, condPropName, npName, "selected");
%>
<option value="<c:out value="<%=npName%>"/>" class="<c:out value="<%=optClass%>" />" <c:out value="<%=selected%>" />><c:out value="<%=displayLabel%>" /></option>
<%
				if (np.getEditor() instanceof RangePropertyEditor) {
					RangePropertyEditor toNp = (RangePropertyEditor) np.getEditor();
					String toNpName = propName + "." + toNp.getToPropertyName();
					String toDisplayLabel = displayLabel + "_To";
					String toSelected = checkDefaultValue(defaultSearchCond, searchCond, condPropName, toNpName, "selected");
%>
<option value="<c:out value="<%=toNpName%>"/>" class="<c:out value="<%=optClass%>" />" <c:out value="<%=toSelected%>" />><c:out value="<%=toDisplayLabel%>" /></option>
<%
				}
			}
		}
	}
%>
