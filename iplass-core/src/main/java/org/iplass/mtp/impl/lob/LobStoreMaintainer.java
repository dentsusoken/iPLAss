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
package org.iplass.mtp.impl.lob;

import org.iplass.mtp.impl.entity.AdditionalStoreMaintainer;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.properties.extend.BinaryType;
import org.iplass.mtp.spi.Config;

public class LobStoreMaintainer implements AdditionalStoreMaintainer {
	
	//FIXME longTextの場合を考慮してない？？

	@Override
	public void inited(EntityService service, Config config) {
	}

	@Override
	public void destroyed() {
	}

	@Override
	public void clean(int tenantId, String defId) {
		//LobのCleanup
		LobHandler lh = LobHandler.getInstance(BinaryType.LOB_STORE_NAME);
		lh.removeBinaryDataByDefId(tenantId, defId);
	}

	@Override
	public void defrag(int tenantId, EntityHandler eh) {
		//Lobのdefrag
		LobHandler lh = LobHandler.getInstance(BinaryType.LOB_STORE_NAME);
		lh.removeBinaryDataForDefrag(tenantId, eh);
	}

}
