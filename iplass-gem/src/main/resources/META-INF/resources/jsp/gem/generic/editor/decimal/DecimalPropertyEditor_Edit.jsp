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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DecimalPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>
<%!
	Object getDefaultValue(DecimalPropertyEditor editor, PropertyDefinition pd) {
		String defaultValue = editor.getDefaultValue();
		if (defaultValue != null) {
			if (pd.getMultiplicity() != 1) {
				String[] vals = defaultValue.split(ViewUtil.getGroupingSeparator());
				int length = vals.length > pd.getMultiplicity() ? pd.getMultiplicity() : vals.length;
				BigDecimal[] ret = new BigDecimal[length];
				for (int i = 0; i < length; i++) {
					ret[i] = getDefaultValue(vals[i]);
				}
				return ret;
			} else {
				return getDefaultValue(defaultValue);
			}
		}
		return null;
	}
	BigDecimal getDefaultValue(String value) {
		if (value != null) {
			try {
				return new BigDecimal(value);
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}
%>
<%
	DecimalPropertyEditor editor = (DecimalPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);
	String displayLabel = (String) request.getAttribute(Constants.EDITOR_DISPLAY_LABEL);

	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);
	Boolean nest = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST);
	Boolean nestDummyRow = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST_DUMMY_ROW);

	Boolean isVirtual = (Boolean) request.getAttribute(Constants.IS_VIRTUAL);
	if (isVirtual == null) isVirtual = false;

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

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/decimal/DecimalPropertyAutocompletion.jsp");
	}

	if (editor.getDisplayType() != NumberDisplayType.LABEL
			&& editor.getDisplayType() != NumberDisplayType.HIDDEN && updatable) {
		//テキスト

		//カスタムスタイル
		String customStyle = "";
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
		}

		String cls = "form-size-01 inpbr";
		if (nest != null && nest) {
			cls = "form-size-08 inpbr";
		}
		String maxlength = "";
		if (editor.getMaxlength() > 0) {
			maxlength = " maxlength=" + editor.getMaxlength();
		}

		if (isMultiple) {
			//複数
			String ulId = "ul_" + propName;
			int length = 0;

			//テンプレート行
			String dummyRowId = "id_li_" + propName + "Dummmy";
			String tmpCls = cls;
			if (editor.isShowComma()) {
				tmpCls += " commaFieldDummy";
			}
			String toggleAddBtnFunc = "toggleAddBtn_" + StringUtil.escapeJavaScript(propName);
%>
<script type="text/javascript">
function <%=toggleAddBtnFunc%>() {
	var display = $("#<%=StringUtil.escapeJavaScript(ulId)%> li:not(:hidden)").length < <%=pd.getMultiplicity()%>;
	$("#id_addBtn_<c:out value="<%=propName%>"/>").toggle(display);

	var $parent = $("#<%=StringUtil.escapeJavaScript(ulId)%>").closest(".property-data");
	if ($(".validate-error", $parent).length === 0) {
		$(".format-error", $parent).remove();
	}
}
</script>
<ul id="<c:out value="<%=ulId %>"/>" class="mb05">
<li id="<c:out value="<%=dummyRowId %>"/>" class="list-add" style="display: none;">
<input type="text" class="<c:out value="<%=tmpCls %>"/>" style="<c:out value="<%=customStyle%>"/>" onblur="numcheck(this, true)" <c:out value="<%=maxlength%>"/> /> <input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.decimal.DecimalPropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" />
</li>
<%
			if (editor.isShowComma()) {
				cls += " commaField";
			}
			BigDecimal[] array = propValue instanceof BigDecimal[] ? (BigDecimal[]) propValue : null;
			if (array != null) {
				length = array.length;
				for (int i = 0; i < array.length; i++) {
					String str = array[i] != null ? array[i].toPlainString() : "";
					String liId = "li_" + propName + i;
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<input type="text" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>" onblur="numcheck(this, true)" <c:out value="<%=maxlength%>"/> />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.decimal.DecimalPropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>', <%=toggleAddBtnFunc%>)" />
</li>
<%
				}
			}
			String addBtnStyle = "";
			if (array != null && array.length >= pd.getMultiplicity()) addBtnStyle = "display: none;";
%>
</ul>
<input type="button" id="id_addBtn_<c:out value="<%=propName %>"/>" value="${m:rs('mtp-gem-messages', 'generic.editor.decimal.DecimalPropertyEditor_Edit.add')}" class="gr-btn-02 add-btn" style="<%=addBtnStyle%>" onclick="addTextItem('<%=StringUtil.escapeJavaScript(ulId)%>', <%=pd.getMultiplicity() + 1%>, '<%=StringUtil.escapeJavaScript(dummyRowId)%>', '<%=StringUtil.escapeJavaScript(propName)%>', 'id_count_<%=StringUtil.escapeJavaScript(propName)%>', ':text', <%=toggleAddBtnFunc%>, <%=toggleAddBtnFunc%>)" />
<input type="hidden" id="id_count_<c:out value="<%=propName %>"/>" value="<c:out value="<%=length %>"/>" />
<script>
$(function() {
	<%-- common.js --%>
	addEditValidator(function() {
		var $input = $("#" + es("<%=ulId%>") + " li :text");
		for (var i = 0; i < $input.length; i++) {
			var val = $($input.get(i)).val();
			if (typeof val !== "undefined" && val !== null && val !== "" && isNaN(val)) {
				alert(scriptContext.gem.locale.common.numFormatErrorMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
				return false;
			}
		}
		return true;
	});
});
</script>
<%
		} else {
			//単一
			String str = propValue instanceof BigDecimal ? ((BigDecimal)propValue).toPlainString() : "";
			//テキスト
			if (editor.isShowComma()) {
				if (nestDummyRow != null && nestDummyRow) {
					cls += " commaFieldDummy";
				} else {
					cls += " commaField";
				}
			}
%>
<input type="text" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>" onblur="numcheck(this, true)" <c:out value="<%=maxlength%>"/> />
<%
			if (nestDummyRow == null || !nestDummyRow) {
				//NestTableダミー行は出力しない＆コピー時はコピー処理側から追加
%>
<script>
$(function() {
	<%-- common.js --%>
	addEditValidator(function() {
		var val = $(":text[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (typeof val !== "undefined" && val !== null && val !== "" && isNaN(val)) {
			alert(scriptContext.gem.locale.common.numFormatErrorMsg.replace("{0}", "<%=StringUtil.escapeJavaScript(displayLabel)%>"));
			return false;
		}
		return true;
	});
});
</script>
<%
			}
		}
	} else {
		//LABELかHIDDENか更新不可

		if (editor.getDisplayType() != NumberDisplayType.HIDDEN) {
			request.setAttribute(Constants.OUTPUT_HIDDEN, true);
		}
%>
<jsp:include page="DecimalPropertyEditor_View.jsp"></jsp:include>
<%
		if (editor.getDisplayType() != NumberDisplayType.HIDDEN) {
			request.removeAttribute(Constants.OUTPUT_HIDDEN);
		}
	}
%>
