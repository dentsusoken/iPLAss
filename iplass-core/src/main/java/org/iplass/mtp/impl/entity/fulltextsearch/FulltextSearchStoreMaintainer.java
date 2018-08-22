/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.entity.fulltextsearch;

import java.sql.SQLException;

import org.iplass.mtp.impl.entity.AdditionalStoreMaintainer;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.fulltextsearch.FulltextSearchService;
import org.iplass.mtp.impl.fulltextsearch.sql.CrawlLogDeleteSql;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogDeleteSql;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;


public class FulltextSearchStoreMaintainer implements AdditionalStoreMaintainer {
	private RdbAdapter rdb;
	private CrawlLogDeleteSql crawlLogDeleteSql;
	private DeleteLogDeleteSql deleteLogDeleteSql;

	@Override
	public void inited(EntityService service, Config config) {
		rdb = ServiceRegistry.getRegistry().getService(RdbAdapterService.class).getRdbAdapter();
		crawlLogDeleteSql = rdb.getUpdateSqlCreator(CrawlLogDeleteSql.class);
		deleteLogDeleteSql = rdb.getUpdateSqlCreator(DeleteLogDeleteSql.class);
	}

	@Override
	public void destroyed() {
	}

	@Override
	public void clean(int tenantId, String defId) {
		FulltextSearchService fulltextSearchService = ServiceRegistry.getRegistry().getService(FulltextSearchService.class);
		if (fulltextSearchService.isUseFulltextSearch()) {
			
			new SqlExecuter<Void>() {
				@Override
				public Void logic() throws SQLException {
					//CrawlLog
					String sql1 = crawlLogDeleteSql.deleteByDefId(tenantId, defId, rdb);
					getStatement().executeUpdate(sql1);
					//DeleteLog
					String sql2 = deleteLogDeleteSql.deleteByDefId(tenantId, defId, rdb);
					getStatement().executeUpdate(sql2);
					return null;
				}
			}.execute(rdb, true);
			
		}
	}

	@Override
	public void defrag(int tenantId, EntityHandler eh) {
		//TODO 実装必要？
	}

}
