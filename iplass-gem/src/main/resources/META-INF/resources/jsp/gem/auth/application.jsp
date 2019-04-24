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

<%@page import="org.iplass.mtp.auth.token.AuthTokenInfo"%>
<%@page import="java.util.List"%>
<%@page import="org.iplass.mtp.auth.AuthContext"%>
<%@page import="org.iplass.mtp.auth.token.AuthTokenInfoList"%>
<%@page import="org.iplass.mtp.impl.i18n.I18nUtil"%>
<%@page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@page import="org.iplass.mtp.view.top.parts.ApplicationMaintenanceParts"%>
<%@page import="org.iplass.mtp.view.top.parts.TopViewParts"%>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.mtp.view.top.TopViewDefinition"%>
<%@page import="org.iplass.mtp.ManagerLocator"%>
<%@page import="org.iplass.mtp.view.top.TopViewDefinitionManager"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%!
	TopViewDefinitionManager tvdm = ManagerLocator.manager(TopViewDefinitionManager.class);
%>
<%
	String roleName = (String) request.getSession().getAttribute(Constants.ROLE_NAME);
	if (roleName == null) roleName = "DEFAULT";
	TopViewDefinition topView = tvdm.get(roleName);
	if (topView == null) {
		return;
	}

	String title = null;
	for (TopViewParts parts : topView.getParts()) {
		if (parts instanceof ApplicationMaintenanceParts) {
			ApplicationMaintenanceParts amp = (ApplicationMaintenanceParts)parts;
			title = I18nUtil.stringDef(amp.getTitle(), amp.getLocalizedTitleList());
			if (title == null) {
				title = GemResourceBundleUtil.resourceString("layout.header.appMaintenance");
			}
			break;
		}
	}

	List<AuthTokenInfo> list = null;
	AuthTokenInfoList infoList = AuthContext.getCurrentContext().getAuthTokenInfos();
	if (infoList != null) {
		list = infoList.getList();
//		for (AuthTokenInfo info : infoList.getList()) {
//			System.out.println(info.getDescription());
//			System.out.println(info.getKey());
//			System.out.println(info.getType());
//			if (info instanceof AccessTokenInfo) {
//				AccessTokenInfo at = (AccessTokenInfo)info;
//				System.out.println(at.getClientName());
//				System.out.println(at.getGrantedScopes().toString());
//			}
//		}
	}


%>
<h2 class="hgroup-01">
<span>
<i class="far fa-circle default-icon"></i>
</span>
<c:out value="<%= title %>"/>
</h2>

<div id="detailForm">
<%
	if (list == null || list.isEmpty()) {
%>
<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "auth.application.noAuthTokenTitle")}</h3>
<span class="msg">${m:rs("mtp-gem-messages", "auth.application.noAuthToken")}</span>
<%
	} else {
%>
<span class="msg"><%=GemResourceBundleUtil.resourceString("auth.application.authTokenCount", list.size()) %></span>
<div class="operation-bar operation-bar_top">
<ul class="list_operation edit-bar">
<li class="btn revoke-all-btn">
<input type="button" value="Revoke All" class="gr-btn" name="revoke-all"/>
</li>
</ul>
<ul class="nav-disc-all">
<li class="all-open"><a href="#">全て開く</a></li>
<li class="all-close"><a href="#">全て閉じる</a></li>
</ul>
</div>
<div class="formArchive">
<%
		for (AuthTokenInfo info : infoList.getList()) {
%>
<div>
<div class="hgroup-03 sechead">
<h3><span class="token-name">NameXXXXX</span></h3>
</div>
<div>
<span class="token-description"><c:out value="<%=info.getDescription()%>"/></span>
</div>
</div>
<%
		}
%>
</div>
<%

	}
%>
</div>