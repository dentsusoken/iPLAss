/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.definition;

import java.io.Serializable;

public class DefinitionModifyResult implements Serializable {

	private static final long serialVersionUID = 5017185294034459446L;

	/** 結果 */
	protected boolean isSuccess;

	/** メッセージ */
	protected String message;

	/**
	 * コンストラクタ
	 */
	public DefinitionModifyResult() {
		this(false, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param isSuccess 結果
	 */
	public DefinitionModifyResult(boolean isSuccess) {
		this(isSuccess, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param isSuccess 結果
	 * @param message メッセージ
	 */
	public DefinitionModifyResult(boolean isSuccess, String message) {
		this.isSuccess = isSuccess;
		this.message = message;
	}

	/**
	 * 結果を取得します。
	 *
	 * @return 結果
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * メッセージを取得します。
	 *
	 * @return メッセージ
	 */
	public String getMessage() {
		return message;
	}
}
