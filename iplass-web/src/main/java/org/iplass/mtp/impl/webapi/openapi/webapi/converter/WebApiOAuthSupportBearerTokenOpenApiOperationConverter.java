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

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(OAuth Support Bearer Token)
 * <p>
 * NOTE: OpenAPI から WebAPI に変換する際には、http タイプの bearer スキームのセキュリティスキームが設定されている場合に、チェックオンとします。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class WebApiOAuthSupportBearerTokenOpenApiOperationConverter extends AbstractWebApiOpenApiOperationConverter {
	private static final String SECURITY_SCHEME_NAME = "Bearer";
	private static final String LOWER_SECURITY_SCHEME_NAME = SECURITY_SCHEME_NAME.toLowerCase();

	@Override
	protected boolean isMapOpenApiOperationValue(WebApiOpenApiConvertContext context) {
		return context.getWebApiDefinition().isSupportBearerToken();
	}

	@Override
	protected void convertOpenApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		var api = context.getOpenApi();

		if (null == api.getComponents()) {
			// /componentsが未定義の場合、componentsを定義する。
			api.setComponents(new Components());
		}

		if (null == api.getComponents().getSecuritySchemes() || null == api.getComponents().getSecuritySchemes().get(SECURITY_SCHEME_NAME)) {
			// /components/securitySchemas/Bearerが未定義の場合、Bearerトークンのセキュリティスキーマを定義する。
			api.getComponents().addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme().type(Type.HTTP).scheme(LOWER_SECURITY_SCHEME_NAME));
		}

		operation.getOperation().addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
	}

	@Override
	protected void setWebApiDefaultValue(WebApiOpenApiConvertContext context) {
		// デフォルト値の設定
		context.getWebApiDefinition().setSupportBearerToken(false);
	}

	@Override
	protected CheckNext convertWebApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		var securityList = operation.getOperation().getSecurity();
		if (null != securityList && !securityList.isEmpty()) {
			for (var securityRequirement : securityList) {
				for (var securityKey : securityRequirement.keySet()) {
					var securityScheme = getComponentsSecurityScheme(context.getOpenApi(), securityKey);
					if (null != securityScheme && SecurityScheme.Type.HTTP == securityScheme.getType() && "bearer".equals(securityScheme.getScheme())) {
						// SecuritySchema が、HTTPタイプ、bearer スキーマであればチェックオンにする
						context.getWebApiDefinition().setSupportBearerToken(true);
						return CheckNext.FINISH;
					}
				}
			}
		}

		return CheckNext.CONTINUE;
	}

	private SecurityScheme getComponentsSecurityScheme(OpenAPI openApi, String securityKey) {
		if (null == openApi.getComponents() || null == openApi.getComponents().getSecuritySchemes()) {
			return null;
		}
		return openApi.getComponents().getSecuritySchemes().get(securityKey);
	}
}
