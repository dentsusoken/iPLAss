<%--
 Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.entity.ValidateError" %>
<%@ page import="org.iplass.gem.command.Constants" %>
<%
	ValidateError[] errors = (ValidateError[]) request.getAttribute(Constants.ERROR_PROP);
	String propName = request.getParameter(Constants.PROP_NAME);

	ValidateError error = null;
	if (errors != null) {
		for (ValidateError tmp : errors) {
			if (propName.equals(tmp.getPropertyName())) {
				error = tmp;
			}
		}
	}

	if (error != null) {
		//エラーがあればメッセージ表示
%>
<p class="error"><span class="error">
<%
		for (int i = 0; i < error.getErrorMessages().size(); i++) {
			String   message = error.getErrorMessages().get(i);
			if (i > 0) {
%>
<br />
<%
			}
%>
<c:out value="<%=message %>" />
<%
		}
%>
</span></p>
<%
	}
%>
