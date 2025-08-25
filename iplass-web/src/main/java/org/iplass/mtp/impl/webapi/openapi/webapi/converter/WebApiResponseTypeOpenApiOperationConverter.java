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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConvertContext.OperationContext;
import org.iplass.mtp.util.StringUtil;

import io.swagger.v3.oas.models.media.MediaType;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(Response Type)
 * @author SEKIGUCHI Naoya
 */
public class WebApiResponseTypeOpenApiOperationConverter extends AbstractWebApiOpenApiOperationConverter {
	@Override
	protected boolean isMapOpenApiOperationValue(WebApiOpenApiConvertContext context) {
		// 常に対象とする
		return true;
	}

	@Override
	protected void convertOpenApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		// OpenAPIのOperationにResponseのContent-Typeを設定
		var content = initResponseContent(operation.getOperation());

		if (StringUtil.isEmpty(context.getWebApiDefinition().getResponseType())) {
			// responseType が設定されていない場合
			content.addMediaType(APPLICATION_JSON, new MediaType());
			content.addMediaType(APPLICATION_XML, new MediaType());

		} else {
			// responseType が設定されている場合
			var mediaTypes = context.getWebApiDefinition().getResponseType().split(",");
			for (String mediaType : mediaTypes) {
				var trimed = mediaType.trim();
				if (StringUtil.isNotEmpty(trimed)) {
					content.addMediaType(trimed, new MediaType());
				}
			}
		}
	}

	@Override
	protected void setWebApiDefaultValue(WebApiOpenApiConvertContext context) {
		// 初期値としては何も設定しない
	}

	@Override
	protected CheckNext convertWebApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		// NOTE: Response に指定のある Content-Type を全て設定する。何も無い場合は空となって結果的に application/json, application/xml が許容されることになる。

		// 特定のメソッドだけにしか書かれていないタイプが存在する可能性があるので、全ての operation をチェックする
		var content = getResponseContent(operation.getOperation());
		if (null == content) {
			return CheckNext.CONTINUE;
		}

		if (StringUtil.isEmpty(context.getWebApiDefinition().getResponseType())) {
			// 新規にプロパティセット
			var commaContentType = content.keySet().stream().collect(Collectors.joining(","));
			context.getWebApiDefinition().setResponseType(commaContentType);

		} else {
			// 既存のプロパティとジョイン。重複した登録はしない。
			var beforeResponseTypeSet = Stream.of(context.getWebApiDefinition().getResponseType().split(","))
					.map(String::trim).filter(StringUtil::isNotEmpty).collect(Collectors.toSet());
			var commaContentType = Stream.concat(beforeResponseTypeSet.stream(), content.keySet().stream()).distinct().collect(Collectors.joining(","));
			context.getWebApiDefinition().setResponseType(commaContentType);
		}

		return CheckNext.CONTINUE;
	}

}
