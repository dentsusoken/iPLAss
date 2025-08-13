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

import org.iplass.mtp.impl.util.ClassUtil;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.definition.openapi.OpenApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;

/**
 * OpenAPI の components/schemas に再利用可能なクラススキーマを追加するファクトリクラス
 * @author SEKIGUCHI Naoya
 */
public class OpenApiComponentClassReusableSchemaFactory implements OpenApiComponentReusableSchemaFactory<Class<?>>, OpenApiComponentTarget {
	/** ログ */
	private Logger logger = LoggerFactory.getLogger(OpenApiComponentClassReusableSchemaFactory.class);
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
		var name = getReusableSchemaName(clazz, schemaType);
		var ref = REUSABLE_SCHEMA_PREFIX + name;

		var components = getOpenApiComponents(openApi);

		if (null != components.getSchemas() && components.getSchemas().containsKey(name)) {
			// すでに定義済みの場合は再利用
			return ref;
		}

		// createObjectSchema から本メソッドをコールされた際に、自クラス定義が存在しない場合無限ループになってしまうため、空スキーマを設定しておく。
		components.addSchemas(name, new Schema<>());

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
			logger.trace("Class to JsonSchema. class={}, jsonSchema={}", clazz, jsonSchemaString);
			// JsonSchema to OpenAPI Schema
			var resolver = ServiceRegistry.getRegistry().getService(OpenApiService.class).getOpenApiResolver();
			var openApiVersion = openApi.getOpenapi().split("\\.");
			var seriesVersion = openApiVersion[0] + "." + openApiVersion[1];
			var mapper = resolver.getObjectMapper(OpenApiFileType.JSON, OpenApiVersion.fromSeriesVersion(seriesVersion));
			var jsonSchema = mapper.readValue(jsonSchemaString, Schema.class);

			if (clazz.isEnum()) {
				// クラスが列挙型の場合は、生成したスキーマをそのまま返却する
				return jsonSchema;
			}

			if (StringUtil.isNotEmpty(jsonSchema.get$ref())) {
				// 解析した結果が $ref になっている場合は、$defs に指定されているスキーマを取得してそれを対象スキーマとして設定する。
				var jsonSchemaMap = mapper.readValue(jsonSchemaString, Map.class);
				var defsMap = (Map<String, Object>) jsonSchemaMap.get("$defs");

				jsonSchema = getDefsSchema(defsMap, jsonSchema, mapper);
			}

			//
			if (null == jsonSchema.getProperties()) {
				// プロパティが null の場合はオブジェクトをそのまま返却する
				return jsonSchema;
			}

			var openApiSchema = new ObjectSchema();
			openApiSchema.setProperties(jsonSchema.getProperties());
			openApiSchema.setTitle(schemaType.name() + " Class Schema " + clazz.getSimpleName());

			var standardClassSchemaResolver = ServiceRegistry.getRegistry().getService(OpenApiService.class).getStandardClassSchemaResolver();
			@SuppressWarnings("rawtypes")
			Map<String, Schema> props = openApiSchema.getProperties();
			var parser = new PropertyDescriptorParser(clazz);
			props.forEach((key, value) -> {
				var isContainsArray = null != value.getTypes() && value.getTypes().contains("array");
				var isTypeArray = "array".equals(value.getType());
				var isNotEmptyArrayRef = StringUtil.isNotEmpty(value.getItems() != null ? value.getItems().get$ref() : null);
				var isArray = isContainsArray || isTypeArray || isNotEmptyArrayRef;

				var isContainsObject = null != value.getTypes() && value.getTypes().contains("object");
				var isTypeObject = "object".equals(value.getType());
				var isNotEmptyObjectRef = StringUtil.isNotEmpty(value.get$ref());
				var isObject = isContainsObject || isTypeObject || isNotEmptyObjectRef;

				if (logger.isTraceEnabled()) {
					logger.trace("class={}, property={}, isArray={}, isObject={}, schema={}", clazz, key, isArray, isObject, value.toString());
				}

				if (isArray) {
					// type が array の場合は、標準データ型を判定
					var descriptor = parser.getPropertyDesctiptor(key);
					var propType = descriptor.getPropertyType();

					logger.trace("array type. class={}, property={}, propertyType={}", clazz, key, propType);

					if (standardClassSchemaResolver.canResolve(propType)) {
						// 解決可能な場合は、スキーマを解決して設定
						props.put(key, value.items(standardClassSchemaResolver.resolve(propType, schemaType)));

					} else if (propType.isAssignableFrom(java.util.List.class) || propType.isAssignableFrom(java.util.Set.class)) {
						// List, Set 派生の場合は、 generics を取得する
						var genericReturnType = descriptor.getReadMethod().getGenericReturnType();
						if (genericReturnType instanceof java.lang.reflect.ParameterizedType pt) {
							// List, Set なので必ず Genericsタイプは１つ
							var genericType = pt.getActualTypeArguments()[0];
							var genericClass = ClassUtil.forName(genericType.getTypeName());

							logger.trace("array generic type. class={}, property={}, genericType={}", clazz, key, genericClass);

							if (standardClassSchemaResolver.canResolve(genericClass)) {
								// 解決可能な場合は、スキーマを解決して設定
								props.put(key, value.items(standardClassSchemaResolver.resolve(genericClass, schemaType)));

							} else {

								// 解決できない場合は、再利用可能なスキーマとして追加
								var refName = addReusableSchema(genericClass, openApi, schemaType);
								value.getItems().set$ref(refName);
								value.getItems().properties(null);
							}
						}

					} else {
						// 解決できない場合は、再利用可能なスキーマとして追加
						var refName = addReusableSchema(propType, openApi, schemaType);
						value.getItems().set$ref(refName);
						value.getItems().properties(null);
					}

				} else if (isObject) {
					// type が object の場合は、標準データ型を判定
					var propType = parser.getPropertyDesctiptor(key).getPropertyType();
					logger.trace("object type. class={}, property={}, propertyType={}", clazz, key, propType);
					if (standardClassSchemaResolver.canResolve(propType)) {
						// 解決可能な場合は、スキーマを解決して設定
						props.put(key, standardClassSchemaResolver.resolve(propType, schemaType));

					} else {
						// 解決できない場合は、再利用可能なスキーマとして追加
						var refName = addReusableSchema(propType, openApi, schemaType);
						value.set$ref(refName);
						// 配下のプロパティは null を設定
						value.setProperties(null);
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

	/**
	 * 再利用可能なスキーマ名を取得する
	 * @param clazz スキーマに変換するクラス
	 * @param schemaType スキーマタイプ
	 * @return 再利用可能なスキーマ名
	 */
	private String getReusableSchemaName(Class<?> clazz, OpenApiJsonSchemaType schemaType) {
		return schemaType.name() + "_class_" + clazz.getName();
	}

	/**
	 * JsonSchema の $defs からスキーマを取得します
	 * @param defsMap $defs のマップ
	 * @param schema $refs が設定されているスキーマ
	 * @param mapper オブジェクトマッパー
	 * @return $refs が設定されているスキーマの定義
	 * @throws JsonProcessingException
	 */
	private Schema<?> getDefsSchema(Map<String, Object> defsMap, Schema<?> schema, ObjectMapper mapper) throws JsonProcessingException {
		var defsKey = schema.get$ref().substring(schema.get$ref().lastIndexOf('/') + 1);
		var defSchemaObject = defsMap.get(defsKey);
		var defSchemaString = mapper.writeValueAsString(defSchemaObject);
		var defSchema = mapper.readValue(defSchemaString, Schema.class);

		if (StringUtil.isNotEmpty(defSchema.get$ref())) {
			// $ref が設定されている場合は、再帰的に取得する
			return getDefsSchema(defsMap, defSchema, mapper);
		} else {
			return defSchema;
		}
	}
}
