/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.oracle;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

/**
 * Oracleの場合、PreparedStatementのsetDateで時間がセットされてしまうための対応Adapter。
 * サマータイム切替日などで時間がセットされてしまう。
 * 
 * @author K.Higuchi
 *
 */
public class OracleDateRdbTypeAdapter extends BaseRdbTypeAdapter.Date {

	public OracleDateRdbTypeAdapter(PropertyType propertyType) {
		super(propertyType);
	}

	@Override
	public void setParameter(int index, Object javaTypeValue,
			PreparedStatement stmt, RdbAdapter rdb) throws SQLException {
		java.sql.Date val = toRdb(javaTypeValue, rdb);
		if (val != null) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = fmt.format(val);
			stmt.setString(index, dateStr);
		} else {
			stmt.setString(index, null);
		}
	}

	@Override
	public void appendParameterPlaceholder(StringBuilder context, RdbAdapter rdb) {
		context.append("TO_DATE(?,'YYYY-MM-DD')");
	}
}
