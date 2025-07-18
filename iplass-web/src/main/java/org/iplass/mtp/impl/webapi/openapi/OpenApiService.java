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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.iplass.mtp.impl.webapi.openapi.entity.EntityWebApiOpenApiEntry;
import org.iplass.mtp.impl.webapi.openapi.schema.OpenApiComponentSchemaFactory;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.openapi.OpenApiVersion;

// TODO たぶん後で消すことになる
/**
 * OpenAPI 操作サービス
 * @author SEKIGUCHI Naoya
 */
public interface OpenApiService extends Service {
	/**
	 * OpenAPI仕様を出力ストリームに書き込みます。
	 * @param out 出力先のOutputStream
	 * @param webApiList WebAPI定義のリスト
	 * @param entityWebApiList エンティティWebAPIのOpenAPIエントリのリスト
	 * @param fileType OpenAPIファイルのタイプ（JSONまたはYAML）
	 * @param version OpenAPIのバージョン（V30またはV31）
	 * @throws IOException 入出力エラー
	 */
	void writeOpenApiSpec(OutputStream out, List<WebApiDefinition> webApiList, List<EntityWebApiOpenApiEntry> entityWebApiList,
			OpenApiFileType fileType, OpenApiVersion version) throws IOException;

	/**
	 * OpenAPI仕様をWebAPIとしてインポートします。
	 * @param file インポートするOpenAPI仕様のファイル
	 * @param fileType OpenAPIファイルのタイプ（JSONまたはYAML）
	 * @param version OpenAPIのバージョン（V30またはV31）
	 * @return インポート結果のリスト
	 * @throws IOException 入出力エラー
	 */
	List<OpenApiImportResult> importOpenApiToWebApi(File file, OpenApiFileType fileType, OpenApiVersion version) throws IOException;

	/**
	 * OpenAPI スキーマ生成機能を取得する。
	 * @return OpenAPIスキーマ生成機能
	 */
	OpenApiComponentSchemaFactory<? super Object> getSchemaFactory();
}
