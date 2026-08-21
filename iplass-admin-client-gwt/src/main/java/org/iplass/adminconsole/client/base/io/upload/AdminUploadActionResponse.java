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
package org.iplass.adminconsole.client.base.io.upload;

import com.google.gwt.json.client.JSONValue;

/**
 * AdminUploadAction レスポンス情報。
 *
 * <p>
 * AdminUploadAction の実行結果にアクセスするためのクラス
 * <p>
 *
 * @see org.iplass.adminconsole.shared.base.io.AdminUploadConstant
 * @see org.iplass.adminconsole.server.base.io.upload.AdminUploadAction
 * @see org.iplass.adminconsole.client.base.io.upload.AdminUploadActionResponseParser
 * @author SEKIGUCHI Naoya
 */
public class AdminUploadActionResponse {
	/** リクエスト成功判定 */
	private boolean isSuccess = false;
	/** アプリデータ */
	private JSONValue data = null;
	/** エラーメッセージ */
	private String errorMessage = null;

	/**
	 * リクエスト成功判定を取得する
	 * @return リクエスト成功判定（true: 成功）
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * アプリデータを取得する
	 * @return アプリデータ
	 */
	public JSONValue getData() {
		return data;
	}

	/**
	 * エラーメッセージを取得する
	 * @return エラーメッセージ
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * リクエスト成功判定を設定する
	 * @param isSuccess リクエスト成功判定（true: 成功）
	 */
	void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * アプリデータを設定する
	 * @param data アプリデータ
	 */
	void setData(JSONValue data) {
		this.data = data;
	}

	/**
	 * エラーメッセージを設定する
	 * @param errorMessage エラーメッセージ
	 */
	void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
