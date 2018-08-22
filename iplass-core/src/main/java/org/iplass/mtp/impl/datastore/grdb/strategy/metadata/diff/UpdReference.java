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

import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaReferenceProperty;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class UpdReference extends Diff {
	
	private MetaReferenceProperty previousProperty;
	private MetaReferenceProperty nextProperty;
	private MetaEntity nextEntity;
	
	public UpdReference(MetaReferenceProperty previousProperty, MetaReferenceProperty nextProperty, MetaEntity nextEntity) {
		this.previousProperty = previousProperty;
		this.nextProperty = nextProperty;
		this.nextEntity = nextEntity;
	}
	
	
	private boolean eq(String a, String b) {
		if (a == null) {
			if (b != null) {
				return false;
			} else {
				return true;
			}
		}
		if (b == null) {
			if (a != null) {
				return false;
			}
		}
		return a.equals(b);
	}

	@Override
	public void applyToData(Statement stmt, RdbAdapter rdb, int tenantId) throws SQLException {
		
		if (!previousProperty.getReferenceEntityMetaDataId().equals(nextProperty.getReferenceEntityMetaDataId())
				|| !eq(previousProperty.getMappedByPropertyMetaDataId(), nextProperty.getMappedByPropertyMetaDataId())) {
			//FIXME 参照の削除
		}
		
	}

	@Override
	public void modifyMetaData() {
	}

	@Override
	public boolean needDataModify() {
		if (!previousProperty.getReferenceEntityMetaDataId().equals(nextProperty.getReferenceEntityMetaDataId())
				|| !eq(previousProperty.getMappedByPropertyMetaDataId(), nextProperty.getMappedByPropertyMetaDataId())) {
			return true;
		}
		
		return false;
	}

}
