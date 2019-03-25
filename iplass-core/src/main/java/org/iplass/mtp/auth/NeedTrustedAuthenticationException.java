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

package org.iplass.mtp.auth;

import org.iplass.mtp.ApplicationException;

/**
 * 信頼された認証処理を経て認証されていない場合にスローする例外。
 * この例外がCommand（Actionで利用する）のexecute()からスローされた場合は、
 * 基盤は、再認証画面（未ログインの場合は、認証画面）を表示するように動作する。
 * 
 * 
 * @author K.Higuchi
 *
 */
public class NeedTrustedAuthenticationException extends ApplicationException {
	private static final long serialVersionUID = 3178320542103087325L;

	public NeedTrustedAuthenticationException() {
		super();
	}

	public NeedTrustedAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public NeedTrustedAuthenticationException(String message) {
		super(message);
	}

	public NeedTrustedAuthenticationException(Throwable cause) {
		super(cause);
	}

}
