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

<%@page import="org.iplass.mtp.view.top.parts.CalendarParts"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.calendar.*" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.gem.command.calendar.CalendarCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%
	String calendarName = (String) request.getAttribute(Constants.CALENDAR_NAME);
	if (calendarName == null) return;

	EntityCalendarManager cm = ManagerLocator.getInstance().getManager(EntityCalendarManager.class);
	EntityCalendar ec = cm.get(calendarName);
	if (ec == null) return;

	//表示名
	String displayName = TemplateUtil.getMultilingualString(ec.getDisplayName(), ec.getLocalizedDisplayNameList());
	
	//設定情報取得
	CalendarParts parts = (CalendarParts) request.getAttribute(Constants.CALENDAR_SETTING);
	
	//スタイルシートのクラス名
	String style = "topview-parts";
	if (StringUtil.isNotBlank(parts.getStyle())) {
		style = style + " " + parts.getStyle();
	}

%>
<div class="<c:out value="<%=style %>"/>">
<h3 class="hgroup-02">
${calendarParts.iconTag}
<c:out value="<%=displayName %>"/>
</h3>
<%
	request.setAttribute("isTop", true);
%>
<jsp:include page="calendarView.jsp"></jsp:include>
<%
	request.removeAttribute("isTop");

	String contextPath = TemplateUtil.getTenantContextPath();
	String action = contextPath + "/" + CalendarCommand.ACTION_NANE;
	String params = "{\"calendarName\": \"" + StringUtil.escapeJavaScript(calendarName) + "\"}";
%>
<ul class="link-list-01 mt10">
<li><a href="javascript:void(0)" onclick='submitForm("<%=action%>", <%=params%>)'>${m:rs("mtp-gem-messages", "calendar.calendarParts.view")}</a></li>
</ul>
</div>
