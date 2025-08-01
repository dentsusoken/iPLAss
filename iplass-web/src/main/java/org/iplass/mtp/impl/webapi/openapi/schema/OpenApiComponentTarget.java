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
package org.iplass.mtp.impl.webapi.openapi.schema;

/**
 * OpenAPIのコンポーネントスキーマを生成する際に、対象となるオブジェクトを判定するインターフェース。
 * @author SEKIGUCHI Naoya
 */
interface OpenApiComponentTarget {
	/**
	 * 指定されたオブジェクトが、OpenAPIのコンポーネントスキーマを生成する対象かどうかを判定します。
	 * @param object 判定対象のオブジェクト
	 * @return 対象であればtrue、そうでなければfalse
	 */
	boolean isTarget(Object object);
}
