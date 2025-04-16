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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.webauthn.definition.AttestationConveyancePreference;
import org.iplass.mtp.auth.webauthn.definition.AuthenticatorAttachment;
import org.iplass.mtp.auth.webauthn.definition.ResidentKeyRequirement;
import org.iplass.mtp.auth.webauthn.definition.UserVerificationRequirement;
import org.iplass.mtp.auth.webauthn.definition.WebAuthnDefinition;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.auth.authenticate.webauthn.store.CredentialRecordHandler;
import org.iplass.mtp.impl.auth.authenticate.webauthn.store.WebAuthnAuthenticatorInfoImpl;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.exception.DataConversionException;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.AuthenticationData;
import com.webauthn4j.data.AuthenticationParameters;
import com.webauthn4j.data.PublicKeyCredentialCreationOptions;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialRequestOptions;
import com.webauthn4j.data.RegistrationData;
import com.webauthn4j.data.RegistrationParameters;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.metadata.data.statement.MetadataStatement;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.verifier.attestation.statement.androidkey.AndroidKeyAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.androidsafetynet.AndroidSafetyNetAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.apple.AppleAnonymousAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.packed.PackedAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.tpm.TPMAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.u2f.FIDOU2FAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.trustworthiness.self.DefaultSelfAttestationTrustworthinessVerifier;
import com.webauthn4j.verifier.exception.TrustAnchorNotFoundException;
import com.webauthn4j.verifier.exception.VerificationException;

public class MetaWebAuthn extends BaseRootMetaData implements DefinableMetaData<WebAuthnDefinition> {
	private static final long serialVersionUID = -9084087363358415733L;

	private static Logger logger = LoggerFactory.getLogger(MetaWebAuthn.class);

	private String rpId;//未設定の場合は自動解決（ホスト名を適用）
	private String origin;//未設定の場合は自動解決（HttpServletから取得）
	private AttestationConveyancePreference attestationConveyancePreference = AttestationConveyancePreference.NONE;
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
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new WebAuthnRuntime();
	}

	@Override
	public RootMetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(WebAuthnDefinition definition) {
		this.name = definition.getName();
		this.description = definition.getDescription();
		this.displayName = definition.getDisplayName();
		this.localizedDisplayNameList = I18nUtil.toMeta(definition.getLocalizedDisplayNameList());

		this.rpId = definition.getRpId();
		this.origin = definition.getOrigin();
		this.attestationConveyancePreference = definition.getAttestationConveyancePreference();
		this.authenticatorAttachment = definition.getAuthenticatorAttachment();
		this.residentKeyRequirement = definition.getResidentKeyRequirement();
		this.userVerificationRequirement = definition.getUserVerificationRequirement();
		this.allowedAaguidList = (definition.getAllowedAaguidList() == null) ? null : new ArrayList<>(definition.getAllowedAaguidList());
		this.selfAttestationAllowed = definition.isSelfAttestationAllowed();
	}

	@Override
	public WebAuthnDefinition currentConfig() {
		WebAuthnDefinition definition = new WebAuthnDefinition();
		definition.setName(this.name);
		definition.setDescription(this.description);
		definition.setDisplayName(this.displayName);
		definition.setLocalizedDisplayNameList(I18nUtil.toDef(this.localizedDisplayNameList));
		definition.setRpId(this.rpId);
		definition.setOrigin(this.origin);
		definition.setAttestationConveyancePreference(this.attestationConveyancePreference);
		definition.setAuthenticatorAttachment(this.authenticatorAttachment);
		definition.setResidentKeyRequirement(this.residentKeyRequirement);
		definition.setUserVerificationRequirement(this.userVerificationRequirement);
		if (this.allowedAaguidList != null) {
			definition.setAllowedAaguidList(new ArrayList<>(this.allowedAaguidList));
		}
		definition.setSelfAttestationAllowed(this.selfAttestationAllowed);
		return definition;
	}

	public class WebAuthnRuntime extends BaseMetaDataRuntime {

		private WebAuthnService service = ServiceRegistry.getRegistry().getService(WebAuthnService.class);
		private CredentialRecordHandler crh = (CredentialRecordHandler) ServiceRegistry.getRegistry().getService(AuthTokenService.class).getHandler(CredentialRecordHandler.TYPE_WEBAUTHN_CREDENTIAL_DEFAULT);
		private AuthenticationPolicyService policyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
		
		private WebAuthnManager webAuthnManager;

		private WebAuthnRuntime() {
			if (attestationConveyancePreference == AttestationConveyancePreference.NONE) {
				webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager(service.getObjectConverter());
			} else {
				DefaultSelfAttestationTrustworthinessVerifier selfAttestationTrustworthinessVerifier = new DefaultSelfAttestationTrustworthinessVerifier();
				selfAttestationTrustworthinessVerifier.setSelfAttestationAllowed(selfAttestationAllowed);;
				webAuthnManager = new WebAuthnManager(
						Arrays.asList(
								new FIDOU2FAttestationStatementVerifier(),
								new PackedAttestationStatementVerifier(),
								new TPMAttestationStatementVerifier(),
								new AndroidKeyAttestationStatementVerifier(),
								new AndroidSafetyNetAttestationStatementVerifier(),
								new AppleAnonymousAttestationStatementVerifier()),
						service.getCertPathTrustworthinessVerifier(),
						selfAttestationTrustworthinessVerifier,
						service.getObjectConverter());
			}
			
		}

		@Override
		public MetaWebAuthn getMetaData() {
			return MetaWebAuthn.this;
		}

		public boolean isAllowedOnPolicy(AuthenticationPolicyRuntime policy) {
			if (policy.getMetaData().getWebAuthnDefinition() != null) {
				for (String waName : policy.getMetaData().getWebAuthnDefinition()) {
					if (getMetaData().getName().equals(waName)) {
						return true;
					}
				}
			}
			return false;
		}

		public void checkEnableRegistrationRequest(AuthContext authContext) {
			if (!authContext.isAuthenticated()) {
				throw new WebAuthnRuntimeException("not authenticated");
			}

			AuthenticationPolicyRuntime policy = policyService.getOrDefault(authContext.getPolicyName());
			if (!isAllowedOnPolicy(policy)) {
				throw new WebAuthnRuntimeException("policy not allow WebAuthnDefinition:" + name);
			}
		}

		public String publicKeyCredentialRequestOptions(WebAuthnServer server, boolean requireUserVerification) {
			checkState();

			UserVerificationRequirement uv = requireUserVerification ? UserVerificationRequirement.REQUIRED : userVerificationRequirement;
			PublicKeyCredentialRequestOptions options = service.getDataMapper().newPublicKeyCredentialRequestOptions(
					rpId(server), service.getWebAuthnChallengeGenerator().randomBytes(16),
					uv, TimeUnit.MINUTES.toMillis(service.getOptionsTimeoutMinutes()));

			WebAuthnState state = new WebAuthnState();
			state.setMetaDataName(name);
			state.setChallenge(options.getChallenge().getValue());
			state.setRpId(options.getRpId());
			server.saveWebAuthnState(state);

			return service.getObjectConverter().getJsonConverter().writeValueAsString(options);
		}

		public String publicKeyCredentialCreationOptions(User user, WebAuthnServer server) {
			checkState();

			PublicKeyCredentialCreationOptions options = service.getDataMapper().newPublicKeyCredentialCreationOptions(
					rpId(server), I18nUtil.stringMeta(displayName, localizedDisplayNameList),
					userId(user), user.getAccountId(), user.getName(),
					service.getWebAuthnChallengeGenerator().randomBytes(16),
					attestationConveyancePreference, authenticatorAttachment, residentKeyRequirement, userVerificationRequirement,
					service.getSignatureAlgorithms(),
					TimeUnit.MINUTES.toMillis(service.getOptionsTimeoutMinutes()));
			
			WebAuthnState state = new WebAuthnState();
			state.setMetaDataName(name);
			state.setChallenge(options.getChallenge().getValue());
			state.setRpId(options.getRp().getId());
			state.setUserId(options.getUser().getId());
			server.saveWebAuthnState(state);

			return service.getObjectConverter().getJsonConverter().writeValueAsString(options);
		}

		private byte[] userId(User user) {
			return service.getUserHandleSupplier().get(user);
		}

		private String rpId(WebAuthnServer server) {
			if (rpId != null) {
				return rpId;
			} else {
				return server.rpId();
			}
		}

		private String origin(WebAuthnServer server) {
			if (origin != null) {
				return origin;
			} else {
				return server.origin();
			}
		}

		public void registrationRequest(String publicKeyCredentialJson, User user, String policyName, WebAuthnServer server) {
			checkState();

			RegistrationData registrationData;
			try {
				registrationData = webAuthnManager.parseRegistrationResponseJSON(publicKeyCredentialJson);
			} catch (DataConversionException e) {
				throw new WebAuthnRuntimeException("Data conversion error occurred while parsing registration response JSON.", e);
			}

			WebAuthnState state = server.getWebAuthnState(true);
			if (state == null || !state.isAvailable(TimeUnit.MINUTES.toMillis(service.getOptionsTimeoutMinutes()))) {
				//invalid state
				if (logger.isDebugEnabled()) {
					logger.debug("invalid state:" + state);
				}
				throw new WebAuthnApplicationException("No challenge is available.");
			}

			//validate with metadata's constraints
			validateRegistrationData(registrationData);

			// Server properties
			Origin origin = service.getDataMapper().newOrigin(origin(server));
			String rpId = state.getRpId();
			Challenge challenge = service.getDataMapper().newChallenge(state.getChallenge());
			ServerProperty serverProperty = new ServerProperty(origin, rpId, challenge);

			// expectations
			List<PublicKeyCredentialParameters> pubKeyCredParams = service.getDataMapper().newPublicKeyCredentialParameters(service.getSignatureAlgorithms());
			boolean userVerificationRequired = (userVerificationRequirement == UserVerificationRequirement.REQUIRED);//client sideでrejectされるが一応
			boolean userPresenceRequired = true;

			RegistrationParameters registrationParameters = new RegistrationParameters(
					serverProperty, pubKeyCredParams, userVerificationRequired, userPresenceRequired);

			try {
				webAuthnManager.verify(registrationData, registrationParameters);
			} catch (TrustAnchorNotFoundException e) {
				if (logger.isDebugEnabled()) {
					logger.error("webauthn registration verification failed. TrustAnchor not found of aaguid: " +
							registrationData.getAttestationObject().getAuthenticatorData().getAttestedCredentialData().getAaguid().toString()
							+ ", publicKeyCredential:" + publicKeyCredentialJson, e);
				} else {
					logger.error("webauthn registration verification failed. TrustAnchor not found of aaguid: " +
							registrationData.getAttestationObject().getAuthenticatorData().getAttestedCredentialData().getAaguid().toString()
							+ ", publicKeyCredential of User:" + user.getOid(), e);
				}
				throw new WebAuthnApplicationException("WebAuthn registration verification failed.", e);
			} catch (VerificationException e) {
				if (logger.isDebugEnabled()) {
					logger.error("webauthn registration verification failed. publicKeyCredential:" + publicKeyCredentialJson, e);
				} else {
					logger.error("webauthn registration verification failed. publicKeyCredential of User:" + user.getOid(), e);
				}
				throw new WebAuthnApplicationException("WebAuthn registration verification failed.", e);
			}

			//total count check
			int count = crh.countCredentialRecordsByUser(user.getOid());
			if (count >= service.getRegistrationLimitOfAuthenticatorPerUser()) {
				throw new WebAuthnApplicationException("Registration limit exceeded.");
			}

			//persist CredentialRecord object, which will be used in the authentication process.
			CredentialRecord credentialRecord = new CredentialRecordImpl(
					registrationData.getAttestationObject(),
					registrationData.getCollectedClientData(),
					registrationData.getClientExtensions(),
					registrationData.getTransports());
			WebAuthnAuthenticatorInfoImpl authenticatorInfo = new WebAuthnAuthenticatorInfoImpl();
			authenticatorInfo.setCredentialRecord(credentialRecord);
			authenticatorInfo.setAuthenticatorDisplayName(
					authenticatorDisplayName(registrationData.getAttestationObject().getAuthenticatorData().getAttestedCredentialData()));
			authenticatorInfo.setUserHandle(state.getUserId());
			
			AuthToken at = crh.newAuthToken(user.getOid(), policyName, authenticatorInfo);
			crh.authTokenStore().create(at);
			if (logger.isDebugEnabled()) {
				logger.debug("WebAuthn registration success. user:" + user.getOid() + ", credentialId: " + at.getSeries());
			}
		}

		public WebAuthnVerifyResult verify(String publicKeyCredentialJson, WebAuthnServer server, boolean requireUserVerification) {
			checkState();

			AuthenticationData authenticationData;
			try {
				authenticationData = webAuthnManager.parseAuthenticationResponseJSON(publicKeyCredentialJson);
			} catch (DataConversionException e) {
				throw new WebAuthnRuntimeException("Data conversion error occurred while parsing registration response JSON.", e);
			}

			WebAuthnState state = server.getWebAuthnState(true);
			if (state == null || !state.isAvailable(TimeUnit.MINUTES.toMillis(service.getOptionsTimeoutMinutes()))) {
				//invalid state
				if (logger.isDebugEnabled()) {
					logger.debug("invalid state:" + state);
				}
				return new WebAuthnVerifyResult("invalid_state", "No challenge is available.", null);
			}

			// Server properties
			Origin origin = service.getDataMapper().newOrigin(origin(server));
			String rpId = state.getRpId();
			Challenge challenge = service.getDataMapper().newChallenge(state.getChallenge());
			ServerProperty serverProperty = new ServerProperty(origin, rpId, challenge);

			// expectations
			List<byte[]> allowCredentials = null;
			boolean userVerificationRequired;
			if (requireUserVerification) {
				userVerificationRequired = true;
			} else {
				userVerificationRequired = userVerificationRequirement == UserVerificationRequirement.REQUIRED;
			}
			boolean userPresenceRequired = true;

			//load CredentialRecord
			AuthToken at = crh.getAuthTokenByCredentialId(authenticationData.getCredentialId());
			if (at == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Credential not found:" + authenticationData);
				}
				return new WebAuthnVerifyResult("authentication_failed", "Authentication failed.", null);
			}
			WebAuthnAuthenticatorInfoImpl authenticatorInfo = (WebAuthnAuthenticatorInfoImpl) crh.toAuthTokenInfo(at);
			CredentialRecord credentialRecord = authenticatorInfo.getCredentialRecord();

			AuthenticationParameters authenticationParameters = new AuthenticationParameters(
					serverProperty,
					credentialRecord,
					allowCredentials,
					userVerificationRequired,
					userPresenceRequired);

			try {
				webAuthnManager.verify(authenticationData, authenticationParameters);
			} catch (VerificationException e) {
				return new WebAuthnVerifyResult("authentication_failed", "Authentication failed.", e);
			}
			
			//念のためチェック
			if (!Arrays.equals(authenticationData.getCredentialId(), credentialRecord.getAttestedCredentialData().getCredentialId())) {
				return new WebAuthnVerifyResult("invalid_credential_state", "Invalid credential state.", null);
			}
			if (authenticationData.getUserHandle() != null) {
				if (!Arrays.equals(authenticationData.getUserHandle(), authenticatorInfo.getUserHandle())) {
					return new WebAuthnVerifyResult("invalid_credential_state", "Invalid credential state.", null);
				}
			}
			
			// update the counter of the authenticator record
			credentialRecord.setCounter(authenticationData.getAuthenticatorData().getSignCount());
			credentialRecord.setUvInitialized(authenticationData.getAuthenticatorData().isFlagUV());
			credentialRecord.setBackedUp(authenticationData.getAuthenticatorData().isFlagBS());
			crh.updateCredentialRecord(at, credentialRecord, new Timestamp(System.currentTimeMillis()));
			
			return new WebAuthnVerifyResult(authenticationData.getCredentialId(), at.getToken(), at.getOwnerId(), at.getPolicyName());
		}

		private String authenticatorDisplayName(AttestedCredentialData attestedCredentialData) {
			MetadataStatement metadataStatement = null;
			if (attestedCredentialData != null) {
				String aaguid = attestedCredentialData.getAaguid().toString();
				metadataStatement = service.getMetadataStatement(aaguid);
			}
			if (metadataStatement != null) {
				return metadataStatement.getDescription();
			} else {
				return "Unknown Authenticator";
			}
		}

		private void validateRegistrationData(RegistrationData registrationData) {
			if (allowedAaguidList != null && !allowedAaguidList.isEmpty()
					&& attestationConveyancePreference != AttestationConveyancePreference.NONE) {
				String aaguid = registrationData.getAttestationObject().getAuthenticatorData().getAttestedCredentialData().getAaguid().toString();
				if (!allowedAaguidList.contains(aaguid)) {
					throw new WebAuthnApplicationException("Authenticator's AAGUID is not allowed.");
				}
			}
		}
	}

}
