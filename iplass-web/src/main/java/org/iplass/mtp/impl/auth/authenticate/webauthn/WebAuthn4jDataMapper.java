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
package org.iplass.mtp.impl.auth.authenticate.webauthn;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.auth.webauthn.definition.AttestationConveyancePreference;
import org.iplass.mtp.auth.webauthn.definition.AuthenticatorAttachment;
import org.iplass.mtp.auth.webauthn.definition.ResidentKeyRequirement;
import org.iplass.mtp.auth.webauthn.definition.UserVerificationRequirement;

import com.webauthn4j.data.AuthenticatorSelectionCriteria;
import com.webauthn4j.data.PublicKeyCredentialCreationOptions;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialRequestOptions;
import com.webauthn4j.data.PublicKeyCredentialRpEntity;
import com.webauthn4j.data.PublicKeyCredentialType;
import com.webauthn4j.data.PublicKeyCredentialUserEntity;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientInputs;
import com.webauthn4j.data.extension.client.RegistrationExtensionClientInput;

class WebAuthn4jDataMapper {
	//現状WebAuthn4jのみ利用想定なので、抽象化せず処理だけ分離

	PublicKeyCredentialRequestOptions newPublicKeyCredentialRequestOptions(
			String rpId,
			byte[] challengeBytes,
			UserVerificationRequirement userVerificationRequirement,
			Long timeout) {

		return new PublicKeyCredentialRequestOptions(
				newChallenge(challengeBytes),
				timeout,
				rpId,
				null,
				newUserVerificationRequirement(userVerificationRequirement),
				null,
				null);
	}

	PublicKeyCredentialCreationOptions newPublicKeyCredentialCreationOptions(
			String rpId, String rpName,
			byte[] userId, String userName, String userDisplayName,
			byte[] challengeBytes,
			AttestationConveyancePreference attestationConveyancePreference,
			AuthenticatorAttachment authenticatorAttachment,
			ResidentKeyRequirement residentKeyRequirement,
			UserVerificationRequirement userVerificationRequirement,
			List<String> algList,
			Long timeout) {

		return new PublicKeyCredentialCreationOptions(
				newPublicKeyCredentialRpEntity(rpId, rpName),
				newPublicKeyCredentialUserEntity(userId, userName, userDisplayName),
				newChallenge(challengeBytes),
				newPublicKeyCredentialParameters(algList),
				timeout,
				null,
				newAuthenticatorSelectionCriteria(authenticatorAttachment, residentKeyRequirement, userVerificationRequirement),
				null,
				newAttestationConveyancePreference(attestationConveyancePreference),
				newRegistrationExtensions());
	}

	private AuthenticationExtensionsClientInputs<RegistrationExtensionClientInput> newRegistrationExtensions() {
		AuthenticationExtensionsClientInputs.BuilderForRegistration builder = new AuthenticationExtensionsClientInputs.BuilderForRegistration();
		return builder.setCredProps(true).build();
	}

	private com.webauthn4j.data.AttestationConveyancePreference newAttestationConveyancePreference(
			AttestationConveyancePreference attestationConveyancePreference) {
		if (attestationConveyancePreference == null) {
			return null;
		}
		switch (attestationConveyancePreference) {
		case NONE:
			return com.webauthn4j.data.AttestationConveyancePreference.NONE;
		case INDIRECT:
			return com.webauthn4j.data.AttestationConveyancePreference.INDIRECT;
		case DIRECT:
			return com.webauthn4j.data.AttestationConveyancePreference.DIRECT;
		case ENTERPRISE:
			return com.webauthn4j.data.AttestationConveyancePreference.ENTERPRISE;
		default:
			throw new IllegalArgumentException("Unknown AttestationConveyancePreference: " + attestationConveyancePreference);
		}
	}

	private AuthenticatorSelectionCriteria newAuthenticatorSelectionCriteria(AuthenticatorAttachment authenticatorAttachment,
			ResidentKeyRequirement residentKeyRequirement,
			UserVerificationRequirement userVerificationRequirement) {
		return new AuthenticatorSelectionCriteria(
				newAuthenticatorAttachment(authenticatorAttachment),
				(residentKeyRequirement == ResidentKeyRequirement.REQUIRED ? true : false),
				newResidentKeyRequirement(residentKeyRequirement),
				newUserVerificationRequirement(userVerificationRequirement));
	}

	private com.webauthn4j.data.AuthenticatorAttachment newAuthenticatorAttachment(AuthenticatorAttachment authenticatorAttachment) {
		if (authenticatorAttachment == null) {
			return null;
		}
		switch (authenticatorAttachment) {
		case CROSS_PLATFORM:
			return com.webauthn4j.data.AuthenticatorAttachment.CROSS_PLATFORM;
		case PLATFORM:
			return com.webauthn4j.data.AuthenticatorAttachment.PLATFORM;
		default:
			throw new IllegalArgumentException("Unknown AuthenticatorAttachment: " + authenticatorAttachment);
		}
	}

	private com.webauthn4j.data.ResidentKeyRequirement newResidentKeyRequirement(ResidentKeyRequirement residentKeyRequirement) {
		if (residentKeyRequirement == null) {
			return null;
		}
		switch (residentKeyRequirement) {
		case DISCOURAGED:
			return com.webauthn4j.data.ResidentKeyRequirement.DISCOURAGED;
		case PREFERRED:
			return com.webauthn4j.data.ResidentKeyRequirement.PREFERRED;
		case REQUIRED:
			return com.webauthn4j.data.ResidentKeyRequirement.REQUIRED;
		default:
			throw new IllegalArgumentException("Unknown ResidentKeyRequirement: " + residentKeyRequirement);
		}
	}

	private com.webauthn4j.data.UserVerificationRequirement newUserVerificationRequirement(UserVerificationRequirement userVerificationRequirement) {

		if (userVerificationRequirement == null) {
			return null;
		}
		switch (userVerificationRequirement) {
		case DISCOURAGED:
			return com.webauthn4j.data.UserVerificationRequirement.DISCOURAGED;
		case PREFERRED:
			return com.webauthn4j.data.UserVerificationRequirement.PREFERRED;
		case REQUIRED:
			return com.webauthn4j.data.UserVerificationRequirement.REQUIRED;
		default:
			throw new IllegalArgumentException("Unknown UserVerificationRequirement: " + userVerificationRequirement);
		}
	}

	private PublicKeyCredentialRpEntity newPublicKeyCredentialRpEntity(String rpId, String rpName) {
		return new PublicKeyCredentialRpEntity(rpId, rpName);
	}

	private PublicKeyCredentialUserEntity newPublicKeyCredentialUserEntity(byte[] id, String name, String displayName) {
		return new PublicKeyCredentialUserEntity(id, name, displayName);
	}

	public Challenge newChallenge(byte[] challengeBytes) {
		return new DefaultChallenge(challengeBytes);
	}

	public List<PublicKeyCredentialParameters> newPublicKeyCredentialParameters(List<String> algList) {
		List<PublicKeyCredentialParameters> pubKeyCredParams = new ArrayList<>();
		for (String alg : algList) {
			pubKeyCredParams.add(newPublicKeyCredentialParameter(alg));
		}
		return pubKeyCredParams;
	}

	private PublicKeyCredentialParameters newPublicKeyCredentialParameter(String alg) {
		return new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, resolveAlg(alg));
	}

	private COSEAlgorithmIdentifier resolveAlg(String alg) {
		switch (alg) {
		case "ES256":
			return COSEAlgorithmIdentifier.ES256;
		case "ES384":
			return COSEAlgorithmIdentifier.ES384;
		case "ES512":
			return COSEAlgorithmIdentifier.ES512;
		case "RS1":
			return COSEAlgorithmIdentifier.RS1;
		case "RS256":
			return COSEAlgorithmIdentifier.RS256;
		case "RS384":
			return COSEAlgorithmIdentifier.RS384;
		case "RS512":
			return COSEAlgorithmIdentifier.RS512;
		case "EdDSA":
			return COSEAlgorithmIdentifier.EdDSA;
		default:
			throw new IllegalArgumentException("Unknown/Unsupported alg: " + alg);
		}
	}

	public Origin newOrigin(String origin) {
		return new Origin(origin);
	}

}
