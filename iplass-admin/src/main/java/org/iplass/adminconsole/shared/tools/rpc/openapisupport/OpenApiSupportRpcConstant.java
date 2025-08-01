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
 * OpenAPIサポートのRPC定数クラス
 * @author SEKIGUCHI Naoya
 */
public final class OpenApiSupportRpcConstant {
	// それぞれのクラスで指定されている SERVICE_NAME の設定値は web-fragment.xml に定義する。

	/**
	 * Export 関連定数
	 */
	public static final class Export {
		/** サービス名 */
		public static final String SERVICE_NAME = "service/exportopenapi";

		/**
		 * パラメータ定数
		 */
		public static final class Parameter {
			/** バージョン */
			public static final String VERSION = "version";
			/** ファイルタイプ */
			public static final String FILE_TYPE = "fileType";
			/** 画面選択値 */
			public static final String SELECT_VALUE = "selectalue";

			/**
			 * プライベートコンストラクタ
			 */
			private Parameter() {
			}
		}

		/**
		 * プライベートコンストラクタ
		 */
		private Export() {
		}
	}

	/**
	 * Import 関連定数
	 */
	public static final class Import {
		/** サービス名 */
		public static final String SERVICE_NAME = "service/importopenapi";

		/**
		 * パラメータ定数
		 */
		public static final class Parameter {
			/** バージョン */
			public static final String VERSION = "version";

			/**
			 * プライベートコンストラクタ
			 */
			private Parameter() {
			}
		}

		/**
		 * レスポンスキー定数
		 */
		public static final class Response {
			/** レスポンスキー：ファイル名 */
			public static final String FILE_NAME = "fileName";
			/** レスポンスキー：詳細情報 */
			public static final String DETAILS = "details";

			/**
			 * プライベートコンストラクタ
			 */
			private Response() {
			}
		}

		/**
		 * プライベートコンストラクタ
		 */
		private Import() {
		}
	}

	/**
	 * サービス関連定数
	 */
	public static final class Service {
		/** サービス名 */
		public static final String SERVICE_NAME = "service/openapi";

		/**
		 * ルートノード名
		 */
		public static final class RootNode {
			/** WebAPIルートノード */
			public static final String WEB_API = "WebApi";
			/** Entity CRUD API ルートノード */
			public static final String ENTITY_CRUD_API = "EntityCRUDApi";

			/**
			 * プライベートコンストラクタ
			 */
			private RootNode() {
			}
		}

		/**
		 * プライベートコンストラクタ
		 */
		private Service() {
		}
	}

	private OpenApiSupportRpcConstant() {
	}
}
