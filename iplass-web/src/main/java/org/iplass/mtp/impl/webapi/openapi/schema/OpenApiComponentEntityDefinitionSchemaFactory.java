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
package org.iplass.mtp.impl.webapi.openapi.schema;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.util.ClassUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ObjectSchema;

/**
 * OpenAPI の components/schemas に再利用可能なエンティティ定義スキーマを追加するファクトリクラス
 * @author SEKIGUCHI Naoya
 */
public class OpenApiComponentEntityDefinitionSchemaFactory implements OpenApiComponentSchemaFactory<EntityDefinition>, OpenApiComponentTarget {
	/** ログ */
	private Logger logger = LoggerFactory.getLogger(OpenApiComponentEntityDefinitionSchemaFactory.class);
	/** エンティティ定義プロパティのJSONスキーマ解決クラス */
	private EntityPropertyDefinitionJsonSchemaResolver entityPropertyResolver;
	/** クラス定義からスキーマを生成するクラス */
	private OpenApiComponentSchemaFactory<Class<?>> schemaFactory;

	@Override
	public boolean isTarget(Object object) {
		return object instanceof EntityDefinition;
	}

	/**
	 * OpenAPI の components/schemas に再利用可能な ObjectSchema を追加します。
	 * <p>
	 * ObjectSchema はパラメータのエンティティ定義から生成します。
	 * <p>
	 * @param entityDef エンティティ定義
	 * @param openApi OpenAPI オブジェクト
	 * @return ObjectSchema の参照文字列
	 */
	@Override
	public String addReusableSchema(EntityDefinition entityDef, OpenAPI openApi, OpenApiJsonSchemaType schemaType) {
		if (null != entityDef.getMapping() && StringUtil.isNotEmpty(entityDef.getMapping().getMappingModelClass())) {
			// POJOマッピングが設定されている場合は、class から定義を作成
			Class<?> entityClass = ClassUtil.forName(entityDef.getMapping().getMappingModelClass());
			return schemaFactory.addReusableSchema(entityClass, openApi, schemaType);
		}

		var name = schemaType.name() + "_entity_" + entityDef.getName();
		var ref = REUSABLE_SCHEMA_PREFIX + name;

		var components = getOpenApiComponents(openApi);

		if (null != components.getSchemas() && components.getSchemas().containsKey(name)) {
			// すでに定義済みの場合は再利用
			return ref;
		}

		components.addSchemas(name, null);

		var schema = createObjectSchemaFromEntityDefinition(entityDef, openApi, schemaType);

		components.addSchemas(name, schema);

		return ref;
	}

	/**
	 * クラス定義からスキーマを生成するファクトリを設定します。
	 * @param schemaFactory クラス定義からスキーマを生成するファクトリ
	 */
	public void setClassSchemaFactory(OpenApiComponentSchemaFactory<Class<?>> schemaFactory) {
		this.schemaFactory = schemaFactory;
	}

	/**
	 * エンティティ定義プロパティのJSONスキーマ解決クラスを設定します。
	 * @param entityPropertyResolver エンティティ定義プロパティのJSONスキーマ解決クラス
	 */
	public void setEntityPropertyDefinitionJsonSchemaResolver(EntityPropertyDefinitionJsonSchemaResolver entityPropertyResolver) {
		this.entityPropertyResolver = entityPropertyResolver;
	}

	/**
	 * エンティティ定義から ObjectSchema を生成します。
	 * @param entityDefinition エンティティ定義
	 * @param openApi OpenAPI オブジェクト
	 * @return ObjectSchema
	 */
	private ObjectSchema createObjectSchemaFromEntityDefinition(EntityDefinition entityDefinition, OpenAPI openApi, OpenApiJsonSchemaType schemaType) {
		var schema = new ObjectSchema();
		schema.setTitle("Entity Schema " + entityDefinition.getName());
		schema.setDescription(entityDefinition.getDescription());

		// エンティティ定義プロパティリストからスキーマを作成する
		for (var prop : entityDefinition.getPropertyList()) {
			var propSchema = entityPropertyResolver.convertSchema(prop.getType(), prop, openApi, schemaType);
			logger.debug("Property to JsonSchema. class = {}, property = {}, jsonSchema = {}", entityDefinition.getName(), prop.getName(),
					propSchema);
			schema.addProperty(prop.getName(), propSchema);
		}

		return schema;
	}


	/**
	 * OpenAPI の Components を取得します。
	 * <p>
	 * インスタンスが存在しない場合は新規に作成します。
	 * </p>
	 * @param openApi OpenAPI オブジェクト
	 * @return OpenAPI の Components
	 */
	private Components getOpenApiComponents(OpenAPI openApi) {
		var components = openApi.getComponents();
		if (null == components) {
			components = new Components();
			openApi.setComponents(components);
		}

		return components;
	}


}
