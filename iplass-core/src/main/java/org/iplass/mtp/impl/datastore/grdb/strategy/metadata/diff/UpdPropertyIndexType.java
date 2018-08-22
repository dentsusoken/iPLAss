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

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbMultiplePropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.sql.IndexDeleteSql;
import org.iplass.mtp.impl.datastore.grdb.sql.IndexInsertSql;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class UpdPropertyIndexType extends Diff {

	private MetaPrimitiveProperty previousProperty;
	private MetaPrimitiveProperty nextProperty;
	private MetaEntity nextEntity;

	public UpdPropertyIndexType(MetaPrimitiveProperty previousProperty,
			MetaPrimitiveProperty nextProperty, MetaEntity nextEntity) {
		this.previousProperty = previousProperty;
		this.nextProperty = nextProperty;
		this.nextEntity = nextEntity;
	}

	@Override
	public void applyToData(Statement stmt, RdbAdapter rdb, int tenantId) throws SQLException {
		
		if (needDataModify()) {
			boolean doExecute = false;

			//previousのIndexの削除
			//今までの状態でIndexが残っている可能性もあるので、旧のIndexTypeにかかわらず削除は実施
			if (previousProperty.getEntityStoreProperty() instanceof MetaGRdbPropertyStore) {
				MetaGRdbPropertyStore propStore = (MetaGRdbPropertyStore) previousProperty.getEntityStoreProperty();
				if(propStore.isExternalIndex()) {
					IndexDeleteSql delSql = rdb.getUpdateSqlCreator(IndexDeleteSql.class);
					stmt.addBatch(delSql.deleteByColName(tenantId, nextEntity.getId(), ((MetaGRdbEntityStore) nextEntity.getEntityStoreDefinition()).getTableNamePostfix(), propStore.getPageNo(), propStore.getColumnName(), rdb.getRdbTypeAdapter(previousProperty.getType()), previousProperty.getIndexType(), rdb));
					doExecute = true;
				}
			}

			//nextのIndexの生成
			if (nextProperty.getEntityStoreProperty() instanceof MetaGRdbPropertyStore) {
				MetaGRdbPropertyStore propStore = (MetaGRdbPropertyStore) nextProperty.getEntityStoreProperty();
				if(propStore.isExternalIndex()) {
					BaseRdbTypeAdapter typeMapping = (BaseRdbTypeAdapter) rdb.getRdbTypeAdapter(nextProperty.getType());
					//もし、このカラムが再利用カラムだったら、insertIndexするまえにdeleteIndex
					//カラム数を拡張された場合は再利用カラムかどうか判断つかないので、一律事前に削除
					IndexDeleteSql delSql = rdb.getUpdateSqlCreator(IndexDeleteSql.class);
					stmt.addBatch(delSql.deleteByColName(tenantId, nextEntity.getId(), ((MetaGRdbEntityStore) nextEntity.getEntityStoreDefinition()).getTableNamePostfix(), propStore.getPageNo(), propStore.getColumnName(), typeMapping, nextProperty.getIndexType(), rdb));
					IndexInsertSql insSql = rdb.getUpdateSqlCreator(IndexInsertSql.class);
					stmt.addBatch(insSql.insertAll(tenantId, nextEntity.getId(), ((MetaGRdbEntityStore) nextEntity.getEntityStoreDefinition()).getTableNamePostfix(), propStore.getPageNo(), propStore.getColumnName(), nextProperty.getIndexType(), typeMapping, rdb));
					doExecute = true;
				}
			}

			if (doExecute) {
				stmt.executeBatch();
			}
		}

	}

	@Override
	public void modifyMetaData() {
	}

	@Override
	public boolean needDataModify() {

		if (previousProperty.getIndexType() != nextProperty.getIndexType()) {
			return true;
		}

		if (!previousProperty.getType().isCompatibleTo(nextProperty.getType())) {
			if (!(previousProperty.getType().isVirtual() && nextProperty.getType().isVirtual())) {
				return true;
			}
		}
		if (!nextProperty.getType().isVirtual()) {
			if (nextProperty.getMultiplicity() != previousProperty.getMultiplicity()) {
				return true;
			}
			if (isExternalIndex(nextProperty) != isExternalIndex(previousProperty)) {
				return true;
			}
		}

		return false;
	}
	
	private boolean isExternalIndex(MetaPrimitiveProperty p) {
		if (p.getEntityStoreProperty() instanceof MetaGRdbMultiplePropertyStore) {
			return false;
		} else if (p.getEntityStoreProperty() instanceof MetaGRdbPropertyStore) {
			return ((MetaGRdbPropertyStore) p.getEntityStoreProperty()).isExternalIndex();
		}
		return false;
	}

}
