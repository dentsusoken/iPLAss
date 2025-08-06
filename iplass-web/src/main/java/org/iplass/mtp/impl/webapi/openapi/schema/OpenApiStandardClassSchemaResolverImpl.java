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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.SelectValue;

import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 * OpenAPI用 標準クラスのスキーマを解決実装
 * <p>
 * エンティティのプロパティとして設定されうるクラスの解決を目的とします。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public class OpenApiStandardClassSchemaResolverImpl implements OpenApiStandardClassSchemaResolver {
	/** 解決可能なクラスとファンクションマップ */
	private Map<Class<?>, Function<OpenApiJsonSchemaType, Schema<?>>> resolveClassFunctionMap = createMap();

	@Override
	public boolean canResolve(Class<?> clazz) {
		return resolveClassFunctionMap.containsKey(clazz);
	}

	@Override
	public Schema<?> resolve(Class<?> clazz, OpenApiJsonSchemaType schemaType) {
		var fn = resolveClassFunctionMap.get(clazz);

		if (fn != null) {
			return fn.apply(schemaType);
		}

		return null;
	}

	protected Map<Class<?>, Function<OpenApiJsonSchemaType, Schema<?>>> createMap() {
		var map = new HashMap<Class<?>, Function<OpenApiJsonSchemaType, Schema<?>>>();
		// 文字
		map.put(String.class, schemaType -> new StringSchema());
		// bool
		map.put(Boolean.class, schemaType -> new BooleanSchema());
		map.put(boolean.class, schemaType -> new BooleanSchema());
		// 数値
		map.put(Integer.class, schemaType -> new IntegerSchema());
		map.put(int.class, schemaType -> new IntegerSchema());
		map.put(Double.class, schemaType -> new IntegerSchema());
		map.put(double.class, schemaType -> new IntegerSchema());
		map.put(Long.class, schemaType -> new IntegerSchema());
		map.put(long.class, schemaType -> new IntegerSchema());
		map.put(Float.class, schemaType -> new IntegerSchema());
		map.put(float.class, schemaType -> new IntegerSchema());
		map.put(BigDecimal.class, schemaType -> new IntegerSchema());
		// 日付
		map.put(java.sql.Date.class, this::resolveDateSchema);
		map.put(java.sql.Timestamp.class, this::resolveTimestampSchema);
		map.put(java.sql.Time.class, this::resolveTimeSchema);
		// バイナリ
		map.put(BinaryReference.class, schemaType -> new IntegerSchema());
		// Select Value
		map.put(SelectValue.class,
				schemaType -> new ObjectSchema().addProperty("displayName", new StringSchema()).addProperty("value", new StringSchema()));
		return map;
	}

	private Schema<?> resolveDateSchema(OpenApiJsonSchemaType schemaType) {
		// DATE: Content-Type によって異なる。
		return switch (schemaType) {
		// JSON = yyyy-MM-dd
		case JSON -> new DateSchema();
		// XML = yyyy-MM-dd+09:00
		case XML -> new StringSchema().description("Date and time in the format yyyy-MM-dd+09:00.");
		// FORM = yyyyMMdd
		case FORM -> new StringSchema().description("Date and time in the format yyyyMMdd.");
		};
	}

	private Schema<?> resolveTimestampSchema(OpenApiJsonSchemaType schemaType) {
		// DATETIME: Content-Type によって異なる。
		return switch (schemaType) {
		// JSON = Integer(unix timestamp)
		case JSON -> new IntegerSchema().description("Unix timestamp in milliseconds.");
		// XML = yyyy-MM-dd'T'HH:mm:ss.000000000+09:00
		case XML -> new StringSchema().description("ISO 8601 date-time format with timezone offset.");
		// FORM = yyyyMMddHHmmss
		case FORM -> new StringSchema().description("Date and time in the format yyyyMMddHHmmss.");
		};
	}

	private Schema<?> resolveTimeSchema(OpenApiJsonSchemaType schemaType) {
		return switch (schemaType) {
		// JSON = HH:mm:ss
		case JSON -> new StringSchema().format("time");
		// XML = HH:mm:ss+09:00
		case XML -> new StringSchema().description("Time in the format HH:mm:ss+09:00.");
		// FORM = HH:mm:ss
		case FORM -> new StringSchema().description("Time in the format HH:mm:ss.");
		};
	}
}
