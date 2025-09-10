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
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.EditorValue" %>
<%@ page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.StringPropertyEditor.StringDisplayType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%!
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
	Object getDefaultValue(StringPropertyEditor editor, PropertyDefinition pd) {
		String defaultValue = editor.getDefaultValue();
		if (defaultValue != null) {
			if (pd.getMultiplicity() != 1) {
				String[] vals = defaultValue.split(",");
				int length = vals.length > pd.getMultiplicity() ? pd.getMultiplicity() : vals.length;
				String[] ret = new String[length];
				for (int i = 0; i < length; i++) {
					ret[i] = vals[i];
				}
				return ret;
			} else {
				return defaultValue;
			}
		}
		return null;
	}
%>
<%
	StringPropertyEditor editor = (StringPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);
	Integer colNum = (Integer) request.getAttribute(Constants.COL_NUM);

	Boolean nest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST);
	Boolean nestDummyRow = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST_DUMMY_ROW);

	Boolean isVirtual = (Boolean) request.getAttribute(Constants.IS_VIRTUAL);
	if (isVirtual == null) isVirtual = false;

	boolean isInsert = Constants.EXEC_TYPE_INSERT.equals(execType);
	String propName = editor.getPropertyName();
	String escapedPropName = StringUtil.escapeJavaScript(propName);
	AuthContext auth = AuthContext.getCurrentContext();
	boolean allowedContent = editor.isAllowedContent();
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

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/string/StringPropertyAutocompletion.jsp");
	}

	//詳細編集
	if (editor.getDisplayType() == StringDisplayType.SELECT) {
		//選択型

		String cls = "form-size-02 inpbr";
		if (nest != null && nest) {
			//cls = "form-size-08 inpbr";
		}

		if (isMultiple) {
			//複数
			String[] array = propValue instanceof String[] ? (String[]) propValue : null;
			String ulId = "ul_" + propName;

			if (updatable) {
				List<String> values = new ArrayList<String>();
				if (array != null && array.length > 0) values.addAll(Arrays.asList(array));
%>
<ul id="<c:out value="<%=ulId %>"/>">
<select name="<c:out value="<%=propName %>"/>" size="6" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>" multiple>
<%
				for (EditorValue tmp : editor.getValues()) {
					String label = EntityViewUtil.getStringPropertySelectTypeLabel(tmp);
					String selected = values.contains(tmp.getValue()) ? " selected" : "";
					String optStyle = tmp.getStyle() != null ? tmp.getStyle() : "";
%>
<option class="<c:out value="<%=optStyle %>"/>" title="<c:out value="<%=label %>" />" value="<c:out value="<%=tmp.getValue() %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=label %>" /></option>
<%
				}
%>
</select>
<c:set var="multiplicity" value="<%= pd.getMultiplicity() %>" />
<p class="error-multiplicity" style="display: none;">
<span class="error">${m:rsp("mtp-gem-messages","generic.editor.string.StringPropertyEditor_Edit.maxMultipleError",multiplicity)}</span>
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
			} else {
				//更新不可
				List<EditorValue> values = getValues(editor, propValue, pd);
%>
<ul style="<c:out value="<%=customStyle%>"/>">
<%
				for (EditorValue tmp : values) {
					String optStyle = tmp.getStyle() != null ? tmp.getStyle() : "";
%>
<li class="<c:out value="<%=optStyle %>"/>">
<c:out value="<%=tmp.getLabel() %>" />
<input type="hidden" name="<c:out value="<%=propName%>"/>" value="<c:out value="<%=tmp.getValue()%>"/>" />
</li>
<%
				}
%>
</ul>
<%
			}
		} else {
			//単一
			String str = propValue instanceof String ? (String) propValue : "";
			if (updatable) {
				String pleaseSelectLabel = "";
				if (ViewUtil.isShowPulldownPleaseSelectLabel()) {
					pleaseSelectLabel = GemResourceBundleUtil.resourceString("generic.editor.string.StringPropertyEditor_Edit.pleaseSelect");
				}
%>
<select name="<c:out value="<%=propName %>"/>" size="1" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>">
<option value="" title="<%= pleaseSelectLabel %>"><%= pleaseSelectLabel %></option>
<%
				for (EditorValue tmp : editor.getValues()) {
					String label = EntityViewUtil.getStringPropertySelectTypeLabel(tmp);
					String selected = str.equals(tmp.getValue()) ? " selected" : "";
					String optStyle = tmp.getStyle() != null ? tmp.getStyle() : "";
%>
<option class="<c:out value="<%=optStyle %>"/>" title="<c:out value="<%=label %>" />" value="<c:out value="<%=tmp.getValue() %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=label %>" /></option>
<%
				}
%>
</select>
<%
			} else {
				//更新不可
				EditorValue ev = getValue(editor, str);
				String label = ev != null ? ev.getLabel() != null ? ev.getLabel() : "" : "";
				String val = ev != null ? ev.getValue() != null ? ev.getValue() : "" : "";

				if (StringUtil.isNotEmpty(customStyle)) {
%>
<span style="<c:out value="<%=customStyle %>"/>">
<%
				}
%>
<c:out value="<%=label %>"/>
<input type="hidden" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=propValue %>"/>" />
<%
				if (StringUtil.isNotEmpty(customStyle)) {
%>
</span>
<%
				}
			}
		}
	} else {
		//選択型以外

		String cls = "form-size-01 inpbr";
		if (nest != null && nest) {
			//cls = "form-size-08 inpbr";
		}
		String maxlength = "";
		String inputType = "text";
		String inputPattern = "";
		if (editor.getDisplayType() == StringDisplayType.TEXT) {
			//テキスト
			if (editor.getMaxlength() > 0) {
				maxlength = "maxlength=\"" + editor.getMaxlength() + "\"";
			}
			if (StringUtil.isNotEmpty(editor.getInputType())) {
				inputType = editor.getInputType();
			}
		}
		if (editor.getDisplayType() == StringDisplayType.TEXT 
				|| editor.getDisplayType() == StringDisplayType.PASSWORD) {
			if (StringUtil.isNotEmpty(editor.getInputPattern())) {
				inputPattern = "pattern=\"" + StringUtil.escapeHtml(editor.getInputPattern()) + "\"";
			}
			if (editor.getHtmlValidationMessage() != null) {
				String editorId = (String)request.getAttribute(Constants.EDITOR_UNIQUE_ID);
%>
<script type="text/javascript">
$(function() {
<%				if (StringUtil.isNotEmpty(editor.getHtmlValidationMessage().getTypeMismatch())) {
					String message = TemplateUtil.getMultilingualString(editor.getHtmlValidationMessage().getTypeMismatch(), editor.getHtmlValidationMessage().getLocalizedTypeMismatchList());
%>
	addHtmlValidationMessage("<%=StringUtil.escapeJavaScript(editorId)%>", "typeMismatch", "<%=StringUtil.escapeJavaScript(message)%>");
<%				}
				if (StringUtil.isNotEmpty(editor.getHtmlValidationMessage().getPatternMismatch())) {
					String message = TemplateUtil.getMultilingualString(editor.getHtmlValidationMessage().getPatternMismatch(), editor.getHtmlValidationMessage().getLocalizedPatternMismatchList());
%>
	addHtmlValidationMessage("<%=StringUtil.escapeJavaScript(editorId)%>", "patternMismatch", "<%=StringUtil.escapeJavaScript(message)%>");
<%				} %>
});
</script>
<%			}
		}

		if (editor.getDisplayType() != StringDisplayType.LABEL
				&& editor.getDisplayType() != StringDisplayType.HIDDEN && updatable) {

			if (isMultiple) {
				//複数
				String ulId = "ul_" + propName;
				String dummyRowId = "id_li_" + propName + "Dummmy";
				String selector = null;
				String toggleAddBtnFunc = "toggleAddBtn_" + escapedPropName;
				if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
					request.setAttribute(Constants.EDITOR_RICHTEXT_LIBRARY, editor.getRichTextLibrary());
%>
<%@include file="../../../layout/resource/richtextEditorResource.inc.jsp" %>
<%
				}
%>
<ul id="<c:out value="<%=ulId %>"/>" class="mb05">

<% 				// ダミー行出力 %>
<li id="<c:out value="<%=dummyRowId %>"/>" class="list-add" style="display: none;">
<%
				if (editor.getDisplayType() == StringDisplayType.TEXT) {
					//テキスト
					selector = "input[type='" + inputType + "']";
%>
<input type="<%=inputType %>" class="<c:out value="<%=cls %>"/> custom-type-text" style="<c:out value="<%=customStyle%>"/>" <%=maxlength%> <%=inputPattern%> 
	onblur="typeValidate(this)" oninvalid="typeInvalid(arguments[0], this)"/>
<%
				} else if (editor.getDisplayType() == StringDisplayType.TEXTAREA) {
					//テキストエリア
					selector = "textarea";
%>
<textarea class="form-size-05 inpbr width-constraint" style="<c:out value="<%=customStyle%>"/>" rows="5" cols="30"></textarea>
<%
				} else if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
					//リッチテキスト
					selector = "textarea";
%>
<textarea class="" style="<c:out value="<%=customStyle%>"/>" rows="5" cols="30"></textarea>
<%
				} else if (editor.getDisplayType() == StringDisplayType.PASSWORD) {
					//パスワード
					selector = "input[type='password']";
%>
<input type="password" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>"  <%=inputPattern%>
	onblur="typeValidate(this)" oninvalid="typeInvalid(arguments[0], this)"/>
<%
				}
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.string.StringPropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" />
</li>
<%
				// 既存値出力
				String[] array = propValue instanceof String[] ? (String[]) propValue : null;
				int length = 0;
				if (array != null) {
					length = array.length;
					for (int i = 0; i < array.length; i++) {
						String str = array[i] != null ? array[i] : "";
						String liId = "li_" + propName + i;
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<%
						if (editor.getDisplayType() == StringDisplayType.TEXT) {
							//テキスト
%>
<input type="<%=inputType %>" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" class="<c:out value="<%=cls %>"/> custom-type-text" style="<c:out value="<%=customStyle%>"/>" <%=maxlength%> <%=inputPattern%> 
	onblur="typeValidate(this)" oninvalid="typeInvalid(arguments[0], this)"/>
<%
						} else if (editor.getDisplayType() == StringDisplayType.TEXTAREA) {
							//テキストエリア
%>
<textarea name="<c:out value="<%=propName %>"/>" class="form-size-05 inpbr width-constraint" style="<c:out value="<%=customStyle%>"/>" rows="5" cols="30"><c:out value="<%=str %>"/></textarea>
<%
						} else if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
							//リッチテキスト
							String textId = "id_" + propName + i;
%>
<textarea name="<c:out value="<%=propName %>"/>" style="<c:out value="<%=customStyle%>"/>" rows="5" cols="30" id="<c:out value="<%=textId %>"/>"><c:out value="<%=str %>"/></textarea>
<%
						} else if (editor.getDisplayType() == StringDisplayType.PASSWORD) {
							//パスワード
%>
<input type="password" name="<c:out value="<%=propName %>"/>" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>" value="<c:out value="<%=str %>"/>" <%=inputPattern%>
	onblur="typeValidate(this)" oninvalid="typeInvalid(arguments[0], this)"/>
<%
						}
%>
<%
						// 削除ボタン
						if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.string.StringPropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" 
    onclick="delete_<%=escapedPropName + i %>('<%=StringUtil.escapeJavaScript(liId)%>')" />

<script type="text/javascript">
function delete_<%=escapedPropName + i%>(liId) {
	deleteRichtextItem_<%=escapedPropName%>("id_<%=escapedPropName + i%>");
	deleteItem(liId, <%=toggleAddBtnFunc%>);
}
</script>
<%
						} else {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.string.StringPropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" 
    onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>', <%=toggleAddBtnFunc%>)" />
<%
						}
%>
</li>
<%
					}
					if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
						request.setAttribute(Constants.EDITOR_RICHTEXT_LIBRARY, editor.getRichTextLibrary());
						request.setAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_CONTENT, allowedContent);
						request.setAttribute(Constants.EDITOR_RICHTEXT_EDITOR_OPTION, editor.getRichtextEditorOption());
						request.setAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME, propName);
%>
<jsp:include page="richtext/RichtextEditor_Edit.jsp" />

<%
						request.removeAttribute(Constants.EDITOR_RICHTEXT_LIBRARY);
						request.removeAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_CONTENT);
						request.removeAttribute(Constants.EDITOR_RICHTEXT_EDITOR_OPTION);
						request.removeAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME);
					}
				}
%>
</ul>

<%
				if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
					request.setAttribute(Constants.EDITOR_RICHTEXT_LIBRARY, editor.getRichTextLibrary());
					request.setAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_CONTENT, allowedContent);
					request.setAttribute(Constants.EDITOR_RICHTEXT_EDITOR_OPTION, editor.getRichtextEditorOption());
					request.setAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME, propName);

					// 削除処理、追加処理を定義
%>
<jsp:include page="richtext/RichtextEditor_Delete.jsp" />
<jsp:include page="richtext/RichtextEditor_Add.jsp" />
<% 
					request.removeAttribute(Constants.EDITOR_RICHTEXT_LIBRARY);
					request.removeAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_CONTENT);
					request.removeAttribute(Constants.EDITOR_RICHTEXT_EDITOR_OPTION);
					request.setAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME, propName);
				}
%>
<script type="text/javascript">
$(function() {
	$("#id_addBtn_<%=escapedPropName%>").on("click", function() {
		addTextItem("<%=StringUtil.escapeJavaScript(ulId)%>", <%=(pd.getMultiplicity() + 1)%>, "<%=StringUtil.escapeJavaScript(dummyRowId)%>", "<%=escapedPropName%>"
				, "id_count_<%=escapedPropName%>", "<%=selector%>"
<%
				if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
%>
				, function(elem) {
					var count = $("#id_count_<%=escapedPropName%>").val();
					var $delBtn = $(elem).next();
					$delBtn.off("click");
					$delBtn.on("click", function() {
						deleteRichtextItem_<%=escapedPropName%>("id_<%=escapedPropName%>" + count);
						deleteItem($(this).parent().attr("id"), <%=toggleAddBtnFunc%>);
					});

					$(elem).attr("id", "id_<%=escapedPropName%>" + count);

					addRichtextItem_<%=escapedPropName%>("id_<%=escapedPropName%>" + count);

					<%=toggleAddBtnFunc%>();
				}
<%
				} else {
%>
				, <%=toggleAddBtnFunc%>
<% 
				}
%>
		, <%=toggleAddBtnFunc%>);
	});
});
function <%=toggleAddBtnFunc%>(){
	var display = $("#<%=StringUtil.escapeJavaScript(ulId)%> li:not(:hidden)").length < <%=pd.getMultiplicity()%>;
	$("#id_addBtn_<%=escapedPropName%>").toggle(display);

	var $parent = $("#<%=StringUtil.escapeJavaScript(ulId)%>").closest(".property-data");
	if ($(".validate-error", $parent).length === 0) {
		$(".format-error", $parent).remove();
	}
}
</script>
<%
				String addBtnStyle = "";
				if (array != null && array.length >= pd.getMultiplicity()) addBtnStyle = "display: none;";
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.string.StringPropertyEditor_Edit.add')}" class="gr-btn-02 add-btn" style="<%=addBtnStyle%>" id="id_addBtn_<c:out value="<%=propName %>"/>" />
<input type="hidden" id="id_count_<c:out value="<%=propName %>"/>" value="<c:out value="<%=length %>"/>" />
<%
			} else {
				//単一
				String str = propValue instanceof String ? (String) propValue : "";
				if (editor.getDisplayType() == StringDisplayType.TEXT) {
					//テキスト
%>
<input type="<%=inputType %>" name="<c:out value="<%=propName%>"/>" value="<c:out value="<%=str %>"/>" class="<c:out value="<%=cls %>"/> custom-type-text" style="<c:out value="<%=customStyle%>"/>" <%=maxlength%> <%=inputPattern%> 
	onblur="typeValidate(this)" oninvalid="typeInvalid(arguments[0], this)"/>
<%
				} else if (editor.getDisplayType() == StringDisplayType.TEXTAREA) {
					//テキストエリア
%>
<textarea name="<c:out value="<%=propName%>"/>" rows="5" cols="30" class="form-size-05 inpbr width-constraint" style="<c:out value="<%=customStyle%>"/>"><c:out value="<%=str %>"/></textarea>
<%
				} else if (editor.getDisplayType() == StringDisplayType.RICHTEXT) {
					//リッチテキスト
					request.setAttribute(Constants.EDITOR_RICHTEXT_LIBRARY, editor.getRichTextLibrary());

					String textId = "id_" + propName;
%>
<textarea name="<c:out value="<%=propName%>"/>" style="<c:out value="<%=customStyle%>"/>" id="<c:out value="<%=textId %>"/>" rows="5" cols="30" ><c:out value="<%=str %>"/></textarea>
<%@include file="../../../layout/resource/richtextEditorResource.inc.jsp" %>

<%
					if (nestDummyRow == null || !nestDummyRow) {
						// NestTableダミー行は適用しない＆追加(コピー)時は追加処理側から適用

						request.setAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_CONTENT, allowedContent);
						request.setAttribute(Constants.EDITOR_RICHTEXT_EDITOR_OPTION, editor.getRichtextEditorOption());
						request.setAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME, propName);
%>
<jsp:include page="richtext/RichtextEditor_Edit.jsp" />
<%
					} else {
						// NestTableダミー行の場合は、追加時の適用処理を設定

						request.setAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_CONTENT, allowedContent);
						request.setAttribute(Constants.EDITOR_RICHTEXT_EDITOR_OPTION, editor.getRichtextEditorOption());
						// NestTableではEditorのプロパティ名にIndexが付くため、プロパティ定義から設定
						request.setAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME, pd.getName());
%>
<jsp:include page="richtext/RichtextEditor_NestTable.jsp" />
<%
					}

					request.removeAttribute(Constants.EDITOR_RICHTEXT_LIBRARY);
					request.removeAttribute(Constants.EDITOR_RICHTEXT_ALLOWED_CONTENT);
					request.removeAttribute(Constants.EDITOR_RICHTEXT_EDITOR_OPTION);
					request.removeAttribute(Constants.EDITOR_RICHTEXT_TARGET_NAME);

				} else if (editor.getDisplayType() == StringDisplayType.PASSWORD) {
					//パスワード
%>
<input type="password" name="<c:out value="<%=propName %>"/>" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>" value="<c:out value="<%=str %>"/>" <%=inputPattern%>
	onblur="typeValidate(this)" oninvalid="typeInvalid(arguments[0], this)"/>
<%
				}
			}
		} else {
			//LABELかHIDDENか更新不可
			if (editor.getDisplayType() != StringDisplayType.HIDDEN) {
				request.setAttribute(Constants.OUTPUT_HIDDEN, true);
			}
%>
<jsp:include page="StringPropertyEditor_View.jsp"></jsp:include>
<%
			if (editor.getDisplayType() != StringDisplayType.HIDDEN) {
				request.removeAttribute(Constants.OUTPUT_HIDDEN);
			}
		}
	}
%>
