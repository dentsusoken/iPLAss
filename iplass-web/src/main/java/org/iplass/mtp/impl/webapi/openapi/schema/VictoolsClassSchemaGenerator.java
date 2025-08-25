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

import java.util.UUID;

import com.github.victools.jsonschema.generator.MethodScope;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerationContext;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.generator.impl.DefinitionKey;
import com.github.victools.jsonschema.generator.naming.DefaultSchemaDefinitionNamingStrategy;
import com.github.victools.jsonschema.module.jackson.JacksonModule;

/**
 * victools/jsonschema-generator を利用したJSONスキーマ生成クラス
 *
 * @see <a href="https://victools.github.io/jsonschema-generator/">https://victools.github.io/jsonschema-generator/</a>
 * @author SEKIGUCHI Naoya
 */
public class VictoolsClassSchemaGenerator implements ClassSchemaGenerator {
	/** victools/jsonschema-generator インスタンス */
	private SchemaGenerator schemaGenerator;

	/**
	 * コンストラクタ
	 */
	public VictoolsClassSchemaGenerator() {
		JacksonModule module = new JacksonModule();
		var configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
				.with(module)
				.with(
						// 非静的・非void・getterメソッドをスキーマに含める
						Option.NONSTATIC_NONVOID_NONGETTER_METHODS,
						// 引数なし getter メソッドをフィールドとして定義
						Option.FIELDS_DERIVED_FROM_ARGUMENTFREE_METHODS);
		// メソッド名が get, is で始まらないもしくは、引数が存在するメソッドを除外する
		configBuilder.forMethods().withIgnoreCheck(this::shouldIgnoreMethod);
		configBuilder.forTypesInGeneral().withDefinitionNamingStrategy(new UniqueSchemaDefinitionNamingStrategy());
		schemaGenerator = new SchemaGenerator(configBuilder.build());

	}

	@Override
	public String generate(Class<?> clazz) {
		var jsonObject = schemaGenerator.generateSchema(clazz);
		return jsonObject.toString();
	}

	/**
	 * メソッドがスキーマ生成の対象外かどうかを判定する。
	 * <p>
	 * メソッド名が get, is で始まらないもしくは、引数が存在するメソッドを除外する
	 * </p>
	 * @param method メソッドスコープ
	 * @return 対象外の場合は true、それ以外は false
	 */
	protected boolean shouldIgnoreMethod(MethodScope method) {
		return !(method.getDeclaredName().startsWith("get") || method.getDeclaredName().startsWith("is")) || method.getArgumentCount() > 0;
	}

	/**
	 * $defs の定義名が必ずユニークになるように設定するための
	 * <p>
	 * NOTE: この定義で名前を設定しないと、クラス内で自クラスをメンバー変数として定義しているクラスを解析する際にエラーになってしまう。
	 * </p>
	 * <h3>エラーになるパターン例</h3>
	 * <pre>
	 * public class ExampleClass {
	 *   private ExampleClass exampleField;
	 * }
	 * </pre>
	 */
	private static class UniqueSchemaDefinitionNamingStrategy extends DefaultSchemaDefinitionNamingStrategy {
		@Override
		public String getDefinitionNameForKey(DefinitionKey key, SchemaGenerationContext context) {
			// 名前が必ずユニークになるように、末尾にUUIDを付与
			var name = super.getDefinitionNameForKey(key, context);
			name = name + "_" + UUID.randomUUID().toString().replaceAll("\\-", "");
			return name;
		}
	}
}
