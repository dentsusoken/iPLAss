/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.webapi;

import org.iplass.mtp.SystemException;

/**
 * WebApi呼び出し時、サーバ側でシステム例外が発生した際、
 * クライアント側へ実際に提示される例外のクラス。
 * 
 * @author K.Higuchi
 *
 */
public class WebApiRuntimeException extends SystemException {
	private static final long serialVersionUID = -9112564002179662504L;

	public WebApiRuntimeException() {
		super();
	}

	public WebApiRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebApiRuntimeException(String message) {
		super(message);
	}

	public WebApiRuntimeException(Throwable cause) {
		super(cause);
	}

}
