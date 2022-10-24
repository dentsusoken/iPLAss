/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.oidc;

import org.apache.http.client.HttpClient;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinition;
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinitionManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenIdConnectService extends AbstractTypedMetaDataService<MetaOpenIdConnect, OpenIdConnectRuntime> implements Service {
	public static final String OIDC_PATH = "/oidc/";
	public static final String DEFAULT_NAME = "DEFAULT";
	
	public static class TypeMap extends DefinitionMetaDataTypeMap<OpenIdConnectDefinition, MetaOpenIdConnect> {
		public TypeMap() {
			super(OIDC_PATH, MetaOpenIdConnect.class, OpenIdConnectDefinition.class);
		}
		@Override
		public TypedDefinitionManager<OpenIdConnectDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(OpenIdConnectDefinitionManager.class);
		}
	}

	private HttpClientConfig httpClientConfig;
	private ObjectMapper objectMapper;
	private ClientSecretHandler clientSecretHandler;

	private int allowedClockSkewMinutes;
	private int jwksCacheLifetimeMinutes = 6 * 60;//6時間
	private String clientSecretType = ClientSecretHandler.TYPE_OIDC_CLIENT_SECRET;

	public String getClientSecretType() {
		return clientSecretType;
	}

	public int getAllowedClockSkewMinutes() {
		return allowedClockSkewMinutes;
	}

	public int getJwksCacheLifetimeMinutes() {
		return jwksCacheLifetimeMinutes;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public HttpClientConfig getHttpClientConfig() {
		return httpClientConfig;
	}

	public HttpClient getHttpClient() {
		return httpClientConfig.getInstance();
	}
	
	@Override
	public Class<MetaOpenIdConnect> getMetaDataType() {
		return MetaOpenIdConnect.class;
	}

	@Override
	public Class<OpenIdConnectRuntime> getRuntimeType() {
		return OpenIdConnectRuntime.class;
	}

	@Override
	public void init(Config config) {
		allowedClockSkewMinutes = config.getValue("allowedClockSkewMinutes", Integer.TYPE, 0);
		jwksCacheLifetimeMinutes = config.getValue("jwksCacheLifetimeMinutes", Integer.TYPE, 6 * 60);//6時間
		clientSecretType = config.getValue("clientSecretType", String.class, ClientSecretHandler.TYPE_OIDC_CLIENT_SECRET);
		
		httpClientConfig = config.getValue("httpClientConfig", HttpClientConfig.class);
		if (httpClientConfig == null) {
			httpClientConfig = new HttpClientConfig();
			httpClientConfig.inited(this, config);
		}
		objectMapper = new ObjectMapper();
		clientSecretHandler = (ClientSecretHandler) ServiceRegistry.getRegistry().getService(AuthTokenService.class).getHandler(clientSecretType);
	}

	@Override
	public void destroy() {
	}

	public OpenIdConnectRuntime getOrDefault(String defName) {
		if (defName == null) {
			defName = DEFAULT_NAME;
		}
		return MetaDataContext.getContext().getMetaDataHandler(OpenIdConnectRuntime.class, OIDC_PATH + defName);
	}

	String getClientSecret(String metaDataId) {
		return clientSecretHandler.getClientSecret(metaDataId);
	}
	
	public void saveClientSecret(String metaDataId, String clientSecret) {
		clientSecretHandler.saveClientSecret(metaDataId, clientSecret);
		MetaDataContext.getContext().reloadById(metaDataId);
	}

	@Override
	public void removeMetaData(String definitionName) {
		OpenIdConnectRuntime r = getRuntimeByName(definitionName);
		if (r != null) {
			clientSecretHandler.deleteClientSecret(r.getMetaData().getId());
		}
		super.removeMetaData(definitionName);
	}

}
