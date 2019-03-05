/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.metadata.typeconversion;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.metadata.MetaDataStore;
import org.iplass.mtp.spi.Config;

public class TypeConversionMetaDataStore implements MetaDataStore {
	
	private MetaDataStore store;
	private List<TypeConverter> converters;

	public MetaDataStore getStore() {
		return store;
	}

	public void setStore(MetaDataStore store) {
		this.store = store;
	}

	public List<TypeConverter> getConverters() {
		return converters;
	}

	public void setConverters(List<TypeConverter> converters) {
		this.converters = converters;
	}

	@Override
	public void inited(MetaDataRepository service, Config config) {
		store.inited(service, config);
	}

	@Override
	public void destroyed() {
		store.destroyed();
	}

	@Override
	public MetaDataEntry loadById(int tenantId, String id) {
		MetaDataEntry entry = store.loadById(tenantId, id);
		if (entry != null && converters != null) {
			for (TypeConverter c: converters) {
				if (c.isConvertTarget(entry)) {
					c.convert(entry);
					break;
				}
			}
		}
		return entry;
	}

	@Override
	public MetaDataEntry loadById(int tenantId, String id, int version) {
		MetaDataEntry entry = store.loadById(tenantId, id, version);
		if (entry != null && converters != null) {
			for (TypeConverter c: converters) {
				if (c.isConvertTarget(entry)) {
					c.convert(entry);
					break;
				}
			}
		}
		return entry;
	}
	
	//TODO prefixPathが/の場合の考慮
	//MetaDataExplorerでExportしてみたら変換されない？

	@Override
	public List<MetaDataEntryInfo> definitionList(int tenantId, String prefixPath) throws MetaDataRuntimeException {
		List<MetaDataEntryInfo> list = store.definitionList(tenantId, prefixPath);
		if (converters != null) {
			List<MetaDataEntryInfo> newList = null;
			for (TypeConverter c: converters) {
				if (c.hasFallbackPath(prefixPath)) {
					String fallbackPath = c.fallbackPath(prefixPath);
					List<MetaDataEntryInfo> fallbackList = store.definitionList(tenantId, fallbackPath);
					if (fallbackList.size() > 0) {
						if (newList == null) {
							newList = new ArrayList<>(list);
						}
						newList.addAll(fallbackList);
					}
				}
			}
			if (newList != null) {
				list = newList;
			}
			
			for (MetaDataEntryInfo mi: list) {
				for (TypeConverter c: converters) {
					if (c.isConvertTarget(mi)) {
						c.convert(mi);
						break;
					}
				}
			}
		}
		
		return list;
	}

	@Override
	public MetaDataEntry load(int tenantId, String path) throws MetaDataRuntimeException {

		MetaDataEntry entry = store.load(tenantId, path);
		if (path !=null && entry == null && converters != null) {
			for (TypeConverter c: converters) {
				if (c.hasFallbackPath(path)) {
					String fallbackPath = c.fallbackPath(path);
					MetaDataEntry fallbackEntry = store.load(tenantId, fallbackPath);
					if (fallbackEntry != null) {
						fallbackEntry.setPath(path);
						entry = fallbackEntry;
						break;
					}
				}
			}
		}
		
		if (entry != null && converters != null) {
			for (TypeConverter c: converters) {
				if (c.isConvertTarget(entry)) {
					c.convert(entry);
					break;
				}
			}
		}
		return entry;
	}

	@Override
	public MetaDataEntry load(int tenantId, String path, int version) throws MetaDataRuntimeException {
		MetaDataEntry entry = store.load(tenantId, path, version);
		if (path !=null && entry == null && converters != null) {
			for (TypeConverter c: converters) {
				if (c.hasFallbackPath(path)) {
					String fallbackPath = c.fallbackPath(path);
					MetaDataEntry fallbackEntry = store.load(tenantId, fallbackPath, version);
					if (fallbackEntry != null) {
						fallbackEntry.setPath(path);
						entry = fallbackEntry;
						break;
					}
				}
			}
		}
		
		if (entry != null && converters != null) {
			for (TypeConverter c: converters) {
				if (c.isConvertTarget(entry)) {
					c.convert(entry);
					break;
				}
			}
		}
		return entry;
	}

	@Override
	public void store(int tenantId, MetaDataEntry metaDataEntry) throws MetaDataRuntimeException {
		store.store(tenantId, metaDataEntry);
	}

	@Override
	public void update(int tenantId, MetaDataEntry metaDataEntry) throws MetaDataRuntimeException {
		store.update(tenantId, metaDataEntry);
	}

	@Override
	public void remove(int tenantId, String path) throws MetaDataRuntimeException {
		boolean removeFallback = false;
		if (path !=null && converters != null) {
			for (TypeConverter c: converters) {
				if (c.hasFallbackPath(path)) {
					String fallbackPath = c.fallbackPath(path);
					MetaDataEntry fallbackEntry = store.load(tenantId, fallbackPath);
					if (fallbackEntry != null) {
						store.remove(tenantId, fallbackPath);
						removeFallback = true;
						break;
					}
				}
			}
		}
		
		if (removeFallback && store.load(tenantId, path) == null) {
			return;
		}
		
		store.remove(tenantId, path);
	}

	@Override
	public void updateConfigById(int tenantId, String id, MetaDataConfig config) {
		store.updateConfigById(tenantId, id, config);
	}

	@Override
	public List<MetaDataEntryInfo> getHistoryById(int tenantId, String id) {
		List<MetaDataEntryInfo> list = store.getHistoryById(tenantId, id);
		for (MetaDataEntryInfo mi: list) {
			for (TypeConverter c: converters) {
				if (c.isConvertTarget(mi)) {
					c.convert(mi);
					break;
				}
			}
		}
		
		return list;
	}

}
