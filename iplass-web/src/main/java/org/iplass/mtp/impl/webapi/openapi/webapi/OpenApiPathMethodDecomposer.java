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
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;

/**
 * OpenAPIのパス・メソッドを分解クラス
 * <p>
 * OpenAPI の PathItem を解析し WebAPI へマッピングする単位を作成します。
 * 本クラスの実行結果は、WebAPI マッピング時のループ単位となります。
 * </p>
 * <h3>分解方針</h3>
 * <ul>
 * <li>OpenAPIのパスを調査し、WebAPIのパスを決定します。</li>
 * <li>WebAPIパスでMetaDataを検索し、すでに存在する場合は、PathItem 全体をメタデータにマッピングします。</li>
 * <li>WebAPIパスでMetaDataが存在しない場合は、PathItemのメソッドごとにWebAPIの定義を作成します。WebAPIパスは末尾にメソッド名を付与します。</li>
 * <li>PathItemに対して１つのメソッドしか存在せず、既存のメソッドが存在しない場合は、末尾のメソッドを除去します。</li>
 * </ul>
 * @author SEKIGUCHI Naoya
 */
public class OpenApiPathMethodDecomposer {
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(OpenApiPathMethodDecomposer.class);

	/**
	 * OpenAPI のパス・メソッドを分解する。
	 * @param openApi OpenAPIインスタンス
	 * @param path OpenAPIのパス
	 * @param pathItem OpenAPIのPathItem
	 * @return 分解されたマッピング処理単位
	 */
	public List<OpenApiProcessUnit> decompose(OpenAPI openApi, String path, PathItem pathItem) {
		// OpenAPI path から WebAPI path を作成する
		var parameterStartIndex = path.indexOf("/{");
		var webApiBasePath = parameterStartIndex < 0
				// "{" が見つからない場合、/path/to/openapi のような形式。先頭のスラッシュを除去し、そのまま利用する。( 例： /path/to/openapi -> path/to/openapi )
				? path.substring(1)
						// "{" が見つかった場合、/path/to/openapi/{parameter} のような形式。先頭のスラッシュとパラメーター部を除去する。 ( 例: /path/to/openapi/{parameter} -> path/to/openapi )
						: path.substring(1, parameterStartIndex);

		// WebAPI 定義が存在するかチェックする
		var wdm = ManagerLocator.manager(WebApiDefinitionManager.class);
		var def = wdm.get(webApiBasePath);

		if (null != def) {
			// WebAPI定義が存在する場合、
			return List.of(new OpenApiProcessUnit(def, null, WebApiUpdateType.UPDATE));

		}

		// WebAPI定義が存在しない場合、メソッド単位で WebAPI定義を作成する
		var result = new ArrayList<OpenApiProcessUnit>();
		// WebAPI定義が存在しない場合、メソッド単位で WebAPI定義を作成する
		for (var entry : pathItem.readOperationsMap().entrySet()) {
			// 対象メソッドを判別
			var methodType = switch (entry.getKey()) {
			case GET -> MethodType.GET;
			case PUT -> MethodType.PUT;
			case POST -> MethodType.POST;
			case DELETE -> MethodType.DELETE;
			case PATCH -> MethodType.PATCH;
			default -> null;
			};

			// 対応していないメソッドの場合はスキップする
			if (null == methodType) {
				logger.warn("OpenAPI path:{}, method:{}. method is not supported for WebAPI definition. Skipping.", path, entry.getKey());
				continue;
			}

			var webApiPath = new StringBuilder(webApiBasePath).append("/").append(methodType.name()).toString();

			var methodDef = wdm.get(webApiPath);
			if (null != methodDef) {
				result.add(new OpenApiProcessUnit(methodDef, methodType, WebApiUpdateType.UPDATE));

			} else {
				// WebAPI定義が存在しない場合、WebAPI定義を作成する
				var newDef = new WebApiDefinition();
				newDef.setName(webApiPath);

				result.add(new OpenApiProcessUnit(newDef, methodType, WebApiUpdateType.CREATE));
			}
		}

		// webApiBasePath 配下に別の WebAPI が存在しているかを確認する
		var underWebApiBasePathDefList = wdm.definitionSummaryList(webApiBasePath);

		if (result.size() == 1 && result.get(0).getUpdateType() == WebApiUpdateType.CREATE && underWebApiBasePathDefList.size() == 0) {
			// メソッドが１つ且つ、新規作成 且つ、webApiBasePath 配下に別のWebAPI が存在していない場合は、メソッド名が指定されていないパスを設定する。
			// 「webApiBasePath 配下に別のWebAPI が存在していない」という条件は、path/to/webapi/GET を作成しようとしたときに、path/to/webapi/POST のように別のメソッドが存在しないこと。
			var before = result.get(0);

			// 末尾にメソッド名が付与されている名前から、メソッド名が付与されていないものに変更する
			before.webApi.setName(webApiBasePath);
		}

		return result;
	}

	/**
	 * OpenAPI を分解し処理する単位
	 * <p>
	 * 内包する WebApiDefinition には以下の特徴があります。
	 * </p>
	 * <ul>
	 * <li>webApiPathに対応するWebAPIメタデータが存在する場合は、存在するメタデータがベースとなります。</li>
	 * <li>webApiPathに対応するWebAPIメタデータが存在しない場合は、名前だけが設定されたメタデータとなります。</li>
	 * </ul>
	 */
	public static class OpenApiProcessUnit {
		/** WebAPI メタデータ定義 */
		private WebApiDefinition webApi;
		/** メソッドタイプ */
		private MethodType methodType;
		/** 更新タイプ */
		private WebApiUpdateType updateType;

		/**
		 * コンストラクタ
		 * @param webApi WebAPIメタデータ定義
		 * @param methodType メソッドタイプ
		 * @param updateType 更新タイプ
		 */
		public OpenApiProcessUnit(WebApiDefinition webApi, MethodType methodType, WebApiUpdateType updateType) {
			this.webApi = webApi;
			this.methodType = methodType;
			this.updateType = updateType;
		}

		/**
		 * WebAPIメタデータ定義を取得します
		 * @return WebAPIメタデータ定義
		 */
		public WebApiDefinition getWebApi() {
			return webApi;
		}

		/**
		 * メソッドタイプを取得します
		 * <p>
		 * メソッドタイプが存在している場合、OpenAPI PathItem の指定されているメソッドのみをWebAPIにマッピングします。<br>
		 * メソッドタイプが存在しない場合、OpenAPI PathItem の全てのメソッドを１つのWebAPIにマッピングします。
		 * </p>
		 * @return メソッドタイプ
		 */
		public MethodType getMethodType() {
			return methodType;
		}

		/**
		 * 更新タイプを取得します
		 * @return 更新タイプ
		 */
		public WebApiUpdateType getUpdateType() {
			return updateType;
		}

	}
}
