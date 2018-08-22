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

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateCondition.UpdateValue;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.hint.IndexHint;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlConverter;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlQueryContext;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlQueryContext.Clause;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.properties.extend.VirtualType;
import org.iplass.mtp.impl.rdb.adapter.MultiTableUpdateMethod;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class ObjStoreUpdateSql extends UpdateSqlHandler {

	private static final String TMP_TABLE_ALIAS = "tt";

	public static boolean canUpdateProperty(PropertyHandler pHandler) {
		if (pHandler instanceof ReferencePropertyHandler) {
			return false;
		}
		PrimitivePropertyHandler pph = (PrimitivePropertyHandler) pHandler;
		if (pph.getMetaData().getType() instanceof VirtualType) {
			return false;
		}
		String propName = pHandler.getName();
		if (propName.equals(Entity.OID)
				|| propName.equals(Entity.UPDATE_DATE)//一律更新
				|| propName.equals(Entity.VERSION)
				) {
			return false;
		}
		return true;
	}

	private Object getValueAt(int i, Object value) {
		if (value instanceof Object[]) {
			if (i < ((Object[]) value).length) {
				return ((Object[]) value)[i];
			} else {
				return null;
			}
		} else {
			if (i == 0) {
				return value;
			} else {
				return null;
			}
		}
	}


	private boolean handlePropAndVal(StringBuilder sb, int tenantId, EntityHandler eh, Entity model, List<String> updatePropNames, int pageNo, RdbAdapter rdbAdaptor, EntityContext context) {
		boolean added = false;
		for (String propName: updatePropNames) {
			PropertyHandler pHandler = eh.getProperty(propName, context);
			if (canUpdateProperty(pHandler)) {
				GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) pHandler.getStoreSpecProperty();
				Object val = model.getValue(propName);
				List<GRdbPropertyStoreHandler> cols = col.asList();
				for (int i = 0; i < cols.size(); i++) {
					GRdbPropertyStoreHandler tcol = cols.get(i);
					Object valToSet = getValueAt(i, val);
					//normal col
					if (tcol.getMetaData().getPageNo() == pageNo) {
						if (added) {
							sb.append(",");
						} else {
							added = true;
						}
						sb.append(tcol.getMetaData().getColumnName());
						sb.append("=");
						if (col.isNative()) {
							col.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(valToSet, sb, rdbAdaptor);
						} else {
							col.getSingleColumnRdbTypeAdapter().appendToTypedCol(sb, rdbAdaptor,
									() -> col.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(valToSet, sb, rdbAdaptor));
						}
					}

					//index col
					if (tcol.getIndexColName() != null && tcol.getMetaData().getIndexPageNo() == pageNo) {
						if (added) {
							sb.append(",");
						} else {
							added = true;
						}
						switch (tcol.getIndexType()) {
						case UNIQUE:
							sb.append(tcol.getIndexColName()).append(ObjStoreTable.INDEX_TD_POSTFIX);
							sb.append("=");
							sb.append("'");
							sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdbAdaptor.sanitize(pHandler.getParent().getMetaData().getId()), pageNo));
							sb.append("',");
							sb.append(tcol.getIndexColName());
							sb.append("=");
							col.getSingleColumnRdbTypeAdapter().appendToTypedCol(sb, rdbAdaptor,
									() -> col.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(valToSet, sb, rdbAdaptor));
							break;
						case UNIQUE_WITHOUT_NULL:
						case NON_UNIQUE:
							if (valToSet != null) {
								sb.append(tcol.getIndexColName()).append(ObjStoreTable.INDEX_TD_POSTFIX);
								sb.append("=");
								sb.append("'");
								sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdbAdaptor.sanitize(pHandler.getParent().getMetaData().getId()), pageNo));
								sb.append("',");
								sb.append(tcol.getIndexColName());
								sb.append("=");
								col.getSingleColumnRdbTypeAdapter().appendToTypedCol(sb, rdbAdaptor,
										() -> col.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(valToSet, sb, rdbAdaptor));
							} else {
								sb.append(tcol.getIndexColName()).append(ObjStoreTable.INDEX_TD_POSTFIX);
								sb.append("=null,");
								sb.append(tcol.getIndexColName());
								sb.append("=null");
							}
							break;
						default:
							break;
						}
					}
				}
			}
		}

		return added;
	}

	//単一Entity更新,page=0
	public String updateMain(int tenantId, EntityHandler eh,
			Entity model, boolean withTimestampCheck, List<String> updatePropNames, RdbAdapter rdbAdaptor, EntityContext context) {
		
		Timestamp updateDate = model.getUpdateDate();
		if (withTimestampCheck && updateDate == null) {
			throw new NullPointerException("for optimistic lock check, updateDate property value must specify, but null.");
		}
		
		int pageNo = 0;

		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" SET ");

		boolean isAdded = handlePropAndVal(sb, tenantId, eh, model, updatePropNames, pageNo, rdbAdaptor, context);

		if (isAdded) {
			sb.append(",");
		}

		//FIXME updateの際にはupdateByをupdatePropertiesに頼っているが、一括更新の場合は、自動セット。整合性を取る！

		//更新日
		sb.append(ObjStoreTable.UP_DATE + "=");
		if (withTimestampCheck) {
			//同じタイムスタンプ値で更新しないように。必ず1以上インクリメントされるように
			long currentTime = System.currentTimeMillis();
			if (updateDate.getTime() >= currentTime) {
				currentTime = updateDate.getTime() + 1;
			}
			sb.append(rdbAdaptor.toTimeStampExpression(new Timestamp(currentTime)));
		} else {
			sb.append(rdbAdaptor.systimestamp());
		}

		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjStoreTable.OBJ_ID + "='").append(rdbAdaptor.sanitize(model.getOid())).append("'");
		if (model.getVersion() != null) {
			sb.append(" AND " + ObjStoreTable.OBJ_VER + "=").append(model.getVersion());
		} else {
			sb.append(" AND " + ObjStoreTable.OBJ_VER + "=0");
		}
		sb.append(" AND " + ObjStoreTable.PG_NO + "=").append(pageNo);
		//新しい定義でデータ洗い替えが発生している場合は、更新しない
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_VER + "<=").append(((MetaGRdbEntityStore) eh.getEntityStoreRuntime().getMetaData()).getVersion());
		if (withTimestampCheck) {
			sb.append(" AND " + ObjStoreTable.UP_DATE + "=").append(rdbAdaptor.toTimeStampExpression(updateDate));
		}

		return sb.toString();
	}

	//単一Entity更新,page1以降
	public String updateSub(int tenantId, EntityHandler eh,
			Entity model, List<String> updatePropNames, int pageNo, RdbAdapter rdbAdaptor, EntityContext context) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" SET ");

		boolean isAdded = handlePropAndVal(sb, tenantId, eh, model, updatePropNames, pageNo, rdbAdaptor, context);

		if (!isAdded) {
			return null;
		}

		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjStoreTable.OBJ_ID + "='").append(rdbAdaptor.sanitize(model.getOid())).append("'");
		if (model.getVersion() != null) {
			sb.append(" AND " + ObjStoreTable.OBJ_VER + "=").append(model.getVersion());
		} else {
			sb.append(" AND " + ObjStoreTable.OBJ_VER + "=0");
		}
		sb.append(" AND " + ObjStoreTable.PG_NO + "=").append(pageNo);

		return sb.toString();
	}


	private String valueExp(ValueExpression val, EntityHandler eh, GRdbPropertyStoreRuntime col, int pageNo, RdbAdapter rdbAdaptor, EntityContext context) {
		String valStr = null;
		if (val == null) {
			valStr ="null";
		} else if (val instanceof Literal) {
			//指定されている値がリテラル値の場合は型チェックするため、colのTypeAdapterを利用
			Object literalValue = ((Literal) val).getValue();
			if (literalValue == null) {
				valStr ="null";
			} else {
				StringBuilder vsb = new StringBuilder();
				if (col.isNative()) {
					col.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(literalValue, vsb, rdbAdaptor);
				} else {
					col.getSingleColumnRdbTypeAdapter().appendToTypedCol(vsb, rdbAdaptor,
							() -> col.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(literalValue, vsb, rdbAdaptor));
				}
				valStr = vsb.toString();
			}
		} else {
			//TODO ValueExpression表現の型チェックがあった方がよい。。

			//複数ページに跨るかチェック
			HasOtherPageChecker chk = new HasOtherPageChecker(pageNo, eh, context);
			val.accept(chk);
			if (chk.hasOtherPage && rdbAdaptor.getMultiTableUpdateMethod() != MultiTableUpdateMethod.DIRECT_JOIN) {
				//相関サブクエリで別page結合
				SqlQueryContext qc = new SqlQueryContext(eh, context, rdbAdaptor, "u", false);
				//qc.setTreatSelectAsRawValue(true);
				SqlConverter conv = new SqlConverter(qc, false);
				Query q = new Query()
						.select(val).from(eh.getMetaData().getName());
				//index利用しないように
				q.select().addHint(new IndexHint(Entity.NAME));
				q.accept(conv);
				StringBuilder vsb = new StringBuilder();
				vsb.append("(");
				vsb.append(qc.toSelectSql());
				//相関項目
				String mainTable = tableNameAlias(pageNo);
				vsb.append(" AND ").append(mainTable).append("." + ObjStoreTable.OBJ_ID + "=u." + ObjStoreTable.OBJ_ID);
				vsb.append(" AND ").append(mainTable).append("." + ObjStoreTable.OBJ_VER + "=u." + ObjStoreTable.OBJ_VER);
				vsb.append(")");
				
				if (col.isNative()) {
					valStr = vsb.toString();
				} else {
					StringBuilder vsb2 = new StringBuilder();
					col.getSingleColumnRdbTypeAdapter().appendToTypedCol(vsb2, rdbAdaptor,
							() -> vsb2.append(vsb));
					valStr = vsb2.toString();
				}
			} else {
				//他のpageを結合せずともOK
				SqlQueryContext qc = new SqlQueryContext(eh, context, rdbAdaptor, "m", false);
				qc.changeCurrentClause(Clause.SELECT);
				SqlConverter conv = new SqlConverter(qc, false);
				//index利用しないように
				new IndexHint(Entity.NAME).accept(conv);
				val.accept(conv);
				if (col.isNative()) {
					valStr = qc.getCurrentSb().toString();
				} else {
					StringBuilder vsb = new StringBuilder();
					col.getSingleColumnRdbTypeAdapter().appendToTypedCol(vsb, rdbAdaptor,
							() -> vsb.append(qc.getCurrentSb().toString()));
					valStr = vsb.toString();
				}
			}
		}
		return valStr;
	}


	private boolean handlePropAndValforUpdateAll(int tenantId, StringBuilder sb, EntityHandler eh, List<UpdateValue> values, int pageNo, RdbAdapter rdbAdaptor, EntityContext context) {
		boolean added = false;
		boolean needTableAlias = rdbAdaptor.getMultiTableUpdateMethod() == MultiTableUpdateMethod.DIRECT_JOIN;
		String mainTable = tableNameAlias(pageNo);
		for (UpdateValue uv: values) {
			PropertyHandler pHandler = eh.getProperty(uv.getEntityField(), context);
			if (canUpdateProperty(pHandler)) {
				GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) pHandler.getStoreSpecProperty();
				ValueExpression val = uv.getValue();

				List<GRdbPropertyStoreHandler> cols = col.asList();
				for (int i = 0; i < cols.size(); i++) {
					GRdbPropertyStoreHandler tcol = cols.get(i);
					String valueStr = null;
					//normal col
					if (tcol.getMetaData().getPageNo() == pageNo) {
						if (added) {
							sb.append(",");
						} else {
							added = true;
						}

						if (needTableAlias) {
							sb.append(mainTable).append(".");
						}
						sb.append(tcol.getMetaData().getColumnName());
						sb.append("=");

						if (valueStr == null) {
							valueStr = valueExp(val, eh, tcol, pageNo, rdbAdaptor, context);
						}
						sb.append(valueStr);
					}

					//index col
					if (!tcol.isExternalIndex() && tcol.getIndexColName() != null && tcol.getMetaData().getIndexPageNo() == pageNo) {
						if (added) {
							sb.append(",");
						} else {
							added = true;
						}
						if (needTableAlias) {
							sb.append(mainTable).append(".");
						}
						sb.append(tcol.getIndexColName());
						sb.append("=");

						if (valueStr == null) {
							valueStr = valueExp(val, eh, tcol, pageNo, rdbAdaptor, context);
						}
						sb.append(valueStr);

						// _TD
						sb.append(",");
						switch (tcol.getIndexType()) {
						case UNIQUE:
							if (needTableAlias) {
								sb.append(mainTable).append(".");
							}
							sb.append(tcol.getIndexColName()).append(ObjStoreTable.INDEX_TD_POSTFIX);
							sb.append("=");
							sb.append("'");
							sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdbAdaptor.sanitize(pHandler.getParent().getMetaData().getId()), pageNo));
							sb.append("'");
							break;
						case UNIQUE_WITHOUT_NULL:
						case NON_UNIQUE:
							if (needTableAlias) {
								sb.append(mainTable).append(".");
							}
							sb.append(tcol.getIndexColName()).append(ObjStoreTable.INDEX_TD_POSTFIX);
							sb.append("=");
							sb.append("CASE WHEN ").append(valueStr).append(" IS NULL THEN NULL ELSE '");
							sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdbAdaptor.sanitize(pHandler.getParent().getMetaData().getId()), pageNo));
							sb.append("' END");
							break;
						default:
							break;
						}
					}
				}
			}
		}

		return added;
	}


	private void multitableJoinClause(StringBuilder sb, int tenantId, int pageNo, EntityHandler eh, EntityContext ec, List<UpdateValue> values, String tableName, RdbAdapter rdb) {
		
		NeedOtherPageJoinChecker cheker = new NeedOtherPageJoinChecker(pageNo, eh, ec);
		Set<Integer> pns = cheker.check(values);
		String mainTable = tableNameAlias(pageNo);
		
		if (pns != null) {
			for (Integer i: pns) {
				String joinTable = tableNameAlias(i);
				sb.append(" INNER JOIN ").append(tableName).append(" ").append(joinTable);
				sb.append(" ON ").append(mainTable).append(".").append(ObjStoreTable.TENANT_ID).append("=").append(joinTable).append(".").append(ObjStoreTable.TENANT_ID);
				sb.append(" AND ").append(mainTable).append(".").append(ObjStoreTable.OBJ_DEF_ID).append("=").append(joinTable).append(".").append(ObjStoreTable.OBJ_DEF_ID);
				sb.append(" AND ").append(mainTable).append(".").append(ObjStoreTable.OBJ_ID).append("=").append(joinTable).append(".").append(ObjStoreTable.OBJ_ID);
				sb.append(" AND ").append(mainTable).append(".").append(ObjStoreTable.OBJ_VER).append("=").append(joinTable).append(".").append(ObjStoreTable.OBJ_VER);
				sb.append(" AND ").append(joinTable).append(".").append(ObjStoreTable.PG_NO).append("=").append(i);
			}
		}
	}
	
	private String tableNameAlias(int pageNo) {
		if (pageNo == 0) {
			return "m";
		} else {
			return "mp" + pageNo;
		}
	}
	
	public String updateAllSubByTempTable(int tenantId, EntityHandler eh,
			List<UpdateValue> values, String userId, int pageNo, RdbAdapter rdbAdaptor, EntityContext context) {
		StringBuilder sb = new StringBuilder();
		String mainTable = tableNameAlias(pageNo);
		
		if (rdbAdaptor.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append("UPDATE ").append(mainTable);
		} else {
			sb.append("UPDATE ").append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE()).append(" ").append(mainTable);
		}
		
		if (rdbAdaptor.getMultiTableUpdateMethod() == MultiTableUpdateMethod.DIRECT_JOIN) {
			multitableJoinClause(sb, tenantId, pageNo, eh, context, values, ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE(), rdbAdaptor);
		}
		
		sb.append(" SET ");

		boolean isAdd = handlePropAndValforUpdateAll(tenantId, sb, eh, values, pageNo, rdbAdaptor, context);

		if (!isAdd) {
			return null;
		}

		if (rdbAdaptor.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append(" FROM ").append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE()).append(" ").append(mainTable);
		}
		sb.append(" WHERE ").append(mainTable).append(".").append(ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND ").append(mainTable).append(".").append(ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId())).append("'");
		sb.append(" AND ").append(mainTable).append(".").append(ObjStoreTable.PG_NO + "=").append(pageNo);
		if (rdbAdaptor.isSupportRowValueConstructor()) {
			sb.append(" AND (").append(mainTable).append(".").append(ObjStoreTable.OBJ_ID + ",").append(mainTable).append(".").append(ObjStoreTable.OBJ_VER + ") IN("
					+ "SELECT " + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + " FROM " + rdbAdaptor.getTemplaryTablePrefix() + ObjStoreTable.TABLE_NAME_TMP + ")");
		} else {
			sb.append(" AND EXISTS (");
			sb.append("SELECT 1 FROM ").append(rdbAdaptor.getTemplaryTablePrefix()).append(ObjStoreTable.TABLE_NAME_TMP ).append(" ").append(TMP_TABLE_ALIAS);
			sb.append(" WHERE ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_ID ).append("=").append(mainTable).append(".").append(ObjStoreTable.OBJ_ID);
			sb.append(" AND ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_VER ).append("=").append(mainTable).append(".").append(ObjStoreTable.OBJ_VER);
			sb.append(")");
		}

		return sb.toString();
	}

	public String updateAllMainByTempTable(int tenantId, EntityHandler eh,
			List<UpdateValue> values, String userId, RdbAdapter rdbAdaptor, EntityContext context) {
		int pageNo = 0;

		String mainTable = tableNameAlias(pageNo);
		StringBuilder sb = new StringBuilder();
		if (rdbAdaptor.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append("UPDATE ").append(mainTable);
		} else {
			sb.append("UPDATE ").append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE()).append(" ").append(mainTable);
		}
		
		if (rdbAdaptor.getMultiTableUpdateMethod() == MultiTableUpdateMethod.DIRECT_JOIN) {
			multitableJoinClause(sb, tenantId, pageNo, eh, context, values, ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE(), rdbAdaptor);
		}
		
		sb.append(" SET ");

		boolean isAdd = handlePropAndValforUpdateAll(tenantId, sb, eh, values, pageNo, rdbAdaptor, context);

		if (isAdd) {
			sb.append(",");
		}

		//FIXME updateの際にはupdateByをupdatePropertiesに頼っているが、一括更新の場合は、自動セット。整合性を取る！

		if (rdbAdaptor.getMultiTableUpdateMethod() == MultiTableUpdateMethod.DIRECT_JOIN) {
			sb.append(mainTable).append(".");
		}
		sb.append(ObjStoreTable.UP_USER + "='").append(rdbAdaptor.sanitize(userId)).append("',");
		if (rdbAdaptor.getMultiTableUpdateMethod() == MultiTableUpdateMethod.DIRECT_JOIN) {
			sb.append(mainTable).append(".");
		}
		sb.append(ObjStoreTable.UP_DATE + "=").append(rdbAdaptor.systimestamp());

		if (rdbAdaptor.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append(" FROM ").append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE()).append(" ").append(mainTable);
		}
		sb.append(" WHERE ").append(mainTable).append("." + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND ").append(mainTable).append("." + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId())).append("'");
		sb.append(" AND ").append(mainTable).append("." + ObjStoreTable.PG_NO + "=").append(pageNo);
		if (rdbAdaptor.isSupportRowValueConstructor()) {
			sb.append(" AND (").append(mainTable).append("." + ObjStoreTable.OBJ_ID + ",").append(mainTable).append("." + ObjStoreTable.OBJ_VER + ") IN("
					+ "SELECT " + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + " FROM " + rdbAdaptor.getTemplaryTablePrefix() + ObjStoreTable.TABLE_NAME_TMP + ")");
		} else {
			sb.append(" AND EXISTS (");
			sb.append("SELECT 1 FROM ").append(rdbAdaptor.getTemplaryTablePrefix()).append(ObjStoreTable.TABLE_NAME_TMP ).append(" ").append(TMP_TABLE_ALIAS);
			sb.append(" WHERE ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_ID ).append("=").append(mainTable).append(".").append(ObjStoreTable.OBJ_ID);
			sb.append(" AND ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_VER ).append("=").append(mainTable).append(".").append(ObjStoreTable.OBJ_VER);
			sb.append(")");
		}
		//新しい定義でデータ洗い替えが発生している場合は、更新しない
		sb.append(" AND ").append(mainTable).append("." + ObjStoreTable.OBJ_DEF_VER + "<=").append(((MetaGRdbEntityStore) eh.getEntityStoreRuntime().getMetaData()).getVersion());

		return sb.toString();
	}

	//一括更新
	public String updateAllOnlyMainByCondition(int tenantId, EntityHandler eh,
			UpdateCondition cond, String userId, RdbAdapter rdbAdaptor, EntityContext context) {
		int pageNo = 0;

		String mainTable = tableNameAlias(pageNo);
		StringBuilder sb = new StringBuilder();
		if (rdbAdaptor.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append("UPDATE ").append(mainTable);
		} else {
			sb.append("UPDATE ").append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE()).append(" ").append(mainTable);
		}
		sb.append(" SET ");

		boolean isAdd = handlePropAndValforUpdateAll(tenantId, sb, eh, cond.getValues(), pageNo, rdbAdaptor, context);

		if (isAdd) {
			sb.append(",");
		}

		//FIXME updateの際にはupdateByをupdatePropertiesに頼っているが、一括更新の場合は、自動セット。整合性を取る！

		if (rdbAdaptor.getMultiTableUpdateMethod() == MultiTableUpdateMethod.DIRECT_JOIN) {
			sb.append(mainTable).append(".");
		}
		sb.append(ObjStoreTable.UP_USER + "='").append(rdbAdaptor.sanitize(userId)).append("',");
		if (rdbAdaptor.getMultiTableUpdateMethod() == MultiTableUpdateMethod.DIRECT_JOIN) {
			sb.append(mainTable).append(".");
		}
		sb.append(ObjStoreTable.UP_DATE + "=").append(rdbAdaptor.systimestamp());

		if (rdbAdaptor.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append(" FROM ").append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE()).append(" ").append(mainTable);
		}
		
		sb.append(" WHERE ").append(mainTable).append("." + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND ").append(mainTable).append("." + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId())).append("'");
		sb.append(" AND ").append(mainTable).append("." + ObjStoreTable.PG_NO + "=").append(pageNo);
		if (rdbAdaptor.isSupportRowValueConstructor()) {
			sb.append(" AND (").append(mainTable).append("." + ObjStoreTable.OBJ_ID + ",").append(mainTable).append("." + ObjStoreTable.OBJ_VER + ") IN(");
		} else {
			sb.append(" AND EXISTS (");
		}

		//TODO joinがない場合は、inのサブクエリでなく、直接条件指定する

		Query q = new Query();
		q.selectDistinct(Entity.OID, Entity.VERSION)
			.from(eh.getMetaData().getName())
			.setWhere(cond.getWhere());
		SqlQueryContext qc = new SqlQueryContext(eh, context, rdbAdaptor);
		SqlConverter conv = new SqlConverter(qc, false);
		q.accept(conv);

		if (rdbAdaptor.isSupportRowValueConstructor()) {
			sb.append(rdbAdaptor.tableAlias(qc.toSelectSql()));
		} else {
			String prefix = qc.getPrefix();

			StringBuilder sbJoin = new StringBuilder();
			if (prefix != null) {
				sbJoin.append(prefix).append(".");
			}
			sbJoin.append(ObjStoreTable.OBJ_ID).append("=").append(mainTable).append(".").append(ObjStoreTable.OBJ_ID);
			sbJoin.append(" AND ");
			if (prefix != null) {
				sbJoin.append(prefix).append(".");
			}
			sbJoin.append(ObjStoreTable.OBJ_VER).append("=").append(mainTable).append(".").append(ObjStoreTable.OBJ_VER);

			sb.append(rdbAdaptor.tableAlias(qc.toSelectSql(sbJoin.toString())));
		}
		sb.append(")");

		//新しい定義でデータ洗い替えが発生している場合は、更新しない
		sb.append(" AND ").append(mainTable).append("." + ObjStoreTable.OBJ_DEF_VER + "<=").append(((MetaGRdbEntityStore) eh.getEntityStoreRuntime().getMetaData()).getVersion());

		return sb.toString();
	}

	//一括削除用
//	public String toSqlForMarkDel(int tenantId, EntityHandler eh,
//			DeleteCondition cond, RdbAdapter rdbAdaptor, EntityContext context) {
//
//		//TODO 継承対応。
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("UPDATE ");
//		sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
//		sb.append(" SET " + ObjStoreTable.DEL_FLG + "='D'");
//		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId()));
//		sb.append("' AND (" + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + ") in(");
//
//		//TODO joinがない場合は、inのサブクエリでなく、直接条件指定する
//
//		Query q = new Query();
//		q.selectDistinct(Entity.OID, Entity.VERSION)
//			.from(eh.getMetaData().getName())
//			.setWhere(cond.getWhere());
//		SqlQueryContext qc = new SqlQueryContext(eh, context, rdbAdaptor);
//		SqlConverter conv = new SqlConverter(qc, false);
//		q.accept(conv);
//
//		sb.append(rdbAdaptor.tableAlias(qc.toSelectSql()));
//		sb.append(")");
//
//		return sb.toString();
//	}

	//デフラグ用(未使用カラムのクリア)
//	public String toSqlForDefrag(int tenantId, EntityHandler eh,
//			List<String> unUseColNames, RdbAdapter rdbAdaptor, EntityContext context) {
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("UPDATE ");
//		sb.append(((RHCEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
//
//		sb.append(" SET ");
//		for (String colName : unUseColNames) {
//			sb.append(colName + " = null,");
//		}
//		sb.deleteCharAt(sb.length() - 1);
//
//		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
//		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId()) + "'");
//
//		//バージョンは指定しない
//
//		return sb.toString();
//	}


	private static class HasOtherPageChecker extends QueryVisitorSupport {

		private int myPageNo;
		private boolean hasOtherPage;
		private EntityHandler eh;
		private EntityContext ec;

		private HasOtherPageChecker(int myPageNo, EntityHandler eh, EntityContext ec) {
			this.myPageNo = myPageNo;
			this.eh = eh;
			this.ec = ec;
		}

		@Override
		public boolean visit(EntityField entityField) {
			if (!hasOtherPage) {
				PropertyHandler ph = eh.getProperty(entityField.getPropertyName(), ec);
				if (ph == null) {
					throw new QueryException(entityField.getPropertyName() + " not define on" + eh.getMetaData().getName());
				}
				GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
				List<GRdbPropertyStoreHandler> cols = col.asList();
				for (GRdbPropertyStoreHandler c: cols) {
					if (c.getMetaData().getPageNo() != myPageNo) {
						hasOtherPage = true;
						break;
					}
				}
			}
			return false;
		}

		@Override
		public boolean visit(SubQuery subQuery) {
			//サブクエリはチェックしない
			return false;
		}

	}

	private static class NeedOtherPageJoinChecker extends QueryVisitorSupport {

		private int myPageNo;
		private Set<Integer> otherPn;
		private EntityHandler eh;
		private EntityContext ec;

		private NeedOtherPageJoinChecker(int myPageNo, EntityHandler eh, EntityContext ec) {
			this.myPageNo = myPageNo;
			this.eh = eh;
			this.ec = ec;
		}
		
		Set<Integer> check(List<UpdateValue> values) {
			for (UpdateValue uv: values) {
				PropertyHandler pHandler = eh.getProperty(uv.getEntityField(), ec);
				if (canUpdateProperty(pHandler)) {
					GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) pHandler.getStoreSpecProperty();
					ValueExpression val = uv.getValue();

					List<GRdbPropertyStoreHandler> cols = col.asList();
					for (int i = 0; i < cols.size(); i++) {
						GRdbPropertyStoreHandler tcol = cols.get(i);
						if (tcol.getMetaData().getPageNo() == myPageNo) {
							val.accept(this);
						}
					}
				}
			}
			return otherPn;
		}

		@Override
		public boolean visit(EntityField entityField) {
			PropertyHandler ph = eh.getProperty(entityField.getPropertyName(), ec);
			if (ph == null) {
				throw new QueryException(entityField.getPropertyName() + " not define on" + eh.getMetaData().getName());
			}
			GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
			List<GRdbPropertyStoreHandler> cols = col.asList();
			for (GRdbPropertyStoreHandler c: cols) {
				if (c.getMetaData().getPageNo() != myPageNo) {
					if (otherPn == null) {
						otherPn = new HashSet<>();
					}
					otherPn.add(c.getMetaData().getPageNo());
				}
				if (!c.isExternalIndex() && c.getIndexColName() != null && c.getMetaData().getIndexPageNo() != myPageNo) {
					if (otherPn == null) {
						otherPn = new HashSet<>();
					}
					otherPn.add(c.getMetaData().getIndexPageNo());
				}
				
			}
			return false;
		}

		@Override
		public boolean visit(SubQuery subQuery) {
			//サブクエリはチェックしない
			return false;
		}

	}
}
