/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.lob;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.impl.counter.CounterService;
import org.iplass.mtp.impl.lob.lobstore.LobStore;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class LobStoreService implements Service {

	private static final String DEFAULT_LOB_STORE_CONFIG_NAME = "defaultLobStoreName";

	private HashMap<String, LobStore> stores;
	private String defaultLobStoreName = "default";
	
	private LobDao lobDao;

	/** LobのサイズをDB(lob_store)で管理するかを指定します */
	private boolean manageLobSizeOnRdb = true;
	
	public LobDao getLobDao() {
		return lobDao;
	}

	public LobStore getLobStore(String name) {
		LobStore s = stores.get(name);
		if (s == null) {
			s = getDefaultLobStore();
		}
		return s;
	}

	public LobStore getDefaultLobStore() {
		return stores.get(defaultLobStoreName);
	}

	public Map<String, LobStore> getLobStoreMap() {
		return new HashMap<String, LobStore>(stores);
	}

	public boolean isManageLobSizeOnRdb() {
		return manageLobSizeOnRdb;
	}

	@Override
	public void init(Config config) {
		if (config.getValue(DEFAULT_LOB_STORE_CONFIG_NAME) != null) {
			defaultLobStoreName = config.getValue(DEFAULT_LOB_STORE_CONFIG_NAME);
		}
		stores = new HashMap<String, LobStore>();
		for (String propName: config.getNames()) {
			Object val = config.getBean(propName);
			if (val instanceof LobStore) {
				stores.put(propName, (LobStore) val);
			}
		}

		if (config.getValue("manageLobSizeOnRdb") != null) {
			manageLobSizeOnRdb = Boolean.valueOf(config.getValue("manageLobSizeOnRdb"));
		}
		
		lobDao = config.getValue("lobDao", LobDao.class);
		if (lobDao == null) {
			lobDao = new LobDao();
		}
		lobDao.init(config.getDependentService(RdbAdapterService.class).getRdbAdapter(),
				config.getDependentService(CounterService.OID_COUNTER_SERVICE_NAME));
	}

	@Override
	public void destroy() {
	}

}
