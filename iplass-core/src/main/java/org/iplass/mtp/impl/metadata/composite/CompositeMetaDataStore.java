/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.MetaDataStore;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class CompositeMetaDataStore implements MetaDataStore {

	private MetaDataStorePathMapping[] pathMapping;

	private Map<String, MetaDataStore> container = new HashMap<String, MetaDataStore>();

	private MetaDataStore[] store;

	private MetaDataStore defaultStore;

	private String defaultStoreClass;

	@Override
	public void destroyed() {
	}

	@Override
	public void inited(MetaDataRepository service, Config config) {

		if(store == null) {
			throw new ServiceConfigrationException("CompositeMetaDataStore must be set 'store' property.");
		}

		if(defaultStoreClass == null) {
			throw new ServiceConfigrationException("CompositeMetaDataStore must be set 'defaultStoreClassName' property.");
		}
		for (MetaDataStore ds : store) {
			if (ds.getClass().getName().equals(defaultStoreClass)) {
				defaultStore = ds;
				break;
			}
		}

		if (pathMapping != null) {
			for (MetaDataStorePathMapping m: pathMapping) {
				String pathPrefix = m.getPathPrefix();
				String className = m.getStore();
				MetaDataStore mdstore = null;
				for (MetaDataStore ds : store) {
					if (ds.getClass().getName().equals(className)) {
						mdstore = ds;
						break;
					}
				}
				container.put(pathPrefix, mdstore);
			}
		}
	}

	@Override
	public MetaDataEntry loadById(int tenantId, String id) {
		return loadById(tenantId, id, -1);
	}

	/**
	 * IDベースでのロード.
	 * <p>パスとストアの対応が定義づけられているストアは、そのストアから返却させる.
	 * <p>定義づけられてないストアは、デフォルトストアから返却させる.
	 */
	@Override
	public MetaDataEntry loadById(int tenantId, String id, int version) {
		MetaDataEntry ent = defaultStore.loadById(tenantId, id, version);
		if (ent == null) {
			//デフォルトストアにない
			for (Map.Entry<String, MetaDataStore> e : container.entrySet()) {
				MetaDataStore ds = e.getValue();
				String pathOfStore = e.getKey();
				if(!ds.getClass().isInstance(defaultStore)) {
					ent = ds.loadById(tenantId, id, version);
					// パス:ストアが1:1なのでエンティティパスとマッピングパスが整合していればそれが正しいストア
					if(ent != null && ent.getPath().startsWith(pathOfStore)) {
						break;
					}
				}
			}
		} else {
			//デフォルトにあるが、開発者が削除を忘れているだけかもしれないので、
			//念の為そのパスに対応するストアがあれば引き直す
			String path = ent.getPath();
			MetaDataStore pathMappedStore = resolveStore(path);
			if(!pathMappedStore.getClass().isInstance(defaultStore)) {
				ent = pathMappedStore.loadById(tenantId, id, version);
			}
		}
		return ent;
	}

	@Override
	public List<MetaDataEntryInfo> definitionList(int tenantId, String prefixPath) throws MetaDataRuntimeException {
		return definitionList(tenantId, prefixPath, false);
	}

	@Override
	public List<MetaDataEntryInfo> definitionList(int tenantId, String prefixPath, boolean withInvalid) throws MetaDataRuntimeException {
		if ("/".equals(prefixPath)) {
			List<MetaDataEntryInfo> ret = new ArrayList<MetaDataEntryInfo>();
			for (Map.Entry<String, MetaDataStore> e : container.entrySet()) {
				// 対象pathでdefinition検索
				String path = e.getKey();
				MetaDataStore ds = e.getValue();
				ret.addAll(ds.definitionList(tenantId, path, withInvalid));
			}
			List<MetaDataEntryInfo> defaultList = defaultStore.definitionList(tenantId, prefixPath, withInvalid);
			Iterator<MetaDataEntryInfo> ite = defaultList.iterator();
			while(ite.hasNext()) {
				MetaDataEntryInfo e = ite.next();
				for (String pathPrefix : container.keySet()) {
					if (e.getPath().startsWith(pathPrefix)) {
						//ダブりがあればマージする前に消しておく.
						ite.remove();
					}
				}
			}
			ret.addAll(defaultList);
			return ret;
		} else {
			String key = prefixPath;
			if (key.startsWith("/")) {
				key = key.substring(1);
			}
			key = "/" + key.split("/")[0] + "/";
			MetaDataStore ds = container.get(key);
			if (ds != null) {
				return ds.definitionList(tenantId, prefixPath, withInvalid);
			} else {
				return defaultStore.definitionList(tenantId, prefixPath, withInvalid);
			}
		}
	}

	@Override
	public MetaDataEntry load(int tenantId, String path) throws MetaDataRuntimeException {
		final int version = -1;
		return load(tenantId, path, version);
	}

	@Override
	public MetaDataEntry load(int tenantId, String path, int version) throws MetaDataRuntimeException {
		MetaDataStore ds = resolveStore(path);
		return ds.load(tenantId, path, version);
	}

	@Override
	public void store(int tenantId, MetaDataEntry metaDataEntry) throws MetaDataRuntimeException {
		MetaDataStore ds = resolveStore(metaDataEntry.getPath());
		ds.store(tenantId, metaDataEntry);
	}

	@Override
	public void update(int tenantId, MetaDataEntry metaDataEntry) throws MetaDataRuntimeException {
		MetaDataStore ds = resolveStore(metaDataEntry.getPath());
		ds.update(tenantId, metaDataEntry);
	}

	@Override
	public void remove(int tenantId, String path) throws MetaDataRuntimeException {
		MetaDataStore ds = resolveStore(path);
		ds.remove(tenantId, path);
	}

	@Override
	public void updateConfigById(int tenantId, String id, MetaDataConfig config) {
		MetaDataStore ds = resolveStoreById(tenantId, id);
		ds.updateConfigById(tenantId, id, config);
	}

	@Override
	public List<MetaDataEntryInfo> getHistoryById(int tenantId, String id) {
		MetaDataStore ds = resolveStoreById(tenantId, id);
		return ds.getHistoryById(tenantId, id);
	}

	@Override
	public List<Integer> getTenantIdsOf(String id) {
		HashSet<Integer> set = new HashSet<>();
		for (Map.Entry<String, MetaDataStore> e : container.entrySet()) {
			MetaDataStore ds = e.getValue();
			List<Integer> dsList = ds.getTenantIdsOf(id);
			if (dsList != null) {
				set.addAll(dsList);
			}
		}
		return new ArrayList<>(set);
	}

	public void purgeById(final int tenantId, final String id) throws MetaDataRuntimeException {
		for (Map.Entry<String, MetaDataStore> e : container.entrySet()) {
			MetaDataStore ds = e.getValue();
			ds.purgeById(tenantId, id);
		}
	}

	public MetaDataStore resolveStore(String path) {
		if(!"/".equals(path) && path != null) {
			for(Map.Entry<String, MetaDataStore> e : container.entrySet()) {
				String pathOfStore = e.getKey();

				if(path.startsWith(pathOfStore)) {

					return e.getValue();
				}
			}
			return defaultStore;
		} else {
			return defaultStore;
		}
	}

	public <T extends MetaDataStore> T getStore(Class<T> storeType) {
		MetaDataStore store = null;
		for(Map.Entry<String, MetaDataStore> e : container.entrySet()) {
			store = e.getValue();
			if (storeType.isInstance(store)) {
				return storeType.cast(store);
			}
		}
		if(storeType.isInstance(defaultStore)) {
			return storeType.cast(defaultStore);
		} else {
			return null;
		}
	}

	private MetaDataStore resolveStoreById(int tenantId, String id) {
		for(Map.Entry<String, MetaDataStore> e : container.entrySet()) {
			MetaDataStore ds = e.getValue();
			if (ds.loadById(tenantId, id) != null) {
				return ds;
			}
		}
		return defaultStore;
	}

	public MetaDataStore[] getStore() {
		return store;
	}

	public void setStore(MetaDataStore[] store) {
		this.store = store;
	}

	public String getDefaultStoreClass() {
		return defaultStoreClass;
	}

	public void setDefaultStoreClass(String defaultStoreClass) {
		this.defaultStoreClass = defaultStoreClass;
	}

	public MetaDataStorePathMapping[] getPathMapping() {
		return pathMapping;
	}

	public void setPathMapping(MetaDataStorePathMapping[] pathMapping) {
		this.pathMapping = pathMapping;
	}

}
