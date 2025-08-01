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

import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConvertContext.OperationContext;
import org.iplass.mtp.webapi.definition.MethodType;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;

/**
 * OpenAPI仕様とWebAPI定義の変換を Operation 単位に行う抽象クラス
 * @author SEKIGUCHI Naoya
 */
public abstract class AbstractWebApiOpenApiOperationConverter implements WebApiOpenApiConverter {
	// NOTE: REST_JSON のみ実装している。そのほかのタイプは未実装。
	/** {@value jakarta.ws.rs.core.MediaType#APPLICATION_FORM_URLENCODED} */
	static final String APPLICATION_FORM_URLENCODED = jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
	/** {@value jakarta.ws.rs.core.MediaType#MULTIPART_FORM_DATA} */
	static final String MULTIPART_FORM_DATA = jakarta.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
	/** {@value jakarta.ws.rs.core.MediaType#APPLICATION_XML} */
	static final String APPLICATION_XML = jakarta.ws.rs.core.MediaType.APPLICATION_XML;
	/** {@value jakarta.ws.rs.core.MediaType#APPLICATION_JSON} */
	static final String APPLICATION_JSON = jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

	/**
	 * Web次のOperation検査フラグ
	 * <p>
	 * APIへのマッピング時に利用します。
	 * </p>
	 */
	protected enum CheckNext {
		/** 次のOperationを検査します*/
		CONTINUE,
		/** 次のOperationを検査せず終了します */
		FINISH;
	}

	@Override
	public void convertOpenApi(WebApiOpenApiConvertContext context) {
		if (isMapOpenApiOperationValue(context)) {
			for (var method : MethodType.METHOD_ALL) {
				var operationContext = context.getOperationContext(method);
				if (null != operationContext) {
					convertOpenApiOperation(operationContext, context);
				}
			}
		}
	}

	@Override
	public void convertWebApi(WebApiOpenApiConvertContext context) {
		// デフォルト値を設定
		setWebApiDefaultValue(context);
		for (var method : MethodType.METHOD_ALL) {
			var operationContext = context.getOperationContext(method);
			var next = null != operationContext ? convertWebApiOperation(operationContext, context) : CheckNext.CONTINUE;
			if (CheckNext.FINISH == next) {
				// 次のOperationを検査せず終了する場合
				return;
			}
		}
	}

	/**
	 * OpenAPI Operationの値をマッピングするかどうかを判定します。
	 * @param context コンテキスト
	 * @return OpenAPI Operationの値をマッピングする場合はtrue、しない場合はfalse
	 */
	protected abstract boolean isMapOpenApiOperationValue(WebApiOpenApiConvertContext context);

	/**
	 * WebAPI定義の内容をOpenAPI Operationの値をマッピングします。
	 * @param operation OpenAPIのOperation
	 * @param context コンテキスト
	 */
	protected abstract void convertOpenApiOperation(OperationContext operation, WebApiOpenApiConvertContext context);

	/**
	 * WebAPI定義のデフォルト値を設定します。
	 * @param context コンテキスト
	 */
	protected abstract void setWebApiDefaultValue(WebApiOpenApiConvertContext context);

	/**
	 * OpenAPI OperationをWebAPI定義にマッピングします。
	 * <ul>
	 * <li>返却値として CheckNext.CONTINUE の場合、次のOperationを検査します。</li>
	 * <li>返却値として CheckNext.FINISH の場合、次のオペレーションを検査しません。</li>
	 * </ul>
	 * @param operation OpenAPIのOperation
	 * @param context コンテキスト
	 * @return 次のOperationを検査するかどうかを示すフラグ
	 */
	protected abstract CheckNext convertWebApiOperation(OperationContext operation, WebApiOpenApiConvertContext context);

	/**
	 * OpenAPI OperationのリクエストボディのContentを初期化します。
	 * <p>
	 * operation.requestBody および、operation.requestBody.content が null の場合、インスタンスを設定します。
	 * </p>
	 * @param operation Operationインスタンス
	 * @return リクエストボディのContent
	 */
	protected Content initRequestBodyContent(Operation operation) {
		if (null == operation.getRequestBody()) {
			operation.setRequestBody(new RequestBody());
		}
		if (null == operation.getRequestBody().getContent()) {
			operation.getRequestBody().setContent(new Content());
		}

		return operation.getRequestBody().getContent();
	}

	/**
	 * OpenAPI OperationのリクエストボディのContentを取得します。
	 * <p>
	 * operation.requestBody および、operation.requestBody.content が null の場合は null を返します。
	 * </p>
	 * @param operation Operationインスタンス
	 * @return リクエストボディのContent
	 */
	protected Content getRequestBodyContent(Operation operation) {
		if (null == operation.getRequestBody()) {
			return null;
		}

		if (null == operation.getRequestBody().getContent()) {
			return null;
		}

		return operation.getRequestBody().getContent();
	}

	/**
	 * OpenAPI Operationの "200" レスポンスを取得します。
	 * @param operation Operationインスタンス
	 * @return "200" の ApiResponse インスタンス
	 */
	protected ApiResponse getOkResponse(Operation operation) {
		return operation.getResponses().get("200");
	}

	/**
	 * OpenAPI Operationの "200" レスポンスのContentを初期化します。
	 * <p>
	 * operation.responses."200".content が null の場合、インスタンスを設定します。
	 * </p>
	 * @param operation Operationインスタンス
	 * @return "200" レスポンスのContent
	 */
	protected Content initResponseContent(Operation operation) {
		var response = getOkResponse(operation);

		if (null == response.getContent()) {
			response.setContent(new Content());
		}

		return response.getContent();
	}

	/**
	 * OpenAPI Operationの "200" レスポンスのContentを取得します。
	 * <p>
	 * operation.responses."200".content が null の場合、null を返却します。
	 * </p>
	 *
	 * @param operation Operationインスタンス
	 * @return "200" レスポンスのContent
	 */
	protected Content getResponseContent(Operation operation) {
		var response = getOkResponse(operation);

		if (null == response.getContent()) {
			return null;
		}

		return response.getContent();
	}

}
