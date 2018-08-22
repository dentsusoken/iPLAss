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
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.SelectValue" %>
<%@ page import="org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition" %>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.SelectProperty" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.EditorValue" %>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	EditorValue getValue(SelectPropertyEditor editor, SelectValue value, List<LocalizedSelectValueDefinition> localeValueList, List<SelectValue> selectValueList) {
		if (value == null || value.getValue() == null) return null;
		for (EditorValue tmp : editor.getValues()) {
			if (value.getValue().equals(tmp.getValue())) {

				String label = EntityViewUtil.getSelectPropertyLabel(localeValueList, tmp, selectValueList);
				if (label != null) {
					EditorValue localEditorValue = new EditorValue();
					localEditorValue.setLabel(label);
					localEditorValue.setStyle(tmp.getStyle());
					localEditorValue.setValue(tmp.getValue());
					return localEditorValue;
				}
				return tmp;
			}
		}
		return null;
	}

	List<EditorValue> getValues(SelectPropertyEditor editor, Object value, PropertyDefinition pd, List<SelectValue> selectValueList, List<LocalizedSelectValueDefinition> localeValueList) {
		List<EditorValue> values = new ArrayList<EditorValue>();
		if (pd.getMultiplicity() != 1){
			SelectValue[] array = value instanceof SelectValue[] ? (SelectValue[]) value : null;
			if (array != null) {
				for (SelectValue tmp : array) {
					EditorValue ev = getValue(editor, tmp, localeValueList, selectValueList);
					if (ev != null) {
						values.add(ev);
					}
				}
			}
		} else {
			SelectValue tmp = value instanceof SelectValue ? (SelectValue) value : null;
			EditorValue ev = getValue(editor, tmp, localeValueList, selectValueList);
			if (ev != null) values.add(ev);
		}
		return values;
	}
%>

<%
	SelectPropertyEditor editor = (SelectPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	Boolean outputHidden = (Boolean) request.getAttribute(Constants.OUTPUT_HIDDEN);
	if (outputHidden == null) outputHidden = false;

	@SuppressWarnings("unchecked") List<SelectValue> selectValueList = (List<SelectValue>)request.getAttribute(Constants.EDITOR_SELECT_VALUE_LIST);
	@SuppressWarnings("unchecked") List<LocalizedSelectValueDefinition> localeValueList = (List<LocalizedSelectValueDefinition>)request.getAttribute(Constants.EDITOR_LOCAL_VALUE_LIST);

	String propName = editor.getPropertyName();

	//タイプ毎に出力内容かえる
	List<EditorValue> values = getValues(editor, propValue, pd, selectValueList, localeValueList);

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
%>
<ul class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
	for (EditorValue tmp : values) {
		String style = tmp.getStyle() != null ? tmp.getStyle() : "";
%>
<li class="<c:out value="<%=style %>"/>">
<c:out value="<%=tmp.getLabel() %>" />
<%
		if (outputHidden) {
%>
<input type="hidden" name="<c:out value="<%=propName%>"/>" value="<c:out value="<%=tmp.getValue()%>"/>" />
<%
		}
%>
</li>
<%
	}
%>
</ul>
