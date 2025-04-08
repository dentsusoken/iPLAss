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
package org.iplass.mtp.impl.auth.authenticate.webauthn.store;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnService;
import org.iplass.mtp.spi.ServiceRegistry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.webauthn4j.converter.AttestedCredentialDataConverter;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.AuthenticatorTransport;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.statement.AttestationStatement;
import com.webauthn4j.data.client.CollectedClientData;
import com.webauthn4j.data.extension.authenticator.AuthenticationExtensionsAuthenticatorOutputs;
import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientOutputs;
import com.webauthn4j.data.extension.client.RegistrationExtensionClientOutput;

class CredentialRecordMement implements Serializable {
	private static final long serialVersionUID = -583879034912856541L;

	private Boolean uvInitialized;
	private Boolean backupEligible;
	private Boolean backedUp;
	private byte[] attestedCredentialData;
	private long counter;
	private String clientData;
	private String clientExtensions;
	private String transports;
	private byte[] attestationStatement;
	private byte[] authenticatorExtensions;

	private String authenticatorDisplayName;
	private Timestamp lastLoginDate;

	CredentialRecordMement() {
	}

	CredentialRecordMement(CredentialRecord credentialRecord) {
		WebAuthnService service = ServiceRegistry.getRegistry().getService(WebAuthnService.class);

		this.attestedCredentialData = serialazeAttestedCredentialData(credentialRecord.getAttestedCredentialData(), service);
		this.attestationStatement = serializeAttestationStatement(credentialRecord.getAttestationStatement(), service);
		this.counter = credentialRecord.getCounter();
		this.authenticatorExtensions = service.getObjectConverter().getCborConverter().writeValueAsBytes(credentialRecord.getAuthenticatorExtensions());
		this.uvInitialized = credentialRecord.isUvInitialized();
		this.backupEligible = credentialRecord.isBackupEligible();
		this.backedUp = credentialRecord.isBackedUp();
		this.clientData = service.getObjectConverter().getJsonConverter().writeValueAsString(credentialRecord.getClientData());
		this.clientExtensions = service.getObjectConverter().getJsonConverter().writeValueAsString(credentialRecord.getClientExtensions());
		this.transports = service.getObjectConverter().getJsonConverter().writeValueAsString(credentialRecord.getTransports());
	}

	CredentialRecord toCredentialRecord() {
		WebAuthnService service = ServiceRegistry.getRegistry().getService(WebAuthnService.class);

		return new CredentialRecordImpl(
				deserializeAttestationStatement(attestationStatement, service),
				uvInitialized,
				backupEligible,
				backedUp,
				counter,
				deserializeAttestedCredentialData(attestedCredentialData, service),
				service.getObjectConverter().getCborConverter().readValue(authenticatorExtensions,
						new TypeReference<AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput>>() {}),
				service.getObjectConverter().getJsonConverter().readValue(clientData, CollectedClientData.class),
				service.getObjectConverter().getJsonConverter().readValue(clientExtensions,
						new TypeReference< AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput>>() {}),
				service.getObjectConverter().getJsonConverter().readValue(transports, new TypeReference<Set<AuthenticatorTransport>>() {})
				);
	}

	private byte[] serialazeAttestedCredentialData(AttestedCredentialData acd, WebAuthnService service) {
		AttestedCredentialDataConverter attestedCredentialDataConverter = new AttestedCredentialDataConverter(service.getObjectConverter());
		return attestedCredentialDataConverter.convert(acd);
	}

	private AttestedCredentialData deserializeAttestedCredentialData(byte[] acd, WebAuthnService service) {
		AttestedCredentialDataConverter attestedCredentialDataConverter = new AttestedCredentialDataConverter(service.getObjectConverter());
		return attestedCredentialDataConverter.convert(acd);
	}

	private byte[] serializeAttestationStatement(AttestationStatement as, WebAuthnService service) {
		AttestationStatementEnvelope asEenv = new AttestationStatementEnvelope(as);
		return service.getObjectConverter().getCborConverter().writeValueAsBytes(asEenv);
	}

	private AttestationStatement deserializeAttestationStatement(byte[] as, WebAuthnService service) {
		AttestationStatementEnvelope asEnv = service.getObjectConverter().getCborConverter().readValue(as, AttestationStatementEnvelope.class);
		return asEnv.getAttestationStatement();
	}

	String getAuthenticatorDisplayName() {
		return authenticatorDisplayName;
	}

	void setAuthenticatorDisplayName(String authenticatorDisplayName) {
		this.authenticatorDisplayName = authenticatorDisplayName;
	}

	public Timestamp getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

}
