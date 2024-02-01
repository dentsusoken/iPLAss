/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import java.sql.Timestamp;
import java.util.Map;

import org.iplass.mtp.entity.fulltextsearch.FulltextSearchManager;
import org.iplass.mtp.spi.ServiceRegistry;

public class FulltextSearchManagerImpl implements FulltextSearchManager {

	FulltextSearchService service = ServiceRegistry.getRegistry().getService(FulltextSearchService.class);

	@Override
	public boolean isUseFulltextSearch() {
		return service.isUseFulltextSearch();
	}

	@Override
	public int getMaxRows() {
		return service.getMaxRows();
	}

	@Override
	public boolean isThrowExceptionWhenOverLimit() {
		return service.isThrowExceptionWhenOverLimit();
	}

	@Override
	public Map<String, Timestamp> getLastCrawlTimestamp(String... defNames) {
		return service.getLastCrawlTimestamp(defNames);
	}

	@Override
	public void crawlEntity(String defName) {
		service.execCrawlEntity(defName);
	}

	@Override
	public void crawlAllEntity() {
		service.execCrawlEntity();
	}

	@Override
	public void recrawlAllEntity() {
		service.deleteAllIndex();
		service.execCrawlEntity();
	}

	@Override
	public void refresh() {
		service.execRefresh();
	}

}
