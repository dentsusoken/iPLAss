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


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.MetaPropertyStore;
import org.iplass.mtp.impl.datastore.PropertyStoreHandler;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjIndexTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.MetaSchemalessRdbStoreMapping;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;


@XmlAccessorType(XmlAccessType.FIELD)
public class MetaGRdbPropertyStore extends MetaPropertyStore {
	private static final long serialVersionUID = -1971559137948520477L;
	
	private int pageNo = 0;
	private String columnName;
	private boolean isNative;
	
	//nullの状態も作りたいので。。
	private Boolean externalIndex;
	private Integer indexPageNo;
	private Integer indexColumnNo;
	
	public MetaGRdbPropertyStore() {
	}
	
	public MetaGRdbPropertyStore(int pageNo, String columnName) {
		this.pageNo = pageNo;
		this.columnName = columnName;
	}
	
	public MetaGRdbPropertyStore(int pageNo, String columnName, boolean isNative) {
		if (!isNative) {
			this.pageNo = pageNo;
		}
		this.columnName = columnName;
		this.isNative = isNative;
	}
	
	public static String makeInternalIndexKey(int tenantId, String metaEntityId, int pageNo) {
		return tenantId + ObjStoreTable.INDEX_TD_SEPARATOR + metaEntityId + ObjStoreTable.INDEX_TD_SEPARATOR + pageNo;
	}
	
	public static String makeExternalIndexColName(int pageNo, String columnName) {
		 return pageNo + ObjIndexTable.PG_COL_SEPARATOR + columnName;
	}
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public boolean isNative() {
		return isNative;
	}
	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}

	public boolean isExternalIndex() {
		if (externalIndex == null) {
			return false;
		}
		return externalIndex;
	}

	public void setExternalIndex(boolean externalIndex) {
		if (!externalIndex) {
			this.externalIndex = null;
		} else {
			this.externalIndex = externalIndex;
		}
	}

	public int getIndexPageNo() {
		if (indexPageNo == null) {
			return -1;
		}
		return indexPageNo;
	}

	public void setIndexPageNo(int indexPageNo) {
		if (indexPageNo < 0) {
			this.indexPageNo = null;
		} else {
			this.indexPageNo = indexPageNo;
		}
	}

	public int getIndexColumnNo() {
		if (indexColumnNo == null) {
			return -1;
		}
		return indexColumnNo;
	}

	public void setIndexColumnNo(int indexColumnNo) {
		if (indexColumnNo < 0) {
			this.indexColumnNo = null;
		} else {
			this.indexColumnNo = indexColumnNo;
		}
	}

	public void resetIndex() {
		externalIndex = null;
		indexPageNo = null;
		indexColumnNo = null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result
				+ ((externalIndex == null) ? 0 : externalIndex.hashCode());
		result = prime * result
				+ ((indexColumnNo == null) ? 0 : indexColumnNo.hashCode());
		result = prime * result
				+ ((indexPageNo == null) ? 0 : indexPageNo.hashCode());
		result = prime * result + (isNative ? 1231 : 1237);
		result = prime * result + pageNo;
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
		MetaGRdbPropertyStore other = (MetaGRdbPropertyStore) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (externalIndex == null) {
			if (other.externalIndex != null)
				return false;
		} else if (!externalIndex.equals(other.externalIndex))
			return false;
		if (indexColumnNo == null) {
			if (other.indexColumnNo != null)
				return false;
		} else if (!indexColumnNo.equals(other.indexColumnNo))
			return false;
		if (indexPageNo == null) {
			if (other.indexPageNo != null)
				return false;
		} else if (!indexPageNo.equals(other.indexPageNo))
			return false;
		if (isNative != other.isNative)
			return false;
		if (pageNo != other.pageNo)
			return false;
		return true;
	}

	@Override
	public MetaGRdbPropertyStore copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public GRdbPropertyStoreHandler createRuntime(PropertyHandler property, MetaEntity metaEntity) {
		return new GRdbPropertyStoreHandler(property, this, metaEntity);
	}
	
	public static class GRdbPropertyStoreHandler extends PropertyStoreHandler implements GRdbPropertyStoreRuntime {
		
		private BaseRdbTypeAdapter typeMapping;
		private RdbAdapter rdb;
		private PrimitivePropertyHandler propertyRuntime;
		
		private String indexColName;
		private RawColType rawColType;
		private List<GRdbPropertyStoreHandler> list;
		private MetaGRdbPropertyStore meta;
		private String externalIndexColName;
		private boolean adjustedExternalIndex;
		
		GRdbPropertyStoreHandler(PropertyHandler propertyRuntime, MetaGRdbPropertyStore meta, MetaEntity metaEntity) {
			//Primitive型しかないはず
			this.propertyRuntime = (PrimitivePropertyHandler) propertyRuntime;
			this.meta = meta;
			rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
			PropertyType type = this.propertyRuntime.getMetaData().getType();
			typeMapping = rdb.getRdbTypeAdapter(type);
			
			//汎用カラムの型
			if (!type.isVirtual() && !meta.isNative) {
				rawColType = RawColType.typeOf(type);
			}
			
			//index関連
			IndexType iType = propertyRuntime.getMetaData().getIndexType();
			if (!type.isVirtual() && !meta.isNative
					&& iType != null && iType != IndexType.NON_INDEXED) {
				if (propertyRuntime.getMetaData().getMultiplicity() > 1) {
					throw new IllegalStateException("No support of index on multipled property(" + propertyRuntime.getMetaData().getName() + ").");
				}
				
				
				//SotrageSpaceMapの定義によっては、内部Indexが使えないことがあるので、、（主にXmlで静的定義されているEntityに関して）
				adjustedExternalIndex = checkUseExternalIndex(iType, metaEntity);
				
				if (!adjustedExternalIndex) {
					IndexType it = propertyRuntime.getMetaData().getIndexType();
					if (it != null) {
						switch (it) {
						case UNIQUE:
						case UNIQUE_WITHOUT_NULL:
							indexColName = rawColType.getUniqueIndexColNamePrefix() + meta.getIndexColumnNo();
							break;
						case NON_UNIQUE:
							indexColName = rawColType.getNonUniqueIndexColNamePrefix() + meta.getIndexColumnNo();
							break;
						default:
							break;
						}
					}
				} else {
					externalIndexColName = makeExternalIndexColName(meta.getPageNo(), meta.columnName);
				}
			}
			
			list = Collections.singletonList(this);
		}
		
		private boolean checkUseExternalIndex(IndexType iType, MetaEntity metaEntity) {
			if (meta.isNative()) {
				return false;
			}
			if (meta.isExternalIndex()) {
				return true;
			}
			
			GRdbDataStore ds = (GRdbDataStore) ServiceRegistry.getRegistry().getService(StoreService.class).getDataStore();
			StorageSpaceMap ssm = ds.getStorageSpaceMapOrDefault((MetaSchemalessRdbStoreMapping) metaEntity.getStoreMapping());
			int max = 0;
			switch (iType) {
			case UNIQUE:
			case UNIQUE_WITHOUT_NULL:
				max = rawColType.getMaxUniqueIndexCol(ssm);
				break;
			case NON_UNIQUE:
				max = rawColType.getMaxIndexCol(ssm);
				break;
			default:
				break;
			}
			
			if (max < meta.getIndexColumnNo()) {
				return true;
			} else {
				return false;
			}
		}

		public String getExternalIndexColName() {
			return externalIndexColName;
		}
		
		@Override
		public BaseRdbTypeAdapter getSingleColumnRdbTypeAdapter() {
			return (BaseRdbTypeAdapter) typeMapping;
		}
		
		@Override
		public int getColCount() {
			return 1;
		}
		
		@Override
		public boolean isNative() {
			return meta.isNative;
		}
		
		public String getIndexColName() {
			return indexColName;
		}
		
		@Override
		public boolean isExternalIndex() {
			return adjustedExternalIndex;
		}
		
		public IndexType getIndexType() {
			IndexType it = propertyRuntime.getMetaData().getIndexType();
			if (it == null) {
				return IndexType.NON_INDEXED;
			} else {
				return it;
			}
		}
		
		public PropertyType getPropertyType() {
			return propertyRuntime.getMetaData().getType();
		}
		
		public RawColType getRawColType() {
			return rawColType;
		}
		
		@Override
		public PrimitivePropertyHandler getPropertyRuntime() {
			return propertyRuntime;
		}

		@Override
		public Object fromDataStore(ResultSet rs, int colNum) throws SQLException {
			return getSingleColumnRdbTypeAdapter().fromDataStore(rs, colNum, rdb);
		}

		@Override
		public MetaGRdbPropertyStore getMetaData() {
			return meta;
		}

		@Override
		public boolean isMulti() {
			return false;
		}

		@Override
		public List<GRdbPropertyStoreHandler> asList() {
			return list;
		}
	}

}
