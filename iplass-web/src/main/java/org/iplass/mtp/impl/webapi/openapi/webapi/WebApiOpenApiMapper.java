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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiAccessPolicyCheckXRequestedWithOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiCacheControlOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiCorsAccessControlAllowCredentialsOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiCorsAccessControlAllowOriginOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiMethodOpenApiConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOAuthSupportBearerTokenOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConvertContext;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiOpenApiConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiParameterMappingsOpenApiConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiResponseTypeOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiRestTypeRestJsonOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiResultsOpenApiOperationConverter;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiTokenCheckOpenApiOperationConverter;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.definition.openapi.OpenApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.SpecVersion;

/**
 * OpenAPI仕様とWebAPI定義の変換機能
 * @author SEKIGUCHI Naoya
 */
public class WebApiOpenApiMapper {
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(WebApiOpenApiMapper.class);

	/** WebAPI OpenAPI 変換機能リスト */
	private List<WebApiOpenApiConverter> converterList = createConverterList();
	/** WebAPIをOpenAPIにマッピングする際の処理単位を決定する機能 */
	private WebApiPathParameterDecomposer webApiDecomposer = createWebApiPathParameterDecomposer();
	/** OpenAPIをWebAPIにマッピングする際の処理単位を決定する機能 */
	private OpenApiPathMethodDecomposer openApiDecomposer = createOpenApiPathMethodDecomposer();

	/**
	 * WebAPI定義をOpenAPI仕様にマッピングします。
	 * @param webApiList WebAPI定義のリスト
	 * @param openApi マッピング先のOpenAPI
	 * @param fileType OpenAPIファイルのタイプ（JSONまたはYAML）
	 * @param version OpenAPIのバージョン（V30またはV31）
	 * @return マッピングされたOpenAPI
	 */
	public OpenAPI mapOpenApi(List<WebApiDefinition> webApiList, OpenAPI openApi, OpenApiFileType fileType, OpenApiVersion version) {
		for (WebApiDefinition webApi : webApiList) {
			if (StringUtil.isNotEmpty(webApi.getOpenApi())) {
				// OpenAPIが定義されている場合は、OpenAPIをマージする。
				mergeOpenApi(openApi, webApi);
				continue;
			}

			// パラメーターによってパスが分離されることを考慮。OpenAPI に設定される単位にパスを分解する。
			var pathUnitList = webApiDecomposer.decompose(webApi, openApi);
			for (String pathUnit : pathUnitList) {
				// OpenAPIに設定するパス単位で WebAPIDefinition ⇒ OpenAPI PathItem のマッピングを実行する
				var pathItem = openApi.getPaths().get(pathUnit);
				var context = new WebApiOpenApiConvertContext(webApi, webApi.getName(), openApi, pathUnit, pathItem, fileType, version);
				converterList.forEach(m -> m.convertOpenApi(context));
			}
		}
		return openApi;
	}

	/**
	 * OpenAPI仕様をWebAPI定義にマッピングします。
	 * @param openApi OpenAPI仕様
	 * @param fileType OpenAPIファイルのタイプ（JSONまたはYAML）
	 * @param version OpenAPIのバージョン（V30またはV31）
	 * @return OpenAPI仕様をWebAPI定義へマッピングした結果
	 */
	public List<WebApiMapInfo> mapWebApi(OpenAPI openApi, OpenApiFileType fileType, OpenApiVersion version) {
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
					var context = new WebApiOpenApiConvertContext(processUnit.getWebApi(), processUnit.getWebApi().getName(), openApi, path, pathItem,
							fileType, version);
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
	 * 機能は {@link org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConverter#getOrder()} でソートされている必要があります。
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
				new WebApiParameterMappingsOpenApiConverter(),
				//
				new WebApiOpenApiOpenApiConverter()));
		list.sort((m1, m2) -> m1.getOrder() - m2.getOrder());
		return list;
	}

	/**
	 * WebAPI パスパラメータ分解クラスのインスタンスを生成します。
	 * @return WebAPI パスパラメータ分解クラスのインスタンス
	 */
	protected WebApiPathParameterDecomposer createWebApiPathParameterDecomposer() {
		return new WebApiPathParameterDecomposer();
	}

	/**
	 * OpenAPIのパス・メソッドを分解クラスのインスタンスを生成します。
	 * @return OpenAPIのパス・メソッドを分解クラスのインスタンス
	 */
	protected OpenApiPathMethodDecomposer createOpenApiPathMethodDecomposer() {
		return new OpenApiPathMethodDecomposer();
	}

	/**
	 * WebAPI定義に設定されている OpenAPI を、ベースとなる OpenAPI にマージします。
	 * @param base ベースOpenAPI
	 * @param webApi WebAPI定義
	 */
	protected void mergeOpenApi(OpenAPI base, WebApiDefinition webApi) {
		// OpenAPIが定義されている場合は、OpenAPIをマージする。
		var version = OpenApiVersion.fromSeriesVersion(webApi.getOpenApiVersion());
		var fileType = OpenApiFileType.fromDisplayName(webApi.getOpenApiFileType());
		OpenApiService service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
		try {
			var webApiOpenApi = service.getOpenApiResolver().getObjectMapper(fileType, version).readValue(webApi.getOpenApi(), OpenAPI.class);
			// マージ対象は paths と components
			mergeComponents(base, webApiOpenApi);
			mergePaths(base, webApiOpenApi);

			logger.debug("Merged OpenAPI that was set in WebAPI: {}.", webApi.getName());

		} catch (JsonProcessingException e) {
			// OpenAPIのパースに失敗
			logger.warn("The OpenAPI configured for WebAPI: {} is incorrect.", webApi.getName(), e);
		}
	}

	/**
	 * OpenAPIのComponentsをマージします。
	 * @param base ベースとなるOpenAPI
	 * @param overlay ベースに追記するOpenAPI
	 * @return マージされたOpenAPI
	 */
	protected OpenAPI mergeComponents(OpenAPI base, OpenAPI overlay) {
		if (null == overlay.getComponents()) {
			// components が定義されていない場合は、マージ対象が存在しないため終了。
			return base;
		}

		if (null == base.getComponents()) {
			// base の components が定義されていない場合は、空の Components をセットする。
			base.setComponents(new Components());
		}

		// schema
		mergeMap(base.getComponents().getSchemas(), overlay.getComponents().getSchemas(),
				() -> {
					base.getComponents().setSchemas(new LinkedHashMap<>());
					return base.getComponents().getSchemas();
				});
		// responses
		mergeMap(base.getComponents().getResponses(), overlay.getComponents().getResponses(),
				() -> {
					base.getComponents().setResponses(new LinkedHashMap<>());
					return base.getComponents().getResponses();
				});
		// parameters
		mergeMap(base.getComponents().getParameters(), overlay.getComponents().getParameters(),
				() -> {
					base.getComponents().setParameters(new LinkedHashMap<>());
					return base.getComponents().getParameters();
				});
		// examples
		mergeMap(base.getComponents().getExamples(), overlay.getComponents().getExamples(),
				() -> {
					base.getComponents().setExamples(new LinkedHashMap<>());
					return base.getComponents().getExamples();
				});
		// requestBodies
		mergeMap(base.getComponents().getRequestBodies(), overlay.getComponents().getRequestBodies(),
				() -> {
					base.getComponents().setRequestBodies(new LinkedHashMap<>());
					return base.getComponents().getRequestBodies();
				});
		// headers
		mergeMap(base.getComponents().getHeaders(), overlay.getComponents().getHeaders(),
				() -> {
					base.getComponents().setHeaders(new LinkedHashMap<>());
					return base.getComponents().getHeaders();
				});
		// securitySchemas
		mergeMap(base.getComponents().getSecuritySchemes(), overlay.getComponents().getSecuritySchemes(),
				() -> {
					base.getComponents().setSecuritySchemes(new LinkedHashMap<>());
					return base.getComponents().getSecuritySchemes();
				});
		// links
		mergeMap(base.getComponents().getLinks(), overlay.getComponents().getLinks(),
				() -> {
					base.getComponents().setLinks(new LinkedHashMap<>());
					return base.getComponents().getLinks();
				});
		// callbacks
		mergeMap(base.getComponents().getCallbacks(), overlay.getComponents().getCallbacks(),
				() -> {
					base.getComponents().setCallbacks(new LinkedHashMap<>());
					return base.getComponents().getCallbacks();
				});

		if (SpecVersion.V31 == base.getSpecVersion()) {
			// for v3.1

			// pathItems
			mergeMap(base.getComponents().getPathItems(), overlay.getComponents().getPathItems(),
					() -> {
						base.getComponents().setPathItems(new LinkedHashMap<>());
						return base.getComponents().getPathItems();
					});
		}

		return base;
	}

	/**
	 * OpenAPIのPathsをマージします。
	 * @param base ベースとなるOpenAPI
	 * @param overlay ベースに追記するOpenAPI
	 * @return マージされたOpenAPI
	 */
	protected OpenAPI mergePaths(OpenAPI base, OpenAPI overlay) {
		mergeMap(base.getPaths(), overlay.getPaths(), () -> {
			base.setPaths(new Paths());
			return base.getPaths();
		});

		return base;
	}

	/**
	 * マップをマージします。
	 * @param <T> マップキーデータ型
	 * @param <U> マップデータデータ型
	 * @param base ベースとなるマップ
	 * @param overlay ベースに追加するマップ
	 * @param baseInit ベースマップがnullの場合に初期化するためのSupplier
	 */
	protected <T, U> void mergeMap(Map<T, U> base, Map<T, U> overlay, Supplier<Map<T, U>> baseInit) {
		if (null == overlay) {
			return;
		}

		var mergeBase = null == base ? baseInit.get() : base;

		mergeBase.putAll(overlay);
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
