/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.postgresql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

/**
 * Postgreの場合、PreparedStatementのsetTimeでTimestamp型カラムに保存する場合にエラーとなるのを回避。
 * 
 * @author K.Higuchi
 *
 */
public class PostgreSQLTimeRdbTypeAdapter extends BaseRdbTypeAdapter.Time {

	public PostgreSQLTimeRdbTypeAdapter(PropertyType propertyType) {
		super(propertyType);
	}

	@Override
	public void setParameter(int index, Object javaTypeValue,
			PreparedStatement stmt, RdbAdapter rdb) throws SQLException {
		java.sql.Time val = toRdb(javaTypeValue, rdb);
		if (val != null) {
			//日付部分を切り捨て
			SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
			val = java.sql.Time.valueOf(fmt.format(val));
			stmt.setTimestamp(index, new Timestamp(val.getTime()));
		} else {
			stmt.setTimestamp(index, null);
		}
	}

}
