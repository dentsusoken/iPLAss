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
<%@ page import="java.sql.Time" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TimePropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.MinIntereval" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange" %>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	String[] split(Object value) {
		String[] ret = { "  ", "  ", "  ", "", ""};
		if (value != null) {
			if (value instanceof Time) {
				//詳細画面からのケース
// 				Calendar cal = Calendar.getInstance();
				Calendar cal = DateUtil.getCalendar(false);
				Time time = (Time) value;
				cal.setTime(time);
				ret[0] = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
				ret[1] = String.format("%02d", cal.get(Calendar.MINUTE));
				ret[2] = String.format("%02d", cal.get(Calendar.SECOND));
				ret[3] = String.format("%03d", cal.get(Calendar.MILLISECOND));
				ret[4] = ret[0] + ret[1] + ret[2] + ret[3];
			} else if (value instanceof String) {
				//検索画面からのケース、詳細画面でフォーマットエラーの場合もこっち
				String str = (String) value;
				if (str.length() < 9) return ret;
				if (str.length() >= 2) ret[0] = str.substring(0, 2);
				if (str.length() >= 4) ret[1] = str.substring(2, 4);
				if (str.length() >= 6) ret[2] = str.substring(4, 6);
				if (str.length() >= 9) ret[3] = str.substring(6, 9);
				ret[4] = str;
			}
		}
		return ret;
	}
%>

<%
	TimePropertyEditor editor = (TimePropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object _propValue = request.getAttribute(Constants.EDITOR_PICKER_PROP_VALUE);
	Object _defaultValue = request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String _propName = (String) request.getAttribute(Constants.EDITOR_PICKER_PROP_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);

	String propName = editor.getPropertyName();

	String[] tmp = split(_propValue);
	String hour = tmp[0];
	String min = tmp[1];
	String sec = tmp[2];
	String strHidden = tmp[4];

	if (editor.getDisplayType() != DateTimeDisplayType.HIDDEN) {
		//HIDDEN以外

		String[] defTmp = split(_defaultValue);
		String defHour = defTmp[0];
		String defMin = defTmp[1];
		String defSec = defTmp[2];
		String defStr = defTmp[4];
	
		//カスタムスタイル
		String customStyle = "";
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, _propValue);
		}

		String onchange = "timeSelectChange('" + StringUtil.escapeJavaScript(_propName) + "')";
%>
<label>
<select id="h_<c:out value="<%=_propName %>"/>" class="inpbr form-size-11" style="<c:out value="<%=customStyle%>"/>" onchange="<%=onchange%>">
<option value="  ">--</option>
<%
		for (int i = 0; i < 24; i++) {
			String h = String.format("%02d", i);
			String selected = hour.equals(h) ? " selected" : "";
%>
<option value="<c:out value="<%=h%>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=h%>"/></option>
<%
		}
%>
</select>${m:rs("mtp-gem-messages", "generic.editor.time.Time.hour")}
</label>

<%
		if (TimeDispRange.isDispMin(editor.getDispRange())) {
			//分を表示
%>
<label>
<select id="m_<c:out value="<%=_propName %>"/>" class="inpbr form-size-11" style="<c:out value="<%=customStyle%>"/>" onchange="<%=onchange%>">
<option value="  ">--</option>
<%
			int minInterval = MinIntereval.toInt(editor.getInterval());
			for (int i = 0; i < 60; i += minInterval) {
				String m = String.format("%02d", i);
				String selected = min.equals(m) ? " selected" : "";
%>
<option value="<c:out value="<%=m %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=m %>"/></option>
<%
			}
%>
</select>${m:rs("mtp-gem-messages", "generic.editor.time.Time.minute")}
</label>
<%
		} else {
			//分を表示しない
			String defaultMin = "00";
			String _defaultMin = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_MIN);
			if (_defaultMin != null) {
				defaultMin = _defaultMin;
			}
%>
<input type="hidden" id="m_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=defaultMin%>"/>" />
<%
		}
		if (TimeDispRange.isDispSec(editor.getDispRange())) {
			//秒を表示
%>
<label>
<select id="s_<c:out value="<%=_propName %>"/>" class="inpbr form-size-11" style="<c:out value="<%=customStyle%>"/>" onchange="<%=onchange%>">
<option value="  ">--</option>
<%
			for (int i = 0; i < 60; i++) {
				String s = String.format("%02d", i);
				String selected = sec.equals(s) ? " selected" : "";
%>
<option value="<c:out value="<%=s %>"/>" <c:out value="<%=selected %>"/>><c:out value="<%=s %>"/></option>
<%
			}
%>
</select>${m:rs("mtp-gem-messages", "generic.editor.time.Time.second")}
</label>
<%
		} else {
			//秒を表示しない
			String defaultSec = "00";
			String _defaultSec = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_SEC);
			if (_defaultSec != null) {
				defaultSec = _defaultSec;
			}
%>
<input type="hidden" id="s_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=defaultSec%>"/>" />
<%
		}
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" id="i_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=strHidden%>"/>" />
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		$("#h_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).val("<%=defHour %>");
<%
		if (TimeDispRange.isDispMin(editor.getDispRange())) {
%>
		$("#m_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).val("<%=defMin %>");
<%
		}
		if (TimeDispRange.isDispSec(editor.getDispRange())) {
%>
		$("#s_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).val("<%=defSec %>");
<%
		}
%>
		$("#i_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).val("<%=defStr %>");
	});
	<%-- common.js --%>
	addCommonValidator(function() {
		var val = $(":hidden[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (val && val.trim().length > 0 && val.indexOf(" ") > -1) {
			alert("${m:rs('mtp-gem-messages', 'generic.editor.time.Time.validationMsg')}");
			$("#h_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).focus();
			return false;
		}
		return true;
	});
});
</script>
<%
	} else {
		//HIDDEN
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" id="i_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=strHidden%>"/>" />
<%
	}
%>