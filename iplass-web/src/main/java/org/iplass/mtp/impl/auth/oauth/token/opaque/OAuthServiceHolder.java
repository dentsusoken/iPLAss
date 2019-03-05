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
package org.iplass.mtp.impl.auth.oauth.token.opaque;

import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.UserEntityResolver;
import org.iplass.mtp.impl.auth.oauth.AccessTokenAuthenticationProvider;
import org.iplass.mtp.impl.auth.oauth.AccessTokenAuthenticationProvider.AccessTokenUserEntityResolver;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.auth.oauth.OAuthClientService;
import org.iplass.mtp.spi.ServiceRegistry;

class OAuthServiceHolder {
	static OAuthClientService client = ServiceRegistry.getRegistry().getService(OAuthClientService.class);
	static OAuthAuthorizationService authorization = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
	
	static UserEntityResolver userEntityResolver;
	static {
		AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
		for (AuthenticationProvider ap: as.getAuthenticationProviders()) {
			if (ap instanceof AccessTokenAuthenticationProvider) {
				userEntityResolver = ((AccessTokenUserEntityResolver) ap.getUserEntityResolver()).getActual();
				break;
			}
		}
	}
	
}
