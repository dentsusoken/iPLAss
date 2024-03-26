/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.pack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.iplass.adminconsole.shared.tools.dto.pack.PackageUploadProperty;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingRuntimeException;
import org.iplass.mtp.impl.tools.pack.PackageRuntimeException;
import org.iplass.mtp.impl.tools.pack.PackageService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class PackageUploadServiceImpl extends AdminUploadAction {

	private static final Logger logger = LoggerFactory.getLogger(PackageUploadServiceImpl.class);

	private PackageService service = ServiceRegistry.getRegistry().getService(PackageService.class);

	@Override
	public String executeAction(final HttpServletRequest request,
			final List<MultipartRequestParameter> sessionFiles) throws UploadActionException {

		final PackageUploadResponseInfo result = new PackageUploadResponseInfo();
		final HashMap<String,Object> args = new HashMap<String,Object>();
		try {

			//リクエスト情報の取得
			readRequest(request, sessionFiles, args);

			//リクエスト情報の検証
			validateRequest(args);

			//テナントIDの取得
			int tenantId = Integer.parseInt((String)args.get(PackageUploadProperty.TENANT_ID));

			//ここでトランザクションを開始
			AuthUtil.authCheckAndInvoke(getServletContext(), request, null, tenantId, new AuthUtil.Callable<Void>() {

				@Override
				public Void call() {
					File file = (File)args.get(PackageUploadProperty.UPLOAD_FILE);
					String fileName = (String)args.get(PackageUploadProperty.UPLOAD_FILE_NAME);
					String description = (String)args.get(PackageUploadProperty.DESCRIPTION);

					//ファイルの変換チェック
					validateFile(file);

					//ファイルの保存
					String oid = service.uploadPackage(fileName, description, file);

					//ステータスの書き込み
					result.setStatusSuccess();
					result.addStatusMessage(resourceString("uploadPackageFile") + fileName + "(" + oid + ")");

					//OIDを格納
					result.setFileOid(oid);

					return null;
				}
			});

		} catch (PackageRuntimeException e) {
			TransactionUtil.setRollback();
			logger.error(e.getMessage(), e);
			result.setStatusError();
			result.addMessage(e.getMessage());
		} catch (MetaDataPortingRuntimeException e) {
			TransactionUtil.setRollback();
			logger.error(e.getMessage(), e);
			result.setStatusError();
			result.addMessage(e.getMessage());
		} catch (UploadRuntimeException e) {
			TransactionUtil.setRollback();
			logger.error(e.getMessage(), e);
			result.setStatusError();
			result.addMessage(e.getMessage());
		} catch (Exception e) {
			TransactionUtil.setRollback();
			logger.error(e.getMessage(), e);
			//			throw new UploadActionException(e);
			result.setStatusError();
			result.addMessage(resourceString("systemErr"));
		} finally {
			//Tempファイルを削除
			File file = (File)args.get(PackageUploadProperty.UPLOAD_FILE);
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

	private void readRequest(final HttpServletRequest request, List<MultipartRequestParameter> sessionFiles, HashMap<String, Object> args) {

		//リクエスト情報の取得
		String fileDefaultName = null;
		try {
			for (MultipartRequestParameter item : sessionFiles) {
				if (item.isFormField()) {
					//File以外のもの
					args.put(item.getFieldName(), UploadUtil.getValueAsString(item));
				} else {
					//Fileの場合、tempに書きだし
					File tempFile = UploadUtil.writeFileToTemporary(item, getContextTempDir());
					args.put(PackageUploadProperty.UPLOAD_FILE, tempFile);
					fileDefaultName = FilenameUtils.removeExtension(FilenameUtils.getName(item.getName()));
				}
			}
		} catch (UploadRuntimeException e) {
			throw new UploadRuntimeException(resourceString("errReadingRequestInfo"), e);
		}
		if (args.get(PackageUploadProperty.UPLOAD_FILE_NAME) == null
				|| ((String)args.get(PackageUploadProperty.UPLOAD_FILE_NAME)).isEmpty()) {
			args.put(PackageUploadProperty.UPLOAD_FILE_NAME, fileDefaultName);
		}
	}

	private void validateRequest(HashMap<String,Object> args) {
		if (args.get(PackageUploadProperty.UPLOAD_FILE) == null) {
			throw new UploadRuntimeException(resourceString("canNotGetImportFile"));
		}

		if (args.get(PackageUploadProperty.TENANT_ID) == null) {
			throw new UploadRuntimeException(resourceString("tenantInfoCannotGet"));
		}
	}

	private void validateFile(File file) {

		//整合性チェックとしてPackage情報を取得
		//PackageInfo info = service.getPackageInfo(file);
		service.getPackageInfo(file);

	}

	private class PackageUploadResponseInfo extends UploadResponseInfo {

		public PackageUploadResponseInfo() {
			super();
		}

		public void setFileOid(String fileOid) {
			put(PackageUploadProperty.FILE_OID, fileOid);
		}

	}

	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("tools.pack.PackageUploadServiceImpl." + suffix, arguments);
	}

}
