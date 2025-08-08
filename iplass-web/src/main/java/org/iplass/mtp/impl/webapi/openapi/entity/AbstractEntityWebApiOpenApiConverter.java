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
package org.iplass.mtp.impl.webapi.openapi.entity;

import java.util.function.Consumer;

import jakarta.ws.rs.core.Response;

import org.iplass.mtp.entity.definition.EntityDefinition;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.PathParameter;

/**
 * 抽象的なEntityWebApiのOpenAPI変換クラス
 *
 * @author SEKIGUCHI Naoya
 */
public abstract class AbstractEntityWebApiOpenApiConverter implements EntityWebApiOpenApiConverter {
	/** EntityWebAPIベースパス */
	private static final String BASE_PATH = "/mtp/entity";
	/** HTTP Status OK 文字列 */
	protected static final String STATUS_OK = String.valueOf(Response.Status.OK.getStatusCode());
	/** デフォルトレスポンス */
	protected static final String RESPONSE_STATUS = "status";

	/**
	 * EntityWebAPIのベースパスを取得します。
	 * <p>
	 * 返却するパスは <code>/mtp/entity</code> です。
	 * </p>
	 * @return EntityWebAPIのベースパス
	 */
	protected String getBasePath() {
		return BASE_PATH;
	}

	/**
	 * エンティティ名のパスを取得します。
	 * <p>
	 * 返却するパスは <code>/mtp/entity/${entityDefinitionName}</code> です。
	 * ${entityDefinitionName} はエンティティ定義名に置換されています。
	 * </p>
	 * @param entityDefinition エンティティ定義
	 * @return エンティティ名のパス
	 */
	protected String getEntityPath(EntityDefinition entityDefinition) {
		return BASE_PATH + "/" + entityDefinition.getName();
	}

	/**
	 * エンティティのOIDを指定するパスを取得します。
	 * <p>
	 * 返却するパスは <code>/mtp/entity/${entityDefinitionName}/{oid}</code> です。
	 * ${entityDefinitionName} はエンティティ定義名に置換されています。
	 * </p>
	 * @param entityDefinition エンティティ定義
	 * @return エンティティのOIDを指定するパス
	 */
	protected String getEntityOidPath(EntityDefinition entityDefinition) {
		return BASE_PATH + "/" + entityDefinition.getName() + "/{oid}";
	}

	/**
	 * エンティティのOID、バージョンを指定するパスを取得します。
	 * <p>
	 * 返却するパスは <code>/mtp/entity/${entityDefinitionName}/{oid}/{version}</code> です。
	 * ${entityDefinitionName} はエンティティ定義名に置換されています。
	 * </p>
	 * @param entityDefinition エンティティ定義
	 * @return エンティティのOID、バージョンを指定するパス
	 */
	protected String getEntityOidVersionPath(EntityDefinition entityDefinition) {
		return BASE_PATH + "/" + entityDefinition.getName() + "/{oid}/{version}";
	}

	/**
	 * EntityWebAPIのベースパスのPathItemを取得します。
	 * <p>
	 * OpenAPI の paths に PathItem が追加されていない場合にインスタンスを新規作成し追加します。
	 * </p>
	 * @see #getBasePath()
	 * @param openApi OpenAPIオブジェクト
	 * @param entityDefinition EntityDefinition
	 * @return EntityWebAPIのベースパスのPathItem
	 */
	protected PathItem getBasePathItem(OpenAPI openApi, EntityDefinition entityDefinition) {
		return getPathItemIfNotExistCreate(openApi, getBasePath(), pathItem -> {

		});
	}

	/**
	 * エンティティ名のPathItemを取得します。
	 * <p>
	 * OpenAPI の paths に PathItem が追加されていない場合にインスタンスを新規作成し追加します。
	 * </p>
	 * @see #getEntityPath(EntityDefinition)
	 * @param openApi OpenAPIオブジェクト
	 * @param entityDefinition EntityDefinition
	 * @return エンティティ名のPathItem
	 */
	protected PathItem getEntityPathItem(OpenAPI openApi, EntityDefinition entityDefinition) {
		var entityPath = getEntityPath(entityDefinition);
		return getPathItemIfNotExistCreate(openApi, entityPath, pathItem -> {

		});
	}

	/**
	 * エンティティのOIDを指定するPathItemを取得します。
	 * <p>
	 * OpenAPI の paths に PathItem が追加されていない場合にインスタンスを新規作成し追加します。
	 * </p>
	 * @see #getEntityOidPath(EntityDefinition)
	 * @param openApi OpenAPIオブジェクト
	 * @param entityDefinition EntityDefinition
	 * @return エンティティのOIDを指定するPathItem
	 */
	protected PathItem getEntityOidPathItem(OpenAPI openApi, EntityDefinition entityDefinition) {
		var entityOidPath = getEntityOidPath(entityDefinition);
		return getPathItemIfNotExistCreate(openApi, entityOidPath, pathItem -> {
			pathItem.addParametersItem(new PathParameter().name("oid").required(Boolean.TRUE).schema(new StringSchema()));
		});
	}

	/**
	 * エンティティのOID、バージョンを指定するPathItemを取得します。
	 * <p>
	 * OpenAPI の paths に PathItem が追加されていない場合にインスタンスを新規作成し追加します。
	 * </p>
	 * @see #getEntityOidVersionPath(EntityDefinition)
	 * @param openApi OpenAPIオブジェクト
	 * @param entityDefinition EntityDefinition
	 * @return エンティティのOID、バージョンを指定するPathItem
	 */
	protected PathItem getEntityOidVersionPathItem(OpenAPI openApi, EntityDefinition entityDefinition) {
		var entityOidVersionPath = getEntityOidVersionPath(entityDefinition);
		return getPathItemIfNotExistCreate(openApi, entityOidVersionPath, pathItem -> {
			pathItem.addParametersItem(new PathParameter().name("oid").required(Boolean.TRUE).schema(new StringSchema()));
			pathItem.addParametersItem(new PathParameter().name("version").required(Boolean.TRUE).schema(new StringSchema()));
		});
	}

	/**
	 * OpenAPIのインスタンスから指定されたパスのPathItemインスタンスを取得します。
	 * <p>
	 * OpenAPI の paths に、指定された path が存在していない場合はインスタンスを生成します。
	 * 存在する場合は、存在しているインスタンスを返却します。
	 * <p>
	 * @param openApi OpenAPIオブジェクト
	 * @param path パス
	 * @param additionalSettingsAtCreation PathItemインスタンスの生成時に追加設定を行うConsumer
	 * @return 指定されたパスのPathItemインスタンス
	 */
	protected PathItem getPathItemIfNotExistCreate(OpenAPI openApi, String path, Consumer<PathItem> additionalSettingsAtCreation) {
		var paths = openApi.getPaths();
		if (null == paths) {
			paths = new Paths();
			openApi.setPaths(paths);
		}
		var pathItem = paths.get(path);
		if (null == pathItem) {
			pathItem = new PathItem();
			additionalSettingsAtCreation.accept(pathItem);
			paths.addPathItem(path, pathItem);
		}
		return pathItem;
	}

	/**
	 * HTTPメソッドとリクエストパスから、成功レスポンスの説明を生成します。
	 * @param httpMethod HTTPメソッド（例：GET、POST、PUT、DELETE）
	 * @param requestPath リクエストパス
	 * @return 成功レスポンスの説明
	 */
	protected String getResponseDescription(String httpMethod, String requestPath) {
		return httpMethod + " " + requestPath + " success response.";
	}
}
