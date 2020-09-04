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

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.DateFormat" %>
<%@page import="java.util.Calendar"%>
<%@page import="org.iplass.mtp.entity.Entity"%>
<%@page import="org.iplass.mtp.util.DateUtil" %>
<%@page import="org.iplass.mtp.util.StringUtil" %>
<%@page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange"%>
<%@page import="org.iplass.mtp.view.top.parts.InformationParts"%>
<%@page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@page import="org.iplass.gem.command.MenuCommand"%>
<%@page import="org.iplass.gem.command.ViewUtil"%>

<%!
	String displayFormat(Timestamp time, DateFormat format) {
		if (time == null) {
			return "";
		}
		String value = format.format(time);
		return "（" + value + "）";
	}
%>
<%
	//データ取得
	Entity entity = (Entity) request.getAttribute(Constants.DATA_ENTITY);

	//設定情報取得
	InformationParts parts = (InformationParts) request.getAttribute(Constants.INFO_SETTING);
	//カスタムスタイル
	String customStyle = (String)request.getAttribute(Constants.INFO_DETAIL_CUSTOM_STYLE);	

	//タイトル
	String dispTitle = TemplateUtil.getMultilingualString(parts.getTitle(), parts.getLocalizedTitleList());
	if (StringUtil.isEmpty(dispTitle)) {
		//デフォルト
		dispTitle = GemResourceBundleUtil.resourceString("information.view.news");
	}
	
	//日時表示スタイル
	TimeDispRange dispTimeRange = null;
	if (parts.getDispRange() != null) {
		dispTimeRange = parts.getDispRange();
	}
	DateFormat format = null;
	if (TimeDispRange.isDispSec(dispTimeRange)) {
		format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true);
	} else if (TimeDispRange.isDispMin(dispTimeRange)) {
		format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeMinFormat(), true);
	} else if (TimeDispRange.isDispHour(dispTimeRange)) {
		format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeHourFormat(), true);
	} else {
		format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), true);
	}

	boolean enableHtmlTag = parts.isEnableHtmlTag();
%>
<div class="content-block">
<h2 class="hgroup-01">
<span>
<i class="far fa-circle default-icon"></i>
</span>
<c:out value="<%=dispTitle%>"/>
</h2>
<h3 class="hgroup-02 hgroup-02-01">
<c:out value="<%=entity.getName() %>"/>&nbsp;&nbsp;<c:out value="<%=displayFormat(entity.getStartDate(), format) %>"/>
</h3>
<table class="tbl-information-detail mb10 flat-block-top">
<tbody>
<tr>
<td>
<%
	String content = entity.getValue("content");
	if (content == null) {
		content = "";
	}
	if (parts.isUseRichtextEditor()) {
		String wrapContent = content;
		if (StringUtil.isNotEmpty(customStyle)) {
			wrapContent = "<span class='data-label' style='" + StringUtil.escapeHtml(customStyle)+ "'>" 
					+ content + "</span>";
		} else {
			wrapContent = "<span class='data-label'>" + content + "</span>";
		}
%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>">
<textarea name="infomation_content" rows="5" cols="30" ><c:out value="<%=wrapContent %>"/></textarea>
</span>
<%
		if (request.getAttribute(Constants.RICHTEXT_LIB_LOADED) == null) {
			request.setAttribute(Constants.RICHTEXT_LIB_LOADED, true);
%>
<%@include file="../layout/resource/ckeditorResource.jsp" %>
<%
		}
%>
<script type="text/javascript">
$(function() {
<%		if (StringUtil.isNotBlank(parts.getRichtextEditorOption())) { %>
	var opt = <%=parts.getRichtextEditorOption()%>;
	if (typeof opt.readOnly === "undefined") opt.readOnly = true;
<%		} else { %>
	var opt = { readOnly: true, allowedContent:<%=enableHtmlTag%> };
<%		} %>
	if (typeof opt.extraPlugins === "undefined") opt.extraPlugins = "autogrow";
	if (typeof opt.autoGrow_onStartup === "undefined") opt.autoGrow_onStartup = true;	
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
	$("textarea[name='infomation_content']").ckeditor(
		function() {}, opt
	);
});
</script>

<%	} else {
		if(!content.isEmpty()){
			if (!enableHtmlTag) {
				content = StringUtil.escapeHtml(content);
				content = content.replaceAll("\r\n", "<BR>").replaceAll("\n", "<BR>").replaceAll("\r", "<BR>");
			}
		}
%>
<span class="data-label" style="<c:out value="<%=customStyle %>"/>">
<%-- XSS対応-エスケープ処理済み＆HTMLタグ出力のため対応なし --%>
<%=content%>
</span>
<%	} %>
</td>
</tr>
</tbody>
</table>
<p class="cancel-link"><a href="javascript:void(0)" onclick="home('<%=MenuCommand.ACTION_NAME%>')">${m:rs("mtp-gem-messages", "information.view.back")}</a></p>
</div>
