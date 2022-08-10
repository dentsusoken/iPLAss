/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.view.filter.expression;

import org.iplass.mtp.entity.EntityApplicationException;

/**
 * サポートしていない操作を行った際にスローされる例外。
 */
public class UnsupportedFilterOperationException extends EntityApplicationException {

	private static final long serialVersionUID = -5035468351961204854L;

	public UnsupportedFilterOperationException() {
		super();
	}

	public UnsupportedFilterOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedFilterOperationException(String message) {
		super(message);
	}

	public UnsupportedFilterOperationException(Throwable cause) {
		super(cause);
	}

}
