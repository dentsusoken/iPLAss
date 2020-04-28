/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.adapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.impl.entity.property.PropertyType;


public class WrapperRdbTypeAdapter extends BaseRdbTypeAdapter {
	
	private BaseRdbTypeAdapter wrappedAdapter;

	public WrapperRdbTypeAdapter(PropertyType propertyType, BaseRdbTypeAdapter wrappedAdapter) {
		super(propertyType);
		this.wrappedAdapter = wrappedAdapter;
	}

	@Override
	public String getColOfIndex() {
		return wrappedAdapter.getColOfIndex();
	}

	@Override
	public int sqlType() {
		return wrappedAdapter.sqlType();
	}

	@Override
	protected Object toJava(ResultSet rs, int columnIndex, RdbAdapter rdb) throws SQLException {
		return wrappedAdapter.toJava(rs, columnIndex, rdb);
	}

	@Override
	protected Object toRdb(Object javaTypeValue, RdbAdapter rdb) {
		return wrappedAdapter.toRdb(javaTypeValue, rdb);
	}

//	@Override
//	public boolean needCastExpFromString() {
//		return wrappedAdapter.needCastExpFromString();
//	}

	@Override
	public void setParameter(int index, Object javaTypeValue,
			PreparedStatement stmt, RdbAdapter rdb) throws SQLException {
		Object val = propertyType.toDataStore(javaTypeValue);
		wrappedAdapter.setParameter(index, val, stmt, rdb);
	}

}
