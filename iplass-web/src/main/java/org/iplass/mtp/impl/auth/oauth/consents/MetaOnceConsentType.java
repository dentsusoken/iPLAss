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
package org.iplass.mtp.impl.auth.oauth.consents;

import java.util.List;

import org.iplass.mtp.auth.oauth.definition.ClientType;
import org.iplass.mtp.auth.oauth.definition.ConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.OnceConsentTypeDefinition;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.oauth.MetaConsentType;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;

/**
 * 一度承認済みのscopeに関しては承認画面を出さないMetaConsentType。
 * ただし、scopeにoffline_accessを含む場合（RefreshTokenの発行）は、承認画面を表示する。
 * 
 * @author K.Higuchi
 *
 */
public class MetaOnceConsentType extends MetaConsentType {
	private static final long serialVersionUID = -4737503827113921073L;

	@Override
	public OnceConsentTypeRuntime createRuntime(String metaId, ClientType ct) {
		return new OnceConsentTypeRuntime();
	}
	
	public class OnceConsentTypeRuntime extends ConsentTypeRuntime {

		@Override
		public boolean needConsent(RequestContext request, List<String> scopes, AccessToken currentToken) {
			if (currentToken == null || currentToken.getGrantedScopes() == null) {
				return true;
			}
			
			//offline_accessを含む場合は、承認画面を出す
			if (scopes.contains(OAuthConstants.SCOPE_OFFLINE_ACCESS)) {
				return true;
			}
			
			return currentToken.getGrantedScopes().containsAll(scopes) == false;
		}
	}

	@Override
	public void applyConfig(ConsentTypeDefinition consentType) {
	}

	@Override
	public ConsentTypeDefinition currentConfig() {
		return new OnceConsentTypeDefinition();
	}

}
