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

package org.iplass.mtp.impl.datastore.grdb.strategy.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.grdb.ColumnPosition;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.RawColIndexType;
import org.iplass.mtp.impl.datastore.grdb.RawColType;

public class ColContext {
	
	public static class ColCopy {
		IndexType indexType;
		ColConverter converter;
		int toPageNo;
		String toColName;
		int fromPageNo;
		String fromColName;
//		int toType;
//		int fromType;
		boolean setToNull;
		public ColCopy(ColConverter converter, int toPageNo, String toColName, /*int toType,*/
				int fromPageNo, String fromColName, /*int fromType,*/ IndexType indexType) {
			this.converter = converter;
			this.toPageNo = toPageNo;
			this.toColName = toColName;
			this.fromPageNo = fromPageNo;
			this.fromColName = fromColName;
//			this.toType = toType;
//			this.fromType = fromType;
			this.indexType = indexType;
		}
		public ColCopy(int toPageNo, String toColName, IndexType indexType, boolean setToNull) {
			this.toPageNo = toPageNo;
			this.toColName = toColName;
			this.indexType = indexType;
			this.setToNull = setToNull;
		}
		
		public boolean isSetToNull() {
			return setToNull;
		}
		public IndexType getIndexType() {
			return indexType;
		}
		public ColConverter getConverter() {
			return converter;
		}
		public int getToPageNo() {
			return toPageNo;
		}
		public String getToColName() {
			return toColName;
		}
//		public int getToType() {
//			return toType;
//		}
		public int getFromPageNo() {
			return fromPageNo;
		}
		public String getFromColName() {
			return fromColName;
		}
//		public int getFromType() {
//			return fromType;
//		}
	}

	private Map<Integer, List<ColCopy>> colParPage;
	
	public ColContext() {
	}
	
	public boolean hasColCopy() {
		return colParPage != null && colParPage.size() > 0;
	}
	
	public List<ColCopy> getColCopyList(int pageNo) {
		if (colParPage == null) {
			return null;
		}
		return colParPage.get(pageNo);
	}
	
	private void add(ColConverter converter, int toPageNo, String toColName, /*int toType,*/ int fromPageNo, String fromColName, /*int fromType,*/ IndexType indexType) {
		if (colParPage == null) {
			colParPage = new HashMap<>();
		}
		List<ColCopy> list = colParPage.get(toPageNo);
		if (list == null) {
			list = new ArrayList<>();
			colParPage.put(toPageNo, list);
		}
		list.add(new ColCopy(converter, toPageNo, toColName, /*toType,*/ fromPageNo, fromColName, /*fromType,*/ indexType));
	}
	
	public void addConvert(ColConverter converter, MetaGRdbPropertyStore toStore, MetaGRdbPropertyStore fromStore) {
		add(converter, toStore.getPageNo(), toStore.getColumnName(),
				fromStore.getPageNo(), fromStore.getColumnName(), null);
	}
	
	public void addIndexConvert(ColConverter converter, IndexType indexType, MetaGRdbPropertyStore toStore, MetaGRdbPropertyStore fromStore) {
		RawColType ct = RawColType.typeOf(converter.to);
		add(converter, toStore.getIndexPageNo(), ct.getIndexColNamePrefix(indexType) + toStore.getIndexColumnNo(),
				fromStore.getPageNo(), fromStore.getColumnName(), indexType);
	}
//	public void addIndex(PropertyType toType, IndexType indexType, MetaGRdbPropertyStore toStore) {
//		RawColType ct = RawColType.typeOf(toType);
//		add(ColConverter.NCC, toStore.getIndexPageNo(), ct.getIndexColNamePrefix(indexType) + toStore.getIndexColumnNo(),
//				toStore.getPageNo(), toStore.getColumnName(), indexType);
//	}
	
	public void addMove(ColumnPosition to, ColumnPosition from, RawColType ct, IndexType indexType) {
		RawColIndexType cit = RawColIndexType.typeOf(indexType);
		add(new ColConverter.NoneColConverter(null, null), to.getPageNo(), ct.getColNamePrefix(cit) + to.getColumnNo(), from.getPageNo(), ct.getColNamePrefix(cit) + from.getColumnNo(), indexType);
	}
	
	public void addSetNull(ColumnPosition to, RawColType ct, IndexType indexType) {
		if (colParPage == null) {
			colParPage = new HashMap<>();
		}
		List<ColCopy> list = colParPage.get(to.getPageNo());
		if (list == null) {
			list = new ArrayList<>();
			colParPage.put(to.getPageNo(), list);
		}
		RawColIndexType cit = RawColIndexType.typeOf(indexType);
		list.add(new ColCopy(to.getPageNo(), ct.getColNamePrefix(cit) + to.getColumnNo(), indexType, true));
	}
	
}
