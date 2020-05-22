/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.command.entity;

import org.iplass.mtp.ApplicationException;

/**
 * XmlのStreaming処理に関する例外クラス。
 */
public class EntityXmlException extends ApplicationException {

	private static final long serialVersionUID = 4115450618438921326L;

	/** メッセージコード */
	private String code;

	public EntityXmlException() {
		super();
	}

	public EntityXmlException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityXmlException(String message) {
		super(message);
	}

	public EntityXmlException(Throwable cause) {
		super(cause);
	}

	public EntityXmlException(String code, String message) {
		super(message);
		setCode(code);
	}

	public EntityXmlException(String code, String message, Throwable cause) {
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
