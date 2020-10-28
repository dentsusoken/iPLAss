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

<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.NestProperty"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.SearchResultSection"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchFormViewData"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil" %>
<%!
	boolean isDispProperty(NestProperty property) {
		if (property.getEditor() == null) return false;
		return true;
	}
%>
<%
	String propName = (String) request.getAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
	ReferenceProperty rp = (ReferenceProperty) request.getAttribute(Constants.EDITOR_REF_NEST_PROPERTY);
	String style = (String) request.getAttribute(Constants.EDITOR_REF_NEST_STYLE);
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_REF_NEST_EDITOR);
	SearchFormViewData data = (SearchFormViewData) request.getAttribute(Constants.DATA);

	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	EntityDefinition red = edm.get(rp.getObjectDefinitionName());
	int colCount = 0;
	for (NestProperty np : editor.getNestProperties()) {
		PropertyDefinition pd = red.getProperty(np.getPropertyName());
		if (isDispProperty(np)) {
			String nestStyle = StringUtil.isNotBlank(style) ? style + "_col" + colCount++ : "";
			if (pd instanceof ReferenceProperty
				&& np.getEditor() instanceof ReferencePropertyEditor
				&& !((ReferencePropertyEditor) np.getEditor()).getNestProperties().isEmpty()) {
				request.setAttribute(Constants.EDITOR_REF_NEST_PROP_NAME, propName + "." + np.getPropertyName());
				request.setAttribute(Constants.EDITOR_REF_NEST_PROPERTY, pd);
				request.setAttribute(Constants.EDITOR_REF_NEST_STYLE, nestStyle);
				request.setAttribute(Constants.EDITOR_REF_NEST_EDITOR, np.getEditor());
%>
<jsp:include page="SearchResultSection_Nest.jsp" />
<%
				request.removeAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
				request.removeAttribute(Constants.EDITOR_REF_NEST_PROPERTY);
				request.removeAttribute(Constants.EDITOR_REF_NEST_STYLE);
				request.removeAttribute(Constants.EDITOR_REF_NEST_EDITOR);
			} else {
				String sortPropName = StringUtil.escapeHtml(propName + "." + np.getPropertyName());
				String displayLabel = TemplateUtil.getMultilingualString(np.getDisplayLabel(), np.getLocalizedDisplayLabelList());
				if (displayLabel == null) displayLabel = pd.getDisplayName();
				String width = "";
				if (np.getWidth() > 0) {
					width = ", width:" + np.getWidth();
				}
				String align = "";
				if (np.getTextAlign() != null) {
					align = ", align:'" + np.getTextAlign().name().toLowerCase() + "'";
				}
				String sortable = "sortable:true";
				if (!ViewUtil.getEntityViewHelper().isSortable(pd)) {
					sortable = "sortable:false";
				}
				String hidden = ", hidden:false";
				if (np.getEditor() != null && np.getEditor().isHide()) {
					hidden = ", hidden:true";
				}
%>
<%-- XSS対応-メタの設定のため対応なし(displayLabel,nestStyle) --%>
	colModel.push({name:"<%=sortPropName%>", index:"<%=sortPropName%>", classes:"<%=nestStyle%>", label:"<p class='title'><%=displayLabel%></p>", <%=sortable%><%=hidden%><%=width%><%=align%>});
<%
			}
		}
	}
%>
