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
package org.iplass.adminconsole.server.tools.rpc.openapisupport;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.server.base.io.download.DownloadRuntimeException;
import org.iplass.adminconsole.shared.tools.dto.openapisupport.OpenApiSupportTreeGridSelected;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.entity.EntityWebApiOpenApiEntry;
import org.iplass.mtp.impl.webapi.openapi.entity.EntityWebApiType;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinitionManager;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinitionManager;
import org.iplass.mtp.webapi.definition.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.definition.openapi.OpenApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenAPIサポート Export サービス
 * <pre>
 * WebAPI, EntityWebAPIを選択してOpenAPI仕様をエクスポートします。
 * </pre>
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportExportService extends AdminDownloadService {
	/** serialVersionUID */
	private static final long serialVersionUID = 2475655180731631372L;
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(OpenApiSupportExportService.class);

	@Override
	protected void doDownload(HttpServletRequest req, HttpServletResponse resp, int tenantId) {
		try {
			OpenApiFileType fileType = OpenApiFileType.fromDisplayName(req.getParameter(OpenApiSupportRpcConstant.Export.Parameter.FILE_TYPE));
			OpenApiVersion version = OpenApiVersion.fromSeriesVersion(req.getParameter(OpenApiSupportRpcConstant.Export.Parameter.VERSION));
			String selectValue = req.getParameter(OpenApiSupportRpcConstant.Export.Parameter.SELECT_VALUE);

			logger.info("version = {}, fileTYpe = {}, selectValue = {}", version, fileType, selectValue);

			var dto = restoreDto(selectValue);

			var webApiList = getWebApiList(dto);
			var entityWebApiList = getEntityWebApiList(dto);

			String contentType = switch (fileType) {
			case JSON -> "application/json";
			case YAML -> "application/yaml";
			};
			var currentDateTime = DateUtil.getCurrentZonedDateTimeFormat(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
			String fileName = String.valueOf(tenantId) + "-openapi-" + currentDateTime + "." + fileType.getExtension();
			resp.setContentType(contentType);
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			var service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
			service.writeOpenApiSpec(resp.getOutputStream(), webApiList, entityWebApiList, fileType, version);

		} catch (IOException e) {
			throw new DownloadRuntimeException(e);
		}
	}

	/**
	 * OpenApiSupportTreeGridSelected DTO に復元します。
	 * <p>
	 * {@link org.iplass.adminconsole.client.tools.ui.openapisupport.OpenApiSupportMainPane} の #encodeString メソッドで DTO から文字列に変換しています。
	 * </p>
	 * @param value 変換された文字列
	 * @return 復元された画面で選択した WebAPI, Entity CRUD API の情報
	 */
	public OpenApiSupportTreeGridSelected restoreDto(String value) {
		if (value == null) {
			return new OpenApiSupportTreeGridSelected();
		}

		String[] parts = value.split("\\|", -1);
		if (parts.length != 2) {
			throw new RuntimeException();
		}

		var dto = new OpenApiSupportTreeGridSelected();
		var webApiValue = parts[0];
		var webApiArray = webApiValue.split(":");
		for (String path : webApiArray) {
			dto.addWebApi(path);
		}

		var entityCRUDApiValue = parts[1];
		var entityCRUDApiArray = entityCRUDApiValue.split(":");
		for (String v : entityCRUDApiArray) {
			String[] pathAndAuthorized = v.split(",");

			for (var i = 1; i < pathAndAuthorized.length; i++) {
				dto.addEntityCRUDApi(pathAndAuthorized[0], pathAndAuthorized[i]);
			}
		}

		return dto;
	}

	/**
	 * WebAPIの定義リストを取得します。
	 * <p>
	 * クライアント画面で選択した WebAPI 情報から WebAPI 定義情報を取得します。
	 * WebAPI定義情報がみつからない場合は、警告ログを出力し処理を継続します。
	 * </p>
	 *
	 * @param selected 画面で選択された WebAPI, Entity CRUD API の情報
	 * @return WebAPI定義リスト
	 */
	private List<WebApiDefinition> getWebApiList(OpenApiSupportTreeGridSelected selected) {
		if (selected.isNotSelectWebApi()) {
			return Collections.emptyList();
		}

		var wdm = ManagerLocator.manager(WebApiDefinitionManager.class);
		var list = new ArrayList<WebApiDefinition>();
		for (var definitionName : selected.getWebApiList()) {

			var webapiDefinition = wdm.get(definitionName);
			if (null == webapiDefinition) {
				logger.warn("WebApiDefinition not found: {}", definitionName);
				continue;
			}

			list.add(webapiDefinition);
		}
		return list;
	}

	/**
	 * EntityWebAPIの定義リストを取得します。
	 * <p>
	 * クライアント画面で選択した Entity CRUD API 情報から、Entity 定義名・Entity CRUD API 権限情報を取得します。
	 * EntityWebApiDefinition が見つからない場合や、不正な権限情報が選択された場合は警告ログを出力し、処理を継続します。
	 * </p>
	 * @param selected 画面で選択された WebAPI, Entity CRUD API の情報
	 * @return EntityWebAPI定義リスト
	 */
	private List<EntityWebApiOpenApiEntry> getEntityWebApiList(OpenApiSupportTreeGridSelected selected) {
		if (selected.isNotSelectEntityCRUDApi()) {
			return Collections.emptyList();
		}

		var ewdm = ManagerLocator.manager(EntityWebApiDefinitionManager.class);
		var list = new ArrayList<EntityWebApiOpenApiEntry>();
		for (var crudEntry : selected.getEntityCRUDApiMap().entrySet()) {
			String definitionName = crudEntry.getKey();
			List<String> webapiTypeStringList = crudEntry.getValue();

			var entityWebApiDefinition = ewdm.get(definitionName);
			if (null == entityWebApiDefinition) {
				// EntityWebApiDefinition が見つからない場合は警告ログを出力し、処理を継続する。
				logger.warn("EntityWebApiDefinition not found. Entity : {}", definitionName);
				continue;
			}

			if (null == webapiTypeStringList || webapiTypeStringList.isEmpty()) {
				logger.warn("Entity CRUD API operation not selected. Entity : {}", definitionName);
				continue;
			}

			var webapiTypeList = webapiTypeStringList.stream().map(t -> {
				var mapped = EntityWebApiType.getValue(t);
				if (t == null) {
					// 不正な権限情報が設定された場合は警告ログを出力
					logger.warn("Invalid EntityWebApiTyp. Entity: {}, EntityWebApiType: {}", definitionName, t);
				}
				return mapped;

			}).filter(t -> {
				if (null == t) {
					return false;
				}

				// 権限の再チェック
				boolean isAllow = switch (t) {
				case LOAD -> entityWebApiDefinition.isLoad();
				case QUERY -> entityWebApiDefinition.isQuery();
				case INSERT -> entityWebApiDefinition.isInsert();
				case UPDATE -> entityWebApiDefinition.isUpdate();
				case DELETE -> entityWebApiDefinition.isDelete();
				};

				if (!isAllow) {
					// 許可されていない場合はログ出力
					logger.warn("Unauthorized privileges were set. Entity: {}, EntityWebApiType: {}", definitionName, t);
				}
				return isAllow;

			}).toList();

			if (webapiTypeList.isEmpty()) {
				// 許可されていない権限のみの場合はスキップ
				continue;
			}

			list.add(new EntityWebApiOpenApiEntry(definitionName, webapiTypeList));
		}

		return list;
	}
}
