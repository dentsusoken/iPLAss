<%--
 Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.

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
<%@page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@page import="org.iplass.mtp.command.RequestContext"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <meta http-equiv="x-ua-compatible" content="IE=edge" />
  <title>POST OAuth Response...</title>
</head>
<body onload="document.forms[0].submit()">
  <form method="post" action="${m:esc(authorizationRequest.redirectUri)}">
    <c:if test="${commandResult == 'SUCCESS_POST'}">
      <input type="hidden" name="code" value="${authorizationCode.codeValue}" />
    </c:if>
    <c:if test="${commandResult == 'ERROR_POST'}">
      <input type="hidden" name="error" value="${error.code}" />
      <c:if test="${not empty error.description}">
        <input type="hidden" name="error_description" value="${m:esc(error.description)}" />
      </c:if>
    </c:if>
    <c:if test="${not empty authorizationRequest.state}">
      <input type="hidden" name="state" value="${m:esc(authorizationRequest.state)}" />
    </c:if>
    <input type="hidden" name="iss" value="${m:esc(iss)}" />
</form>
</body>
</html>