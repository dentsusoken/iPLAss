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

import java.util.function.Consumer;

import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiComponentSchemaFactory;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス（OpenAPI, OpenAPI FileType, OpenAPI Version）
 * <p>
 * インポート対象のOpenAPI仕様をWebAPI定義に設定します。
 * OpenAPI の対象パス・メソッドと、設定されている再利用可能なスキーマ設定を取り込みます。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class WebApiOpenApiOpenApiConverter implements WebApiOpenApiConverter {
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(WebApiOpenApiOpenApiConverter.class);

	@Override
	public void convertOpenApi(WebApiOpenApiConvertContext context) {
		// OpenAPI から変換処理は無し
		// もしも WebAPI に OpenAPI が設定されている場合は、WebApiOpenApiMapper で解決され、 WebApiOpenApiConverter は実行されません。
	}

	@Override
	public void convertWebApi(WebApiOpenApiConvertContext context) {
		var org = context.getOpenApi();
		var openapi = new OpenAPI(org.getSpecVersion()).openapi(org.getOpenapi());
		// WebAPI への変換の際は、OpenAPI に設定する。
		if (null == context.getTargetMethod()) {
			// 全メソッドを対象
			openapi.path(context.getOpenApiPath(), context.getPathItem());

		} else {
			var orgPathItem = context.getPathItem();
			var pathItem = new PathItem();
			// メソッドに一致するオペレーションをコピーする
			switch (context.getTargetMethod()) {
			case GET:
				pathItem.get(context.getPathItem().getGet());
				break;
			case POST:
				pathItem.post(context.getPathItem().getPost());
				break;
			case PUT:
				pathItem.put(context.getPathItem().getPut());
				break;
			case DELETE:
				pathItem.delete(context.getPathItem().getDelete());
				break;
			case PATCH:
				pathItem.patch(context.getPathItem().getPatch());
				break;
			default:
				throw new IllegalArgumentException("Unsupported method type: " + context.getTargetMethod());
			}

			pathItem.summary(orgPathItem.getSummary()).parameters(orgPathItem.getParameters());
			openapi.path(context.getOpenApiPath(), pathItem);
		}

		// components/schemas の解決
		for (var pathItem : openapi.getPaths().values()) {
			// pathItem のパラメータ解決
			nonNull(pathItem.getParameters(), parameters -> parameters.stream().forEach(p -> copyRefSchema(org, openapi, p.getSchema(), context)));

			for (var operation : pathItem.readOperations()) {
				// リクエストボディのスキーマを解決
				nonNull(operation.getRequestBody(), requestBody -> {
					nonNull(requestBody.getContent(), content -> content.values().forEach(m -> copyRefSchema(org, openapi, m.getSchema(), context)));
				});

				// パラメータのスキーマを解決
				nonNull(operation.getParameters(), parameters -> parameters.stream().forEach(p -> copyRefSchema(org, openapi, p.getSchema(), context)));

				// レスポンスのスキーマを解決
				nonNull(operation.getResponses(), responses -> responses.values().forEach(response -> {
					nonNull(response.getContent(), content -> content.values().forEach(m -> copyRefSchema(org, openapi, m.getSchema(), context)));
				}));
			}
		}

		var resolver = ServiceRegistry.getRegistry().getService(OpenApiService.class).getOpenApiResolver();
		var writer = resolver.getObjectWriter(context.getFileType(), context.getVersion());
		try {
			context.getWebApiDefinition().setOpenApi(writer.writeValueAsString(openapi));
			context.getWebApiDefinition().setOpenApiFileType(context.getFileType().name());
			context.getWebApiDefinition().setOpenApiVersion(context.getVersion().getSeriesVersion());

		} catch (JsonProcessingException e) {
			// OpenAPIの文字列化に失敗
			throw new RuntimeException("Failed to serialize OpenAPI.", e);
		}
	}

	/**
	 * components.schemas に設定されている再利用可能なスキーマをコピーします。
	 * @param from コピー元
	 * @param to コピー先
	 * @param schema 調査スキーマ
	 * @param context WebApiOpenApiConvertContext
	 */
	@SuppressWarnings("rawtypes")
	private void copyRefSchema(OpenAPI from, OpenAPI to, Schema schema, WebApiOpenApiConvertContext context) {
		if (null == schema) {
			// スキーマがnullの場合は何もしない
			return;
		}

		if (StringUtil.isNotEmpty(schema.get$ref())) {
			// スキーマに参照が設定されている場合

			var schemaKey = schema.get$ref().substring(OpenApiComponentSchemaFactory.REUSABLE_SCHEMA_PREFIX.length());

			// コピー先の componentns にインスタンス設定
			if (null == to.getComponents()) {
				to.setComponents(new Components());
			}

			if (null == to.getComponents().getSchemas() || !to.getComponents().getSchemas().containsKey(schemaKey)) {
				// コピー先の components.schemas にスキーマが存在しない場合は、コピーする

				var fromSchema = from.getComponents().getSchemas().get(schemaKey);
				if (null != fromSchema) {
					to.getComponents().addSchemas(schemaKey, fromSchema);

					// コピースキーマ定義も調査する
					copyRefSchema(from, to, fromSchema, context);

				} else {
					// スキーマが存在しない場合は警告ログ出力
					logger.warn("WebAPI: {}, OpenAPI path: {} - OpenAPI schema reference not found: {}", context.getWebApiPath(),
							context.getOpenApiPath(), schema.get$ref());
				}
			}

		} else if (null != schema.getItems()) {
			// 配列スキーマの場合、配列のスキーマを再帰調査
			copyRefSchema(from, to, schema.getItems(), context);

		} else if (null != schema.getProperties() && !schema.getProperties().isEmpty()) {
			// プロパティスキーマの場合、プロパティのスキーマを再帰調査
			for (var propertySchema : schema.getProperties().values()) {
				copyRefSchema(from, to, (Schema) propertySchema, context);

			}
		}
	}

	/**
	 * 値がnullでない場合に、指定された関数を実行します。
	 * @param <T> 調査する値の型
	 * @param value 調査値
	 * @param nonNullFn nullでない場合に実行する関数
	 */
	private <T> void nonNull(T value, Consumer<T> nonNullFn) {
		if (value != null) {
			nonNullFn.accept(value);
		}
	}
}
