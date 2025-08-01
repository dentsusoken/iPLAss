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
package org.iplass.mtp.impl.webapi.openapi;

import org.iplass.mtp.webapi.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.openapi.OpenApiVersion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.SpecVersion;

/**
 * OpenAPIのバージョンやファイルタイプに応じて、ObjectMapperやObjectWriterを取得するクラス。
 * @author SEKIGUCHI Naoya
 */
public class OpenApiResolver {
	/**
	 * OpenAPIの仕様バージョンを取得します。
	 * @param version OpenApiVersion
	 * @return SpecVersion
	 */
	public SpecVersion getOpenApiSpecVersion(OpenApiVersion version) {
		return switch (version) {
		case V30 -> SpecVersion.V30;
		case V31 -> SpecVersion.V31;
		};
	}

	/**
	 * ファイルタイプ、バージョンに応じたObjectWriterを取得します。
	 * @param fileType OpenApiFileType
	 * @param version OpenApiVersion
	 * @return ObjectWriter インスタンス
	 */
	public ObjectWriter getObjectWriter(OpenApiFileType fileType, OpenApiVersion version) {
		return switch (fileType) {
		case JSON -> {
			yield switch (version) {
			case V30 -> Json.pretty();
			case V31 -> Json31.pretty();
			};
		}
		case YAML -> {
			yield switch (version) {
			case V30 -> Yaml.pretty();
			case V31 -> Yaml31.pretty();
			};
		}
		};
	}

	/**
	 * ファイルタイプ、バージョンに応じたObjectMapperを取得します。
	 * @param fileType OpenApiFileType
	 * @param version OpenApiVersion
	 * @return ObjectMapper インスタンス
	 */
	public ObjectMapper getObjectMapper(OpenApiFileType fileType, OpenApiVersion version) {
		return switch (fileType) {
		case JSON -> {
			yield switch (version) {
			case V30 -> Json.mapper();
			case V31 -> Json31.mapper();
			};
		}
		case YAML -> {
			yield switch (version) {
			case V30 -> Yaml.mapper();
			case V31 -> Yaml31.mapper();
			};
		}
		};

	}

}
