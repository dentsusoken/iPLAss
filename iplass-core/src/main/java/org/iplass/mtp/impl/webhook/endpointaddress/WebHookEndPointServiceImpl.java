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
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.webhook.WebHookAuthTokenHandler;
import org.iplass.mtp.impl.webhook.endpointaddress.MetaWebHookEndPointDefinition.WebHookEndPointRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.endpoint.WebhookEndPoint;
import org.iplass.mtp.webhook.endpoint.definition.WebHookEndPointDefinition;
import org.iplass.mtp.webhook.endpoint.definition.WebHookEndPointDefinitionManager;

public class WebHookEndPointServiceImpl extends AbstractTypedMetaDataService<MetaWebHookEndPointDefinition, WebHookEndPointRuntime> implements WebHookEndPointService {
	WebHookAuthTokenHandler tokenHandler;

	public static final String WEBHOOKENDPOINT_DEFINITION_META_PATH = "/webhook/endpointaddress/";
	public static class TypeMap extends DefinitionMetaDataTypeMap<WebHookEndPointDefinition, MetaWebHookEndPointDefinition>{
		public TypeMap() {
			super(getFixedPath(), MetaWebHookEndPointDefinition.class, WebHookEndPointDefinition.class);
		}
		public static String getFixedPath() {
			return WEBHOOKENDPOINT_DEFINITION_META_PATH;
		}
		@Override
		public TypedDefinitionManager<WebHookEndPointDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(WebHookEndPointDefinitionManager.class);
		}
	}
	
	@Override
	public Class<MetaWebHookEndPointDefinition> getMetaDataType() {
		return MetaWebHookEndPointDefinition.class;
	}

	@Override
	public Class<WebHookEndPointRuntime> getRuntimeType() {
		return WebHookEndPointRuntime.class;
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
	public void deleteSecurityTokenById(String metaDataId) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		WebHookAuthTokenHandler tokenHandler = (WebHookAuthTokenHandler)ServiceRegistry
				.getRegistry().getService(AuthTokenService.class)
				.getHandler(WebHookAuthTokenHandler.TYPE_WEBHOOK_AUTHTOKEN_HANDLER);

		String series = metaDataId;
		tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, series);
		tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, series);
		tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, series);
		tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.CUSTOM_AUTHENTICATION_TYPE, series);
	}
	/**
	 * 
	 * basic = basicName+":"+basicPassword;
	 * */	
	@Override
	public void updateBasicSecurityTokenById(int tenantId,String metaDataId, String basic) {
		
		if (basic == null || basic.isEmpty()) {
			tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, metaDataId);
		} else if (getBasicTokenById(tenantId,metaDataId)==null) {
			String secret = Base64.encodeBase64String(basic.getBytes());
			tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		} else {
			String secret = Base64.encodeBase64String(basic.getBytes());
			tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		}
	}
	@Override
	public void updateBearerSecurityTokenById(int tenantId,String metaDataId, String secret) {
		if (secret == null || secret.isEmpty()) {
			tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, metaDataId);
		} else if (getBearerTokenById(tenantId, metaDataId)==null) {
			tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		} else {
			tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		}
	}
	@Override
	public void updateHmacSecurityTokenById(int tenantId,String metaDataId, String secret) {
		if (secret == null || secret.isEmpty()) {
			tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, metaDataId);
		} else if (getHmacTokenById(tenantId, metaDataId)==null) {
			tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		} else {
			tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		}
	}
	@Override
	public String getBasicTokenById(int tenantId,String metaDataId){
		String base64 = tokenHandler.getSecret(tenantId, metaDataId, WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE);
		if (base64 ==null || base64.isEmpty()) {
			return null;
		}
		String token = new String(Base64.decodeBase64(base64));
		return token;
	}
	@Override
	public String getBearerTokenById(int tenantId,String metaDataId){
		String token = tokenHandler.getSecret(tenantId, metaDataId, WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE);
		return token;
	}
	@Override
	public String getHmacTokenById(int tenantId,String metaDataId){
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

	@Override
	public void deleteSecurityTokenByDefinitionName(String definitionName) {
		
		deleteSecurityTokenById(getMetaIdByDefinitionName(definitionName));
	}

	@Override
	public void updateBasicSecurityTokenByDefinitionName(int tenantId, String definitionName, String basic) {
		updateBasicSecurityTokenById(tenantId,getMetaIdByDefinitionName(definitionName),basic);
		
	}

	@Override
	public void updateHmacSecurityTokenByDefinitionName(int tenantId, String definitionName, String secret) {
		
		updateHmacSecurityTokenById(tenantId,getMetaIdByDefinitionName(definitionName),secret);
	}

	@Override
	public void updateBearerSecurityTokenByDefinitionName(int tenantId, String definitionName, String secret) {
		updateBearerSecurityTokenById(tenantId, getMetaIdByDefinitionName(definitionName), secret);
		
	}

	@Override
	public String getHmacTokenByDefinitionName(int tenantId, String definitionName) {
		return getHmacTokenById(tenantId, getMetaIdByDefinitionName(definitionName));
	}

	@Override
	public String getBearerTokenByDefinitionName(int tenantId, String definitionName) {
		return getBearerTokenById(tenantId, getMetaIdByDefinitionName(definitionName));
	}

	@Override
	public String getBasicTokenByDefinitionName(int tenantId, String definitionName) {
		return getBasicTokenById(tenantId, getMetaIdByDefinitionName(definitionName));
	}
	
	private String getMetaIdByDefinitionName(String definitionName) {
		String path = DefinitionService.getInstance().getPathByMeta(MetaWebHookEndPointDefinition.class, definitionName);
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(path);
		return entry.getMetaData().getId();
	}
	
	public GroovyTemplate  getUrlTemplateByName(String definitionName) {
		return this.getRuntimeByName(definitionName).getUrlTemplate();
	}
	
	@Override
	public void updateCustomSecurityTokenByDefinitionName(int tenantId, String definitionName, String secret) {
		updateCustomSecurityTokenById(tenantId,getMetaIdByDefinitionName(definitionName),secret);
	}

	@Override
	public void updateCustomSecurityTokenById(int tenantId, String metaDataId, String secret) {
		if (secret == null || secret.isEmpty()) {
			tokenHandler.deleteSecret(tenantId, WebHookAuthTokenHandler.CUSTOM_AUTHENTICATION_TYPE, metaDataId);
		} else if (getCustomTokenById(tenantId, metaDataId)==null) {
			tokenHandler.insertSecret(tenantId, WebHookAuthTokenHandler.CUSTOM_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		} else {
			tokenHandler.updateSecret(tenantId, WebHookAuthTokenHandler.CUSTOM_AUTHENTICATION_TYPE, metaDataId, metaDataId, secret);
		}
	}

	@Override
	public String getCustomTokenByDefinitionName(int tenantId, String definitionName) {
		return getCustomTokenById(tenantId, getMetaIdByDefinitionName(definitionName));
	}

	@Override
	public String getCustomTokenById(int tenantId, String metaDataId) {
		String token = tokenHandler.getSecret(tenantId, metaDataId, WebHookAuthTokenHandler.CUSTOM_AUTHENTICATION_TYPE);
		return token;
	}
	
	/**
	 * <p>getToken</p> 
	 * トークンタイプ:WHHM,WHBT,WHBA,WHCT
	 * */
	@Override
	public String getSecurityToken(int tenantId, String definitionName,  String tokenType) {
		if(tokenType==null||tokenType.replaceAll("\\s","").isEmpty()) {
			return null;
		}
		if( WebHookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE.equals(tokenType)){
			return getHmacTokenByDefinitionName(tenantId,definitionName);
		}
		if( WebHookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE.equals(tokenType)){
			return getBearerTokenByDefinitionName(tenantId,definitionName);
		}
		if( WebHookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE.equals(tokenType)){
			return getBasicTokenByDefinitionName(tenantId,definitionName);
		}
		if( WebHookAuthTokenHandler.CUSTOM_AUTHENTICATION_TYPE.equals(tokenType)){
			return getCustomTokenByDefinitionName(tenantId,definitionName);
		}
		throw new RuntimeException("unknown TokenType");
	}

	@Override
	public WebhookEndPoint getWebhookEndPointByDefinitionName(String definitionName, Map<String, Object> binding) {
		return getRuntimeByName(definitionName).createWebhookEndPoint(binding);
	}
	
}
