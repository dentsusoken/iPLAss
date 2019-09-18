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
<%@page import="org.iplass.mtp.auth.AuthContext"%>
<%@page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@page import="org.iplass.mtp.entity.Entity"%>
<%@page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%@page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@page import="org.iplass.mtp.view.generic.editor.BooleanPropertyEditor" %>
<%@page import="org.iplass.mtp.view.generic.editor.BooleanPropertyEditor.BooleanDisplayType" %>
<%@page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@page import="org.iplass.gem.command.Constants" %>
<%@page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@page import="org.iplass.gem.command.ViewUtil" %>
<%!
	Object getDefaultValue(BooleanPropertyEditor editor, PropertyDefinition pd) {
		String defaultValue = editor.getDefaultValue();
		if (defaultValue != null) {
			if (pd.getMultiplicity() != 1) {
				String[] vals = defaultValue.split(",");
				Boolean[] ret = new Boolean[vals.length];
				for (int i = 0; i < vals.length; i++) {
					ret[i] = getDefaultValue(vals[i]);
				}
				return ret;
			} else {
				return getDefaultValue(defaultValue);
			}
		}
		return null;
	}
	Boolean getDefaultValue(String value) {
		if (value != null) {
			if ("true".equals(value) || "1".equals(value)) {
				return true;
			} else if ("false".equals(value) || "0".equals(value)) {
				return false;
			}
		}
		return null;
	}
%>
<%
	BooleanPropertyEditor editor = (BooleanPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);

	Boolean isVirtual = (Boolean) request.getAttribute(Constants.IS_VIRTUAL);
	if (isVirtual == null) isVirtual = false;

	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;
	
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
	if (isInsert && isEditable && propValue == null) propValue = getDefaultValue(editor, pd);

	boolean isMultiple = pd.getMultiplicity() != 1;

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.boolean.BooleanPropertyEditor_Edit.pleaseSelect");
	}

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/boolean/BooleanPropertyAutocompletion.jsp");
	}

	String trueLabel = TemplateUtil.getMultilingualString(editor.getTrueLabel(), editor.getLocalizedTrueLabelList());
	if (StringUtil.isEmpty(trueLabel)) {
		trueLabel = TemplateUtil.getMultilingualString(
				GemResourceBundleUtil.resourceString("generic.editor.boolean.BooleanPropertyEditor_Edit.enable"),
				GemResourceBundleUtil.resourceList("generic.editor.boolean.BooleanPropertyEditor_Edit.enable"));
	}
	String falseLabel = TemplateUtil.getMultilingualString(editor.getFalseLabel(), editor.getLocalizedFalseLabelList());
	if (StringUtil.isEmpty(falseLabel)) {
		falseLabel = TemplateUtil.getMultilingualString(
				GemResourceBundleUtil.resourceString("generic.editor.boolean.BooleanPropertyEditor_Edit.invalid"),
				GemResourceBundleUtil.resourceList("generic.editor.boolean.BooleanPropertyEditor_Edit.invalid"));
	}


	if (editor.getDisplayType() != BooleanDisplayType.LABEL && updatable) {
		if (isMultiple) {
			//複数
			Boolean[] array = propValue instanceof Boolean[] ? (Boolean[]) propValue : null;
			if (editor.getDisplayType() == BooleanDisplayType.SELECT) {
				String selectedTrue = "";
				String selectedFalse = "";
				if (array != null) {
					for (Boolean b : array) {
						if (b != null && b) selectedTrue =  "selected";
						if (b != null && !b) selectedFalse = "selected";
					}
				}
%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-12 inpbr" style="<c:out value="<%=customStyle%>"/>" size="2" multiple>
<option value="true" <c:out value="<%=selectedTrue%>"/>><c:out value="<%=trueLabel %>"/></option>
<option value="false" <c:out value="<%=selectedFalse%>"/>><c:out value="<%=falseLabel %>"/></option>
</select>
<%
			} else {
				String cls = "list-radio-01";
				if (editor.getDisplayType() == BooleanDisplayType.CHECKBOX) cls = "list-check-01";
%>
<ul>
<%
				for (int i = 0; i < pd.getMultiplicity(); i++) {
%>
<li>
<%
					if (editor.getDisplayType() == BooleanDisplayType.CHECKBOX) {
						//チェックボックス
						String checked = "";
						if (array != null && array.length > i) {
							checked = array[i] != null ? array[i] ? "checked" : "" : "";
						}
%>
<ul class="<c:out value="<%=cls %>"/>">
<li><input type="checkbox" name="<c:out value="<%=propName + i %>"/>" style="<c:out value="<%=customStyle%>"/>" value="true" <c:out value="<%=checked %>"/> /></li>
</ul>
<%
					} else if (editor.getDisplayType() == BooleanDisplayType.RADIO) {
						//ラジオボタン
						String checkedTrue = "";
						String checkedFalse = "";
						if (array != null && array.length > i) {
							checkedTrue = array[i] != null ? array[i] ? "checked" : "" : "";
							checkedFalse = array[i] != null ? !array[i] ? "checked" : "" : "";
						}

						//BooleanPropertyEditorの場合はmultipleでも選択解除可能か判定
						String cssTogglable = required ? "" : "radio-togglable";
%>
<ul class="<c:out value="<%=cls %>"/>">
<li><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=trueLabel %>" />">
	<input type="radio" name="<c:out value="<%=propName + i %>"/>" value="true" class="<%=cssTogglable%>" <c:out value="<%=checkedTrue %>"/> /><c:out value="<%=trueLabel %>"/>
</label></li>
<li><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=falseLabel %>" />">
	<input type="radio" name="<c:out value="<%=propName + i %>"/>" value="false" class="<%=cssTogglable%>" <c:out value="<%=checkedFalse %>"/> /><c:out value="<%=falseLabel %>"/>
</label></li>
</ul>
<%
					}
%>
</li>
<%
				}
%>
</ul>
<%
			}
		} else {
			//単一
			Boolean b = propValue instanceof Boolean ? (Boolean) propValue : null;
			if (editor.getDisplayType() == BooleanDisplayType.CHECKBOX) {
				//チェックボックス
				String checked = b != null ? b ? "checked" : "" : "";
%>
<ul class="list-check-01">
<li><input type="checkbox" name="<c:out value="<%=propName %>"/>" style="<c:out value="<%=customStyle%>"/>" value="true" <c:out value="<%=checked %>"/> /></li>
</ul>
<%
			} else if (editor.getDisplayType() == BooleanDisplayType.RADIO && updatable) {
				//ラジオボタン
				String checkedTrue = b != null ? b ? "checked" : "" : "";
				String checkedFalse = b != null ? !b ? "checked" : "" : "";
				
				//選択解除可能か
				String cssTogglable = required ? "" : "radio-togglable";
%>
<ul class="list-radio-01">
<li><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=trueLabel %>" />">
	<input type="radio" name="<c:out value="<%=propName %>"/>" value="true" class="<%=cssTogglable%>" <c:out value="<%=checkedTrue %>"/> />
	<c:out value="<%=trueLabel %>"/>
</label></li>
<li><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=falseLabel %>" />">
	<input type="radio" name="<c:out value="<%=propName %>"/>" value="false" class="<%=cssTogglable%>" <c:out value="<%=checkedFalse %>"/> />
	<c:out value="<%=falseLabel %>"/>
</label></li>
</ul>
<%
			} else if (editor.getDisplayType() == BooleanDisplayType.SELECT) {
				String selectedTrue = b != null ? b ? "selected" : "" : "";
				String selectedFalse = b != null ? !b ? "selected" : "" : "";
%>
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>" size="1">
<option value=""><%=pleaseSelectLabel %></option>
<option value="true" <c:out value="<%=selectedTrue %>"/>><c:out value="<%=trueLabel %>"/></option>
<option value="false" <c:out value="<%=selectedFalse %>"/>><c:out value="<%=falseLabel %>"/></option>
</select>
<%
			}
		}
	} else {
		//ラベル
		request.setAttribute(Constants.OUTPUT_HIDDEN, true);
%>
<jsp:include page="BooleanPropertyEditor_View.jsp"></jsp:include>
<%
		request.removeAttribute(Constants.OUTPUT_HIDDEN);
	}

	String type = editor.getDisplayType().name();
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>Type" value="<c:out value="<%=type %>"/>" />
