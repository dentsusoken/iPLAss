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
<%@ page import="java.sql.Time" %>
<%@ page import="java.text.*" %>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPropertyPermission"%>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.entity.ValidateError"%>
<%@ page import="org.iplass.mtp.entity.definition.PropertyDefinition" %>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TimePropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.MinIntereval" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%!
	Object getDefaultValue(TimePropertyEditor editor, PropertyDefinition pd) {
		String defaultValue = editor.getDefaultValue();
		if (defaultValue != null) {
			if (pd.getMultiplicity() != 1) {
				String[] vals = defaultValue.split(",");
				int length = vals.length > pd.getMultiplicity() ? pd.getMultiplicity() : vals.length;
				Time[] ret = new Time[length];
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
	Time getDefaultValue(String value) {
		if (value != null) {
			if ("NOW".equals(value)) {
				//TODO 予約語の検討
				return new Time(TemplateUtil.getCurrentTimestamp().getTime());
			} else {
				try {
					SimpleDateFormat format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerTimeFormat(), false);
					Long l = format.parse(value + "000").getTime();
					return new Time(l);
				} catch (ParseException e) {
				}
			}
		}
		return null;
	}
	boolean checkError(String propName) {
		ValidateError[] errors = (ValidateError[]) TemplateUtil.getRequestContext().getAttribute(Constants.ERROR_PROP);
		if (errors != null) {
			for (ValidateError tmp : errors) {
				if (propName.equals(tmp.getPropertyName())) {
					return true;
				}
			}
		}
		return false;
	}
%>
<%
	TimePropertyEditor editor = (TimePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object propValue = request.getAttribute(Constants.EDITOR_PROP_VALUE);

	String defName = (String)request.getAttribute(Constants.DEF_NAME);
	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	PropertyDefinition pd = (PropertyDefinition) request.getAttribute(Constants.EDITOR_PROPERTY_DEFINITION);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);
	String execType = (String) request.getAttribute(Constants.EXEC_TYPE);

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
	if (propValue == null) {
		//表示するパラメータがない場合は別の所から補完
		if (checkError(propName)) {
			//更新時のTimestampへの変換でエラーが出た場合、文字列で取り出して再表示する
			if (pd.getMultiplicity() == 1) {
				propValue = request.getParameter(propName);
			} else {
				propValue = request.getParameterValues(propName);
			}
		} else {
			//新規追加で更新可能な項目ならデフォルト値を設定
			if (isInsert && isEditable) propValue = getDefaultValue(editor, pd);
		}
	}

	boolean isUseTimePicker = editor.isUseTimePicker();

	boolean isMultiple = pd.getMultiplicity() != 1;

	//カスタムスタイル
	String customStyle = "";
	if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
		customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, propValue);
	}

	if (ViewUtil.isAutocompletionTarget()) {
		request.setAttribute(Constants.AUTOCOMPLETION_EDITOR, editor);
		request.setAttribute(Constants.AUTOCOMPLETION_SCRIPT_PATH, "/jsp/gem/generic/editor/time/TimePropertyAutocompletion.jsp");
	}

	//タイプ毎に出力内容かえる
	if (editor.getDisplayType() == DateTimeDisplayType.DATETIME && updatable) {
		//日時
		if (isMultiple) {
			//複数
			String ulId = "ul_" + propName;
			int length = 0;
			String dummyRowId = "id_li_" + propName + "Dummmy";


			if (isUseTimePicker) {
%>
<ul id="<c:out value="<%=ulId %>"/>" class="mb05">
<li id="<c:out value="<%=dummyRowId %>"/>" class="list-add picker-list timepicker-field" style="display: none;">
<%
				StringBuffer sbFormat = new StringBuffer();
				int maxLength = 2;
				sbFormat.append("HH");

				String defaultMin = "00";
				int minInterval = MinIntereval.toInt(editor.getInterval());
				if (TimeDispRange.isDispMin(editor.getDispRange())) {
					//分を表示
					sbFormat.append(":mm");
					defaultMin = "";
					maxLength = 5;
				}

				String defaultSec = "00";
				if (TimeDispRange.isDispSec(editor.getDispRange())) {
					//秒を表示
					sbFormat.append(":ss");
					maxLength = 8;
					defaultSec = "";
				}
%>
<input type="text" class="inpbr timepicker-form-size-01" style="<c:out value="<%=customStyle%>"/>" maxlength="<c:out value="<%=maxLength %>"/>"
	data-stepmin="<c:out value="<%=minInterval %>"/>" data-timeformat="<c:out value="<%=sbFormat.toString() %>"/>"
	data-fixedMin="<c:out value="<%=defaultMin%>"/>" data-fixedSec="<c:out value="<%=defaultSec%>"/>"/>
<input type="hidden" />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.time.TimePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" />
</li>
<%
				Object[] array = propValue instanceof Object[] ? (Object[]) propValue : null;
				if (array != null) {
					length = array.length;
					for (int i = 0; i < array.length; i++) {
						String liId = "li_" + propName + i;
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add picker-list timepicker-field">
<%
						request.setAttribute(Constants.EDITOR_PICKER_PROP_NAME, propName + i);
						request.setAttribute(Constants.EDITOR_PICKER_PROP_VALUE, array[i]);
%>
<jsp:include page="TimeTimePicker.jsp"></jsp:include>
<%
						request.removeAttribute(Constants.EDITOR_PICKER_PROP_NAME);
						request.removeAttribute(Constants.EDITOR_PICKER_PROP_VALUE);
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.time.TimePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>')" />
</li>
<%
					}
				}
%>
</ul>
<input type="button" id="id_addBtn_<c:out value="<%=propName %>"/>" value="${m:rs('mtp-gem-messages', 'generic.editor.time.TimePropertyEditor_Edit.add')}" class="gr-btn-02 add-btn"
	onclick="addTimePickerItem('<%=StringUtil.escapeJavaScript(ulId)%>', <%=pd.getMultiplicity() + 1 %>, '<%=StringUtil.escapeJavaScript(dummyRowId)%>', '<%=StringUtil.escapeJavaScript(propName)%>', 'id_count_<%=StringUtil.escapeJavaScript(propName)%>')" />
<input type="hidden" id="id_count_<c:out value="<%=propName %>"/>" value="<c:out value="<%=length %>"/>" />
<%

			} else {

%>
<ul id="<c:out value="<%=ulId %>"/>" class="mb05">
<li id="<c:out value="<%=dummyRowId %>"/>" class="list-add picker-list timeselect-field" style="display: none;">
<label>
<select class="inpbr form-size-11" style="<c:out value="<%=customStyle%>"/>">
<option value="  ">--</option>
<%
				for (int j = 0; j < 24; j++) {
					String h = String.format("%02d", j);
%>
<option value="<c:out value="<%=h%>"/>"><c:out value="<%=h%>"/></option>
<%
				}
%>
</select>${m:rs("mtp-gem-messages", "generic.editor.time.TimePropertyEditor_Edit.hour")}
</label>
<%
				int minInterval = MinIntereval.toInt(editor.getInterval());
				if (TimeDispRange.isDispMin(editor.getDispRange())) {
					//分を表示
%>
<label>
<select class="inpbr form-size-11" style="<c:out value="<%=customStyle%>"/>">
<option value="  ">--</option>
<%
					for (int j = 0; j < 60; j += minInterval) {
						String m = String.format("%02d", j);
%>
<option value="<c:out value="<%=m %>"/>"><c:out value="<%=m %>"/></option>
<%
					}
%>
</select>${m:rs("mtp-gem-messages", "generic.editor.time.TimePropertyEditor_Edit.minute")}
</label>
<%
				} else {
%>
<input type="hidden" value="00" />
<%
				}
				if (TimeDispRange.isDispSec(editor.getDispRange())) {
					//秒を表示
%>
<label>
<select class="inpbr form-size-11" style="<c:out value="<%=customStyle%>"/>">
<option value="  ">--</option>
<%
					for (int j = 0; j < 60; j++) {
						String s = String.format("%02d", j);
%>
<option value="<c:out value="<%=s %>"/>"><c:out value="<%=s %>"/></option>
<%
					}
%>
</select>${m:rs("mtp-gem-messages", "generic.editor.time.TimePropertyEditor_Edit.second")}
</label>
<%
				} else {
%>
<input type="hidden" value="00" />
<%
				}
%>
<input type="hidden" />
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.time.TimePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" />
</li>
<%
				Object[] array = propValue instanceof Object[] ? (Object[]) propValue : null;
				if (array != null) {
					length = array.length;
					for (int i = 0; i < array.length; i++) {
						String liId = "li_" + propName + i;
%>
<li id="<c:out value="<%=liId %>"/>" class="list-add picker-list timeselect-field">
<%
						request.setAttribute(Constants.EDITOR_PICKER_PROP_NAME, propName + i);
						request.setAttribute(Constants.EDITOR_PICKER_PROP_VALUE, array[i]);
%>
<jsp:include page="Time.jsp"></jsp:include>
<%
						request.removeAttribute(Constants.EDITOR_PICKER_PROP_NAME);
						request.removeAttribute(Constants.EDITOR_PICKER_PROP_VALUE);
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'generic.editor.time.TimePropertyEditor_Edit.delete')}" class="gr-btn-02 del-btn" onclick="deleteItem('<%=StringUtil.escapeJavaScript(liId)%>')" />
</li>
<%
					}
				}

%>
</ul>
<input type="button" id="id_addBtn_<c:out value="<%=propName %>"/>" value="${m:rs('mtp-gem-messages', 'generic.editor.time.TimePropertyEditor_Edit.add')}" class="gr-btn-02 add-btn"
	onclick="addTimeSelectItem('<%=StringUtil.escapeJavaScript(ulId)%>', <%=pd.getMultiplicity() + 1 %>, '<%=StringUtil.escapeJavaScript(dummyRowId)%>', '<%=StringUtil.escapeJavaScript(propName)%>', 'id_count_<%=StringUtil.escapeJavaScript(propName)%>')" />
<input type="hidden" id="id_count_<c:out value="<%=propName %>"/>" value="<c:out value="<%=length %>"/>" />
<%
			}

		} else {
			//単一
			request.setAttribute(Constants.EDITOR_PICKER_PROP_NAME, propName);
			request.setAttribute(Constants.EDITOR_PICKER_PROP_VALUE, propValue);

			if (isUseTimePicker) {

%>
<span class="timepicker-field">
<jsp:include page="TimeTimePicker.jsp"></jsp:include>
</span>
<%
			} else {
%>
<span class="timeselect-field">
<jsp:include page="Time.jsp"></jsp:include>
</span>
<%
			}
			request.removeAttribute(Constants.EDITOR_PICKER_PROP_NAME);
			request.removeAttribute(Constants.EDITOR_PICKER_PROP_VALUE);
		}
	} else {
		//ラベル
		request.setAttribute(Constants.OUTPUT_HIDDEN, true);
%>
<jsp:include page="TimePropertyEditor_View.jsp"></jsp:include>
<%
		request.removeAttribute(Constants.OUTPUT_HIDDEN);
	}
%>
