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
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="java.util.HashMap" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.ReferenceProperty"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.NestProperty"%>
<%@ page import="org.iplass.mtp.view.generic.editor.PropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor"%>
<%@ page import="org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.ViewConst"%>
<%@ page import="org.iplass.mtp.ApplicationException"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.mtp.ManagerLocator"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%!
	boolean isDispProperty(NestProperty property) {
		if (property.getEditor() == null) return false;
		return true;
	}

	PropertyDefinition getNestTablePropertyDefinition(NestProperty np, EntityDefinition ed) {
		PropertyDefinition pd = EntityViewUtil.getNestTablePropertyDefinition(np, ed);
		if (pd == null) {
			throw new ApplicationException(GemResourceBundleUtil.resourceString("generic.editor.reference.ReferencePropertyEditor_ConditionNest.editorExceptionMessage")
					+ ":propertyName=[" + np.getPropertyName() + "]");

		}
		
		return pd;
	}
%>
<%
	ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute(Constants.EDITOR_REF_NEST_EDITOR);

	String rootDefName = (String) request.getAttribute(Constants.ROOT_DEF_NAME);
	ReferenceProperty rp = (ReferenceProperty) request.getAttribute(Constants.EDITOR_REF_NEST_PROPERTY);
	String scriptKey = (String) request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String propName = (String) request.getAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
	String _propName = Constants.SEARCH_COND_PREFIX + propName;
	String nestStyle = (String) request.getAttribute(Constants.EDITOR_REF_NEST_STYLE);
	@SuppressWarnings("unchecked") HashMap<String, Object> defaultSearchCond = (HashMap<String, Object>) request.getAttribute(Constants.DEFAULT_SEARCH_COND);
	String viewName = (String)request.getAttribute(Constants.VIEW_NAME);
	if (viewName == null) viewName = "";

	Boolean showProperty = (Boolean) request.getAttribute(Constants.EDITOR_REF_SHOW_PROPERTY);
	if (showProperty == null) showProperty = true;

	//HIDDENの場合は、UseNestConditionWithPropertyが指定されていても不可
	boolean useNestCondition = editor.getDisplayType() != ReferenceDisplayType.HIDDEN && editor.isUseNestConditionWithProperty();

	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST_REQUIRED);
	if (required == null) required = false;

	EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	EntityDefinition ed = edm.get(rp.getObjectDefinitionName());
	int rowNum = 0;
	for (NestProperty np : editor.getNestProperties()) {
		PropertyDefinition _pd = ed.getProperty(np.getPropertyName());
		if (isDispProperty(np)) rowNum++;
	}

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
	}

	boolean showNest = false;
	if (!showProperty) {
		//親側で条件を表示してないケース
		if (rowNum == 0) {
			//ネストの項目がないので参照の名前だけで検索
			String value = "";
			if (defaultSearchCond != null) {
				//最上位のEntityから.付きのプロパティ名で値を取得
				String[] propValue = (String[]) defaultSearchCond.get(propName);
				if (propValue != null && propValue.length > 0) {
					value = propValue[0];
				}
			}
%>
<input type="text" name="<c:out value="<%=_propName %>"/>" value="<c:out value="<%=value %>"/>" class="form-size-04 inpbr" style="<c:out value="<%=customStyle%>"/>"/>

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$(":text[name='" + es("<%=StringUtil.escapeJavaScript(_propName)%>") + "']").val("<%=StringUtil.escapeJavaScript(value) %>");
	});
<%
			if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(_propName)%>") + "']").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "${m:escJs(nestDisplayLabel)}"));
			return false;
		}
		return true;
	});
<%
			}
%>
});
</script>
<%
		} else {
			showNest = true;
			if (useNestCondition) {
				//ネストはあるが親がない→親の名前とネストを表示
				String value = "";
				if (defaultSearchCond != null) {
					//最上位のEntityから.付きのプロパティ名で値を取得
					String[] propValue = (String[]) defaultSearchCond.get(propName);
					if (propValue != null && propValue.length > 0) value = propValue[0];
				}
%>
<input type="text" name="<c:out value="<%=_propName %>"/>" value="<c:out value="<%=value %>"/>" class="form-size-04 inpbr" style="<c:out value="<%=customStyle%>"/>"/>

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$(":text[name='" + es("<%=StringUtil.escapeJavaScript(_propName)%>") + "']").val("<%=StringUtil.escapeJavaScript(value) %>");
	});
<%
				if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(_propName)%>") + "']").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "${m:escJs(nestDisplayLabel)}"));
			return false;
		}
		return true;
	});
<%
				}
%>
});
</script>
</td>
</tr>
<%
			} else {
				//ネストだけ表示
				//参照自身の行は後で消す
%>
<div class="deleteRow"></div>
</td>
</tr>
<%
			}
		}
	}

	if (showNest || useNestCondition) {
		int i = 0;
		for (NestProperty np : editor.getNestProperties()) {
			PropertyDefinition _pd = getNestTablePropertyDefinition(np, ed);
			if (isDispProperty(np)) {
				String nestPropStyle = StringUtil.isNotBlank(nestStyle) ? nestStyle + "_cond" + i : "";
				String displayLabel = TemplateUtil.getMultilingualString(np.getDisplayLabel(), np.getLocalizedDisplayLabelList());
				if (displayLabel == null) displayLabel = _pd.getDisplayName();
				String idName = editor.getPropertyName().replaceAll("\\.", "_");
%>
<tr>
<th id="id_th_<c:out value="<%=idName %>"/>_<c:out value="<%=np.getPropertyName() %>"/>" class="<c:out value="<%=nestStyle%>" />">
<%-- XSS対応-メタの設定のため対応なし(displayLabel) --%>
<%=displayLabel %>
<%
				if (np.isRequiredNormal()) {
%>
<span class="ico-required ml10 vm" style="text-shadow: none;">${m:rs("mtp-gem-messages", "generic.element.property.Property.required")}</span>
<%
				}
				String tooltip = TemplateUtil.getMultilingualString(np.getTooltip(), np.getLocalizedTooltipList());
				if (StringUtil.isNotBlank(tooltip)) {
%>
<%-- XSS対応-メタの設定のため対応なし(tooltip) --%>
<span class="ml05"><img src="${m:esc(skinImagePath)}/icon-help-01.png" alt="" class="vm tp"  title="<%=tooltip %>" /></span>
<%
				}
%>
</th>
<td id="id_td_<c:out value="<%=idName %>"/>_<c:out value="<%=np.getPropertyName() %>"/>" class="<c:out value="<%=nestStyle%>" /> property-data">
<%
				PropertyEditor npEditor = np.getEditor();
				npEditor.setPropertyName(editor.getPropertyName() + "." + _pd.getName());
				String path =  EntityViewUtil.getJspPath(npEditor, ViewConst.DESIGN_TYPE_GEM);
				if (path != null) {
					request.setAttribute(Constants.EDITOR_STYLE, nestPropStyle);
					request.setAttribute(Constants.EDITOR_REQUIRED, np.isRequiredNormal());
					request.setAttribute(Constants.EDITOR_DISPLAY_LABEL, displayLabel);
					request.setAttribute(Constants.EDITOR_REF_NEST_PROP_NAME, propName);
					request.setAttribute(Constants.EDITOR_EDITOR, npEditor);
					request.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, _pd);
					if (defaultSearchCond != null) {
						Object sc = defaultSearchCond.get(propName + "." + np.getPropertyName());
						request.setAttribute(Constants.EDITOR_PROP_VALUE, sc);
						request.setAttribute(Constants.EDITOR_DEFAULT_VALUE, sc);
					} else {
						request.removeAttribute(Constants.EDITOR_PROP_VALUE);
						request.removeAttribute(Constants.EDITOR_DEFAULT_VALUE);
					}
					request.setAttribute(Constants.AUTOCOMPLETION_SETTING, np.getAutocompletionSetting());
%>
<jsp:include page="<%=path %>" />
<%
					request.removeAttribute(Constants.EDITOR_STYLE);
					request.removeAttribute(Constants.EDITOR_REQUIRED);
					request.removeAttribute(Constants.EDITOR_DISPLAY_LABEL);
					request.removeAttribute(Constants.EDITOR_REF_NEST_PROP_NAME);
					request.removeAttribute(Constants.EDITOR_EDITOR);
					request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
					request.removeAttribute(Constants.EDITOR_PROP_VALUE);
					request.removeAttribute(Constants.EDITOR_DEFAULT_VALUE);
				}
				if (np.getAutocompletionSetting() != null) {
					request.setAttribute(Constants.AUTOCOMPLETION_DEF_NAME, rootDefName);
					request.setAttribute(Constants.AUTOCOMPLETION_VIEW_NAME, viewName);
					request.setAttribute(Constants.AUTOCOMPLETION_PROP_NAME, npEditor.getPropertyName());
					request.setAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY, 1);
					request.setAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME, propName);
					String autocompletionPath = "/jsp/gem/generic/common/SearchConditionAutocompletion.jsp";
%>
<jsp:include page="<%=autocompletionPath %>"/>
<%
					request.removeAttribute(Constants.AUTOCOMPLETION_SETTING);
					request.removeAttribute(Constants.AUTOCOMPLETION_DEF_NAME);
					request.removeAttribute(Constants.AUTOCOMPLETION_VIEW_NAME);
					request.removeAttribute(Constants.AUTOCOMPLETION_PROP_NAME);
					request.removeAttribute(Constants.AUTOCOMPLETION_MULTIPLICTTY);
					request.removeAttribute(Constants.AUTOCOMPLETION_REF_NEST_PROP_NAME);
					request.removeAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH);
				}
				if (++i != rowNum) {
					//最後のネストは出力しない
%>
</td>
</tr>
<%
				}
			}
		}
	}
%>
