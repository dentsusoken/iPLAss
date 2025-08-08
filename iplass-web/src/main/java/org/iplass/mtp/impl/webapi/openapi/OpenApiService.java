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
package org.iplass.mtp.impl.webapi.openapi;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.iplass.mtp.impl.util.RequestPathUtil;
import org.iplass.mtp.impl.webapi.openapi.entity.EntityWebApiOpenApiEntry;
import org.iplass.mtp.impl.webapi.openapi.entity.EntityWebApiOpenApiMapper;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiComponentReusableSchemaFactory;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiComponentReusableSchemaFactoryAssigner;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiStandardClassSchemaResolver;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiStandardClassSchemaResolverImpl;
import org.iplass.mtp.impl.webapi.openapi.webapi.OpenApiMappedWebApiUpdater;
import org.iplass.mtp.impl.webapi.openapi.webapi.WebApiOpenApiMapper;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.openapi.OpenApiVersion;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI 操作サービス
 * @author SEKIGUCHI Naoya
 */
public class OpenApiService implements Service {
	/** OpenAPI リゾルバ */
	private OpenApiResolver openApiResolver;
	/** エンティティ Web API マッパー */
	private EntityWebApiOpenApiMapper entityWebApiMapper;
	/** WebAPI マッパー */
	private WebApiOpenApiMapper webApiMapper;
	/** WebAPI 更新機能 */
	private OpenApiMappedWebApiUpdater webApiUpdater;
	/** OpenAPI Component スキーマ生成クラス */
	private OpenApiComponentReusableSchemaFactory<? super Object> reusableSchemaFactory;
	/** OpenAPI 標準クラススキーマ解決クラス */
	private OpenApiStandardClassSchemaResolver standardSchemaResolver;

	/** エクスポート時の info.title の値 */
	private String infoTitleOfExport;
	/** エクスポート時の info.version の値 */
	private String infoVersionOfExport;
	/** エクスポート時の info.description の値 */
	private String infoDescriptionOfExport;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		this.openApiResolver = createDefaultOpenApiResolver();
		this.entityWebApiMapper = createDefaultEntityWebApiOpenApiMapper();
		this.webApiMapper = createDefaultWebApiOpenApiMapper();
		this.webApiUpdater = createDefaultOpenApiMappedWebApiUpdater();
		this.reusableSchemaFactory = (OpenApiComponentReusableSchemaFactory<? super Object>) createDefaultOpenApiComponentSchemaFactory();
		this.standardSchemaResolver = createDefaultOpenApiClassSchemaResolver();

		this.infoTitleOfExport = config.getValue("infoTitleOfExport");
		this.infoVersionOfExport = config.getValue("infoVersionOfExport");
		this.infoDescriptionOfExport = config.getValue("infoDescriptionOfExport");
	}

	@Override
	public void destroy() {
	}

	/**
	 * OpenAPI仕様を出力ストリームに書き込みます。
	 * @param out 出力先のOutputStream
	 * @param webApiList WebAPI定義のリスト
	 * @param entityWebApiList エンティティWebAPIのOpenAPIエントリのリスト
	 * @param fileType OpenAPIファイルのタイプ（JSONまたはYAML）
	 * @param version OpenAPIのバージョン（V30またはV31）
	 * @throws IOException 入出力エラー
	 */
	public void writeOpenApiSpec(OutputStream out, List<WebApiDefinition> webApiList, List<EntityWebApiOpenApiEntry> entityWebApiList,
			OpenApiFileType fileType, OpenApiVersion version) throws IOException {
		var openApi = new OpenAPI(openApiResolver.getOpenApiSpecVersion(version));
		openApi.openapi(version.getDefaultVersion());
		openApi.info(new Info()
				.title(infoTitleOfExport)
				.version(infoVersionOfExport)
				.description(infoDescriptionOfExport));
		openApi.addServersItem(new Server().url(RequestPathUtil.getWebApiRoot()));

		webApiMapper.mapOpenApi(webApiList, openApi, fileType, version);

		for (var entityWebApi : entityWebApiList) {
			entityWebApiMapper.map(openApi, entityWebApi.getEntityDefinitionName(), entityWebApi.getEntityWebApiTypeList());
		}

		openApiResolver.getObjectWriter(fileType, version).writeValue(out, openApi);
	}

	/**
	 * OpenAPI仕様をWebAPIとしてインポートします。
	 * @param file インポートするOpenAPI仕様のファイル
	 * @param fileType OpenAPIファイルのタイプ（JSONまたはYAML）
	 * @param version OpenAPIのバージョン（V30またはV31）
	 * @return インポート結果のリスト
	 * @throws IOException 入出力エラー
	 */
	public List<OpenApiImportResult> importOpenApiToWebApi(File file, OpenApiFileType fileType, OpenApiVersion version) throws IOException {
		try (var reader = Files.newBufferedReader(Path.of(file.toURI()), StandardCharsets.UTF_8)) {

			var openApi = openApiResolver.getObjectMapper(fileType, version).readValue(reader, OpenAPI.class);
			var webApiMapResultList = webApiMapper.mapWebApi(openApi, fileType, version);
			var updateResult = webApiUpdater.updateWebApi(webApiMapResultList);
			return updateResult.stream().map(r -> {
				var webApiDefinition = r.getMapInfo().getWebApiDefinition();
				var webApiPath = webApiDefinition == null ? "Not Import." : webApiDefinition.getName();
				return new OpenApiImportResult(
						r.getMapInfo().getOpenApiPath(),
						webApiPath,
						r.getMapInfo().getUpdateType().name(),
						r.getUpdateResult().name());
			}).toList();
		}
	}

	/**
	 * OpenAPI スキーマ生成機能を取得する。
	 * <p>
	 * 本機能は EntityDefinition や複雑なクラス構成から再利用可能な形式で OpenAPI のスキーマを定義します。
	 * </p>
	 * @return OpenAPIスキーマ生成機能
	 */
	public OpenApiComponentReusableSchemaFactory<? super Object> getReusableSchemaFactory() {
		return reusableSchemaFactory;
	}

	/**
	 * OpenAPI 標準クラススキーマ解決機能を取得します。
	 * <p>
	 * 本機能は String, Long, Boolean 等の単純なデータ型をスキーマに変換します。
	 * </p>
	 * @return OpenAPI 標準クラススキーマ解決機能
	 */
	public OpenApiStandardClassSchemaResolver getStandardClassSchemaResolver() {
		return standardSchemaResolver;
	}

	/**
	 * OpenAPIリゾルバーを取得します。
	 * @return OpenApiResolver インスタンス
	 */
	public OpenApiResolver getOpenApiResolver() {
		return openApiResolver;
	}

	/**
	 * デフォルトの OpenApiResolver を生成します。
	 * @return OpenApiResolver インスタンス
	 */
	protected OpenApiResolver createDefaultOpenApiResolver() {
		return new OpenApiResolver();
	}

	/**
	 * デフォルトの EntityWebApiOpenApiMapper を生成します。
	 * @return EntityWebApiOpenApiMapper インスタンス
	 */
	protected EntityWebApiOpenApiMapper createDefaultEntityWebApiOpenApiMapper() {
		return new EntityWebApiOpenApiMapper();
	}

	/**
	 * デフォルトの WebApiOpenApiMapper を生成します。
	 * @return WebApiOpenApiMapper インスタンス
	 */
	protected WebApiOpenApiMapper createDefaultWebApiOpenApiMapper() {
		return new WebApiOpenApiMapper();
	}

	/**
	 * デフォルトの OpenApiMappedWebApiUpdater を生成します。
	 * @return OpenApiMappedWebApiUpdater インスタンス
	 */
	protected OpenApiMappedWebApiUpdater createDefaultOpenApiMappedWebApiUpdater() {
		return new OpenApiMappedWebApiUpdater();
	}

	/**
	 * デフォルトの OpenApiComponentSchemaFactory を生成します。
	 * @return OpenApiComponentSchemaFactory インスタンス
	 */
	protected OpenApiComponentReusableSchemaFactory<?> createDefaultOpenApiComponentSchemaFactory() {
		return new OpenApiComponentReusableSchemaFactoryAssigner();
	}

	/**
	 * デフォルトの OpenApiStandardClassSchemaResolver を生成します。
	 * @return OpenApiStandardClassSchemaResolver インスタンス
	 */
	protected OpenApiStandardClassSchemaResolver createDefaultOpenApiClassSchemaResolver() {
		return new OpenApiStandardClassSchemaResolverImpl();
	}
}
