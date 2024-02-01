/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.pack;

import org.iplass.mtp.ApplicationException;

/**
 * Package処理時に発生するException
 */
public class PackageRuntimeException extends ApplicationException {

	private static final long serialVersionUID = -1607888920666486066L;

	public PackageRuntimeException() {
		super();
	}

	public PackageRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PackageRuntimeException(String message) {
		super(message);
	}

	public PackageRuntimeException(Throwable cause) {
		super(cause);
	}

}
