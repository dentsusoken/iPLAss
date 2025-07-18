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

import org.iplass.mtp.impl.web.token.TokenStore;
import org.iplass.mtp.webapi.definition.WebApiTokenCheck;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(Token Check)
 * @author SEKIGUCHI Naoya
 */
public class WebApiTokenCheckOpenApiOperationConverter implements WebApiOpenApiConverter {
	private static final String HEADER = TokenStore.TOKEN_HEADER_NAME;
	private static final String LOWER_HEADER = HEADER.toLowerCase();

	private static final String QUERY = TokenStore.TOKEN_PARAM_NAME;
	private static final String LOWER_QUERY = QUERY.toLowerCase();

	@Override
	public void convertOpenApi(WebApiOpenApiConvertContext context) {
		var tokenCheck = context.getWebApiDefinition().getTokenCheck();
		if (null == tokenCheck) {
			// Token Checkが設定されていない場合は何もしない
			return;
		}

		var pathItem = context.getPathItem();
		pathItem.addParametersItem(new HeaderParameter().name(HEADER).schema(new StringSchema()));
		pathItem.addParametersItem(new QueryParameter().name(QUERY).schema(new StringSchema()));
	}

	@Override
	public void convertWebApi(WebApiOpenApiConvertContext context) {
		// 初期値設定
		context.getWebApiDefinition().setTokenCheck(null);

		var parmeters = context.getPathItem().getParameters();
		if (null != parmeters) {
			for (Parameter param : parmeters) {
				boolean isHeader = param.getName().toLowerCase().equals(LOWER_HEADER) && param.getIn().equals("header");
				boolean isQuery = param.getName().toLowerCase().equals(LOWER_QUERY) && param.getIn().equals("query");

				if (isHeader || isQuery) {
					var tokenCheck = context.getWebApiDefinition().getTokenCheck();
					if (null == tokenCheck) {
						tokenCheck = new WebApiTokenCheck();
						context.getWebApiDefinition().setTokenCheck(tokenCheck);
						return;
					}
				}
			}
		}
	}

}
