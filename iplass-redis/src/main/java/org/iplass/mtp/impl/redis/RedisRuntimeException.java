/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.redis;

import org.iplass.mtp.SystemException;

public class RedisRuntimeException extends SystemException {

	private static final long serialVersionUID = 8271412128977083405L;

	public RedisRuntimeException() {
		super();
	}

	public RedisRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisRuntimeException(String message) {
		super(message);
	}

	public RedisRuntimeException(Throwable cause) {
		super(cause);
	}
}
