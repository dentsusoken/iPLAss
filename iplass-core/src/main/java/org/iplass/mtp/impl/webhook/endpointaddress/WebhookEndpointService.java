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

import java.util.Map;

import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.webhook.endpointaddress.MetaWebhookEndpointDefinition.WebhookEndpointRuntime;
import org.iplass.mtp.webhook.endpoint.WebhookEndpoint;

public interface WebhookEndpointService extends TypedMetaDataService<MetaWebhookEndpointDefinition, WebhookEndpointRuntime>{

	/** delete all */
	void deleteSecurityTokenByDefinitionName(String definitionName);
	void deleteSecurityTokenById(String metaDataId);
	
	/** if secret is null, it will delete the token. */
	void updateBasicSecurityTokenByDefinitionName(int tenantId,String definitionName, String basic);
	void updateHmacSecurityTokenByDefinitionName(int tenantId,String definitionName, String secret);
	void updateBearerSecurityTokenByDefinitionName(int tenantId,String definitionName, String secret);
	void updateCustomSecurityTokenByDefinitionName(int tenantId,String definitionName, String secret);

	void updateBasicSecurityTokenById(int tenantId,String metaDataId, String basic);
	void updateHmacSecurityTokenById(int tenantId,String metaDataId, String secret);
	void updateBearerSecurityTokenById(int tenantId,String metaDataId, String secret);
	void updateCustomSecurityTokenById(int tenantId,String metaDataId, String secret);
	
	//secret getter
	String getSecurityToken(int tenantId, String definitionName, String tokenType);//総合メソッド。tokenTypeで下記単体メソッドを呼ぶ
	
	String getHmacTokenByDefinitionName(int tenantId,String definitionName);
	String getBearerTokenByDefinitionName(int tenantId,String definitionName);
	String getBasicTokenByDefinitionName(int tenantId,String definitionName);
	String getCustomTokenByDefinitionName(int tenantId,String definitionName);

	String getHmacTokenById(int tenantId,String metaDataId);
	String getBearerTokenById(int tenantId,String metaDataId);
	String getBasicTokenById(int tenantId,String metaDataId);
	String getCustomTokenById(int tenantId,String metaDataId);

	String generateHmacTokenString();
	
	WebhookEndpoint getWebhookEndpointByDefinitionName(String definitionName, Map<String, Object> binding);
	
}
