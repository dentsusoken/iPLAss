/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.composite.CompositeMetaDataStore;
import org.iplass.mtp.impl.metadata.rdb.RdbMetaDataStore;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MetaDataRepository implements Service {

	private static Logger logger = LoggerFactory.getLogger(MetaDataRepository.class);

	//TODO インタフェースは再検討
	//TODO バージョニング
	//TODO ロック（ステータスの変更）

	//TODO XMLTypeの使用

	//TODO サブディレクトリを定義できるようにして、サブディレクトリ単位で実リポジトリを切り替え

	private MetaDataStore tenantLocalStore;
	private List<MetaDataStore> sharedStore;//indexが0に近い方が優先
//	private int sharedTenantId = -1;
	
	//TODO 古いMetaDataの変換アダプター

	public MetaDataStore getTenantLocalStore() {
		return tenantLocalStore;
	}

	public List<MetaDataStore> getSharedStore() {
		return sharedStore;
	}

	public MetaDataEntry loadById(int tenantId, String id, boolean withShared) {
		MetaDataEntry ent = tenantLocalStore.loadById(tenantId, id);
		if (ent != null) {
			ent.setRepositryType(RepositoryType.TENANT_LOCAL);
			return ent;
		}

		if (sharedStore != null && withShared) {
			for (int i = 0; i < sharedStore.size(); i++) {
				ent = sharedStore.get(i).loadById(tenantId, id);
				if (ent != null && ent.isSharable()) {
					if(ent.isOverwritable()){
						ent.setRepositryType(RepositoryType.TENANT_LOCAL);
					}else{
						ent.setRepositryType(RepositoryType.SHARED);
					}
					return ent;
				}
			}
		}
		return null;
	}

	public MetaDataEntry loadById(int tenantId, String id, int version, boolean withShared) {
		MetaDataEntry ent = tenantLocalStore.loadById(tenantId, id, version);
		if (ent != null) {
			ent.setRepositryType(RepositoryType.TENANT_LOCAL);
			return ent;
		}

		if (sharedStore != null && withShared) {
			for (int i = 0; i < sharedStore.size(); i++) {
				ent = sharedStore.get(i).loadById(tenantId, id, version);
				if (ent != null && ent.isSharable()) {
					if(ent.isOverwritable()){
						ent.setRepositryType(RepositoryType.TENANT_LOCAL);
					}else{
						ent.setRepositryType(RepositoryType.SHARED);
					}
					return ent;
				}
			}
		}
		return null;
	}

	public List<MetaDataEntryInfo> definitionList(final int tenantId, final String prefixPath, boolean withShared)
			throws MetaDataRuntimeException {
		HashMap<String, MetaDataEntryInfo> map = new HashMap<String, MetaDataEntryInfo>();
		//indexが0の方が優先、local定義が一番優先
		if (sharedStore != null && withShared) {
			for (int i = sharedStore.size() - 1; i > -1; i--) {
				List<MetaDataEntryInfo> list = sharedStore.get(i).definitionList(tenantId, prefixPath);
				for (MetaDataEntryInfo definition : list) {
					if (definition.isSharable()) {
						if(definition.isOverwritable()){
							definition.setRepositryType(RepositoryType.TENANT_LOCAL);
						}else{
							definition.setRepositryType(RepositoryType.SHARED);
						}
						map.put(definition.getPath(), definition);
					}
				}
			}
		}
		List<MetaDataEntryInfo> list = tenantLocalStore.definitionList(tenantId, prefixPath);
		for (MetaDataEntryInfo definition : list) {
			definition.setRepositryType(RepositoryType.TENANT_LOCAL);
			map.put(definition.getPath(), definition);
		}

		List<MetaDataEntryInfo> res = new ArrayList<MetaDataEntryInfo>(map.values());

//		//ソート
//		Collections.sort(res, new Comparator<MetaDataEntryInfo>() {
//			@Override
//			public int compare(MetaDataEntryInfo o1, MetaDataEntryInfo o2) {
//				return o1.getPath().toLowerCase().compareTo(o2.getPath().toLowerCase());
//			}
//		});
		return res;
	}

	@Override
	public void destroy() {
		tenantLocalStore = null;
		sharedStore = null;
//		sharedTenantId = -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {

		tenantLocalStore = (MetaDataStore) config.getBean("tenantLocalStore");
		sharedStore = (List<MetaDataStore>) config.getBeans("sharedStore");
//		TenantContextService tcService = config.getDependentService(TenantContextService.class);
//		sharedTenantId = tcService.getSharedTenantId();
	}

	public MetaDataEntry load(int tenantId, String path, boolean withShared)
			throws MetaDataRuntimeException {
		MetaDataEntry ent = tenantLocalStore.load(tenantId, path);
		if (ent != null) {
			ent.setRepositryType(RepositoryType.TENANT_LOCAL);
			return ent;
		}

		if (sharedStore != null && withShared) {
			for (int i = 0; i < sharedStore.size(); i++) {
				ent = sharedStore.get(i).load(tenantId, path);
				if (ent != null && ent.isSharable()) {
					if (ent.getMetaData() == null) {
						throw new SystemException(tenantId + "'s " + path + " MetaData is Incompatible or not defined.");
					}
					if(ent.isOverwritable()){
						ent.setRepositryType(RepositoryType.TENANT_LOCAL);
					}else{
						ent.setRepositryType(RepositoryType.SHARED);
					}
					return ent;
				}
			}
		}
		return null;
	}

	public MetaDataEntry load(int tenantId, String path, int version, boolean withShared)
			throws MetaDataRuntimeException {
		MetaDataEntry ent = tenantLocalStore.load(tenantId, path, version);
		if (ent != null) {
			ent.setRepositryType(RepositoryType.TENANT_LOCAL);
			return ent;
		}

		if (sharedStore != null && withShared) {
			for (int i = 0; i < sharedStore.size(); i++) {
				ent = sharedStore.get(i).load(tenantId, path, version);
				if (ent != null && ent.isSharable()) {
					if (ent.getMetaData() == null) {
						throw new SystemException(tenantId + "'s " + path + " MetaData is Incompatible or not defined.");
					}
					if(ent.isOverwritable()){
						ent.setRepositryType(RepositoryType.TENANT_LOCAL);
					}else{
						ent.setRepositryType(RepositoryType.SHARED);
					}
					return ent;
				}
			}
		}
		return null;
	}

	public void store(int tenantId, MetaDataEntry metaDataEntry)
		throws MetaDataRuntimeException {
		tenantLocalStore.store(tenantId, metaDataEntry);
		logger.info("store MetaData:" + metaDataEntry.getPath());
	}

	public void update(int tenantId, MetaDataEntry metaDataEntry)
			throws MetaDataRuntimeException {
		tenantLocalStore.update(tenantId, metaDataEntry);

		logger.info("update MetaData:" + metaDataEntry.getPath());
	}

	public void remove(int tenantId, String path) throws MetaDataRuntimeException {
		tenantLocalStore.remove(tenantId, path);
		logger.info("remove MetaData:" + path);
	}

	public void updateConfigById(int tenantId, String id, MetaDataConfig config) {
		tenantLocalStore.updateConfigById(tenantId, id, config);

		logger.info("update MetaData config of id:" + id);
	}

	public boolean hasOverwriteMetaData(int sharedTenantId, MetaDataEntry entry) {
		//TODO インタフェース要検討
		if (tenantLocalStore instanceof RdbMetaDataStore) {
			return ((RdbMetaDataStore) tenantLocalStore).hasOverwriteMetaData(sharedTenantId, entry.getMetaData().getId());
		} else if(tenantLocalStore instanceof CompositeMetaDataStore) {
			return ((CompositeMetaDataStore) tenantLocalStore).hasOverwriteMetaData(sharedTenantId, entry);
		} else {
			return false;
		}
	}

	public List<Integer> getOverwriteTenantIdList(int sharedTenantId, String metaDataId) {
		//TODO インタフェース要検討
		if (tenantLocalStore instanceof RdbMetaDataStore) {
			return ((RdbMetaDataStore) tenantLocalStore).getOverwriteTenantIdList(sharedTenantId, metaDataId);
		} else if(tenantLocalStore instanceof CompositeMetaDataStore) {
			return ((CompositeMetaDataStore) tenantLocalStore).getOverwriteTenantIdList(sharedTenantId, metaDataId);
		} else {
			return new ArrayList<Integer>();
		}
	}

	public List<MetaDataEntryInfo> getInvalidEntryList(final int tenantId) {
		if (getTenantLocalStore() instanceof RdbMetaDataStore) {
			return ((RdbMetaDataStore)getTenantLocalStore()).getInvalidEntryList(tenantId);
		} else if(getTenantLocalStore() instanceof CompositeMetaDataStore) {
			return ((CompositeMetaDataStore) getTenantLocalStore()).getInvalidEntryList(tenantId);
		}
		return Collections.emptyList();
	}

	public void purgeById(int tenantId, String id) throws MetaDataRuntimeException {
		if (getTenantLocalStore() instanceof RdbMetaDataStore) {
			((RdbMetaDataStore)getTenantLocalStore()).purgeById(tenantId, id);
		} else if(getTenantLocalStore() instanceof CompositeMetaDataStore) {
			((CompositeMetaDataStore) getTenantLocalStore()).purgeById(tenantId, id);
		}
	}

	public List<MetaDataEntryInfo> getHistoryById(final int tenantId, final String definitionId)
			throws MetaDataRuntimeException {
		List<MetaDataEntryInfo> list = tenantLocalStore.getHistoryById(tenantId, definitionId);
		return list;
	}

}
