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
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.TimestampPropertyEditor" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.MinIntereval" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	String format(Timestamp time) {
		DateFormat format = DateUtil.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, true);
		return time != null ? format.format(time) : "";
	}
	String[] split(Object value) {
		String[] ret = {"", "  ", "  ", "  ", "", ""};
		if (value != null) {
			if (value instanceof Timestamp) {
				// 検索結果一覧から編集画面に遷移した場合
// 				Calendar cal = Calendar.getInstance();
				Calendar cal = DateUtil.getCalendar(true);
				Timestamp timestamp = (Timestamp) value;
				cal.setTime(timestamp);
// 				ret[0] = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), true).format(cal.getTime());
				ret[0] = cal.get(Calendar.YEAR) +
					String.format("%02d", cal.get(Calendar.MONTH) + 1) +
					String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
				ret[1] = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
				ret[2] = String.format("%02d", cal.get(Calendar.MINUTE));
				ret[3] = String.format("%02d", cal.get(Calendar.SECOND));
				ret[4] = String.format("%03d", cal.get(Calendar.MILLISECOND));

				// hiddenにセットするのでyyyyMMddHHmmssSSS形式にする
				ret[5] = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateFormat(), true).format(cal.getTime()) + ret[1] + ret[2] + ret[3] + ret[4];
			} else if (value instanceof String) {
				// 詳細、編集画面から検索画面に戻ってきた場合、もしくは入力エラーの場合
				String str = (String) value;

				int dateLength = TemplateUtil.getLocaleFormat().getServerDateFormat().length();
				int maxLength = dateLength + 9;
				if (str.length() < maxLength) return ret;

				ret[0] = str.substring(0, dateLength).trim();
				ret[1] = str.substring(dateLength, dateLength + 2);
				ret[2] = str.substring(dateLength + 2, dateLength + 4);
				ret[3] = str.substring(dateLength + 4, dateLength + 6);
				ret[4] = str.substring(dateLength + 6, dateLength + 9);

				// hiddenにyyyyMMddHHmmssSSS形式でセットする
				ret[5] = str;

			}
		}
		return ret;
	}
	/**
	 * 文字列をサニタイズする
	 *
	 * @param value 対象文字列
	 * @return サニタイズ後の文字列
	 */
	String sanitize(String value) {
		return StringUtil.escapeHtml(StringUtil.escapeJavaScript(value));
	}
	/**
	 * ノードに設定する属性キー・バリューを取得する。
	 * バリューの値はサニタイズする。
	 *
	 * @param key 属性キー
	 * @param configValue 設定値
	 * @return 属性文字列 （${key}="${configValue}"）。設定値が空の場合は空文字を返却。
	 */
	String getAttribute(String key, String configValue) {
		if (null == configValue || "".equals(configValue)) {
			return "";
		}
		return key + "=\"" + sanitize(configValue) + "\"";
	}
	/**
	 * 入力フィールドの属性情報を取得する。
	 * 設定対象の属性値は以下の通り。
	 * <ul>
	 * <li>リスト説明 ⇒ 属性キー: 属性値の内容説明
	 * <li>data-min-date: TimestampPropertyEditor#getMinDate() の設定値</li>
	 * <li>data-min-date-function: TimestampPropertyEditor#isMinDateFunction() で判断。 data-min-date の値が function として実行される場合 true が設定される</li>
	 * <li>data-max-date: TimestampPropertyEditor#getMaxDate() の設定値</li>
	 * <li>data-max-date-function: TimestampPropertyEditor#isMaxDateFunction() で判断。data-max-date の値が function として実行される場合 true が設定される</li>
	 * <li>readonly: TimestampPropertyEditor#isRestrictDirectEditing() が true の場合にキーを指定</li>
	 * </ul>
	 *
	 * @param editor TimestampPropertyEditor インスタンス
	 * @return 入力フィールドの属性情報
	 */
	String getInputAttrs(TimestampPropertyEditor editor) {
		StringBuilder attrs = new StringBuilder("");
		attrs
			// getMinDate() で設定が存在していれば data-min-date 属性を設定する
			.append(getAttribute("data-min-date", editor.getMinDate()))
			.append(" ")
			// isMinDateFunction が true の場合は、 data-min-date-function="true" を付与する
			.append(editor.isMinDateFunction() ? "data-min-date-function=\"true\"" : "")
			.append(" ")
			// getMaxDate() で設定が存在していれば data-max-date 属性を設定する
			.append(getAttribute("data-max-date", editor.getMaxDate()))
			.append(" ")
			// isMaxDateFunction が true の場合は、 data-max-date-function="true" を付与する
			.append(editor.isMaxDateFunction() ? "data-max-date-function=\"true\"" : "")
			.append(" ")
			// isRestrictDirectEditing が true の場合は、 readonly 属性を付与する
			.append(editor.isRestrictDirectEditing() ? "readonly" : "");
		return attrs.toString();
	}
%>
<%
	TimestampPropertyEditor editor = (TimestampPropertyEditor) request.getAttribute(Constants.EDITOR_EDITOR);

	Entity entity = request.getAttribute(Constants.ENTITY_DATA) instanceof Entity ? (Entity) request.getAttribute(Constants.ENTITY_DATA) : null;
	Object _propValue = request.getAttribute(Constants.EDITOR_PICKER_PROP_VALUE);
	Object _defaultValue = request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_VALUE);

	String rootDefName = (String)request.getAttribute(Constants.ROOT_DEF_NAME);
	String _propName = (String) request.getAttribute(Constants.EDITOR_PICKER_PROP_NAME);
	String scriptKey = (String)request.getAttribute(Constants.SECTION_SCRIPT_KEY);

	Boolean nestDummyRow = (Boolean) request.getAttribute(Constants.EDITOR_REF_NEST_DUMMY_ROW);

	String propName = editor.getPropertyName();

	String[] tmp = split(_propValue);
	String date = tmp[0];
	String hour = tmp[1];
	String min = tmp[2];
	String sec = tmp[3];
	String strHidden = tmp[5];

	if (editor.getDisplayType() != DateTimeDisplayType.HIDDEN) {
		//HIDDEN以外

		String[] defTmp = split(_defaultValue);
		String defDate = defTmp[0];
		String defHour = defTmp[1];
		String defMin = defTmp[2];
		String defSec = defTmp[3];
		String defStr = defTmp[5];

		// 入力フィールドの属性値を取得する
		String inputAttributes = getInputAttrs(editor);

		//カスタムスタイル
		String customStyle = "";
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, _propValue);
		}

		//コピー用のダミー行ならカレンダーを出さない
		String cls = "";
		if (nestDummyRow != null && nestDummyRow) {
			cls = "inpbr";
		} else {
			cls = "datepicker inpbr";
		}

		String onchange = "timestampSelectChange('" + StringUtil.escapeJavaScript(_propName) + "')";
%>
<input type="text" class="<c:out value="<%=cls %>"/>" style="<c:out value="<%=customStyle%>"/>" value="" id="d_<c:out value="<%=_propName %>"/>" onchange="<%=onchange%>" data-showButtonPanel="<%=!editor.isHideButtonPanel()%>" data-notFillTime="<%=editor.isNotFillTime()%>" data-showWeekday=<%=editor.isShowWeekday()%> data-suppress-alert="true" <%= inputAttributes %> />
<%
		String defaultHour = "00";
		String _defaultHour = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_HOUR);
		if (_defaultHour != null) {
			defaultHour = _defaultHour;
		}
		if (TimeDispRange.isDispHour(editor.getDispRange())) {
			//時間を表示
%>
<label>
<select id="h_<c:out value="<%=_propName %>"/>" class="inpbr form-size-11" style="<c:out value="<%=customStyle%>"/>" onchange="<%=onchange%>" data-defaultValue="<c:out value="<%=defaultHour%>"/>">
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
</select>${m:rs("mtp-gem-messages", "generic.editor.timestamp.Timestamp.hour")}
</label>
<%
		} else {
			//時間を表示しない
%>
<input type="hidden" id="h_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=defaultHour %>"/>" />
<%
		}
		String defaultMin = "00";
		String _defaultMin = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_MIN);
		if (_defaultMin != null) {
			defaultMin = _defaultMin;
		}
		if (TimeDispRange.isDispMin(editor.getDispRange())) {
			//分を表示
%>
<label>
<select id="m_<c:out value="<%=_propName %>"/>" class="inpbr form-size-11" style="<c:out value="<%=customStyle%>"/>" onchange="<%=onchange%>" data-defaultValue="<c:out value="<%=defaultMin%>"/>">
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
</select>${m:rs("mtp-gem-messages", "generic.editor.timestamp.Timestamp.minute")}
</label>
<%
		} else {
			//分を表示しない
%>
<input type="hidden" id="m_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=defaultMin%>"/>" />
<%
		}
		String defaultSec = "00";
		String _defaultSec = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_SEC);
		if (_defaultSec != null) {
			defaultSec = _defaultSec;
		}
		if (TimeDispRange.isDispSec(editor.getDispRange())) {
			//秒を表示
%>
<label>
<select id="s_<c:out value="<%=_propName %>"/>" class="inpbr form-size-11" style="<c:out value="<%=customStyle%>"/>" onchange="<%=onchange%>" data-defaultValue="<c:out value="<%=defaultSec%>"/>">
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
</select>${m:rs("mtp-gem-messages", "generic.editor.timestamp.Timestamp.second")}
</label>
<%
		} else {
			//秒を表示しない
%>
<input type="hidden" id="s_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=defaultSec%>"/>" />
<%
		}
		String defaultMsec = "000";
		String _defaultMsec = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_MSEC);
		if (_defaultMsec != null) {
			defaultMsec = _defaultMsec;
		}
%>
<input type="hidden" id="ms_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=defaultMsec%>"/>" />
<input type="hidden" name="<c:out value="<%=propName %>"/>" id="i_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=strHidden%>"/>" />
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		var defaultDate = convertToLocaleDateString("<%= StringUtil.escapeJavaScript(defDate) %>");
		var $date = $("#d_" + es("<%=StringUtil.escapeJavaScript(_propName)%>"));
		$date.val(defaultDate);
<%
		if (TimeDispRange.isDispHour(editor.getDispRange())) {
%>
		$("#h_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).val("<%=defHour %>");
<%
		}
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

		var $parent = $date.closest(".property-data");
		$date.removeClass("validate-error");
		$(".format-error", $parent).remove();
	});
	<%-- common.js --%>
	addCommonValidator(function() {
		var val = $(":hidden[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (val && val.length > 0 && val.indexOf(" ") > -1) {
			alert("${m:rs('mtp-gem-messages', 'generic.editor.timestamp.Timestamp.validationMsg')}");
			$("#d_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).focus();
			return false;
		}
		return true;
	});

	var date = convertToLocaleDateString("<%= StringUtil.escapeJavaScript(date) %>");
	$("#d_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).val(date).trigger("blur");
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