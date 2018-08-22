/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.spi;

/**
 * Service配下にて管理されるBeanが
 * Serviceのライフサイクルの通知を受け取る場合に実装するインタフェースです。
 * 
 * @author K.Higuchi
 *
 * @param <T>
 */
public interface ServiceInitListener<T extends Service> {
	
	/**
	 * Service初期化時に呼び出されます。
	 * 
	 * @param service
	 * @param config
	 */
	public void inited(T service, Config config);
	
	/**
	 * Service破棄時に呼び出されます。
	 * 
	 */
	public void destroyed();

}
