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

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.iplass.mtp.auth.AuthContext"%>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinition"%>
<%@ page import="org.iplass.mtp.entity.permission.EntityPermission"%>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.calendar.*" %>
<%@ page import="org.iplass.mtp.view.calendar.EntityCalendar.CalendarType"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.mtp.ManagerLocator" %>
<%@ page import="org.iplass.mtp.entity.definition.EntityDefinitionManager" %>
<%@ page import="org.iplass.gem.command.calendar.ref.CalendarFilterCommand"%>
<%@ page import="org.iplass.gem.command.calendar.AddCalendarCommand"%>
<%@ page import="org.iplass.gem.command.Constants"%>

<%

Boolean isTop = (Boolean) request.getAttribute("isTop");
if (isTop == null) isTop = false;

String calendarName;
if (isTop) {
	calendarName = (String) request.getAttribute(Constants.CALENDAR_NAME);
} else {
	calendarName = (String) request.getParameter(Constants.CALENDAR_NAME);
}
if (calendarName == null) return;

String defaultDispType = (String) request.getParameter(Constants.CALENDAR_TYPE);
String defaultDate = (String) request.getParameter("targetDate");


EntityCalendarManager cm = ManagerLocator.getInstance().getManager(EntityCalendarManager.class);
EntityCalendar ec = cm.get(calendarName);
if (ec == null) return;

AuthContext auth = AuthContext.getCurrentContext();
String displayName = TemplateUtil.getMultilingualString(ec.getDisplayName(), ec.getLocalizedDisplayNameList());

if (defaultDispType == null) {
	switch (ec.getType()) {
		case  DAY :
			defaultDispType = "agendaDay";
			break;
		case WEEK :
			defaultDispType = "agendaWeek";
			break;
		case MONTH :
			defaultDispType = "month";
			break;
		default :
			defaultDispType = "month";
	}
}

if (request.getAttribute(Constants.CALENDAR_LIB_LOADED) == null) {
	request.setAttribute(Constants.CALENDAR_LIB_LOADED, true);
%>
<%@include file="../layout/resource/calendarResource.inc.jsp"%>
<%
}
%>
<script>
	$(function() {
		var calDefs = new Array();
<%
List<EntityCalendarItem> calendarItems = ec.getItems();
EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
for (int i = 0; i <  calendarItems.size(); i++) {
	EntityCalendarItem item = calendarItems.get(i);
	if (auth.checkPermission(new EntityPermission(item.getDefinitionName(), EntityPermission.Action.CREATE))) {
		String defName = item.getDefinitionName();
		EntityDefinition ed = edm.get(defName);
		String dispName = TemplateUtil.getMultilingualString(ed.getDisplayName(), ed.getLocalizedDisplayNameList());
		String addAction = item.getAddAction() != null ? item.getAddAction() : AddCalendarCommand.ACTION_NANE;
		String viewName = item.getViewName() != null ? item.getViewName() : "";
%>
		calDefs[<%=i%>] = {"defName":"<%=defName %>", "dispName":"<%=dispName%>", "addAction":"<%=addAction %>", "viewName":"<%=viewName %>"};
<%
	}
}
%>
		if (!scriptContext.calendarDefs) {
			scriptContext.calendarDefs = new Array();
		}
		scriptContext.calendarDefs["<%=calendarName%>"] = calDefs;

		var $calendar = $("#" + es("<%=calendarName%>"));
		$calendar.attr("calendarName", "<%=calendarName%>");
		$calendar.attr("isTop", "<%=isTop%>");
		$calendar.attr("defaultDispType", "<%=defaultDispType%>");
		$calendar.attr("imagePath", "${m:esc(skinImagePath)}");
		$calendar.attr("addAction", "<%=AddCalendarCommand.ACTION_NANE%>");
		$calendar.attr("filterAction", "<%=CalendarFilterCommand.ACTION_NANE%>");
		$calendar.attr("defaultDate", "<%=defaultDate%>");

		$calendar.calendarView();
	});
</script>
<%
if (!isTop) {
%>
<h2 class="hgroup-01">
<span>
<i class="far fa-circle default-icon"></i>
</span>
<c:out value="<%=displayName %>"/>
</h2>
<%
}
%>
<div id ="<%=calendarName%>" class="flat-block-top"></div>
