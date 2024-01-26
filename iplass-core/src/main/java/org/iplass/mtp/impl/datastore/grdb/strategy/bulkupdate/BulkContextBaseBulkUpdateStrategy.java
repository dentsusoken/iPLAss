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
package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate;

import java.sql.Connection;
import java.sql.SQLException;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity;
import org.iplass.mtp.impl.datastore.grdb.strategy.GRdbEntityStoreStrategy;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public class BulkContextBaseBulkUpdateStrategy implements BulkUpdateStrategy {

	private GRdbEntityStoreStrategy storeStrategy;
	private RdbAdapter rdb;
	
	public BulkContextBaseBulkUpdateStrategy(GRdbEntityStoreStrategy storeStrategy, RdbAdapter rdb) {
		this.storeStrategy = storeStrategy;
		this.rdb = rdb;
	}

	@Override
	public void bulkUpdate(BulkUpdatable bulkUpdatable,
			EntityContext entityContext, EntityHandler entityHandler,
			String clientId) {
		
		final int tenantId = entityContext.getLocalTenantId();
		int buffSize = rdb.getBatchSize();//TODO 別定義がよいか？？
		
		try (Connection con = rdb.getConnection();
				BulkUpdateState updateState = new BulkUpdateState(storeStrategy, tenantId, clientId, entityContext, entityHandler, bulkUpdatable, con, rdb, buffSize)) {
			
			int count = 0;
			for (BulkUpdateEntity target: bulkUpdatable) {
				
				if (target.getEntity().getVersion() == null) {
					target.getEntity().setVersion(Long.valueOf(0));
				}
				if (target.getEntity().getState() == null) {
					target.getEntity().setValue(Entity.STATE, Entity.STATE_VALID_VALUE);
				}
				
				updateState.addToBuffer(target);
				count++;
				
				if (count >= buffSize) {
					updateState.doUpdate();
					count = 0;
				}
			}
			
			if (count > 0) {
				updateState.doUpdate();
			}
			
			updateState.flushAll();
			
		} catch (SQLException e) {
			throw new EntityRuntimeException(e);
		}
	}
	

}
