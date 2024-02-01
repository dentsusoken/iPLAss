/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.auth.login.rememberme;

import org.iplass.mtp.auth.login.LoginException;

/**
 * RememberMeトークンでの認証の際、トークンが他者に盗まれた可能性がある場合にスローされる例外。
 * ただし、実際には盗まれていないが発生する可能性もある
 * （ほとんど同時に同一ブラウザ内からログイン処理を複数実行した場合、
 * サーバ処理は完結したが、クライアントのクッキーへの書き込みに失敗した場合など）。
 * 
 * @author K.Higuchi
 *
 */
public class RememberMeTokenStolenException extends LoginException {
	private static final long serialVersionUID = 3697510537428690627L;

	public RememberMeTokenStolenException() {
		super();
	}

	public RememberMeTokenStolenException(String message, Throwable cause) {
		super(message, cause);
	}

	public RememberMeTokenStolenException(String message) {
		super(message);
	}

	public RememberMeTokenStolenException(Throwable cause) {
		super(cause);
	}

}
