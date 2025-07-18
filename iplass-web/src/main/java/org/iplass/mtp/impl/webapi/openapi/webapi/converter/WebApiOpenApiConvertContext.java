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
package org.iplass.mtp.impl.webapi.openapi.webapi.converter;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

/**
 * OpenAPI仕様とWebAPI定義の変換コンテキスト
 * @author SEKIGUCHI Naoya
 */
public class WebApiOpenApiConvertContext {
	/** WebAPI定義 */
	private WebApiDefinition webApiDefinition;
	/** WebAPI定義パス */
	private String webApiPath;

	/** OpenAPI 定義 */
	private OpenAPI openApi;
	/** OpenAPI PathItem が定義されているパス  */
	private String openApiPath;
	/** OpenAPI パスアイテム */
	private PathItem pathItem;

	/** 変換対象メソッド */
	private MethodType targetMethod;

	private Map<MethodType, OperationContext> methodOperationContext = new HashMap<>();

	/**
	 * コンストラクタ
	 * @param openApi OpenAPI定義
	 * @param path OpenAPIパス
	 * @param pathItem OpenAPIパスに対応したパスアイテム
	 */
	public WebApiOpenApiConvertContext(OpenAPI openApi, String path, PathItem pathItem) {
		this(new WebApiDefinition(), null, openApi, path, pathItem);
	}

	/**
	 * コンストラクタ
	 * @param webApiDefinition WebAPI定義
	 * @param webApiPath WebAPI定義名
	 * @param openApi OpenAPI定義
	 * @param openApiPath OpenAPIパス
	 * @param pathItem OpenAPIパスに対応したパスアイテム
	 */
	public WebApiOpenApiConvertContext(WebApiDefinition webApiDefinition, String webApiPath, OpenAPI openApi, String openApiPath, PathItem pathItem) {
		this.webApiDefinition = webApiDefinition;
		this.webApiPath = webApiPath;

		this.openApi = openApi;
		this.openApiPath = openApiPath;
		this.pathItem = pathItem;
	}

	/**
	 * WebAPI定義を取得します。
	 * @return WebAPI定義
	 */
	public WebApiDefinition getWebApiDefinition() {
		return webApiDefinition;
	}

	/**
	 * 変換対象のWebAPI定義パスを取得します。
	 * @return 変換対象のWebAPI定義パス
	 */
	public String getWebApiPath() {
		return webApiPath;
	}

	/**
	 * OpenAPIを取得します。
	 * @return OpenAPI
	 */
	public OpenAPI getOpenApi() {
		return openApi;
	}

	/**
	 * 変換対象の OpenAPI パス文字列を取得します
	 * @return 変換対象の OpenAPI パス文字列
	 */
	public String getOpenApiPath() {
		return openApiPath;
	}

	/**
	 * OpenAPIパスアイテムを取得します。
	 * @return OpenAPIパスアイテム
	 */
	public PathItem getPathItem() {
		return pathItem;
	}

	/**
	 * 変換対象メソッドを設定します
	 * @param targetMethod 変換対象メソッド
	 */
	public void setTargetMethod(MethodType targetMethod) {
		this.targetMethod = targetMethod;
	}

	/**
	 * 変換対象メソッドを取得します
	 * <p>
	 * OpenAPI から WebAPI へ変換する際に、全メソッドを対象とするかメソッド単位とするか決定するための設定値となります。
	 * </p>
	 * <ul>
	 * <li>メソッド設定あり：メソッド単一でWebAPIを設定する。メソッド定義名は path/to/webapi/METHOD_NAME となる</li>
	 * <li>メソッド設定無し：メソッド全体でWebAPIを設定する。メソッド定義名は path/to/webapi となる</li>
	 * </ul>
	 * @return 変換対象メソッド
	 */
	public MethodType getTargetMethod() {
		return targetMethod;
	}

	/**
	 * OperationContextを追加します。
	 * @param operationContext OperationContext
	 */
	public void putOperationContext(OperationContext operationContext) {
		methodOperationContext.put(operationContext.getMethod(), operationContext);
	}

	/**
	 * 指定されたメソッドに対応するOperationContextを取得します。
	 * @param method メソッド
	 * @return OperationContext
	 */
	public OperationContext getOperationContext(MethodType method) {
		return methodOperationContext.get(method);
	}

	/**
	 * OperationContextを保持するクラス
	 */
	public static class OperationContext {
		/** メソッド */
		private final MethodType method;
		/** OpenAPI operation */
		private final Operation operation;

		/**
		 * コンストラクタ
		 * @param method メソッド
		 * @param operation オペレーション
		 */
		public OperationContext(MethodType method, Operation operation) {
			this.method = method;
			this.operation = operation;
		}

		/**
		 * メソッドを取得します
		 * @return メソッド
		 */
		public MethodType getMethod() {
			return method;
		}

		/**
		 * オペレーションを取得します
		 * @return オペレーション
		 */
		public Operation getOperation() {
			return operation;
		}
	}
}
