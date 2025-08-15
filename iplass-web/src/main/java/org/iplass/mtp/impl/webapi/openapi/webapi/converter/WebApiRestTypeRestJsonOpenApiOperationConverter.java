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
import java.util.stream.Stream;

import org.iplass.mtp.impl.util.ClassUtil;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiJsonSchemaType;
import org.iplass.mtp.impl.webapi.openapi.webapi.converter.WebApiOpenApiConvertContext.OperationContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.ArrayUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.RequestType;

import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;

/**
 * OpenAPI仕様とWebAPI定義の変換クラス(Request Type REST JSON)
 * <p>
 * WebAPI から OpenAPI への変換時に REST JSON を指定した場合は、 REST JSON Parameter も設定可能となる。REST JSON Parameter も考慮する。
 * OpenAPI から WebAPI への変換時は、 requestBody/content に設定されている content-type のみ判断する。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class WebApiRestTypeRestJsonOpenApiOperationConverter extends AbstractWebApiRestTypeOpenApiOperationConverter {
	@Override
	protected RequestType getTargetRequestType() {
		return RequestType.REST_JSON;
	}

	@Override
	protected void convertOpenApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		if (!isBodySupportMethod(operation.getMethod())) {
			// POST, PUT, PATCH以外のメソッドは何もしない
			return;
		}

		var def = context.getWebApiDefinition();
		initRequestBodyContent(operation.getOperation());
		if (ArrayUtil.isEmpty(def.getRestJsonAcceptableContentTypes())) {
			// acceptableContentTypes が設定されていない場合
			createMediaType(operation, context, APPLICATION_JSON);

		} else {
			// acceptableContentTypes が設定されている場合、OpenAPIのOperationにRestJsonのContent-Typeを設定
			for (String contentType : def.getRestJsonAcceptableContentTypes()) {
				createMediaType(operation, context, contentType.trim());
			}
		}
	}

	@Override
	protected void setWebApiDefaultValue(WebApiOpenApiConvertContext context) {
		var accepts = context.getWebApiDefinition().getAccepts();
		if (ArrayUtil.contains(accepts, RequestType.REST_JSON)) {
			// accepts に REST_JSON が含まれている場合は削除する
			var list = new ArrayList<RequestType>(accepts.length - 1);
			Stream.of(accepts).filter(a -> RequestType.REST_JSON != a).forEach(a -> list.add(a));
			context.getWebApiDefinition().setAccepts(list.toArray(RequestType[]::new));
		}
	}

	@Override
	protected CheckNext convertWebApiOperation(OperationContext operation, WebApiOpenApiConvertContext context) {
		if (!isBodySupportMethod(operation.getMethod())) {
			// POST, PUT, PATCH以外のメソッドは何もしない
			return CheckNext.CONTINUE;
		}

		var content = getRequestBodyContent(operation.getOperation());

		if (null == content || content.keySet().stream().noneMatch(k -> null != k && APPLICATION_JSON.equals(k.toLowerCase()))) {
			return CheckNext.CONTINUE;
		}

		// accepts に REST_JSON を追加する
		var accepts = context.getWebApiDefinition().getAccepts();
		var appended = ArrayUtil.add(accepts, RequestType.REST_JSON, RequestType[]::new);
		context.getWebApiDefinition().setAccepts(appended);
		return CheckNext.FINISH;
	}

	private void createMediaType(OperationContext operation, WebApiOpenApiConvertContext context, String contentType) {
		var def = context.getWebApiDefinition();
		var objectSchema = new ObjectSchema();
		operation.getOperation().getRequestBody().getContent().addMediaType(contentType, new MediaType().schema(objectSchema));

		if (StringUtil.isNotEmpty(def.getRestJsonParameterType())) {
			var clazz = ClassUtil.forName(def.getRestJsonParameterType());
			var service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
			var ref = service.getReusableSchemaFactory().addReusableSchema(clazz, context.getOpenApi(), OpenApiJsonSchemaType.JSON);
			objectSchema.set$ref(ref);
		}

	}
}
