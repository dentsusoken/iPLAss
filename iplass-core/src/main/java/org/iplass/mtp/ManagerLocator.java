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
package org.iplass.mtp;

import org.iplass.mtp.impl.core.ManagerLocatorImpl;

/**
 * ManagerのServiceLocatorです。
 * iPLAssが提供するManagerのインスタンスを取得可能です。
 * 
 * @author K.Higuchi
 *
 */
public abstract class ManagerLocator {
	public static final String MANAGER_LOCATOR_SYSTEM_PROPERTY_NAME = "mtp.managerlocator";

	private static final class Holder {
		private static final ManagerLocator instance;
		static {
			String managerLocatorName = System.getProperty(MANAGER_LOCATOR_SYSTEM_PROPERTY_NAME);
			if (managerLocatorName != null) {
				try {
					instance = (ManagerLocator) Class.forName(managerLocatorName).newInstance();
				} catch (InstantiationException | IllegalAccessException
						| ClassNotFoundException e) {
					throw new SystemException(managerLocatorName + " can't instanceate.");
				}
			} else {
				instance = new ManagerLocatorImpl();
			}
		}
	}
	
	/**
	 * ManagerLocator自体のインスタンスを取得します。
	 *
	 * @return ServiceLocator
	 */
	public static ManagerLocator getInstance() {
		return Holder.instance;
	}
	
	/**
	 * Managerのインスタンスを取得するためのユーティリティメソッドです。
	 * ManagerLocator.getInstance().getManager(type)を呼び出します。
	 * 
	 * @param type
	 * @return
	 */
	public static <M extends Manager> M manager(Class<M> type) {
		return getInstance().getManager(type);
	}

	/**
	 * Managerのインスタンスを取得します。
	 * 
	 * @param type 取得するManagerのタイプ
	 * @return Managerのインスタンス
	 */
	public abstract <M extends Manager> M getManager(Class<M> type);

}
