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
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.impl.webapi.openapi.webapi.WebApiOpenApiMapper.WebApiMapInfo;
import org.iplass.mtp.webapi.definition.WebApiDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenAPI でマッピングした WebAPI の更新クラス
 * <p>
 * {@link org.iplass.mtp.impl.webapi.openapi.webapi.WebApiOpenApiMapper#mapWebApi(io.swagger.v3.oas.models.OpenAPI, org.iplass.mtp.webapi.openapi.OpenApiFileType, org.iplass.mtp.webapi.openapi.OpenApiVersion)} で OpenAPI から WebAPI にマッピングされた結果をもとに、
 * WebAPI 定義を更新するクラスです。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class OpenApiMappedWebApiUpdater {
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(OpenApiMappedWebApiUpdater.class);

	/**
	 * WebAPIの更新を行います。
	 * @param webApiMapInfoList WebAPIマッピング情報のリスト
	 * @return 更新結果のリスト
	 */
	public List<WebApiUpdateInfo> updateWebApi(List<WebApiMapInfo> webApiMapInfoList) {
		var result = new ArrayList<WebApiUpdateInfo>();

		for (var webApiMapInfo : webApiMapInfoList) {
			try {
				var updateResult = doUpdate(webApiMapInfo);
				var updateState = updateResult != null ? updateResult.isSuccess() ? WebApiUpdateState.SUCCESS : WebApiUpdateState.FAILED
						: WebApiUpdateState.NOT_PROCESS;
				result.add(new WebApiUpdateInfo(webApiMapInfo, updateState));

			} catch (RuntimeException e) {
				logger.warn("Failed to update WebAPI. OpenAPI path = {}, definitionName = {}.",
						webApiMapInfo.getOpenApiPath(), webApiMapInfo.getWebApiDefinition().getName(), e);
				var errorResult = new WebApiUpdateInfo(webApiMapInfo, WebApiUpdateState.FAILED);
				result.add(errorResult);
			}
		}

		return result;
	}

	/**
	 * WebAPIの更新を行います。
	 * <p>
	 * {@link org.iplass.mtp.impl.webapi.openapi.webapi.WebApiUpdateType} に基づいて WebAPI の更新を行います。
	 * </p>
	 * @param webApiMapInfo WebAPIマッピング情報
	 * @return 更新結果
	 */
	public DefinitionModifyResult doUpdate(WebApiMapInfo webApiMapInfo) {
		var wdm = ManagerLocator.manager(WebApiDefinitionManager.class);
		return switch (webApiMapInfo.getUpdateType()) {
		// 新規登録
		case CREATE -> wdm.create(webApiMapInfo.getWebApiDefinition());
		// 更新
		case UPDATE -> wdm.update(webApiMapInfo.getWebApiDefinition());
		// 処理無し
		case NONE -> null;
		};
	}

	/**
	 * WebAPIの更新結果情報クラス
	 */
	public static class WebApiUpdateInfo  {
		/** WebAPIマッピング情報 */
		private WebApiMapInfo mapInfo;
		/** WebAPI更新結果 */
		private WebApiUpdateState updateResult;

		/**
		 * コンストラクタ
		 * @param mapInfo WebAPIマッピング情報
		 * @param updateResult WebAPI更新結果
		 */
		public WebApiUpdateInfo(WebApiMapInfo mapInfo, WebApiUpdateState updateResult) {
			this.mapInfo = mapInfo;
			this.updateResult = updateResult;
		}

		/**
		 * WebAPIマッピング情報を取得します。
		 * @return WebAPIマッピング情報
		 */
		public WebApiMapInfo getMapInfo() {
			return mapInfo;
		}

		/**
		 * WebAPI更新結果を取得します。
		 * @return WebAPI更新結果
		 */
		public WebApiUpdateState getUpdateResult() {
			return updateResult;
		}
	}
}
