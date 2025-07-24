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
import java.util.Arrays;

import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConvertContext.OperationContext;
import org.iplass.mtp.util.ArrayUtil;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(Results)
 * @author SEKIGUCHI Naoya
 */
public class WebApiResultsOpenApiOperationConverter extends AbstractWebApiOpenApiOperationConverter {
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
		for (var key : content.keySet()) {
			var schema = new ObjectSchema();

			if (ArrayUtil.isNotEmpty(context.getWebApiDefinition().getResults())) {
				// results に設定されている場合
				for (var result : context.getWebApiDefinition().getResults()) {
					// result が定義されている場合はプロパティを追加する。スキーマの定義はわからないため、StringSchema とする。
					schema.addProperty(result, new StringSchema());
				}

				if (!ArrayUtil.contains(context.getWebApiDefinition().getResults(), "status")) {
					// status が定義されていない場合は追加する
					schema.addProperty("status", new StringSchema());
				}

			} else {
				// results が設定されていない場合はデフォルトのプロパティを設定する
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

	@Override
	protected void setWebApiDefaultValue(WebApiOpenApiConvertContext context) {
		// results を初期化する
		context.getWebApiDefinition().setResults(new String[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected CheckNext convertWebApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		var content = getResponseContent(operation.getOperation());
		if (null == content) {
			return CheckNext.CONTINUE;
		}

		var propList = new ArrayList<String>();
		for (var mediaType : content.values()) {
			if (null != mediaType.getSchema() && null != mediaType.getSchema().getProperties() && !mediaType.getSchema().getProperties().isEmpty()) {
				propList.addAll(mediaType.getSchema().getProperties().keySet());
			}
		}

		// ※注意：$ref は考慮しない
		// 既存プロパティとジョイン
		if (null != context.getWebApiDefinition().getResults()) {
			propList.addAll(Arrays.asList(context.getWebApiDefinition().getResults()));
		}
		// distinct
		var newResults = propList.stream().distinct().toArray(String[]::new);
		// 新しい値を設定
		context.getWebApiDefinition().setResults(newResults);

		return CheckNext.CONTINUE;
	}

	private Components getComponents(OpenAPI openApi) {
		var components = openApi.getComponents();
		if (null == components) {
			components = new Components();
			openApi.setComponents(components);
		}

		return components;
	}
}
