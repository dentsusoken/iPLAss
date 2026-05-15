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

import org.iplass.mtp.SystemException;
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
	/** 再利用可能なスキーマの参照プレフィックス */
	private static final String DEFS = "$defs";

	/** ログ */
	private Logger logger = LoggerFactory.getLogger(OpenApiComponentClassReusableSchemaFactory.class);
	/** JSON Schema ジェネレータ */
	private ClassSchemaGenerator schemaGenerator;

	/**
	 * スキーマタイプ検出器
	 * <p>
	 * Schema オブジェクトから配列型やオブジェクト型を判定します。
	 * </p>
	 */
	private static class SchemaTypeDetector {
		/** スキーマタイプ: object */
		private static final String OBJECT = "object";
		/** スキーマタイプ: array */
		private static final String ARRAY = "array";

		/** 検出対象のスキーマ */
		private final Schema<?> schema;

		/**
		 * コンストラクタ
		 * @param schema 検出対象のスキーマ
		 */
		SchemaTypeDetector(Schema<?> schema) {
			this.schema = schema;
		}

		/**
		 * 配列型かどうかを判定します
		 * @return 配列型の場合 true
		 */
		boolean isArray() {
			var isContainsArray = schema.getTypes() != null && schema.getTypes()
					.contains(ARRAY);
			var isTypeArray = ARRAY.equals(schema.getType());
			var isNotEmptyArrayRef = StringUtil.isNotEmpty(schema.getItems() != null ? schema.getItems()
					.get$ref() : null);
			return isContainsArray || isTypeArray || isNotEmptyArrayRef;
		}

		/**
		 * オブジェクト型かどうかを判定します
		 * @return オブジェクト型の場合 true
		 */
		boolean isObject() {
			var isContainsObject = schema.getTypes() != null && schema.getTypes()
					.contains(OBJECT);
			var isTypeObject = OBJECT.equals(schema.getType());
			var isNotEmptyObjectRef = StringUtil.isNotEmpty(schema.get$ref());
			return isContainsObject || isTypeObject || isNotEmptyObjectRef;
		}
	}

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

		if (components.getSchemas() != null && components.getSchemas()
				.containsKey(name)) {
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
	 * @param schemaType JSONスキーマタイプ
	 * @return 生成された ObjectSchema
	 */
	@SuppressWarnings("unchecked")
	private Schema<?> createObjectSchemaFromClass(Class<?> clazz, OpenAPI openApi, OpenApiJsonSchemaType schemaType) {
		var openApiService = ServiceRegistry.getRegistry()
				.getService(OpenApiService.class);
		var standardClassSchemaResolver = openApiService.getStandardClassSchemaResolver();

		try {
			// Class to JsonSchema
			String jsonSchemaString = schemaGenerator.generate(clazz);
			logger.trace("Class to JsonSchema. class={}, jsonSchema={}", clazz, jsonSchemaString);
			// JsonSchema to OpenAPI Schema
			var openApiVersion = openApi.getOpenapi()
					.split("\\.");
			var seriesVersion = openApiVersion[0] + "." + openApiVersion[1];
			var mapper = openApiService.getOpenApiResolver()
					.getObjectMapper(OpenApiFileType.JSON, OpenApiVersion.fromSeriesVersion(seriesVersion));
			var jsonSchema = mapper.readValue(jsonSchemaString, Schema.class);

			if (clazz.isEnum()) {
				// クラスが列挙型の場合は、生成したスキーマをそのまま返却する
				return jsonSchema;
			}

			if (StringUtil.isNotEmpty(jsonSchema.get$ref())) {
				// 解析した結果が $ref になっている場合は、$defs に指定されているスキーマを取得してそれを対象スキーマとして設定する。
				var jsonSchemaMap = mapper.readValue(jsonSchemaString, Map.class);
				var defsMap = (Map<String, Object>) jsonSchemaMap.get(DEFS);

				jsonSchema = getDefsSchema(defsMap, jsonSchema, mapper);
			}

			if (jsonSchema.getProperties() == null) {
				// プロパティが null の場合はオブジェクトをそのまま返却する
				return jsonSchema;
			}

			var openApiSchema = new ObjectSchema();
			openApiSchema.setProperties(jsonSchema.getProperties());
			openApiSchema.setTitle(schemaType.name() + " Class Schema " + clazz.getSimpleName());

			@SuppressWarnings("rawtypes")
			Map<String, Schema> props = openApiSchema.getProperties();
			var parser = new PropertyDescriptorParser(clazz);
			props.forEach((key, schema) -> {
				var detector = new SchemaTypeDetector(schema);

				if (logger.isTraceEnabled()) {
					// 引数を toString しているため、isTraceEnable でガードする
					logger.trace("class={}, property={}, isArray={}, isObject={}, schema={}",
							clazz, key, detector.isArray(), detector.isObject(), schema.toString());
				}

				if (detector.isArray()) {
					// type が array の場合は、配列クラスもしくは、コレクション派生クラスとなる。標準データ型を判定
					var descriptor = parser.getPropertyDescriptor(key);
					var propType = descriptor.getPropertyType();

					if (propType.isArray()) {
						// クラス配列の場合
						var extractClass = propType.getComponentType();
						logger.trace("array type. class={}, property={}, propertyType={}, extractClass={}", clazz, key, propType, extractClass);

						updateArrayPropertySchema(props, key, schema, extractClass, standardClassSchemaResolver, openApi, schemaType);

					} else {
						// List, Set 派生の場合は、generics を取得する
						var genericReturnType = descriptor.getReadMethod()
								.getGenericReturnType();
						if (genericReturnType instanceof java.lang.reflect.ParameterizedType pt) {
							// List, Set なので必ず Genericsタイプは１つ
							var genericType = pt.getActualTypeArguments()[0];
							logger.trace("collection type. class={}, property={}, propertyType={}, genericType={}", clazz, key, propType, genericType);

							var genericClass = ClassUtil.forName(genericType.getTypeName());

							updateArrayPropertySchema(props, key, schema, genericClass, standardClassSchemaResolver, openApi, schemaType);
						}
					}

				} else if (detector.isObject()) {
					// type が object の場合は、標準データ型を判定
					var propType = parser.getPropertyDescriptor(key)
							.getPropertyType();
					logger.trace("object type. class={}, property={}, propertyType={}", clazz, key, propType);
					if (standardClassSchemaResolver.canResolve(propType)) {
						// 解決可能な場合は、スキーマを解決して設定
						props.put(key, standardClassSchemaResolver.resolve(propType, schemaType));

					} else {
						// カスタムクラスの場合は、components/schemas に再利用可能なスキーマとして追加
						var refName = addReusableSchema(propType, openApi, schemaType);
						// schema に参照（$ref）を設定
						schema.set$ref(refName);
						// $ref を使用する場合、OpenAPI仕様上 properties は不要なため null を設定
						schema.setProperties(null);
					}
				}
			});

			return openApiSchema;

		} catch (JsonProcessingException e) {
			throw new SystemException("Failed to create ObjectSchema for class: " + clazz.getName(), e);
		}
	}

	/**
	 * 配列型プロパティのスキーマを更新します。
	 * <p>
	 * 配列の要素クラス（コンポーネント型またはジェネリクス型）に応じて、適切なスキーマを設定します。
	 * </p>
	 *
	 * @param props プロパティマップ（更新対象）
	 * @param propKey 更新対象のプロパティ名
	 * @param propSchema 更新対象のプロパティスキーマ
	 * @param extractClassFromArray 配列から抽出された要素クラス（配列の場合はコンポーネント型、コレクションの場合はジェネリクス型）
	 * @param standardClassSchemaResolver 標準クラススキーマリゾルバ
	 * @param openApi OpenAPI オブジェクト
	 * @param schemaType スキーマタイプ（REQUEST/RESPONSE）
	 */
	@SuppressWarnings("rawtypes")
	private void updateArrayPropertySchema(Map<String, Schema> props, String propKey, Schema<?> propSchema, Class<?> extractClassFromArray,
			OpenApiStandardClassSchemaResolver standardClassSchemaResolver, OpenAPI openApi, OpenApiJsonSchemaType schemaType) {

		if (standardClassSchemaResolver.canResolve(extractClassFromArray)) {
			// 標準クラス（String, Integer, Long 等）の場合は、対応する型スキーマを items に設定
			props.put(propKey, propSchema.items(standardClassSchemaResolver.resolve(extractClassFromArray, schemaType)));

		} else {
			// カスタムクラスの場合は、components/schemas に再利用可能なスキーマとして追加
			var refName = addReusableSchema(extractClassFromArray, openApi, schemaType);
			var items = propSchema.getItems();
			// items に参照（$ref）を設定
			items.set$ref(refName);
			// $ref を使用する場合、OpenAPI仕様上 properties は不要なため null を設定
			items.properties(null);
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
		if (components == null) {
			components = new Components();
			openApi.setComponents(components);
		}

		return components;
	}

	/**
	 * 再利用可能なスキーマ名を取得します
	 * @param clazz スキーマに変換するクラス
	 * @param schemaType スキーマタイプ
	 * @return 再利用可能なスキーマ名
	 */
	private String getReusableSchemaName(Class<?> clazz, OpenApiJsonSchemaType schemaType) {
		return schemaType.name() + "_class_" + clazz.getName();
	}

	/**
	 * JsonSchema の $defs からスキーマを取得します
	 * <h3>注意点</h3>
	 * <ul>
	 * <li>本メソッドは、{@link io.swagger.v3.oas.models.media.Schema#get$ref()} が非nullであることを確認後に実行してください。</li>
	 * </ul>
	 *
	 * @param defsMap $defs のマップ
	 * @param schema $refs が設定されているスキーマ
	 * @param mapper オブジェクトマッパー
	 * @return $refs が設定されているスキーマの定義
	 * @throws JsonProcessingException $defs からスキーマの取得に失敗した場合
	 */
	private Schema<?> getDefsSchema(Map<String, Object> defsMap, Schema<?> schema, ObjectMapper mapper) throws JsonProcessingException {
		var ref = schema.get$ref();
		if (StringUtil.isEmpty(ref)) {
			throw new SystemException("Schema $ref is empty");
		}

		var lastSeparatorIndex = ref.lastIndexOf('/');
		if (lastSeparatorIndex < 0) {
			throw new SystemException("Invalid schema $ref format: " + ref);
		}

		var defsKey = ref.substring(lastSeparatorIndex + 1);
		if (!defsMap.containsKey(defsKey)) {
			throw new SystemException("Schema definition not found for key: " + defsKey + " in " + DEFS);
		}

		var defSchemaObject = defsMap.get(defsKey);
		var defSchemaString = mapper.writeValueAsString(defSchemaObject);
		var defSchema = mapper.readValue(defSchemaString, Schema.class);

		if (StringUtil.isNotEmpty(defSchema.get$ref())) {
			// $ref が設定されている場合は、再帰的に取得する
			return getDefsSchema(defsMap, defSchema, mapper);
		}

		return defSchema;
	}
}
