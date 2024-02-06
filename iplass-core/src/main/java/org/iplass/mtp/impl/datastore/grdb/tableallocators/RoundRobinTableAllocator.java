/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.grdb.tableallocators;

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.datastore.grdb.TableAllocator;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.spi.ServiceRegistry;

public class RoundRobinTableAllocator implements TableAllocator {

	@Override
	public int allocate(int tenantId, String metaId, StorageSpaceMap storage) {
		//tenant内のEntityでのラウンドロビン
		
		int[] counts = new int[storage.getTableCount()];
		
		EntityService es = ServiceRegistry.getRegistry().getService(EntityService.class);
		for (MetaDataEntryInfo minfo: es.list()) {
			if (!minfo.getId().equals(metaId)) {
				EntityHandler eh = es.getRuntimeById(minfo.getId());
				if (eh.getMetaData().getEntityStoreDefinition() instanceof MetaGRdbEntityStore) {
					MetaGRdbEntityStore estore = (MetaGRdbEntityStore) eh.getMetaData().getEntityStoreDefinition();
					int no = storage.tableNo(estore.getTableNamePostfix());
					if (no >= 0 && no < counts.length) {
						counts[no]++;
					}
				}
			}
		}
		
		int minIndex = 0;
		for (int i = 1; i < counts.length; i++) {
			if (counts[minIndex] > counts[i]) {
				minIndex = i;
			}
		}
		
		return minIndex;
	}
}
