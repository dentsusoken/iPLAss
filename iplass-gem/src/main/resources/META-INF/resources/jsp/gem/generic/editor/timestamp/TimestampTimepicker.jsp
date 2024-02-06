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

<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.view.generic.EntityViewUtil"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType"%>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.MinIntereval" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange" %>
<%@ page import="org.iplass.mtp.view.generic.editor.TimestampPropertyEditor" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%!
	String[] split(Object value) {
		String[] ret = {"", "", "", "", "", "", ""};
		if (value != null) {
			if (value instanceof Timestamp) {
				//詳細画面からのケース
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
				// 表示項目のyyyymmdd+時分秒
				ret[5] = ret[0] + ret[1] + ret[2] + ret[3];

				// hiddenで保持するDatetimePickerの値(yyyymmdd+時分秒ミリ秒)
				// hiddenにセットするのでyyyyMMddHHmmssSSS形式にする
				ret[6] = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateFormat(), true).format(cal.getTime()) + ret[1] + ret[2] + ret[3] + ret[4];
			} else if (value instanceof String) {
				//検索画面からのケース、詳細画面でフォーマットエラーの場合もこっち
				//これはそもそもフォーマットエラーだと画面表示時にスクリプトで空になってしまうのでは？
				String str = (String) value;
				int dateLength = TemplateUtil.getLocaleFormat().getServerDateFormat().length();
				int maxLength = dateLength + 9;
				if (str.length() < maxLength) return ret;

// 				Date tempDate = null;
// 				try {
// 					SimpleDateFormat inSdf = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getServerDateFormat(), true);
// 					tempDate = inSdf.parse(str.substring(0, dateLength).trim());
// 					SimpleDateFormat outSdf = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), true);
// 					ret[0] = outSdf.format(tempDate);
// 				} catch (ParseException e) {
// 					ret[0] = "";
// 				}
				ret[0] = str.substring(0, dateLength).trim();
				ret[1] = str.substring(dateLength, dateLength + 2);
				ret[2] = str.substring(dateLength + 2, dateLength + 4);
				ret[3] = str.substring(dateLength + 4, dateLength + 6);
				ret[5] = str.substring(0, dateLength + 6);
				ret[4] = str.substring(dateLength + 6, dateLength + 9);

				// hiddenにyyyyMMddHHmmssSSS形式でセットする
				ret[6] = str;
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
	if (nestDummyRow == null) {
		nestDummyRow = false;
	}

	//表示用の文字列
	String[] tmp = split(_propValue);
	String strHidden = tmp[6];

	String propName = editor.getPropertyName();

	if (editor.getDisplayType() != DateTimeDisplayType.HIDDEN) {
		//HIDDEN以外

		String[] defTmp = split(_defaultValue);
		String defStrHidden = defTmp[6];

		StringBuffer sbValue = new StringBuffer();
		StringBuffer sbDefValue = new StringBuffer();
		sbValue.append(tmp[0]);
		sbDefValue.append(defTmp[0]);
		if (TimeDispRange.isDispHour(editor.getDispRange())) {
			if (!tmp[1].isEmpty()) {
				sbValue.append(" ");
			}
			sbValue.append(tmp[1]);

			if (!defTmp[1].isEmpty()) {
				sbDefValue.append(" ");
			}
			sbDefValue.append(defTmp[1]);
		}
		if (TimeDispRange.isDispMin(editor.getDispRange())) {
			if (!tmp[1].isEmpty()) {
				sbValue.append(":");
			}
			sbValue.append(tmp[2]);

			if (!defTmp[1].isEmpty()) {
				sbDefValue.append(":");
			}
			sbDefValue.append(defTmp[2]);
		}
		if (TimeDispRange.isDispSec(editor.getDispRange())) {
			if (!tmp[2].isEmpty()) {
				sbValue.append(":");
			}
			sbValue.append(tmp[3]);

			if (!defTmp[2].isEmpty()) {
				sbDefValue.append(":");
			}
			sbDefValue.append(defTmp[3]);
		}

		//時分秒省略時に補完する値
		String defaultHour = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_HOUR);
		if (defaultHour == null) {
			defaultHour = "00";
		}
		String defaultMin = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_MIN);
		if (defaultMin == null) {
			defaultMin = "00";
		}
		String defaultSec = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_SEC);
		if (defaultSec == null) {
			defaultSec = "00";
		}
		String defaultMsec = (String) request.getAttribute(Constants.EDITOR_PICKER_DEFAULT_MSEC);
		if (defaultMsec == null) {
			defaultMsec = "000";
		}

		String timeFormat = null;
		if (TimeDispRange.isDispSec(editor.getDispRange())) {
			timeFormat = "HH:mm:ss";
			defaultHour = "";
			defaultMin = "";
			defaultSec = "";
		} else if (TimeDispRange.isDispMin(editor.getDispRange())) {
			timeFormat = "HH:mm";
			defaultHour = "";
			defaultMin = "";
		} else if (TimeDispRange.isDispHour(editor.getDispRange())) {
			timeFormat = "HH";
			defaultHour = "";
		}

		//カスタムスタイル
		String customStyle = "";
		if (StringUtil.isNotEmpty(editor.getInputCustomStyle())) {
			customStyle = EntityViewUtil.getCustomStyle(rootDefName, scriptKey, editor.getInputCustomStyleScriptKey(), entity, _propValue);
		}

		//コピー用のダミー行ならカレンダーを出さない
		String cls = "inpbr";
		if (!nestDummyRow) {
			cls += " datetimepicker";
		}

		int minInterval = MinIntereval.toInt(editor.getInterval());

		String onchange = "timestampPickerChange('" + StringUtil.escapeJavaScript(_propName) + "')";

		String range = "SEC";
		if (editor.getDispRange() != null) {
			range = editor.getDispRange().toString();
		}
		
		// 入力フィールドの属性値を取得する
		String inputAttributes = getInputAttrs(editor);
%>
<input type="text" class="<c:out value="<%= cls%>"/>" style="<c:out value="<%=customStyle%>"/>" value="" id="datetime_<c:out value="<%=_propName %>"/>"
	    onchange="<%=onchange%>" data-stepmin="<c:out value="<%=minInterval %>"/>" data-timeformat="<c:out value="<%=timeFormat %>"/>"
	    data-fixedMin="<c:out value="<%=defaultMin%>"/>" data-fixedSec="<c:out value="<%=defaultSec%>"/>"  data-fixedMSec="<c:out value="<%=defaultMsec%>"/>" data-showWeekday=<%=editor.isShowWeekday()%> data-suppress-alert="true" 
	    <%= inputAttributes %> />
<input type="hidden" name="<c:out value="<%=propName %>"/>" id="i_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<script type="text/javascript">
$(function() {
	<%-- common.js --%>
	addNormalConditionItemResetHandler(function(){
		var $datetime = $("#datetime_" + es("<%=StringUtil.escapeJavaScript(_propName)%>"));
		$datetime.val("<%=sbDefValue.toString() %>");
		$(":hidden[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val("<%=defStrHidden %>");

		var $parent = $datetime.closest(".property-data");
		$datetime.removeClass("validate-error");
		$(".format-error", $parent).remove();
	});
	<%-- common.js --%>
	addCommonValidator(function() {
		var val = $(":hidden[name='" + es("<%=StringUtil.escapeJavaScript(propName)%>") + "']").val();
		if (val && val.length > 0 && val.indexOf("error") > -1) {
			alert("${m:rs('mtp-gem-messages', 'generic.editor.timestamp.TimestampTimepicker.validationMsg')}");
			$("#datetime_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).focus();
			return false;
		}
		return true;
	});

	var datetime = convertToLocaleDatetimeString("<%= StringUtil.escapeJavaScript(strHidden) %>", "", "<%= StringUtil.escapeJavaScript(range) %>");
	$("#datetime_" + es("<%=StringUtil.escapeJavaScript(_propName)%>")).val(datetime).trigger("blur");
});
</script>
<%
	} else {
		//HIDDEN
%>
<input type="hidden" name="<c:out value="<%=propName %>"/>" id="i_<c:out value="<%=_propName %>"/>" value="<c:out value="<%=strHidden %>"/>" />
<%
	}
%>
