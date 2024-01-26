/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.Serializable;

/**
 * Entityのpropertyを物理テーブルの特定のカラムに直接マッピングする場合に利用する定義のクラス。
 * 当該のカラムは、事前に直接RDBのテーブル上に追加されている必要がある。
 * このマッピングが定義された場合は、このプロパティの値は直接そのカラムに格納される。
 * 定義されるカラムの型、制約はプロパティに合わせて適切なものである必要がある。
 * 
 * @author K.Higuchi
 *
 */
public class ColumnMapping implements Serializable {
	private static final long serialVersionUID = 3409874259116874619L;
	
	private String propertyName;
	private String columnName;
	
	public ColumnMapping() {
	}
	
	public ColumnMapping(String propertyName, String columnName) {
		this.propertyName = propertyName;
		this.columnName = columnName;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}
