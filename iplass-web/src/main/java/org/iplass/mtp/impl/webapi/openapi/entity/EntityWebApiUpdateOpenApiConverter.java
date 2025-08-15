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

import java.util.List;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.webapi.command.entity.UpdateEntityCommand;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiJsonSchemaType;
import org.iplass.mtp.spi.ServiceRegistry;

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
 * EntityWebAPI の UPDATE 操作の OpenAPI変換クラス
 * <p>
 * 以下のパス・メソッドを追加します。
 * </p>
 * <ul>
 * <li>PUT api/mtp/entity/${Entity定義名}/{oid}</li>
 * <li>PUT api/mtp/entity/${Entity定義名}/{oid}/{version}</li>
 * </ul>
 * @author SEKIGUCHI Naoya
 */
public class EntityWebApiUpdateOpenApiConverter extends AbstractEntityWebApiOpenApiConverter {

	@Override
	public EntityWebApiType getEntityWebApiType() {
		return EntityWebApiType.UPDATE;
	}

	@Override
	public void convert(OpenAPI openApi, EntityDefinition entityDefinition) {
		var entityOidDescription = getResponseDescription("PUT", getEntityOidPath(entityDefinition));
		var entityOidOperation = createOperation(openApi, entityDefinition, entityOidDescription);
		getEntityOidPathItem(openApi, entityDefinition).setPut(entityOidOperation);

		var entityOidVersionDescription = getResponseDescription("PUT", getEntityOidVersionPath(entityDefinition));
		var entityOidVersionOperation = createOperation(openApi, entityDefinition, entityOidVersionDescription);
		var targetVersion = new QueryParameter().name(UpdateEntityCommand.PARAM_PUT_TARGET_VERSION)
				.required(Boolean.FALSE)
				.schema(new StringSchema()._enum(List.of("CURRENT_VALID", "SPECIFIC", "NEW")));
		entityOidVersionOperation.addParametersItem(targetVersion);
		getEntityOidVersionPathItem(openApi, entityDefinition).setPut(entityOidVersionOperation);
	}

	private Operation createOperation(OpenAPI openApi, EntityDefinition entityDefinition, String description) {
		var operation = new Operation().responses(new ApiResponses());

		// parameters
		var withValidation = new QueryParameter().name(UpdateEntityCommand.PARAM_PUT_WITH_VALIDATION).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var notifyListeners = new QueryParameter().name(UpdateEntityCommand.PARAM_PUT_NOTIFY_LISTENERS).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var localized = new QueryParameter().name(UpdateEntityCommand.PARAM_PUT_LOCALIZED).required(Boolean.FALSE).schema(new BooleanSchema());
		var forceUpdate = new QueryParameter().name(UpdateEntityCommand.PARAM_PUT_FORCE_UPDATE).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var updateProperties = new QueryParameter().name(UpdateEntityCommand.PARAM_PUT_UPDATE_PROPERTIES).required(Boolean.FALSE)
				.schema(new StringSchema());
		var checkTimestamp = new QueryParameter().name(UpdateEntityCommand.PARAM_PUT_CHECK_TIMESTAMP).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var purgeCompositionedEntity = new QueryParameter().name(UpdateEntityCommand.PARAM_PUT_PURGE_COMPOSITIONED_ENTITY)
				.required(Boolean.FALSE)
				.schema(new BooleanSchema());
		var checkLockedByUser = new QueryParameter().name(UpdateEntityCommand.PARAM_PUT_CHECK_LOCKED_BY_USER).required(Boolean.FALSE)
				.schema(new BooleanSchema());

		operation
		.addParametersItem(withValidation)
		.addParametersItem(notifyListeners)
		.addParametersItem(localized)
		.addParametersItem(forceUpdate)
		.addParametersItem(updateProperties)
		.addParametersItem(checkTimestamp)
		.addParametersItem(purgeCompositionedEntity)
		.addParametersItem(checkLockedByUser);

		var service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
		var componentsSchemaRef = service.getReusableSchemaFactory().addReusableSchema(entityDefinition, openApi, OpenApiJsonSchemaType.JSON);

		// requestBody
		var requestBodySchema = new ObjectSchema().$ref(componentsSchemaRef);
		var requestBodyMediaType = new MediaType().schema(requestBodySchema);
		var requestBodyContent = new Content().addMediaType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON,requestBodyMediaType);
		operation.requestBody(new RequestBody().required(Boolean.TRUE).content(requestBodyContent));

		// responses
		var okSchema = new ObjectSchema()
				.addProperty(RESPONSE_STATUS, new StringSchema())
				.addProperty(UpdateEntityCommand.RESULT_OID, new StringSchema());
		var okMediaType = new MediaType().schema(okSchema);
		var okContent = new Content().addMediaType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON, okMediaType);
		var okResponse = new ApiResponse().content(okContent).description(description);

		operation.getResponses().addApiResponse(STATUS_OK, okResponse);

		return operation;
	}
}
