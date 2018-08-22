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

import java.util.List;
import java.util.TreeSet;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColContext.ColCopy;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class ObjStoreMaintenanceSql extends UpdateSqlHandler {

	public String insertNewPage(int tenantId, String metaId, int pageNo, String tableNamePostfix, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(MetaGRdbEntityStore.makeObjStoreName(tableNamePostfix));
		sb.append("(");
		sb.append(ObjStoreTable.TENANT_ID).append(",");
		sb.append(ObjStoreTable.OBJ_DEF_ID).append(",");
		sb.append(ObjStoreTable.PG_NO).append(",");
		sb.append(ObjStoreTable.OBJ_ID).append(",");
		sb.append(ObjStoreTable.OBJ_VER);
		sb.append(") SELECT ");
		sb.append(tenantId).append(",");
		sb.append("'").append(rdb.sanitize(metaId)).append("',");
		sb.append(pageNo).append(",");
		sb.append(ObjStoreTable.OBJ_ID).append(",");
		sb.append(ObjStoreTable.OBJ_VER);
		sb.append(" FROM ").append(MetaGRdbEntityStore.makeObjStoreName(tableNamePostfix));
		sb.append(" WHERE ");
		sb.append(ObjStoreTable.TENANT_ID).append("=").append(tenantId);
		sb.append(" AND ");
		sb.append(ObjStoreTable.OBJ_DEF_ID).append("='").append(rdb.sanitize(metaId)).append("'");
		sb.append(" AND ");
		sb.append(ObjStoreTable.PG_NO).append("=0");

		return sb.toString();
	}

	public String insertNewPageRB(int tenantId, String metaId, int pageNo, String tableNamePostfix, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(MetaGRdbEntityStore.makeObjRbTableName(tableNamePostfix));
		sb.append("(");
		sb.append(ObjStoreTable.RB_ID).append(",");
		sb.append(ObjStoreTable.RB_DATE).append(",");
		sb.append(ObjStoreTable.RB_USER).append(",");
		sb.append(ObjStoreTable.TENANT_ID).append(",");
		sb.append(ObjStoreTable.OBJ_DEF_ID).append(",");
		sb.append(ObjStoreTable.PG_NO).append(",");
		sb.append(ObjStoreTable.OBJ_ID).append(",");
		sb.append(ObjStoreTable.OBJ_VER);
		sb.append(") SELECT ");
		sb.append(ObjStoreTable.RB_ID).append(",");
		sb.append(ObjStoreTable.RB_DATE).append(",");
		sb.append(ObjStoreTable.RB_USER).append(",");
		sb.append(tenantId).append(",");
		sb.append("'").append(rdb.sanitize(metaId)).append("',");
		sb.append(pageNo).append(",");
		sb.append(ObjStoreTable.OBJ_ID).append(",");
		sb.append(ObjStoreTable.OBJ_VER);
		sb.append(" FROM ").append(MetaGRdbEntityStore.makeObjRbTableName(tableNamePostfix));
		sb.append(" WHERE ");
		sb.append(ObjStoreTable.TENANT_ID).append("=").append(tenantId);
		sb.append(" AND ");
		sb.append(ObjStoreTable.OBJ_DEF_ID).append("='").append(rdb.sanitize(metaId)).append("'");
		sb.append(" AND ");
		sb.append(ObjStoreTable.PG_NO).append("=0");

		return sb.toString();
	}

	public String deletePage(int tenantId, String id, int fromPageNo, int toPageNo,
			String tableNamePostfix, RdbAdapter rdb) {
		return deletePageInternal(tenantId, id, fromPageNo, toPageNo, MetaGRdbEntityStore.makeObjStoreName(tableNamePostfix), rdb);
	}
	public String deletePageRB(int tenantId, String id, int fromPageNo, int toPageNo,
			String tableNamePostfix, RdbAdapter rdb) {
		return deletePageInternal(tenantId, id, fromPageNo, toPageNo, MetaGRdbEntityStore.makeObjRbTableName(tableNamePostfix), rdb);
	}

	private String deletePageInternal(int tenantId, String id, int fromPageNo, int toPageNo,
			String tableName, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(tableName);
		sb.append(" WHERE ");
		sb.append(ObjStoreTable.TENANT_ID).append("=").append(tenantId);
		sb.append(" AND ");
		sb.append(ObjStoreTable.OBJ_DEF_ID).append("='").append(rdb.sanitize(id)).append("'");
		sb.append(" AND ");
		sb.append(ObjStoreTable.PG_NO).append(" IN(");
		for (int i = fromPageNo; i <= toPageNo; i++) {
			if (i != fromPageNo) {
				sb.append(",");
			}
			sb.append(i);
		}
		sb.append(")");
		return sb.toString();
	}

	public String updateCol(int tenantId, String metaId, int defVer, int pageNo, List<ColCopy> ccl, String tableNamePostfix, RdbAdapter rdb) {
		return updateColInternal(tenantId, metaId, defVer, pageNo, ccl, MetaGRdbEntityStore.makeObjStoreName(tableNamePostfix), rdb);
	}

	private String updateColInternal(int tenantId, String metaId, int defVer, int pageNo, List<ColCopy> ccl, String tableName, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		if (rdb.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append("UPDATE ").append("M");
		} else {
			sb.append("UPDATE ").append(tableName).append(" M");
		}
		sb.append(" SET ");
		boolean addedCol = false;
		if (pageNo == 0) {
			sb.append(ObjStoreTable.OBJ_DEF_VER).append("=").append(defVer);
			addedCol = true;
		}
		if (ccl != null && ccl.size() > 0) {
			for (int i = 0; i < ccl.size(); i++) {
				if (addedCol) {
					sb.append(",");
				} else {
					addedCol = true;
				}
				ColCopy cc = ccl.get(i);
				if (cc.isSetToNull()) {
					//nullで更新
					if (cc.getIndexType() == null) {
						//通常カラム
						sb.append(cc.getToColName()).append("=null");
					} else {
						//Indexカラム
						sb.append(cc.getToColName() + ObjStoreTable.INDEX_TD_POSTFIX).append("=null");
						sb.append(",");
						sb.append(cc.getToColName()).append("=null");
					}
				} else if (cc.getFromPageNo() == pageNo) {
					//同一page内
					if (cc.getIndexType() == null) {
						//通常カラム
						sb.append(cc.getToColName()).append("=");
						cc.getConverter().appendConvertExp(sb, cc.getFromColName(), rdb);
//						if (cc.isNeedCast()) {
//							sb.append(castExpression(rdb, cc.getToType(), cc.getFromColName(), cc.getFromType()));
//						} else {
//							sb.append(cc.getFromColName());
//						}
					} else {
						//Indexカラム
						sb.append(cc.getToColName() + ObjStoreTable.INDEX_TD_POSTFIX).append("=");
						if (cc.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL
								||cc.getIndexType() == IndexType.NON_UNIQUE) {
							sb.append("CASE WHEN ").append(cc.getFromColName()).append(" IS NULL THEN NULL ELSE '");
							sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdb.sanitize(metaId), pageNo));
							sb.append("' END");
						} else {
							sb.append("'");
							sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdb.sanitize(metaId), pageNo));
							sb.append("'");
						}
						sb.append(",");
						sb.append(cc.getToColName()).append("=");
						cc.getConverter().appendConvertExp(sb, cc.getFromColName(), rdb);
//						sb.append(cc.getFromColName());
					}
				} else {
					//相関サブクエリで別テーブル（自己結合）から
					if (cc.getIndexType() == null) {
						//通常カラム
						sb.append(cc.getToColName()).append("=");
						sb.append("(SELECT ");
						cc.getConverter().appendConvertExp(sb, cc.getFromColName(), rdb);
//						if (cc.isNeedCast()) {
//							sb.append(castExpression(rdb, cc.getToType(), cc.getFromColName(), cc.getFromType()));
//						} else {
//							sb.append(cc.getFromColName());
//						}
						sb.append(" FROM ").append(tableName).append(" S");
						sb.append(" WHERE ");
						sb.append("M.").append(ObjStoreTable.TENANT_ID).append("=S.").append(ObjStoreTable.TENANT_ID);
						sb.append(" AND ");
						sb.append("M.").append(ObjStoreTable.OBJ_DEF_ID).append("=S.").append(ObjStoreTable.OBJ_DEF_ID);
						sb.append(" AND ");
						sb.append("S.").append(ObjStoreTable.PG_NO).append("=").append(cc.getFromPageNo());
						sb.append(" AND ");
						sb.append("M.").append(ObjStoreTable.OBJ_ID).append("=S.").append(ObjStoreTable.OBJ_ID);
						sb.append(" AND ");
						sb.append("M.").append(ObjStoreTable.OBJ_VER).append("=S.").append(ObjStoreTable.OBJ_VER);
						sb.append(")");
					} else {
						//Indexカラム
						sb.append(cc.getToColName() + ObjStoreTable.INDEX_TD_POSTFIX).append("=");
						if (cc.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL) {
							sb.append("CASE WHEN ");
							sb.append("(SELECT ");
							cc.getConverter().appendConvertExp(sb, cc.getFromColName(), rdb);
//							sb.append(cc.getFromColName());
							sb.append(" FROM ").append(tableName).append(" S");
							sb.append(" WHERE ");
							sb.append("M.").append(ObjStoreTable.TENANT_ID).append("=S.").append(ObjStoreTable.TENANT_ID);
							sb.append(" AND ");
							sb.append("M.").append(ObjStoreTable.OBJ_DEF_ID).append("=S.").append(ObjStoreTable.OBJ_DEF_ID);
							sb.append(" AND ");
							sb.append("S.").append(ObjStoreTable.PG_NO).append("=").append(cc.getFromPageNo());
							sb.append(" AND ");
							sb.append("M.").append(ObjStoreTable.OBJ_ID).append("=S.").append(ObjStoreTable.OBJ_ID);
							sb.append(" AND ");
							sb.append("M.").append(ObjStoreTable.OBJ_VER).append("=S.").append(ObjStoreTable.OBJ_VER);
							sb.append(")");
							sb.append(" IS NULL THEN NULL ELSE '");
							sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdb.sanitize(metaId), pageNo));
							sb.append("' END");
						} else {
							sb.append("'");
							sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdb.sanitize(metaId), pageNo));
							sb.append("'");
						}
						sb.append(",");

						sb.append(cc.getToColName()).append("=");
						sb.append("(SELECT ");
						sb.append(cc.getFromColName());
						sb.append(" FROM ").append(tableName).append(" S");
						sb.append(" WHERE ");
						sb.append("M.").append(ObjStoreTable.TENANT_ID).append("=S.").append(ObjStoreTable.TENANT_ID);
						sb.append(" AND ");
						sb.append("M.").append(ObjStoreTable.OBJ_DEF_ID).append("=S.").append(ObjStoreTable.OBJ_DEF_ID);
						sb.append(" AND ");
						sb.append("S.").append(ObjStoreTable.PG_NO).append("=").append(cc.getFromPageNo());
						sb.append(" AND ");
						sb.append("M.").append(ObjStoreTable.OBJ_ID).append("=S.").append(ObjStoreTable.OBJ_ID);
						sb.append(" AND ");
						sb.append("M.").append(ObjStoreTable.OBJ_VER).append("=S.").append(ObjStoreTable.OBJ_VER);
						sb.append(")");
					}

				}
			}
		}
		if (rdb.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append(" FROM ").append(tableName).append(" M");
		}

		sb.append(" WHERE ").append(ObjStoreTable.TENANT_ID).append("=").append(tenantId);
		sb.append(" AND ").append(ObjStoreTable.OBJ_DEF_ID).append("='").append(rdb.sanitize(metaId)).append("'");
		sb.append(" AND ").append(ObjStoreTable.PG_NO).append("=").append(pageNo);
		return sb.toString();
	}

	public String updateColRB(int tenantId, String metaId, int defVer, int pageNo, List<ColCopy> ccl, String tableNamePostfix, RdbAdapter rdb) {
		return updateColInternal(tenantId, metaId, defVer, pageNo, ccl, MetaGRdbEntityStore.makeObjRbTableName(tableNamePostfix), rdb);
	}

	public String updateColDirectJoin(int tenantId, String metaId, int defVer, List<ColCopy>[] ccl, String tableNamePostfix, RdbAdapter rdb) {
		return updateColDirectJoinInternal(tenantId, metaId, defVer, ccl, MetaGRdbEntityStore.makeObjStoreName(tableNamePostfix), rdb);
	}
	public String updateColDirectJoinRB(int tenantId, String metaId, int defVer, List<ColCopy>[] ccl, String tableNamePostfix, RdbAdapter rdb) {
		return updateColDirectJoinInternal(tenantId, metaId, defVer, ccl, MetaGRdbEntityStore.makeObjRbTableName(tableNamePostfix), rdb);
	}

	private String updateColDirectJoinInternal(int tenantId, String metaId, int defVer, List<ColCopy>[] ccls, String tableName, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		if (rdb.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append("UPDATE ").append("p0");
		} else {
			sb.append("UPDATE ").append(tableName).append(" p0");
		}

		multitableJoinClause(sb, tenantId, metaId, defVer, ccls, tableName, rdb);
		multitableUpdateSetClause(sb, tenantId, metaId, defVer, ccls, rdb);

		if (rdb.isNeedFromClauseTableAliasUpdateStatement()) {
			sb.append(" FROM ").append(tableName).append(" p0");
		}

		sb.append(" WHERE p0.").append(ObjStoreTable.TENANT_ID).append("=").append(tenantId);
		sb.append(" AND p0.").append(ObjStoreTable.OBJ_DEF_ID).append("='").append(rdb.sanitize(metaId)).append("'");
		sb.append(" AND p0.").append(ObjStoreTable.PG_NO).append("=0");
		return sb.toString();
	}

	private void multitableJoinClause(StringBuilder sb, int tenantId, String metaId, int defVer, List<ColCopy>[] ccls, String tableName, RdbAdapter rdb) {
		TreeSet<Integer> joinPages = new TreeSet<>();
		for (int i = 0; i < ccls.length; i++) {
			if (ccls[i] != null) {
				for (ColCopy cc: ccls[i]) {
					if (!cc.isSetToNull()
							&& cc.getFromPageNo() != 0) {
						joinPages.add(cc.getFromPageNo());
					}
					if (cc.getToPageNo() != 0) {
						joinPages.add(cc.getToPageNo());
					}
				}
			}
		}

			for (Integer i: joinPages) {
			sb.append(" INNER JOIN ").append(tableName).append(" p").append(i);
			sb.append(" ON p0.").append(ObjStoreTable.TENANT_ID).append("=p").append(i).append(".").append(ObjStoreTable.TENANT_ID);
			sb.append(" AND p0.").append(ObjStoreTable.OBJ_DEF_ID).append("=p").append(i).append(".").append(ObjStoreTable.OBJ_DEF_ID);
			sb.append(" AND p0.").append(ObjStoreTable.OBJ_ID).append("=p").append(i).append(".").append(ObjStoreTable.OBJ_ID);
			sb.append(" AND p0.").append(ObjStoreTable.OBJ_VER).append("=p").append(i).append(".").append(ObjStoreTable.OBJ_VER);
			sb.append(" AND p").append(i).append(".").append(ObjStoreTable.PG_NO).append("=").append(i);
		}
	}

	private void multitableUpdateSetClause(StringBuilder sb, int tenantId, String metaId, int defVer, List<ColCopy>[] ccls, RdbAdapter rdb) {
		sb.append(" SET ");
		sb.append("p0.").append(ObjStoreTable.OBJ_DEF_VER).append("=").append(defVer);
		for (int i = 0; i < ccls.length; i++) {
			if (ccls[i] != null) {
				for (ColCopy cc: ccls[i]) {
					sb.append(",");
					if (cc.isSetToNull()) {
						//nullで更新
						if (cc.getIndexType() == null) {
							//通常カラム
							sb.append("p").append(cc.getToPageNo()).append(".").append(cc.getToColName()).append("=null");
						} else {
							//Indexカラム
							sb.append("p").append(cc.getToPageNo()).append(".").append(cc.getToColName() + ObjStoreTable.INDEX_TD_POSTFIX).append("=null");
							sb.append(",");
							sb.append("p").append(cc.getToPageNo()).append(".").append(cc.getToColName()).append("=null");
						}
					} else {
						//同一page内
						if (cc.getIndexType() == null) {
							//通常カラム
							sb.append("p").append(cc.getToPageNo()).append(".").append(cc.getToColName()).append("=");
							cc.getConverter().appendConvertExp(sb, "p" + cc.getFromPageNo() + "." + cc.getFromColName(), rdb);
						} else {
							//Indexカラム
							sb.append("p").append(cc.getToPageNo()).append(".").append(cc.getToColName() + ObjStoreTable.INDEX_TD_POSTFIX).append("=");
							if (cc.getIndexType() == IndexType.UNIQUE_WITHOUT_NULL
									||cc.getIndexType() == IndexType.NON_UNIQUE) {
								sb.append("CASE WHEN ").append("p").append(cc.getFromPageNo()).append(".").append(cc.getFromColName()).append(" IS NULL THEN NULL ELSE '");
								sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdb.sanitize(metaId), cc.getToPageNo()));
								sb.append("' END");
							} else {
								sb.append("'");
								sb.append(MetaGRdbPropertyStore.makeInternalIndexKey(tenantId, rdb.sanitize(metaId), cc.getToPageNo()));
								sb.append("'");
							}
							sb.append(",");
							sb.append("p").append(cc.getToPageNo()).append(".").append(cc.getToColName()).append("=");
							cc.getConverter().appendConvertExp(sb, "p" + cc.getFromPageNo() + "." + cc.getFromColName(), rdb);
						}
					}
				}
			}
		}
	}

//	public String updateColInlineView(int tenantId, String metaId, int defVer, List<ColCopy>[] ccl, String tableNamePostfix, RdbAdapter rdb) {
//		return updateColInlineViewInternal(tenantId, metaId, defVer, ccl, MetaGRdbEntityStore.makeObjStoreName(tableNamePostfix), rdb);
//	}
//	public String updateColInlineViewRB(int tenantId, String metaId, int defVer, List<ColCopy>[] ccl, String tableNamePostfix, RdbAdapter rdb) {
//		return updateColInlineViewInternal(tenantId, metaId, defVer, ccl, MetaGRdbEntityStore.makeObjRbTableName(tableNamePostfix), rdb);
//	}

	//TODO SELECT *だと、ORA-01792に引っ掛かる。。。。1000件にいってないのに。。。あと、カラム名をちゃんと決めてあげる必要がある。。。
//	private String updateColInlineViewInternal(int tenantId, String metaId, int defVer, List<ColCopy>[] ccls, String tableName, RdbAdapter rdb) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("UPDATE (SELECT * FROM ").append(tableName).append(" p0");
//
//		multitableJoinClause(sb, tenantId, metaId, defVer, ccls, tableName, rdb);
//
//		sb.append(" WHERE p0.").append(ObjStoreTable.TENANT_ID).append("=").append(tenantId);
//		sb.append(" AND p0.").append(ObjStoreTable.OBJ_DEF_ID).append("='").append(rdb.sanitize(metaId)).append("'");
//		sb.append(" AND p0.").append(ObjStoreTable.PG_NO).append("=0");
//		sb.append(")");
//
//		multitableUpdateSetClause(sb, tenantId, metaId, defVer, ccls, rdb);
//		return sb.toString();
//	}
}
