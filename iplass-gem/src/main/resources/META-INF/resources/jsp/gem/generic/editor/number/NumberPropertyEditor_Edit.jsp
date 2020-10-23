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
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.IntegerPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.NumberPropertyEditor.NumberDisplayType" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>
<%!
	String format(Number value) {
		if (value == null) return "";
		String str = null;
		try {
			DecimalFormat df = new DecimalFormat("#.#");
			df.setMaximumFractionDigits(18);
			str = df.format(value);
		} catch (NumberFormatException e) {
			str = "";
		}
		return str;
	}
	Object getDefaultValue(NumberPropertyEditor editor, PropertyDefinition pd) {
		String defaultValue = editor.getDefaultValue();
		if (defaultValue != null) {
			if (pd.getMultiplicity() != 1) {
				String[] vals = defaultValue.split(ViewUtil.getGroupingSeparator());
				int length = vals.length > pd.getMultiplicity() ? pd.getMultiplicity() : vals.length;
				Number[] ret = new Number[length];
				for (int i = 0; i < length; i++) {
					ret[i] = getDefaultValue(vals[i], (editor instanceof IntegerPropertyEditor));
				}
				return ret;
			} else {
				return getDefaultValue(defaultValue, (editor instanceof IntegerPropertyEditor));
			}
		}
		return null;
	}
	Number getDefaultValue(String value, boolean isInteger) {
		if (value != null) {
			try {
				if (isInteger) {
					return Integer.parseInt(value);
				} else {
					return Double.parseDouble(value);
				}
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}
%>
<%
	NumberPropertyEditor editor = (NumberPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

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

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/number/NumberPropertyAutocompletion.jsp");
	}

	String cls = "form-size-01 inpbr";
	if (nest != null && nest) {
		//cls = "form-size-08 inpbr";
	}
	String maxlength = "";
	if (editor.getMaxlength() > 0) {
		maxlength = " maxlength=" + editor.getMaxlength();
	}

	if (editor.getDisplayType() != NumberDisplayType.LABEL 
			&& editor.getDisplayType() != NumberDisplayType.HIDDEN && updatable) {
		
		//テキスト
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
%>
<ul id="<c:out value="<%=ulId %>"/>" class="mb05">
<li id="<c:out value="<%=dummyRowId %>"/>" class="list-add" style="display: none;">
<input type="text" class="<c:out value="<%=tmpCls %>"/>" style="<c:out value="<%=customStyle%>"/>" onblur="numcheck(this)" <c:out value="<%=maxlength%>"/> /> <input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.number.NumberPropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" />
</li>
<%
			//データ出力
			if (editor.isShowComma()) {
				cls += " commaField";
			}
			Number[] array = propValue instanceof Number[] ? (Number[]) propValue : null;
			if (array != null) {
				length = array.length;
				for (int i = 0; i < array.length; i++) {
					String str = array[i] != null ? format(array[i]) : "";
					String liId = "li_" + propName + i;
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add">
<input type="text" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>" onblur="numcheck(this)" <c:out value="<%=maxlength%>"/> /> <input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.number.NumberPropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>')" />
</li>
<%
				}
			}
%>
</ul>
<input type="button" id="id_addBtn_<c:out value="<%=propName %>"/>" value="${m:rs('mtp-gem-messages', 'generic.editor.number.NumberPropertyEditor_Edit.add')}" class="gr-btn-02 add-btn" onclick="addTextItem('<%=StringUtil.escapeJavaScript(ulId)%>', <%=pd.getMultiplicity() + 1%>, '<%=StringUtil.escapeJavaScript(dummyRowId)%>', '<%=StringUtil.escapeJavaScript(propName)%>', 'id_count_<%=StringUtil.escapeJavaScript(propName)%>', ':text')" />
<input type="hidden" id="id_count_<c:out value="<%=propName %>"/>" value="<c:out value="<%=length %>"/>" />
<%
		} else {
			//単一
			String str = propValue instanceof Number ? format((Number) propValue) : "";
			if (editor.isShowComma()) {
				if (nestDummyRow != null && nestDummyRow) {
					cls += " commaFieldDummy";
				} else {
					cls += " commaField";
				}
			}
%>
<input type="text" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=str %>"/>" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>" onblur="numcheck(this)" <c:out value="<%=maxlength%>"/> />
<%
		}
	} else {
		//LABELかHIDDENか更新不可
		
		if (editor.getDisplayType() != NumberDisplayType.HIDDEN) {
			request.setAttribute(Constants.OUTPUT_HIDDEN, true);
		}
%>
<jsp:include page="NumberPropertyEditor_View.jsp"></jsp:include>
<%
		if (editor.getDisplayType() != NumberDisplayType.HIDDEN) {
			request.removeAttribute(Constants.OUTPUT_HIDDEN);
		}
	}
%>
