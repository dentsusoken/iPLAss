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

<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.List" %>
<%@ page import="org.iplass.mtp.entity.Entity"%>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.TimeDispRange"%>
<%@ page import="org.iplass.mtp.view.top.parts.InformationParts"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.information.InformationViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@ page import="org.iplass.gem.command.auth.UpdatePasswordCommand"%>

<%!
	String displayFormat(Timestamp time, DateFormat format) {
		if (time == null) {
			return "";
		}
		String value = format.format(time);

		return value;
	}
%>
<%
	//設定情報取得
	InformationParts parts = (InformationParts) request.getAttribute(Constants.INFO_SETTING);

	//お知らせデータ取得
	@SuppressWarnings("unchecked") List<Entity> data = (List<Entity>) request.getAttribute(Constants.DATA);

	//タイトル
	String dispTitle = TemplateUtil.getMultilingualString(parts.getTitle(), parts.getLocalizedTitleList());
	if (StringUtil.isEmpty(dispTitle)) {
		//デフォルト
		dispTitle = GemResourceBundleUtil.resourceString("information.list.newsList");
	}

	//日時表示スタイル
	DateFormat format = null;
	String thStyle = "";
	if (TimeDispRange.isDispSec(parts.getDispRange())) {
		format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true);
		thStyle = "sec";
	} else if (TimeDispRange.isDispMin(parts.getDispRange())) {
		format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeMinFormat(), true);
		thStyle = "min";
	} else if (TimeDispRange.isDispHour(parts.getDispRange())) {
		format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeHourFormat(), true);
		thStyle = "hour";
	} else {
		format = DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDateFormat(), true);
		thStyle = "none";
	}

	//パスワード期限警告
	Boolean isPasswordWarning = (Boolean) request.getAttribute(Constants.INFO_PASSWORD_WARNING);
	if (isPasswordWarning == null) {
		isPasswordWarning = false;
	}
	String passwordWarnAreaStyleClass = null;
	String passwordWarnMarkStyleClass = null;
	String passwordWarnMessage = null;
	if (isPasswordWarning) {
		passwordWarnMessage = TemplateUtil.getMultilingualString(parts.getPasswordWarningMessage(), parts.getLocalizedPasswordWarningMessageList());
		if (passwordWarnMessage == null || passwordWarnMessage.isEmpty()) {
			passwordWarnMessage = GemResourceBundleUtil.resourceString("information.list.passwordWaringMsg");;
		}

		if (parts.getPasswordWarnAreaStyleClass() != null) {
			passwordWarnAreaStyleClass = parts.getPasswordWarnAreaStyleClass();
		} else {
			passwordWarnAreaStyleClass = "ui-state-highlight ui-corner-all";
		}
		if (parts.getPasswordWarnMarkStyleClass() != null) {
			passwordWarnMarkStyleClass = parts.getPasswordWarnMarkStyleClass();
		} else {
			passwordWarnMarkStyleClass = "ui-icon-check";
		}
	}

%>

<div class="info-list topview-parts">
<h3 class="hgroup-02">
${infoSetting.iconTag}
<c:out value="<%=dispTitle%>"/>
</h3>
<%
	boolean noNews = true;
	if (isPasswordWarning) {
		noNews = false;
%>
<p class="pw-warn-area <c:out value="<%=passwordWarnAreaStyleClass%>"/>">
<span class="pw-warn-mark ui-icon <c:out value="<%=passwordWarnMarkStyleClass%>"/>"></span>
<c:out value="<%=passwordWarnMessage%>"/>
<span>&gt;&gt;</span><a href="javascript:void(0)" onclick="changePassword()">${m:rs("mtp-gem-messages", "layout.header.passChng")}</a>
</p>
<script type="text/javascript">
function changePassword() {
	submitForm("${m:tcPath()}/<%=UpdatePasswordCommand.ACTION_VIEW_UPDATE_PASSWORD%>");
}
</script>
<%
	}
%>
<div class="info-block mb20">
<table class="tbl-information-list flat-block-top">
<%
	if (data != null && !data.isEmpty()) {
		noNews = false;
		for (int i = 0; i < data.size(); i++) {
			Entity entity = data.get(i);
			String trStyle = (i + 1) % 2 == 0 ? "even" : "odd";
%>
<tr class="<%=trStyle%>">
<th class="<%=thStyle%>"><c:out value="<%=displayFormat(entity.getStartDate(), format) %>"/></th>
<td>
<a href="javascript:void(0)" onclick="detail_onclick('<%=StringUtil.escapeJavaScript(entity.getOid())%>', '<%=entity.getVersion()%>')">
<c:out value="<%=entity.getName() %>"/>
</a>
</td>
</tr>
<%
		}
	}

	if (noNews) {
%>
<tr>
<td>${m:rs("mtp-gem-messages", "information.list.noNews")}</td>
</tr>
<%
	}
%>
</table>
</div>

<script type="text/javascript">
<%if (parts.getNumberOfDisplay() != null && parts.getNumberOfDisplay() < data.size()) {%>
$(function() {
	$(".info-block").height($(".info-block").find("tr:nth(0)").height() * <%=parts.getNumberOfDisplay()%>);
});
<%}%>
function detail_onclick(oid, version) {
	submitForm("${m:tcPath()}/<%=InformationViewCommand.ACTION_NAME%>", {oid: oid, version: version});
}
</script>
</div><!-- info-list -->
