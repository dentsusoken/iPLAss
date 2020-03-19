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

import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.webhook.endpointaddress.MetaWebHookEndPointDefinition.WebHookEndPointRuntime;
import org.iplass.mtp.webhook.template.endpointaddress.WebHookEndPoint;

public interface WebHookEndPointService extends TypedMetaDataService<MetaWebHookEndPointDefinition, WebHookEndPointRuntime>{

	void deleteSecurityTokenByDefinitionName(String definitionName);

	void updateBasicSecurityTokenByDefinitionName(int tenantId,String definitionName, String basic);

	void updateHmacSecurityTokenByDefinitionName(int tenantId,String definitionName, String secret);

	void updateBearerSecurityTokenByDefinitionName(int tenantId,String definitionName, String secret);
	
	String getHmacTokenByDefinitionName(int tenantId,String definitionName);

	String getBearerTokenByDefinitionName(int tenantId,String definitionName);

	String getBasicTokenByDefinitionName(int tenantId,String definitionName);

	void deleteSecurityTokenById(String metaDataId);

	void updateBasicSecurityTokenById(int tenantId,String metaDataId, String basic);

	void updateHmacSecurityTokenById(int tenantId,String metaDataId, String secret);

	void updateBearerSecurityTokenById(int tenantId,String metaDataId, String secret);
	
	String getHmacTokenById(int tenantId,String metaDataId);

	String getBearerTokenById(int tenantId,String metaDataId);

	String getBasicTokenById(int tenantId,String metaDataId);
	
	GroovyTemplate getUrlTemplateByName(String definitionName);
	
	String generateHmacTokenString();
	
	WebHookEndPoint getWebHookEndPointByName(String definitionName);

}
