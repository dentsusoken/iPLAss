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

import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConvertContext.OperationContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.ArrayUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.WebApiStubContent;
import org.iplass.mtp.webapi.definition.openapi.OpenApiFileType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.models.examples.Example;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(Stub Contents用)
 * @author SEKIGUCHI Naoya
 */
public class WebApiStubContentsOperationConverter extends AbstractWebApiOpenApiOperationConverter {

	@Override
	protected boolean isMapOpenApiOperationValue(WebApiOpenApiConvertContext context) {
		return ArrayUtil.isNotEmpty(context.getWebApiDefinition().getStubContents());
	}

	@Override
	protected void convertOpenApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		var content = initResponseContent(operation.getOperation());
		for (var stubContent : context.getWebApiDefinition().getStubContents()) {
			var contentMediaType = content.get(stubContent.getContentType());
			var example = new Example();
			example.setValue(stubContent.getContent());
			contentMediaType.addExamples(stubContent.getLabel(), example);
		}
	}

	@Override
	protected void setWebApiDefaultValue(WebApiOpenApiConvertContext context) {
		context.getWebApiDefinition().setStubContents(null);
	}

	@Override
	protected CheckNext convertWebApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		var service = ServiceRegistry.getRegistry(). getService(OpenApiService.class);
		var jsonMapper = service.getOpenApiResolver().getObjectMapper(OpenApiFileType.JSON, context.getVersion());

		var okResponse = getOkResponse(operation.getOperation());
		for (var entry : okResponse.getContent().entrySet()) {
			var contentType = entry.getKey();
			var examples = entry.getValue().getExamples();

			if (null != examples) {
				for (var exampleEntry : examples.entrySet()) {
					var label = exampleEntry.getKey();
					var contentObject = exampleEntry.getValue().getValue();

					var content = getStubContent(contentObject, jsonMapper);

					if (StringUtil.isNotEmpty(content)) {
						var stubContent = new WebApiStubContent();
						stubContent.setContentType(contentType);
						stubContent.setLabel(label);
						stubContent.setContent(content);

						if (!containsStubContent(context.getWebApiDefinition().getStubContents(), stubContent)) {
							// 同一スタブが含まれていない場合は追加する
							var newStubContents = ArrayUtil.add(context.getWebApiDefinition().getStubContents(), stubContent, WebApiStubContent[]::new);
							context.getWebApiDefinition().setStubContents(newStubContents);
						}
					}
				}
			}
		}
		return CheckNext.CONTINUE;
	}

	/**
	 * スタブコンテンツを取得します
	 * @param contentObject OpenAPIの Examples#getValue() の値
	 * @param mapper ObjectMapper
	 * @return スタブコンテンツの文字列表現
	 */
	private String getStubContent(Object contentObject, ObjectMapper mapper) {
		if (null == contentObject) {
			return "";
		}

		if (contentObject instanceof String) {
			return (String) contentObject;
		}

		try {
			// Object形式の値の場合は、JSON文字列に変換する
			return mapper.writeValueAsString(contentObject);

		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * スタブコンテンツが含まれているか確認します。
	 * @param stubContents WebApiStubContentの配列
	 * @param check 確認するWebApiStubContent
	 * @return スタブコンテンツが含まれている場合はtrue、含まれていない場合はfalse
	 */
	private boolean containsStubContent(WebApiStubContent[] stubContents, WebApiStubContent check) {
		if (ArrayUtil.isEmpty(stubContents)) {
			return false;
		}
		for (var content : stubContents) {
			if (StringUtil.equals(check.getContentType(), content.getContentType()) && StringUtil.equals(check.getLabel(), content.getLabel())
					&& StringUtil.equals(check.getContent(), content.getContent())) {
				// 全て同一のスタブコンテンツが含まれている場合
				return true;
			}
		}
		return false;
	}

}
