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
package org.iplass.mtp.impl.webapi.openapi.entity;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.webapi.openapi.EntityWebApiType;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * EntityWebAPI の操作の OpenAPI 変換インターフェース
 * @author SEKIGUCHI Naoya
 */
public interface EntityWebApiOpenApiConverter {
	/**
	 * EntityWebAPIタイプを取得します。
	 * @return EntityWebAPIタイプ
	 */
	EntityWebApiType getEntityWebApiType();

	/**
	 * OpenAPIに変換します。
	 * <p>
	 * 実装クラスでは {@link #getEntityWebApiType()} で取得したEntityWebAPIタイプに応じて、OpenAPI を構成します。
	 * </p>
	 * @param openApi OpenAPIオブジェクト
	 * @param entityDefinition EntityDefinition
	 */
	void convert(OpenAPI openApi, EntityDefinition entityDefinition);
}
