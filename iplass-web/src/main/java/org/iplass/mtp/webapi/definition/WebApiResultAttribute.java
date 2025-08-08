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
package org.iplass.mtp.webapi.definition;

import java.io.Serializable;

/**
 * WebAPI 結果属性
 * <p>
 * WebAPI レスポンスに関する属性を設定します。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class WebApiResultAttribute implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = -243504146183422654L;

	/** レスポンス属性名 */
	private String name;
	/** レスポンスデータ型 */
	private String dataType;

	/**
	 * レスポンス属性名を取得します。
	 * @return レスポンス属性名
	 */
	public String getName() {
		return name;
	}

	/**
	 * レスポンス属性名を設定します。
	 * <p>
	 * 必須の設定項目です。<br>
	 * {@link org.iplass.mtp.command.RequestContext#setAttribute(String, Object)} で設定したキーに一致する値を、レスポンスの属性として設定します。
	 * </p>
	 * @param name レスポンス属性名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * レスポンスデータ型を取得します。
	 * @return レスポンスデータ型
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * レスポンスデータ型を設定します。
	 * <p>
	 * 任意の設定項目です。設定値は WebAPI の動作に影響はありません。<br>
	 * 設定することで、OpenAPI 出力時にデータ型を指定することが可能となります。
	 * </p>
	 * @param dataType レスポンスデータ型
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
