/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class MultiInsertContext {

	//TODO Oracleのマルチテーブルインサート対応

	private Statement stmt;
	private List<String> sqls;

	public MultiInsertContext(Statement stmt) {
		this.stmt = stmt;
		sqls = new ArrayList<String>();
	}

	/**
	 * insert文を一括で投げるため、実行順の組み換えがあっても問題ないInsertだけ登録する。
	 * 組みかえられる場合は、insertは一番最後に実行される。
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public void addInsertSql(String sql) throws SQLException {
		sqls.add(sql);
//		stmt.addBatch(sql);
	}

	public boolean isSqlAdded() {
		return sqls.size() != 0;
	}

	public int[] execute() throws SQLException {
		if (sqls.size() == 0) {
			throw new SQLException("no sql added");
		}
		if (sqls.size() == 1) {
			return new int[]{stmt.executeUpdate(sqls.get(0))};
		}

		for (String s: sqls) {
			stmt.addBatch(s);
		}
		return stmt.executeBatch();
	}

	public void addUpdateSql(String sql) throws SQLException {
		sqls.add(sql);
//		stmt.addBatch(sql);
	}



}
