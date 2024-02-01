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

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.Manager;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class ManagerFactory implements Service {
	
	private Map<Class<?>, ManagerConstructor> map;

	@Override
	public void init(Config config) {
		
		map = new HashMap<>();
		for (String name: config.getNames()) {
			Class<?> ifType;
			try {
				ifType = Class.forName(name);
			} catch (ClassNotFoundException e) {
				throw new ServiceConfigrationException(e.toString(), e);
			}
			
			Object value = config.getBean(name);
			ManagerConstructor mc;
			if (value instanceof ManagerConstructor) {
				mc = (ManagerConstructor) value;
			} else {
				final Class<?> implType;
				try {
					implType = Class.forName((String) value);
				} catch (ClassNotFoundException e) {
					throw new ServiceConfigrationException(e.toString(), e);
				}
				if (!Manager.class.isAssignableFrom(ifType)) {
					throw new ServiceConfigrationException("interface type:" + ifType.getName() + " must extends/implements Manager interface");
				}
				if (!ifType.isAssignableFrom(implType)) {
					throw new ServiceConfigrationException(implType.getName() + " must extends/implements " + ifType.getName());
				}
				
				mc = () -> {
					try {
						return (Manager) implType.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new ServiceConfigrationException("Can't instanceate manager:" + implType.getName(), e);
					}
				};
			}
			
			map.put(ifType, mc);
		}
	}

	@Override
	public void destroy() {
	}
	
	public <M extends Manager> M create(Class<M> type) {
		ManagerConstructor constructor = map.get(type);
		if (constructor == null) {
			throw new ServiceConfigrationException("Manager:" + type.getName() + " not registered.");
		}
		
		@SuppressWarnings("unchecked")
		M m = (M) constructor.construct();
		return m;
	}
	
}
