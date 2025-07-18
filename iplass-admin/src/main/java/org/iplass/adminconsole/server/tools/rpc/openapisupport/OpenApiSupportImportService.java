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
import org.iplass.mtp.webapi.openapi.OpenApiFileType;
import org.iplass.mtp.webapi.openapi.OpenApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO service ?
/**
 *
 */
public class OpenApiSupportImportService extends AdminUploadAction {

	private static final Logger logger = LoggerFactory.getLogger(OpenApiSupportImportService.class);

	@Override
	public String executeAction(final HttpServletRequest request,
			final List<MultipartRequestParameter> sessionFiles) throws UploadActionException {

		final OpenApiSupportUploadResponseInfo result = new OpenApiSupportUploadResponseInfo();
		HashMap<String, Object> requestInfo = null;
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

		} catch (UploadRuntimeException e) {
			TransactionUtil.setRollback();
			logger.error(e.getMessage(), e);
			result.setStatusError();
			result.addMessage(e.getMessage());

		} catch (Exception e) {
			throw new UploadActionException(e);

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
	 * リクエスト情報取得
	 * @param request
	 * @param sessionFiles
	 */
	private HashMap<String, Object> readRequest(final HttpServletRequest request, List<MultipartRequestParameter> sessionFiles) {
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
			// FIXME アプリケーション固有のエラーに変更する
			throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.errReadingRequestInfo"), e);
		}
	}

	/**
	 * 入力チェック
	 * @param args
	 */
	private void validateRequest(HashMap<String,Object> args) {
		if (args.get(UploadProperty.UPLOAD_FILE) == null) {
			// FIXME アップロードファイル無しのメッセージに変更する
			throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.canNotGetImportFile"));
		}

		if (args.get(UploadProperty.UPLOAD_FILE_NAME) == null) {
			// FIXME アップロードファイル名無しのメッセージに変更する
			throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.canNotGetImportFile"));
		}

		if (args.get(UploadProperty.TENANT_ID) == null) {
			// FIXME テナント無しのメッセージに変更する
			throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.tenantInfoCannotGet"));
		}

		if (args.get(OpenApiSupportRpcConstant.Export.Parameter.VERSION) == null) {
			// FIXME バージョン無しのメッセージに変更する
			throw new UploadRuntimeException();
		}
	}

	private static class ImportLogic implements AuthUtil.Callable<List<OpenApiImportResult>> {
		private File file;
		private String fileName;
		private OpenApiFileType fileType;
		private OpenApiVersion version;

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
				throw new UploadRuntimeException("Failed to import OpenAPI file: " + fileName + " (server path: " + file.getAbsolutePath() + " )", e);
			}
		}
	}

	/**
	 * OpenAPIサポートのアップロードレスポンス情報
	 */
	private static class OpenApiSupportUploadResponseInfo extends UploadResponseInfo {
		private static final String RESPONSE_KEY_FILE_NAME = "fileName";
		private static final String RESPONSE_KEY_DETAILS = "details";

		public OpenApiSupportUploadResponseInfo() {
			super();
		}

		public String getFileName() {
			return (String) get(RESPONSE_KEY_FILE_NAME);
		}

		public void setFileName(String fileName) {
			put(RESPONSE_KEY_FILE_NAME, fileName);
		}

		@SuppressWarnings("unchecked")
		public List<OpenApiImportResult> getDetails() {
			return (List<OpenApiImportResult>) get(RESPONSE_KEY_DETAILS);
		}

		public void setDetails(List<OpenApiImportResult> details) {
			put(RESPONSE_KEY_DETAILS, details);
		}
	}

	private String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}
}
