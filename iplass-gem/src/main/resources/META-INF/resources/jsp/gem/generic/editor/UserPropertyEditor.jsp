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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.Map"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.entity.definition.properties.StringProperty"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewRuntimeException"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.UserPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.UserPropertyEditor.UserDisplayType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>

<%
	UserPropertyEditor editor = (UserPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);
	Object defaultValue = request.getAttribute(Constants.EDITOR_DEFAULT_VALUE);

	String rootDefName = (String) request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String) request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);
	Boolean required = (Boolean) request.getAttribute(Constants.EDITOR_REQUIRED);
	if (required == null) required = false;

	@SuppressWarnings("unchecked") Map<String, Entity> userMap = (Map<String, Entity>) request.getAttribute(Constants.USER_INFO_MAP);

	String propName = editor.getPropertyName();
	if (pd == null || !(pd instanceof StringProperty)) {
		//定義がStringPropertyでなければ表示不可
		throw new EntityViewRuntimeException(propName + " 's editor is unsupport "
				+ (pd != null ? pd.getClass().getSimpleName() : "(unknown)") + " type." );
	}

	if (OutputType.EDIT == type || OutputType.VIEW == type) {
		//詳細編集 or 詳細表示
		String str = "";
		if (propValue != null) {
			if (userMap != null) {
				Entity user = userMap.get(propValue.toString());
				if (user != null) {
					str = user.getName();
				} else {
					str = propValue.toString();
				}
			} else {
				str = propValue.toString();
			}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=propValue.toString() %>"/>" />
<%
		}

		if (editor.getDisplayType() != UserDisplayType.HIDDEN) {

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
<span class="data-label" style="<c:out value="<%=customStyle %>"/>">
<c:out value="<%=str %>"/>
</span>
<%
		}
	} else if (OutputType.SEARCHCONDITION == type) {
		//検索条件
		propName = Constants.SEARCH_COND_PREFIX + propName;

		String[] _propValue = propValue instanceof String[] ? (String[]) propValue : null;
		String str = "";
		if (_propValue != null && _propValue.length > 0) {
			str = _propValue[0];
		}

		String strDefault = "";
		String[] _defaultValue = defaultValue instanceof String[] ? (String[]) defaultValue : null;
		if (_defaultValue != null && _defaultValue.length > 0) {
			strDefault = _defaultValue[0];
		}

		if (editor.getDisplayType() != UserDisplayType.HIDDEN) {

			//カスタムスタイル
			String customStyle = "";
			if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
				customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), null, null);
			}
%>
<input type="text" class="form-size-04 inpbr" style="<c:out value="<%=customStyle%>"/>" value="<c:out value="<%=str %>"/>" name="<c:out value="<%=propName %>"/>" />

<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val("<%=StringUtil.escapeJavaScript(strDefault) %>");
	});
<%
			if (required) {
%>
	<%-- common.js --%>
	addNormalValidator(function() {
		var val = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
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
		} else {
			//HIDDEN
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" />
<%
		}
	} else if (OutputType.SEARCHRESULT == type) {
		//検索結果
		String str = "";
		if (propValue != null) {
			if (userMap != null) {
				Entity user = userMap.get(propValue.toString());
				if (user != null) {
					str = user.getName();
				} else {
					str = propValue.toString();
				}
			} else {
				str = propValue.toString();
			}
		}

		if (editor.getDisplayType() != UserDisplayType.HIDDEN) {
%>
<span class="data-label">
<c:out value="<%=str %>"/>
</span>
<%
		} else {
			//HIDDEN
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" />
<%
		}
	}
	request.removeAttribute(Constants.EDITOR_EDITOR);
	request.removeAttribute(Constants.EDITOR_PROP_VALUE);
	request.removeAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	request.removeAttribute(Constants.EDITOR_DISPLAY_LABEL);
%>
