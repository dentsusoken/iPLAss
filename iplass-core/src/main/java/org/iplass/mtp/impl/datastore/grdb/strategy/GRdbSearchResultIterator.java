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

package org.iplass.mtp.impl.datastore.grdb.strategy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.datastore.RdbBaseValueTypeResolver;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.strategy.SearchResultIterator;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GRdbSearchResultIterator implements SearchResultIterator {
	
	private ResultSet rs;
	private EntityHandler dataModelHandler;
	private EntityContext context;
	private Query query;
	private RdbAdapter rdb;
	private RdbBaseValueTypeResolver resolver;
	private IdentityHashMap<ValueExpression, SelectCol> colMap;
	private List<SelectCol> colList;
	
	private static class SelectCol {
		ValueExpression val;
		int colNum;
		GRdbPropertyStoreRuntime colDef;
		BaseRdbTypeAdapter adapter;
	}
	
	public GRdbSearchResultIterator(ResultSet rs, EntityHandler dataModelHandler, EntityContext context, Query query, RdbAdapter rdb) {
		this.rs = rs;
		this.context = context;
		this.dataModelHandler = dataModelHandler;
		this.query = query;
		this.rdb = rdb;
		resolver = new RdbBaseValueTypeResolver(dataModelHandler, context, rdb);
		createCol();
	}
	
	private void createCol() {
		List<ValueExpression> select = query.getSelect().getSelectValues();
		colMap = new IdentityHashMap<>(select.size());
		colList = new ArrayList<>(select.size());
		int res = 1;
		for (ValueExpression v: select) {
			SelectCol col = new SelectCol();
			col.val = v;
			col.colNum = res;
			
			if (!(v instanceof EntityField)) {
				PropertyType type = resolver.resolve(v);
				if (type != null) {
					col.adapter = (BaseRdbTypeAdapter) rdb.getRdbTypeAdapter(type);
				}
				res += 1;
			} else {
				PropertyHandler ph = dataModelHandler.getPropertyCascade(((EntityField) v).getPropertyName(), context);
				col.colDef = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
				if (col.colDef.isMulti() && ((EntityField) v).getArrayIndex() != EntityField.ARRAY_INDEX_UNSPECIFIED) {
					int arrayIndex = ((EntityField) v).getArrayIndex();
					List<GRdbPropertyStoreHandler> handlers = col.colDef.asList();
					if (arrayIndex < 0 || arrayIndex >= handlers.size()) {
						throw new EntityRuntimeException(
								"Array index " + arrayIndex + " is out of range. Property=" + ((EntityField) v).getPropertyName());
					}
					col.colDef = handlers.get(arrayIndex);
				}
				res += col.colDef.getColCount();
			}
			
			colMap.put(v, col);
			colList.add(col);
		}
	}
	
	public void close() {
		Statement stmt = null;
		Connection con = null;
		try {
			stmt = rs.getStatement();
			con = stmt.getConnection();
			rs.close();
		} catch (SQLException e) {
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Fail to close DB connection Resource. Check whether resource is leak or not.", e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger logger = LoggerFactory.getLogger(this.getClass());
					logger.error("Fail to close DB connection Resource. Check whether resource is leak or not.", e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					Logger logger = LoggerFactory.getLogger(this.getClass());
					logger.error("Fail to close DB connection Resource. Check whether resource is leak or not.", e);
				}
			}
		}
	}
	
	public Object getValue(ValueExpression propName) {
		
		SelectCol col = colMap.get(propName);
		if (col == null) {
			throw new EntityRuntimeException(propName + " is not contains select clause.");
		}
		
		return getValueImpl(col);
	}
	
	private Object getValueImpl(SelectCol col) {
		
		try {
			if (col.colDef != null) {
				return col.colDef.fromDataStore(rs, col.colNum);
			} else if (col.adapter != null) {
				return col.adapter.fromDataStore(rs, col.colNum, rdb);
			} else {
				return rs.getObject(col.colNum);
			}
		} catch (SQLException e) {
			throw new EntityRuntimeException(col.val + " can not convert.", e);
		}
	}

	@Override
	public Object getValue(int index) {
		return getValueImpl(colList.get(index));
	}

	public boolean next() {
		try {
			return rs.next();
		} catch (Exception e) {
			throw new EntityRuntimeException(e);
		}
	}

}
