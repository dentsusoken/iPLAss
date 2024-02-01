/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.oauth.definition.OAuthClientDefinition;
import org.iplass.mtp.auth.oauth.definition.OAuthClientDefinitionManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class OAuthClientService extends AbstractTypedMetaDataService<MetaOAuthClient, OAuthClientRuntime> implements Service {
	public static final String OAUTH_CLIENT_PATH = "/oauth/client/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<OAuthClientDefinition, MetaOAuthClient> {
		public TypeMap() {
			super(getFixedPath(), MetaOAuthClient.class, OAuthClientDefinition.class);
		}
		@Override
		public TypedDefinitionManager<OAuthClientDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(OAuthClientDefinitionManager.class);
		}
	}

	public static String getFixedPath() {
		return OAUTH_CLIENT_PATH;
	}

	@Override
	public Class<MetaOAuthClient> getMetaDataType() {
		return MetaOAuthClient.class;
	}

	@Override
	public Class<OAuthClientRuntime> getRuntimeType() {
		return OAuthClientRuntime.class;
	}

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}
	
}
