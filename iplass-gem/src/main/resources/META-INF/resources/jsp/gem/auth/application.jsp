<%--
 Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.

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

<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.iplass.gem.command.Constants"%>
<%@page import="org.iplass.gem.command.GemResourceBundleUtil"%>
<%@page import="org.iplass.gem.command.auth.RevokeApplicationCommand"%>
<%@page import="org.iplass.mtp.ManagerLocator"%>
<%@page import="org.iplass.mtp.auth.AuthContext"%>
<%@page import="org.iplass.mtp.auth.login.rememberme.RememberMeTokenInfo"%>
<%@page import="org.iplass.mtp.auth.oauth.AccessTokenInfo"%>
<%@page import="org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition"%>
<%@page import="org.iplass.mtp.auth.oauth.definition.OAuthClientDefinition"%>
<%@page import="org.iplass.mtp.auth.oauth.definition.OAuthClientDefinitionManager"%>
<%@page import="org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinitionManager"%>
<%@page import="org.iplass.mtp.auth.oauth.definition.ScopeDefinition"%>
<%@page import="org.iplass.mtp.auth.token.AuthTokenInfo"%>
<%@page import="org.iplass.mtp.auth.token.AuthTokenInfoList"%>
<%@page import="org.iplass.mtp.impl.auth.oauth.OAuthClientCredentialHandler"%>
<%@page import="org.iplass.mtp.impl.auth.oauth.token.RefreshToken"%>
<%@page import="org.iplass.mtp.impl.i18n.I18nUtil"%>
<%@page import="org.iplass.mtp.util.StringUtil"%>
<%@page import="org.iplass.mtp.view.top.TopViewDefinition"%>
<%@page import="org.iplass.mtp.view.top.TopViewDefinitionManager"%>
<%@page import="org.iplass.mtp.view.top.parts.ApplicationMaintenanceParts"%>
<%@page import="org.iplass.mtp.view.top.parts.TopViewParts"%>
<%@page import="org.iplass.gem.command.auth.GenerateAuthTokenCommand"%>
<%@page import="org.iplass.mtp.auth.login.token.SimpleAuthTokenCredential"%>
<%@page import="org.iplass.mtp.auth.login.token.SimpleAuthTokenInfo"%>
<%@ page import="org.iplass.gem.command.ViewUtil"%>

<%!
	TopViewDefinitionManager tvdm = ManagerLocator.manager(TopViewDefinitionManager.class);
	OAuthAuthorizationDefinitionManager oaadm = ManagerLocator.manager(OAuthAuthorizationDefinitionManager.class);
	OAuthClientDefinitionManager oacdm = ManagerLocator.manager(OAuthClientDefinitionManager.class);
%>
<%
	String roleName = (String) request.getSession().getAttribute(Constants.ROLE_NAME);
	if (roleName == null) roleName = "DEFAULT";
	TopViewDefinition topView = tvdm.get(roleName);
	if (topView == null) {
		return;
	}

	String title = null;
	boolean usePersonalAccessToken = false;
	ApplicationMaintenanceParts amp = null;
	for (TopViewParts parts : topView.getParts()) {
		if (parts instanceof ApplicationMaintenanceParts) {
			amp = (ApplicationMaintenanceParts)parts;
			title = I18nUtil.stringDef(amp.getTitle(), amp.getLocalizedTitleList());
			if (title == null) {
				title = GemResourceBundleUtil.resourceString("layout.header.appMaintenance");
			}
			usePersonalAccessToken = amp.isUsePersonalAccessToken();
			break;
		}
	}

	String styleAttr = amp != null ? ViewUtil.buildHeightStyleAttr(amp.getMaxHeight()) : "";
	
	List<AccessTokenInfo> applications = new ArrayList<>();
	List<RememberMeTokenInfo> validRememberMeTokens  = new ArrayList<>();
	List<SimpleAuthTokenInfo> simpleAuthTokens = new ArrayList<>();
	AuthTokenInfoList infoList = AuthContext.getCurrentContext().getAuthTokenInfos();
	if (infoList != null) {
		for (AuthTokenInfo info : infoList.getList()) {
			if (info instanceof AccessTokenInfo) {
				applications.add((AccessTokenInfo)info);
			} else if (info instanceof RememberMeTokenInfo) {
				RememberMeTokenInfo remmeInfo = (RememberMeTokenInfo)info;
				if (!remmeInfo.isExpired()) {
					validRememberMeTokens.add(remmeInfo);
				}
			} else if (info instanceof SimpleAuthTokenInfo) {
				simpleAuthTokens.add((SimpleAuthTokenInfo) info);
			}
		}
	}
	pageContext.setAttribute("validRememberMeTokens", validRememberMeTokens);
	pageContext.setAttribute("simpleAuthTokens", simpleAuthTokens);

%>
<h2 class="hgroup-01">
<span>
<i class="far fa-circle default-icon"></i>
</span>
<c:out value="<%= title %>"/>
</h2>

<div class="auth-application">
<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "auth.application.authApplication")}</h3>
<div class="detailForm" <%= styleAttr %>>
<%
	if (applications.isEmpty()) {
%>
<div class="mb20"><span class="success">${m:rs("mtp-gem-messages", "auth.application.noAuthApplication")}</span></div>

<%
	} else {
%>
<div class="operation-bar operation-bar_top">
<ul class="list_operation edit-bar">
<li class="btn revoke-all-btn">
<input type="button" value="${m:rs('mtp-gem-messages', 'auth.application.deleteAll')}" class="gr-btn revoke-all-app" name="revoke-all"/>
</li>
</ul>
<ul class="nav-disc-all">
<li class="all-open"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.detail.allOpen")}</a></li>
<li class="all-close"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.detail.allClose")}</a></li>
</ul>
</div>

<span class=success><%=GemResourceBundleUtil.resourceString("auth.application.authApplicationCount", applications.size()) %></span>
<ul class="nav-section"></ul>

<%
		for (AccessTokenInfo info : applications) {
			OAuthClientDefinition client = oacdm.get(info.getClientName());

			String clientName = null;
			if (client != null) {
				clientName = I18nUtil.stringDef(client.getDisplayName(), client.getLocalizedDisplayNameList());
				if (clientName == null) {
					clientName = client.getName();
				}
			} else {
				clientName = "Unknown Client";
			}
%>
<div class="token-info">
<div class="hgroup-03 sechead">
<h3><span class="token-name"><c:out value="<%=clientName%>"/></span></h3>
</div>
<div class="token-detail">
<table class="tbl-section">
<tbody>
<tr>
<th class="section-data col1">${m:rs("mtp-gem-messages", "auth.application.permission")}</th>
<td class="section-data col1">
<ul>
<%
			if (info.getGrantedScopes() != null && !info.getGrantedScopes().isEmpty()) {
				if (client != null) {
					OAuthAuthorizationDefinition server = oaadm.get(client.getAuthorizationServer());
					if (server != null) {
						for (ScopeDefinition scope : server.getScopes()) {
							if (info.getGrantedScopes().contains(scope.getName())) {
								String scopeDesc = I18nUtil.stringDef(scope.getDisplayName(), scope.getLocalizedDisplayNameList());
								if (scopeDesc == null) {
									scopeDesc = scope.getName();
								}
%>
<li><c:out value="<%=scopeDesc%>"/></li>
<%
							}
						}
					}
				}
			}
%>
</ul>
</td>
</tr>
<%
			if (client != null && client.getClientUri() != null) {
%>
<tr>
<th class="section-data col1">URL</th>
<td class="section-data col1">
<a href="<c:out value="<%=client.getClientUri()%>"/>" target="_blank"><c:out value="<%=client.getClientUri()%>"/></a>
</td>
</tr>
<%
			}
			if (client != null && client.getTosUri() != null) {
%>
<tr>
<th class="section-data col1">${m:rs("mtp-gem-messages", "auth.application.tosUri")}</th>
<td class="section-data col1">
<a href="<c:out value="<%=client.getTosUri()%>"/>" target="_blank"><c:out value="<%=client.getTosUri()%>"/></a>
</td>
</tr>
<%
			}
			if (client != null && client.getPolicyUri() != null) {
%>
<tr>
<th class="section-data col1">${m:rs("mtp-gem-messages", "auth.application.policy")}</th>
<td class="section-data col1">
<a href="<c:out value="<%=client.getPolicyUri()%>"/>" target="_blank"><c:out value="<%=client.getPolicyUri()%>"/></a>
</td>
</tr>
<%
			}
%>
<tr>
<th class="section-data col1"></th>
<td class="section-data col1">
<input type="button" value="${m:rs('mtp-gem-messages', 'auth.application.delete')}" class="gr-btn revoke" name="revoke"
	data-token-type="<c:out value="<%=info.getType()%>"/>" data-token-key="<c:out value="<%=info.getKey()%>"/>" >
</td>
</tr>
</tbody>
</table>
</div>
</div>
<%
		}
	}
%>
</div>

<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "auth.application.rememberMe")}</h3>
<div class="detailForm" <%= styleAttr %>>
<%
	if (validRememberMeTokens.isEmpty()) {
%>
<div class="mb20"><span class="success">${m:rs("mtp-gem-messages", "auth.application.noRememberMeToken")}</span></div>
<%
	} else {
%>

<span class="success">${m:rs("mtp-gem-messages", "auth.application.existRememberMeToken")}<br>
${m:rs("mtp-gem-messages", "auth.application.explainRememberMeToken")}
</span>
<ul class="nav-section"></ul>

<div class="token-detail">
<table class="tbl-section">
<tbody>
<tr>
<th class="section-data col1"  rowspan=<%=validRememberMeTokens.size()+1 %>>
${m:rs("mtp-gem-messages", "auth.application.activeRememberMeToke")}</th>
</tr>
<c:forEach var="info" items="${validRememberMeTokens}">
<tr>
<td class="section-data col1">${m:rs('mtp-gem-messages', 'auth.application.rememberMeTokenExpiryDate')}&nbsp;
${m:esc(m:fmt(info.expiryDate , 'yyyy/MM/dd HH:mm:ss'))}
</td>
</tr>
</c:forEach>
<tr>
<th class="section-data col1"></th>
<td class="section-data col1">
<input type="button" value="${m:rs('mtp-gem-messages', 'auth.application.revokeRememberMeToken')}" class="gr-btn revoke-all-rememberme" name="revoke-all"/>
</td>
</tr>
</tbody>
</table>
</div>
<%
	}
%>
</div>

<%
	if (usePersonalAccessToken) {
%>
<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "auth.application.simpleAuthToken")}</h3>
<div class="detailForm" <%= styleAttr %>>
<%
	if (simpleAuthTokens.isEmpty()) {
%>
<div class="mb20"><span class="success">${m:rs("mtp-gem-messages", "auth.application.noSimpleAuthToken")}</span></div>
<%
	} else {
%>
<ul class="nav-section"></ul>

<div class="token-detail">
<table class="tbl-section">
<tbody>
<tr>
<th class="section-data col1" rowspan=<%=simpleAuthTokens.size()+1 %>>
${m:rs("mtp-gem-messages", "auth.application.activeSimpleAuthToken")}
</th>
</tr>

<c:forEach var="info" items="${simpleAuthTokens}">
<tr>
<td class="section-data col1">${m:esc(info.application)}<br>
${m:rs('mtp-gem-messages', 'auth.application.simpleAuthTokenGenerateDate')}&nbsp;${m:esc(m:fmt(info.startDate , 'yyyy/MM/dd HH:mm:ss'))}
</td>
<td class="section-data col1">
<input type="button" value="${m:rs('mtp-gem-messages', 'auth.application.delete')}" class="gr-btn revoke" name="revoke"
	data-token-type="<c:out value="${info.type}"/>" data-token-key="<c:out value="${info.key}"/>" >
</td>
</tr>
</c:forEach>
</tbody>
</table>
</div>
<%
	}
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'auth.application.generateSimpleAuthToken')}" class="gr-btn create-sat" name="create"/>
<%
	if (!simpleAuthTokens.isEmpty()) {
%>
<input type="button" value="${m:rs('mtp-gem-messages', 'auth.application.deleteAll')}" class="gr-btn revoke-all-sat" name="revoke-all"/>
<%
	}
%>
</div>

<%-- SimpleAuthToken生成/表示ダイアログ --%>
<div id="generateAuthTokenDialog" class="mtp-jq-dialog" title="${m:rs('mtp-gem-messages', 'auth.application.generateSimpleAuthToken')}" style="display:none;">
<form>
<table>
<tbody>
<tr>
<th>${m:rs("mtp-gem-messages", "auth.application.simpleAuthToken")}</th>
<td><span class="tokenApplication"></span></td>
</tr>
<tr>
<th>${m:rs("mtp-gem-messages", "auth.application.simpleAuthTokenDescription")}</th>
<td><input type="text" name="application" class="form-size-01 inpbr" /></td>
</tr>
</tbody>
</table>
<input type="hidden" name="_t" value="${m:fixToken()}">
</form>
</div>
<%
	}
%>

<script>
$(function() {
	$(".auth-application input.revoke-all-app").on("click", function() {
		var params = {
			target:"all-app"
		};
		revoke(params);
	});
	$(".auth-application input.revoke-all-rememberme").on("click", function() {
		var params = {
			target:"all-rememberme"
		};
		revoke(params);
	});
	$(".auth-application input.revoke-all-sat").on("click", function() {
		var params = {
			target:"all-sat"
		};
		revoke(params);
	});
	$(".auth-application input.revoke").on("click", function() {
		var $this = $(this);
		var params = {
			target:"one-token",
			tokenType: $this.attr("data-token-type"),
			tokenKey: $this.attr("data-token-key")
		};
		revoke(params);
	});

	function revoke(params) {
		params["_t"] = $('input[name="_t"]').val();
		var revokeParams = JSON.stringify(params);
		
		if (confirm("${m:rs('mtp-gem-messages', 'auth.application.deleteMsg')}")) {
			webapiJson("<%=StringUtil.escapeJavaScript(RevokeApplicationCommand.WEBAPI_NAME)%>" ,{
				type: "POST",
				cache: false,
				data: revokeParams,
				success: function(response) {
					location.reload(true);
				}
			});
		}
	}

	$(".auth-application input.create-sat").on("click", function() {
		$("#generateAuthTokenDialog").dialog("open");
	});

	var tokenCreatedFlg = false;
	$("#generateAuthTokenDialog").dialog({
		resizable: false,
		autoOpen: false,
		height: 225,
		width: 520,
		modal: true,
		buttons: [{
			text: "OK",
			click: function() {
				var $dialog = $(this);
				var application = $(":text[name='application']", $(this)).val();
				
				if (typeof application == "undefined" || application == null || application == "") {
					alert(scriptContext.gem.locale.application.specifyDescription);
					return;
				}

				webapi("<%=StringUtil.escapeJavaScript(GenerateAuthTokenCommand.WEBAPI_NAME)%>" ,{
					type: "POST",
					contentType:"application/x-www-form-urlencoded",
					cache: false,
					data: $dialog.children('form').serialize(),
					success: function(response) {
						$dialog.children('form').remove();
						$dialog.append("<div class='result-title'>${m:rs('mtp-gem-messages', 'auth.application.tokenGeneratedMsg')}</div>"
								+ "<input type='text' value='" + response.token + "' class='form-size-01 show-token' name='copy' readonly />"
								+ "<input type='button' value='${m:rs('mtp-gem-messages', 'auth.application.copy')}' class='gr-btn copy-btn'/>"
								+ "<span class='copied' style='display:none;'>${m:rs('mtp-gem-messages', 'auth.application.copied')}</span>");
						
						$('.ui-dialog-buttonset').children('button').remove();
						$('.ui-dialog-buttonset').append('<button type="button" class="ui-button ui-corner-all ui-widget close-dialog">OK</button>');
						tokenCreatedFlg = true;
					}
				});
			}
		},{
			text: scriptContext.gem.locale.common.cancel,
			click: function() {
				$(":text[name='application']", $(this)).val("");
				$(this).dialog("close");
			}
		}],
		close: function() {
			$(":text[name='application']", $(this)).val("");
			if(tokenCreatedFlg) {
				location.reload(true);
		    }
		}
	});
	$("#generateAuthTokenDialog").on("dialogopen", function(e) {
		adjustDialogLayer($(".ui-widget-overlay"));
	});
	
	$(document).on("click", "#generateAuthTokenDialog input.copy-btn", function() {
		var copyText =$(':text[name="copy"]').val();
		
		copyTextToClipboard(copyText, {
			success: function(response) {
				$("#generateAuthTokenDialog .copied").show().fadeOut(3000);
			}
		});
	});
	
	$(document).on("click", "button.close-dialog", function() {
		$("#generateAuthTokenDialog").dialog("close");
	});
	
});
</script>
</div>
