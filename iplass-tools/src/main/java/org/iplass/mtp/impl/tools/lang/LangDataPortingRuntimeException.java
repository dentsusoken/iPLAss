/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.lang;

import org.iplass.mtp.ApplicationException;

/**
 * 多言語情報のExport/Import時に発生するException
 */
public class LangDataPortingRuntimeException extends ApplicationException {

	private static final long serialVersionUID = -8640506195002489691L;

	public LangDataPortingRuntimeException() {
		super();
	}

	public LangDataPortingRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public LangDataPortingRuntimeException(String message) {
		super(message);
	}

	public LangDataPortingRuntimeException(Throwable cause) {
		super(cause);
	}

}
