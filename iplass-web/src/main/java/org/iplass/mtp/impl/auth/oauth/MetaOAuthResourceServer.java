/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.oauth.definition.OAuthResourceServerDefinition;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;

public class MetaOAuthResourceServer extends BaseRootMetaData implements DefinableMetaData<OAuthResourceServerDefinition> {
	private static final long serialVersionUID = 1339189788049685788L;

	@Override
	public void applyConfig(OAuthResourceServerDefinition def) {
		name = def.getName();
		description = def.getDescription();
		displayName = def.getDisplayName();
	}

	@Override
	public OAuthResourceServerDefinition currentConfig() {
		OAuthResourceServerDefinition def = new OAuthResourceServerDefinition();
		def.setName(name);
		def.setDescription(description);
		def.setDisplayName(displayName);
		return def;
	}

	@Override
	public MetaOAuthResourceServer copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public OAuthResourceServerRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new OAuthResourceServerRuntime();
	}

	public class OAuthResourceServerRuntime extends BaseMetaDataRuntime {
		private OAuthClientCredentialHandler ch = (OAuthClientCredentialHandler) ServiceRegistry.getRegistry().getService(AuthTokenService.class).getHandler(OAuthClientCredentialHandler.TYPE_RESOURCE_SERVER);
		
		private OAuthResourceServerRuntime() {
		}
		
		public MetaOAuthResourceServer getMetaData() {
			return MetaOAuthResourceServer.this;
		}
		
		public Credential generateCredential() {
			return ch.generateCredential(getName());
		}
		
		public boolean validateCredential(Credential cre) {
			return ch.validateCredential(cre, getName());
		}
		
		public void deleteOldCredential() {
			ch.deleteOldCredential(getName());
		}
	}

}
