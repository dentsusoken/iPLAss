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

package org.iplass.mtp.impl.datastore.grdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;

public interface GRdbPropertyStoreRuntime {
	
	public BaseRdbTypeAdapter getSingleColumnRdbTypeAdapter();
	public int getColCount();
	public boolean isNative();
	public boolean isExternalIndex();
	public PrimitivePropertyHandler getPropertyRuntime();
	public Object fromDataStore(ResultSet rs, int colNum) throws SQLException;
	public boolean isMulti();
	public List<GRdbPropertyStoreHandler> asList();

}
