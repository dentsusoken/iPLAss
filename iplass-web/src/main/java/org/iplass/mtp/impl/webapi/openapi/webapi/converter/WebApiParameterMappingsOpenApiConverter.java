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
import java.util.regex.Pattern;

import org.iplass.mtp.webapi.definition.WebApiParamMapDefinition;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(WebAPIパラメータマッピング)
 * <p>
 * WebAPI -> OpenAPI 変換時には、{@link org.iplass.mtp.impl.webapi.openapi.webapi.WebApiPathParameterDecomposer} にてパスパラメータを解析する必要があるため、本クラスでは何もしません。
 * </p>
 * @see org.iplass.mtp.impl.webapi.openapi.webapi.WebApiPathParameterDecomposer
 */
public class WebApiParameterMappingsOpenApiConverter implements WebApiOpenApiConverter {
	private static final Pattern PATH_PARAMETER_PATTERN = Pattern.compile("^\\{(.*)\\}$");

	@Override
	public void convertOpenApi(WebApiOpenApiConvertContext context) {
		// 解決済みなので何もしない
	}

	@Override
	public void convertWebApi(WebApiOpenApiConvertContext context) {
		var path = context.getOpenApiPath();

		var parameterStartIndex = path.indexOf("{");
		if (0 > parameterStartIndex) {
			// パラメータが存在しない場合は終了
			return;
		}

		var pathExcludeParam = path.substring(1, parameterStartIndex - 1);
		var pathOnlyLen = pathExcludeParam.split("/").length;

		// 先頭の "/" を除き、split する
		var splittedPath = path.substring(1).split("/");

		var paramMapDefList = new ArrayList<WebApiParamMapDefinition>();

		// パスパラメータを取得
		for (int i = 0; i < splittedPath.length; i++) {
			var paramName = splittedPath[i];
			var paramNameMatcher = PATH_PARAMETER_PATTERN.matcher(paramName);
			if (!paramNameMatcher.find()) {
				// パスパラメータではない場合は、スキップ
				continue;
			}

			// パスパラメータの場合

			// 名前を取得
			paramName = paramNameMatcher.group(1);

			WebApiParamMapDefinition def = new WebApiParamMapDefinition();
			def.setName(paramName);
			def.setMapFrom("${" + (i - pathOnlyLen) + "}");

			paramMapDefList.add(def);
		}

		context.getWebApiDefinition().setWebApiParamMap(paramMapDefList.toArray(WebApiParamMapDefinition[]::new));
	}
}
