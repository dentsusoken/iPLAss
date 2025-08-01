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

import java.util.Map;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.openapi.OpenApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 * OpenAPI の components/schemas に再利用可能なクラススキーマを追加するファクトリクラス
 * @author SEKIGUCHI Naoya
 */
public class OpenApiComponentClassSchemaFactory implements OpenApiComponentSchemaFactory<Class<?>>, OpenApiComponentTarget {
	/** ログ */
	private Logger logger = LoggerFactory.getLogger(OpenApiComponentClassSchemaFactory.class);
	/** JSON Schema ジェネレータ */
	private ClassSchemaGenerator schemaGenerator;

	@Override
	public boolean isTarget(Object object) {
		return object instanceof Class;
	}

	/**
	 * OpenAPI の components/schemas に再利用可能な ObjectSchema を追加します。
	 * <p>
	 * ObjectSchema はパラメータのクラスから生成します。
	 * <p>
	 * @param clazz スキーマクラス
	 * @param openApi OpenAPI オブジェクト
	 * @return ObjectSchema の参照文字列
	 */
	@Override
	public String addReusableSchema(Class<?> clazz, OpenAPI openApi, OpenApiJsonSchemaType schemaType) {
		var name = schemaType.name() + "_class_" + clazz.getName();
		var ref = REUSABLE_SCHEMA_PREFIX + name;

		var components = getOpenApiComponents(openApi);

		if (null != components.getSchemas() && components.getSchemas().containsKey(name)) {
			// すでに定義済みの場合は再利用
			return ref;
		}

		// createObjectSchema から本メソッドをコールされた際に、自クラス定義が存在しない場合無限ループになってしまうため、null を設定しておく。
		components.addSchemas(name, null);

		var schema = createObjectSchemaFromClass(clazz, openApi, schemaType);

		// 生成したスキーマを設定する
		components.addSchemas(name, schema);

		return ref;
	}

	/**
	 * クラススキーマ生成機能を設定します。
	 * @param schemaGenerator クラススキーマ生成機能
	 */
	public void setClassSchemaGenerator(ClassSchemaGenerator schemaGenerator) {
		this.schemaGenerator = schemaGenerator;
	}

	/**
	 * クラスから ObjectSchema を生成します。
	 * @param clazz スキーマに変換するクラス
	 * @param openApi OpenAPI オブジェクト
	 * @return 生成された ObjectSchema
	 */
	@SuppressWarnings("unchecked")
	private Schema<?> createObjectSchemaFromClass(Class<?> clazz, OpenAPI openApi, OpenApiJsonSchemaType schemaType) {
		try {
			// Class to JsonSchema
			String jsonSchemaString = schemaGenerator.generate(clazz);
			logger.debug("Class to JsonSchema. class = {}, jsonSchema = {}", clazz.getName(), jsonSchemaString);
			// JsonSchema to OpenAPI Schema
			var resolver = ServiceRegistry.getRegistry().getService(OpenApiService.class).getOpenApiResolver();
			var openApiVersion = openApi.getOpenapi().split("\\.");
			var seriesVersion = openApiVersion[0] + "." + openApiVersion[1];
			var mapper = resolver.getObjectMapper(OpenApiFileType.JSON, OpenApiVersion.fromSeriesVersion(seriesVersion));
			var jsonSchema = mapper.readValue(jsonSchemaString, Schema.class);
			var openApiSchema = new ObjectSchema();
			openApiSchema.setProperties(jsonSchema.getProperties());
			openApiSchema.setTitle(schemaType.name() + " Class Schema " + clazz.getSimpleName());

			@SuppressWarnings("rawtypes")
			Map<String, Schema> props = openApiSchema.getProperties();
			var parser = new PropertyDescriptorParser(clazz);
			props.forEach((k, v) -> {
				if (StringUtil.isNotEmpty(v.get$ref())) {
					// $ref が設定されている場合は、オブジェクトと判断。
					var propType = parser.getPropertyDesctiptor(k).getPropertyType();
					if (propType == PropertyDefinitionType.DATETIME.getJavaType()) {
						// DATETIME は Unix タイムスタンプに変換
						props.put(k, new IntegerSchema().description("Unix timestamp in milliseconds"));

					} else if (propType == PropertyDefinitionType.TIME.getJavaType()) {
						// TIME は HH:mm:ss フォーマットの文字列に変換
						props.put(k, new StringSchema().description("format is HH:mm:ss"));

					} else {
						var refName = addReusableSchema(propType, openApi, schemaType);
						v.set$ref(refName);
					}
				}
			});

			return openApiSchema;

		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to create ObjectSchema for class: " + clazz.getName(), e);
		}

	}

	/**
	 * OpenAPI の Components を取得します。
	 * <p>
	 * インスタンスが存在しない場合は新規に作成します。
	 * </p>
	 * @param openApi OpenAPI オブジェクト
	 * @return OpenAPI の Components
	 */
	private Components getOpenApiComponents(OpenAPI openApi) {
		var components = openApi.getComponents();
		if (null == components) {
			components = new Components();
			openApi.setComponents(components);
		}

		return components;
	}
}
