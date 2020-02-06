/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webhook.endpointaddress;

import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.webhook.WebHookAuthTokenHandler;
import org.iplass.mtp.impl.webhook.endpointaddress.MetaWebEndPointDefinition.WebEndPointRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.template.endpointaddress.WebEndPointDefinition;
import org.iplass.mtp.webhook.template.endpointaddress.WebEndPointDefinitionManager;

public class WebEndPointServiceImpl extends AbstractTypedMetaDataService<MetaWebEndPointDefinition, WebEndPointRuntime> implements WebEndPointService {
	WebHookAuthTokenHandler tokenHandler;

	public static final String WEBENDPOINT_DEFINITION_META_PATH = "/webhook/endpointaddress/";
	public static class TypeMap extends DefinitionMetaDataTypeMap<WebEndPointDefinition, MetaWebEndPointDefinition>{
		public TypeMap() {
			super(getFixedPath(), MetaWebEndPointDefinition.class, WebEndPointDefinition.class);
		}
		public static String getFixedPath() {
			return WEBENDPOINT_DEFINITION_META_PATH;
		}
		@Override
		public TypedDefinitionManager<WebEndPointDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(WebEndPointDefinitionManager.class);
		}
	}
	
	@Override
	public Class<MetaWebEndPointDefinition> getMetaDataType() {
		return MetaWebEndPointDefinition.class;
	}

	@Override
	public Class<WebEndPointRuntime> getRuntimeType() {
		return WebEndPointRuntime.class;
	}

	@Override
	public void init(Config config) {
		tokenHandler = (WebHookAuthTokenHandler)ServiceRegistry
				.getRegistry().getService(AuthTokenService.class)
				.getHandler(WebHookAuthTokenHandler.TYPE_WEBHOOK_AUTHTOKEN_HANDLER);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void deleteSecurityTokenByDef(String metaDataId) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		WebHookAuthTokenHandler tokenHandler = (WebHookAuthTokenHandler)ServiceRegistry
				.getRegistry().getService(AuthTokenService.class)
				.getHandler(WebHookAuthTokenHandler.TYPE_WEBHOOK_AUTHTOKEN_HANDLER);

		String series = metaDataId;
		tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, series);
		tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, series);
		tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, series);
	}
	/**
	 * 
	 * basic = basicName+":"+basicPassword;
	 * */	
	@Override
	public void updateBasicSecurityTokenByDef(int tenantId,String metaDataId, String basic) {
		
		if (basic == null || basic.isEmpty()) {
			tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, metaDataId);
		} else if (getBasicTokenByDef(tenantId,metaDataId)==null) {
			String secret = Base64.encodeBase64String(basic.getBytes());
			tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		} else {
			String secret = Base64.encodeBase64String(basic.getBytes());
			tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		}
	}
	@Override
	public void updateBearerSecurityTokenByDef(int tenantId,String metaDataId, String secret) {
		if (secret == null || secret.isEmpty()) {
			tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, metaDataId);
		} else if (getBearerTokenByDef(tenantId, metaDataId)==null) {
			tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		} else {
			tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		}
	}
	@Override
	public void updateHmacSecurityTokenByDef(int tenantId,String metaDataId, String secret) {
		if (secret == null || secret.isEmpty()) {
			tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, metaDataId);
		} else if (getHmacTokenByDef(tenantId, metaDataId)==null) {
			tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		} else {
			tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		}
	}
	@Override
	public String getBasicTokenByDef(int tenantId,String metaDataId){
		String base64 = tokenHandler.getSecret(tenantId, metaDataId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE);
		if (base64 ==null || base64.isEmpty()) {
			return null;
		}
		String token = new String(Base64.decodeBase64(base64));
		return token;
	}
	@Override
	public String getBearerTokenByDef(int tenantId,String metaDataId){
		String token = tokenHandler.getSecret(tenantId, metaDataId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE);
		return token;
	}
	@Override
	public String getHmacTokenByDef(int tenantId,String metaDataId){
		String token = tokenHandler.getSecret(tenantId, metaDataId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE);
		return token;
	}
	
	@Override
	public String generateHmacTokenString() {
			KeyGenerator keyGen;
			try {
				keyGen = KeyGenerator.getInstance("HmacSHA256");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			SecretKey secretKey = keyGen.generateKey();
			String encodedKey = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
		return encodedKey;
	}
}
