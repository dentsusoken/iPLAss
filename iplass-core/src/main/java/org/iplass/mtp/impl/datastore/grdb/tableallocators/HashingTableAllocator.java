/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.datastore.grdb.TableAllocator;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class HashingTableAllocator implements TableAllocator {
	private boolean useMetaDataId = true;
	private boolean useTenantId = true;

	public boolean isUseMetaDataId() {
		return useMetaDataId;
	}

	public void setUseMetaDataId(boolean useMetaDataId) {
		this.useMetaDataId = useMetaDataId;
	}

	public boolean isUseTenantId() {
		return useTenantId;
	}

	public void setUseTenantId(boolean useTenantId) {
		this.useTenantId = useTenantId;
	}

	@Override
	public int allocate(int tenantId, String metaId, StorageSpaceMap storage) {
		return Math.abs(toKey(tenantId, metaId).hashCode() % storage.getTableCount());
	}
	
	private String toKey(int tenantId, String metaId) {
		if (useMetaDataId) {
			if (useTenantId) {
				return metaId + tenantId;
			} else {
				return metaId;
			}
		} else {
			if (useTenantId) {
				return Integer.toString(tenantId);
			} else {
				throw new ServiceConfigrationException("At least useMetaDataId or useTenantId must be specified to true.");
			}
		}
	}

}
