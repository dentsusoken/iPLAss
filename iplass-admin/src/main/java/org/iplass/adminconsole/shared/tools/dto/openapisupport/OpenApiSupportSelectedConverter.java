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
package org.iplass.adminconsole.shared.tools.dto.openapisupport;

import java.util.List;
import java.util.Map;

// FIXME shared にロジック配置するのは変だと思われる。サーバークライアントに分離し、相互に参照するコメントを記載すること。
/**
 *
 */
public class OpenApiSupportSelectedConverter {
	public static String convertString(OpenApiSupportTreeGridSelected dto) {
		if (dto == null) {
			return null;
		}

		StringBuilder converted = new StringBuilder();
		// WebApi <-- | --> EntityCRUDApi
		// path:path:path:path|path,select,select:path,select

		if (dto.getWebApiList() != null && !dto.getWebApiList().isEmpty()) {
			for (String webApi : dto.getWebApiList()) {
				converted.append(webApi).append(":");
			}
			converted.deleteCharAt(converted.length() - 1); // 最後のコロンを削除
		}
		converted.append("|");
		if (dto.getEntityCRUDApiMap() != null && !dto.getEntityCRUDApiMap().isEmpty()) {
			for (Map.Entry<String, List<String>> entityCRUDApi : dto.getEntityCRUDApiMap().entrySet()) {
				var definitionName = entityCRUDApi.getKey();
				var authList = entityCRUDApi.getValue();
				converted.append(definitionName).append(",");
				converted.append(String.join(",", authList));
				converted.append(":");
			}
			converted.deleteCharAt(converted.length() - 1); // 最後のコロンを削除
		}

		return converted.toString();
	}

	public static OpenApiSupportTreeGridSelected convertDto(String value) {
		if (value == null) {
			return null;
		}

		String[] parts = value.split("\\|", -1);
		if (parts.length != 2) {
			throw new RuntimeException();
		}

		var dto = new OpenApiSupportTreeGridSelected();
		var webApiValue = parts[0];
		var webApiArray = webApiValue.split(":");
		for (String path : webApiArray) {
			dto.addWebApi(path);
		}

		var entityCRUDApiValue = parts[1];
		var entityCRUDApiArray = entityCRUDApiValue.split(":");
		for (String v : entityCRUDApiArray) {
			String[] pathAndAuthorized = v.split(",");

			for (var i = 1; i < pathAndAuthorized.length; i++) {
				dto.addEntityCRUDApi(pathAndAuthorized[0], pathAndAuthorized[i]);
			}
		}

		return dto;
	}
}
