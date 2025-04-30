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
<%@page import="java.util.List"%>
<%@page import="org.iplass.mtp.util.StringUtil" %>
<%@page import="org.iplass.mtp.impl.auth.AuthContextHolder"%>
<%@page import="org.iplass.mtp.impl.core.ExecuteContext"%>
<%@page import="org.iplass.mtp.impl.auth.authenticate.webauthn.command.RegistrationOptionsCommand"%>
<%@page import="org.iplass.mtp.impl.auth.authenticate.webauthn.command.RegistrationCommand"%>
<%@page import="org.iplass.mtp.impl.auth.authenticate.webauthn.command.DeregistrationCommand"%>
<%@page import="org.iplass.mtp.impl.auth.authenticate.webauthn.command.KeyListCommand"%>
<%@page import="org.iplass.gem.command.auth.GenerateAuthTokenCommand"%>
<%@page import="org.iplass.gem.command.auth.RevokeApplicationCommand"%>
<%@page import="org.iplass.mtp.tenant.TenantAuthInfo"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>

<%
TenantAuthInfo tenantAuthInfo = ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantAuthInfo.class);
List<String> waList = AuthContextHolder.getAuthContext().getPolicy().getMetaData().getWebAuthnDefinition();
boolean isWebAuthnEnabled = tenantAuthInfo.isUseWebAuthn() && waList != null && waList.size() > 0;

if (isWebAuthnEnabled) {
	String webapiDefinitionName = StringUtil.isEmpty(tenantAuthInfo.getWebAuthnDefinitonName()) ? "" : "/" + tenantAuthInfo.getWebAuthnDefinitonName();
	String regOptionsWebApi = RegistrationOptionsCommand.WEBAPI_NAME + webapiDefinitionName;
	String regWebApi        = RegistrationCommand.WEBAPI_NAME + webapiDefinitionName;
%>


<h3 class="hgroup-02 hgroup-02-01">${m:rs("mtp-gem-messages", "auth.webAuthnManage.passkeyManage")}</h3>
<p id="noWebAuthnRegister" class="mb30"><span class="success">${m:rs("mtp-gem-messages", "auth.webAuthnManage.noPasskeySupport")}</span></p>

<div id="webAuthnRegister" style="display:none;">
<div id="noWebAuthnList" class="mb20"><span class="success">${m:rs("mtp-gem-messages", "auth.webAuthnManage.passkeySupport")}</span></div>
<div id="webAuthnListSection" class="formArchive" style="display:none;">
<table class="tbl-section">
<tbody>
</tbody>
</table>
</div>
<p id="webAuthnRegisterSection" class="mb30"><input type="button" value="${m:rs('mtp-gem-messages', 'auth.webAuthnManage.register')}" class="gr-btn register-wa" name="register"/></p>
</div>
<script>
$(function() {

	function register() {
		webapiJson("<%= regOptionsWebApi %>" ,{
			type: "POST",
			cache: false,
			data: '{}',
			success: function(optionsJson) {
				let options;
				if (typeof PublicKeyCredential.parseCreationOptionsFromJSON === 'function') {
					options = PublicKeyCredential.parseCreationOptionsFromJSON(optionsJson);
				} else {
					//for Safari 
					options = optionsJson;
					options.challenge = base64UrlDecode(options.challenge);
					options.user.id = base64UrlDecode(options.user.id);
				}

				navigator.credentials.create({ publicKey: options })
					.then(function(credential) {
						let credentialData;
						if (typeof credential.toJSON === 'function') {
							credentialData = credential.toJSON();
						} else {
							//for Safari 
							credentialData = {
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
								credentialData.authenticatorAttachment = credential.authenticatorAttachment;
							}
						}
						
						webapiJson("<%= regWebApi %>" ,{
							type: "POST",
							cache: false,
							data: JSON.stringify(credentialData),
							success: function(response) {
								keyList();
							}
						});
					});
			}
		});
	}
	
	function deregister(params) {
		if (confirm("${m:rs('mtp-gem-messages', 'auth.webAuthnManage.deleteMsg')}")) {
			webapiJson("<%=DeregistrationCommand.WEBAPI_NAME%>" ,{
				type: "POST",
				cache: false,
				data: JSON.stringify(params),
				success: function(response) {
					keyList();
				}
			});
		}
	}
	
	function keyList() {
		webapi("<%=KeyListCommand.WEBAPI_NAME%>" ,{
			type: "GET",
			cache: false,
			success: function(response) {
				if (response.list.length > 0) {
					const webAuthnList = $('#webAuthnListSection table tbody');
					webAuthnList.empty();
					webAuthnList.append(`<tr><th class="section-data col1" rowspan="\${response.list.length + 1}">${m:rs('mtp-gem-messages', 'auth.webAuthnManage.availablePasskey')}<br>(${m:rs('mtp-gem-messages', 'auth.webAuthnManage.maxCount')}:&nbsp;\${response.registrationLimit})</th></tr>`);
					$.each(response.list, function(index, item) {
						let passkeyRow = `<tr><td class="section-data col1">\${item.authenticatorDisplayName}<br>
							${m:rs('mtp-gem-messages', 'auth.webAuthnManage.registrationDate')}&nbsp;\${dateUtil.format(item.startDate, dateUtil.getOutputDatetimeFormat())}`;
						if (item.lastLoginDate) {
							passkeyRow += `<br>${m:rs('mtp-gem-messages', 'auth.webAuthnManage.lastLoginDate')}&nbsp;\${dateUtil.format(item.lastLoginDate, dateUtil.getOutputDatetimeFormat())}`;
						}
						passkeyRow += `</td><td class="section-data col1"><input type="button" value="${m:rs('mtp-gem-messages', 'auth.webAuthnManage.delete')}" class="gr-btn deregister-wa" name="deregister" data-token-key="\${item.key}"></td></tr>`;
						webAuthnList.append(passkeyRow);
					});
					
					$("#webAuthnListSection .deregister-wa").on("click", function() {
						const params = {
							keyId: $(this).attr("data-token-key")
						};
						deregister(params);
					});
					
					$('#noWebAuthnList').hide();
					$('#webAuthnListSection').show();
					$(".fixHeight").fixHeight();
					
				} else {
					$('#noWebAuthnList').show();
					$('#webAuthnListSection').hide();
					$(".fixHeight").fixHeight();
				}
				
				if (response.registrationLimit > response.list.length) {
					$("#webAuthnRegisterSection input").prop('disabled', false);
				} else {
					$("#webAuthnRegisterSection input").prop('disabled', true);
				}
			}
		});
	}

	$("#webAuthnRegisterSection input.register-wa").on("click", function() {
		register();
	});
	
	if (window.PublicKeyCredential) {
		$('#noWebAuthnRegister').hide();
		$('#webAuthnRegister').show();
		keyList();
	}

});
</script>

<%}%>
