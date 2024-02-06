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
package org.iplass.mtp.impl.auth.authenticate.token;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class AuthTokenService implements Service {
	public static final String DEFAULT_STORE_NAME = "default";

	private Map<String, AuthTokenStore> storeMap;
	private AuthTokenStore defaultStore;
	
	private Map<String, AuthTokenHandler> handlerMap;
	
	public Collection<AuthTokenStore> getStores() {
		return storeMap.values();
	}
	
	public AuthTokenStore getStore(String type) {
		AuthTokenStore store = storeMap.get(type);
		if (store == null) {
			store = defaultStore;
		}
		return store;
	}
	
	public AuthTokenHandler getHandler(String type) {
		return handlerMap.get(type);
	}
	
	public Collection<AuthTokenHandler> getHandlers() {
		return handlerMap.values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		storeMap = config.getValue("storeMap", Map.class);
		defaultStore = storeMap.get(DEFAULT_STORE_NAME);
		
		handlerMap = new HashMap<>();
		List<AuthTokenHandler> handlers = config.getValues("handler", AuthTokenHandler.class);
		if (handlers != null) {
			for (AuthTokenHandler ah: handlers) {
				handlerMap.put(ah.getType(), ah);
			}
		}
	}

	@Override
	public void destroy() {
	}

}
