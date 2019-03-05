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
package org.iplass.mtp.impl.auth.oauth.vh;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaScope;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.auth.oauth.OAuthClientService;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationRequest;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.spi.ServiceRegistry;

public class ConsentViewHelper {
	
	private static OAuthAuthorizationService authorizationService = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
	private static OAuthClientService clientService = ServiceRegistry.getRegistry().getService(OAuthClientService.class);
	
	private AuthorizationRequest authRequest;
	private OAuthAuthorizationRuntime server;
	private OAuthClientRuntime client;
	private List<ScopeItem> scopes;
	
	public ConsentViewHelper(AuthorizationRequest authRequest) {
		this.authRequest = authRequest;
		server = authorizationService.getRuntimeByName(authRequest.getAuthorizationServerId());
		client = clientService.getRuntimeByName(authRequest.getClientId());
		scopes = new ArrayList<>();
		
		List<MetaScope> msList = server.getScopeByName(authRequest.getScopes());
		for (MetaScope ms: msList) {
			scopes.add(new ScopeItem(ms));
		}
	}
	
	public String getClientName() {
		String name = I18nUtil.stringMeta(client.getMetaData().getDisplayName(), client.getMetaData().getLocalizedDisplayNameList());
		if (name == null) {
			name = client.getMetaData().getName();
		}
		return name;
	}
	
	public String getClientUri() {
		return client.getMetaData().getClientUri();
	}
	
	public String getLogoUri() {
		return client.getMetaData().getLogoUri();
	}
	
	public List<String> getContacts() {
		return client.getMetaData().getContacts();
	}
	
	public String getTosUri() {
		return client.getMetaData().getTosUri();
	}
	
	public String getPolicyUri() {
		return client.getMetaData().getPolicyUri();
	}
	
	public String getRequestId() {
		return authRequest.getRequestId();
	}
	
	public List<ScopeItem> getScopes() {
		return scopes;
	}
	
	public static class ScopeItem {
		private MetaScope scope;
		public ScopeItem(MetaScope scope) {
			this.scope = scope;
		}
		
		public String getName() {
			String str = I18nUtil.stringMeta(scope.getDisplayName(), scope.getLocalizedDisplayNameList());
			if (str == null) {
				str = scope.getName();
			}
			return str;
		}
		
		public String getDescription() {
			return I18nUtil.stringMeta(scope.getDescription(), scope.getLocalizedDescriptionList());
		}
	}
}
