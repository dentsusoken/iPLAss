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
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColResolver;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaReferenceProperty;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public class InsReference extends Diff {
	
	private MetaReferenceProperty insertProperty;
	private ColResolver colResolver;
	private MetaEntity entity;
	
	public InsReference(MetaReferenceProperty insertProperty, MetaEntity entity, ColResolver colResolver) {
		this.insertProperty = insertProperty;
		this.colResolver = colResolver;
		this.entity = entity;
	}

	@Override
	public void modifyMetaData() {
		insertProperty.setIndexType(IndexType.NON_INDEXED);
		insertProperty.setEntityStoreProperty(null);
	}

	@Override
	public void applyToData(Statement stmt, RdbAdapter rdb, int tenantId) throws SQLException {
	}

	@Override
	public boolean needDataModify() {
		return false;
	}

}
