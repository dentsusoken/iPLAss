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

import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.oauth.definition.ClientType;
import org.iplass.mtp.auth.oauth.definition.ConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.AlwaysConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.OnceConsentTypeDefinition;
import org.iplass.mtp.auth.oauth.definition.consents.ScriptingConsentTypeDefinition;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.oauth.consents.MetaAlwaysConsentType;
import org.iplass.mtp.impl.auth.oauth.consents.MetaScriptingConsentType;
import org.iplass.mtp.impl.auth.oauth.consents.MetaOnceConsentType;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

@XmlSeeAlso({
	MetaAlwaysConsentType.class,
	MetaOnceConsentType.class,
	MetaScriptingConsentType.class
})
public abstract class MetaConsentType implements MetaData {
	private static final long serialVersionUID = -1385773019313952621L;

	@Override
	public MetaConsentType copy() {
		return ObjectUtil.deepCopy(this);
	}

	public abstract void applyConfig(ConsentTypeDefinition consentType);
	public abstract ConsentTypeDefinition currentConfig();
	public abstract ConsentTypeRuntime createRuntime(String metaId, ClientType ct);
	
	public static abstract class ConsentTypeRuntime {
		
		public abstract boolean needConsent(RequestContext request, List<String> scopes, AccessToken currentToken);
	}

	public static MetaConsentType createInstance(ConsentTypeDefinition consentType) {
		if (consentType instanceof AlwaysConsentTypeDefinition) {
			return new MetaAlwaysConsentType();
		}
		if (consentType instanceof OnceConsentTypeDefinition) {
			return new MetaOnceConsentType();
		}
		if (consentType instanceof ScriptingConsentTypeDefinition) {
			return new MetaScriptingConsentType();
		}
		return null;
	}
	
}
