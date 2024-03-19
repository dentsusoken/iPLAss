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
package org.iplass.adminconsole.shared.base.io;

/**
 * AdminSingleUpload 機能で共有する値
 *
 * @author SEKIGUCHI Naoya
 */
public class AdminUploadConstant {
	/**
	 * AdminUploadAction のレスポンスキー定義。
	 *
	 * <p>
	 * レスポンスは JSON となる。
	 * 本クラスでは、JSON のキー文字列を定義する。
	 * </p>
	 */
	public static class ResponseKey {
		/** 正常終了の判断キー */
		public static final String IS_SUCCESS = "isSuccess";
		/** データキー */
		public static final String DATA = "data";
		/** エラーメッセージキー */
		public static final String ERROR_MESSAGE = "errorMessage";

		/**
		 * プライベートコンストラクタ
		 */
		private ResponseKey() {
		}
	}

	/**
	 * プライベートコンストラクタ
	 */
	private AdminUploadConstant() {
	}
}
