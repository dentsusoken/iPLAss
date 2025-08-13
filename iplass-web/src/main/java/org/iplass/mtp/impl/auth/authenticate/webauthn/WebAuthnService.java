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

import java.util.List;
import java.util.Set;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.webauthn.definition.WebAuthnDefinition;
import org.iplass.mtp.auth.webauthn.definition.WebAuthnDefinitionManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.auth.authenticate.webauthn.MetaWebAuthn.WebAuthnRuntime;
import org.iplass.mtp.impl.auth.authenticate.webauthn.userhandle.UserHandleSupplier;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.util.random.SecureRandomGenerator;
import org.iplass.mtp.impl.util.random.SecureRandomService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;

import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.attestation.authenticator.AAGUID;
import com.webauthn4j.metadata.MetadataStatementRepository;
import com.webauthn4j.metadata.data.statement.MetadataStatement;
import com.webauthn4j.verifier.attestation.trustworthiness.certpath.CertPathTrustworthinessVerifier;
import com.webauthn4j.verifier.attestation.trustworthiness.certpath.DefaultCertPathTrustworthinessVerifier;

public class WebAuthnService extends AbstractTypedMetaDataService<MetaWebAuthn, WebAuthnRuntime> implements Service {
	/** メタデータパス */
	public static final String WEBAUTHN_PATH = "/webauthn/";
	/** メタデータデフォルト名 */
	public static final String DEFAULT_NAME = "DEFAULT";

	/**
	 * type map
	 */
	public static class TypeMap extends DefinitionMetaDataTypeMap<WebAuthnDefinition, MetaWebAuthn> {
		/**
		 * コンストラクタ
		 */
		public TypeMap() {
			super(WEBAUTHN_PATH, MetaWebAuthn.class, WebAuthnDefinition.class);
		}

		@Override
		public TypedDefinitionManager<WebAuthnDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(WebAuthnDefinitionManager.class);
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return DefinitionNameChecker.getPathSlashDefinitionNameChecker();
		}
	}

	//configurable properties
	private List<String> signatureAlgorithms;
	private int optionsTimeoutMinutes;
	private int registrationLimitOfAuthenticatorPerUser;
	private MetadataStatementRepository metadataStatementRepository;
	private UserHandleSupplier userHandleSupplier;

	//internal components
	private CertPathTrustworthinessVerifier certPathTrustworthinessVerifier;
	private ObjectConverter objectConverter;
	private WebAuthn4jDataMapper dataMapper;
	private SecureRandomGenerator webAuthnChallengeGenerator;

	@Override
	public void init(Config config) {
		signatureAlgorithms = config.getValues("signatureAlgorithms", String.class);
		optionsTimeoutMinutes = config.getValue("optionsTimeoutMinutes", Integer.TYPE, 5);
		registrationLimitOfAuthenticatorPerUser = config.getValue("registrationLimitOfAuthenticatorPerUser", Integer.TYPE, 3);
		metadataStatementRepository = config.getValue("metadataStatementRepository", MetadataStatementRepository.class);
		userHandleSupplier = config.getValue("userHandleSupplier", UserHandleSupplier.class);

		certPathTrustworthinessVerifier = new DefaultCertPathTrustworthinessVerifier(new TrustAnchorRepositoryImpl(metadataStatementRepository));
		objectConverter = new ObjectConverter();
		dataMapper = new WebAuthn4jDataMapper();
		webAuthnChallengeGenerator = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator("webAuthnChallengeGenerator");
	}

	@Override
	public void destroy() {

	}

	public int getOptionsTimeoutMinutes() {
		return optionsTimeoutMinutes;
	}

	public int getRegistrationLimitOfAuthenticatorPerUser() {
		return registrationLimitOfAuthenticatorPerUser;
	}

	public List<String> getSignatureAlgorithms() {
		return signatureAlgorithms;
	}

	public SecureRandomGenerator getWebAuthnChallengeGenerator() {
		return webAuthnChallengeGenerator;
	}

	@Override
	public Class<MetaWebAuthn> getMetaDataType() {
		return MetaWebAuthn.class;
	}

	@Override
	public Class<WebAuthnRuntime> getRuntimeType() {
		return WebAuthnRuntime.class;
	}

	public WebAuthnRuntime getOrDefault(String defName) {
		if (defName == null) {
			defName = DEFAULT_NAME;
		}
		return MetaDataContext.getContext().getMetaDataHandler(WebAuthnRuntime.class, WEBAUTHN_PATH + defName);
	}

	MetadataStatement getMetadataStatement(String aaguid) {
		//TODO 先頭の1件目で良いか？
		Set<MetadataStatement> statements = metadataStatementRepository.find(new AAGUID(aaguid));
		if (statements == null || statements.isEmpty()) {
			return null;
		} else {
			return statements.iterator().next();
		}
	}

	public ObjectConverter getObjectConverter() {
		return objectConverter;
	}

	WebAuthn4jDataMapper getDataMapper() {
		return dataMapper;
	}

	CertPathTrustworthinessVerifier getCertPathTrustworthinessVerifier() {
		return certPathTrustworthinessVerifier;
	}

	public UserHandleSupplier getUserHandleSupplier() {
		return userHandleSupplier;
	}

}
