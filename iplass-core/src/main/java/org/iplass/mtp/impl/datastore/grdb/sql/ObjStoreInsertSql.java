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

package org.iplass.mtp.impl.datastore.grdb.sql;

import java.sql.Timestamp;
import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbMultiplePropertyStore.GRdbMultiplePropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlConverter;
import org.iplass.mtp.impl.datastore.grdb.sql.queryconvert.SqlQueryContext;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.properties.extend.VirtualType;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class ObjStoreInsertSql extends UpdateSqlHandler {

	public String copyToTemporaryTable(EntityHandler handler, Where cond, RdbAdapter rdb, EntityContext entityContext) {
		Query q = new Query();
		q.select(Entity.OID, Entity.VERSION)
			.from(handler.getMetaData().getName())
			.setWhere(cond);
		SqlQueryContext qc = new SqlQueryContext(handler, entityContext, rdb);
		SqlConverter conv = new SqlConverter(qc, false);
		q.accept(conv);
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + rdb.getTemplaryTablePrefix() + ObjStoreTable.TABLE_NAME_TMP);
		sb.append(" ");
		sb.append(qc.toSelectSql());
		return sb.toString();
	}

	public String insertSubPage(int tenantId, EntityHandler eh,
			Entity model, int pageNo, RdbAdapter rdbAdaptor, EntityContext context) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append("(" + ObjStoreTable.TENANT_ID
				+ "," + ObjStoreTable.OBJ_DEF_ID
				+ "," + ObjStoreTable.OBJ_ID
				+ "," + ObjStoreTable.OBJ_VER
				+ "," + ObjStoreTable.PG_NO);
		List<PropertyHandler> propList = eh.getDeclaredPropertyList();

		handlePropList(sb, tenantId, propList, model, pageNo, rdbAdaptor);

		sb.append(") VALUES(");
		sb.append(tenantId).append(",'");
		sb.append(rdbAdaptor.sanitize(eh.getMetaData().getId())).append("',");
		sb.append("'").append(rdbAdaptor.sanitize(model.getOid())).append("'");
		sb.append(",");
		sb.append(model.getVersion()).append(",");
		sb.append(pageNo);

		handleValue(sb, tenantId, propList, model, pageNo, rdbAdaptor);

		sb.append(")");

		return sb.toString();
	}

	private void handlePropList(StringBuilder sb, int tenantId, List<PropertyHandler> propList, Entity model, int pageNo, RdbAdapter rdbAdaptor) {
		for (PropertyHandler p: propList) {
			if (p instanceof PrimitivePropertyHandler
					&& !((PrimitivePropertyHandler) p).getMetaData().getType().isVirtual()) {
				GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) p.getStoreSpecProperty();
				Object val = model.getValue(p.getName());
				if (!colDef.isMulti()) {
					//本体
					GRdbPropertyStoreHandler scol = (GRdbPropertyStoreHandler) colDef;
					if (scol.getMetaData().getPageNo() == pageNo) {
						if (val != null) {
							sb.append(",");
							sb.append(scol.getMetaData().getColumnName());
						}
					}
					//index
					if (scol.getIndexColName() != null && scol.getMetaData().getIndexPageNo() == pageNo) {
						String indexColName = scol.getIndexColName();
						switch (scol.getIndexType()) {
						case UNIQUE:
							sb.append(",");
							sb.append(indexColName).append(ObjStoreTable.INDEX_TD_POSTFIX);
							sb.append(",");
							sb.append(indexColName);
							break;
						case UNIQUE_WITHOUT_NULL:
						case NON_UNIQUE:
							if (val != null) {
								sb.append(",");
								sb.append(indexColName).append(ObjStoreTable.INDEX_TD_POSTFIX);
								sb.append(",");
								sb.append(indexColName);
							}
							break;
						default:
							break;
						}
					}
				} else {
					//本体
					if (val != null) {
						GRdbMultiplePropertyStoreHandler mcol = (GRdbMultiplePropertyStoreHandler) colDef;
						for (MetaGRdbPropertyStore metaCol: mcol.getMetaData().getStore()) {
							if (metaCol.getPageNo() == pageNo) {
									sb.append(",");
									sb.append(metaCol.getColumnName());
							}
						}
						//TODO multiは現状index非対応
					}
				}
			}
		}
	}

	private void handleValue(StringBuilder sb, int tenantId, List<PropertyHandler> propList, Entity model, int pageNo, RdbAdapter rdbAdaptor) {
		for (PropertyHandler p: propList) {
			if (p instanceof PrimitivePropertyHandler
					&& !(((PrimitivePropertyHandler) p).getMetaData().getType() instanceof VirtualType)) {
				GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) p.getStoreSpecProperty();
				Object val = model.getValue(p.getName());
				if (!colDef.isMulti()) {
					//本体
					GRdbPropertyStoreHandler scol = (GRdbPropertyStoreHandler) colDef;
					if (scol.getMetaData().getPageNo() == pageNo) {
						if (val != null) {
							sb.append(",");
							if (colDef.isNative()) {
								colDef.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(val, sb, rdbAdaptor);
							} else {
								colDef.getSingleColumnRdbTypeAdapter().appendToTypedCol(sb, rdbAdaptor,
										() -> colDef.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(val, sb, rdbAdaptor));
							}
						}
					}
					//index
					if (scol.getIndexColName() != null && scol.getMetaData().getIndexPageNo() == pageNo) {
						switch (scol.getIndexType()) {
						case UNIQUE:
							sb.append(",'");
							sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdbAdaptor.sanitize(p.getParent().getMetaData().getId()), pageNo));
							sb.append("',");
							colDef.getSingleColumnRdbTypeAdapter().appendToTypedCol(sb, rdbAdaptor,
									() -> colDef.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(val, sb, rdbAdaptor));
							break;
						case UNIQUE_WITHOUT_NULL:
						case NON_UNIQUE:
							if (val != null) {
								sb.append(",'");
								sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdbAdaptor.sanitize(p.getParent().getMetaData().getId()), pageNo));
								sb.append("',");
								colDef.getSingleColumnRdbTypeAdapter().appendToTypedCol(sb, rdbAdaptor,
										() -> colDef.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(val, sb, rdbAdaptor));
							}
							break;
						default:
							break;
						}
					}
				} else {
					if (val != null) {
						//本体
						Object[] valList;
						if (val instanceof Object[]) {
							valList = (Object[]) val;
						} else {
							valList = new Object[]{val};
						}
						GRdbMultiplePropertyStoreHandler mcol = (GRdbMultiplePropertyStoreHandler) colDef;
						for (int i = 0; i < mcol.getMetaData().getStore().size(); i++) {
							MetaGRdbPropertyStore metaCol = mcol.getMetaData().getStore().get(i);
							if (metaCol.getPageNo() == pageNo) {
								sb.append(",");
								if (i < valList.length) {
									final int index = i;
									if (colDef.isNative()) {
										colDef.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(valList[index], sb, rdbAdaptor);
									} else {
										colDef.getSingleColumnRdbTypeAdapter().appendToTypedCol(sb, rdbAdaptor,
												() -> colDef.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(valList[index], sb, rdbAdaptor));
									}
								} else {
									sb.append("null");
								}
							}
							//TODO multiは現状index非対応
						}
					}
				}
			}
		}
	}

	public String insertMain(int tenantId, EntityHandler eh,
			Entity model, RdbAdapter rdbAdaptor, EntityContext context) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append("(" + ObjStoreTable.TENANT_ID
				+ "," + ObjStoreTable.OBJ_DEF_ID
				+ "," + ObjStoreTable.OBJ_ID
				+ "," + ObjStoreTable.OBJ_VER
				+ "," + ObjStoreTable.PG_NO
				+ "," + ObjStoreTable.OBJ_DEF_VER
				+ "," + ObjStoreTable.STATUS
				+ "," + ObjStoreTable.OBJ_NAME
				+ "," + ObjStoreTable.OBJ_DESC
				+ "," + ObjStoreTable.CRE_DATE
				+ "," + ObjStoreTable.UP_DATE
				+ "," + ObjStoreTable.S_DATE
				+ "," + ObjStoreTable.E_DATE
				+ "," + ObjStoreTable.LOCK_USER
				+ "," + ObjStoreTable.CRE_USER
				+ "," + ObjStoreTable.UP_USER);
		List<PropertyHandler> propList = eh.getDeclaredPropertyList();

		handlePropList(sb, tenantId, propList, model, 0, rdbAdaptor);

		sb.append(") VALUES(");
		sb.append(tenantId).append(",'");
		sb.append(rdbAdaptor.sanitize(eh.getMetaData().getId())).append("',");
		sb.append("'").append(rdbAdaptor.sanitize(model.getOid())).append("'");
		sb.append(",");
		sb.append(model.getVersion()).append(",");
		sb.append("0,");
		sb.append(((MetaGRdbEntityStore) eh.getMetaData().getEntityStoreDefinition()).getVersion()).append(",");
		if (model.getValue(Entity.STATE) != null) {
			PropertyHandler p = eh.getProperty(Entity.STATE, context);
			GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) p.getStoreSpecProperty();
			colDef.getSingleColumnRdbTypeAdapter().appendToSqlAsRealType(model.getValue(Entity.STATE), sb, rdbAdaptor);
			sb.append(",");
		} else {
			sb.append("'V',");
		}
		sb.append("'").append(rdbAdaptor.sanitize(model.getName())).append("',");
		if (model.getDescription() != null) {
			sb.append("'").append(rdbAdaptor.sanitize(model.getDescription())).append("',");
		} else {
			sb.append("null,");
		}
		
		Timestamp createDate = model.getCreateDate();
		if (createDate == null) {
			sb.append(rdbAdaptor.systimestamp()).append(",");
		} else {
			sb.append(rdbAdaptor.toTimeStampExpression(createDate)).append(",");
		}
		
		Timestamp updateDate = model.getUpdateDate();
		if (model.getUpdateDate() == null) {
			sb.append(rdbAdaptor.systimestamp()).append(",");
		} else {
			sb.append(rdbAdaptor.toTimeStampExpression(updateDate)).append(",");
		}
		
		if (model.getStartDate() != null) {
			sb.append(rdbAdaptor.toTimeStampExpression(model.getStartDate())).append(",");
		} else {
			sb.append("null,");
		}
		if (model.getEndDate() != null) {
			sb.append(rdbAdaptor.toTimeStampExpression(model.getEndDate())).append(",");
		} else {
			sb.append("null,");
		}
		if (model.getLockedBy() != null) {
			sb.append("'").append(rdbAdaptor.sanitize(model.getLockedBy())).append("',");
		} else {
			sb.append("null,");
		}
		sb.append("'").append(rdbAdaptor.sanitize(model.getCreateBy())).append("',");
		sb.append("'").append(rdbAdaptor.sanitize(model.getUpdateBy())).append("'");

		handleValue(sb, tenantId, propList, model, 0, rdbAdaptor);

		sb.append(")");

		return sb.toString();
	}

}
