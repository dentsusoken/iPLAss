/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata;

/**
 * メタデータがパス(コンテキストパス＋名前)の一意制約により登録できなかった場合スローされる例外。
 *
 */
public class MetaDataDuplicatePathException extends MetaDataRuntimeException {

	private static final long serialVersionUID = -8635403535315575973L;

	public MetaDataDuplicatePathException() {
		super();
	}

	public MetaDataDuplicatePathException(String message, Throwable cause) {
		super(message, cause);
	}

	public MetaDataDuplicatePathException(String message) {
		super(message);
	}

	public MetaDataDuplicatePathException(Throwable cause) {
		super(cause);
	}
}
