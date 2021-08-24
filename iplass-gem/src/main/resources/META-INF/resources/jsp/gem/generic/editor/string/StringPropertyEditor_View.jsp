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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition" %>
<%@ page import="org.iplass.mtp.util.*"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.EditorValue" %>
<%@ page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	String replacePassword(String str) {
		StringBuffer sb = new StringBuffer();
		if (str != null)
			for (int i = 0; i < str.length(); i++)
				sb.append("*");
		return sb.toString();
	}
	EditorValue getValue(StringPropertyEditor editor, String value) {
		if (value == null) return null;
		for (EditorValue tmp : editor.getValues()) {
			if (value.equals(tmp.getValue())) {
				return tmp;
			}
		}
		return null;
	}
	List<EditorValue> getValues(StringPropertyEditor editor, Object value, PropertyDefinition pd) {
		List<EditorValue> values = new ArrayList<EditorValue>();
		if (pd.getMultiplicity() != 1){
			String[] array = value instanceof String[] ? (String[]) value : null;
			if (array != null) {
				for (String tmp : array) {
					EditorValue ev = getValue(editor, tmp);
					if (ev != null) {
						values.add(ev);
					}
				}
			}
		} else {
			String tmp = value instanceof String ? (String) value : null;
			EditorValue ev = getValue(editor, tmp);
			if (ev != null) values.add(ev);
		}
		return values;
	}
%>
<%
	StringPropertyEditor editor = (StringPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);
	OutputType type = (OutputType) request.getAttribute(Constants.OUTPUT_TYPE);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	boolean allowedContent = editor.isAllowedContent();
	Boolean outputHidden = (Boolean) request.getAttribute(Constants.OUTPUT_HIDDEN);
	if (outputHidden == null) outputHidden = false;

	String propName = editor.getPropertyName();

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

	boolean isMultiple = pd.getMultiplicity() != 1;
	
	if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
		//詳細編集か詳細表示の場合だけ表示する
		if (OutputType.EDIT == type || OutputType.VIEW == type) {
%>
<span class="data-label">
<%
			if (isMultiple) {
				String[] array = propValue instanceof String[] ? (String[]) propValue : null;
				if (array != null) {
					int length = array.length;
					for (int i = 0; i < array.length; i++) {
						String str = array[i] != null ? array[i] : "";
						String textId = "id_" + propName + i;
%>
<textarea name="<c:out value="<%=propName%>"/>" rows="5" cols="30" style="<c:out value="<%=customStyle %>"/>" id="<c:out value="<%=textId %>"/>"><c:out value="<%=str %>"/></textarea>
<%
					}
				}
			} else {
				String str = propValue instanceof String ? (String) propValue : "";
%>
<textarea name="<c:out value="<%=propName%>"/>" rows="5" cols="30" style="<c:out value="<%=customStyle %>"/>"><c:out value="<%=str %>"/></textarea>
<%
			}
		}
%>
</span>
<%
		if (request.getAttribute(Constants.RICHTEXT_LIB_LOADED) == null) {
			request.setAttribute(Constants.RICHTEXT_LIB_LOADED, true);
%>
<%@include file="../../../layout/resource/ckeditorResource.inc.jsp" %>
<%
		}
%>
<script type="text/javascript">
$(function() {
<%		if (StringUtil.isNotBlank(editor.getRichtextEditorOption())) { %>
	var opt = <%=editor.getRichtextEditorOption()%>;
	if (typeof opt.readOnly === "undefined") opt.readOnly = true;
<%		} else { %>
	var opt = { readOnly: true, allowedContent:<%=allowedContent%> };
<%		} %>
	if (typeof opt.extraPlugins === "undefined") opt.extraPlugins = "autogrow";
	if (typeof opt.autoGrow_onStartup === "undefined") opt.autoGrow_onStartup = true;
<% 		if (editor.isHideRichtextEditorToolBar()) { %>
	var readyOpt = {
		on: {
			instanceReady: function (evt) {
				<%-- 全体border、ツールバーを非表示 --%>
				var containerId = evt.editor.container.$.id;
				var editorId = evt.editor.id;
				$("#" + containerId).css("border", "none");
				$("#" + editorId + "_top").hide();
				$("#" + editorId + "_bottom").hide();
			}
		}
	}
	$.extend(opt, readyOpt);
<% 		} %>

	$("textarea[name='<%=StringUtil.escapeJavaScript(propName)%>']").ckeditor(
		function() {}, opt
	);
});
</script>
<%
	} else if (editor.getDisplayType() == StringDisplayType.SELECT) {
		
		if (isMultiple) {
			List<EditorValue> values = getValues(editor, propValue, pd);
%>
<ul class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
			for (EditorValue tmp : values) {
				String label = EntityViewUtil.getStringPropertySelectTypeLabel(tmp);
				String style = tmp.getStyle() != null ? tmp.getStyle() : "";
%>
<li class="<c:out value="<%=style %>"/>">
<c:out value="<%=label %>" />
</li>
<%
			}
%>
</ul>
<%
		} else {
			EditorValue ev = getValue(editor, (String) propValue);
			String label = "";
			if (ev != null) {
				label = EntityViewUtil.getStringPropertySelectTypeLabel(ev);
			}
%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>">
<c:out value="<%=label %>"/>
</span>
<%
		}
	} else if (editor.getDisplayType() == StringDisplayType.HIDDEN) {
		if (isMultiple) {
			String[] array = propValue instanceof String[] ? (String[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					String str = array[i] != null ? array[i] : "";
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" />
<%
				}
			}
		} else {
			//単一
			String str = propValue instanceof String ? (String) propValue : "";
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" />
<%
		}
		
	} else {
		//RICHTEXT,SELECT,HIDDEN以外
		
		if (isMultiple) {
%>
<ul class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%
			String[] array = propValue instanceof String[] ? (String[]) propValue : null;
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					String str = array[i] != null ? array[i] : "";
					String _str = str;
					if (editor.getDisplayType() == StringDisplayType.PASSWORD) {
						str = replacePassword(str);
					}
					str = StringUtil.escapeXml10(str, true);
					if (editor.getDisplayType() == StringDisplayType.TEXTAREA || editor.getDisplayType() == StringDisplayType.LABEL) {
						str = str.replaceAll("\r\n", "<BR>").replaceAll("\n", "<BR>").replaceAll("\r", "<BR>").replaceAll(" ", "&nbsp;");
					}
%>
<li>
<%-- XSS対応-エスケープ処理済み＆HTMLタグ出力のため対応なし --%>
<%=str %>
<%
					if (outputHidden) {
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_str %>"/>" />
<%
					}
%>
</li>
<%
				}
			}
%>
</ul>
<%
		} else {
			//単一
			String str = propValue instanceof String ? (String) propValue : "";
			String _str = str;
			if (editor.getDisplayType() == StringDisplayType.PASSWORD) {
				str = replacePassword(str);
			}
			str = StringUtil.escapeXml10(str, true);
			if (editor.getDisplayType() == StringDisplayType.TEXTAREA || editor.getDisplayType() == StringDisplayType.LABEL) {
				str = str.replaceAll("\r\n", "<BR>").replaceAll("\n", "<BR>").replaceAll("\r", "<BR>").replaceAll(" ", "&nbsp;");
			}
%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%-- XSS対応-エスケープ処理済み＆HTMLタグ出力のため対応なし --%>
<%=str %>
<%
			if (outputHidden) {
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=_str %>"/>" />
<%
			}
%>
</span>
<%
		}
	}
%>
