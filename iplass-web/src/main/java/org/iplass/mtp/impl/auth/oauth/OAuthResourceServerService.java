/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinition;
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinitionManager;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthResourceServer.OAuthResourceServerRuntime;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class OAuthResourceServerService extends AbstractTypedMetaDataService<MetaOAuthResourceServer, OAuthResourceServerRuntime> implements Service {
	public static final String OAUTH_RS_PATH = "/oauth/resource/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<OAuthResourceServerDefinition, MetaOAuthResourceServer> {
		public TypeMap() {
			super(getFixedPath(), MetaOAuthResourceServer.class, OAuthResourceServerDefinition.class);
		}
		@Override
		public TypedDefinitionManager<OAuthResourceServerDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(OAuthResourceServerDefinitionManager.class);
		}
	}

	public static String getFixedPath() {
		return OAUTH_RS_PATH;
	}

	@Override
	public Class<MetaOAuthResourceServer> getMetaDataType() {
		return MetaOAuthResourceServer.class;
	}

	@Override
	public Class<OAuthResourceServerRuntime> getRuntimeType() {
		return OAuthResourceServerRuntime.class;
	}

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}
	
}
