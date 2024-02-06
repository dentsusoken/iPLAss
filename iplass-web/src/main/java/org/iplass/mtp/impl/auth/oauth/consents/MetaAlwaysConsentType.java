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
package org.iplass.mtp.impl.auth.oauth.consents;

import java.util.List;

import org.iplass.mtp.auth.oauth.definition.ClientType;
import org.iplass.mtp.auth.oauth.definition.ConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.AlwaysConsentTypeDefinition;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.oauth.MetaConsentType;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;

/**
 * 毎回承認画面を表示するConsentType。
 * 
 * @author K.Higuchi
 *
 */
public class MetaAlwaysConsentType extends MetaConsentType {
	private static final long serialVersionUID = 1300348800806303632L;

	@Override
	public AlwaysConsentTypeRuntime createRuntime(String metaId, ClientType ct) {
		return new AlwaysConsentTypeRuntime();
	}
	
	public class AlwaysConsentTypeRuntime extends ConsentTypeRuntime {

		@Override
		public boolean needConsent(RequestContext request, List<String> scopes, AccessToken currentToken) {
			return true;
		}
	}

	@Override
	public void applyConfig(ConsentTypeDefinition consentType) {
	}

	@Override
	public ConsentTypeDefinition currentConfig() {
		return new AlwaysConsentTypeDefinition();
	}

}
