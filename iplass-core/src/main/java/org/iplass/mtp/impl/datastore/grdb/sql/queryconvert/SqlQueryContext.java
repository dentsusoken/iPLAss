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

package org.iplass.mtp.impl.datastore.grdb.sql.queryconvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.impl.datastore.RdbBaseValueTypeResolver;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.ToSqlResult.BindValue;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.HintPlace;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.util.StringUtil;

public class SqlQueryContext {

	public enum Clause {
		SELECT,WHERE,ORDERBYGROUPBY,HINT,REFER
	}

	public static class QueryBindValue extends BindValue {
		public Clause clause;
		public boolean inIndexTable;

		public QueryBindValue(Object value, BaseRdbTypeAdapter type, Clause clause, boolean inIndexTable) {
			super(value, type);
			this.clause = clause;
			this.inIndexTable = inIndexTable;
		}
	}

	private String prefix;

	private RdbAdapter rdb;

	private EntityHandler primaryMetaData;
	private EntityContext metaContext;
	private JoinPath joinPath;
	private TableAliasMapping aliases;

	private List<String> indexTable;
	private boolean useIndexTable = true;

	private boolean enableCorrelation;

	private RdbBaseValueTypeResolver valueTypeResolver;

	private int limitCount = -1;
	private int limitOffset = -1;
	private boolean limitBind;

	private StringBuilder hintSb;
	private HashMap<String, StringBuilder> tableHintSbMap;
	private StringBuilder whereSb;
	private StringBuilder selectSb;
	private StringBuilder orderBySb;
	private Clause currentClause;

	private StringBuilder currentSb;

	private HashSet<String> indexHint;
	private HashSet<String> noIndexHint;

	private boolean useRollup;

	private boolean enableBindVariable;
	private List<BindValue> bindVariables;

	private SqlQueryContext parentContext;

	private boolean treatSelectAsRawValue;
	private Integer stringTypeLengthOnQuery;

//	private boolean isCurrentIndexedStack;

	//rootクエリ用
	public SqlQueryContext(EntityHandler primaryMetaData, EntityContext metaContext, RdbAdapter rdb) {
		this(primaryMetaData, metaContext, rdb, "q0", false);
	}

	//rootクエリ用(prefix未指定)
	public SqlQueryContext(EntityHandler primaryMetaData, EntityContext metaContext, RdbAdapter rdb, String prefix, boolean enableBindVariable) {
		this.primaryMetaData = primaryMetaData;
		this.metaContext = metaContext;
		this.rdb = rdb;
		this.prefix = prefix;
		aliases = new TableAliasMapping(prefix);
		hintSb = new StringBuilder();
		whereSb = new StringBuilder();
		selectSb = new StringBuilder();
		orderBySb = new StringBuilder();
		joinPath = new JoinPath(aliases, true, primaryMetaData, metaContext);
		this.enableBindVariable = enableBindVariable;
	}

	//サブクエリ用
	public SqlQueryContext(EntityHandler eh, SqlQueryContext parent, boolean treatSelectAsRawValue) {
		this(eh, parent.metaContext, parent.rdb, "q" + stackSize(parent), parent.enableBindVariable);
		this.parentContext = parent;
		this.treatSelectAsRawValue = treatSelectAsRawValue;
		this.stringTypeLengthOnQuery = parent.stringTypeLengthOnQuery;
	}

	public SqlQueryContext(EntityHandler primaryMetaData, EntityContext metaContext, RdbAdapter rdb, String prefix, TableAliasMapping aliases, JoinPath joinPath, List<String> indexTable, boolean enableBindVariable) {
		this.primaryMetaData = primaryMetaData;
		this.metaContext = metaContext;
		this.rdb = rdb;
		this.prefix = prefix;
		this.aliases = aliases;
		hintSb = new StringBuilder();
		whereSb = new StringBuilder();
		selectSb = new StringBuilder();
		orderBySb = new StringBuilder();
		this.joinPath = joinPath;
		this.indexTable = indexTable;
		this.enableBindVariable = enableBindVariable;
	}

	private static int stackSize(SqlQueryContext parent) {
		int ret = 0;
		while (parent != null) {
			ret++;
			parent = parent.parentContext;
		}
		return ret;
	}

	public Integer getStringTypeLengthOnQuery() {
		return stringTypeLengthOnQuery;
	}

	public void setStringTypeLengthOnQuery(Integer stringTypeLengthOnQuery) {
		this.stringTypeLengthOnQuery = stringTypeLengthOnQuery;
	}

	public boolean isTreatSelectAsRawValue() {
		return treatSelectAsRawValue;
	}

	public void setTreatSelectAsRawValue(boolean treatSelectAsRawValue) {
		this.treatSelectAsRawValue = treatSelectAsRawValue;
	}

	public SqlQueryContext getParentContext() {
		return parentContext;
	}

	public boolean isEnableCorrelation() {
		return enableCorrelation;
	}

	public void setEnableCorrelation(boolean enableCorrelation) {
		this.enableCorrelation = enableCorrelation;
	}

	public void setEnableBindVariable(boolean enableBindVariable) {
		this.enableBindVariable = enableBindVariable;
	}

	public boolean isEnableBindVariable() {
		return enableBindVariable;
	}

	public void addBindVariable(Object value, BaseRdbTypeAdapter type) {
		if (!enableBindVariable) {
			throw new QueryException("bind variable not supported.");
		}
		getBindVariables().add(new QueryBindValue(value, type, currentClause, false));
	}

	public List<BindValue> getBindVariables() {
		if (enableBindVariable && bindVariables == null) {
			bindVariables = new ArrayList<>();
		}
		return bindVariables;
	}

	public boolean isUseRollup() {
		return useRollup;
	}

	public void setUseRollup(boolean useRollup) {
		this.useRollup = useRollup;
	}

	boolean checkIndexHint(String prop, boolean external) {
		SqlQueryContext target = this;

		if (enableCorrelation) {
			int unnestCount = unnestCount(prop);

			//外部indexの場合、相関のプロパティ項目はindex利用しない
			if (external) {
				if (unnestCount != 0) {
					return false;
				}
			} else {
				target = parent(unnestCount);
				if (target == null) {
					throw new QueryException(prop + " undefined (can't unnest property).");
				}
				if (unnestCount > 0) {
					prop = prop.substring(unnestCount);
				}
			}
		}

		return checkIndexHint(target, prop);
	}

	private static boolean checkIndexHint(SqlQueryContext target, String prop) {
		if (target.indexHint != null) {
			return target.indexHint.contains(prop);
		}
		if (target.noIndexHint != null) {
			return !target.noIndexHint.contains(prop);
		}
		return true;
	}

	public void addIndexHint(String prop) {
		if (indexHint == null) {
			indexHint = new HashSet<>();
		}
		indexHint.add(prop);
	}

	public void addNoIndexHint(String prop) {
		if (noIndexHint == null) {
			noIndexHint = new HashSet<>();
		}
		noIndexHint.add(prop);
	}

	public boolean isUseIndexTable() {
		return useIndexTable;
	}

	public void setUseIndexTable(boolean useIndexTable) {
		this.useIndexTable = useIndexTable;
	}

	public RdbBaseValueTypeResolver getValueTypeResolver() {
		if (valueTypeResolver == null) {
			valueTypeResolver = new RdbBaseValueTypeResolver(primaryMetaData, metaContext, rdb);
		}
		return valueTypeResolver;
	}

	public String getPrefix() {
		return prefix;
	}


	public StringBuilder getCurrentSb() {
		return currentSb;
	}

	public List<String> getIndexTable() {
		return indexTable;
	}

	public void setIndexTable(List<String> indexTable) {
		this.indexTable = indexTable;
	}

	/**
	 *
	 * @param table
	 * @return テーブル別名
	 */
	public String addIndexTable(String table) {
		if (indexTable == null) {
			indexTable = new ArrayList<String>();
		}
		int index = indexTable.size();
		String talias = "i" + index;
		indexTable.add(table + " " + talias);
		return talias;
	}

	public void addTableHint(String tableName, String hint) {
		if (tableHintSbMap == null) {
			tableHintSbMap = new HashMap<>();
		}
		StringBuilder sb = tableHintSbMap.get(tableName);
		if (sb == null) {
			sb = new StringBuilder(hint);
			tableHintSbMap.put(tableName, sb);
		} else {
			sb.append(",");//TODO カンマ固定でよいか？
			sb.append(hint);
		}
	}

	void appendTableHint(String tableName, StringBuilder sb) {
		if (tableHintSbMap == null) {
			return;
		}
		if (!rdb.isSupportTableHint()) {
			return;
		}

		StringBuilder thsb = tableHintSbMap.get(tableName);
		if (thsb != null) {
			String[] bracket = rdb.getTableHintBracket();
			if (bracket != null && bracket[0] != null) {
				sb.append(bracket[0]);
			}

			sb.append(thsb);

			if (bracket != null && bracket[1] != null) {
				sb.append(bracket[1]);
			}
			sb.append(" ");
		}
	}

	private void appendHint(StringBuilder sb) {
		if (hintSb.length() != 0 || rdb.getOptimizerHint() != null) {
			String[] hintBracket = rdb.getOptimizerHintBracket();
			if (hintBracket != null && hintBracket[0] != null) {
				sb.append(hintBracket[0]);
			}
			if (hintSb.length() != 0) {
				sb.append(hintSb);
			} else {
				sb.append(rdb.getOptimizerHint());
			}
			if (hintBracket != null && hintBracket[1] != null) {
				sb.append(hintBracket[1]);
			}
		}
	}

	public List<BindValue> toOrderedBindVariables(boolean withJoin) {
		List<BindValue> joinClauseBindVariables = null;
		if (withJoin) {
			joinClauseBindVariables = joinPath.getOrderedBindVariables();
		}

		if ((bindVariables == null || bindVariables.size() == 0)
				&& (joinClauseBindVariables == null || joinClauseBindVariables.size() == 0)) {
			//バインド変数がない場合

			if (getLimitCount() != -1 && enableBindVariable && limitBind) {
				int limitOffset = 0;
				if (getLimitOffset() != -1) {
					limitOffset = getLimitOffset();
				}
				List<BindValue> limited = new ArrayList<>(2);
				Object[] limitBindValues = rdb.toLimitSqlBindValue(getLimitCount(), limitOffset);
				for (Object v: limitBindValues) {
					limited.add(new QueryBindValue(v, rdb.getRdbTypeAdapter(v), Clause.ORDERBYGROUPBY, false));
				}
				return limited;
			} else {
				return bindVariables;
			}
		}

		if (joinClauseBindVariables != null || indexTable != null && indexTable.size() > 0) {
			//joinでの結合条件、indexテーブル追加によるバインド変数順の組み換えが発生している
			List<BindValue> ordered;
			if (bindVariables == null) {
				ordered = new ArrayList<>();
			} else {
				ordered = new ArrayList<>(bindVariables.size() + 10);
			}
			//join,indextable以前のバインド変数追加
			if (bindVariables != null) {
				for (BindValue b: bindVariables) {
					if (((QueryBindValue) b).clause != Clause.SELECT) {
						break;
					}
					if (!((QueryBindValue) b).inIndexTable) {
						ordered.add(b);
					}
				}
			}
			//join追加
			if (joinClauseBindVariables != null) {
				for (BindValue b: joinClauseBindVariables) {
					ordered.add(b);
				}
			}
			if (bindVariables != null) {
				//index追加
				for (BindValue b: bindVariables) {
					if (((QueryBindValue) b).inIndexTable) {
						ordered.add(b);
					}
				}
				//それ以外追加
				for (BindValue b: bindVariables) {
					if (((QueryBindValue) b).clause != Clause.SELECT) {
						if (!((QueryBindValue) b).inIndexTable) {
							ordered.add(b);
						}
					}
				}
			}
			//limit
			if (getLimitCount() != -1 && enableBindVariable && limitBind) {
				int limitOffset = 0;
				if (getLimitOffset() != -1) {
					limitOffset = getLimitOffset();
				}
				Object[] limitBindValues = rdb.toLimitSqlBindValue(getLimitCount(), limitOffset);
				for (Object v: limitBindValues) {
					ordered.add(new QueryBindValue(v, rdb.getRdbTypeAdapter(v), Clause.ORDERBYGROUPBY, false));
				}
			}

			return ordered;
		} else {
			//組み換え発生なし
			if (getLimitCount() != -1 && enableBindVariable && limitBind) {
				int limitOffset = 0;
				if (getLimitOffset() != -1) {
					limitOffset = getLimitOffset();
				}
				List<BindValue> limited = new ArrayList<>(2 + bindVariables.size());
				limited.addAll(bindVariables);
				Object[] limitBindValues = rdb.toLimitSqlBindValue(getLimitCount(), limitOffset);
				for (Object v: limitBindValues) {
					limited.add(new QueryBindValue(v, rdb.getRdbTypeAdapter(v), Clause.ORDERBYGROUPBY, false));
				}
				return limited;
			} else {
				return bindVariables;
			}
		}
	}

	public String toSelectSql() {
		return toSelectSql(null);
	}

	public String toSelectSql(String additionalJoin) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		if (rdb.isSupportOptimizerHint() && rdb.getOptimizerHintPlace() == HintPlace.AFTER_SELECT) {
			appendHint(sb);
			sb.append(" ");
		}
		sb.append(selectSb);
		sb.append(" FROM ");
		sb.append(((GRdbEntityStoreRuntime) primaryMetaData.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" ");
		if (getPrefix() != null) {
			sb.append(getPrefix()).append(" ");
			appendTableHint(getPrefix(), sb);
		}
		joinPath.appendJoinCause(sb, this);

		if (indexTable != null) {
			for (String t: indexTable) {
				sb.append(",");
				sb.append(t);
			}
		}

		sb.append(" WHERE ");
		if (getPrefix() != null) {
			sb.append(getPrefix()).append(".");
		}
		sb.append("OBJ_DEF_ID='").append(rdb.sanitize(primaryMetaData.getMetaData().getId()));
		sb.append("' AND ");
		if (getPrefix() != null) {
			sb.append(getPrefix()).append(".");
		}
		sb.append("TENANT_ID=").append(metaContext.getTenantId(primaryMetaData));
		sb.append(" AND ");
		if (getPrefix() != null) {
			sb.append(getPrefix()).append(".");
		}
		sb.append(ObjStoreTable.PG_NO + "=0");
//		sb.append(" AND ");
//		if (getPrefix() != null) {
//			sb.append(getPrefix()).append(".");
//		}
//		sb.append("STATUS='V'");

		if (StringUtil.isNotEmpty(additionalJoin)) {
			sb.append(" AND (").append(additionalJoin).append(")");
		}

		if (whereSb.length() != 0) {
			sb.append(" AND (");
			sb.append(whereSb);
			sb.append(")");
		}

		if (orderBySb.length() != 0) {
			sb.append(orderBySb);
		}

		CharSequence sql = null;
		//Limit対応
		if (getLimitCount() != -1) {
			int limitOffset = 0;
			if (getLimitOffset() != -1) {
				limitOffset = getLimitOffset();
			}
			sql = rdb.toLimitSql(sb.toString(), getLimitCount(), limitOffset, enableBindVariable && limitBind);
		} else {
			sql = sb;
		}

		if (rdb.isSupportOptimizerHint() && rdb.getOptimizerHintPlace() == HintPlace.HEAD_OF_SQL) {
			StringBuilder sb2 = new StringBuilder();
			appendHint(sb2);
			sb2.append(" ");
			sb2.append(sql);
			sql = sb2;
		}

		return sql.toString();
	}

//	public int getTenantId() {
//		return tenantId;
//	}

	public RdbAdapter getRdb() {
		return rdb;
	}

	public void setFrom(String entityName) {
		if (primaryMetaData == null ||
				!primaryMetaData.getMetaData().getName().equals(entityName)) {
			EntityHandler e = metaContext.getHandlerByName(entityName);

			if (e == null) {
				throw new QueryException(entityName + " undefined.");
			}
			primaryMetaData = e;
		}
	}

	public void changeCurrentClause(Clause currentClause) {
		this.currentClause = currentClause;
		switch (currentClause) {
		case SELECT:
			currentSb = selectSb;
			break;
		case WHERE:
			currentSb = whereSb;
			break;
		case ORDERBYGROUPBY:
			currentSb = orderBySb;
			break;
		case HINT:
			currentSb = hintSb;
			break;
		default:
			throw new UnsupportedOperationException("no impl of " + currentClause);
		}
	}

	public Clause getCurrentClause() {
		return currentClause;
	}

	public SqlQueryContext append(CharSequence charSeq) {
		currentSb.append(charSeq);
		return this;
	}

	public SqlQueryContext append(int intValue) {
		currentSb.append(intValue);
		return this;
	}

	public JoinPath getJoinPath() {
		return joinPath;
	}

	public TableAliasMapping getAliases() {
		return aliases;
	}

	public EntityContext getMetaContext() {
		return metaContext;
	}

	public EntityHandler getFromEntity() {
		return primaryMetaData;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	public int getLimitOffset() {
		return limitOffset;
	}

	public void setLimitOffset(int limitOffset) {
		this.limitOffset = limitOffset;
	}

	public void setLimitBind(boolean limitBind) {
		this.limitBind = limitBind;
	}

	private static int unnestCount(String propName) {
		for (int i = 0; i < propName.length(); i++) {
			if (propName.charAt(i) != '.') {
				return i;
			}
		}
		return 0;
	}

	private SqlQueryContext parent(int unnestCount) {
		SqlQueryContext ret = this;
		for (int i = 0; i < unnestCount; i++) {
			if (parentContext == null) {
				return null;
			}
			ret = parentContext;
		}
		return ret;
	}

	public PropertyHandler getProperty(String propName) {
		SqlQueryContext target = this;
		if (enableCorrelation) {
			int unnestCount = unnestCount(propName);
			if (unnestCount != 0) {
				target = parent(unnestCount);
				if (target == null) {
					throw new QueryException(propName + " undefined (can't unnest property).");
				}
				propName = propName.substring(unnestCount);
			}
		}

		return target.getFromEntity().getPropertyCascade(propName, getMetaContext());
	}

	public void notifyUsedPropertyName(String propertyName) {
		SqlQueryContext target = this;
		if (enableCorrelation) {
			int unnestCount = unnestCount(propertyName);
			if (unnestCount != 0) {
				target = parent(unnestCount);
				if (target == null) {
					throw new QueryException(propertyName + " undefined (can't unnest property).");
				}
				propertyName = propertyName.substring(unnestCount);
			}
		}
//		if (propertyName.contains(".")) {
			boolean useIndex = checkIndexHint(target, propertyName);
			target.joinPath.addPath(propertyName.split("[.]"), -1, metaContext, target.primaryMetaData, target.currentClause == Clause.WHERE, useIndex);
//		}
	}

	public String getColPrefix(String propName, GRdbPropertyStoreHandler targetCol) {
		SqlQueryContext target = this;
		if (enableCorrelation) {
			int unnestCount = unnestCount(propName);
			if (unnestCount != 0) {
				target = parent(unnestCount);
				if (target == null) {
					throw new QueryException(propName + " undefined (can't unnest property).");
				}
				propName = propName.substring(unnestCount);
			}
		}

//		GRdbPropertyStoreHandler targetCol;
//		if (col.isMulti()) {
//			targetCol = col.asList().get(index);
//		} else {
//			targetCol = (GRdbPropertyStoreHandler) col;
//		}
//
		StringBuilder sb = new StringBuilder();

		if (propName.contains(".")) {
			sb.append(target.aliases.getAlias(propName.substring(0, propName.lastIndexOf('.'))));
		} else if (target.getPrefix() != null) {
			sb.append(target.getPrefix());
		}

		if (!targetCol.isNative()) {
			if (targetCol.getIndexColName() != null && checkIndexHint(target, propName)) {
				if (targetCol.getMetaData().getIndexPageNo() > 0) {
					sb.append(JoinPath.PAGE_PREFIX).append(targetCol.getMetaData().getIndexPageNo());
				}
			} else {
				if (targetCol.getMetaData().getPageNo() > 0) {
					sb.append(JoinPath.PAGE_PREFIX).append(targetCol.getMetaData().getPageNo());
				}
			}
		}

		if (sb.length() > 0) {
			sb.append(".");
		}
		return sb.toString();
	}

}
