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

<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.SelectProperty" %>
<%@ page import="org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition" %>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.SelectValue"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.EditorValue" %>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewRuntimeException"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>
<%
	SelectPropertyEditor editor = (SelectPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String propName = Constants.SEARCH_COND_PREFIX + editor.getPropertyName();

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	Map<String, List<String>> searchCondMap = (Map<String, List<String>>)request.getAttribute(Constants.SEARCH_COND_MAP);
	String[] _propValue = ViewUtil.getSearchCondValue(searchCondMap, Constants.SEARCH_COND_PREFIX + editor.getPropertyName());
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	@SuppressWarnings("unchecked") List<SelectValue> selectValueList = (List<SelectValue>) request.getAttribute(Constants.EDITOR_SELECT_VALUE_LIST);
	@SuppressWarnings("unchecked") List<LocalizedSelectValueDefinition> localeValueList = (List<LocalizedSelectValueDefinition>) request.getAttribute(Constants.EDITOR_LOCAL_VALUE_LIST);

	String pleaseSelectLabel = "";
	if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
		pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.select.SelectPropertyEditor_Condition.pleaseSelect");
	}

	//カスタムスタイル
	String customStyle = "";
	if (editor.getDisplayType() != SelectDisplayType.LABEL) {
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
		}
	} else {
		if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getOutputCustomStyleScriptKey(), null, null);
		}
	}
	
	// RADIO、CHECKBOX形式の場合のアイテムを縦に並べるような表示するか
	Boolean isItemDirectionColumn = (editor.getDisplayType() == SelectDisplayType.CHECKBOX || editor.getDisplayType() == SelectDisplayType.RADIO) &&
			editor.isItemDirectionColumn();


	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/select/SelectPropertyAutocompletion.jsp");
	}

	if (editor.getDisplayType() == SelectDisplayType.CHECKBOX) {
		List<String> valueList = new ArrayList<String>();
		if (_propValue == null || _propValue.length == 0) {
			if (propValue != null && propValue.length > 0) {
				valueList.addAll(Arrays.asList(propValue));
			}
		} else {
			valueList.addAll(Arrays.asList(_propValue));
		}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>_dispType" value="<%=SelectDisplayType.CHECKBOX.name()%>" />
<ul class="list-check-01">
<%
		for (EditorValue param : editor.getValues()) {
			String label = EntityViewUtil.getSelectPropertyLabel(localeValueList, param, selectValueList);
			String checked = valueList.contains(param.getValue()) ? " checked" : "";
%>
<li <c:if test="<%=isItemDirectionColumn %>">style="display: block;"</c:if>><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=label %>" />">
<input type="checkbox" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=param.getValue() %>"/>" <c:out value="<%=checked %>"/> /><c:out value="<%=label %>" />
</label></li>
<%
		}
%>
</ul>

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		<%-- 全部外す --%>
		$(":checkbox[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").prop("checked",false).trigger("iplassCheckboxPropChange");
<%
		//個別に選択
		if (defaultValue != null && defaultValue.length > 0) {
			for (String strDefault: defaultValue) {
%>
		$(":checkbox[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "'][value='<%=StringUtil.escapeJavaScript(strDefault)%>']").prop("checked",true).trigger("iplassCheckboxPropChange");
<%
			}
		}
%>
	});
<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":checkbox[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']:checked").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
	} else if (editor.getDisplayType() == SelectDisplayType.RADIO) {
		String value = "";
		if (_propValue == null || _propValue.length == 0) {
			if (propValue != null && propValue.length > 0) {
				value = propValue[0];
			}
		} else {
			value = _propValue[0];
		}
		String defaultCheckValue = "";
		if (defaultValue != null && defaultValue.length > 0) {
			defaultCheckValue = defaultValue[0];
		}

		String unspecifiedOptionStyleContent = "";
		if (required) {
			//通常検索で必須Radio条件の「未選定」を非表示する
			unspecifiedOptionStyleContent = "display:none;";
		}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>_dispType" value="<%=SelectDisplayType.RADIO.name()%>" />
<ul class="list-radio-01" data-itemName="<c:out value="<%=propName %>"/>">
<%
		String defaultChecked = " checked";
		for (EditorValue param : editor.getValues()) {
			String label = EntityViewUtil.getSelectPropertyLabel(localeValueList, param, selectValueList);

			String checked = "";
			if (value.equals(param.getValue())) {
				checked = " checked";
				defaultChecked = "";
			}
%>
<li <c:if test="<%=isItemDirectionColumn %>">style="display: block;"</c:if>><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=label %>" />">
<input type="radio" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=param.getValue() %>"/>" <c:out value="<%=checked %>"/> /><c:out value="<%=label %>" />
</label></li>
<%
		}
%>
<li class="unspecified-option" style="<c:out value="<%=unspecifiedOptionStyleContent %>" />">
<label style="<c:out value="<%=customStyle%>"/>" title="${m:rs('mtp-gem-messages', 'generic.editor.select.SelectPropertyEditor_Condition.unspecified')}">
<input type="radio" name="<c:out value="<%=propName %>"/>" value="" <c:out value="<%=defaultChecked %>"/> />${m:rs("mtp-gem-messages", "generic.editor.select.SelectPropertyEditor_Condition.unspecified")}
</label></li>
</ul>

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$(":radio[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val(["<%=StringUtil.escapeJavaScript(defaultCheckValue) %>"]);
		$(":radio[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "'][value='<%=StringUtil.escapeJavaScript(defaultCheckValue) %>']").trigger("click");
	});
<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":radio[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']:checked").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
	} else if (editor.getDisplayType() == SelectDisplayType.SELECT) {
		String value = "";
		if (propValue != null && propValue.length > 0) {
			value = propValue[0];
		}
		String strDefault = "";
		if (defaultValue != null && defaultValue.length > 0) {
			strDefault = defaultValue[0];
		}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>_dispType" value="<%=SelectDisplayType.SELECT.name()%>" />
<select name="<c:out value="<%=propName %>"/>" class="form-size-02 inpbr" style="<c:out value="<%=customStyle%>"/>">
<option value=""><%= pleaseSelectLabel %></option>
<%
		for (EditorValue param : editor.getValues()) {
			String label = EntityViewUtil.getSelectPropertyLabel(localeValueList, param, selectValueList);
			String optStyle = param.getStyle() != null ? param.getStyle() : "";
			String selected = "";
			if (value.equals(param.getValue())) selected = " selected";
%>
<option class="<c:out value="<%=optStyle %>"/>" value="<c:out value="<%=param.getValue() %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=label %>" /></option>
<%
		}
%>
</select>

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val("<%=StringUtil.escapeJavaScript(strDefault) %>");
	});
<%
		if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (typeof val === "undefined" || val == null || val == "") {
			alert(scriptContext.gem.locale.common.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
	} else if (editor.getDisplayType() == SelectDisplayType.LABEL) {
		String strDefault = defaultValue != null && defaultValue.length > 0 ? defaultValue[0] : "";
		strDefault = _propValue != null && _propValue.length > 0 ? _propValue[0] : strDefault;
		String label = null;
		for (EditorValue param : editor.getValues()) {
			if (strDefault.equals(param.getValue())) {
				label = EntityViewUtil.getSelectPropertyLabel(localeValueList, param, selectValueList);
			}
		}
		if (label == null) label = strDefault;
%>
<span  style="<c:out value="<%=customStyle%>"/>">
<c:out value="<%=label %>"/>
<input data-norewrite="true" type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=strDefault %>"/>" />
</span>
<%
	} else if (editor.getDisplayType() == SelectDisplayType.HIDDEN) {
		List<String> valueList = new ArrayList<String>();
		if (_propValue == null || _propValue.length == 0) {
			if (propValue != null && propValue.length > 0) {
				valueList.addAll(Arrays.asList(propValue));
			}
		} else {
			valueList.addAll(Arrays.asList(_propValue));
		}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>_dispType" value="<%=SelectDisplayType.HIDDEN.name()%>" />
<%
		for (String value : valueList) {
%>
<input type="hidden" name="<c:out value="<%=propName%>"/>" value="<c:out value="<%=value%>"/>" data-norewrite="true"/>
<%
		}
	} else {
		throw new EntityViewRuntimeException(propName + " 's editor display type is invalid. editor=["
				+ editor.getClass().getSimpleName() + "]. display type=[" + editor.getDisplayType() + "]");
	}
%>
