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
import org.iplass.mtp.impl.webapi.command.entity.DeleteEntityCommand;
import org.iplass.mtp.webapi.openapi.EntityWebApiType;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * EntityWebAPI の DELETE 操作の OpenAPI変換クラス
 * <p>
 * 以下のパス・メソッドを追加します。
 * </p>
 * <ul>
 * <li>DELETE /api/mtp/entity/${Entity定義名}/{oid}</li>
 * </ul>
 * @author SEKIGUCHI Naoya
 */
public class EntityWebApiDeleteOpenApiConverter extends AbstractEntityWebApiOpenApiConverter {

	@Override
	public EntityWebApiType getEntityWebApiType() {
		return EntityWebApiType.DELETE;
	}

	@Override
	public void convert(OpenAPI openApi, EntityDefinition entityDefinition) {
		var operation = new Operation().responses(new ApiResponses());

		// parameters
		var timestamp = new QueryParameter().name(DeleteEntityCommand.PARAM_TIMESTAMP).required(Boolean.FALSE).schema(new IntegerSchema());
		operation.addParametersItem(timestamp);

		// responses
		var description = getResponseDescription("DELETE", getEntityOidPath(entityDefinition));
		var okSchema = new ObjectSchema()
				.addProperty(RESPONSE_STATUS, new StringSchema());
		var okMediaType = new MediaType().schema(okSchema);
		var okContent = new Content().addMediaType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON, okMediaType);
		var okResponse = new ApiResponse().content(okContent).description(description);;
		operation.getResponses().addApiResponse(STATUS_OK, okResponse);

		// OpenAPI に設定
		getEntityOidPathItem(openApi, entityDefinition).setDelete(operation);
	}
}
