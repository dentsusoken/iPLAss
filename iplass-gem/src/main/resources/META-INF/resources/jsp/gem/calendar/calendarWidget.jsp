<%--
 Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
 
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

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager" %>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission"%>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.calendar.*" %>
<%@ page import="org.iplass.mtp.view.calendar.EntityCalendar.CalendarType"%>
<%@ page import="org.iplass.mtp.view.top.parts.CalendarParts"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.gem.command.calendar.AddCalendarCommand"%>
<%@ page import="org.iplass.gem.command.calendar.CalendarCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%
String calendarName = (String) request.getAttribute(Constants.CALENDAR_NAME);
if (calendarName == null) return;

EntityCalendarManager cm = ManagerLocator.getInstance().getManager(EntityCalendarManager.class);
EntityCalendar ec = cm.get(calendarName);
if (ec == null) return;

String displayName = TemplateUtil.getMultilingualString(ec.getDisplayName(), ec.getLocalizedDisplayNameList());

String contextPath = TemplateUtil.getTenantContextPath();
String action = contextPath + "/" + CalendarCommand.ACTION_NANE;
String params = "{\"calendarName\": \"" + StringUtil.escapeJavaScript(calendarName) + "\"}";
String defaultDate = (String) request.getParameter("targetDate");

//設定情報取得
CalendarParts parts = (CalendarParts) request.getAttribute(Constants.CALENDAR_SETTING);

//スタイルシートのクラス名
String style = "topview-widget";
if (StringUtil.isNotBlank(parts.getStyle())) {
	style = style + " " + parts.getStyle();
}

%>
<div class="<c:out value="<%=style %>"/>">
<%
if (request.getAttribute(Constants.CALENDAR_LIB_LOADED) == null) {
	request.setAttribute(Constants.CALENDAR_LIB_LOADED, true);
%>
<%@include file="../layout/resource/calendarResource.inc.jsp"%>
<%
}
%>
<script>
	$(function() {
		var widget = $("#calendarWidget_" + es("${m:esc(calendarName)}"));
		widget.attr("calendarName", "${m:esc(calendarName)}");
		widget.attr("displayName", "<%=displayName%>");
		widget.attr("action", "<%=action%>");

		widget.calendarWidgetView();
	});
</script>
<div id="calendarWidget_${m:esc(calendarName)}" class="lyt-shortcut-01 mb05">
	${calendarParts.iconTag}
	<p class="title"></p>
	<div class="calendar-block">
		<table id="calendarWidgetTable_${m:esc(calendarName)}" class="tbl-calendar" >
		<thead>
			<tr>
				<th><a href="#" class="prev" >&lt;</a></th>
				<th class="month-title" colspan="5"></th>
				<th><a href="#" class="next" >&gt;</a></th>
			</tr>
		</thead>
		<tbody></tbody>
		</table>
	</div>
</div>

<ul class="link-widget">
<li class="list-view"><a href="javascript:void(0)" onclick='submitForm("<%=action%>", <%=params%>)'>${m:rs("mtp-gem-messages", "calendar.calendarParts.view")}</a></li>
</ul>
</div>
