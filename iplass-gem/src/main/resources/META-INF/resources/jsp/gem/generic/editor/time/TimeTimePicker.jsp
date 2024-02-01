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
		String[] ret = { "", "", "", "", ""};
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

	Boolean nestDummyRow = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST_DUMMY_ROW);
	if (nestDummyRow == null) {
		nestDummyRow = false;
	}

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
		String defStrHidden = defTmp[4];

		// 非表示項目設定がある場合の時間フォーマットと最大入力文字数を判断
	    StringBuffer sbFormat = new StringBuffer();
	    int maxLength = 2;
	    sbFormat.append("HH");

	    StringBuffer sbValue = new StringBuffer();
	    StringBuffer sbDefValue = new StringBuffer();
	    sbValue.append(hour);
	    sbDefValue.append(defHour);

	    String defaultMin = "00";
	    String _defaultMin = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_MIN);
		if (_defaultMin != null) {
			defaultMin = _defaultMin;
		}
		if (TimeDispRange.isDispMin(editor.getDispRange())) {
			defaultMin = "";
			sbFormat.append(":mm");

			if (!hour.isEmpty()) {
				sbValue.append(":");
			}
			sbValue.append(min);
			maxLength = 5;

			if (!defHour.isEmpty()) {
				sbDefValue.append(":");
			}
			sbDefValue.append(defMin);
		}
	    String defaultSec = "00";
	    String _defaultSec = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_SEC);
	    if (_defaultSec != null) {
	    	defaultSec = _defaultSec;
	    }
	    if (TimeDispRange.isDispSec(editor.getDispRange())) {
	    	defaultSec = "";
	    	sbFormat.append(":ss");

			if (!min.isEmpty()) {
				sbValue.append(":");
	    	}
			sbValue.append(sec);
	    	maxLength = 8;

			if (!defMin.isEmpty()) {
				sbDefValue.append(":");
	    	}
			sbDefValue.append(defSec);
	    }

		//カスタムスタイル
		String customStyle = "";
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, _propValue);
		}

	    //コピー用のダミー行ならtimepickerを出さない
		String cls = "inpbr timepicker-form-size-01";
		if (!nestDummyRow) {
			cls += " timepicker";
		}

		int minInterval = MinIntereval.toInt(editor.getInterval());

		String onchange = "timePickerChange('" + StringUtil.escapeJavaScript(_propName) + "')";
%>
<input type="text" class="<c:out value="<%=cls%>"/>" style="<c:out value="<%=customStyle%>"/>" value="<c:out value="<%=sbValue.toString() %>"/>" id="time_<c:out value="<%=_propName %>"/>" onchange="<%=onchange%>"
	    maxlength="<c:out value="<%=maxLength %>"/>"  data-stepmin="<c:out value="<%=minInterval %>"/>" data-timeformat="<c:out value="<%=sbFormat.toString() %>"/>"
	    data-fixedMin="<c:out value="<%=defaultMin%>"/>" data-fixedSec="<c:out value="<%=defaultSec%>"/>" data-suppress-alert="true"/>
<input type="hidden" name="<c:out value="<%=propName %>"/>" id="i_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=strHidden%>"/>" />
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		var $time = $("#time_" + es("<%=StringUtil.escapeJavaScript(_propName)%>"));
		$time.val("<%=sbDefValue.toString() %>");
		$(":hidden[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val("<%=defStrHidden %>");

		var $parent = $time.closest(".property-data");
		$time.removeClass("validate-error");
		$(".format-error", $parent).remove();
	});
	<%-- common.js --%>
	addCommonValidator(function() {
		var val = $(":hidden[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (val && val.trim().length > 0 && val.indexOf("error") > -1) {
			alert("${m:rs('mtp-gem-messages', 'generic.editor.time.TimeTimePicker.validationMsg')}");
			$("#time_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).focus();
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