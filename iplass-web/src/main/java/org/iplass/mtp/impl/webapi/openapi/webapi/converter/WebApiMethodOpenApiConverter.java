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

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.Response;

import org.iplass.mtp.impl.webapi.openapi.util.OasUtil;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConvertContext.OperationContext;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * OpenAPI仕様とWebAPI定義の変換(WebAPI名とMethod単位)
 * <p>
 * 本クラスはマッピング処理の最初に実行されることを想定しています。
 * 以下の操作を実行します。
 * </p>
 * <ul>
 * <li>PathItem のメソッドに対応する Operation の生成</li>
 * <li>Operationのデフォルトレスポンスの生成</li>
 * </ul>
 *
 * @author SEKIGUCHI Naoya
 */
public class WebApiMethodOpenApiConverter implements WebApiOpenApiConverter {
	/** HTTP Status OK 文字列 */
	protected static final String STATUS_OK = String.valueOf(Response.Status.OK.getStatusCode());

	private Logger logger = LoggerFactory.getLogger(WebApiMethodOpenApiConverter.class);

	@Override
	public int getOrder() {
		return Order.INIT;
	}

	@Override
	public void convertOpenApi(WebApiOpenApiConvertContext context) {
		var def = context.getWebApiDefinition();
		var pathItem = context.getPathItem();

		var acceptMethods = def.getMethods() == null || 0 == def.getMethods().length ? MethodType.values() : def.getMethods();
		for (var m : acceptMethods) {
			var operation = new Operation();
			operation.setOperationId(getOperationId(def, m));
			// デフォルトの成功レスポンスを設定
			operation.setResponses(new ApiResponses().addApiResponse(STATUS_OK, new ApiResponse()));

			switch (m) {
			case GET:
				pathItem.setGet(operation);
				context.putOperationContext(new OperationContext(MethodType.GET, operation));
				break;
			case POST:
				pathItem.setPost(operation);
				context.putOperationContext(new OperationContext(MethodType.POST, operation));
				break;
			case PUT:
				pathItem.setPut(operation);
				context.putOperationContext(new OperationContext(MethodType.PUT, operation));
				break;
			case DELETE:
				pathItem.setDelete(operation);
				context.putOperationContext(new OperationContext(MethodType.DELETE, operation));
				break;
			case PATCH:
				pathItem.setPatch(operation);
				context.putOperationContext(new OperationContext(MethodType.PATCH, operation));
				break;
			}
		}

	}

	@Override
	public void convertWebApi(WebApiOpenApiConvertContext context) {
		if (null != context.getTargetMethod()) {
			// メソッドが１パターンに指定されている場合は、当該メソッドのみを対象とする。
			context.getWebApiDefinition().setMethods(new MethodType[] { context.getTargetMethod() });
			var opertaion = switch (context.getTargetMethod()) {
			case GET -> context.getPathItem().getGet();
			case POST -> context.getPathItem().getPost();
			case PUT -> context.getPathItem().getPut();
			case DELETE -> context.getPathItem().getDelete();
			case PATCH -> context.getPathItem().getPatch();
			};
			context.putOperationContext(new OperationContext(context.getTargetMethod(), opertaion));

		} else {
			// 全メソッドを対象としている場合は、全てのメソッドをチェックする
			List<MethodType> methodList = new ArrayList<>();
			var pathItem = context.getPathItem();

			for (var entry : pathItem.readOperationsMap().entrySet()) {
				var methodType = OasUtil.toMethodType(entry.getKey());
				if (null == methodType) {
					// 対応していないメソッドの場合はスキップする
					logger.warn("OpenAPI path:{}, method:{}. method is not supported for WebAPI definition. Skipping.", context.getWebApiPath(),
							entry.getKey());
					continue;
				}

				context.putOperationContext(new OperationContext(methodType, entry.getValue()));
				methodList.add(methodType);
			}
			context.getWebApiDefinition().setMethods(methodList.toArray(MethodType[]::new));
		}
	}

	private String getOperationId(WebApiDefinition def, MethodType method) {
		return OasUtil.toCamelCase(def.getName() + "/" + method.name(), "/");
	}
}
