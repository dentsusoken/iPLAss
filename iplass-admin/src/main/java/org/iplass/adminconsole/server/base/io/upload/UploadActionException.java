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
 * アップロード操作例外
 *
 * <p>
 * {@link org.iplass.adminconsole.server.base.io.upload.AdminUploadAction} のサブクラスの処理で
 * 例外が派生した場合にスローされることを想定した例外。。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class UploadActionException extends Exception {
	/** serialVersionUID */
	private static final long serialVersionUID = -7823376367867638450L;

	/**
	 * コンストラクタ
	 * @param cause 原因例外
	 */
	public UploadActionException(Throwable cause) {
		super(cause);
	}

	/**
	 * コンストラクタ
	 * @param message メッセージ
	 */
	public UploadActionException(String message) {
		super(message);
	}

	/**
	 * コンストラクタ
	 * @param message メッセージ
	 * @param cause 原因例外
	 */
	public UploadActionException(String message, Throwable cause) {
		super(message, cause);
	}

}
