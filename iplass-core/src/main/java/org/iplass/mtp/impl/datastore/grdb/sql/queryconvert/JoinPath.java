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

package org.iplass.mtp.impl.datastore.grdb.sql.queryconvert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.AsOf.AsOfSpec;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.ToSqlResult.BindValue;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;


public class JoinPath {

//	public static final String ROOT_ALIAS = "c";

	public static String PAGE_PREFIX = "p";

	private static String OUTER_JOIN = "LEFT OUTER JOIN";
	private static String INNER_JOIN = "INNER JOIN";

//	private String rootAlias;
	private TableAliasMapping aliases;
	String name;
	boolean isUse;
	boolean isMappedBy;
	private boolean isRoot;
	String refDefId;
	String taregtObjDefId;
	String mappedByObjDefId;
	String myAlias;

	String objRefTableName;
	String objDataTableName;

	int refTenantId;
	int dataTenantId;

	private boolean joinWithVersionCol;
	private boolean joinRefWithDataTable;

	private String additionalCondition;
	private List<BindValue> bindVariables;
	private AsOfSpec asOfSpec;

	JoinPath parent;
	ArrayList<JoinPath> children;

	HashSet<Integer> pageNo;

	public JoinPath(TableAliasMapping aliases, EntityContext context) {
		this(aliases, false, null, context);
	}

	public JoinPath(TableAliasMapping aliases, boolean isRoot, EntityHandler rootEh, EntityContext context) {
		this.aliases = aliases;
		this.isRoot = isRoot;
		if (isRoot) {
			myAlias = aliases.getAlias(null);
			objDataTableName = ((GRdbEntityStoreRuntime) rootEh.getEntityStoreRuntime()).OBJ_STORE();
			taregtObjDefId = rootEh.getMetaData().getId();
			dataTenantId = context.getTenantId(rootEh);
		}
		joinWithVersionCol = true;
	}

	public void merge(JoinPath another) {
		isUse = isUse || another.isUse;
		joinWithVersionCol = joinWithVersionCol || another.joinWithVersionCol;
		joinRefWithDataTable = joinRefWithDataTable || another.joinRefWithDataTable;

		if (another.children != null) {
			if (children == null) {
				children = new ArrayList<JoinPath>();
			}
			for (JoinPath ac: another.children) {
				boolean find = false;
				for (JoinPath c: children) {
					if (c.name.equals(ac.name)) {
						find = true;
						c.merge(ac);
						break;
					}
				}
				if (!find) {
					children.add(ac);
				}
			}
		}
	}

	public JoinPath getJoinPath(String path) {
		if (children == null) {
			return null;
		}
		int dotIndex = path.indexOf('.');
		if (dotIndex > -1) {
			String childName = path.substring(0, dotIndex);
			for (JoinPath c: children) {
				if (c.name.equals(childName)) {
					return c.getJoinPath(path.substring(dotIndex + 1));
				}
			}
		} else {
			for (JoinPath c: children) {
				if (c.name.equals(path)) {
					return c;
				}
			}
		}
		return null;
	}


	private void checkVersionPropertyUsed(String propName, EntityHandler eh) {
		if (joinWithVersionCol) {
			if (eh.isVersioned()) {
				switch (propName) {
				case Entity.VERSION:
				case Entity.STATE:
				case Entity.START_DATE:
				case Entity.END_DATE:
					joinWithVersionCol = false;
					break;
				default:
					break;
				}
			}
		}
	}

	private void addPageNo(int pn) {
		if (pageNo == null) {
			pageNo = new HashSet<>();
		}
		pageNo.add(pn);
	}

	private void checkPageNo(String propName, EntityHandler eh, boolean useIndex) {
		PropertyHandler ph = eh.getDeclaredProperty(propName);
		if (ph != null && ph instanceof PrimitivePropertyHandler) {
			GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
			if (col != null) {
				if (!col.isNative()) {
					for (GRdbPropertyStoreHandler scol: col.asList()) {
						if (!useIndex || col.isMulti() || scol.getIndexColName() == null) {
							if (scol.getMetaData().getPageNo() > 0) {
								addPageNo(scol.getMetaData().getPageNo());
							}
						} else {
							if (scol.getMetaData().getIndexPageNo() > 0) {
								addPageNo(scol.getMetaData().getIndexPageNo());
							}
						}
					}
				}
			}
		}
	}

	//TODO 再帰のプログラムが汚いので後でリファクタリング

	public void addPath(String[] propPath, int currentPos, EntityContext context, EntityHandler dataModelHandler, boolean onCondition, boolean useIndex) {

		if (propPath.length == currentPos + 2) {
			//この結合先テーブルのプロパティを利用
			isUse = true;
			if (onCondition) {
				checkVersionPropertyUsed(propPath[currentPos + 1], dataModelHandler);
			}
			checkPageNo(propPath[currentPos + 1], dataModelHandler, useIndex);

		}
		if (propPath.length > currentPos + 2) {
			if (children == null) {
				children = new ArrayList<JoinPath>();
			}
			JoinPath matched = null;
			for (JoinPath j: children) {
				if (j.name.equals(propPath[currentPos + 1])) {
					matched = j;
					break;
				}
			}

			ReferencePropertyHandler pHandler = (ReferencePropertyHandler) dataModelHandler.getDeclaredProperty(propPath[currentPos + 1]);
			if (pHandler == null) {
				throw new NullPointerException(dataModelHandler.getMetaData().getName() + "." + propPath[currentPos + 1] + " not found");
			}
//			ReferenceDefinition refDef = (ReferenceDefinition) pHandler.getStoreSpecProperty();
//			ReferenceProperty refPropDef = (ReferenceProperty) pHandler.getDefinition();
			if (matched == null) {
				matched = new JoinPath(aliases, context);
				matched.name = propPath[currentPos + 1];

				//myAlias
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i <= currentPos + 1; i++) {
					if (i != 0) {
						sb.append(".");
					}
					sb.append(propPath[i]);
				}
				matched.myAlias = aliases.getAlias(sb.toString());

				matched.parent = this;
				ReferencePropertyHandler mappedBy = pHandler.getMappedByPropertyHandler(context);
				EntityHandler refEh = pHandler.getReferenceEntityHandler(context);
				matched.taregtObjDefId = refEh.getMetaData().getId();
				matched.isMappedBy = (mappedBy != null);
				if (matched.isMappedBy) {
					matched.refDefId = mappedBy.getId();
					matched.mappedByObjDefId = refEh.getMetaData().getId();
					matched.objRefTableName = ((GRdbEntityStoreRuntime) refEh.getEntityStoreRuntime()).OBJ_REF();
					matched.refTenantId = context.getTenantId(refEh);
				} else {
					matched.refDefId = pHandler.getId();
					matched.objRefTableName = ((GRdbEntityStoreRuntime) dataModelHandler.getEntityStoreRuntime()).OBJ_REF();
					matched.refTenantId = context.getTenantId(dataModelHandler);
				}
				
				if (dataModelHandler.isVersioned() && !matched.isMappedBy) {
					matched.joinRefWithDataTable = true;
					isUse = true;
				}

//				if (pHandler.getMetaData().getVersionControlType() == VersionControlReferenceType.RECORD_BASE) {
//					matched.joinWithVersionCol = true;
//				}
				matched.objDataTableName = ((GRdbEntityStoreRuntime) refEh.getEntityStoreRuntime()).OBJ_STORE();
				matched.dataTenantId = context.getTenantId(refEh);

				children.add(matched);
			}
			matched.addPath(propPath, currentPos + 1, context, pHandler.getReferenceEntityHandler(context), onCondition, useIndex);
		}
	}

	public void setAdditionalCondition(String additionalCondition, List<BindValue> bindVariables, AsOfSpec asOfSpec) {
		this.additionalCondition = additionalCondition;
		this.bindVariables = bindVariables;
		this.asOfSpec = asOfSpec;
	}

	public List<BindValue> getOrderedBindVariables() {
		if (children == null) {
			return bindVariables;
		}

		ArrayList<BindValue> ret = null;
		if (bindVariables != null) {
			ret = new ArrayList<>();
			ret.addAll(bindVariables);
		}
		for (JoinPath c: children) {
			List<BindValue> cblist = c.getOrderedBindVariables();
			if (cblist != null) {
				if (ret == null) {
					ret = new ArrayList<>();
				}
				ret.addAll(cblist);
			}
		}

		if (ret != null && ret.size() > 0) {
			return ret;
		} else {
			return null;
		}
	}

	public void appendJoinCause(StringBuilder sb, SqlQueryContext sqc) {

		//参照先を結合
		if (!isRoot) {
//			sb.append(" LEFT OUTER JOIN OBJ_REF \"");
			sb.append(" ").append(OUTER_JOIN).append(" ");
			sb.append(objRefTableName);
			sb.append(" ");
			appendPath(sb);
			sb.append("_r ");
			if (sqc.getRdb().isSupportTableHint()) {
				sqc.appendTableHint(myAlias + "_r", sb);
			}
			sb.append("ON ");
			//テナントID
//			appendRefTablePrefix(parent, sb);
//			sb.append("TENANT_ID=");
			appendRefTablePrefix(this, sb);
			sb.append("TENANT_ID=");
			sb.append(refTenantId);
			if (isMappedBy) {
				//オブジェクト定義
				sb.append(" AND ");
				appendRefTablePrefix(this, sb);
				sb.append("OBJ_DEF_ID='");
				sb.append(taregtObjDefId);
				sb.append("' AND ");
				appendRefTablePrefix(this, sb);
				sb.append("TARGET_OBJ_DEF_ID='");
				sb.append(parent.taregtObjDefId);
				sb.append("'");
			} else {
				sb.append(" AND ");
				appendRefTablePrefix(this, sb);
				sb.append("OBJ_DEF_ID='");
				sb.append(parent.taregtObjDefId);
				sb.append("' AND ");
				appendRefTablePrefix(this, sb);
				sb.append("TARGET_OBJ_DEF_ID='");
				sb.append(taregtObjDefId);
				sb.append("'");
			}
//			sb.append(" AND ");
//			appendRefColEquals("OBJ_DEF_ID", "TARGET_OBJ_DEF_ID", sb);
			//オブジェクトID
			sb.append(" AND ");
			if (joinRefWithDataTable) {
				//バージョン管理されている場合
				appendDataColEquals("OBJ_ID", "TARGET_OBJ_ID", sb);
				sb.append(" AND ");
				appendDataColEquals("OBJ_VER", "TARGET_OBJ_VER", sb);
			} else {
				appendRefColEquals("OBJ_ID", "TARGET_OBJ_ID", sb);
				sb.append(" AND ");
				appendRefColEquals("OBJ_VER", "TARGET_OBJ_VER", sb);
			}
			//リファレンス定義ID
			sb.append(" AND ");
			appendRefTablePrefix(this, sb);
			sb.append("REF_DEF_ID='");
			sb.append(refDefId);
			sb.append("'");

			if (isUse) {
//				sb.append(" LEFT OUTER JOIN OBJ_DATA \"");
				sb.append(" ").append(OUTER_JOIN).append(" ");
				sb.append(objDataTableName);
				sb.append(" ");
				appendPath(sb);
				sb.append(" ");
				if (sqc.getRdb().isSupportTableHint()) {
					sqc.appendTableHint(myAlias, sb);
				}
				sb.append("ON ");
				//テナントID
//				appendRefTablePrefix(this, sb);
//				sb.append("TENANT_ID=");
				appendPath(sb);
				sb.append(".TENANT_ID=");
				sb.append(dataTenantId);
				//オブジェクト定義
				sb.append(" AND ");
				appendPath(sb);
				sb.append(".OBJ_DEF_ID='");
				sb.append(taregtObjDefId);
				sb.append("'");
//				sb.append(" AND ");
//				appendRefTablePrefix(this, sb);
//				if (isMappedBy) {
//					sb.append("OBJ_DEF_ID");
//				} else {
//					sb.append("TARGET_OBJ_DEF_ID");
//				}
//				sb.append("=");
//				appendPath(sb);
//				sb.append(".OBJ_DEF_ID");
				//オブジェクトID
				sb.append(" AND ");
				appendRefTablePrefix(this, sb);
				if (isMappedBy) {
					sb.append("OBJ_ID");
				} else {
					sb.append("TARGET_OBJ_ID");
				}
				sb.append("=");
				appendPath(sb);
				sb.append(".OBJ_ID");
				//オブジェクトversion
				//version
				if (isRoot || joinWithVersionCol || isMappedBy || asOfSpec == AsOfSpec.UPDATE_TIME) {
					sb.append(" AND ");
					appendRefTablePrefix(this, sb);
					if (isMappedBy) {
						sb.append("OBJ_VER");
					} else {
						sb.append("TARGET_OBJ_VER");
					}
					sb.append("=");
					appendPath(sb);
					sb.append(".OBJ_VER");
				}
				//pageNo
				sb.append(" AND ");
				appendPath(sb);
				sb.append(".PG_NO=0");

				//追加の制限条件
				if (additionalCondition != null) {
					sb.append(" AND (");
					sb.append(additionalCondition);
					sb.append(")");
				}

			}
		}

		//複数ページに跨る場合、各ページを結合
		if (pageNo != null && pageNo.size() > 0) {
			for (Integer pn: pageNo) {
				sb.append(" ");
				if (isRoot) {
					sb.append(INNER_JOIN);
				} else {
					//参照先の場合は、外部結合されていることを考慮する必要あり
					sb.append(OUTER_JOIN);
				}
				sb.append(" ");
				sb.append(objDataTableName).append(" ");
				appendPath(sb);
				sb.append(PAGE_PREFIX).append(pn);
				sb.append(" ");
				if (sqc.getRdb().isSupportTableHint()) {
					sqc.appendTableHint(myAlias + PAGE_PREFIX + pn, sb);
				}


				sb.append("ON ");
//				appendPath(sb);
//				sb.append(".").append(ObjStoreTable.TENANT_ID);
//				sb.append("=");
				appendPath(sb);
				sb.append(PAGE_PREFIX).append(pn);
				sb.append(".").append(ObjStoreTable.TENANT_ID);
				sb.append("=");
				sb.append(dataTenantId);

				sb.append(" AND ");
//				appendPath(sb);
//				sb.append(".").append(ObjStoreTable.OBJ_DEF_ID);
//				sb.append("=");
				appendPath(sb);
				sb.append(PAGE_PREFIX).append(pn);
				sb.append(".").append(ObjStoreTable.OBJ_DEF_ID);
				sb.append("=");
				sb.append("'").append(taregtObjDefId).append("'");
				sb.append(" AND ");
				appendPath(sb);
				sb.append(".").append(ObjStoreTable.OBJ_ID);
				sb.append("=");
				appendPath(sb);
				sb.append(PAGE_PREFIX).append(pn);
				sb.append(".").append(ObjStoreTable.OBJ_ID);
				sb.append(" AND ");
				appendPath(sb);
				sb.append(".").append(ObjStoreTable.OBJ_VER);
				sb.append("=");
				appendPath(sb);
				sb.append(PAGE_PREFIX).append(pn);
				sb.append(".").append(ObjStoreTable.OBJ_VER);
				sb.append(" AND ");
				appendPath(sb);
				sb.append(PAGE_PREFIX).append(pn);
				sb.append(".").append(ObjStoreTable.PG_NO);
				sb.append("=").append(pn);
			}
		}

		if (children != null) {
			for (JoinPath c: children) {
				c.appendJoinCause(sb, sqc);
			}
		}
	}

	private void appendRefColEquals(String ownerColName, String targetColName, StringBuilder sb) {
		appendRefTablePrefix(parent, sb);
		if (parent.isRoot) {
			sb.append(ownerColName);
		} else {
			if (parent.isMappedBy) {
				sb.append(ownerColName);
			} else {
				sb.append(targetColName);
			}
		}
		sb.append("=");
		appendRefTablePrefix(this, sb);
		if (isMappedBy) {
			sb.append(targetColName);
		} else {
			sb.append(ownerColName);
		}
	}

	private void appendDataColEquals(String ownerColName, String targetColName, StringBuilder sb) {
		if (parent.isRoot) {
			sb.append(aliases.getAlias(null)).append(".");
		} else {
			parent.appendPath(sb);
			sb.append(".");
		}
		sb.append(ownerColName);
		sb.append("=");
		appendRefTablePrefix(this, sb);
		if (isMappedBy) {
			sb.append(targetColName);
		} else {
			sb.append(ownerColName);
		}
	}

	//TODO どこかのユーティリティクラスに移動
	private void appendRefTablePrefix(JoinPath table, StringBuilder sb) {
		if (table.isRoot) {
			sb.append(aliases.getAlias(null)).append(".");
		} else {
			table.appendPath(sb);
			sb.append("_r.");
		}
	}

	private void appendPath(StringBuilder sb) {
		sb.append(myAlias);
	}

}
