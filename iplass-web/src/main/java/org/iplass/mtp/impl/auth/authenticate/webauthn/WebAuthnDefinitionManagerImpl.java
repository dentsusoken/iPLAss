/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.webauthn;

import org.iplass.mtp.auth.webauthn.definition.WebAuthnDefinition;
import org.iplass.mtp.auth.webauthn.definition.WebAuthnDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.spi.ServiceRegistry;

public class WebAuthnDefinitionManagerImpl extends AbstractTypedDefinitionManager<WebAuthnDefinition> implements WebAuthnDefinitionManager {

	private WebAuthnService webAuthnService = ServiceRegistry.getRegistry().getService(WebAuthnService.class);

	@Override
	public Class<WebAuthnDefinition> getDefinitionType() {
		return WebAuthnDefinition.class;
	}

	@Override
	protected RootMetaData newInstance(WebAuthnDefinition definition) {
		return new MetaWebAuthn();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return webAuthnService;
	}

}
