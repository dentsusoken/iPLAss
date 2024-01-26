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
package org.iplass.mtp.impl.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.iplass.mtp.Manager;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.spi.ServiceRegistry;

public class ManagerLocatorImpl extends ManagerLocator {
	
	private Map<Class<?>, Manager> map = new ConcurrentHashMap<>();
	
	@Override
	public <M extends Manager> M getManager(Class<M> type) {
		
		@SuppressWarnings("unchecked")
		M m = (M) map.get(type);
		
		if (m == null) {
			m = createManagerInstance(type);
			map.put(type, m);
		}
		return m;
	}
	
	protected <M extends Manager> M createManagerInstance(Class<M> type) {
		ManagerFactory f = ServiceRegistry.getRegistry().getService(ManagerFactory.class);
		return f.create(type);
	}

}
