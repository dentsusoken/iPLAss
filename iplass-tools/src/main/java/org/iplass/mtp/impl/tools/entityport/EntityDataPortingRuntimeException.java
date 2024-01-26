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

package org.iplass.mtp.impl.tools.entityport;

import org.iplass.mtp.ApplicationException;

/**
 * EntityのExport/Import時に発生するException
 */
public class EntityDataPortingRuntimeException extends ApplicationException {

	private static final long serialVersionUID = 10225253702917668L;

	public EntityDataPortingRuntimeException() {
		super();
	}

	public EntityDataPortingRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityDataPortingRuntimeException(String message) {
		super(message);
	}

	public EntityDataPortingRuntimeException(Throwable cause) {
		super(cause);
	}

}
