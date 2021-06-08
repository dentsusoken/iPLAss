/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.grdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.definition.stores.SchemalessRdbStore;
import org.iplass.mtp.impl.counter.CounterService;
import org.iplass.mtp.impl.datastore.MetaEntityStore;
import org.iplass.mtp.impl.datastore.RdbDataStore;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.grdb.strategy.GRdbApplyMetaDataStrategy;
import org.iplass.mtp.impl.datastore.grdb.strategy.GRdbEntityStoreStrategy;
import org.iplass.mtp.impl.datastore.strategy.ApplyMetaDataStrategy;
import org.iplass.mtp.impl.datastore.strategy.EntityStoreStrategy;
import org.iplass.mtp.impl.entity.MetaSchemalessRdbStoreMapping;
import org.iplass.mtp.impl.entity.MetaStoreMapping;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GRdbDataStore extends RdbDataStore {
	
	private static Logger logger = LoggerFactory.getLogger(GRdbDataStore.class);
	
	private GRdbEntityStoreStrategy entityStore;
	private GRdbApplyMetaDataStrategy applyMeta;
	private Map<String, StorageSpaceMap> storageSpaceMap;
	
	private List<StorageSpaceMap> storageSpace;
	private boolean enableWindowFunctionEmulation;
	
	private Integer stringTypeLengthOnQuery;
	
	public GRdbDataStore() {
	}
	
	public Map<String, StorageSpaceMap> getStorageSpaceMap() {
		return storageSpaceMap;
	}
	public List<StorageSpaceMap> getStorageSpace() {
		return storageSpace;
	}
	public void setStorageSpace(List<StorageSpaceMap> storageSpace) {
		this.storageSpace = storageSpace;
	}
	
	@Override
	public ApplyMetaDataStrategy getApplyMetaDataStrategy() {
		return applyMeta;
	}

	@Override
	public EntityStoreStrategy getEntityStoreStrategy() {
		return entityStore;
	}

	public boolean isEnableWindowFunctionEmulation() {
		return enableWindowFunctionEmulation;
	}

	public void setEnableWindowFunctionEmulation(
			boolean enableWindowFunctionEmulation) {
		this.enableWindowFunctionEmulation = enableWindowFunctionEmulation;
	}

	public Integer getStringTypeLengthOnQuery() {
		return stringTypeLengthOnQuery;
	}

	public void setStringTypeLengthOnQuery(Integer stringTypeLengthOnQuery) {
		this.stringTypeLengthOnQuery = stringTypeLengthOnQuery;
	}

	@Override
	public void inited(StoreService service, Config config) {
		storageSpaceMap = new HashMap<>();
		if (storageSpace != null) {
			for (StorageSpaceMap m: storageSpace) {
				storageSpaceMap.put(m.getStorageSpaceName(), m);
			}
		}

		CounterService cs = config.getDependentService(CounterService.OID_COUNTER_SERVICE_NAME);
		RdbAdapter rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();
		
		entityStore = new GRdbEntityStoreStrategy(this, rdb, cs);
		applyMeta = new GRdbApplyMetaDataStrategy(this, rdb);
	}

	@Override
	public void destroyed() {
	}

	@Override
	public MetaEntityStore newEntityStoreInstance() {
		return new MetaGRdbEntityStore();
	}

	@Override
	public Class<? extends MetaEntityStore> getEntityStoreType() {
		return MetaGRdbEntityStore.class;
	}


	@Override
	public List<String> getStorageSpaceList() {
		if (storageSpaceMap != null) {
			return new ArrayList<>(storageSpaceMap.keySet());
		} else {
			return Collections.emptyList();
		}
	}
	
	public StorageSpaceMap getStorageSpaceMapOrDefault(String name) {
		StorageSpaceMap ret = storageSpaceMap.get(name);
		if (ret == null) {
			if (name != null) {
				logger.warn("not define storageSpace:" + name + ", so use storageSpace of 'default'.");
			}
			ret = storageSpaceMap.get(SchemalessRdbStore.DEFAULT_STORAGE_SPACE);
		}
		return ret;
	}

	public StorageSpaceMap getStorageSpaceMapOrDefault(MetaSchemalessRdbStoreMapping metaMapping) {
		String name = null;
		if (metaMapping != null) {
			name = metaMapping.getStorageSpace();
		}
		return getStorageSpaceMapOrDefault(name);
	}

	@Override
	public int stringPropertyStoreMaxLength(MetaStoreMapping metaStoreMapping) {
		StorageSpaceMap ssm = getStorageSpaceMapOrDefault((MetaSchemalessRdbStoreMapping) metaStoreMapping);
		return ssm.getVarcharColumnLength();
	}
}
