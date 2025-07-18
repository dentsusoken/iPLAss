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
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiComponentSchemaFactory;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiComponentSchemaFactoryAssigner;
import org.iplass.mtp.impl.webapi.openapi.webapi.OpenApiMappedWebApiUpdater;
import org.iplass.mtp.impl.webapi.openapi.webapi.WebApiOpenApiMapper;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.openapi.OpenApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI操作サービスの実装クラス。
 * @author SEKIGUCHI Naoya
 */
public class OpenApiServiceImpl implements OpenApiService {
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(OpenApiServiceImpl.class);

	private OpenApiResolver openApiResolver;
	private EntityWebApiOpenApiMapper entityWebApiMapper;
	private WebApiOpenApiMapper webApiMapper;
	private OpenApiComponentSchemaFactory<? super Object> schemaFactory;
	private OpenApiMappedWebApiUpdater webApiUpdater;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		this.openApiResolver = config.getValueWithSupplier("openApiResolver", OpenApiResolver.class, () -> createDefaultOpenApiResolver());
		this.entityWebApiMapper = config.getValueWithSupplier("entityWebApiMapper", EntityWebApiOpenApiMapper.class,
				() -> createDefaultEntityWebApiOpenApiMapper());
		this.webApiMapper = config.getValueWithSupplier("webApiMapper", WebApiOpenApiMapper.class, () -> createDefaultWebApiOpenApiMapper());
		this.schemaFactory = config.getValueWithSupplier("schemaFactory", OpenApiComponentSchemaFactory.class,
				() -> createOpenApiComponentSchemaFactory());
		this.webApiUpdater = config.getValueWithSupplier("webApiUpdater", OpenApiMappedWebApiUpdater.class, () -> createOpenApiMappedWebApiUpdater());
	}

	@Override
	public void destroy() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void writeOpenApiSpec(OutputStream out, List<WebApiDefinition> webApiList, List<EntityWebApiOpenApiEntry> entityWebApiList,
			OpenApiFileType fileType, OpenApiVersion version) throws IOException {
		var openApi = new OpenAPI(openApiResolver.getOpenApiSpecVersion(version));
		openApi.openapi(version.getDefaultVersion());
		openApi.info(new Info()
				.title("iPLAss WebAPI, EntityCRUDApi OpenAPI Spec")
				.version("1.0.0")
				.description("iPLAss WebAPI and EntityCRUDApi OpenAPI Specification"));
		openApi.addServersItem(new Server().url(RequestPathUtil.getWebApiRoot()));

		webApiMapper.mapOpenApi(webApiList, openApi);

		for (var entityWebApi : entityWebApiList) {
			entityWebApiMapper.map(openApi, entityWebApi.getEntityDefinitionName(), entityWebApi.getEntityWebApiTypeList());
		}

		openApiResolver.getObjectWriter(fileType, version).writeValue(out, openApi);
	}

	@Override
	public List<OpenApiImportResult> importOpenApiToWebApi(File file, OpenApiFileType fileType, OpenApiVersion version) throws IOException {
		try (var reader = Files.newBufferedReader(Path.of(file.toURI()), StandardCharsets.UTF_8)) {

			var openApi = openApiResolver.getObjectMapper(fileType, version).readValue(reader, OpenAPI.class);
			var webApiMapResultList = webApiMapper.mapWebApi(openApi);
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

	@Override
	public OpenApiComponentSchemaFactory<? super Object> getSchemaFactory() {
		return schemaFactory;
	}

	protected OpenApiResolver createDefaultOpenApiResolver() {
		return new OpenApiResolver();
	}

	protected EntityWebApiOpenApiMapper createDefaultEntityWebApiOpenApiMapper() {
		return new EntityWebApiOpenApiMapper();
	}

	protected WebApiOpenApiMapper createDefaultWebApiOpenApiMapper() {
		return new WebApiOpenApiMapper();
	}

	protected OpenApiMappedWebApiUpdater createOpenApiMappedWebApiUpdater() {
		return new OpenApiMappedWebApiUpdater();
	}

	protected OpenApiComponentSchemaFactory<?> createOpenApiComponentSchemaFactory() {
		return new OpenApiComponentSchemaFactoryAssigner();
	}
}
