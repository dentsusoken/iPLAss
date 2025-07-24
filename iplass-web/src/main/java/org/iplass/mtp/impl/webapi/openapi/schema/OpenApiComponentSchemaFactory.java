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

import io.swagger.v3.oas.models.OpenAPI;

/**
 * OpenAPI の components/schemas に再利用可能なスキーマを追加するファクトリインターフェース。
 * @param <T> スキーマを生成するオブジェクトの型
 * @author SEKIGUCHI Naoya
 */
public interface OpenApiComponentSchemaFactory<T> {
	/** 再利用可能なスキーマの接頭辞 */
	String REUSABLE_SCHEMA_PREFIX = "#/components/schemas/";

	/**
	 * OpenAPI の components/schemas に再利用可能なスキーマを追加します。
	 * <p>
	 * スキーマは、指定されたオブジェクトから生成されます。
	 * </p>
	 * @param object スキーマを生成するオブジェクト
	 * @param openApi OpenAPI オブジェクト
	 * @param schemaType スキーマのタイプ（JSON, XML, FORMなど）
	 * @return スキーマの参照文字列
	 */
	String addReusableSchema(T object, OpenAPI openApi, OpenApiJsonSchemaType schemaType);
}
