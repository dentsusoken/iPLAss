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


import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.mtp.impl.datastore.MetaPropertyStore;
import org.iplass.mtp.impl.datastore.PropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;


public class MetaGRdbMultiplePropertyStore extends MetaPropertyStore {
	private static final long serialVersionUID = -1703768839712299060L;

	private List<MetaGRdbPropertyStore> store;

	public MetaGRdbMultiplePropertyStore() {
	}

	public MetaGRdbMultiplePropertyStore(List<MetaGRdbPropertyStore> store) {
		this.store = store;
	}

	public List<MetaGRdbPropertyStore> getStore() {
		return store;
	}

	public void setStore(List<MetaGRdbPropertyStore> store) {
		this.store = store;
	}

	public void add(MetaGRdbPropertyStore propStore) {
		if (store == null) {
			store = new ArrayList<>();
		}
		store.add(propStore);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((store == null) ? 0 : store.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaGRdbMultiplePropertyStore other = (MetaGRdbMultiplePropertyStore) obj;
		if (store == null) {
			if (other.store != null)
				return false;
		} else if (!store.equals(other.store))
			return false;
		return true;
	}

	@Override
	public MetaGRdbMultiplePropertyStore copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public GRdbMultiplePropertyStoreHandler createRuntime(PropertyHandler property, MetaEntity metaEntity) {
		return new GRdbMultiplePropertyStoreHandler(property, metaEntity);
	}

	public class GRdbMultiplePropertyStoreHandler extends PropertyStoreHandler implements GRdbPropertyStoreRuntime {

		private BaseRdbTypeAdapter typeMapping;
		private RdbAdapter rdb;
		private PrimitivePropertyHandler propertyRuntime;
		private List<GRdbPropertyStoreHandler> list;

		private GRdbMultiplePropertyStoreHandler(PropertyHandler propertyRuntime, MetaEntity metaEntity) {
			//Primitive型しかないはず
			this.propertyRuntime = (PrimitivePropertyHandler) propertyRuntime;
			rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
			PropertyType type = this.propertyRuntime.getMetaData().getType();
			typeMapping = rdb.getRdbTypeAdapter(type);
			if (store == null) {
				list = Collections.emptyList();
			} else {
				list = new ArrayList<>(store.size());
				for (int i = 0; i < propertyRuntime.getMetaData().getMultiplicity(); i++) {
					list.add(new GRdbPropertyStoreHandler(propertyRuntime, store.get(i), metaEntity));
				}
			}
		}

		@Override
		public BaseRdbTypeAdapter getSingleColumnRdbTypeAdapter() {
			return typeMapping;
		}

		@Override
		public int getColCount() {
			return list.size();
		}

		@Override
		public boolean isNative() {
			if (store == null || store.size() == 0) {
				return false;
			} else {
				return store.get(0).isNative();
			}
		}

		@Override
		public boolean isExternalIndex() {
			if (store == null || store.size() == 0) {
				return false;
			} else {
				return store.get(0).isExternalIndex();
			}
		}

		@Override
		public PrimitivePropertyHandler getPropertyRuntime() {
			return propertyRuntime;
		}

		@Override
		public Object fromDataStore(ResultSet rs, int colNum) throws SQLException {
			//配列型
			ArrayList<Object> vals = new ArrayList<Object>(store.size());
			int nullCount = 0;
			for (int i = 0; i < propertyRuntime.getMetaData().getMultiplicity(); i++) {
				Object val = getSingleColumnRdbTypeAdapter().fromDataStore(rs, colNum + i, rdb);
				if (val == null) {
					nullCount++;
				} else {
					if (nullCount > 0) {
						for (int j = 0; j < nullCount; j++) {
							vals.add(null);
						}
						nullCount = 0;
					}
					vals.add(val);
				}
			}
			if (vals.size() > 0) {
				return vals.toArray((Object[]) Array.newInstance(propertyRuntime.getMetaData().getType().storeType(), vals.size()));
			} else {
				//all null
				return null;
			}
		}

		@Override
		public MetaGRdbMultiplePropertyStore getMetaData() {
			return MetaGRdbMultiplePropertyStore.this;
		}

		@Override
		public boolean isMulti() {
			return true;
		}

		@Override
		public List<GRdbPropertyStoreHandler> asList() {
			return list;
		}
	}
}
