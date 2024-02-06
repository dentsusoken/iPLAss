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
package org.iplass.mtp.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.iplass.mtp.Manager;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.core.ManagerLocatorImpl;

/**
 * テスト時に利用されるManagerLocatorの実装です。
 *
 * @author K.Higuchi
 *
 */
public class TestManagerLocator extends ManagerLocator {

	private ConcurrentHashMap<Class<?>, Object> mocks = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class<?>, Object> proxyCache = new ConcurrentHashMap<>();

	private ManagerLocatorImpl impl = new ManagerLocatorImpl();

	public <T> void setManager(Class<T> managerInterface, T mock) {
		if (mock != null && !managerInterface.isAssignableFrom(mock.getClass())) {
			throw new IllegalArgumentException(mock.getClass().getName() + " can't assign to " + managerInterface.getName());
		}

		mocks.put(managerInterface, mock);
	}

	public void reset() {
		mocks.clear();
	}

	@SuppressWarnings("unchecked")
	private <T> T getProxy(Class<T> managerInterface, Supplier<T> factoryMethod) {

		T instance = (T) proxyCache.get(managerInterface);
		if (instance == null) {
			instance = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
					new Class<?>[]{managerInterface},
					(proxy, method, args) -> {
						try {
							T manager = (T) mocks.get(managerInterface);
							if (manager != null) {
								return method.invoke(manager, args);
							} else {
								manager = factoryMethod.get();
								return method.invoke(manager, args);
							}

						} catch (InvocationTargetException e) {
							throw e.getCause();
						}
					});
			proxyCache.put(managerInterface, instance);
		}

		return instance;
	}
	
	@Override
	public <M extends Manager> M getManager(Class<M> type) {
		return getProxy(type, () -> impl.getManager(type));
	}

}
