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

import jakarta.ws.rs.core.MediaType;

/**
 * OpenAPIのJSONスキーマのタイプを表す列挙型。
 * <p>
 * 一部のデータ型は content-type によってデータフォーマットが異なる為、送信する content-type に応じてスキーマフォーマットを指定する。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public enum OpenApiJsonSchemaType {
	/**
	 * JSONタイプ
	 * <p>
	 * application/json などが該当します。
	 * </p>
	 */
	JSON,
	/**
	 * XMLタイプ
	 * <p>
	 * application/xml などが該当します。
	 */
	XML,
	/**
	 * フォームタイプ
	 * <p>
	 * application/x-www-form-urlencoded や multipart/form-data などが該当します。
	 * </p>
	 */
	FORM;

	/**
	 * コンテンツタイプからOpenApiJsonSchemaTypeを取得します。
	 * <p>
	 * 想定外のコンテンツタイプの場合は、デフォルト値を返します。
	 * </p>
	 *
	 * @param contentType コンテンツタイプ
	 * @param defaultValue 想定外のコンテンツタイプの場合に返すデフォルト値
	 * @return OpenApiJsonSchemaType
	 */
	public static OpenApiJsonSchemaType fromContentType(String contentType, OpenApiJsonSchemaType defaultValue) {
		var mediaType = MediaType.valueOf(contentType);
		if (mediaType.getType().equals("application") && (mediaType.getSubtype().equals("json") || mediaType.getSubtype().endsWith("+json"))) {
			// application/json もしくは、 application/*+json の場合
			return OpenApiJsonSchemaType.JSON;

		} else if (mediaType.getType().equals("application") && (mediaType.getSubtype().equals("xml") || mediaType.getSubtype().endsWith("+xml"))) {
			// application/xml もしくは、 application/*+xml の場合
			return OpenApiJsonSchemaType.XML;

		} else if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType) || MediaType.MULTIPART_FORM_DATA.equals(contentType)) {
			return OpenApiJsonSchemaType.FORM;
		}

		// 想定外コンテンツタイプは JSON とする
		return OpenApiJsonSchemaType.JSON;
	}

}
