/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

/**
 *
 */
package org.iplass.mtp.auth.login;

import org.iplass.mtp.ApplicationException;

/**
 * 認証情報更新時の、更新ポリシー違反の場合の例外
 * 
 * @author K.Higuchi
 *
 */
public class CredentialUpdateException extends ApplicationException {
	private static final long serialVersionUID = 1157452737837303955L;

	/**
	 *
	 */
	public CredentialUpdateException() {
	}

	/**
	 * @param message
	 */
	public CredentialUpdateException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CredentialUpdateException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CredentialUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

}
