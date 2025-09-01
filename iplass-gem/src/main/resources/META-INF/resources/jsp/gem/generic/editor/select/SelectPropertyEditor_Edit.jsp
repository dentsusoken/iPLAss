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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.SelectValue" %>
<%@ page import="org.iplass.mtp.entity.definition.properties.SelectProperty" %>
<%@ page import="org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition" %>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.EditorValue" %>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType" %>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%!
	Object getDefaultValue(SelectPropertyEditor editor, PropertyDefinition pd) {
		String defaultValue = editor.getDefaultValue();
		if (defaultValue != null) {
			if (pd.getMultiplicity() != 1) {
				String[] vals = defaultValue.split(",");
				int length = vals.length > pd.getMultiplicity() ? pd.getMultiplicity() : vals.length;
				SelectValue[] ret = new SelectValue[length];
				for (int i = 0; i < length; i++) {
					ret[i] = getDefaultValue(editor.getValue(vals[i]));
				}
				return ret;
			} else {
				return getDefaultValue(editor.getValue(defaultValue));
			}
		}
		return null;
	}
	SelectValue getDefaultValue(EditorValue value) {
		if (value != null) {
			return new SelectValue(value.getValue());
		}
		return null;
	}
%>
<%
	SelectPropertyEditor editor = (SelectPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

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

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/select/SelectPropertyAutocompletion.jsp");
	}

	//タイプ毎に出力内容かえる
	if (editor.getDisplayType() != SelectDisplayType.LABEL 
			&& editor.getDisplayType() != SelectDisplayType.HIDDEN && updatable) {
		
		boolean isMultiple = pd.getMultiplicity() != 1;

		//詳細編集
		List<String> values = new ArrayList<String>();
		if (isMultiple){
			SelectValue[] array = propValue instanceof SelectValue[] ? (SelectValue[]) propValue : null;
			if (array != null) {
				for (SelectValue tmp : array) {
					if (tmp != null) {
						values.add(tmp.getValue());
					}
				}
			}
		} else {
			SelectValue tmp = propValue instanceof SelectValue ? (SelectValue) propValue : null;
			if (tmp != null) values.add(tmp.getValue());
		}

		//カスタムスタイル
		String customStyle = "";
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
		}
		
		@SuppressWarnings("unchecked") List<SelectValue> selectValueList = (List<SelectValue>)request.getAttribute(Constants.EDITOR_SELECT_VALUE_LIST);
		@SuppressWarnings("unchecked") List<LocalizedSelectValueDefinition> localeValueList = (List<LocalizedSelectValueDefinition>)request.getAttribute(Constants.EDITOR_LOCAL_VALUE_LIST);

		if (editor.getDisplayType() == SelectDisplayType.SELECT) {
			//セレクトボックス
			String size = isMultiple ? "5" : "1";
			String cls = isMultiple ? "form-size-12 inpbr" : "form-size-02 inpbr";
			String multiple = isMultiple ? " multiple" : "";
			String ulId = "ul_" + propName;
			
%>
<ul id="<c:out value="<%=ulId %>"/>">
<select name="<c:out value="<%=propName %>"/>" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>" size="<c:out value="<%=size %>"/>" <c:out value="<%=multiple %>"/> >
<%
			if (!isMultiple){
				String pleaseSelectLabel = "";
				if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
					pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.select.SelectPropertyEditor_Edit.pleaseSelect");
				}
%>
<option value=""><%= pleaseSelectLabel %></option>
<%
			}
			for (EditorValue tmp : editor.getValues()) {
				String label = EntityViewUtil.getSelectPropertyLabel(localeValueList, tmp, selectValueList);
				String selected = values.contains(tmp.getValue()) ? " selected" : "";
				String optStyle = tmp.getStyle() != null ? tmp.getStyle() : "";
%>
<option class="<c:out value="<%=optStyle %>"/>" title="<c:out value="<%=label%>"/>" value="<c:out value="<%=tmp.getValue() %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=label %>" /></option>
<%
			}
%>
</select>
<c:set var="multiplicity" value="<%= pd.getMultiplicity() %>" />
<p class="error-multiplicity" style="display: none;">
<span class="error">${m:rsp("mtp-gem-messages","generic.editor.select.SelectPropertyEditor_Edit.maxMultipleError",multiplicity)}</span>
</p>
</ul>
<script>
$(function() {
	const $ul = $("#" + es("<%=StringUtil.escapeJavaScript(ulId)%>"));
	const $errorMsg = $(".error-multiplicity", $ul); 

	$("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").on("change", function() {
        const selectedCount = $(this).find("option:selected").length;

        if (selectedCount > <%=pd.getMultiplicity()%>) {
        	$errorMsg.show();  
		} else {
			$errorMsg.hide(); 
		}
    });
    
	<%-- common.js --%>
	addEditValidator(function() {
		const selectedCount = $("select[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "'] option:selected", $ul).length;
		if (selectedCount > <%=pd.getMultiplicity()%>) {
			alert("${m:rs("mtp-gem-messages","command.generic.detail.DetailCommandBase.inputErr")}");
			return false;
		}
		return true;
	});
});
</script>
<%
		} else if (editor.getDisplayType() == SelectDisplayType.CHECKBOX
				|| editor.getDisplayType() == SelectDisplayType.RADIO) {
			//チェックボックスorラジオボタン
			String ulId = "ul_" + propName;
			String cls = "list-radio-01";
			if (editor.getDisplayType() == SelectDisplayType.CHECKBOX) cls = "list-check-01";
%>
<ul id="<c:out value="<%=ulId %>"/>" class="<c:out value="<%=cls %>"/>" data-itemName="<c:out value="<%=propName %>"/>">
<%
			for (EditorValue tmp : editor.getValues()) {
				String label = EntityViewUtil.getSelectPropertyLabel(localeValueList, tmp, selectValueList);
				String optStyle = tmp.getStyle() != null ? tmp.getStyle() : "";
%>
<li <c:if test="<%=editor.isItemDirectionColumn() %>">style="display: block;"</c:if>><label style="<c:out value="<%=customStyle%>"/>" title="<c:out value="<%=label %>" />">
<%
				String checked = values.contains(tmp.getValue()) ? " checked" : "";
				if (isMultiple) {
					//チェックボックス
%>
<input type="checkbox" name="<c:out value="<%=propName %>"/>" class="<c:out value="<%=optStyle %>"/>" value="<c:out value="<%=tmp.getValue() %>"/>" <c:out value="<%=checked %>"/> /><c:out value="<%=label %>" />
<%
				} else {
					//ラジオボタン
					
					//選択解除可能か
					String cssTogglable = required ? "" : "radio-togglable ";
					
%>
<input type="radio" name="<c:out value="<%=propName %>"/>" class="<%=cssTogglable %><c:out value="<%=optStyle %>"/>" value="<c:out value="<%=tmp.getValue() %>"/>" <c:out value="<%=checked %>"/> /><c:out value="<%=label %>" />
<%
				}
%>
</label></li>
<%
			}
%>
<c:set var="multiplicity" value="<%= pd.getMultiplicity() %>" />
<p class="error-multiplicity" style="display: none;">
<span class="error">${m:rsp("mtp-gem-messages","generic.editor.select.SelectPropertyEditor_Edit.maxMultipleError",multiplicity)}</span>
</p>
</ul>
<script>
$(function() {
	const $ul = $("#" + es("<%=StringUtil.escapeJavaScript(ulId)%>"));
	const $errorMsg = $(".error-multiplicity", $ul); 
	
	$(":checkbox[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']", $ul).on("change", function() {

		const checkedCount = $(":checkbox[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']:checked", $ul).length;
		if (checkedCount > <%=pd.getMultiplicity()%>) {
			$errorMsg.show();  
		} else {
			$errorMsg.hide(); 
		}
	});

	<%-- common.js --%>
	addEditValidator(function() {
		const checkedCount = $(":checkbox[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']:checked", $ul).length;
		if (checkedCount > <%=pd.getMultiplicity()%>) {
			alert("${m:rs("mtp-gem-messages","command.generic.detail.DetailCommandBase.inputErr")}");
			return false;
		}
		return true;
	});
}); 
</script>
<%
		}
	} else {
		//LABELかHIDDENか更新不可
		
		if (editor.getDisplayType() != SelectDisplayType.HIDDEN) {
			request.setAttribute(Constants.OUTPUT_HIDDEN, true);
		}
%>
<jsp:include page="SelectPropertyEditor_View.jsp"></jsp:include>
<%
		if (editor.getDisplayType() != SelectDisplayType.HIDDEN) {
			request.removeAttribute(Constants.OUTPUT_HIDDEN);
		}
	}
%>
