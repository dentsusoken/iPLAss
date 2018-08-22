/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity;

import java.util.List;

import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.hint.FetchSizeHint;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.TimeoutHint;

public class QueryOption {
	
	private int fetchSize = 0;
	private int queryTimeout = 0;

	public QueryOption() {
	}
	
	public QueryOption(int fetchSize) {
		this.fetchSize = fetchSize;
	}
	
	public int getQueryTimeout() {
		return queryTimeout;
	}

	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}
	
	public static QueryOption getQueryOption(Query query) {
		QueryOption qo = null;
		if (query.getSelect().getHintComment() != null) {
			List<Hint> list = query.getSelect().getHintComment().getHintList();
			if (list != null) {
				for (Hint h: list) {
					if (h instanceof FetchSizeHint) {
						if (qo == null) {
							qo = new QueryOption();
						}
						qo.setFetchSize(((FetchSizeHint) h).getSize());
					} else if (h instanceof TimeoutHint) {
						if (qo == null) {
							qo = new QueryOption();
						}
						qo.setQueryTimeout(((TimeoutHint) h).getSeconds());
					}
				}
			}
		}
		
		return qo;
	}

	
}
