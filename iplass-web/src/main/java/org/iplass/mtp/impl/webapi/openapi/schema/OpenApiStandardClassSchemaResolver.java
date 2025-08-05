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

import io.swagger.v3.oas.models.media.Schema;

/**
 * OpenAPI用 標準クラスのスキーマを解決するインターフェース
 * <p>
 * エンティティのプロパティとして設定されうるクラスの解決を目的とします。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public interface OpenApiStandardClassSchemaResolver {
	/**
	 * 指定されたクラスが解決可能かどうかを確認します。
	 * @param clazz クラス
	 * @return 解決可能な場合はtrue、そうでない場合はfalse
	 */
	boolean canResolve(Class<?> clazz);

	/**
	 * 指定されたクラスのスキーマを解決します。
	 * @param clazz クラス
	 * @param schemaType スキーマのタイプ（JSON, XML, FORMなど）
	 * @return スキーマ
	 */
	Schema<?> resolve(Class<?> clazz, OpenApiJsonSchemaType schemaType);
}
