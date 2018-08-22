/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformerSupport;
import org.iplass.mtp.entity.query.condition.predicate.Contains;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.spi.ServiceRegistry;

public class FulltextSearchQueryASTTransformer extends ASTTransformerSupport {

	private String defName;
	private String searchText;
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	private final Map<String, Map<String, List<String>>> highlightMap = new HashMap<String, Map<String, List<String>>>();

	public Map<String, Map<String, List<String>>> getHighlightMap() {
		return highlightMap;
	}

	public FulltextSearchQueryASTTransformer(String defName) {
		this.defName = defName;
	}

	@Override
	public ASTNode visit(Contains contains) {

		searchText = contains.getSearchText();

		FulltextSearchService fulltextSearchService = ServiceRegistry.getRegistry().getService(FulltextSearchService.class);

		List<FulltextSearchResult> resultList = fulltextSearchService.execFulltextSearch(defName, searchText);

		if (resultList.size() < 1) {
			return new In("oid", "");
		}

		Object[] oidArr = new Object[resultList.size()];
		int cnt = 0;
		for (FulltextSearchResult result : resultList) {
			String oid = result.getOid();
			highlightMap.put(oid, result.getHighlight());
			oidArr[cnt] = oid;
			cnt ++;
		}

		return new In("oid", oidArr);
	}
	
}
