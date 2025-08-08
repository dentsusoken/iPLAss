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
package org.iplass.mtp.impl.webapi.openapi.entity;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.webapi.command.entity.CreateEntityCommand;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiJsonSchemaType;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.openapi.EntityWebApiType;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * EntityWebAPI の INSERT 操作の OpenAPI変換クラス
 * <p>
 * 以下のパス・メソッドを追加します。
 * </p>
 * <ul>
 * <li>POST api/mtp/entity/${Entity定義名}</li>
 * </ul>
 * @author SEKIGUCHI Naoya
 */
public class EntityWebApiInsertOpenApiConverter extends AbstractEntityWebApiOpenApiConverter {

	@Override
	public EntityWebApiType getEntityWebApiType() {
		return EntityWebApiType.INSERT;
	}

	@Override
	public void convert(OpenAPI openApi, EntityDefinition entityDefinition) {
		var operation = new Operation().responses(new ApiResponses());

		// parameters
		var withValidation = new QueryParameter().name(CreateEntityCommand.PARAM_POST_WITH_VALIDATION).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var notifyListeners = new QueryParameter().name(CreateEntityCommand.PARAM_POST_NOTIFY_LISTENERS).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var enableAuditPropertySpecification = new QueryParameter().name(CreateEntityCommand.PARAM_POST_ENABLE_AUDIT_PROPERTY_SPECIFICATION)
				.required(Boolean.FALSE).schema(new BooleanSchema());
		var regenerateOid = new QueryParameter().name(CreateEntityCommand.PARAM_POST_REGENERATE_OID).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var regenerateAutoNumber = new QueryParameter().name(CreateEntityCommand.PARAM_POST_REGENERATE_AUTO_NUMBER).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var versionSpecified = new QueryParameter().name(CreateEntityCommand.PARAM_POST_VERSION_SPECIFIED).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var localized = new QueryParameter().name(CreateEntityCommand.PARAM_POST_LOCALIZED).required(Boolean.FALSE)
				.schema(new BooleanSchema());

		operation.addParametersItem(withValidation)
		.addParametersItem(notifyListeners)
		.addParametersItem(enableAuditPropertySpecification)
		.addParametersItem(regenerateOid)
		.addParametersItem(regenerateAutoNumber)
		.addParametersItem(versionSpecified)
		.addParametersItem(localized);

		var service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
		var componentsSchemaRef = service.getReusableSchemaFactory().addReusableSchema(entityDefinition, openApi, OpenApiJsonSchemaType.JSON);

		// requestBody
		var requestBodySchema = new ObjectSchema().$ref(componentsSchemaRef);
		var requestBodyMediaType = new MediaType().schema(requestBodySchema);
		var requestBodyContent = new Content().addMediaType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON, requestBodyMediaType);
		operation.requestBody(new RequestBody().required(Boolean.TRUE).content(requestBodyContent));

		// responses
		var description = getResponseDescription("POST", getEntityPath(entityDefinition));
		var okSchema = new ObjectSchema()
				.addProperty(RESPONSE_STATUS, new StringSchema())
				.addProperty(CreateEntityCommand.RESULT_OID, new StringSchema());
		var okMediaType = new MediaType().schema(okSchema);
		var okContent = new Content().addMediaType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON, okMediaType);
		var okResponse = new ApiResponse().content(okContent).description(description);
		operation.getResponses().addApiResponse(STATUS_OK, okResponse);

		getEntityPathItem(openApi, entityDefinition).setPost(operation);
	}
}
