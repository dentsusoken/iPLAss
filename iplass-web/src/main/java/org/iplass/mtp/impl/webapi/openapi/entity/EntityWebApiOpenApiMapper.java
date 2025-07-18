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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.webapi.openapi.EntityWebApiType;

import io.swagger.v3.oas.models.OpenAPI;

/**
 *
 */
public class EntityWebApiOpenApiMapper {
	private Map<EntityWebApiType, EntityWebApiOpenApiConverter> map = createMap();

	public void map(OpenAPI openApi, String definitionName, List<EntityWebApiType> entityCRUD) {
		EntityDefinition entityDefinition = ManagerLocator.manager(EntityDefinitionManager.class).get(definitionName);
		entityCRUD.forEach(e -> map.get(e).convert(openApi, entityDefinition));
	}


	private Map<EntityWebApiType, EntityWebApiOpenApiConverter> createMap() {
		return Stream.of(
				new EntityWebApiLoadOpenApiConverter(),
				new EntityWebApiQueryOpenApiConverter(),
				new EntityWebApiInsertOpenApiConverter(),
				new EntityWebApiUpdateOpenApiConverter(),
				new EntityWebApiDeleteOpenApiConverter())
				.collect(Collectors.toMap(c -> c.getEntityWebApiType(), c -> c));
	}
}
