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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.entity.definition.StoreDefinition;
import org.iplass.mtp.impl.datastore.EntityStoreRuntime;
import org.iplass.mtp.impl.datastore.MetaEntityStore;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjIndexTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjRefTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.util.ObjectUtil;


@XmlAccessorType(XmlAccessType.FIELD)
public class MetaGRdbEntityStore extends MetaEntityStore {
	private static final long serialVersionUID = 5985226568564126006L;

	private ColumnPosition maxVarchar;
	private ColumnPosition maxDecimal;
	private ColumnPosition maxTimestamp;
	private ColumnPosition maxDouble;
	private ColumnPosition maxIndexedVarchar;
	private ColumnPosition maxIndexedDecimal;
	private ColumnPosition maxIndexedTimestamp;
	private ColumnPosition maxIndexedDouble;
	private ColumnPosition maxUniqueIndexedVarchar;
	private ColumnPosition maxUniqueIndexedDecimal;
	private ColumnPosition maxUniqueIndexedTimestamp;
	private ColumnPosition maxUniqueIndexedDouble;
	
	private String tableNamePostfix;
	private int version;
	
	public int currentMaxPage() {
		int ret = 0;
		if (maxVarchar != null) {
			ret = (ret >= maxVarchar.getPageNo()) ? ret : maxVarchar.getPageNo();
		};
		if (maxDecimal != null) {
			ret = (ret >= maxDecimal.getPageNo()) ? ret : maxDecimal.getPageNo();
		};
		if (maxTimestamp != null) {
			ret = (ret >= maxTimestamp.getPageNo()) ? ret : maxTimestamp.getPageNo();
		};
		if (maxDouble != null) {
			ret = (ret >= maxDouble.getPageNo()) ? ret : maxDouble.getPageNo();
		};
		if (maxIndexedVarchar != null) {
			ret = (ret >= maxIndexedVarchar.getPageNo()) ? ret : maxIndexedVarchar.getPageNo();
		};
		if (maxIndexedDecimal != null) {
			ret = (ret >= maxIndexedDecimal.getPageNo()) ? ret : maxIndexedDecimal.getPageNo();
		};
		if (maxIndexedTimestamp != null) {
			ret = (ret >= maxIndexedTimestamp.getPageNo()) ? ret : maxIndexedTimestamp.getPageNo();
		};
		if (maxIndexedDouble != null) {
			ret = (ret >= maxIndexedDouble.getPageNo()) ? ret : maxIndexedDouble.getPageNo();
		};
		if (maxUniqueIndexedVarchar != null) {
			ret = (ret >= maxUniqueIndexedVarchar.getPageNo()) ? ret : maxUniqueIndexedVarchar.getPageNo();
		};
		if (maxUniqueIndexedDecimal != null) {
			ret = (ret >= maxUniqueIndexedDecimal.getPageNo()) ? ret : maxUniqueIndexedDecimal.getPageNo();
		};
		if (maxUniqueIndexedTimestamp != null) {
			ret = (ret >= maxUniqueIndexedTimestamp.getPageNo()) ? ret : maxUniqueIndexedTimestamp.getPageNo();
		};
		if (maxUniqueIndexedDouble != null) {
			ret = (ret >= maxUniqueIndexedDouble.getPageNo()) ? ret : maxUniqueIndexedDouble.getPageNo();
		};
		return ret;
	}
	
	public ColumnPosition getMaxVarchar() {
		return maxVarchar;
	}
	public void setMaxVarchar(ColumnPosition maxVarchar) {
		this.maxVarchar = maxVarchar;
	}
	public ColumnPosition getMaxDecimal() {
		return maxDecimal;
	}
	public void setMaxDecimal(ColumnPosition maxDecimal) {
		this.maxDecimal = maxDecimal;
	}
	public ColumnPosition getMaxTimestamp() {
		return maxTimestamp;
	}
	public void setMaxTimestamp(ColumnPosition maxTimestamp) {
		this.maxTimestamp = maxTimestamp;
	}
	public ColumnPosition getMaxDouble() {
		return maxDouble;
	}
	public void setMaxDouble(ColumnPosition maxDouble) {
		this.maxDouble = maxDouble;
	}
	public ColumnPosition getMaxIndexedVarchar() {
		return maxIndexedVarchar;
	}
	public void setMaxIndexedVarchar(ColumnPosition maxIndexedVarchar) {
		this.maxIndexedVarchar = maxIndexedVarchar;
	}
	public ColumnPosition getMaxIndexedDecimal() {
		return maxIndexedDecimal;
	}
	public void setMaxIndexedDecimal(ColumnPosition maxIndexedDecimal) {
		this.maxIndexedDecimal = maxIndexedDecimal;
	}
	public ColumnPosition getMaxIndexedTimestamp() {
		return maxIndexedTimestamp;
	}
	public void setMaxIndexedTimestamp(ColumnPosition maxIndexedTimestamp) {
		this.maxIndexedTimestamp = maxIndexedTimestamp;
	}
	public ColumnPosition getMaxIndexedDouble() {
		return maxIndexedDouble;
	}
	public void setMaxIndexedDouble(ColumnPosition maxIndexedDouble) {
		this.maxIndexedDouble = maxIndexedDouble;
	}
	public ColumnPosition getMaxUniqueIndexedVarchar() {
		return maxUniqueIndexedVarchar;
	}
	public void setMaxUniqueIndexedVarchar(ColumnPosition maxUniqueIndexedVarchar) {
		this.maxUniqueIndexedVarchar = maxUniqueIndexedVarchar;
	}
	public ColumnPosition getMaxUniqueIndexedDecimal() {
		return maxUniqueIndexedDecimal;
	}
	public void setMaxUniqueIndexedDecimal(ColumnPosition maxUniqueIndexedDecimal) {
		this.maxUniqueIndexedDecimal = maxUniqueIndexedDecimal;
	}
	public ColumnPosition getMaxUniqueIndexedTimestamp() {
		return maxUniqueIndexedTimestamp;
	}
	public void setMaxUniqueIndexedTimestamp(ColumnPosition maxUniqueIndexedTimestamp) {
		this.maxUniqueIndexedTimestamp = maxUniqueIndexedTimestamp;
	}
	public ColumnPosition getMaxUniqueIndexedDouble() {
		return maxUniqueIndexedDouble;
	}
	public void setMaxUniqueIndexedDouble(ColumnPosition maxUniqueIndexedDouble) {
		this.maxUniqueIndexedDouble = maxUniqueIndexedDouble;
	}
	public String getTableNamePostfix() {
		return tableNamePostfix;
	}
	public void setTableNamePostfix(String tableNamePostfix) {
		this.tableNamePostfix = tableNamePostfix;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	public MetaGRdbEntityStore copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((maxDecimal == null) ? 0 : maxDecimal.hashCode());
		result = prime * result
				+ ((maxDouble == null) ? 0 : maxDouble.hashCode());
		result = prime
				* result
				+ ((maxIndexedDecimal == null) ? 0 : maxIndexedDecimal
						.hashCode());
		result = prime
				* result
				+ ((maxIndexedDouble == null) ? 0 : maxIndexedDouble.hashCode());
		result = prime
				* result
				+ ((maxIndexedTimestamp == null) ? 0 : maxIndexedTimestamp
						.hashCode());
		result = prime
				* result
				+ ((maxIndexedVarchar == null) ? 0 : maxIndexedVarchar
						.hashCode());
		result = prime * result
				+ ((maxTimestamp == null) ? 0 : maxTimestamp.hashCode());
		result = prime
				* result
				+ ((maxUniqueIndexedDecimal == null) ? 0
						: maxUniqueIndexedDecimal.hashCode());
		result = prime
				* result
				+ ((maxUniqueIndexedDouble == null) ? 0
						: maxUniqueIndexedDouble.hashCode());
		result = prime
				* result
				+ ((maxUniqueIndexedTimestamp == null) ? 0
						: maxUniqueIndexedTimestamp.hashCode());
		result = prime
				* result
				+ ((maxUniqueIndexedVarchar == null) ? 0
						: maxUniqueIndexedVarchar.hashCode());
		result = prime * result
				+ ((maxVarchar == null) ? 0 : maxVarchar.hashCode());
		result = prime
				* result
				+ ((tableNamePostfix == null) ? 0 : tableNamePostfix.hashCode());
		result = prime * result + version;
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
		MetaGRdbEntityStore other = (MetaGRdbEntityStore) obj;
		if (maxDecimal == null) {
			if (other.maxDecimal != null)
				return false;
		} else if (!maxDecimal.equals(other.maxDecimal))
			return false;
		if (maxDouble == null) {
			if (other.maxDouble != null)
				return false;
		} else if (!maxDouble.equals(other.maxDouble))
			return false;
		if (maxIndexedDecimal == null) {
			if (other.maxIndexedDecimal != null)
				return false;
		} else if (!maxIndexedDecimal.equals(other.maxIndexedDecimal))
			return false;
		if (maxIndexedDouble == null) {
			if (other.maxIndexedDouble != null)
				return false;
		} else if (!maxIndexedDouble.equals(other.maxIndexedDouble))
			return false;
		if (maxIndexedTimestamp == null) {
			if (other.maxIndexedTimestamp != null)
				return false;
		} else if (!maxIndexedTimestamp.equals(other.maxIndexedTimestamp))
			return false;
		if (maxIndexedVarchar == null) {
			if (other.maxIndexedVarchar != null)
				return false;
		} else if (!maxIndexedVarchar.equals(other.maxIndexedVarchar))
			return false;
		if (maxTimestamp == null) {
			if (other.maxTimestamp != null)
				return false;
		} else if (!maxTimestamp.equals(other.maxTimestamp))
			return false;
		if (maxUniqueIndexedDecimal == null) {
			if (other.maxUniqueIndexedDecimal != null)
				return false;
		} else if (!maxUniqueIndexedDecimal
				.equals(other.maxUniqueIndexedDecimal))
			return false;
		if (maxUniqueIndexedDouble == null) {
			if (other.maxUniqueIndexedDouble != null)
				return false;
		} else if (!maxUniqueIndexedDouble.equals(other.maxUniqueIndexedDouble))
			return false;
		if (maxUniqueIndexedTimestamp == null) {
			if (other.maxUniqueIndexedTimestamp != null)
				return false;
		} else if (!maxUniqueIndexedTimestamp
				.equals(other.maxUniqueIndexedTimestamp))
			return false;
		if (maxUniqueIndexedVarchar == null) {
			if (other.maxUniqueIndexedVarchar != null)
				return false;
		} else if (!maxUniqueIndexedVarchar
				.equals(other.maxUniqueIndexedVarchar))
			return false;
		if (maxVarchar == null) {
			if (other.maxVarchar != null)
				return false;
		} else if (!maxVarchar.equals(other.maxVarchar))
			return false;
		if (tableNamePostfix == null) {
			if (other.tableNamePostfix != null)
				return false;
		} else if (!tableNamePostfix.equals(other.tableNamePostfix))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	@Override
	public GRdbEntityStoreRuntime createRuntime(EntityHandler eh) {
		return new GRdbEntityStoreRuntime();
	}

	public class GRdbEntityStoreRuntime extends EntityStoreRuntime {

		private String objStore = ObjStoreTable.TABLE_NAME;
		private String objStoreRb = ObjStoreTable.TABLE_NAME_RB;
		private String objRef = ObjRefTable.TABLE_NAME;
		private String objRefRb = ObjRefTable.TABLE_NAME_RB;
		
		private int currentMaxPage;

		GRdbEntityStoreRuntime() {
			if (tableNamePostfix != null) {
				objStore = objStore + "__" + tableNamePostfix;
				objStoreRb = objStoreRb + "__" + tableNamePostfix;
				objRef = objRef + "__" + tableNamePostfix;
				objRefRb = objRefRb + "__" + tableNamePostfix;
			}
			currentMaxPage = currentMaxPage();
		}
		
		public int getCurrentMaxPage() {
			return currentMaxPage;
		}
		
		@Override
		public MetaEntityStore getMetaData() {
			return MetaGRdbEntityStore.this;
		}


		public String OBJ_STORE() {
			return objStore;
		}

		public String OBJ_STORE_RB() {
			return objStoreRb;
		}

		public String OBJ_INDEX(String colTypePostFix) {
			return makeIndexTableName(colTypePostFix, tableNamePostfix);
		}

		public String OBJ_UNIQUE(String colTypePostFix) {
			return makeUniqueIndexTableName(colTypePostFix, tableNamePostfix);
		}

		public String OBJ_REF() {
			return objRef;
		}

		public String OBJ_REF_RB() {
			return objRefRb;
		}

	}

	public static String makeObjStoreName(String tableNamePostfix) {
		if (tableNamePostfix == null) {
			return ObjStoreTable.TABLE_NAME;
		} else {
			return ObjStoreTable.TABLE_NAME + "__" + tableNamePostfix;
		}
	}

	public static String makeObjRefTableName(String tableNamePostfix) {
		if (tableNamePostfix == null) {
			return ObjRefTable.TABLE_NAME;
		} else {
			return ObjRefTable.TABLE_NAME + "__" + tableNamePostfix;
		}
	}

	public static String makeIndexTableName(String colTypePostFix, String tableNamePostfix) {
		if (tableNamePostfix == null) {
			return ObjIndexTable.TABLE_INDEX_PREFIX_NAME + colTypePostFix;
		} else {
			return ObjIndexTable.TABLE_INDEX_PREFIX_NAME + colTypePostFix + "__" + tableNamePostfix;
		}
	}

	public static String makeUniqueIndexTableName(String colTypePostFix, String tableNamePostfix) {
		if (tableNamePostfix == null) {
			return ObjIndexTable.TABLE_UNIQUE_PREFIX_NAME + colTypePostFix;
		} else {
			return ObjIndexTable.TABLE_UNIQUE_PREFIX_NAME + colTypePostFix + "__" + tableNamePostfix;
		}
	}

	public static String makeObjRbTableName(String tableNamePostfix) {
		if (tableNamePostfix == null) {
			return ObjStoreTable.TABLE_NAME_RB;
		} else {
			return ObjStoreTable.TABLE_NAME_RB + "__" + tableNamePostfix;
		}
	}

	public static String makeObjRefRbTableName(String tableNamePostfix) {
		if (tableNamePostfix == null) {
			return ObjRefTable.TABLE_NAME_RB;
		} else {
			return ObjRefTable.TABLE_NAME_RB + "__" + tableNamePostfix;
		}
	}

	@Override
	public void applyConfig(StoreDefinition storeDefinition, MetaEntity metaEntity) {
	}
	
	@Override
	public StoreDefinition currentConfig(MetaEntity metaEntity) {
		return null;
	}

	public void clearColumnPosition() {
		maxVarchar = null;
		maxDecimal = null;
		maxTimestamp = null;
		maxDouble = null;
		maxIndexedVarchar = null;
		maxIndexedDecimal = null;
		maxIndexedTimestamp = null;
		maxIndexedDouble = null;
		maxUniqueIndexedVarchar = null;
		maxUniqueIndexedDecimal = null;
		maxUniqueIndexedTimestamp = null;
		maxUniqueIndexedDouble = null;
	}

}
