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
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.entity.Entity" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.generic.refcombo.GetEditorCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.ReferenceComboCommand"%>
<%@ page import="org.iplass.gem.command.generic.refcombo.SearchParentCommand"%>

<%
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);
	ReferenceProperty pd = (ReferenceProperty) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);

	String viewName = StringUtil.escapeHtml((String)request.getAttribute(Constants.VIEW_NAME), true);

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
	}

	if (pd.getMultiplicity() == 1) {
		Entity refEntity = propValue instanceof Entity ? (Entity) propValue : null;
		String oid = refEntity != null ? refEntity.getOid() != null ? refEntity.getOid() : "" : "";
%>
<select name="${m:esc(editor.propertyName)}" class="form-size-02 inpbr refCombo" style="<c:out value="<%=customStyle%>"/>" data-oid="<c:out value="<%=oid%>"/>"
 data-propName="${m:esc(propertyDefinition.name)}" data-defName="${m:esc(defName)}"
 data-viewName="<%=viewName %>" data-webapiName="<%=ReferenceComboCommand.WEBAPI_NAME%>"
 data-getEditorWebapiName="<%=GetEditorCommand.WEBAPI_NAME %>" data-searchParentWebapiName="<%=SearchParentCommand.WEBAPI_NAME %>"
 data-viewType="<%=Constants.VIEW_TYPE_DETAIL %>" data-prefix="" data-searchType="NONE" data-upperName="" data-upperOid="" data-customStyle="<c:out value="<%=customStyle%>"/>">
</select>
<%
	} else {
		String ulId = "ul_" + editor.getPropertyName();
		int length = 0;

		//テンプレート行
		String dummyRowId = "id_li_" + editor.getPropertyName() + "Dummmy";
%>
<ul id="<c:out value="<%=ulId %>"/>" class="mb05">
<li id="<c:out value="<%=dummyRowId %>"/>" style="display: none;">
<select class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>" data-oid=""
 data-propName="${m:esc(propertyDefinition.name)}" data-defName="${m:esc(defName)}"
 data-viewName="<%=viewName %>" data-webapiName="<%=ReferenceComboCommand.WEBAPI_NAME%>"
 data-getEditorWebapiName="<%=GetEditorCommand.WEBAPI_NAME %>" data-searchParentWebapiName="<%=SearchParentCommand.WEBAPI_NAME %>"
 data-viewType="<%=Constants.VIEW_TYPE_DETAIL %>" data-prefix="" data-searchType="NONE" data-upperName="" data-upperOid="" data-customStyle="<c:out value="<%=customStyle%>"/>">
</select> <input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_RefCombo.delete')}" class="gr-btn-02 del-btn" />
</li>
<%
		//データ出力
		Entity[] array = propValue instanceof Entity[] ? (Entity[]) propValue : null;
		if (array != null) {
			length = array.length;
			for (int i = 0; i < array.length; i++) {
				String oid = array[i] != null ? array[i].getOid() != null ? array[i].getOid() : "" : "";
				String liId = "li_" + editor.getPropertyName() + i;
%>
<li id="<c:out value="<%=liId %>"/>">
<select name="${m:esc(editor.propertyName)}" class="form-size-02 inpbr refCombo" style="<c:out value="<%=customStyle%>"/>" data-oid="<c:out value="<%=oid%>"/>"
 data-propName="${m:esc(propertyDefinition.name)}" data-defName="${m:esc(defName)}"
 data-viewName="<%=viewName %>" data-webapiName="<%=ReferenceComboCommand.WEBAPI_NAME%>"
 data-getEditorWebapiName="<%=GetEditorCommand.WEBAPI_NAME %>" data-searchParentWebapiName="<%=SearchParentCommand.WEBAPI_NAME %>"
 data-viewType="<%=Constants.VIEW_TYPE_DETAIL %>" data-prefix="" data-searchType="NONE" data-upperName="" data-upperOid="" data-customStyle="<c:out value="<%=customStyle%>"/>">
</select> <input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_RefCombo.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>')" />
</li>
<%
			}
		}
%>
</ul>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.reference.ReferencePropertyEditor_RefCombo.add')}" class="gr-btn-02 add-btn refComboController" data-multiplicity="${propertyDefinition.multiplicity}" data-ulId="<c:out value="<%=ulId %>"/>" data-dummyId="<c:out value="<%=dummyRowId %>"/>" data-propName="${m:esc(editor.propertyName)}" />
<%
	}
%>
