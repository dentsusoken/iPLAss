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

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(check X-Requested-Withヘッダ)
 * @author SEKIGUCHI Naoya
 */
public class WebApiAccessPolicyCheckXRequestedWithOpenApiOperationConverter implements WebApiOpenApiConverter {
	private static final String HEADER = "X-Requested-With";
	private static final String LOWER_HEADER = HEADER.toLowerCase();

	@Override
	public void convertOpenApi(WebApiOpenApiConvertContext context) {
		if (!context.getWebApiDefinition().isCheckXRequestedWithHeader()) {
			// X-Requested-Withヘッダにチェックされていない場合は何もしない
			return;
		}

		var pathItem = context.getPathItem();
		pathItem.addParametersItem(new HeaderParameter().name(HEADER).schema(new StringSchema()));
	}

	@Override
	public void convertWebApi(WebApiOpenApiConvertContext context) {
		// 初期値設定
		context.getWebApiDefinition().setCheckXRequestedWithHeader(false);

		var parameters = context.getPathItem().getParameters();
		if (null != parameters) {
			for (var parameter : parameters) {
				if (parameter.getName().toLowerCase().equals(LOWER_HEADER) && parameter.getIn().equals("header")) {
					// 1つでも見つかればチェックオン
					context.getWebApiDefinition().setCheckXRequestedWithHeader(true);
					return;
				}
			}
		}

	}
}
