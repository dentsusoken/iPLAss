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
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinition;
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;

public class OAuthResourceServerDefinitionManagerImpl extends AbstractTypedDefinitionManager<OAuthResourceServerDefinition> implements OAuthResourceServerDefinitionManager {

	private OAuthResourceServerService service = ServiceRegistry.getRegistry().getService(OAuthResourceServerService.class);

	@Override
	public IdPasswordCredential generateClientSecret(String resourceServerName) {
		return (IdPasswordCredential) service.getRuntimeByName(resourceServerName).generateCredential();
	}

	@Override
	public Class<OAuthResourceServerDefinition> getDefinitionType() {
		return OAuthResourceServerDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(OAuthResourceServerDefinition definition) {
		return new MetaOAuthResourceServer();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

}
