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

package org.iplass.mtp.impl.entity;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.StoreDefinition;
import org.iplass.mtp.entity.definition.stores.ColumnMapping;
import org.iplass.mtp.entity.definition.stores.SchemalessRdbStore;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaSchemalessRdbStoreMapping extends MetaStoreMapping {
	private static final long serialVersionUID = 1247940347601034869L;

	private String storageSpace;
	private List<MetaRdbColumnMapping> columnMappingList;

	public String getStorageSpace() {
		return storageSpace;
	}

	public void setStorageSpace(String storageSpace) {
		this.storageSpace = storageSpace;
	}

	public List<MetaRdbColumnMapping> getColumnMappingList() {
		return columnMappingList;
	}

	public void setColumnMappingList(List<MetaRdbColumnMapping> columnMappingList) {
		this.columnMappingList = columnMappingList;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnMappingList == null) ? 0 : columnMappingList.hashCode());
		result = prime * result
				+ ((storageSpace == null) ? 0 : storageSpace.hashCode());
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
		MetaSchemalessRdbStoreMapping other = (MetaSchemalessRdbStoreMapping) obj;
		if (columnMappingList == null) {
			if (other.columnMappingList != null)
				return false;
		} else if (!columnMappingList.equals(other.columnMappingList))
			return false;
		if (storageSpace == null) {
			if (other.storageSpace != null)
				return false;
		} else if (!storageSpace.equals(other.storageSpace))
			return false;
		return true;
	}

	
	@Override
	public void applyConfig(StoreDefinition storeDefinition, MetaEntity metaEntity) {
		SchemalessRdbStore srs = (SchemalessRdbStore) storeDefinition;
		storageSpace = srs.getStorageSpace();
		if (srs.getColumnMappingList() == null) {
			columnMappingList = null;
		} else {
			ArrayList<MetaRdbColumnMapping> list = new ArrayList<>();
			for (ColumnMapping cm: srs.getColumnMappingList()) {
				MetaProperty p = metaEntity.getDeclaredProperty(cm.getPropertyName());
				if (p == null) {
					throw new EntityRuntimeException("ColumnMapping's property not found:" + cm.getPropertyName());
				}
				list.add(new MetaRdbColumnMapping(p.getId(), cm.getColumnName()));
			}
			columnMappingList = list;
		}
	}
	@Override
	public StoreDefinition currentConfig(MetaEntity metaEntity) {
		SchemalessRdbStore srs = new SchemalessRdbStore();
		if (storageSpace != null) {
			srs.setStorageSpace(storageSpace);
		}
		if (columnMappingList != null) {
			ArrayList<ColumnMapping> list = new ArrayList<>();
			for (MetaRdbColumnMapping cm: columnMappingList) {
				MetaProperty p = metaEntity.getDeclaredPropertyById(cm.getPropertyId());
				if (p != null) {
					list.add(new ColumnMapping(p.getName(), cm.getColumnName()));
				}
			}
			srs.setColumnMappingList(list);
		}
		return srs;
	}

}
