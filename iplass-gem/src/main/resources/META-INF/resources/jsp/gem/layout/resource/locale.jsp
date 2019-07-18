<%--
 Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%
	String serverDateFormat = TemplateUtil.getLocaleFormat().getServerDateFormat();
	if (StringUtil.isEmpty(serverDateFormat)) {
		serverDateFormat = "yyyyMMdd";
	}

	String serverTimeFormat = TemplateUtil.getLocaleFormat().getServerTimeFormat();
	if (StringUtil.isEmpty(serverTimeFormat)) {
		serverTimeFormat = "HHmmssSSS";
	}

	String outputDateFormat = TemplateUtil.getLocaleFormat().getOutputDateFormat();
	if (StringUtil.isEmpty(outputDateFormat)) {
		outputDateFormat = "yyyy/MM/dd";
	}

	String outputDateWeekdayFormat = TemplateUtil.getLocaleFormat().getOutputDateWeekdayFormat();
	if (StringUtil.isEmpty(outputDateWeekdayFormat)) {
		outputDateWeekdayFormat = "yyyy/MM/dd EEEE";
	}

	String outputTimeHourFormat = TemplateUtil.getLocaleFormat().getOutputTimeHourFormat();
	if (StringUtil.isEmpty(outputTimeHourFormat)) {
		outputTimeHourFormat = "HH";
	}

	String outputTimeMinFormat = TemplateUtil.getLocaleFormat().getOutputTimeMinFormat();
	if (StringUtil.isEmpty(outputTimeMinFormat)) {
		outputTimeMinFormat = "HH:mm";
	}

	String outputTimeSecFormat = TemplateUtil.getLocaleFormat().getOutputTimeSecFormat();
	if (StringUtil.isEmpty(outputTimeSecFormat)) {
		outputTimeSecFormat = "HH:mm:ss";
	}

	String inputDateFormat = TemplateUtil.getLocaleFormat().getBrowserInputDateFormat();
	if (StringUtil.isEmpty(inputDateFormat)) {
		inputDateFormat = "yyyyMMdd";
	}

	String inputTimeHourFormat = TemplateUtil.getLocaleFormat().getBrowserInputTimeHourFormat();
	if (StringUtil.isEmpty(inputTimeHourFormat)) {
		inputTimeHourFormat = "HH";
	}

	String inputTimeMinFormat = TemplateUtil.getLocaleFormat().getBrowserInputTimeMinFormat();
	if (StringUtil.isEmpty(inputTimeMinFormat)) {
		inputTimeMinFormat = "HH:mm";
	}

	String inputTimeSecFormat = TemplateUtil.getLocaleFormat().getBrowserInputTimeSecFormat();
	if (StringUtil.isEmpty(inputTimeSecFormat)) {
		inputTimeSecFormat = "HH:mm:ss";
	}
%>
<script>
scriptContext.locale = {};
scriptContext.locale.defaultLocale = "${language}";
scriptContext.locale.serverDateFormat = "<%=serverDateFormat%>";
scriptContext.locale.serverTimeFormat = "<%=serverTimeFormat%>";
scriptContext.locale.outputDateFormat = "<%=outputDateFormat%>";
scriptContext.locale.outputDateWeekdayFormat = "<%=outputDateWeekdayFormat%>";
scriptContext.locale.outputTimeHourFormat = "<%=outputTimeHourFormat%>";
scriptContext.locale.outputTimeMinFormat = "<%=outputTimeMinFormat%>";
scriptContext.locale.outputTimeSecFormat = "<%=outputTimeSecFormat%>";
scriptContext.locale.inputDateFormat = "<%=inputDateFormat%>";
scriptContext.locale.inputTimeHourFormat = "<%=inputTimeHourFormat%>";
scriptContext.locale.inputTimeMinFormat = "<%=inputTimeMinFormat%>";
scriptContext.locale.inputTimeSecFormat = "<%=inputTimeSecFormat%>";
scriptContext.locale.showPulldownPleaseSelectLabel = <%=ViewUtil.isShowPulldownPleaseSelectLabel()%>;
</script>
<script src="${staticContentPath}/scripts/gem/i18n/gem-locale.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/i18n/gem-locale-${language}.js?cv=${apiVersion}"></script>
<%-- For compatibility before 3.0.11  --%>
<script src="${staticContentPath}/scripts/gem/i18n/gem-locale-migration-${language}.js?cv=${apiVersion}"></script>
