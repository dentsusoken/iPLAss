/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.runtime;

import org.iplass.mtp.SystemException;


/**
 * <% if (doclang == "ja") {%>
 * EntryPointがすでに初期化済みの際にスローされる例外です。
 * <%} else {%>
 * Throws when an EntryPoint has already been initialized.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public class AlreadyInitializedException extends SystemException {
	private static final long serialVersionUID = 2147335645626979676L;

	public AlreadyInitializedException() {
		super();
	}

	public AlreadyInitializedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyInitializedException(String message) {
		super(message);
	}

	public AlreadyInitializedException(Throwable cause) {
		super(cause);
	}

}
