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
package org.iplass.mtp.command.annotation.webapi;

/**
 * 結果属性アノテーション
 * <p>
 * {@link org.iplass.mtp.webapi.definition.WebApiResultAttribute} のアノテーション指定です。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public @interface WebApiResultAttribute {
	/**
	 * レスポンス属性名
	 * <p>
	 * 必須の設定項目です。<br>
	 * {@link org.iplass.mtp.command.RequestContext#setAttribute(String, Object)} で設定したキーに一致する値を、レスポンスの属性として設定します。
	 * </p>
	 * @return レスポンス属性名
	 */
	String name();

	/**
	 * レスポンスデータ型
	 * <p>
	 * 任意の設定項目です。設定値は WebAPI の動作に影響はありません。<br>
	 * 設定することで、OpenAPI 出力時にデータ型を指定することが可能となります。
	 * </p>
	 * @return レスポンスデータ型
	 */
	String dataType() default "";

}
