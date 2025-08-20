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
package org.iplass.adminconsole.server.tools.rpc.openapisupport;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.upload.AdminUploadAction;
import org.iplass.adminconsole.server.base.io.upload.MultipartRequestParameter;
import org.iplass.adminconsole.server.base.io.upload.UploadActionException;
import org.iplass.adminconsole.server.base.io.upload.UploadResponseInfo;
import org.iplass.adminconsole.server.base.io.upload.UploadRuntimeException;
import org.iplass.adminconsole.server.base.io.upload.UploadUtil;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.rpc.util.TransactionUtil;
import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;
import org.iplass.adminconsole.shared.tools.rpc.openapisupport.OpenApiSupportRpcConstant;
import org.iplass.mtp.impl.webapi.openapi.OpenApiImportResult;
import org.iplass.mtp.impl.webapi.openapi.OpenApiService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.definition.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.definition.openapi.OpenApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenAPIサポート Import サービス
 * <pre>
 * OpenAPI を解析し、WebAPI としてインポートします。
 * </pre>
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportImportService extends AdminUploadAction {
	/** serialVersionUID */
	private static final long serialVersionUID = 8413077695123654731L;
	/** ロガー */
	private static final Logger logger = LoggerFactory.getLogger(OpenApiSupportImportService.class);

	@Override
	public String executeAction(final HttpServletRequest request,
			final List<MultipartRequestParameter> sessionFiles) throws UploadActionException {

		final OpenApiSupportUploadResponseInfo result = new OpenApiSupportUploadResponseInfo();
		Map<String, Object> requestInfo = null;
		try {

			//リクエスト情報の取得
			requestInfo = readRequest(request, sessionFiles);

			//リクエスト情報の検証
			validateRequest(requestInfo);

			//テナントIDの取得
			int tenantId = Integer.parseInt((String) requestInfo.get(UploadProperty.TENANT_ID));
			File file = (File) requestInfo.get(UploadProperty.UPLOAD_FILE);
			String fileName = (String) requestInfo.get(UploadProperty.UPLOAD_FILE_NAME);
			OpenApiFileType fileType = OpenApiFileType.fromExtension(fileName);
			OpenApiVersion version = OpenApiVersion.fromSeriesVersion((String) requestInfo.get(OpenApiSupportRpcConstant.Export.Parameter.VERSION));

			//ここでトランザクションを開始
			var importResultList = AuthUtil.authCheckAndInvoke(getServletContext(), request, null, tenantId,
					new ImportLogic(file, fileName, fileType, version));

			result.setFileName(fileName);
			result.setDetails(importResultList);

		} finally {
			//Tempファイルを削除
			File file = (File) requestInfo.get(UploadProperty.UPLOAD_FILE);
			if (file != null) {
				if (!file.delete()) {
					logger.warn("Fail to delete temporary resource:" + file.getPath());
				}
			}
		}

		//ResultをJSON形式に変換
		try {
			return UploadUtil.toJsonResponse(result);
		} catch (UploadRuntimeException e) {
			throw new UploadActionException(e);
		}
	}

	/**
	 * アップロードリクエスト情報を取得する
	 * @param request HttpServletRequest
	 * @param sessionFiles MultipartRequestParameterのリスト
	 * @return リクエスト情報を格納したMap
	 */
	private Map<String, Object> readRequest(final HttpServletRequest request, List<MultipartRequestParameter> sessionFiles) {
		HashMap<String, Object> args = new HashMap<>();
		//リクエスト情報の取得
		try {
			for (MultipartRequestParameter item : sessionFiles) {
				if (item.isFormField()) {
					//File以外のもの
					args.put(item.getFieldName(), UploadUtil.getValueAsString(item));
				} else {
					//Fileの場合、tempに書きだし
					args.put(UploadProperty.UPLOAD_FILE_NAME, FilenameUtils.getName(item.getName()));
					File tempFile = UploadUtil.writeFileToTemporary(item, getContextTempDir());
					args.put(UploadProperty.UPLOAD_FILE, tempFile);
				}
			}

			return args;
		} catch (UploadRuntimeException e) {
			// アプリケーション固有のエラーに変更する
			throw new UploadRuntimeException(rs("tools.openapisupport.OpenApiSupportImportService.errReadingRequestInfo"), e);
		}
	}

	/**
	 * 入力チェック
	 * @param args リクエスト情報を格納したMap
	 */
	private void validateRequest(Map<String, Object> args) {
		if (args.get(UploadProperty.UPLOAD_FILE) == null) {
			// アップロードファイル無し
			throw new UploadRuntimeException(rs("tools.openapisupport.OpenApiSupportImportService.canNotGetImportFile"));
		}

		if (args.get(UploadProperty.UPLOAD_FILE_NAME) == null) {
			// アップロードファイル名無し
			throw new UploadRuntimeException(rs("tools.openapisupport.OpenApiSupportImportService.canNotGetFileName"));
		}

		if (args.get(UploadProperty.TENANT_ID) == null) {
			// テナント無し
			throw new UploadRuntimeException(rs("tools.openapisupport.OpenApiSupportImportService.canNotGetTenantId"));
		}

		if (args.get(OpenApiSupportRpcConstant.Import.Parameter.VERSION) == null) {
			// バージョン無し
			throw new UploadRuntimeException(rs("tools.openapisupport.OpenApiSupportImportService.canNotGetVersion"));
		}
	}

	/**
	 * OpenAPI ファイルインポートロジック
	 */
	private static class ImportLogic implements AuthUtil.Callable<List<OpenApiImportResult>> {
		/** アップロードファイル */
		private File file;
		/** アップロードファイル名 */
		private String fileName;
		/** アップロードファイルタイプ */
		private OpenApiFileType fileType;
		/** アップロードファイルバージョン */
		private OpenApiVersion version;

		/**
		 * コンストラクタ
		 * @param file アップロードファイル
		 * @param fileName アップロードファイル名
		 * @param fileType アップロードファイルタイプ
		 * @param version アップロードファイルバージョン
		 */
		public ImportLogic(File file, String fileName, OpenApiFileType fileType, OpenApiVersion version) {
			this.file = file;
			this.fileName = fileName;
			this.version = version;
			this.fileType = fileType;
		}

		@Override
		public List<OpenApiImportResult> call() {
			try {
				var service = ServiceRegistry.getRegistry().getService(OpenApiService.class);
				return service.importOpenApiToWebApi(file, fileType, version);
			} catch (Exception e) {
				TransactionUtil.setRollback();
				throw new UploadRuntimeException("Failed to import OpenAPI file: " + fileName, e);
			}
		}
	}

	/**
	 * OpenAPIサポートのアップロードレスポンス情報
	 */
	private static class OpenApiSupportUploadResponseInfo extends UploadResponseInfo {
		/** serialVersionUID */
		private static final long serialVersionUID = 3744985170367577851L;

		/**
		 * アップロードファイル名を取得します。
		 * @return アップロードファイル名
		 */
		@SuppressWarnings("unused")
		public String getFileName() {
			return (String) get(OpenApiSupportRpcConstant.Import.Response.FILE_NAME);
		}

		/**
		 * アップロードファイル名を設定します。
		 * @param fileName アップロードファイル名
		 */
		public void setFileName(String fileName) {
			put(OpenApiSupportRpcConstant.Import.Response.FILE_NAME, fileName);
		}

		/**
		 * インポート結果の詳細情報を取得します。
		 * @return インポート結果の詳細情報
		 */
		@SuppressWarnings({ "unchecked", "unused" })
		public List<OpenApiImportResult> getDetails() {
			return (List<OpenApiImportResult>) get(OpenApiSupportRpcConstant.Import.Response.DETAILS);
		}

		/**
		 * インポート結果の詳細情報を設定します。
		 * @param details  インポート結果の詳細情報
		 */
		public void setDetails(List<OpenApiImportResult> details) {
			put(OpenApiSupportRpcConstant.Import.Response.DETAILS, details);
		}
	}

	/**
	 * リソース文字列を取得します。
	 * @param key リソースキー
	 * @param arguments 置換用の引数
	 * @return リソースキーに一致する文字列
	 */
	private String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}
}
