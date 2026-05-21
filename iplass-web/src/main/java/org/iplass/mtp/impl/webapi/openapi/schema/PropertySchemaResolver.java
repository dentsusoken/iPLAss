/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.iplass.mtp.util.StringUtil;

import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 * プロパティスキーマ解決機能
 * <p>
 * クラス・プロパティの組み合わせまたはプロパティ名のみで、スキーマ情報を解決する機能を提供します。
 * </p>
 * <h3>スキーマ返却優先度</h3>
 * <ol>
 * <li>クラス・プロパティの組み合わせでスキーマ定義されている場合</li>
 * <li>プロパティ名のみでスキーマ定義されている場合</li>
 * </ol>
 *
 * @author SEKIGUCHI Naoya
 */
public class PropertySchemaResolver {
	/** 何も設定されていない PropertySchemaResolver インスタンス */
	public static final PropertySchemaResolver EMPTY_SETTING = new PropertySchemaResolver();

	/** クラス・プロパティの組み合わせでスキーマを保持するマップ（クラス名 -> プロパティ名 -> スキーマ） */
	private final Map<String, Map<String, Schema<?>>> classPropertySchemaMap;
	/** プロパティ名のみでスキーマを保持するマップ（プロパティ名 -> スキーマ） */
	private final Map<String, Schema<?>> propertySchemaMap;

	/**
	 * コンストラクタ
	 * @param propertySchemas クラス・プロパティスキーマのリスト
	 */
	public PropertySchemaResolver(List<PropertySchema> propertySchemas) {
		Objects.requireNonNull(propertySchemas, "propertySchemas must not be null.");

		classPropertySchemaMap = new HashMap<>();
		propertySchemaMap = new HashMap<>();

		for (var schema : propertySchemas) {
			// スキーマがnullでないことを確認
			if (schema == null) {
				throw new IllegalArgumentException(PropertySchema.class.getSimpleName() + " must not be null.");
			}

			// プロパティ名は必須
			if (StringUtil.isEmpty(schema.getPropertyName())) {
				throw new IllegalArgumentException(
						PropertySchema.class.getSimpleName() + " must have non-empty propertyName. className=" + schema.getClassName());
			}

			// スキーマタイプは必須
			if (schema.getSchemaType() == null) {
				throw new IllegalArgumentException(
						PropertySchema.class.getSimpleName() + " must have non-null schemaType. className=" + schema.getClassName()
								+ ", propertyName=" + schema.getPropertyName());
			}

			// スキーマを作成
			var openApiSchema = createSchema(schema);

			if (StringUtil.isNotEmpty(schema.getClassName())) {
				// クラス名がある場合は、クラス・プロパティの組み合わせでスキーマを登録
				classPropertySchemaMap
						.computeIfAbsent(schema.getClassName(), k -> new HashMap<>())
						.put(schema.getPropertyName(), openApiSchema);
			} else {
				// クラス名がない場合は、プロパティ名のみでスキーマを登録
				propertySchemaMap.put(schema.getPropertyName(), openApiSchema);
			}
		}
	}

	/**
	 * デフォルトコンストラクタ
	 * <p>
	 * クラス・プロパティスキーマが設定されていない状態のインスタンスを作成します。
	 * </p>
	 */
	private PropertySchemaResolver() {
		this(List.of());
	}

	/**
	 * クラス・プロパティの組み合わせまたはプロパティ名のみでスキーマを解決します。
	 * <p>
	 * スキーマが見つからない場合は、空の Optional を返します。
	 * </p>
	 * @param clazz クラス
	 * @param propertyName プロパティ名
	 * @return 解決されたスキーマ
	 */
	public Optional<Schema<?>> resolve(Class<?> clazz, String propertyName) {
		// クラス・プロパティの組み合わせでスキーマを取得
		var classPropertySchema = classPropertySchemaMap
				.getOrDefault(clazz.getName(), Map.of())
				.get(propertyName);
		if (classPropertySchema != null) {
			return Optional.of(classPropertySchema);
		}

		// プロパティ名のみでスキーマを取得
		var propertySchema = propertySchemaMap.get(propertyName);
		if (propertySchema != null) {
			return Optional.of(propertySchema);
		}

		// スキーマが見つからない場合は、空の Optional を返す
		return Optional.empty();
	}

	/**
	 * クラス・プロパティスキーマから OpenAPI の Schema を作成します。
	 * @param propertySchema クラス・プロパティスキーマ
	 * @return OpenAPI の Schema
	 */
	private static Schema<?> createSchema(PropertySchema propertySchema) {
		var openApiSchema = switch (propertySchema.getSchemaType()) {
		case STRING -> new StringSchema();
		case INTEGER -> new IntegerSchema();
		case BOOLEAN -> new BooleanSchema();
		case DATE -> new DateSchema();
		case DATETIME -> new DateTimeSchema();
		};

		applyIfNotEmpty(propertySchema.getDescription(), openApiSchema::setDescription);
		applyIfNotEmpty(propertySchema.getFormat(), openApiSchema::setFormat);

		return openApiSchema;
	}

	/**
	 * 値が空でない場合に、指定されたセッターを呼び出して値を設定します。
	 * @param value 設定する値
	 * @param setter 値を設定するセッター
	 */
	private static void applyIfNotEmpty(String value, Consumer<String> setter) {
		if (StringUtil.isNotEmpty(value)) {
			setter.accept(value);
		}
	}
}
