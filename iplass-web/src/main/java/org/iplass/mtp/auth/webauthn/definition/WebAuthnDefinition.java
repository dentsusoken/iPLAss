/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.auth.webauthn.definition;

import java.util.List;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

public class WebAuthnDefinition implements Definition {
	private static final long serialVersionUID = 6699487573619224285L;

	private String name;
	private String description;
	private String displayName;//as rp entity name
	private List<LocalizedStringDefinition> localizedDisplayNameList;

	private String rpId;//未設定の場合は自動解決（ホスト名を適用）
	private String origin;//未設定の場合は自動解決（HttpServletから取得）

	//以下デフォルトのままでも動作可能だが、登録可能なAuthenticatorに制約を付けたい場合指定
	private AttestationConveyancePreference attestationConveyancePreference = AttestationConveyancePreference.NONE;//default none
	private AuthenticatorAttachment authenticatorAttachment;
	private ResidentKeyRequirement residentKeyRequirement = ResidentKeyRequirement.PREFERRED;
	private UserVerificationRequirement userVerificationRequirement = UserVerificationRequirement.PREFERRED;
	private List<String> allowedAaguidList;
	private boolean selfAttestationAllowed = true;

	public String getRpId() {
		return rpId;
	}

	public void setRpId(String rpId) {
		this.rpId = rpId;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public AttestationConveyancePreference getAttestationConveyancePreference() {
		return attestationConveyancePreference;
	}

	public void setAttestationConveyancePreference(AttestationConveyancePreference attestationConveyancePreference) {
		this.attestationConveyancePreference = attestationConveyancePreference;
	}

	public AuthenticatorAttachment getAuthenticatorAttachment() {
		return authenticatorAttachment;
	}

	public void setAuthenticatorAttachment(AuthenticatorAttachment authenticatorAttachment) {
		this.authenticatorAttachment = authenticatorAttachment;
	}

	public ResidentKeyRequirement getResidentKeyRequirement() {
		return residentKeyRequirement;
	}

	public void setResidentKeyRequirement(ResidentKeyRequirement residentKeyRequirement) {
		this.residentKeyRequirement = residentKeyRequirement;
	}

	public UserVerificationRequirement getUserVerificationRequirement() {
		return userVerificationRequirement;
	}

	public void setUserVerificationRequirement(UserVerificationRequirement userVerificationRequirement) {
		this.userVerificationRequirement = userVerificationRequirement;
	}

	public List<String> getAllowedAaguidList() {
		return allowedAaguidList;
	}

	public void setAllowedAaguidList(List<String> allowedAaguidList) {
		this.allowedAaguidList = allowedAaguidList;
	}

	public boolean isSelfAttestationAllowed() {
		return selfAttestationAllowed;
	}

	public void setSelfAttestationAllowed(boolean selfAttestationAllowed) {
		this.selfAttestationAllowed = selfAttestationAllowed;
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
