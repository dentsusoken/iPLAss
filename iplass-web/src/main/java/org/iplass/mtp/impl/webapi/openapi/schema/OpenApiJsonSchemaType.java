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
}
