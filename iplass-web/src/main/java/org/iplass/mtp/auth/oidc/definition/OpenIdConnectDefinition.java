/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
 * 
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.mtp.auth.oidc.definition;

import java.util.List;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * iPLAssがOpenID Connect RPとして動作する場合の定義です。
 * 
 * @author K.Higuchi
 *
 */
public class OpenIdConnectDefinition implements Definition {
	private static final long serialVersionUID = -6374680220009311372L;

	private String name;
	private String displayName;
	private List<LocalizedStringDefinition> localizedDisplayNameList;
	private String description;

	private String issuer;
	private String authorizationEndpoint;
	private String tokenEndpoint;
	private String userInfoEndpoint;
	private String jwksEndpoint;
	private String jwksContents;
	private String clientId;
	private List<String> scopes;
	private ClientAuthenticationType clientAuthenticationType;
	private boolean useNonce = true;
	private boolean enablePKCE = true;//methodはS256固定
	private boolean issParameterSupported = true;
	private boolean validateSign;
	private ResponseMode responseMode = ResponseMode.FORM_POST;
	private String subjectNameClaim ="preferred_username";
	private String autoUserProvisioningHandler;
	private boolean enableTransientUser;

	private String backUrlAfterAuth;
	private String backUrlAfterConnect;
	private String prompt;
	
	public String getBackUrlAfterAuth() {
		return backUrlAfterAuth;
	}

	public void setBackUrlAfterAuth(String backUrlAfterAuth) {
		this.backUrlAfterAuth = backUrlAfterAuth;
	}

	public String getBackUrlAfterConnect() {
		return backUrlAfterConnect;
	}

	public void setBackUrlAfterConnect(String backUrlAfterConnect) {
		this.backUrlAfterConnect = backUrlAfterConnect;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getAuthorizationEndpoint() {
		return authorizationEndpoint;
	}

	public void setAuthorizationEndpoint(String authorizationEndpoint) {
		this.authorizationEndpoint = authorizationEndpoint;
	}

	public String getTokenEndpoint() {
		return tokenEndpoint;
	}

	public void setTokenEndpoint(String tokenEndpoint) {
		this.tokenEndpoint = tokenEndpoint;
	}

	public String getUserInfoEndpoint() {
		return userInfoEndpoint;
	}

	public void setUserInfoEndpoint(String userInfoEndpoint) {
		this.userInfoEndpoint = userInfoEndpoint;
	}

	public String getJwksEndpoint() {
		return jwksEndpoint;
	}

	public void setJwksEndpoint(String jwksEndpoint) {
		this.jwksEndpoint = jwksEndpoint;
	}

	public String getJwksContents() {
		return jwksContents;
	}

	public void setJwksContents(String jwksContents) {
		this.jwksContents = jwksContents;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public List<String> getScopes() {
		return scopes;
	}

	public void setScopes(List<String> scopes) {
		this.scopes = scopes;
	}

	public ClientAuthenticationType getClientAuthenticationType() {
		return clientAuthenticationType;
	}

	public void setClientAuthenticationType(ClientAuthenticationType clientAuthenticationType) {
		this.clientAuthenticationType = clientAuthenticationType;
	}

	public boolean isUseNonce() {
		return useNonce;
	}

	public void setUseNonce(boolean useNonce) {
		this.useNonce = useNonce;
	}

	public boolean isEnablePKCE() {
		return enablePKCE;
	}

	public void setEnablePKCE(boolean enablePKCE) {
		this.enablePKCE = enablePKCE;
	}

	public boolean isIssParameterSupported() {
		return issParameterSupported;
	}

	public void setIssParameterSupported(boolean issParameterSupported) {
		this.issParameterSupported = issParameterSupported;
	}

	public boolean isValidateSign() {
		return validateSign;
	}

	public void setValidateSign(boolean validateSign) {
		this.validateSign = validateSign;
	}

	public ResponseMode getResponseMode() {
		return responseMode;
	}

	public void setResponseMode(ResponseMode responseMode) {
		this.responseMode = responseMode;
	}

	public String getSubjectNameClaim() {
		return subjectNameClaim;
	}

	public void setSubjectNameClaim(String subjectNameClaim) {
		this.subjectNameClaim = subjectNameClaim;
	}

	public String getAutoUserProvisioningHandler() {
		return autoUserProvisioningHandler;
	}

	public void setAutoUserProvisioningHandler(String autoUserProvisioningHandler) {
		this.autoUserProvisioningHandler = autoUserProvisioningHandler;
	}

	public boolean isEnableTransientUser() {
		return enableTransientUser;
	}

	public void setEnableTransientUser(boolean enableTransientUser) {
		this.enableTransientUser = enableTransientUser;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

}
