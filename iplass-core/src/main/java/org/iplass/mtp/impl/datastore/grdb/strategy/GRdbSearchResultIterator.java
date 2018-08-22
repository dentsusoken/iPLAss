/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.IdentityHashMap;
import java.util.List;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.datastore.RdbBaseValueTypeResolver;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
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
	private IdentityHashMap<ValueExpression, Integer> valIndexMap;
	
	public GRdbSearchResultIterator(ResultSet rs, EntityHandler dataModelHandler, EntityContext context, Query query, RdbAdapter rdb) {
		this.rs = rs;
		this.context = context;
		this.dataModelHandler = dataModelHandler;
		this.query = query;
		this.rdb = rdb;
		resolver = new RdbBaseValueTypeResolver(dataModelHandler, context, rdb);
		createIndexMap();
	}
	
	private void createIndexMap() {
		List<ValueExpression> select = query.getSelect().getSelectValues();
		valIndexMap = new IdentityHashMap<>(select.size());
		int res = 1;
		for (ValueExpression v: select) {
			valIndexMap.put(v, res);
			if (!(v instanceof EntityField)) {
				res += 1;
			} else {
				EntityField current = (EntityField) v;
				PropertyHandler ph = dataModelHandler.getPropertyCascade(current.getPropertyName(), context);
				GRdbPropertyStoreRuntime psh = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
				res += psh.getColCount();
			}
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
		
		int colNum = getIndex(propName);
		try {
			if (propName instanceof EntityField) {
				PropertyHandler ph = dataModelHandler.getPropertyCascade(((EntityField) propName).getPropertyName(), context);
				GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
				return colDef.fromDataStore(rs, colNum);
			} else {
				PropertyType type = resolver.resolve(propName);
				if (type == null) {
					return rs.getObject(colNum);
				} else {
					BaseRdbTypeAdapter adapter = (BaseRdbTypeAdapter) rdb.getRdbTypeAdapter(type);
					return adapter.fromDataStore(rs, colNum);
				}
			}
		} catch (SQLException e) {
			throw new EntityRuntimeException(propName + " can not convert.", e);
		}
	}
	

	private int getIndex(ValueExpression propName) {
		Integer index = valIndexMap.get(propName);
		if (index == null) {
			throw new EntityRuntimeException(propName + " is not contains select clause.");
		} else {
			return index;
		}
	}
	
	public boolean next() {
		try {
			return rs.next();
		} catch (Exception e) {
			throw new EntityRuntimeException(e);
		}
	}

}
