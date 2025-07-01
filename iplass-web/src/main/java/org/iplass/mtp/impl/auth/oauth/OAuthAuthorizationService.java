/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition;
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinitionManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationCodeStore;
import org.iplass.mtp.impl.auth.oauth.jwt.JwtKeyStore;
import org.iplass.mtp.impl.auth.oauth.jwt.JwtProcessor;
import org.iplass.mtp.impl.auth.oauth.token.OAuthAccessTokenStore;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class OAuthAuthorizationService extends AbstractTypedMetaDataService<MetaOAuthAuthorization, OAuthAuthorizationRuntime> implements Service {
	public static final String OAUTH_AUTHZ_PATH = "/oauth/authZ/";
	public static final String DEFAULT_NAME = "DEFAULT";

	public static class TypeMap extends DefinitionMetaDataTypeMap<OAuthAuthorizationDefinition, MetaOAuthAuthorization> {
		public TypeMap() {
			super(getFixedPath(), MetaOAuthAuthorization.class, OAuthAuthorizationDefinition.class);
		}
		@Override
		public TypedDefinitionManager<OAuthAuthorizationDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(OAuthAuthorizationDefinitionManager.class);
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return DefinitionNameChecker.getPathSlashDefinitionNameChecker();
		}
	}
	
	private boolean paramStateRequired = true;
	private boolean paramNonceRequired = false;
	private boolean forceS256ForCodeChallengeMethod = true;
	private boolean forcePKCE = true;
	private String defaultConsentTemplateName;
	
	private AuthorizationCodeStore authorizationCodeStore;
	private OAuthAccessTokenStore accessTokenStore;
	private JwtProcessor jwtProcessor;
	private JwtKeyStore jwtKeyStore;
	
	private String subjectIdHashAlgorithm;
	private String subjectIdHashSalt;
	private long idTokenLifetimeSeconds = 60 * 60;//1 hour
	
	public long getIdTokenLifetimeSeconds() {
		return idTokenLifetimeSeconds;
	}

	public JwtProcessor getJwtProcessor() {
		return jwtProcessor;
	}

	public JwtKeyStore getJwtKeyStore() {
		return jwtKeyStore;
	}

	public String getDefaultConsentTemplateName() {
		return defaultConsentTemplateName;
	}

	public boolean isForcePKCE() {
		return forcePKCE;
	}

	public boolean isForceS256ForCodeChallengeMethod() {
		return forceS256ForCodeChallengeMethod;
	}

	public boolean isParamNonceRequired() {
		return paramNonceRequired;
	}

	public boolean isParamStateRequired() {
		return paramStateRequired;
	}

	public String getSubjectIdHashAlgorithm() {
		return subjectIdHashAlgorithm;
	}

	public String getSubjectIdHashSalt() {
		return subjectIdHashSalt;
	}

	public static String getFixedPath() {
		return OAUTH_AUTHZ_PATH;
	}

	@Override
	public Class<MetaOAuthAuthorization> getMetaDataType() {
		return MetaOAuthAuthorization.class;
	}

	@Override
	public Class<OAuthAuthorizationRuntime> getRuntimeType() {
		return OAuthAuthorizationRuntime.class;
	}

	@Override
	public void init(Config config) {
		paramStateRequired = config.getValue("paramStateRequired", Boolean.TYPE, Boolean.TRUE);
		paramNonceRequired = config.getValue("paramNonceRequired", Boolean.TYPE, Boolean.FALSE);
		forceS256ForCodeChallengeMethod = config.getValue("forceS256ForCodeChallengeMethod", Boolean.TYPE, Boolean.TRUE);
		forcePKCE = config.getValue("forcePKCE", Boolean.TYPE, Boolean.TRUE);
		defaultConsentTemplateName = config.getValue("defaultConsentTemplateName");
		authorizationCodeStore = config.getValue("authorizationCodeStore", AuthorizationCodeStore.class);
		accessTokenStore = config.getValue("accessTokenStore", OAuthAccessTokenStore.class);
		subjectIdHashAlgorithm = config.getValue("subjectIdHashAlgorithm");
		subjectIdHashSalt = config.getValue("subjectIdHashSalt");
		jwtProcessor = config.getValue("jwtProcessor", JwtProcessor.class);
		jwtKeyStore = config.getValue("jwtKeyStore", JwtKeyStore.class);
		idTokenLifetimeSeconds = config.getValue("idTokenLifetimeSeconds", Long.TYPE, 3600L);
		
		if (subjectIdHashAlgorithm != null) {
			try {
				MessageDigest.getInstance(subjectIdHashAlgorithm);
			} catch (NoSuchAlgorithmException e) {
				throw new ServiceConfigrationException("invalid subjectIdHashAlgorithm", e);
			}
		}
	}

	@Override
	public void destroy() {
	}

	public OAuthAuthorizationRuntime getOrDefault(String defName) {
		if (defName == null) {
			defName = DEFAULT_NAME;
		}
		return MetaDataContext.getContext().getMetaDataHandler(OAuthAuthorizationRuntime.class, OAUTH_AUTHZ_PATH  + defName);
	}
	
	public OAuthAccessTokenStore getAccessTokenStore() {
		return accessTokenStore;
	}
	
	public AuthorizationCodeStore getAuthorizationCodeStore() {
		return authorizationCodeStore;
	}

}
