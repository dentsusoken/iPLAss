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

package org.iplass.mtp.impl.rdb.mysql;

import java.sql.SQLException;
import java.sql.Statement;

import org.iplass.mtp.impl.rdb.adapter.MultiInsertContext;


public class MysqlMultiInsertContext extends MultiInsertContext {
	
	private StringBuilder sb;
	
//	private boolean isInsert;
	
	public MysqlMultiInsertContext(Statement stmt) {
		super(stmt);
		sb = new StringBuilder();
//		sb.append("INSERT ALL ");
//		isInsert = false;
	}
	
	@Override
	public void addInsertSql(String sql) throws SQLException {
		
//		if (sql.regionMatches(true,0,"INSERT",0,6)) {
//			sb.append(sql.substring(6));
//			sb.append(" ");
//			isInsert = true;
//		} else {
			super.addInsertSql(sql);
//		}
	}
	
	@Override
	public int[] execute() throws SQLException {
//		if (isInsert) {
//			sb.append(" SELECT 1 FROM DUAL");
//			super.addInsertSql(sb.toString());
//			
//		}
		return super.execute();
	}
	
	

}
