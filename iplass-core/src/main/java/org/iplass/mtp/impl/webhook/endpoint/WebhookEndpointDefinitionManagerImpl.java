/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webhook.endpoint;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.webhook.WebhookAuthTokenHandler;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webhook.endpoint.definition.WebhookEndpointDefinition;
import org.iplass.mtp.webhook.endpoint.definition.WebhookEndpointDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebhookEndpointDefinitionManagerImpl extends AbstractTypedDefinitionManager<WebhookEndpointDefinition> implements WebhookEndpointDefinitionManager{
	private static final Logger logger = LoggerFactory.getLogger(WebhookEndpointDefinitionManager.class);
	private WebhookEndpointService service;
	
	public WebhookEndpointDefinitionManagerImpl() {
		this.service = ServiceRegistry.getRegistry().getService(WebhookEndpointService.class);
	}
	
	@Override
	public Class<WebhookEndpointDefinition> getDefinitionType() {
		return WebhookEndpointDefinition.class; 
	}

	/**
	 * remove時、自動でdb削除。
	 * */
	@Override
	public DefinitionModifyResult remove(String definitionName) {
		WebhookEndpointDefinition definition = super.get(definitionName);
		
		WebhookEndpointService weps = (WebhookEndpointService) getService();
		try {
			weps.deleteSecurityTokenByDefinitionName(definition.getName());
		} catch (Exception e) {
			logger.warn("Exception occured while removing the data from Database for :"+definitionName+". Caused by: "+e.getCause()+". With following message : "+e.getCause().getMessage());
			throw (RuntimeException) e;
		}
		
		return super.remove(definitionName);
	}
	
	/**
	 * <p>updateToken</p> 
	 *  トークンタイプ:WHHM,WHBT,WHBA
	 * 
	 * */
	@Override
	public void modifySecurityToken(int tenantId, String definitionName, String secret, String tokenType) {
		if(tokenType==null) {
			throw new RuntimeException("null TokenType");
		}
		if( WebhookAuthTokenHandler.HMAC_AUTHENTICATION_TYPE.equals(tokenType)){
			service.updateHmacSecurityTokenByDefinitionName(tenantId,definitionName,secret);
		}
		if( WebhookAuthTokenHandler.BEARER_AUTHENTICATION_TYPE.equals(tokenType)){
			service.updateBearerSecurityTokenByDefinitionName(tenantId,definitionName,secret);
		}
		if( WebhookAuthTokenHandler.BASIC_AUTHENTICATION_TYPE.equals(tokenType)){
			service.updateBasicSecurityTokenByDefinitionName(tenantId,definitionName,secret);
		}
		if( WebhookAuthTokenHandler.CUSTOM_AUTHENTICATION_TYPE.equals(tokenType)){
			service.updateCustomSecurityTokenByDefinitionName(tenantId,definitionName,secret);
		}
	}

	@Override
	public String generateHmacKey() {
		return service.generateHmacTokenString();
	}

	@Override
	protected RootMetaData newInstance(WebhookEndpointDefinition definition) {
		return new MetaWebhookEndpoint();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
