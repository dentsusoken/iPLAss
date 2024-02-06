/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore;

import java.util.Collections;
import java.util.List;

import org.iplass.mtp.entity.definition.StoreDefinition;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;


public class StoreService implements Service {

	private DataStore dataStore;

	public StoreService() {
	}
	
	public DataStore getDataStore() {
		return dataStore;
	}
	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	public List<String> getStorageSpaceList() {
		if (dataStore instanceof RdbDataStore) {
			return ((RdbDataStore) dataStore).getStorageSpaceList();
		} else {
			return Collections.emptyList();
		}
	}

	public MetaEntityStore newEntityStoreInstance(StoreDefinition storeDefinition) {
		return dataStore.newEntityStoreInstance();
	}
	
	public Class<? extends MetaEntityStore> getEntityStoreType(StoreDefinition storeDefinition) {
		return dataStore.getEntityStoreType();
	}

	public String getCharset() {
		return "UTF-8";
	}

	public void destroy() {
	}

	public void init(Config config) {
		dataStore = (DataStore) config.getBean("dataStore");
	}

}
