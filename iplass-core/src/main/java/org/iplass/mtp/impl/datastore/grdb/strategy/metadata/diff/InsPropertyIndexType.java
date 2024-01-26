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

package org.iplass.mtp.impl.datastore.grdb.strategy.metadata.diff;

import java.sql.SQLException;
import java.sql.Statement;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.sql.IndexDeleteSql;
import org.iplass.mtp.impl.datastore.grdb.sql.IndexInsertSql;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class InsPropertyIndexType extends Diff {

	private MetaPrimitiveProperty nextProperty;
	private MetaEntity nextEntity;

	public InsPropertyIndexType(MetaPrimitiveProperty nextProperty, MetaEntity nextEntity) {
		this.nextProperty = nextProperty;
		this.nextEntity = nextEntity;
	}

	@Override
	public void applyToData(Statement stmt, RdbAdapter rdb, int tenantId) throws SQLException {

		if (needDataModify()) {
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

					stmt.executeBatch();
				}
			}
		}
	}

	@Override
	public void modifyMetaData() {
	}

	@Override
	public boolean needDataModify() {
		return nextProperty.getIndexType() != null && nextProperty.getIndexType() != IndexType.NON_INDEXED;
	}

}
