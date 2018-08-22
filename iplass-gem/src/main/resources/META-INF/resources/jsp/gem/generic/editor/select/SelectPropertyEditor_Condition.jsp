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

<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.SelectProperty" %>
<%@ page import="org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition" %>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.SelectValue"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.EditorValue" %>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>
<%!
	String[] getSelectValue(String searchCond, String key) {
		ArrayList<String> list = new ArrayList<String>();
		if (searchCond != null && searchCond.indexOf(key) > -1) {
			String[] split = searchCond.split("&");
			if (split != null && split.length > 0) {
				for (String tmp : split) {
					String[] kv = tmp.split("=");
					if (kv != null && kv.length > 1 && key.equals(kv[0])) {
						list.add(kv[1]);
					}
				}
			}
		}
		return list.size() > 0 ? list.toArray(new String[list.size()]) : null;
	}
%>
<%
	SelectPropertyEditor editor = (SelectPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	String propName = Constants.SEARCH_COND_PREFIX + editor.getPropertyName();

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);

	String[] propValue = (String[]) request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String[] defaultValue = (String[]) request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	String searchCond = request.getParameter(Constants.SEARCH_COND);
	String[] _propValue = getSelectValue(searchCond, Constants.SEARCH_COND_PREFIX + editor.getPropertyName());
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
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
	}

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
<ul class="list-check-01">
<%
		for (EditorValue param : editor.getValues()) {
			String label = EntityViewUtil.getSelectPropertyLabel(localeValueList, param, selectValueList);
			String checked = valueList.contains(param.getValue()) ? " checked" : "";
%>
<li><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=label %>" />">
<input type="checkbox" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=param.getValue() %>"/>" <c:out value="<%=checked %>"/> /><c:out value="<%=label %>" />
</label></li>
<%
		}
%>
</ul>
<input type="hidden" name="<c:out value="<%=propName %>"/>_dispType" value="checkbox" />

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
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
		if (propValue != null && propValue.length > 0) {
			value = propValue[0];
		}
		String defaultCheckValue = "";
		if (defaultValue != null && defaultValue.length > 0) {
			defaultCheckValue = defaultValue[0];
		}
%>
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
<li><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=label %>" />">
<input type="radio" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=param.getValue() %>"/>" <c:out value="<%=checked %>"/> /><c:out value="<%=label %>" />
</label></li>
<%
		}
%>
<li><label style="<c:out value="<%=customStyle%>"/>" title="${m:rs('mtp-gem-messages', 'generic.editor.select.SelectPropertyEditor_Condition.unspecified')}">
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
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
		String value = "";
		if (propValue != null && propValue.length > 0) {
			value = propValue[0];
		}
		String strDefault = "";
		if (defaultValue != null && defaultValue.length > 0) {
			strDefault = defaultValue[0];
		}
%>
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
			alert(scriptContext.locale.requiredMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
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
	}
%>
