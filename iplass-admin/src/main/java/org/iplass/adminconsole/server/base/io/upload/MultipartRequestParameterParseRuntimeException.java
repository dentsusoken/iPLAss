/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.server.base.io.upload;

/**
 * マルチパートリクエスト パラメータ解析例外
 *
 * <p>
 * {@link org.iplass.adminconsole.server.base.io.upload.MultipartRequestParameterParser} でパラメータ解析に失敗した場合にスローされる例外。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class MultipartRequestParameterParseRuntimeException extends RuntimeException {
	/** serialVersionUID */
	private static final long serialVersionUID = -3231520568194840293L;

	/**
	 * コンストラクタ
	 * @param cause 原因例外
	 */
	public MultipartRequestParameterParseRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * コンストラクタ
	 * @param message メッセージ
	 */
	public MultipartRequestParameterParseRuntimeException(String message) {
		super(message);
	}

	/**
	 * コンストラクタ
	 * @param message メッセージ
	 * @param cause 原因例外
	 */
	public MultipartRequestParameterParseRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
