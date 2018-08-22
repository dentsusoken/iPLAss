/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.shared.base.dto.auth;

import org.iplass.mtp.ApplicationException;

/**
 * AdminConsole セキュリティエラー
 */
public class AdminAuthException extends ApplicationException {

	private static final long serialVersionUID = 6811493673705364397L;

	public AdminAuthException() {
	}

	public AdminAuthException(String message) {
		super(message);
	}

	public AdminAuthException(Throwable cause) {
		super(cause);
	}

	public AdminAuthException(String message, Throwable cause) {
		super(message, cause);
	}
}
