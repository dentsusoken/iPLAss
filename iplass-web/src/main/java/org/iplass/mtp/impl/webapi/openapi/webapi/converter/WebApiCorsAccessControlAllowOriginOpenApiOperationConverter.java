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
import org.iplass.mtp.util.StringUtil;

import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(CORS Access-Control-Allow-Credentials)
 * @author SEKIGUCHI Naoya
 */
public class WebApiCorsAccessControlAllowOriginOpenApiOperationConverter extends AbstractWebApiOpenApiOperationConverter {
	private static final String HEADER = "Access-Control-Allow-Origin";

	@Override
	protected boolean isMapOpenApiOperationValue(WebApiOpenApiConvertContext context) {
		return StringUtil.isNotEmpty(context.getWebApiDefinition().getAccessControlAllowOrigin());
	}

	@Override
	protected void convertOpenApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		var response = getOkResponse(operation.getOperation());
		response.addHeaderObject(HEADER, new Header().schema(new StringSchema()));
	}

	@Override
	protected void setWebApiDefaultValue(WebApiOpenApiConvertContext context) {
		// 何も設定しない
	}

	@Override
	protected CheckNext convertWebApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		// 何も設定しない
		return CheckNext.FINISH;
	}

}
