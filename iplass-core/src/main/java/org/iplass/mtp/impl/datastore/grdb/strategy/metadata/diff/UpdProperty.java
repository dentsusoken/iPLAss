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
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class UpdProperty extends Diff {
	
	private UpdPropertyIndexType updPropertyIndexType;
	
	private MetaPrimitiveProperty previousProperty;
	private MetaPrimitiveProperty nextProperty;
	
	private ColResolver colResolver;
	private MetaEntity nextEntity;
	
	public UpdProperty(MetaPrimitiveProperty previousProperty, MetaPrimitiveProperty nextProperty, MetaEntity nextEntity, ColResolver colResolver) {
		this.previousProperty = previousProperty;
		this.nextProperty = nextProperty;
		this.colResolver = colResolver;
		this.nextEntity = nextEntity;
		
		if ((previousProperty.getIndexType() != null &&	previousProperty.getIndexType() != IndexType.NON_INDEXED)
				|| (nextProperty.getIndexType() != null && nextProperty.getIndexType() != IndexType.NON_INDEXED)) {
			updPropertyIndexType = new UpdPropertyIndexType(previousProperty, nextProperty, nextEntity);
		}
	}
	
	@Override
	public void applyToData(Statement stmt, RdbAdapter rdb, int tenantId) throws SQLException {
		if (updPropertyIndexType != null) {
			updPropertyIndexType.applyToData(stmt, rdb, tenantId);
		}
		
	}

	@Override
	public void modifyMetaData() {
		if (nextProperty.getMultiplicity() > 1) {
			nextProperty.setIndexType(IndexType.NON_INDEXED);
		}
		colResolver.allocateCol(nextProperty, previousProperty, nextEntity.getVersionControlType());
		if (updPropertyIndexType != null) {
			updPropertyIndexType.modifyMetaData();
		}
	}

	@Override
	public boolean needDataModify() {
		if (updPropertyIndexType != null && updPropertyIndexType.needDataModify()) {
			return true;
		}
		
		return false;
	}

}
