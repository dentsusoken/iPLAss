/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition.stores;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.definition.StoreDefinition;

/**
 * 物理データベースに関する定義を表すクラス。
 *
 * @author K.Higuchi
 *
 */
public class SchemalessRdbStore extends StoreDefinition {
	private static final long serialVersionUID = -1347359195417259943L;

	public static final String DEFAULT_STORAGE_SPACE = "default";

	private String storageSpace = DEFAULT_STORAGE_SPACE;
	private List<ColumnMapping> columnMappingList;

	public SchemalessRdbStore() {
	}

	public SchemalessRdbStore(String storageSpace) {
		this.storageSpace = storageSpace;
	}

	/**
	 * ColumnMappingのリストを取得。
	 *
	 * @return
	 */
	public List<ColumnMapping> getColumnMappingList() {
		return columnMappingList;
	}

	/**
	 * ColumnMappingのリストを取得。
	 *
	 * @param columnMappingList
	 */
	public void setColumnMappingList(List<ColumnMapping> columnMappingList) {
		this.columnMappingList = columnMappingList;
	}

	/**
	 * Entityのデータを格納するStorageSpaceを取得。
	 * StorageSpaceが異なる場合、物理データベース上では、
	 * そのStorageSpaceに紐付け定義されれいる別テーブルに格納される。
	 *
	 * @return
	 */
	public String getStorageSpace() {
		return storageSpace;
	}

	/**
	 * Entityのデータを格納するStorageSpaceを設定。
	 *
	 * @param storageSpace
	 */
	public void setStorageSpace(String storageSpace) {
		this.storageSpace = storageSpace;
	}

	/**
	 * ColumnMappingを追加する。
	 *
	 * @param columnMapping
	 */
	public void addColumnMapping(ColumnMapping columnMapping) {
		if (columnMappingList == null) {
			columnMappingList = new ArrayList<ColumnMapping>();
		}
		columnMappingList.add(columnMapping);
	}

}
