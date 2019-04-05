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
package org.iplass.mtp.impl.auth.oauth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.oauth.definition.CustomTokenIntrospectorDefinition;
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinition;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.auth.oauth.MetaCustomTokenIntrospector.CustomTokenIntrospectorRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;

public class MetaOAuthResourceServer extends BaseRootMetaData implements DefinableMetaData<OAuthResourceServerDefinition> {
	private static final long serialVersionUID = 1339189788049685788L;

	private List<MetaCustomTokenIntrospector> customTokenIntrospectors;

	public List<MetaCustomTokenIntrospector> getCustomTokenIntrospectors() {
		return customTokenIntrospectors;
	}

	public void setCustomTokenIntrospectors(List<MetaCustomTokenIntrospector> customTokenIntrospectors) {
		this.customTokenIntrospectors = customTokenIntrospectors;
	}

	@Override
	public void applyConfig(OAuthResourceServerDefinition def) {
		name = def.getName();
		description = def.getDescription();
		displayName = def.getDisplayName();
		if (def.getCustomTokenIntrospectors() != null) {
			customTokenIntrospectors = new ArrayList<>();
			for (CustomTokenIntrospectorDefinition d: def.getCustomTokenIntrospectors()) {
				MetaCustomTokenIntrospector m = MetaCustomTokenIntrospector.createInstance(d);
				m.applyConfig(d);
				customTokenIntrospectors.add(m);
			}
		} else {
			customTokenIntrospectors = null;
		}
	}

	@Override
	public OAuthResourceServerDefinition currentConfig() {
		OAuthResourceServerDefinition def = new OAuthResourceServerDefinition();
		def.setName(name);
		def.setDescription(description);
		def.setDisplayName(displayName);
		if (customTokenIntrospectors != null) {
			def.setCustomTokenIntrospectors(new ArrayList<>());
			for (MetaCustomTokenIntrospector m: customTokenIntrospectors) {
				def.getCustomTokenIntrospectors().add(m.currentConfig());
			}
		}
		return def;
	}

	@Override
	public MetaOAuthResourceServer copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public OAuthResourceServerRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new OAuthResourceServerRuntime();
	}

	public class OAuthResourceServerRuntime extends BaseMetaDataRuntime {
		private OAuthClientCredentialHandler ch = (OAuthClientCredentialHandler) ServiceRegistry.getRegistry().getService(AuthTokenService.class).getHandler(OAuthClientCredentialHandler.TYPE_RESOURCE_SERVER);
		private List<CustomTokenIntrospectorRuntime> customTokenIntrospectorRuntimes;


		private OAuthResourceServerRuntime() {
			try {
				if (customTokenIntrospectors != null) {
					customTokenIntrospectorRuntimes = new ArrayList<>();
					for (int i = 0; i < customTokenIntrospectors.size(); i++) {
						customTokenIntrospectorRuntimes.add(customTokenIntrospectors.get(i).createRuntime(getId(), i));
					}
				}
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		public MetaOAuthResourceServer getMetaData() {
			return MetaOAuthResourceServer.this;
		}

		public Credential generateCredential() {
			return ch.generateCredential(getName());
		}

		public boolean validateCredential(Credential cre) {
			return ch.validateCredential(cre, getName());
		}

		public void deleteOldCredential() {
			ch.deleteOldCredential(getName());
		}

		public Map<String, Object> toResponseMap(RequestContext request, AccessToken accessToken, OAuthAuthorizationRuntime authServer) {
			Map<String, Object> res = new HashMap<>();
			res.put("active", true);
			res.put("token_type", OAuthConstants.TOKEN_TYPE_BEARER);
			if (accessToken.getGrantedScopes() != null) {
				res.put("scope", String.join(" ", accessToken.getGrantedScopes()));
			}
			res.put("client_id", accessToken.getClientId());
			res.put("username", accessToken.getUser().getName());
			res.put("sub", accessToken.getUser().getOid());
			res.put("exp", accessToken.getExpirationTime());
			res.put("iat", accessToken.getIssuedAt());
			res.put("nbf", accessToken.getNotbefore());
			res.put("aud", getName());
			res.put("iss", authServer.issuerId(request));

			//以下は現状未レスポンス
			//jti
			// OPTIONAL.  String identifier for the token, as defined in JWT
			// [RFC7519].

			if (customTokenIntrospectors != null) {
				for (CustomTokenIntrospectorRuntime ctir: customTokenIntrospectorRuntimes) {
					if (!ctir.handle(res, request, accessToken)) {
						return null;
					}
				}
			}

			return res;
		}
	}

}
