<%--
 Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

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
<%@page import="org.iplass.mtp.impl.auth.oauth.command.AuthorizeCommand"%>
<%@page import="org.iplass.mtp.impl.auth.oauth.code.AuthorizationRequest"%>
<%@page import="org.iplass.mtp.impl.auth.oauth.vh.ConsentViewHelper"%>
<%@page import="org.iplass.mtp.web.template.TemplateUtil"%>
<%@page import="org.iplass.mtp.command.RequestContext"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%
pageContext.setAttribute("vh", new ConsentViewHelper((AuthorizationRequest) request.getSession().getAttribute(AuthorizeCommand.SESSION_AUTHORIZATION_REQUEST)));
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <meta http-equiv="x-ua-compatible" content="IE=edge" />
  <title>OAuth Consent</title>
</head>
<body>
  <div id="logo">
    <c:if test="${not empty vh.logoUri}">
    <img src="${vh.logoUri}">
    </c:if>
  </div>

  <div id="consent">
    <p>${m:esc(vh.clientName)} requests the following permissions.</p>
    <ul>
      <c:forEach var="scope" items="${vh.scopes}">
      <li><strong>${m:esc(scope.name)}</strong> : ${m:esc(scope.description)} </li>
      </c:forEach>
    </ul>

    <form method="post" action="${m:tcPath()}/oauth/consent">
      <input type="hidden" name="requestId" value="${vh.requestId}" />
      <button name="submit" value="cancel">Cancel</button>
      <button name="submit" value="accept">Accept</button>
    </form>
  </div>

  <div id="client">
    <h4>Client Details</h4>
    <table>
      <tr><th>Name:</th><td>${m:esc(vh.clientName)}</td></tr>
      <tr>
        <th>URL:</th>
        <td>
          <c:if test="${not empty vh.clientUri}">
          <a href="${vh.clientUri}">${vh.clientUri}</a>
          </c:if>
        </td>
      </tr>
      <tr>
        <th>Contacts:</th>
        <td>
          <c:forEach var="contact" items="${vh.contacts}">
          ${m:esc(contact)}<br>
          </c:forEach>
        </td>
      </tr>
      <tr>
        <th>Terms of Service:</th>
        <td>
          <c:if test="${not empty vh.tosUri}">
          <a href="${vh.tosUri}">${vh.tosUri}</a>
          </c:if>
        </td>
      </tr>
      <tr>
        <th>Policy:</th>
        <td>
          <c:if test="${not empty vh.policyUri}">
          <a href="${vh.policyUri}">${vh.policyUri}</a>
          </c:if>
        </td>
      </tr>
    </table>
  </div>
</body>
</html>