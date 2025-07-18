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

/**
 * OpenAPIインポート結果
 * <p>
 * 本クラスでは、以下の情報を表現します。
 * </p>
 * <ul>
 * <li>インポート元の OpenAPI パス</li>
 * <li>インポート先 WebAPI パス</li>
 * <li>更新タイプ（新規作成、更新、何もしない）</li>
 * <li>更新状態（成功、失敗）</li>
 * </ul>
 * @author SEKIGUCHI Naoya
 */
public class OpenApiImportResult {
	/** OpenAPI パス*/
	private String openApiPath;
	/** インポート先 WebAPI 定義パス */
	private String webApiDefinitionPath;
	/** 更新タイプ */
	private String updateType;
	/** 更新状態 */
	private String updateState;

	/**
	 * コンストラクタ
	 * @param openApiPath OpenAPIパス
	 * @param webApiDefinitionPath WebAPI定義パス
	 * @param updateType 更新タイプ
	 * @param updateState 更新状態
	 */
	public OpenApiImportResult(String openApiPath, String webApiDefinitionPath, String updateType, String updateState) {
		this.openApiPath = openApiPath;
		this.webApiDefinitionPath = webApiDefinitionPath;
		this.updateType = updateType;
		this.updateState = updateState;
	}

	/**
	 * OpenAPIパスを取得します
	 * @return OpenAPIパス
	 */
	public String getOpenApiPath() {
		return openApiPath;
	}

	/**
	 * WebAPI定義パスを取得します
	 * @return WebAPI定義パス
	 */
	public String getWebApiDefinitionPath() {
		return webApiDefinitionPath;
	}

	/**
	 * 更新タイプを取得します
	 * @return 更新タイプ
	 */
	public String getUpdateType() {
		return updateType;
	}

	/**
	 * 更新状態を取得します
	 * @return 更新状態
	 */
	public String getUpdateState() {
		return updateState;
	}

}
