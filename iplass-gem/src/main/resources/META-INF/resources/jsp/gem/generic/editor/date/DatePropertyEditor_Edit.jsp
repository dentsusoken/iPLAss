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
<%@ page import="java.sql.Date" %>
<%@ page import="java.text.*" %>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition"%>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.OutputType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DatePropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%!
	String dateToString(Date date, String format) {
		return date != null ? DateUtil.getSimpleDateFormat(format, false).format(date) : "";
	}
	Object getDefaultValue(DatePropertyEditor editor, PropertyDefinition pd) {
		String defaultValue = editor.getDefaultValue();
		if (defaultValue != null) {
			if (pd.getMultiplicity() != 1) {
				String[] vals = defaultValue.split(",");
				int length = vals.length > pd.getMultiplicity() ? pd.getMultiplicity() : vals.length;
				Date[] ret = new Date[length];
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
	Date getDefaultValue(String value) {
		if (value != null) {
			if ("NOW".equals(value)) {
				//TODO 予約語の検討、X日後とか特定日付からの加減も必要？
				return new Date(TemplateUtil.getCurrentTimestamp().getTime());
			} else {
				try {
					SimpleDateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateFormat(), false);
					Long l = format.parse(value).getTime();
					return new Date(l);
				} catch (ParseException e) {
				}
			}
		}
		return null;
	}
%>
<%
	DatePropertyEditor editor = (DatePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);
	Boolean nestDummyRow = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST_DUMMY_ROW);
	if (nestDummyRow == null) {
		nestDummyRow = false;
	}

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

	//カスタム入力スタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/date/DatePropertyAutocompletion.jsp");
	}

	String ulId = "ul_" + propName;
	int length = 0;
	if (editor.getDisplayType() == DateTimeDisplayType.DATETIME && updatable) {
		//日時
		if (isMultiple) {
			//複数
			String dummyRowId = "id_li_" + propName + "Dummmy";
			String toggleAddBtnFunc = "toggleAddBtn_" + StringUtil.escapeJavaScript(propName);
%>
<script type="text/javascript">
function <%=toggleAddBtnFunc%>() {
	var display = $("#<%=StringUtil.escapeJavaScript(ulId)%> li:not(:hidden)").length < <%=pd.getMultiplicity()%>;
	$("#id_addBtn_<c:out value="<%=propName%>"/>").toggle(display);
}
</script>
<ul id="<c:out value="<%=ulId %>"/>" class="mb05">
<li id="<c:out value="<%=dummyRowId %>"/>" class="list-add picker-list" style="display: none;">
<input type="text" class="inpbr" style="<c:out value="<%=customStyle%>"/>" data-showButtonPanel=<%=!editor.isHideButtonPanel()%> data-showWeekday=<%=editor.isShowWeekday()%> />
<input type="hidden" />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.date.DatePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" />
</li>
<%
			Date[] array = propValue instanceof Date[] ? (Date[]) propValue : null;
			if (array != null) {
				length = array.length;
				for (int i = 0; i < array.length; i++) {
					String liId = "li_" + propName + i;
					String str = dateToString(array[i], TemplateUtil.getLocaleFormat().getOutputDateFormat());
					String hiddenDate = dateToString(array[i], TemplateUtil.getLocaleFormat().getServerDateFormat());
					String onchange = "dateChange('" + StringUtil.escapeJavaScript(liId) + "')";
%>
<li id="<c:out value="<%=liId%>"/>" class="list-add picker-list">
<input type="text" id="d_<c:out value="<%=liId%>"/>" class="inpbr datepicker" style="<c:out value="<%=customStyle%>"/>" value="<c:out value="<%=str%>"/>" onchange="<%=onchange%>" data-showButtonPanel="<%=!editor.isHideButtonPanel()%>" data-showWeekday=<%=editor.isShowWeekday()%> />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.date.DatePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>', <%=toggleAddBtnFunc%>)" />
<input type="hidden" id="i_<c:out value="<%=liId%>"/>" name="<c:out value="<%=propName%>"/>" value="<c:out value="<%=hiddenDate%>"/>" />
<script type="text/javascript">
$(function() {
	var date = convertToLocaleDateString("<%=StringUtil.escapeJavaScript(hiddenDate)%>");
	$("#d_" + es("<%=StringUtil.escapeJavaScript(liId)%>")).val(date).trigger("blur");
});
</script>
</li>
<%
				}
			}
			String addBtnStyle = "";
			if (array != null && array.length >= pd.getMultiplicity()) addBtnStyle = "display: none;";
%>
</ul>
<input type="button" id="id_addBtn_<c:out value="<%=propName%>"/>" value="${m:rs('mtp-gem-messages', 'generic.editor.date.DatePropertyEditor_Edit.add')}" class="gr-btn-02 add-btn" style="<%=addBtnStyle%>" onclick="addDateItem('<%=StringUtil.escapeJavaScript(ulId)%>', <%=pd.getMultiplicity() + 1%>, '<%=StringUtil.escapeJavaScript(dummyRowId)%>', '<%=StringUtil.escapeJavaScript(propName)%>', 'id_count_<%=StringUtil.escapeJavaScript(propName)%>', <%=toggleAddBtnFunc%>, <%=toggleAddBtnFunc%>)" />
<input type="hidden" id="id_count_<c:out value="<%=propName%>"/>" value="<c:out value="<%=length%>"/>" />
<%
		} else {
			//単一
			String cls = "inpbr";
			if (!nestDummyRow) {
				cls += " datepicker";
			}
			String hiddenDate = "";

			if (propValue instanceof Date) {
				hiddenDate = dateToString((Date)propValue, TemplateUtil.getLocaleFormat().getServerDateFormat());
			}
			String onchange = "dateChange('" + StringUtil.escapeJavaScript(propName) + "')";
%>
<input type="text" id="d_<c:out value="<%=propName%>"/>" class="<c:out value="<%=cls%>"/>" style="<c:out value="<%=customStyle%>"/>" value="" onchange="<%=onchange%>" data-showButtonPanel="<%=!editor.isHideButtonPanel()%>" data-showWeekday=<%=editor.isShowWeekday()%> />
<input type="hidden" id="i_<c:out value="<%=propName %>"/>" name="<c:out value="<%=propName %>"/>" value="<c:out value="<%=hiddenDate %>"/>" />
<script type="text/javascript">
$(function() {
	var date = convertToLocaleDateString("<%= StringUtil.escapeJavaScript(hiddenDate) %>");
	$("#d_" + es("<%=StringUtil.escapeJavaScript(propName)%>")).val(date).trigger("blur");
});
</script>
<%
		}
	} else {
		//ラベル
		request.setAttribute(Constants.OUTPUT_HIDDEN, true);
%>
<jsp:include page="DatePropertyEditor_View.jsp"></jsp:include>
<%
		request.removeAttribute(Constants.OUTPUT_HIDDEN);
	}
%>
