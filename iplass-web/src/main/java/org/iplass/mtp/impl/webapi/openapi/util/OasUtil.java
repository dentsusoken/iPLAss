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
package org.iplass.mtp.impl.webapi.openapi.util;

import org.iplass.mtp.webapi.definition.MethodType;

import io.swagger.v3.oas.models.PathItem.HttpMethod;

// FIXME とりあえずまとめたユーティリティクラス。後で適切に分離する。
/**
 * OpenAPIのユーティリティクラスです。
 */
public class OasUtil {
	/**
	 * 文字列をキャメルケースに変換します。
	 * <p>
	 * 指定された次の文字列を大文字に変換して連結します。
	 * </p>
	 * <h3>例</h3>
	 * <pre>
	 * toCamelCase("hello_world", "_") = "helloWorld"
	 * </pre>
	 * @param s 変換する文字列
	 * @param delimiter 区切り文字
	 * @return キャメルケースに変換された文字列
	 */
	public static String toCamelCase(String s, String delimiter) {
		var arr = s.split(delimiter);
		var result = new StringBuilder(arr[0]);
		for (int idx = 1; idx < arr.length; idx++) {
			// 区切り文字の先頭を大文字にして、残りを小文字にする
			result.append(arr[idx].substring(0, 1).toUpperCase());
			result.append(arr[idx].substring(1).toLowerCase());
		}

		return result.toString();
	}

	public static MethodType toMethodType(HttpMethod method) {
		return switch (method) {
		case GET -> MethodType.GET;
		case PUT -> MethodType.PUT;
		case POST -> MethodType.POST;
		case DELETE -> MethodType.DELETE;
		case PATCH -> MethodType.PATCH;
		default -> null;
		};
	}

	public static HttpMethod toHttpMethod(MethodType method) {
		return switch (method) {
		case GET -> HttpMethod.GET;
		case PUT -> HttpMethod.PUT;
		case POST -> HttpMethod.POST;
		case DELETE -> HttpMethod.DELETE;
		case PATCH -> HttpMethod.PATCH;
		};
	}

//	public static <T, U> void ifNotNull(T nullCheckObject, U param, BiConsumer<T, U> action) {
//		if (nullCheckObject != null) {
//			action.accept(nullCheckObject, param);
//		}
//	}
//
//	public static <T, U, R> R ifNotNull(T nullCheckObject, U param, BiFunction<T, U, R> action) {
//		if (nullCheckObject != null) {
//			return action.apply(nullCheckObject, param);
//		}
//		return null;
//	}
//
}
