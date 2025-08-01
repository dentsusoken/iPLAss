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
import org.iplass.mtp.impl.webapi.command.entity.GetEntityCommand;
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
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * EntityWebAPI の LOAD 操作の OpenAPI 変換クラス
 * <p>
 * 以下のパス・メソッドを追加します。
 * </p>
 * <ul>
 * <li>GET api/mtp/entity/${Entity定義名}/{oid}</li>
 * <li>GET api/mtp/entity/${Entity定義名}/{oid}/{version}</li>
 * </ul>
 * @author SEKIGUCHI Naoya
 */
public class EntityWebApiLoadOpenApiConverter extends AbstractEntityWebApiOpenApiConverter {

	@Override
	public EntityWebApiType getEntityWebApiType() {
		return EntityWebApiType.LOAD;
	}

	@Override
	public void convert(OpenAPI openApi, EntityDefinition entityDefinition) {
		var loadOidDescription = getResponseDescription("GET", getEntityOidPath(entityDefinition));
		var loadOidOperation = createOperation(openApi, entityDefinition, loadOidDescription);
		getEntityOidPathItem(openApi, entityDefinition).setGet(loadOidOperation);

		var loadOidVersionDescription = getResponseDescription("GET", getEntityOidVersionPath(entityDefinition));
		var loadOidVersionOperation = createOperation(openApi, entityDefinition, loadOidVersionDescription);
		getEntityOidVersionPathItem(openApi, entityDefinition).setGet(loadOidVersionOperation);
	}

	private Operation createOperation(OpenAPI openApi, EntityDefinition entityDefinition, String description) {
		var operation = new Operation().responses(new ApiResponses());

		// parameters
		var withMappedByReference = new QueryParameter().name(GetEntityCommand.PARAM_WITH_MAPPED_BY_REFERENCE).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		operation.addParametersItem(withMappedByReference);

		var service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
		var componentsSchemaRef = service.getSchemaFactory().addReusableSchema(entityDefinition, openApi, OpenApiJsonSchemaType.JSON);

		// responses
		var okSchema = new ObjectSchema()
				.addProperty(RESPONSE_STATUS, new StringSchema())
				.addProperty(GetEntityCommand.RESULT_ENTITY, new ObjectSchema().$ref(componentsSchemaRef));
		var okMediaType = new MediaType().schema(okSchema);
		var okContent = new Content().addMediaType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON, okMediaType);
		var okResponse = new ApiResponse().content(okContent).description(description);
		operation.getResponses().addApiResponse(STATUS_OK, okResponse);

		return operation;
	}

}
