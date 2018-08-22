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

package org.iplass.mtp.impl.entity.csv;

import org.iplass.mtp.ApplicationException;

/**
 * CsvUploadに関する例外クラス。
 */
public class EntityCsvException extends ApplicationException {

	private static final long serialVersionUID = -4885795448741566808L;

	/** メッセージコード */
	private String code;

	public EntityCsvException() {
		super();
	}

	public EntityCsvException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityCsvException(String message) {
		super(message);
	}

	public EntityCsvException(Throwable cause) {
		super(cause);
	}

	public EntityCsvException(String code, String message) {
		super(message);
		setCode(code);
	}

	public EntityCsvException(String code, String message, Throwable cause) {
		super(message, cause);
		setCode(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
