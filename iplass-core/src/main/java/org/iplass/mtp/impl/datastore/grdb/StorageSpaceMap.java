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
import java.util.List;

import org.iplass.mtp.impl.datastore.grdb.tableallocators.HashingTableAllocator;

public class StorageSpaceMap {
	public static final String TABLE_NAME_SEPARATOR = "__";

	private String storageSpaceName;
	private String tableNamePostfix;
	private int tableCount = 0;

	private int varcharColumns;
	private int decimalColumns;
	private int timestampColumns;
	private int doubleColumns;
	private boolean useExternalIndexedTable = true;
	private int indexedVarcharColumns;
	private int indexedDecimalColumns;
	private int indexedTimestampColumns;
	private int indexedDoubleColumns;
	private boolean useExternalUniqueIndexedTable = true;;
	private int uniqueIndexedVarcharColumns;
	private int uniqueIndexedDecimalColumns;
	private int uniqueIndexedTimestampColumns;
	private int uniqueIndexedDoubleColumns;

	private boolean customPartition;
	
	private TableAllocator tableAllocator = new HashingTableAllocator();
	
	private int varcharColumnLength = -1;

	public String generateTableNamePostfix(int tenantId, String metaId) {
		if (tableCount == 0) {
			if (tableNamePostfix == null) {
				return null;
			} else {
				return tableNamePostfix;
			}
		} else {
			//物理的に格納テーブルを分ける場合（擬似パーティショニングする場合）
			int myCount = tableAllocator.allocate(tenantId, metaId, this);

			if (tableNamePostfix == null) {
				if (myCount == 0) {
					return null;
				} else {
					return String.valueOf(myCount);
				}
			} else {
				if (myCount == 0) {
					return tableNamePostfix;
				} else {
					return tableNamePostfix + TABLE_NAME_SEPARATOR + String.valueOf(myCount);
				}
			}
		}
	}
	
	public int maxColumns() {
		int ret = Math.max(varcharColumns, decimalColumns);
		ret = Math.max(ret, timestampColumns);
		ret = Math.max(ret, doubleColumns);
		ret = Math.max(ret, indexedVarcharColumns);
		ret = Math.max(ret, indexedDecimalColumns);
		ret = Math.max(ret, indexedTimestampColumns);
		ret = Math.max(ret, indexedDoubleColumns);
		ret = Math.max(ret, uniqueIndexedVarcharColumns);
		ret = Math.max(ret, uniqueIndexedDecimalColumns);
		ret = Math.max(ret, uniqueIndexedTimestampColumns);
		ret = Math.max(ret, uniqueIndexedDoubleColumns);
		return ret;
	}

	public int tableNo(String checkTableNamePostfix) {
		if (checkTableNamePostfix == null) {
			if (tableNamePostfix == null) {
				return 0;
			} else {
				//can not decide..
				return -1;
			}
		}
		
		if (checkTableNamePostfix.equals(tableNamePostfix)) {
			return 0;
		}
		
		int i = checkTableNamePostfix.lastIndexOf(TABLE_NAME_SEPARATOR);
		if (i == -1) {
			try {
				return Integer.parseInt(checkTableNamePostfix);
			} catch (NumberFormatException e) {
				//can not decide..
				return -1;
			}
		} else {
			String ssPart = checkTableNamePostfix.substring(0, i);
			String noPart = checkTableNamePostfix.substring(i + TABLE_NAME_SEPARATOR.length(), checkTableNamePostfix.length());
			if (ssPart.equals(tableNamePostfix)) {
				try {
					return Integer.parseInt(noPart);
				} catch (NumberFormatException e) {
					//can not decide..
					return -1;
				}
			} else {
				//can not decide..
				return -1;
			}
		}
	}

	public List<String> allTableNamePostfix() {
		if (tableCount == 0) {
			if (tableNamePostfix == null) {
				return Collections.singletonList(null);
			} else {
				return Collections.singletonList(tableNamePostfix);
			}
		} else {
			ArrayList<String> res = new ArrayList<>();
			if (tableNamePostfix == null) {
				res.add(null);
				for (int i = 1; i < tableCount; i++) {
					res.add(String.valueOf(i));
				}
			} else {
				res.add(tableNamePostfix);
				for (int i = 1; i < tableCount; i++) {
					res.add(tableNamePostfix + TABLE_NAME_SEPARATOR + String.valueOf(i));
				}
			}
			return res;
		}
	}

	public String getStorageSpaceName() {
		return storageSpaceName;
	}
	public void setStorageSpaceName(String storageSpaceName) {
		this.storageSpaceName = storageSpaceName;
	}
	public String getTableNamePostfix() {
		return tableNamePostfix;
	}
	public void setTableNamePostfix(String tableNamePostfix) {
		this.tableNamePostfix = tableNamePostfix;
	}

	public int getTableCount() {
		return tableCount;
	}
	public void setTableCount(int tableCount) {
		this.tableCount = tableCount;
	}

	public int getVarcharColumns() {
		return varcharColumns;
	}

	public void setVarcharColumns(int varcharColumns) {
		this.varcharColumns = varcharColumns;
	}

	public int getDecimalColumns() {
		return decimalColumns;
	}

	public void setDecimalColumns(int decimalColumns) {
		this.decimalColumns = decimalColumns;
	}

	public int getTimestampColumns() {
		return timestampColumns;
	}

	public void setTimestampColumns(int timestampColumns) {
		this.timestampColumns = timestampColumns;
	}

	public int getDoubleColumns() {
		return doubleColumns;
	}

	public void setDoubleColumns(int doubleColumns) {
		this.doubleColumns = doubleColumns;
	}
	
	public boolean isUseExternalIndexedTable() {
		return useExternalIndexedTable;
	}

	public void setUseExternalIndexedTable(boolean useExternalIndexedTable) {
		this.useExternalIndexedTable = useExternalIndexedTable;
	}

	public int getIndexedVarcharColumns() {
		return indexedVarcharColumns;
	}

	public void setIndexedVarcharColumns(int indexedVarcharColumns) {
		this.indexedVarcharColumns = indexedVarcharColumns;
	}

	public int getIndexedDecimalColumns() {
		return indexedDecimalColumns;
	}

	public void setIndexedDecimalColumns(int indexedDecimalColumns) {
		this.indexedDecimalColumns = indexedDecimalColumns;
	}

	public int getIndexedTimestampColumns() {
		return indexedTimestampColumns;
	}

	public void setIndexedTimestampColumns(int indexedTimestampColumns) {
		this.indexedTimestampColumns = indexedTimestampColumns;
	}

	public int getIndexedDoubleColumns() {
		return indexedDoubleColumns;
	}

	public void setIndexedDoubleColumns(int indexedDoubleColumns) {
		this.indexedDoubleColumns = indexedDoubleColumns;
	}

	public boolean isUseExternalUniqueIndexedTable() {
		return useExternalUniqueIndexedTable;
	}

	public void setUseExternalUniqueIndexedTable(boolean useExternalUniqueIndexedTable) {
		this.useExternalUniqueIndexedTable = useExternalUniqueIndexedTable;
	}
	public int getUniqueIndexedVarcharColumns() {
		return uniqueIndexedVarcharColumns;
	}

	public void setUniqueIndexedVarcharColumns(int uniqueIndexedVarcharColumns) {
		this.uniqueIndexedVarcharColumns = uniqueIndexedVarcharColumns;
	}

	public int getUniqueIndexedDecimalColumns() {
		return uniqueIndexedDecimalColumns;
	}

	public void setUniqueIndexedDecimalColumns(int uniqueIndexedDecimalColumns) {
		this.uniqueIndexedDecimalColumns = uniqueIndexedDecimalColumns;
	}

	public int getUniqueIndexedTimestampColumns() {
		return uniqueIndexedTimestampColumns;
	}

	public void setUniqueIndexedTimestampColumns(int uniqueIndexedTimestampColumns) {
		this.uniqueIndexedTimestampColumns = uniqueIndexedTimestampColumns;
	}

	public int getUniqueIndexedDoubleColumns() {
		return uniqueIndexedDoubleColumns;
	}

	public void setUniqueIndexedDoubleColumns(int uniqueIndexedDoubleColumns) {
		this.uniqueIndexedDoubleColumns = uniqueIndexedDoubleColumns;
	}

	public boolean isCustomPartition() {
		return customPartition;
	}

	public void setCustomPartition(boolean customPartition) {
		this.customPartition = customPartition;
	}

	public TableAllocator getTableAllocator() {
		return tableAllocator;
	}

	public void setTableAllocator(TableAllocator tableAllocator) {
		this.tableAllocator = tableAllocator;
	}

	public int getVarcharColumnLength() {
		return varcharColumnLength;
	}

	public void setVarcharColumnLength(int varcharColumnLength) {
		this.varcharColumnLength = varcharColumnLength;
	}

}
