/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.auth;

import org.iplass.mtp.ApplicationException;

/**
 * セキュリティ権限上、許可されていない操作を行おうとした場合、スローされる例外。
 * 
 * 
 * @author K.Higuchi
 * 
 */
public class NoPermissionException extends ApplicationException {
	private static final long serialVersionUID = 5902999193661172625L;

	/**
	 *
	 */
	public NoPermissionException() {
	}

	/**
	 * @param message
	 */
	public NoPermissionException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NoPermissionException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoPermissionException(String message, Throwable cause) {
		super(message, cause);
	}

}
