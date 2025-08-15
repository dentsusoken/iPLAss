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
package org.iplass.mtp.impl.webapi.openapi.webapi.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iplass.mtp.impl.util.ClassUtil;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiJsonSchemaType;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConvertContext.OperationContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.ArrayUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiResultAttribute;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(Results)
 * @author SEKIGUCHI Naoya
 */
public class WebApiResponseResultsOpenApiOperationConverter extends AbstractWebApiOpenApiOperationConverter {
	/** components/schemas に設定するデフォルトスキーマ名 */
	private static final String DEFAULT_SCHEMA_NAME = "webApiDefaultResults";
	/** object schema の $ref 設定値 */
	private static final String DEFAULT_SCHEMA_REF = "#/components/schemas/" + DEFAULT_SCHEMA_NAME;

	@Override
	public int getOrder() {
		// 最後に実行する
		return Order.LAST;
	}

	@Override
	protected boolean isMapOpenApiOperationValue(WebApiOpenApiConvertContext context) {
		return true;
	}

	@Override
	protected void convertOpenApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		// 全 Operation の response に対して results を追加する。

		var content = initResponseContent(operation.getOperation());

		var openApiService = ServiceRegistry.getRegistry().getService(OpenApiService.class);
		// メタデータ設定値ベースで設定する
		var definition = context.getWebApiDefinition();
		var responseResults = mergeResponseResults(definition);
		var isNotEmptyResponseResults = !responseResults.isEmpty();
		var isNotContensStatusKey = responseResults.stream()
				.filter(r -> r.getName().equals("status")).findFirst().isEmpty();

		for (var key : content.keySet()) {
			var schema = new ObjectSchema();

			var jsonSchemaType = OpenApiJsonSchemaType.fromContentType(key, OpenApiJsonSchemaType.JSON);

			if (isNotEmptyResponseResults) {
				// responseResults に設定されている場合
				for (var result : responseResults) {
					if (null != result.getDataType()) {
						// dataType の設定あり
						Class<?> dataTypeClass = ClassUtil.forName(result.getDataType());
						if (openApiService.getStandardClassSchemaResolver().canResolve(dataTypeClass)) {
							var resultSchema = openApiService.getStandardClassSchemaResolver().resolve(dataTypeClass, jsonSchemaType);
							schema.addProperty(result.getName(), resultSchema);
						} else {
							var ref = openApiService.getReusableSchemaFactory().addReusableSchema(dataTypeClass, context.getOpenApi(),
									jsonSchemaType);
							schema.addProperty(result.getName(), new ObjectSchema().$ref(ref));
						}
					} else {
						// dataType の設定なし。スキーマの定義はわからないため、StringSchema とする。
						schema.addProperty(result.getName(), new StringSchema());
					}
				}

				if (isNotContensStatusKey) {
					// status が定義されていない場合は追加する
					schema.addProperty("status", new StringSchema());
				}

			} else {
				// runtime 無し、もしくは results が設定されていない場合はデフォルトのプロパティを設定する
				var components = getComponents(context.getOpenApi());
				var schemas = components.getSchemas();
				if (null == schemas || !schemas.containsKey(DEFAULT_SCHEMA_NAME)) {
					// WebApiResults が定義されていない場合は、ObjectSchema を定義する
					var webApiDefaultResults = new ObjectSchema();
					webApiDefaultResults.addProperty("status", new StringSchema());
					components.addSchemas(DEFAULT_SCHEMA_NAME, webApiDefaultResults);
				}

				// デフォルトのプロパティを参照する
				schema.$ref(DEFAULT_SCHEMA_REF);
			}

			var mediaType = content.get(key);
			mediaType.schema(schema);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setWebApiDefaultValue(WebApiOpenApiConvertContext context) {
		// results, responseResults を初期化する
		context.getWebApiDefinition().setResults(new String[0]);
		context.getWebApiDefinition().setResponseResults(new WebApiResultAttribute[0]);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected CheckNext convertWebApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		var content = getResponseContent(operation.getOperation());
		if (null == content || null == content.values()) {
			return CheckNext.CONTINUE;
		}

		var nameResultMap = new HashMap<String, WebApiResultAttribute>();
		for (var result : context.getWebApiDefinition().getResponseResults()) {
			nameResultMap.put(result.getName(), result);
		}

		for (var mediaType : content.values()) {
			// メディアタイプに設定されている最上位スキーマのプロパティのキーをWebApiに反映する
			if (null != mediaType.getSchema() && null != mediaType.getSchema().getProperties() && !mediaType.getSchema().getProperties().isEmpty()) {
				for (var prop : mediaType.getSchema().getProperties().keySet()) {
					var name = (String) prop;
					if (nameResultMap.containsKey(name)) {
						// 既存のプロパティはスキップする
						continue;
					}
					var attr = new WebApiResultAttribute();
					// キー名を反映する
					attr.setName(name);
					nameResultMap.put(name, attr);
				}
			}
		}

		// 新しい値を設定
		context.getWebApiDefinition().setResults(nameResultMap.keySet().stream().toArray(String[]::new));
		context.getWebApiDefinition().setResponseResults(nameResultMap.values().stream().toArray(WebApiResultAttribute[]::new));

		return CheckNext.CONTINUE;
	}

	/**
	 * OpenAPIのComponentsを取得します。
	 * @param openApi OpenAPIインスタンス
	 * @return Componentsインスタンス
	 */
	private Components getComponents(OpenAPI openApi) {
		var components = openApi.getComponents();
		if (null == components) {
			components = new Components();
			openApi.setComponents(components);
		}

		return components;
	}

	private List<WebApiResultAttribute> mergeResponseResults(WebApiDefinition def) {
		List<WebApiResultAttribute> responseResults = new ArrayList<>();
		if (ArrayUtil.isNotEmpty(def.getResponseResults())) {
			for (var result : def.getResponseResults()) {
				responseResults.add(result);
			}
		}

		if (ArrayUtil.isNotEmpty(def.getResults())) {
			// results が設定されている場合は、WebApiResultAttribute に変換して追加する
			for (var resultName : def.getResults()) {
				if (responseResults.stream().filter(r -> r.getName().equals(resultName)).findFirst().isEmpty()) {
					// 同一キーの値が存在しない場合は、responseResults に追加する
					var result = new WebApiResultAttribute();
					result.setName(resultName);
					responseResults.add(result);
				}
			}
		}

		return responseResults;
	}
}
