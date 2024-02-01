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
package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.mapper;

import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbMultiplePropertyStore.GRdbMultiplePropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.PropertyColumnValue;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.bulk.ColumnValue;
import org.iplass.mtp.impl.rdb.adapter.bulk.DynamicColumnValue;

public class PropertyColumnValueMapper implements ColumnValueMapper {
	
	private int tenantId;
	private EntityHandler eh;
	private PrimitivePropertyHandler ph;
	private int pageNo;
	
	private String internalIndexKey;
	
	public PropertyColumnValueMapper(int tenantId, EntityHandler eh, PrimitivePropertyHandler ph, int pageNo) {
		this.tenantId = tenantId;
		this.eh = eh;
		this.ph = ph;
		this.pageNo = pageNo;
	}

	@Override
	public void columns(List<ColumnValue> columnValues, RdbAdapter rdb) {
		GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
		if (!colDef.isMulti()) {
			//本体
			GRdbPropertyStoreHandler scol = (GRdbPropertyStoreHandler) colDef;
			if (scol.getMetaData().getPageNo() == pageNo) {
				columnValues.add(new PropertyColumnValue(scol.getMetaData().getColumnName(), scol.getSingleColumnRdbTypeAdapter(), scol.isNative(), null, rdb));
			}
			//index
			if (!scol.isExternalIndex() && scol.getIndexColName() != null && scol.getMetaData().getIndexPageNo() == pageNo) {
				columnValues.add(new DynamicColumnValue(scol.getIndexColName() + ObjStoreTable.INDEX_TD_POSTFIX, rdb));
				columnValues.add(new PropertyColumnValue(scol.getIndexColName(), scol.getSingleColumnRdbTypeAdapter(), false, null, rdb));
				internalIndexKey = MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, eh.getMetaData().getId(), pageNo);
			}
		} else {
			//本体
			GRdbMultiplePropertyStoreHandler mcol = (GRdbMultiplePropertyStoreHandler) colDef;
			for (MetaGRdbPropertyStore metaCol: mcol.getMetaData().getStore()) {
				if (metaCol.getPageNo() == pageNo) {
					columnValues.add(new PropertyColumnValue(metaCol.getColumnName(), mcol.getSingleColumnRdbTypeAdapter(), mcol.isNative(), null, rdb));
				}
			}
			//TODO multiは現状index非対応
		}

	}

	@Override
	public void values(List<Object> values, Entity target, RdbAdapter rdb) {
		GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
		Object val = target.getValue(ph.getName());
		if (!colDef.isMulti()) {
			//本体
			GRdbPropertyStoreHandler scol = (GRdbPropertyStoreHandler) colDef;
			if (scol.getMetaData().getPageNo() == pageNo) {
				values.add(val);
			}
			//index
			if (!scol.isExternalIndex() && scol.getIndexColName() != null && scol.getMetaData().getIndexPageNo() == pageNo) {
				switch (scol.getIndexType()) {
				case UNIQUE:
					values.add(internalIndexKey);
					values.add(val);
					break;
				case UNIQUE_WITHOUT_NULL:
				case NON_UNIQUE:
					if (val != null) {
						values.add(internalIndexKey);
						values.add(val);
					} else {
						values.add(null);
						values.add(null);
					}
					break;
				default:
					break;
				}
			}
		} else {
			//本体
			GRdbMultiplePropertyStoreHandler mcol = (GRdbMultiplePropertyStoreHandler) colDef;
			for (int i = 0; i < mcol.getMetaData().getStore().size(); i++) {
				MetaGRdbPropertyStore metaCol = mcol.getMetaData().getStore().get(i);
				if (metaCol.getPageNo() == pageNo) {
					if (val instanceof Object[]) {
						Object[] valList = (Object[]) val;
						if (i < valList.length) {
							values.add(valList[i]);
						} else {
							values.add(null);
						}
					} else {
						if (i == 0) {
							values.add(val);
						} else {
							values.add(null);
						}
					}
				}
				//TODO multiは現状index非対応
			}
		}
	}

}
