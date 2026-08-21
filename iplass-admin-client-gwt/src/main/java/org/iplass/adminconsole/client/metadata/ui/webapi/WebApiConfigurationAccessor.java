/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.metadata.ui.webapi;

import java.util.function.Supplier;

/**
 * WebAPI設定アクセサ
 * <p>
 * WebAPI編集画面で編集中の現在値を取得するための機能です。
 * クラス間でリアルタイムな設定情報が必要な場合に利用します。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class WebApiConfigurationAccessor {
	/** ResponseType 取得操作 */
	private Supplier<String> responseTypeSupplier;

	/**
	 * ResponseTypeを取得します。
	 * @return ResponseType
	 */
	public String getResponseType() {
		if (responseTypeSupplier != null) {
			return responseTypeSupplier.get();
		}
		return null;
	}

	/**
	 * ResponseTypeを取得する機能を設定します。
	 * @param responseTypeSupplier ResponseTypeを取得操作
	 */
	public void setResponseTypeSupplier(Supplier<String> responseTypeSupplier) {
		this.responseTypeSupplier = responseTypeSupplier;
	}
}
