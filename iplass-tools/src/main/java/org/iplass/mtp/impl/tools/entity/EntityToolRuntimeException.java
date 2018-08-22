/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.entity;

import org.iplass.mtp.ApplicationException;

public class EntityToolRuntimeException extends ApplicationException {

	private static final long serialVersionUID = -4922016740302458021L;

	public EntityToolRuntimeException() {
		super();
	}

	public EntityToolRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityToolRuntimeException(String message) {
		super(message);
	}

	public EntityToolRuntimeException(Throwable cause) {
		super(cause);
	}

}
