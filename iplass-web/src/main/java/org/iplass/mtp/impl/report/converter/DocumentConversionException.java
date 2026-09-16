/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.iplass.mtp.impl.report.converter;

import org.iplass.mtp.SystemException;

/**
 * ドキュメント変換失敗を表す例外。
 */
public class DocumentConversionException extends SystemException {

	private static final long serialVersionUID = -6029021818526956842L;
	
	/** HTTP ステータスコード。ネットワークエラー等の未知は -1 */
	private final int httpStatus;

	public DocumentConversionException(String message, int httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public DocumentConversionException(String message, int httpStatus, Throwable cause) {
		super(message, cause);
		this.httpStatus = httpStatus;
	}

	public int getHttpStatus() {
		return httpStatus;
	}
}
