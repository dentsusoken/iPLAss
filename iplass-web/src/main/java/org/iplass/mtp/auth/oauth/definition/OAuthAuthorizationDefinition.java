/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.auth.oauth.definition;

import java.util.List;

import org.iplass.mtp.definition.Definition;

/**
 * OAuth2のAuthorizationServerに関する定義です。
 * 
 * @author K.Higuchi
 *
 */
public class OAuthAuthorizationDefinition implements Definition {
	private static final long serialVersionUID = 4147920670556792667L;

	/**
	 * すべてのロールを表現する定数です。実際の値は*です。
	 */
	public static final String AVAILABLE_ROLE_ANY = "*";

	private String name;
	private String displayName;
	private String description;

	private List<String> availableRoles;
	private List<ScopeDefinition> scopes;
	private String consentTemplateName;
	private List<ClientPolicyDefinition> clientPolicies;
	private SubjectIdentifierTypeDefinition subjectIdentifierType;

	private String issuerUri;
	
	public List<String> getAvailableRoles() {
		return availableRoles;
	}
	
	/**
	 * OAuth2によるアクセスを許可するroleを指定します。
	 * すべてのユーザに許可する場合は、*を指定します。
	 * @param availableRoles
	 */
	public void setAvailableRoles(List<String> availableRoles) {
		this.availableRoles = availableRoles;
	}
	public List<ScopeDefinition> getScopes() {
		return scopes;
	}
	
	/**
	 * 利用可能なスコープの定義を設定します。
	 * 
	 * @param scopes
	 */
	public void setScopes(List<ScopeDefinition> scopes) {
		this.scopes = scopes;
	}
	public String getConsentTemplateName() {
		return consentTemplateName;
	}
	
	/**
	 * スコープ承認画面のテンプレート名を指定します。
	 * 未指定の場合は、デフォルトの簡易な承認画面が利用されます。
	 * 
	 * @param consentTemplateName
	 */
	public void setConsentTemplateName(String consentTemplateName) {
		this.consentTemplateName = consentTemplateName;
	}
	public List<ClientPolicyDefinition> getClientPolicies() {
		return clientPolicies;
	}
	public void setClientPolicies(List<ClientPolicyDefinition> clientPolicies) {
		this.clientPolicies = clientPolicies;
	}
	public SubjectIdentifierTypeDefinition getSubjectIdentifierType() {
		return subjectIdentifierType;
	}
	public void setSubjectIdentifierType(SubjectIdentifierTypeDefinition subjectIdentifierType) {
		this.subjectIdentifierType = subjectIdentifierType;
	}
	public String getIssuerUri() {
		return issuerUri;
	}
	
	/**
	 * issuerのuriを指定します。
	 * 未指定の場合は、iPLAssがHost名などを元に自動で生成しますが、
	 * その自動生成される値ではなく特定の値としたい場合に指定します。
	 * 
	 * @param issuerUri
	 */
	public void setIssuerUri(String issuerUri) {
		this.issuerUri = issuerUri;
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
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
