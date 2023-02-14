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

package org.iplass.mtp.impl.datastore.grdb.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.GroupBy;
import org.iplass.mtp.entity.query.Having;
import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.hint.BindHint;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.NoBindHint;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.entity.query.value.window.PartitionBy;
import org.iplass.mtp.entity.query.value.window.WindowOrderBy;
import org.iplass.mtp.entity.query.value.window.WindowSortSpec;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlConverter;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlQueryContext;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjStoreSearchSql extends QuerySqlHandler {

	private static Logger logger = LoggerFactory.getLogger(ObjStoreSearchSql.class);

	public ToSqlResult query(EntityHandler metaData,
			EntityContext context,
			Query query, boolean withLock, boolean treatBindHint, Integer stringTypeLengthOnQuery, RdbAdapter rdbAdaptor) {

		long time = 0;
		if (logger.isTraceEnabled()) {
			time = System.nanoTime();
		}
		try {
			//TODO SqlContextと、ExpressionSqlConverterの統合（SqlContextのExpressionSqlConverter内部への隠蔽化）
//			SqlQueryContext sqlContext = new SqlQueryContext(tenantId, metaData, context, rdbAdaptor);
			SqlQueryContext sqlContext = new SqlQueryContext(metaData, context, rdbAdaptor);
			if (stringTypeLengthOnQuery != null && stringTypeLengthOnQuery > 0) {
				sqlContext.setStringTypeLengthOnQuery(stringTypeLengthOnQuery);
			}
			SqlConverter converter = new SqlConverter(sqlContext, treatBindHint);
			if (rdbAdaptor.isAlwaysBind()) {
				//Oracleでは、GroupBy,PartitionBY,WindowGroupByにバインド変数利用できない為、、
				GroupByNoBinder noBinder = new GroupByNoBinder();
				noBinder.modify(query);
				boolean hasNoBindHint = false;
				if (query.select().getHintComment() != null
						&& query.select().getHintComment().getHintList() != null) {
					for (Hint h: query.select().getHintComment().getHintList()) {
						if (h instanceof NoBindHint) {
							hasNoBindHint = true;
							break;
						}
					}
				}
				if (!hasNoBindHint) {
					query.select().hintComment().add(new BindHint());
				}
			}
			query.accept(converter);
			if (withLock) {
				return new ToSqlResult(rdbAdaptor.createRowLockSql(sqlContext.toSelectSql()), sqlContext.toOrderedBindVariables(true));
			} else {
				return new ToSqlResult(sqlContext.toSelectSql(), sqlContext.toOrderedBindVariables(true));
			}

		} finally {
			if (logger.isTraceEnabled()) {
				logger.trace("translate EQL to SQL:time=" + ((double) (System.nanoTime() - time)/1000000) + "ms.");
			}
		}
	}

	private static class GroupByCollector extends QueryVisitorSupport {
		Set<ValueExpression> groupBySet;

		private void initSet() {
			if (groupBySet == null) {
				groupBySet = new HashSet<>();
			}
		}

		@Override
		public boolean visit(GroupBy groupBy) {
			if (groupBy.getGroupingFieldList() != null) {
				initSet();
				for (ValueExpression ve: groupBy.getGroupingFieldList()) {
					groupBySet.add(ve);
				}
			}

			return false;
		}

		@Override
		public boolean visit(SubQuery subQuery) {
			return false;
		}

		@Override
		public boolean visit(PartitionBy partitionBy) {
			if (partitionBy.getPartitionFieldList() != null) {
				initSet();
				for (ValueExpression ve: partitionBy.getPartitionFieldList()) {
					groupBySet.add(ve);
				}
			}
			return false;
		}

		@Override
		public boolean visit(WindowSortSpec sortSpec) {
			if (sortSpec.getSortKey() != null) {
				initSet();
				groupBySet.add(sortSpec.getSortKey());
			}
			return false;
		}

	}

	private static class GroupByNoBinder extends QueryVisitorSupport {
		GroupByCollector collector;
		boolean noBind;
		ArrayList<Literal> target;

		public void modify(Query query) {
			target = new ArrayList<>();
			query.accept(this);
			for (Literal l: target) {
				l.setBindable(false);
			}
		}

		@Override
		public boolean visit(Query query) {
			collector = new GroupByCollector();
			query.accept(collector);
			return true;
		}

		@Override
		public boolean visit(SubQuery subQuery) {

			GroupByCollector prevCollector = collector;

			subQuery.getQuery().accept(this);

			if (subQuery.getOn() != null) {
				subQuery.getOn().accept(this);
			}

			collector = prevCollector;

			return false;
		}

		@Override
		public boolean visit(OrderBy orderBy) {
			for (SortSpec ss: orderBy.getSortSpecList()) {
				boolean prevNoBind = noBind;
				if (collector.groupBySet != null
						&& collector.groupBySet.contains(ss.getSortKey())) {
					noBind = true;
				}
				ss.getSortKey().accept(this);
				noBind = prevNoBind;
			}
			return false;
		}

		@Override
		public boolean visit(Having having) {
			boolean prevNoBind = noBind;
			noBind = true;

			if (having.getCondition() != null) {
				having.getCondition().accept(this);
			}

			noBind = prevNoBind;
			return false;
		}

		@Override
		public boolean visit(Select select) {
			for (ValueExpression ve: select.getSelectValues()) {
				boolean prevNoBind = noBind;
				if (collector.groupBySet != null
						&& collector.groupBySet.contains(ve)) {
					noBind = true;
				}
				ve.accept(this);
				noBind = prevNoBind;
			}
			return false;
		}

		@Override
		public boolean visit(GroupBy groupBy) {
			boolean prevNoBind = noBind;
			noBind = true;

			if (groupBy.getGroupingFieldList() != null) {
				for (ValueExpression ve: groupBy.getGroupingFieldList()) {
					ve.accept(this);
				}
			}

			noBind = prevNoBind;

			return false;
		}

		@Override
		public boolean visit(PartitionBy partitionBy) {
			boolean prevNoBind = noBind;
			noBind = true;

			if (partitionBy.getPartitionFieldList() != null) {
				for (ValueExpression ve: partitionBy.getPartitionFieldList()) {
					ve.accept(this);
				}
			}

			noBind = prevNoBind;

			return false;
		}

		@Override
		public boolean visit(WindowOrderBy orderBy) {
			boolean prevNoBind = noBind;
			noBind = true;

			if (orderBy.getSortSpecList() != null) {
				for (WindowSortSpec wss: orderBy.getSortSpecList()) {
					wss.accept(this);
				}
			}

			noBind = prevNoBind;

			return false;
		}

		@Override
		public boolean visit(Literal literal) {
			if (noBind) {
				target.add(literal);
			}
			return true;
		}
	}

	public ToSqlResult count(EntityHandler metaData,
			EntityContext context,
			Query query, boolean enableBindVariable, RdbAdapter rdbAdaptor) {

		Query subQuery = query.copy();
		subQuery.setOrderBy(null);
		
		UnaryOperator<CharSequence> sqlModifiier = rdbAdaptor.countQuery(subQuery);

		ToSqlResult res = query(metaData, context, subQuery, false, enableBindVariable, null, rdbAdaptor);
		res.sql = sqlModifiier.apply(res.sql).toString();
		return res;
	}


	public String checkExistsSql(int tenantId, EntityHandler eh, Entity entity, RdbAdapter rdbAdaptor) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT 1 FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=? AND " + ObjStoreTable.OBJ_DEF_ID
				+ "=? AND " + ObjStoreTable.OBJ_ID + "=? AND " + ObjStoreTable.OBJ_VER + "=? AND " + ObjStoreTable.PG_NO + "=0");
		return sb.toString();
	}

	public void checkExistsParameter(PreparedStatement stmt, int tenantId, EntityHandler eh, Entity entity) throws SQLException {
		stmt.setInt(1, tenantId);
		stmt.setString(2, eh.getMetaData().getId());
		stmt.setString(3, entity.getOid());
		if (entity.getVersion() != null) {
			stmt.setLong(4, entity.getVersion());
		} else {
			stmt.setLong(4, 0L);
		}
	}

	public static String checkExistsByKeysSql(int tenantId, EntityHandler eh, List<Object[]> keys, RdbAdapter rdbAdaptor) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT " + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + " FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId())).append("'");
		if (rdbAdaptor.isSupportRowValueConstructor()) {
			sb.append(" AND (" + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + "," + ObjStoreTable.PG_NO + ") IN (");
			for (int i = 0; i < keys.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append("('").append(rdbAdaptor.sanitize((String) keys.get(i)[0])).append("',").append(keys.get(i)[1]).append(",0)");
			}
			sb.append(")");
		} else {
			sb.append(" AND (");
			for (int i = 0; i < keys.size(); i++) {
				if (i != 0) {
					sb.append(" OR ");
				}
				sb.append("(");
				sb.append(ObjStoreTable.OBJ_ID).append("='").append(rdbAdaptor.sanitize((String) keys.get(i)[0])).append("'");
				sb.append(" AND ");
				sb.append(ObjStoreTable.OBJ_VER).append("=").append(keys.get(i)[1]);
				sb.append(" AND ");
				sb.append(ObjStoreTable.PG_NO).append("=0");
				sb.append(")");
			}
			sb.append(")");
		}

		return sb.toString();
	}

}
