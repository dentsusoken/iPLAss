<%--
 Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.

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

<%@page import="org.iplass.mtp.util.StringUtil" %>
<%@page import="org.iplass.gem.command.auth.LoginCommand"%>
<%@page import="org.iplass.mtp.impl.auth.authenticate.webauthn.command.ReAuthenticationCommand"%>
<%@page import="org.iplass.mtp.impl.auth.authenticate.webauthn.command.AuthenticationCommand"%>
<%@page import="org.iplass.mtp.impl.auth.authenticate.webauthn.command.AuthenticationOptionsCommand"%>
<%@page import="org.iplass.mtp.impl.core.ExecuteContext"%>
<%@page import="org.iplass.mtp.tenant.TenantAuthInfo"%>
<%@page import="org.iplass.mtp.web.WebRequestConstants"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%
TenantAuthInfo tenantAuthInfo = ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantAuthInfo.class);
boolean isWebAuthnEnabled = tenantAuthInfo.isUseWebAuthn();
boolean isReAuth = pageContext.getAttribute("isReAuth") != null;
if (isWebAuthnEnabled) {
	String redirectPath = LoginCommand.getValidRedirectPath((String) request.getAttribute(WebRequestConstants.REDIRECT_PATH));
	String loginLabelKey = isReAuth ? "auth.webAuthn.passkeyReAuth" : "auth.webAuthn.passkeyLogin";
	pageContext.setAttribute("loginLabelKey", loginLabelKey);
	
	String webapiDefinitionName = StringUtil.isEmpty(tenantAuthInfo.getWebAuthnDefinitonName()) ? "" : "/" + tenantAuthInfo.getWebAuthnDefinitonName();
	String authOptionsWebApi = AuthenticationOptionsCommand.WEBAPI_NAME + webapiDefinitionName;
	String authWebApi        = AuthenticationCommand.WEBAPI_NAME + webapiDefinitionName;
	String reAuthWebApi      = ReAuthenticationCommand.WEBAPI_NAME + webapiDefinitionName;
%>
<div id="webauthnLoginSection">
	<p class="nav-login-01">
		<span>${m:rs("mtp-gem-messages", "auth.webAuthn.passkeyOr")}</span>
	</p>
	<p class="nav-login-01">
		<input type="submit" class="gr-btn" id="webauthnLogin" value="${m:rs('mtp-gem-messages', loginLabelKey)}"/>
	</p>
	<script type="text/javascript">
		if (!window.PublicKeyCredential) {
			document.getElementById('webauthnLoginSection').style.display = 'none';
		} else {
			document.getElementById('webauthnLogin').addEventListener('click', async () => {
				try {
					const optionsJson = await fetch('${m:tcPath()}/api/<%= authOptionsWebApi %>', {
						method: 'POST',
						headers: {
							'Content-Type': 'application/json',
							'Accept': 'application/json',
							'X-Requested-With': 'XMLHttpRequest'},
						body: '{}'
					}).then(res => res.json());

					let options;
					if (typeof PublicKeyCredential.parseRequestOptionsFromJSON === 'function') {
						options = PublicKeyCredential.parseRequestOptionsFromJSON(optionsJson);
					} else {
						//for Safari 
						options = optionsJson;
						options.challenge = base64UrlDecode(options.challenge);
					}

					const credential = await navigator.credentials.get({publicKey: options});
					let credentialJson;
					if (typeof credential.toJSON === 'function') {
						credentialJson = credential.toJSON();
					} else {
						//for Safari 
						credentialJson = {
							id: credential.id,
							type: credential.type,
							rawId: base64UrlEncode(credential.rawId),
							clientExtensionResults: credential.getClientExtensionResults(),
							response: {
								clientDataJSON: base64UrlEncode(credential.response.clientDataJSON),
								attestationObject: base64UrlEncode(credential.response.attestationObject),
								transports: credential.response.getTransports(),
								authenticatorData: base64UrlEncode(credential.response.getAuthenticatorData()),
								publicKey: base64UrlEncode(credential.response.getPublicKey()),
								publicKeyAlgorithm: credential.response.getPublicKeyAlgorithm()
							}
						};
						if (credential.authenticatorAttachment) {
							credentialJson.authenticatorAttachment = credential.authenticatorAttachment;
						}
					}

					let apiPath = '${m:tcPath()}/api/<%= isReAuth ? reAuthWebApi : authWebApi %>';
					const remMe = document.getElementById('rememberMe');
					if (remMe != null && remMe.checked) {
						apiPath += '?rememberMe=true';
					}
					const result = await fetch(apiPath, {
						method: 'POST',
						headers: {
							'Content-Type': 'application/json',
							'Accept': 'application/json',
							'X-Requested-With': 'XMLHttpRequest' },
						body: JSON.stringify(credentialJson)
					});

					if (result.ok) {
						window.location.href = '<c:out value="<%=redirectPath%>" />';
					} else {
						const errJson = await result.json()
						console.error('WebAuthn authentication failed: ' + JSON.stringify(errJson));
						if (errJson.exceptionMessage != null) {
							alert(errJson.exceptionMessage);
						} else {
							alert('${m:rs("mtp-gem-messages", "auth.webAuthn.passkeyError")}');
						}
					}
				} catch (err) {
					console.error(err);
					alert('${m:rs("mtp-gem-messages", "auth.webAuthn.passkeyError")}: ' + err.message);
				}
			});
		}
	</script>
</div>
<%}%>
