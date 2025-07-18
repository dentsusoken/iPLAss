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
import org.iplass.mtp.entity.definition.properties.SelectProperty;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 * エンティティプロパティ定義のJSONスキーマ解決クラス
 * @author SEKIGUCHI Naoya
 */
public class EntityPropertyDefinitionJsonSchemaResolver {
	/** エンティティ定義からスキーマを生成する機能 */
	private OpenApiComponentSchemaFactory<EntityDefinition> factory;

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
		case AUTONUMBER -> new StringSchema();
		case BINARY -> new IntegerSchema();
		case BOOLEAN -> new BooleanSchema();
		case DATE -> {
			// DATE: Content-Type によって異なる。
			yield switch (schemaType) {
			// JSON = yyyy-MM-dd
			case JSON -> new DateSchema();
			// XML = yyyy-MM-dd+09:00
			case XML -> new StringSchema().description("Date and time in the format yyyy-MM-dd+09:00.");
			// FORM = yyyyMMdd
			case FORM -> new StringSchema().description("Date and time in the format yyyyMMdd.");
			};
		}
		case DATETIME -> {
			// DATETIME: Content-Type によって異なる。
			yield switch (schemaType) {
			// JSON = Integer(unix timestamp)
			case JSON ->  new IntegerSchema().description("Unix timestamp in milliseconds.");
			// XML = yyyy-MM-dd'T'HH:mm:ss.000000000+09:00
			case XML -> new StringSchema().description("ISO 8601 date-time format with timezone offset.");
			// FORM = yyyyMMddHHmmss
			case FORM -> new StringSchema().description("Date and time in the format yyyyMMddHHmmss.");
			};
		}
		case DECIMAL -> new IntegerSchema();
		case EXPRESSION -> convertSchema(((ExpressionProperty) propDef).getResultType(), propDef, openApi, schemaType);
		case FLOAT -> new IntegerSchema();
		case INTEGER -> new IntegerSchema();
		case LONGTEXT -> new StringSchema();
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
			var selectPropDef = (SelectProperty) propDef;
			var schema = new StringSchema();
			var enumValueList = selectPropDef.getSelectValueList().stream().map(s -> s.getValue()).toList();
			schema.setEnum(enumValueList);
			yield schema;
		}
		case STRING -> new StringSchema();
		// TIME: Content-Type によって異なる。
		case TIME -> {
			yield switch (schemaType) {
			// JSON = HH:mm:ss
			case JSON -> new StringSchema().format("time");
			// XML = HH:mm:ss+09:00
			case XML -> new StringSchema().description("Time in the format HH:mm:ss+09:00.");
			// FORM = HH:mm:ss
			case FORM -> new StringSchema().description("Time in the format HH:mm:ss.");
			};
		}
		};
	}

	/**
	 * エンティティ定義からスキーマを生成する機能を設定する
	 * @param factory エンティティ定義からスキーマを生成する機能
	 */
	public void setOpenApiSchemaFactory(OpenApiComponentSchemaFactory<EntityDefinition> factory) {
		this.factory = factory;
	}
}
