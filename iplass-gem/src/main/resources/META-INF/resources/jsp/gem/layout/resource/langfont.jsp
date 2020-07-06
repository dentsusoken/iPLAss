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

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@ page import="org.iplass.mtp.util.StringUtil"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%
	String fontLanguage = (String) request.getAttribute("language");
	String languageFonts = TemplateUtil.getLanguageFonts(fontLanguage);
%>
<%if (StringUtil.isNotBlank(languageFonts)) {%>
<script>
scriptContext.languageFonts = '<%=languageFonts%>';
</script>
<style>
body, input, textarea, .ui-widget,
.ui-dialog label, .ui-dialog span, .ui-dialog input, .ui-dialog textarea, .ui-dialog button, .ui-dialog select,
ul.context-menu-list li.context-menu-item span {
font-family: <%=languageFonts%>;
}
</style>
<%}%>
