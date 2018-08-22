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

import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class DelProperty extends Diff {
	
	private MetaProperty deletedProperty;
	
	public DelProperty(MetaProperty deletedProperty) {
		this.deletedProperty = deletedProperty;
	}

	@Override
	public void applyToData(Statement stmt, RdbAdapter rdb, int tenantId) throws SQLException {
		//MetaDataから削除時にはデータは消さない。（不用意な削除からデータ復旧できるように。）
	}

	@Override
	public void modifyMetaData() {
	}

	@Override
	public boolean needDataModify() {
		return false;
	}

}
