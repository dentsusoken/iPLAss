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
package org.iplass.adminconsole.shared.tools.rpc.openapisupport;

/**
 *
 */
public final class OpenApiSupportRpcConstant {
	// FIXME サービス名しか設定していない

	public static final class Export {
		public static final String SERVICE_NAME = "service/exportopenapi";

		public static final class Parameter {
			public static final String VERSION = "version";
			public static final String FILE_TYPE = "fileType";
			public static final String SELECT_VALUE = "selectalue";

			private Parameter() {
			}
		}
	}

	public static final class Import {
		public static final String SERVICE_NAME = "service/importopenapi";

		public static final class Parameter {

			public static final String VERSION = "version";

			private Parameter() {
			}
		}
	}

	public static final class Service {
		public static final String SERVICE_NAME = "service/openapi";

		public static final class RootNode {
			public static final String WEB_API = "WebApi";
			public static final String ENTITY_CRUD_API = "EntityCRUDApi";
		}
	}

	private OpenApiSupportRpcConstant() {
	}
}
