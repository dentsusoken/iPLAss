/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

	/** 強制的なテーブル名接尾辞の再生成（true の場合に再生成する） */
	private boolean forceRegenerateTableNamePostfix;

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

	/**
	 * ストレージスペース名が同一の場合に、強制的なテーブル名接尾辞の再生成を実施するかを取得する。
	 * @return true の場合、ストレージスペース名が同一の場合でも強制的にテーブル名接尾辞を再生成する
	 */
	public boolean isForceRegenerateTableNamePostfix() {
		return forceRegenerateTableNamePostfix;
	}

	/**
	 * ストレージスペース名が同一の場合に、強制的なテーブル名接尾辞の再生成を実施するかを設定する。
	 *
	 * <p>
	 * 本フラグを設定することで、エンティティ定義更新時にテーブルスペース位置（tableNamePostfix）に変更が入る。
	 * ストレージスペース移行を実施する際に設定することを想定しており、業務アプリから設定してはいけない。
	 * </p>
	 *
	 * @see org.iplass.mtp.impl.datastore.grdb.strategy.GRdbApplyMetaDataStrategy#modify(org.iplass.mtp.impl.entity.MetaEntity, org.iplass.mtp.impl.entity.MetaEntity, org.iplass.mtp.impl.entity.EntityContext, int[])
	 * @see org.iplass.mtp.impl.datastore.grdb.strategy.metadata.diff.UpdEntity#modifyMetaData()
	 * @param forceRegenerateTableNamePostfix true の場合、ストレージスペース名が同一の場合でも強制的にテーブル名接尾辞を再生成する
	 */
	public void setForceRegenerateTableNamePostfix(boolean forceRegenerateTableNamePostfix) {
		this.forceRegenerateTableNamePostfix = forceRegenerateTableNamePostfix;
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
