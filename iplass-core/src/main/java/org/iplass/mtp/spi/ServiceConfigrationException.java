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

import org.iplass.mtp.SystemException;

/**
 * Serviceの初期化時に、設定が不正の場合にスローする例外です。
 * 
 * @author K.Higuchi
 *
 */
public class ServiceConfigrationException extends SystemException {
	private static final long serialVersionUID = 2023377217775447954L;

	public ServiceConfigrationException() {
		super();
	}

	public ServiceConfigrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceConfigrationException(String message) {
		super(message);
	}

	public ServiceConfigrationException(Throwable cause) {
		super(cause);
	}

}
