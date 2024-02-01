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

package org.iplass.mtp.impl.metadata;

/**
 * MetaDataがコンパイルエラー、設定不備などで実行可能な状態でない場合、スローされる例外。
 * 
 * @author K.Higuchi
 *
 */
public class MetaDataIllegalStateException extends MetaDataRuntimeException {
	private static final long serialVersionUID = 9086691551531855088L;

	public MetaDataIllegalStateException() {
		super();
	}

	public MetaDataIllegalStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public MetaDataIllegalStateException(String message) {
		super(message);
	}

	public MetaDataIllegalStateException(Throwable cause) {
		super(cause);
	}

}
