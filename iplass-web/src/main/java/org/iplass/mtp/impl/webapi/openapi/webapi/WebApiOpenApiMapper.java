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
package org.iplass.mtp.impl.webapi.openapi.webapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiAccessPolicyCheckXRequestedWithOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiCacheControlOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiCorsAccessControlAllowCredentialsOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiCorsAccessControlAllowOriginOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiMethodOpenApiConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOAuthSupportBearerTokenOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConvertContext;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiParameterMappingsOpenApiConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiResponseTypeOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiRestTypeRestJsonOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiResultsOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiTokenCheckOpenApiOperationConverter;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * OpenAPI仕様とWebAPI定義の変換機能
 * @author SEKIGUCHI Naoya
 */
public class WebApiOpenApiMapper {
	/** WebAPI OpenAPI 変換機能リスト */
	private List<WebApiOpenApiConverter> converterList = createConverterList();
	/** WebAPIをOpenAPIにマッピングする際の処理単位を決定する機能 */
	private WebApiPathParameterDecomposer webApiDecomposer = createPathParameterResolver();
	/** OpenAPIをWebAPIにマッピングする際の処理単位を決定する機能 */
	private OpenApiPathMethodDecomposer openApiDecomposer = createOpenApiPathMethodResolver();

	/**
	 * WebAPI定義をOpenAPI仕様にマッピングします。
	 * @param webApiList WebAPI定義のリスト
	 * @param openApi マッピング先のOpenAPI
	 * @return マッピングされたOpenAPI
	 */
	public OpenAPI mapOpenApi(List<WebApiDefinition> webApiList, OpenAPI openApi) {
		for (WebApiDefinition webApi : webApiList) {
			// パラメーターによってパスが分離されることを考慮。OpenAPI に設定される単位にパスを分解する。
			var pathUnitList = webApiDecomposer.decompose(webApi, openApi);
			for (String pathUnit : pathUnitList) {
				// OpenAPIに設定するパス単位で WebAPIDefinition ⇒ OpenAPI PathItem のマッピングを実行する
				var pathItem = openApi.getPaths().get(pathUnit);
				var context = new WebApiOpenApiConvertContext(webApi, webApi.getName(), openApi, pathUnit, pathItem);
				converterList.forEach(m -> m.convertOpenApi(context));
			}
		}
		return openApi;
	}

	/**
	 * OpenAPI仕様をWebAPI定義にマッピングします。
	 * @param openApi OpenAPI仕様
	 * @return OpenAPI仕様をWebAPI定義へマッピングした結果
	 */
	public List<WebApiMapInfo> mapWebApi(OpenAPI openApi) {
		if (null == openApi.getPaths()) {
			// OpenAPIのパスが存在しない場合は、何もマッピングされない。
			return Collections.emptyList();
		}

		List<WebApiMapInfo> mapResultList = new ArrayList<>();
		for (var entry : openApi.getPaths().entrySet()) {
			var path = entry.getKey();
			if (path.startsWith("/mtp/entity")) {
				// EntityWebAPI は WebAPI 定義ではないのでマッピングしない
				mapResultList.add(new WebApiMapInfo(null, path, WebApiUpdateType.NONE));
				continue;
			}

			var pathItem = entry.getValue();
			// OpenAPI のパス・メソッド単位で WebAPI 定義を作成する。
			// すでに WebAPI が存在し、１つの WebAPI で複数メソッドに対応している場合はまとめて設定する。
			var processUnitList = openApiDecomposer.decompose(openApi, path, pathItem);

			if (null != processUnitList && !processUnitList.isEmpty()) {
				for (var processUnit : processUnitList) {
					var context = new WebApiOpenApiConvertContext(processUnit.getWebApi(), processUnit.getWebApi().getName(), openApi, path, pathItem);
					context.setTargetMethod(processUnit.getMethodType());
					converterList.forEach(m -> m.convertWebApi(context));

					mapResultList.add(new WebApiMapInfo(context.getWebApiDefinition(), path, processUnit.getUpdateType()));
				}

			} else {
				// 指定されたパスに、WebAPIで定義可能なメソッド以外しか存在しない場合は、何もマッピングされない。
				// その場合もログとして出力する
				mapResultList.add(new WebApiMapInfo(null, path, WebApiUpdateType.NONE));
			}
		}
		return mapResultList;
	}

	/**
	 * OpenAPI仕様の変換機能リストを作成します。
	 * <p>
	 * 本機能を利用して OpenAPI <-> WebAPI 定義の変換を行います。
	 * </p>
	 * @return 変換機能リスト
	 */
	protected List<WebApiOpenApiConverter> createConverterList() {
		var list = new ArrayList<>(List.of(
				//
				new WebApiMethodOpenApiConverter(),
				//
				new WebApiAccessPolicyCheckXRequestedWithOpenApiOperationConverter(),
				//
				new WebApiTokenCheckOpenApiOperationConverter(),
				//
				new WebApiCacheControlOpenApiOperationConverter(),
				//
				new WebApiCorsAccessControlAllowOriginOpenApiOperationConverter(),
				//
				new WebApiCorsAccessControlAllowCredentialsOpenApiOperationConverter(),
				//
				new WebApiOAuthSupportBearerTokenOpenApiOperationConverter(),
				//
				new WebApiRestTypeRestJsonOpenApiOperationConverter(),
				//
				new WebApiResponseTypeOpenApiOperationConverter(),
				//
				new WebApiResultsOpenApiOperationConverter(),
				//
				new WebApiParameterMappingsOpenApiConverter()));
		list.sort((m1, m2) -> m1.getOrder() - m2.getOrder());
		return list;
	}

	protected WebApiPathParameterDecomposer createPathParameterResolver() {
		return new WebApiPathParameterDecomposer();
	}

	protected OpenApiPathMethodDecomposer createOpenApiPathMethodResolver() {
		return new OpenApiPathMethodDecomposer();
	}

	/**
	 * OpenAPIからWebAPIへのマッピング情報
	 */
	public static class WebApiMapInfo {
		/** WebAPI定義 */
		private WebApiDefinition webApiDefinition;
		/** マッピング元のOpenAPIパス */
		private String openApiPath;
		/** WebAPI更新タイプ */
		private WebApiUpdateType updateType;

		/**
		 * コンストラクタ
		 * @param webApiDefinition WebAPI定義
		 * @param openApiPath マッピング元のOpenAPIパス
		 * @param updateType WebAPI更新タイプ
		 */
		public WebApiMapInfo(WebApiDefinition webApiDefinition, String openApiPath, WebApiUpdateType updateType) {
			this.webApiDefinition = webApiDefinition;
			this.openApiPath = openApiPath;
			this.updateType = updateType;
		}

		/**
		 * WebAPI定義を取得します。
		 * @return WebAPI定義
		 */
		public WebApiDefinition getWebApiDefinition() {
			return webApiDefinition;
		}

		/**
		 * OpenAPIパスを取得します。
		 * @return OpenAPIパス
		 */
		public String getOpenApiPath() {
			return openApiPath;
		}

		/**
		 * WebAPI更新タイプを取得します
		 * @return WebAPI更新タイプ
		 */
		public WebApiUpdateType getUpdateType() {
			return updateType;
		}

	}
}
