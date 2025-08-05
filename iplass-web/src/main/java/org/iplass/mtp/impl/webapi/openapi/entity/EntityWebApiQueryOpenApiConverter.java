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
import org.iplass.mtp.impl.webapi.command.entity.GetEntityCommand;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiJsonSchemaType;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.openapi.EntityWebApiType;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * EntityWebAPI の QUERY 操作の OpenAPI変換クラス
 * <p>
 * 以下のパス・メソッドを追加します。
 * </p>
 * <ul>
 * <li>GET api/mtp/entity/${Entity定義名}?filter={where条件}</li>
 * <li>GET api/mtp/entity?query={EQL}</li>
 * </ul>
 * @author SEKIGUCHI Naoya
 */
public class EntityWebApiQueryOpenApiConverter extends AbstractEntityWebApiOpenApiConverter {

	@Override
	public EntityWebApiType getEntityWebApiType() {
		return EntityWebApiType.QUERY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void convert(OpenAPI openApi, EntityDefinition entityDefinition) {
		var queryDescription = getResponseDescription("GET", getEntityPath(entityDefinition));
		var queryOperation = createOperation(openApi, entityDefinition, queryDescription);
		var filter = new QueryParameter().name(GetEntityCommand.PARAM_FILTER).required(Boolean.FALSE).schema(new StringSchema());
		var withMappedByReference = new QueryParameter().name(GetEntityCommand.PARAM_WITH_MAPPED_BY_REFERENCE).required(Boolean.FALSE)
				.schema(new BooleanSchema());
		queryOperation.addParametersItem(filter).addParametersItem(withMappedByReference);
		getEntityPathItem(openApi, entityDefinition).setGet(queryOperation);

		var basePathItem = getBasePathItem(openApi, entityDefinition);
		if (null == basePathItem.getGet()) {
			var baseDescription = getResponseDescription("GET", getBasePath());
			var baseOperation = createOperation(openApi, entityDefinition, baseDescription);
			var query = new QueryParameter().name(GetEntityCommand.PARAM_QUERY).required(Boolean.TRUE).schema(new StringSchema());
			baseOperation.addParametersItem(query);
			basePathItem.setGet(baseOperation);

		} else {
			// 既に定義されている場合は、response list にエンティティのデータ型を追加する
			var okResponse = basePathItem.getGet().getResponses().get(STATUS_OK);
			var okContent = okResponse.getContent();
			var okMediaType = okContent.get(jakarta.ws.rs.core.MediaType.APPLICATION_JSON);
			List<Schema<?>> okSchema = okMediaType.getSchema().getOneOf();

			var listSchema = okSchema.stream().filter(s -> !s.getProperties().containsKey("listHeader")).findFirst().get();
			var listProperty = listSchema.getProperties().get("list");
			var listPropertyItems = listProperty.getItems();
			if ("object".equals(listPropertyItems.getType())) {
				listPropertyItems = new ComposedSchema().addOneOfItem(listPropertyItems);
				listProperty.setItems(listPropertyItems);
			}

			var service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
			var componentsSchemaRef = service.getReusableSchemaFactory().addReusableSchema(entityDefinition, openApi, OpenApiJsonSchemaType.JSON);
			listPropertyItems.addOneOfItem(new ObjectSchema().$ref(componentsSchemaRef));
		}

	}

	private Operation createOperation(OpenAPI openApi, EntityDefinition entityDefinition, String description) {
		var operation = new Operation().responses(new ApiResponses());

		// parameters
		var tabular = new QueryParameter().name(GetEntityCommand.PARAM_TABLE_MODE).required(Boolean.FALSE).schema(new BooleanSchema());
		var countTotal = new QueryParameter().name(GetEntityCommand.PARAM_COUNT_TOTAL).required(Boolean.FALSE).schema(new BooleanSchema());
		var footer = new QueryParameter().name(GetEntityCommand.FOOTER).required(Boolean.FALSE).schema(new BooleanSchema());
		operation
		.addParametersItem(tabular)
		.addParametersItem(countTotal)
		.addParametersItem(footer);

		var service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
		var componentsSchemaRef = service.getReusableSchemaFactory().addReusableSchema(entityDefinition, openApi, OpenApiJsonSchemaType.JSON);

		// responses
		var listSchema = new ObjectSchema()
				.addProperty("status", new StringSchema())
				.addProperty("list", new ArraySchema().items(new ObjectSchema().$ref(componentsSchemaRef)))
				.addProperty("totalCount", new IntegerSchema());

		var tabularSchema = new ObjectSchema()
				.addProperty("status", new StringSchema())
				.addProperty("listHeader", new ArraySchema().items(new StringSchema()))
				.addProperty("list", new ArraySchema().items(new ComposedSchema()
						.addAnyOfItem(new StringSchema())
						.addAnyOfItem(new IntegerSchema())
						// NOTE 値としては null はあり得るが、OpenAPI 仕様として出力されないのでコメント化する
						//.addAnyOfItem(new ArbitrarySchema().type("null"))
						))
				.addProperty("totalCount", new IntegerSchema());
		var okMediaType = new MediaType().schema(new ComposedSchema().addOneOfItem(listSchema).addOneOfItem(tabularSchema));
		var okContent = new Content().addMediaType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON, okMediaType);
		var okResponse = new ApiResponse().content(okContent).description(description);

		operation.getResponses().addApiResponse(STATUS_OK, okResponse);

		return operation;

	}

}
