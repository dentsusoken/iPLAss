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

package org.iplass.mtp.impl.datastore.grdb.sql.queryconvert;

import java.util.HashMap;

public class TableAliasMapping {
	private static final String PREFIX = "t";
	
	private HashMap<String, String> refNameTableAliasMap;
	private String prefix;
	private int count;
	
	public TableAliasMapping(String prefix) {
		this.prefix = prefix;
		refNameTableAliasMap = new HashMap<String, String>();
	}
	
	public String getAlias(String refName) {
		if (refName == null) {
			return prefix;
		}
		
		String alias = refNameTableAliasMap.get(refName);
		if (alias == null) {
			alias = prefix + PREFIX + count;
			count++;
			refNameTableAliasMap.put(refName, alias);
		}
		return alias;
	}
}
