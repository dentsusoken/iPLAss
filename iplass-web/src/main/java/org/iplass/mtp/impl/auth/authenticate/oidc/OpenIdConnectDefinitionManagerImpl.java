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

import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinition;
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinitionManager;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;

public class OpenIdConnectDefinitionManagerImpl extends AbstractTypedDefinitionManager<OpenIdConnectDefinition> implements OpenIdConnectDefinitionManager {
	
	private OpenIdConnectService service = ServiceRegistry.getRegistry().getService(OpenIdConnectService.class);

	@Override
	public Class<OpenIdConnectDefinition> getDefinitionType() {
		return OpenIdConnectDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(OpenIdConnectDefinition definition) {
		return new MetaOpenIdConnect();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}

	@Override
	public void saveClientSecret(String oidcDefinitionName, String clientSecret) {
		OpenIdConnectRuntime r = service.getRuntimeByName(oidcDefinitionName);
		service.saveClientSecret(r.getMetaData().getId(), clientSecret);
	}

}
