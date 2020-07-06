<%--
 Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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

<%@page import="org.iplass.mtp.view.top.parts.LastLoginParts"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.auth.AuthContext" %>
<%@ page import="org.iplass.mtp.util.DateUtil"%>
<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%!
String format(Object value) {
	if (value == null) return "";

	try {
		return DateUtil.getSimpleDateFormat(TemplateUtil.getLocaleFormat().getOutputDatetimeSecFormat(), true).format(value);
	} catch (Exception e) {
	}
	return "";
}
%>
<%
	Object obj = AuthContext.getCurrentContext().getAttribute("lastLoginOn");
	String value = format(obj);
	
	//設定情報取得
	LastLoginParts parts = (LastLoginParts) request.getAttribute("lastLoginParts");
	
	//スタイルシートのクラス名
	String style = "topview-parts last-login";
	if (StringUtil.isNotBlank(parts.getStyle())) {
		style = style + " " + parts.getStyle();
	}
%>
<div class="<c:out value="<%=style %>"/>" >
<p>
<span class="last-login-title">${m:rs("mtp-gem-messages", "auth.LastLogin.title")}</span>
<span class="last-login-datetime"><c:out value="<%=value%>"/></span>
</p>
</div>
