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
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iplass.adminconsole.server.base.io.download.AdminDownloadService;
import org.iplass.adminconsole.shared.tools.dto.openapisupport.OpenApiSupportSelectedConverter;
import org.iplass.adminconsole.shared.tools.dto.openapisupport.OpenApiSupportTreeGridSelected;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.entity.EntityWebApiOpenApiEntry;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinitionManager;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinitionManager;
import org.iplass.mtp.webapi.openapi.EntityWebApiType;
import org.iplass.mtp.webapi.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.openapi.OpenApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class OpenApiSupportExportService extends AdminDownloadService {
	private Logger logger = LoggerFactory.getLogger(OpenApiSupportExportService.class);

	@Override
	protected void doDownload(HttpServletRequest req, HttpServletResponse resp, int tenantId) {
		try {
			OpenApiFileType fileType = OpenApiFileType.fromDisplayName(req.getParameter(OpenApiSupportRpcConstant.Export.Parameter.FILE_TYPE));
			OpenApiVersion version = OpenApiVersion.fromSeriesVersion(req.getParameter(OpenApiSupportRpcConstant.Export.Parameter.VERSION));
			String selectValue = req.getParameter(OpenApiSupportRpcConstant.Export.Parameter.SELECT_VALUE);

			logger.info("version = {}, fileTYpe = {}, selectValue = {}", version, fileType, selectValue);

			var dto = OpenApiSupportSelectedConverter.convertDto(selectValue);

//			var edm = ManagerLocator.manager(EntityDefinitionManager.class);

			var webApiList = getWebApiList(dto);
			var entityWebApiList = getEntityWebApiList(dto);

			String contentType = switch (fileType) {
			case JSON -> "application/json";
			case YAML -> "application/yaml";
			};
			String fileName = "openapi." + fileType.getExtension();
			resp.setContentType(contentType);
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			var service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
			service.writeOpenApiSpec(resp.getOutputStream(), webApiList, entityWebApiList, fileType, version);

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private List<WebApiDefinition> getWebApiList(OpenApiSupportTreeGridSelected selected) {
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

	private List<EntityWebApiOpenApiEntry> getEntityWebApiList(OpenApiSupportTreeGridSelected selected) {

		var ewdm = ManagerLocator.manager(EntityWebApiDefinitionManager.class);
		var list = new ArrayList<EntityWebApiOpenApiEntry>();
		for (var c : selected.getEntityCRUDApiMap().entrySet()) {
			String definitionName = c.getKey();
			List<String> webapiTypeStringList = c.getValue();

			var entityWebapiDefinition = ewdm.get(definitionName);
			if (null == entityWebapiDefinition) {
				logger.warn("EntityWebApiDefinition not found: {}", definitionName);
				continue;
			}

			if (null == webapiTypeStringList || webapiTypeStringList.isEmpty()) {
				logger.warn("EntityWebApiDefinition has no webapi: {}", definitionName);
				continue;
			}

			var webapiTypeList = webapiTypeStringList.stream().map(t -> EntityWebApiType.getValue(t)).filter(t -> {
				// 権限の再チェック
				boolean isAllow = switch (t) {
				case LOAD -> entityWebapiDefinition.isLoad();
				case QUERY -> entityWebapiDefinition.isQuery();
				case INSERT -> entityWebapiDefinition.isInsert();
				case UPDATE -> entityWebapiDefinition.isUpdate();
				case DELETE -> entityWebapiDefinition.isDelete();
				};

				if (!isAllow) {
					// 許可されていない場合はログ出力
					// FIXME ログを英語
					logger.warn("許可されていない権限が設定されていました: {}, {}", definitionName, t);
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
