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

import java.sql.Types;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;

public enum RawColType {
	VARCHAR {
		@Override
		public String getColNamePrefix() {
			return ObjStoreTable.VALUE_STR_PREFIX;
		}
		@Override
		public String getNonUniqueIndexColNamePrefix() {
			return ObjStoreTable.INDEX_STR_PREFIX;
		}
		@Override
		public String getUniqueIndexColNamePrefix() {
			return ObjStoreTable.UNIQUE_STR_PREFIX;
		}
		@Override
		public int getMaxNormalCol(StorageSpaceMap ssm) {
			return ssm.getVarcharColumns();
		}
		@Override
		public int getMaxIndexCol(StorageSpaceMap ssm) {
			return ssm.getIndexedVarcharColumns();
		}
		@Override
		public int getMaxUniqueIndexCol(StorageSpaceMap ssm) {
			return ssm.getUniqueIndexedVarcharColumns();
		}
		@Override
		public ColumnPosition getColumnPositionOf(MetaGRdbEntityStore store, RawColIndexType type) {
			if (type == null) {
				type = RawColIndexType.NONE;
			}
			switch (type) {
			case NONE:
				return store.getMaxVarchar();
			case INDEX:
				return store.getMaxIndexedVarchar();
			case UNIQUE_INDEX:
				return store.getMaxUniqueIndexedVarchar();
			default:
				return null;
			}
		}
		@Override
		public void setColumnPositionOf(MetaGRdbEntityStore store,
				RawColIndexType type, ColumnPosition columnPosition) {
			if (type == null) {
				type = RawColIndexType.NONE;
			}
			switch (type) {
			case NONE:
				store.setMaxVarchar(columnPosition);
				break;
			case INDEX:
				store.setMaxIndexedVarchar(columnPosition);
				break;
			case UNIQUE_INDEX:
				store.setMaxUniqueIndexedVarchar(columnPosition);
				break;
			default:
				break;
			}
		}
	},
	DECIMAL {
		@Override
		public String getColNamePrefix() {
			return ObjStoreTable.VALUE_NUM_PREFIX;
		}
		@Override
		public String getNonUniqueIndexColNamePrefix() {
			return ObjStoreTable.INDEX_NUM_PREFIX;
		}
		@Override
		public String getUniqueIndexColNamePrefix() {
			return ObjStoreTable.UNIQUE_NUM_PREFIX;
		}
		@Override
		public int getMaxNormalCol(StorageSpaceMap ssm) {
			return ssm.getDecimalColumns();
		}
		@Override
		public int getMaxIndexCol(StorageSpaceMap ssm) {
			return ssm.getIndexedDecimalColumns();
		}
		@Override
		public int getMaxUniqueIndexCol(StorageSpaceMap ssm) {
			return ssm.getUniqueIndexedDecimalColumns();
		}
		@Override
		public ColumnPosition getColumnPositionOf(MetaGRdbEntityStore store, RawColIndexType type) {
			if (type == null) {
				type = RawColIndexType.NONE;
			}
			switch (type) {
			case NONE:
				return store.getMaxDecimal();
			case INDEX:
				return store.getMaxIndexedDecimal();
			case UNIQUE_INDEX:
				return store.getMaxUniqueIndexedDecimal();
			default:
				return null;
			}
		}
		@Override
		public void setColumnPositionOf(MetaGRdbEntityStore store,
				RawColIndexType type, ColumnPosition columnPosition) {
			if (type == null) {
				type = RawColIndexType.NONE;
			}
			switch (type) {
			case NONE:
				store.setMaxDecimal(columnPosition);
				break;
			case INDEX:
				store.setMaxIndexedDecimal(columnPosition);
				break;
			case UNIQUE_INDEX:
				store.setMaxUniqueIndexedDecimal(columnPosition);
				break;
			default:
				break;
			}
		}
	},
	TIMESTAMP {
		@Override
		public String getColNamePrefix() {
			return ObjStoreTable.VALUE_TS_PREFIX;
		}
		@Override
		public String getNonUniqueIndexColNamePrefix() {
			return ObjStoreTable.INDEX_TS_PREFIX;
		}
		@Override
		public String getUniqueIndexColNamePrefix() {
			return ObjStoreTable.UNIQUE_TS_PREFIX;
		}
		@Override
		public int getMaxNormalCol(StorageSpaceMap ssm) {
			return ssm.getTimestampColumns();
		}
		@Override
		public int getMaxIndexCol(StorageSpaceMap ssm) {
			return ssm.getIndexedTimestampColumns();
		}
		@Override
		public int getMaxUniqueIndexCol(StorageSpaceMap ssm) {
			return ssm.getUniqueIndexedTimestampColumns();
		}
		@Override
		public ColumnPosition getColumnPositionOf(MetaGRdbEntityStore store, RawColIndexType type) {
			if (type == null) {
				type = RawColIndexType.NONE;
			}
			switch (type) {
			case NONE:
				return store.getMaxTimestamp();
			case INDEX:
				return store.getMaxIndexedTimestamp();
			case UNIQUE_INDEX:
				return store.getMaxUniqueIndexedTimestamp();
			default:
				return null;
			}
		}
		@Override
		public void setColumnPositionOf(MetaGRdbEntityStore store,
				RawColIndexType type, ColumnPosition columnPosition) {
			if (type == null) {
				type = RawColIndexType.NONE;
			}
			switch (type) {
			case NONE:
				store.setMaxTimestamp(columnPosition);
				break;
			case INDEX:
				store.setMaxIndexedTimestamp(columnPosition);
				break;
			case UNIQUE_INDEX:
				store.setMaxUniqueIndexedTimestamp(columnPosition);
				break;
			default:
				break;
			}
		}
	},
	DOUBLE {
		@Override
		public String getColNamePrefix() {
			return ObjStoreTable.VALUE_DBL_PREFIX;
		}
		@Override
		public String getNonUniqueIndexColNamePrefix() {
			return ObjStoreTable.INDEX_DBL_PREFIX;
		}
		@Override
		public String getUniqueIndexColNamePrefix() {
			return ObjStoreTable.UNIQUE_DBL_PREFIX;
		}
		@Override
		public int getMaxNormalCol(StorageSpaceMap ssm) {
			return ssm.getDoubleColumns();
		}
		@Override
		public int getMaxIndexCol(StorageSpaceMap ssm) {
			return ssm.getIndexedDoubleColumns();
		}
		@Override
		public int getMaxUniqueIndexCol(StorageSpaceMap ssm) {
			return ssm.getUniqueIndexedDoubleColumns();
		}
		@Override
		public ColumnPosition getColumnPositionOf(MetaGRdbEntityStore store, RawColIndexType type) {
			if (type == null) {
				type = RawColIndexType.NONE;
			}
			switch (type) {
			case NONE:
				return store.getMaxDouble();
			case INDEX:
				return store.getMaxIndexedDouble();
			case UNIQUE_INDEX:
				return store.getMaxUniqueIndexedDouble();
			default:
				return null;
			}
		}
		@Override
		public void setColumnPositionOf(MetaGRdbEntityStore store,
				RawColIndexType type, ColumnPosition columnPosition) {
			if (type == null) {
				type = RawColIndexType.NONE;
			}
			switch (type) {
			case NONE:
				store.setMaxDouble(columnPosition);
				break;
			case INDEX:
				store.setMaxIndexedDouble(columnPosition);
				break;
			case UNIQUE_INDEX:
				store.setMaxUniqueIndexedDouble(columnPosition);
				break;
			default:
				break;
			}
		}
	};
	
	public int getMaxCol(StorageSpaceMap ssm, RawColIndexType indexType) {
		switch (indexType) {
		case NONE:
			return getMaxNormalCol(ssm);
		case INDEX:
			return getMaxIndexCol(ssm);
		case UNIQUE_INDEX:
			return getMaxUniqueIndexCol(ssm);
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public String getColNamePrefix(RawColIndexType indexType) {
		switch (indexType) {
		case NONE:
			return getColNamePrefix();
		case INDEX:
			return getNonUniqueIndexColNamePrefix();
		case UNIQUE_INDEX:
			return getUniqueIndexColNamePrefix();
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public abstract String getColNamePrefix();
	public abstract String getNonUniqueIndexColNamePrefix();
	public abstract String getUniqueIndexColNamePrefix();
	public abstract int getMaxNormalCol(StorageSpaceMap ssm);
	public abstract int getMaxIndexCol(StorageSpaceMap ssm);
	public abstract int getMaxUniqueIndexCol(StorageSpaceMap ssm);
	public abstract ColumnPosition getColumnPositionOf(MetaGRdbEntityStore store, RawColIndexType type);
	public abstract void setColumnPositionOf(MetaGRdbEntityStore store, RawColIndexType type, ColumnPosition columnPosition);
	
	public void setColumnPositionOf(MetaGRdbEntityStore store, IndexType type, ColumnPosition columnPosition) {
		setColumnPositionOf(store, RawColIndexType.typeOf(type), columnPosition);
	}
	public ColumnPosition getColumnPositionOf(MetaGRdbEntityStore store, IndexType type) {
		return getColumnPositionOf(store, RawColIndexType.typeOf(type));
	}
	
	public int getMaxCol(StorageSpaceMap ssm, IndexType type) {
		if (type == null) {
			type = IndexType.NON_INDEXED;
		}
		switch (type) {
		case NON_INDEXED:
			return getMaxNormalCol(ssm);
		case NON_UNIQUE:
			return getMaxIndexCol(ssm);
		case UNIQUE:
		case UNIQUE_WITHOUT_NULL:
			return getMaxUniqueIndexCol(ssm);
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public String getIndexColNamePrefix(IndexType type) {
		if (type == null) {
			return null;
		}
		switch (type) {
		case NON_UNIQUE:
			return getNonUniqueIndexColNamePrefix();
		case UNIQUE:
		case UNIQUE_WITHOUT_NULL:
			return getUniqueIndexColNamePrefix();
		default:
			return null;
		}
	}
	
	public static RawColType typeOf(BaseRdbTypeAdapter type) {
		switch (type.sqlType()) {
		case Types.TIMESTAMP:
		case Types.DATE:
		case Types.TIME:
			return TIMESTAMP;
		case Types.DECIMAL:
		case Types.BIGINT:
			return DECIMAL;
		case Types.DOUBLE:
			return DOUBLE;
		case Types.VARCHAR:
			return VARCHAR;
		default:
			throw new IllegalArgumentException("no support sql type:" + type.sqlType() + ", rdbType:" + type);
		}
	}
	
	public static RawColType typeOf(PropertyType type) {
		switch (type.getDataStoreEnumType()) {
//		case AUTONUMBER:
		case BOOLEAN:
//		case SELECT:
		case STRING:
//		case LONGTEXT:
//		case BINARY:
			return VARCHAR;
		case DATE:
		case DATETIME:
		case TIME:
			return TIMESTAMP;
		case DECIMAL:
		case INTEGER:
			return DECIMAL;
		case FLOAT:
			return DOUBLE;
		case REFERENCE:
		case EXPRESSION:
		default:
			throw new IllegalArgumentException("no support PropertyType:" + type);
		}
	}
}
