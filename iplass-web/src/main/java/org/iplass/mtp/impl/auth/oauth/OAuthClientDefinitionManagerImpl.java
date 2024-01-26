/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.oauth.definition.OAuthClientDefinition;
import org.iplass.mtp.auth.oauth.definition.OAuthClientDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;

public class OAuthClientDefinitionManagerImpl extends AbstractTypedDefinitionManager<OAuthClientDefinition> implements OAuthClientDefinitionManager {
	
	
	private OAuthClientService service = ServiceRegistry.getRegistry().getService(OAuthClientService.class);


	@Override
	public IdPasswordCredential generateClientSecret(String clientName) {
		return (IdPasswordCredential) service.getRuntimeByName(clientName).generateCredential();
	}

	@Override
	public Class<OAuthClientDefinition> getDefinitionType() {
		return OAuthClientDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(OAuthClientDefinition definition) {
		return new MetaOAuthClient();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

}
