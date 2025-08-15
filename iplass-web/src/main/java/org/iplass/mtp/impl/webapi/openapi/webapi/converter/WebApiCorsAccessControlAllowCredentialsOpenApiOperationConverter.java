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

import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.BooleanSchema;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(CORS Access-Control-Allow-Origin)
 * @author SEKIGUCHI Naoya
 */
public class WebApiCorsAccessControlAllowCredentialsOpenApiOperationConverter extends AbstractWebApiOpenApiOperationConverter {
	private static final String HEADER = "Access-Control-Allow-Credentials";
	private static final String LOWER_HEADER = HEADER.toLowerCase();

	@Override
	protected boolean isMapOpenApiOperationValue(WebApiOpenApiConvertContext context) {
		return context.getWebApiDefinition().isAccessControlAllowCredentials();
	}

	@Override
	protected void convertOpenApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		var response = getOkResponse(operation.getOperation());
		response.addHeaderObject(HEADER, new Header().schema(new BooleanSchema()));
	}

	@Override
	protected void setWebApiDefaultValue(WebApiOpenApiConvertContext context) {
		context.getWebApiDefinition().setAccessControlAllowCredentials(false);
	}

	@Override
	protected CheckNext convertWebApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		var response = getOkResponse(operation.getOperation());
		if (null != response.getHeaders()) {
			for (var key : response.getHeaders().keySet()) {
				if (LOWER_HEADER.equals(key.toLowerCase())) {
					// Access-Control-Allow-Credentials ヘッダが存在する場合、Access-Control-Allow-Credentialsはtrueにする。
					context.getWebApiDefinition().setAccessControlAllowCredentials(true);
					return CheckNext.FINISH;
				}
			}
		}
		return CheckNext.CONTINUE;
	}
}
