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

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * JSON Schema に変換するデータを確認し、適切な SchemaFactory へ割り当てを行うクラス。
 * <p>
 * 本クラスに設定する factory インスタンスは org.iplass.mtp.impl.webapi.openapi.schema.OpenApiComponentTarget を実装したものに限ります。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class OpenApiComponentReusableSchemaFactoryAssigner implements OpenApiComponentReusableSchemaFactory<Object> {
	/** OpenAPIスキーマ生成クラスリスト */
	@SuppressWarnings("rawtypes")
	private List<OpenApiComponentReusableSchemaFactory> factoryList = createFactoryList();

	@SuppressWarnings("unchecked")
	@Override
	public String addReusableSchema(Object object, OpenAPI openApi, OpenApiJsonSchemaType schemaType) {
		if (object == null || openApi == null) {
			throw new IllegalArgumentException("The schema conversion target or OpenAPI is null.");
		}
		for (var factory : factoryList) {
			if (factory instanceof OpenApiComponentTarget targetFactory && targetFactory.isTarget(object)) {
				return factory.addReusableSchema(object, openApi, schemaType);
			}
		}

		throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
	}

	/**
	 * OpenAPI の components/schemas に再利用可能なスキーマを追加するファクトリクラスを作成します。スキーマファクトリを生成します。
	 * <p>
	 * ファクトリクラスは、OpenApiComponentTarget を実装している必要があります。
	 * </p>
	 * @return ファクトリクラスのリスト
	 */
	@SuppressWarnings("rawtypes")
	protected List<OpenApiComponentReusableSchemaFactory> createFactoryList() {
		var schemaGenerator = createClassSchemaGenerator();
		var classFactory = new OpenApiComponentClassReusableSchemaFactory();
		classFactory.setClassSchemaGenerator(schemaGenerator);

		var entityPropertyJsonSchemaResolver = createEntityPropertyDefinitionJsonSchemaResolver();
		var entityDefinitionFactory = new OpenApiComponentEntityDefinitionReusableSchemaFactory();
		entityDefinitionFactory.setClassSchemaFactory(classFactory);
		entityDefinitionFactory.setEntityPropertyDefinitionJsonSchemaResolver(entityPropertyJsonSchemaResolver);

		// OpenApiSchemaFactory を設定する。設定するインスタンスは、EntityDefinition のスキーマを解決することができるインスタンス
		entityPropertyJsonSchemaResolver.setOpenApiSchemaFactory(entityDefinitionFactory);

		return List.of(entityDefinitionFactory, classFactory);
	}

	/**
	 * クラススキーマ生成機能を作成します。
	 * <p>
	 * デフォルトインスタンスとして、 VictoolsClassSchemaGenerator を生成します。
	 * </p>
	 * @return クラススキーマ生成機能
	 */
	protected ClassSchemaGenerator createClassSchemaGenerator() {
		return new VictoolsClassSchemaGenerator();
	}

	/**
	 * エンティティプロパティ定義のJSONスキーマ解決クラスを作成します。
	 * @return エンティティプロパティ定義のJSONスキーマ解決クラス
	 */
	protected EntityPropertyDefinitionJsonSchemaResolver createEntityPropertyDefinitionJsonSchemaResolver() {
		return new EntityPropertyDefinitionJsonSchemaResolver();
	}
}
