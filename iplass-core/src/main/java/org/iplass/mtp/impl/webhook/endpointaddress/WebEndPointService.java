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
import org.iplass.mtp.impl.webhook.endpointaddress.MetaWebEndPointDefinition.WebEndPointRuntime;

public interface WebEndPointService extends TypedMetaDataService<MetaWebEndPointDefinition, WebEndPointRuntime>{

	public void deleteSecurityTokenByDef(String metaDataId);

	public void updateBasicSecurityTokenByDef(int tenantId,String metaDataId, String basic);

	public void updateHmacSecurityTokenByDef(int tenantId,String metaDataId, String secret);

	public void updateBearerSecurityTokenByDef(int tenantId,String metaDataId, String secret);
	
	public String getHmacTokenByDef(int tenantId,String metaDataId);

	public String getBearerTokenByDef(int tenantId,String metaDataId);

	public String getBasicTokenByDef(int tenantId,String metaDataId);

	String generateHmacTokenString();

}
