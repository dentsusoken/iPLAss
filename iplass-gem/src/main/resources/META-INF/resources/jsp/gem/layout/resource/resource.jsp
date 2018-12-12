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

<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.util.DateUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil" %>

<%
	request.setAttribute("staticContentPath", TemplateUtil.getStaticContentPath());
	request.setAttribute("apiVersion", TemplateUtil.getAPIVersion());
	request.setAttribute("skinName", ViewUtil.getSkin().getPageSkinName());
	request.setAttribute("skinImagePath", ViewUtil.getSkinImagePath());
	request.setAttribute("themeName", ViewUtil.getTheme().getThemeName());
	request.setAttribute("themeImagePath", ViewUtil.getThemeImagePath());

	String language = TemplateUtil.getLanguage();
	if (StringUtil.isEmpty(language)) {
		language = "ja";
	}

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
		inputDateFormat = "yyyy/MM/dd";
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
contentPath = "${staticContentPath}";
contextPath = "${m:tcPath()}";
sysdate = "<%=DateUtil.getSimpleDateFormat("yyyyMMddHHmmss", true).format(TemplateUtil.getCurrentTimestamp())%>";
scriptContext = {};
document.scriptContext = scriptContext;
scriptContext.locale = {};
scriptContext.locale.defaultLocale = "<%=language%>";
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
dType="/gem";
</script>
<script src="${staticContentPath}/webjars/jquery/3.2.1/jquery.min.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jquery-ui/1.12.1/jquery-ui.min.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/functions.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/common.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/webapi.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/plugin/fixHeight.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/free-jqgrid/4.14.1/js/jquery.jqgrid.min.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jqtree/1.4.2/tree.jquery.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jQuery-Timepicker-Addon/1.6.3/jquery-ui-timepicker-addon.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jQuery-Timepicker-Addon/1.6.3/jquery-ui-sliderAccess.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jQuery-contextMenu/2.6.2/dist/jquery.contextMenu.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/momentjs/2.18.1/min/moment-with-locales.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/scripts/gem/locale-<%=language%>.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/free-jqgrid/4.14.1/js/i18n/min/grid.locale-<%=language%>.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/font-awesome/5.0.9/svg-with-js/js/fontawesome-all.min.js?cv=${apiVersion}"></script>
<%
	if (!"en".equals(language)) {
		//enの場合はデフォルトを利用
%>
<script src="${staticContentPath}/webjars/jquery-ui-src/1.11.4/ui/i18n/datepicker-<%=language %>.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jQuery-Timepicker-Addon/1.6.3/i18n/jquery-ui-timepicker-<%=language %>.js?cv=${apiVersion}"></script>
<%
	}
%>
<link rel="stylesheet" href="${staticContentPath}/webjars/jquery-ui/1.12.1/jquery-ui.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/webjars/free-jqgrid/4.14.1/css/ui.jqgrid.min.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/webjars/free-jqgrid/4.14.1/plugins/css/ui.multiselect.min.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/webjars/jqtree/1.4.2/jqtree.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/webjars/jQuery-Timepicker-Addon/1.6.3/jquery-ui-timepicker-addon.css?cv=${apiVersion}" />
<link rel="stylesheet" href="${staticContentPath}/webjars/jQuery-contextMenu/2.6.2/dist/jquery.contextMenu.css?cv=${apiVersion}" />
