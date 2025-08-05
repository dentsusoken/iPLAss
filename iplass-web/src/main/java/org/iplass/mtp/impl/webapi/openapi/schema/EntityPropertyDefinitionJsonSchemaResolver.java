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

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.spi.ServiceRegistry;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;

/**
 * エンティティプロパティ定義のJSONスキーマ解決クラス
 * @author SEKIGUCHI Naoya
 */
public class EntityPropertyDefinitionJsonSchemaResolver {
	/** エンティティ定義からスキーマを生成する機能 */
	private OpenApiComponentReusableSchemaFactory<EntityDefinition> factory;

	/**
	 * プロパティ定義からスキーマ定義に変換する
	 * @param type プロパティ定義の型
	 * @param propDef プロパティ定義
	 * @param openApi OpenAPI オブジェクト
	 * @param schemaType スキーマのタイプ（JSON, XML, FORMなど）
	 * @return 変換後のスキーマ
	 */
	public Schema<?> convertSchema(PropertyDefinitionType type, PropertyDefinition propDef, OpenAPI openApi, OpenApiJsonSchemaType schemaType) {
		return switch (type) {
		case EXPRESSION -> convertSchema(((ExpressionProperty) propDef).getResultType(), propDef, openApi, schemaType);
		case REFERENCE -> {
			ReferenceProperty refPropDef = (ReferenceProperty) propDef;
			var referenceEntityDefinitionName = refPropDef.getObjectDefinitionName();
			var edm = ManagerLocator.manager(EntityDefinitionManager.class);
			var referenceEntityDefinition = edm.get(referenceEntityDefinitionName);
			if (null == referenceEntityDefinition) {
				throw new RuntimeException("Reference entity definition not found: " + referenceEntityDefinitionName);
			}
			var ref = factory.addReusableSchema(referenceEntityDefinition, openApi, schemaType);
			yield new ObjectSchema().$ref(ref);
		}
		case SELECT -> {
			var schemaResolver = ServiceRegistry.getRegistry().getService(OpenApiService.class).getStandardClassSchemaResolver();
			yield schemaResolver.resolve(type.getJavaType(), schemaType);
		}
		case BINARY -> new IntegerSchema();
		case
		// string
		AUTONUMBER, LONGTEXT, STRING,
		// boolean
		BOOLEAN,
		// 数値,
		DECIMAL, FLOAT, INTEGER,
		// 日付
		DATE, DATETIME, TIME -> {
			var schemaResolver = ServiceRegistry.getRegistry().getService(OpenApiService.class).getStandardClassSchemaResolver();
			yield schemaResolver.resolve(type.getJavaType(), schemaType);
		}
		};
	}

	/**
	 * エンティティ定義からスキーマを生成する機能を設定する
	 * @param factory エンティティ定義からスキーマを生成する機能
	 */
	public void setOpenApiSchemaFactory(OpenApiComponentReusableSchemaFactory<EntityDefinition> factory) {
		this.factory = factory;
	}
}
