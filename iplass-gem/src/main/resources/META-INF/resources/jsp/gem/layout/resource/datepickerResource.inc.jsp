<%--
 Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 
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
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page import="org.iplass.gem.GemConfigService"%>
<%@page import="org.iplass.mtp.spi.ServiceRegistry"%>
<%
	String datepickerOption = ServiceRegistry.getRegistry().getService(GemConfigService.class).getDatePickerDefaultOption();
%>

<script src="${staticContentPath}/webjars/jQuery-Timepicker-Addon/1.6.3/jquery-ui-timepicker-addon.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jQuery-Timepicker-Addon/1.6.3/jquery-ui-sliderAccess.js?cv=${apiVersion}"></script>

<%-- enの場合はデフォルトを利用 --%>
<c:if test="${language != 'en'}" >
<script src="${staticContentPath}/webjars/jquery-ui/ui/i18n/datepicker-${language}.js?cv=${apiVersion}"></script>
<script src="${staticContentPath}/webjars/jQuery-Timepicker-Addon/1.6.3/i18n/jquery-ui-timepicker-${language}.js?cv=${apiVersion}"></script>
</c:if>
<script src="${staticContentPath}/scripts/gem/plugin/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon-mtp.js?cv=${apiVersion}"></script>
 
<link rel="stylesheet" href="${staticContentPath}/webjars/jQuery-Timepicker-Addon/1.6.3/jquery-ui-timepicker-addon.css?cv=${apiVersion}" />

<%
	if (datepickerOption != null) {
		//service-config値なのでエスケープしない
%>
<script>
$.datepicker.setDefaults({<%=datepickerOption%>});
</script>
<%
	}
%>
