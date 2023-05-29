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

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@page import="org.iplass.mtp.entity.Entity"%>
<%@page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%@page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@page import="org.iplass.mtp.view.generic.OutputType"%>
<%@page import="org.iplass.mtp.view.generic.editor.BooleanPropertyEditor" %>
<%@page import="org.iplass.mtp.view.generic.editor.BooleanPropertyEditor.BooleanDisplayType"%>
<%@page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%
	BooleanPropertyEditor editor = (BooleanPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	Boolean outputHidden = (Boolean) request.getAttribute(Constants.OUTPUT_HIDDEN);
	if (outputHidden == null) outputHidden = false;

	String propName = editor.getPropertyName();

	boolean isMultiple = pd.getMultiplicity() != 1;
	
	if (editor.getDisplayType() != BooleanDisplayType.HIDDEN) {
		//HIDDEN以外
		
		String trueLabel = TemplateUtil.getMultilingualString(editor.getTrueLabel(), editor.getLocalizedTrueLabelList());
		if (StringUtil.isEmpty(trueLabel)) {
			trueLabel = TemplateUtil.getMultilingualString(
					GemResourceBundleUtil.resourceString("generic.editor.boolean.BooleanPropertyEditor_View.enable"),
					GemResourceBundleUtil.resourceList("generic.editor.boolean.BooleanPropertyEditor_View.enable"));
		}
		String falseLabel = TemplateUtil.getMultilingualString(editor.getFalseLabel(), editor.getLocalizedFalseLabelList());
		if (StringUtil.isEmpty(falseLabel)) {
			falseLabel = TemplateUtil.getMultilingualString(
					GemResourceBundleUtil.resourceString("generic.editor.boolean.BooleanPropertyEditor_View.invalid"),
					GemResourceBundleUtil.resourceList("generic.editor.boolean.BooleanPropertyEditor_View.invalid"));
		}
		
		// 自動補完があるラベルの場合は、表示に必要な情報をセットする
		if (ViewUtil.isAutocompletionTarget() && editor.getDisplayType() == BooleanDisplayType.LABEL) {
			String[] booleanLabel = {trueLabel, falseLabel};
			request.setAttribute("autocompletionBooleanLabel", booleanLabel);
		}

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

		if (isMultiple) {
			//複数
			Boolean[] array = propValue instanceof Boolean[] ? (Boolean[]) propValue : null;
%>
<ul name="data-label-<c:out value="<%=propName %>"/>"  class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
			for (int i = 0; i < pd.getMultiplicity(); i++) {
%>
<li>
<%
				String str = "";
				String label = "";
				if (array != null && array.length > i && array[i] != null) {
					str = array[i].toString();
					label = array[i] ? trueLabel : falseLabel;
				}
				if (label == null || label.length() == 0) {
					label = str;
				}
%>
<c:out value="<%=label %>"/>
<%
				if (outputHidden) {
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" />
<%
				}
%>
</li>
<%
			}
%>
</ul>
<%
		} else {
			//単一
			Boolean b = propValue instanceof Boolean ? (Boolean) propValue : null;
			String str = b != null ? b.toString() : "";
			String label = b != null ? b ? trueLabel : falseLabel : str;
%>
<span name="data-label-<c:out value="<%=propName %>"/>"  class="data-label" style="<c:out value="<%=customStyle %>"/>" data-true-label="<c:out value="<%=trueLabel %>"/>" data-false-label="<c:out value="<%=falseLabel %>"/>">
<c:out value="<%=label %>"/>
<%
			if (outputHidden) {
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" />
<%
			}
%>
</span>
<%
		}
	} else {
		//HIDDEN

		if (isMultiple) {
			//複数
%>
<ul name="data-hidden-<c:out value="<%=propName %>"/>">
<%
			Boolean[] array = propValue instanceof Boolean[] ? (Boolean[]) propValue : null;
			for (int i = 0; i < pd.getMultiplicity(); i++) {
				String str = "";
				if (array != null && array.length > i && array[i] != null) {
					str = array[i].toString();
				}
%>
<li>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" />
</li>
<%
			}
%>
</ul>
<%
		} else {
			//単一
			Boolean b = propValue instanceof Boolean ? (Boolean) propValue : null;
			String strHidden = b != null ? b.toString() : "";
%>
			<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<%
		}
	}
%>
