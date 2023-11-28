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

package org.iplass.mtp.impl.datastore.grdb.strategy.metadata.diff;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreMaintenanceSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColContext.ColCopy;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColResolver;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.MetaRdbColumnMapping;
import org.iplass.mtp.impl.entity.MetaSchemalessRdbStoreMapping;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.property.MetaReferenceProperty;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UpdEntity extends Diff {

	private static Logger logger = LoggerFactory.getLogger(UpdEntity.class);

	private MetaEntity previousEntity;
	private MetaEntity nextEntity;

	private ColResolver colResolver;

	private EntityContext context;
//	private int tableCount;
//	private Map<String, StorageSpaceMap> storageSpaceMap;
	private StorageSpaceMap storage;
	private StorageSpaceMap previousStorage;

	private List<Diff> propertyList;

	/** 強制的なテーブル名接尾辞の再生成（true の場合に再生成する） */
	private boolean forceRegenerateTableNamePostfix;

	private List<MetaProperty> getEmptyListIfNull(List<MetaProperty> list) {
		if (list == null) {
			return new ArrayList<MetaProperty>();
		} else {
			return list;
		}
	}

	public UpdEntity(MetaEntity previousEntity, MetaEntity nextEntity, EntityContext context, StorageSpaceMap previousStorage,
			StorageSpaceMap nextStorage, RdbAdapter rdb, boolean forceRegenerateTableNamePostfix) {
		this.previousEntity = previousEntity;
		this.nextEntity = nextEntity;
		this.context = context;
		this.storage = nextStorage;
		this.previousStorage = previousStorage;
		this.colResolver = new ColResolver(previousEntity, (MetaSchemalessRdbStoreMapping) nextEntity.getStoreMapping(), storage, rdb);
		this.forceRegenerateTableNamePostfix = forceRegenerateTableNamePostfix;

		propertyList = new ArrayList<Diff>();
		List<MetaProperty> nextPropList = getEmptyListIfNull(nextEntity.getDeclaredPropertyList());
		nextEntity.setDeclaredPropertyList(nextPropList);//TODO 空のListでMetaEntity側で初期化する？
		List<MetaProperty> previousPropList = new ArrayList<MetaProperty>(getEmptyListIfNull(previousEntity.getDeclaredPropertyList()));
		for (MetaProperty np: nextPropList) {
			boolean isMatched = false;
			Iterator<MetaProperty> ppIt = previousPropList.iterator();
			while (ppIt.hasNext()) {
				MetaProperty pp = ppIt.next();
				if (pp.getId().equals(np.getId())) {
					isMatched = true;
					addUpd(pp, np);
					ppIt.remove();
					break;
				}
			}
			if (isMatched == false) {
				addIns(np);
			}
		}

		for (MetaProperty dp: previousPropList) {
			addDel(dp);
		}
	}

	public UpdEntity(MetaEntity previousEntity, MetaEntity nextEntity, EntityContext context, StorageSpaceMap previousStorage,
			StorageSpaceMap nextStorage, RdbAdapter rdb) {
		this(previousEntity, nextEntity, context, previousStorage, nextStorage, rdb, false);
	}

	private void addDel(MetaProperty del) {
		DelProperty delP = new DelProperty(del);
		propertyList.add(delP);
	}
	private void addIns(MetaProperty ins) {
		if (ins instanceof MetaPrimitiveProperty) {
			InsProperty insP = new InsProperty((MetaPrimitiveProperty) ins, nextEntity, colResolver);
			propertyList.add(insP);
		} else if (ins instanceof MetaReferenceProperty) {
			InsReference insR = new InsReference((MetaReferenceProperty) ins, nextEntity, colResolver);
			propertyList.add(insR);
		}
	}
	private void addUpd(MetaProperty pre, MetaProperty next) {
		if (!pre.equals(next) || isMappingChanged(next.getId())) {
			logger.debug("diff update (prev:{} next:{})", pre.getName(), next.getName());

			if (pre.getClass() != next.getClass()) {
				throw new IllegalArgumentException("Primitive to Reference can not convert, or vice versa.");
			}

			if (pre instanceof MetaPrimitiveProperty) {
				UpdProperty updP = new UpdProperty((MetaPrimitiveProperty) pre, (MetaPrimitiveProperty) next, nextEntity, colResolver);
				propertyList.add(updP);
			} else {
				UpdReference updP = new UpdReference((MetaReferenceProperty) pre, (MetaReferenceProperty) next, nextEntity);
				propertyList.add(updP);
			}
		}
	}
	private boolean isMappingChanged(String propId) {
		String preMappedCol = mappedColName((MetaSchemalessRdbStoreMapping) previousEntity.getStoreMapping(), propId);
		String nextMappedCol = mappedColName((MetaSchemalessRdbStoreMapping) nextEntity.getStoreMapping(), propId);
		return !preMappedCol.equals(nextMappedCol);
	}
	private String mappedColName(MetaSchemalessRdbStoreMapping map, String propId) {
		if (map == null) {
			return "";
		} else {
			List<MetaRdbColumnMapping> list = map.getColumnMappingList();
			if (list != null) {
				for (MetaRdbColumnMapping cm: list) {
					if (propId.equals(cm.getPropertyId())) {
						return cm.getColumnName();
					}
				}
			}
			return "";
		}
	}


	@Override
	public void applyToData(Statement stmt, RdbAdapter rdb, int tenantId) throws SQLException {
		if (needDataModify()) {

			ObjStoreMaintenanceSql sc =rdb.getUpdateSqlCreator(ObjStoreMaintenanceSql.class);

			MetaGRdbEntityStore storeDef = (MetaGRdbEntityStore) nextEntity.getEntityStoreDefinition();


			switch (rdb.getMultiTableUpdateMethod()) {
			case INLINE_VIEW:
				//TODO ORA-01792の発生を防ぐように実装が必要。。。
			case NO_SUPPORT:
				//まずはpageNo=0を更新
				List<ColCopy> ccl = colResolver.getColContext().getColCopyList(0);
				stmt.executeUpdate(sc.updateCol(tenantId, nextEntity.getId(), storeDef.getVersion(), 0, ccl, storeDef.getTableNamePostfix(), rdb));
				stmt.executeUpdate(sc.updateColRB(tenantId, nextEntity.getId(), storeDef.getVersion(), 0, ccl, storeDef.getTableNamePostfix(), rdb));

				//pageNoが増えた場合、新規pageをinsert
				int prePageNo = ((MetaGRdbEntityStore) previousEntity.getEntityStoreDefinition()).currentMaxPage();
				int nextPageNo = storeDef.currentMaxPage();

				if (nextPageNo > prePageNo) {
					for (int i = prePageNo + 1; i <= nextPageNo; i++) {
						stmt.executeUpdate(sc.insertNewPage(tenantId, nextEntity.getId(), i, storeDef.getTableNamePostfix(), rdb));
						stmt.executeUpdate(sc.insertNewPageRB(tenantId, nextEntity.getId(), i, storeDef.getTableNamePostfix(), rdb));
					}
				}

				//pageNo=1以降を更新
				for (int i = 1; i <= nextPageNo; i++) {
					ccl = colResolver.getColContext().getColCopyList(i);
					if (ccl != null) {
						stmt.executeUpdate(sc.updateCol(tenantId, nextEntity.getId(), storeDef.getVersion(), i, ccl, storeDef.getTableNamePostfix(), rdb));
						stmt.executeUpdate(sc.updateColRB(tenantId, nextEntity.getId(), storeDef.getVersion(), i, ccl, storeDef.getTableNamePostfix(), rdb));
					}
				}
				break;
			case DIRECT_JOIN:
				//まずはpageNo=0を更新（レコードロックのみ行う）
				stmt.executeUpdate(sc.updateCol(tenantId, nextEntity.getId(), storeDef.getVersion(), 0, null, storeDef.getTableNamePostfix(), rdb));
				stmt.executeUpdate(sc.updateColRB(tenantId, nextEntity.getId(), storeDef.getVersion(), 0, null, storeDef.getTableNamePostfix(), rdb));

				//pageNoが増えた場合、新規pageをinsert
				prePageNo = ((MetaGRdbEntityStore) previousEntity.getEntityStoreDefinition()).currentMaxPage();
				nextPageNo = storeDef.currentMaxPage();

				if (nextPageNo > prePageNo) {
					for (int i = prePageNo + 1; i <= nextPageNo; i++) {
						stmt.executeUpdate(sc.insertNewPage(tenantId, nextEntity.getId(), i, storeDef.getTableNamePostfix(), rdb));
						stmt.executeUpdate(sc.insertNewPageRB(tenantId, nextEntity.getId(), i, storeDef.getTableNamePostfix(), rdb));
					}
				}

				//pageNo=0以降を一括更新
				@SuppressWarnings("unchecked")
				List<ColCopy>[] ccls = new List[nextPageNo + 1];
				for (int i = 0; i <= nextPageNo; i++) {
					ccls[i] = colResolver.getColContext().getColCopyList(i);
				}
				stmt.executeUpdate(sc.updateColDirectJoin(tenantId, nextEntity.getId(), storeDef.getVersion(), ccls, storeDef.getTableNamePostfix(), rdb));
				stmt.executeUpdate(sc.updateColDirectJoinRB(tenantId, nextEntity.getId(), storeDef.getVersion(), ccls, storeDef.getTableNamePostfix(), rdb));
				break;
			default:
				break;
			}

			if (propertyList != null) {
				for (Diff p: propertyList) {
					p.applyToData(stmt, rdb, tenantId);
				}
			}
		}

		//FIXME データ整合性チェック＆修正プロセスを別トランザクション（メタデータを更新済みにした状態）で。ctrlテーブルのステータスにチェック中を追加
		//		・pageNo間で欠けているものないか？⇒pageNo=0から外部結合してoidがnullの列がないか？
		//		・Index項目（Obj_Store内）と、実データ項目で値がずれてないか？
		//		・外部Indexテーブルと、実データ項目で値がずれてないか？
		//FIXME もう少し拡張して、ロックしない形でindexの更新を可能にできないか？

	}

	@Override
	public void modifyMetaData() {
		MetaGRdbEntityStore preStoreDef = (MetaGRdbEntityStore) previousEntity.getEntityStoreDefinition();
		MetaGRdbEntityStore nextStoreDef = colResolver.getMetaStore();
		nextStoreDef.setVersion(preStoreDef.getVersion() + 1);

		if (isNotForceRegenerateTableNamePostfix() && previousStorage.getStorageSpaceName().equals(storage.getStorageSpaceName())) {
			nextStoreDef.setTableNamePostfix(preStoreDef.getTableNamePostfix());
		} else {
			// 強制的なテーブル名接尾辞の再生成しないが false もしくは、ストレージスペース名が異なる場合
			nextStoreDef.setTableNamePostfix(storage.generateTableNamePostfix(context.getLocalTenantId(), nextEntity.getId()));
		}
		nextEntity.setEntityStoreDefinition(nextStoreDef);

		if (propertyList != null) {
			for (Diff p: propertyList) {
				p.modifyMetaData();
			}
		}
	}

	@Override
	public boolean needDataModify() {

		int prePageNo = ((MetaGRdbEntityStore) previousEntity.getEntityStoreDefinition()).currentMaxPage();
		int nextPageNo = ((MetaGRdbEntityStore) nextEntity.getEntityStoreDefinition()).currentMaxPage();
		if (nextPageNo > prePageNo) {
			return true;
		}
		if (colResolver.getColContext().hasColCopy()) {
			return true;
		}

		boolean isDataModify = false;
		if (propertyList != null) {
			for (Diff p: propertyList) {
				isDataModify |= p.needDataModify();
			}
		}
		return isDataModify;
	}

	/**
	 * 強制的なテーブル名接尾辞の再生成を実施しないかを判定する。
	 * @return true の場合、強制的なテーブル名接尾辞の再生成を実施しない
	 */
	private boolean isNotForceRegenerateTableNamePostfix() {
		return !forceRegenerateTableNamePostfix;
	}

}
