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
package org.iplass.mtp.impl.entity;

import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.query.Query;

public class ExtendPropertyAdapterFactory {
	
	public VirtualPropertyAdapter create(Query query, EntityContext ec, EntityHandler eh) {
		return new VirtualPropertyAdapter(query, ec, eh);
	}
	
	public VirtualPropertyAdapter create(UpdateCondition updateCond, EntityContext ec, EntityHandler eh) {
		return new VirtualPropertyAdapter(updateCond, ec, eh);
	}
	
	public VirtualPropertyAdapter create(DeleteCondition deleteCond, EntityContext ec, EntityHandler eh) {
		return new VirtualPropertyAdapter(deleteCond, ec, eh);
	}

}
