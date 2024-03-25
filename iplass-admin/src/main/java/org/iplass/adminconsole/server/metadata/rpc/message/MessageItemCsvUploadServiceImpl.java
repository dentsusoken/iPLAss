/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.message.MessageItem;
import org.iplass.mtp.message.MessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageItemCsvUploadServiceImpl extends AdminUploadAction {

	private static final long serialVersionUID = -2127709715667994925L;

	private static final Logger logger = LoggerFactory.getLogger(MessageItemCsvUploadServiceImpl.class);

	@Override
	public String executeAction(final HttpServletRequest request,
			final List<MultipartRequestParameter> sessionFiles) throws UploadActionException {

		final MessageItemCsvUploadResponseInfo result = new MessageItemCsvUploadResponseInfo();
		final HashMap<String,Object> args = new HashMap<String,Object>();
		try {

			//リクエスト情報の取得
			readRequest(request, sessionFiles, args);

			//リクエスト情報の検証
			validateRequest(args);

			//テナントIDの取得
			int tenantId = Integer.parseInt((String)args.get(UploadProperty.TENANT_ID));

			//ここでトランザクションを開始
			AuthUtil.authCheckAndInvoke(getServletContext(), request, null, tenantId, new AuthUtil.Callable<Void>() {

				@Override
				public Void call() {
					String definitionName = (String) args.get("definitionName");

					//更新
					importCSV(result, args, definitionName);

					return null;
				}
			});

		} catch (UploadRuntimeException e) {
			logger.error(e.getMessage(), e);
			result.setStatusError();
			result.addMessage(e.getMessage());
		} catch (Exception e) {
			throw new UploadActionException(e);
		} finally {
			//Tempファイルを削除
			File file = (File) args.get(UploadProperty.UPLOAD_FILE);
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
	 * @return
	 */
	private void readRequest(final HttpServletRequest request, List<MultipartRequestParameter> sessionFiles, HashMap<String, Object> args) {

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
		} catch (UploadRuntimeException e) {
			throw new UploadRuntimeException(rs("metadata.message.MessageItemCsvUploadServiceImpl.errReadingRequestInfo"), e);
		}
	}

	/**
	 * 入力チェック
	 * @param args
	 */
	private void validateRequest(HashMap<String,Object> args) {
		if (args.get(UploadProperty.UPLOAD_FILE) == null) {
			throw new UploadRuntimeException(rs("metadata.message.MessageItemCsvUploadServiceImpl.canNotGetImportFile"));
		}

		if (args.get(UploadProperty.TENANT_ID) == null) {
			throw new UploadRuntimeException(rs("metadata.message.MessageItemCsvUploadServiceImpl.cannotGetTenantInfo"));
		}
	}

	/**
	 * 更新
	 *
	 * @param result
	 * @param args
	 * @param definitionName
	 */
	private void importCSV(MessageItemCsvUploadResponseInfo result, HashMap<String, Object> args, String definitionName) {

		File file = (File)args.get(UploadProperty.UPLOAD_FILE);

		//更新対象の定義取得
		MessageManager mm = ManagerLocator.getInstance().getManager(MessageManager.class);
		MessageCategory category = mm.get(definitionName);

		if (category == null) {
			result.setStatusError();
			result.addMessage(rs("metadata.message.MessageItemCsvUploadServiceImpl.cannotGetTargetMetaData", definitionName));
			return;
		}

		try (FileInputStream fis = new FileInputStream(file);
				MessageItemCsvReader reader = new MessageItemCsvReader(fis);) {

			//IDでソートするためTreeMapに格納
			Map<String, MessageItem> messageItems = new TreeMap<>();

			Iterator<MessageItem> iterator = reader.iterator();	//このタイミングでHeaderが読み込まれる

			while (iterator.hasNext()) {
				MessageItem item = iterator.next();
				messageItems.put(item.getMessageId(), item);
			}

			if (!messageItems.isEmpty()) {
				category.setMessageItems(messageItems);
			}

			//定義の更新
			mm.update(category);

			result.setStatusSuccess();
			result.addMessage(rs("metadata.message.MessageItemCsvUploadServiceImpl.updateComplete", messageItems.size()));

		} catch (IOException e) {
			throw new UploadRuntimeException(rs("metadata.message.MessageItemCsvUploadServiceImpl.errReadFile"), e);
		}
	}

	/**
	 * アップロード結果
	 */
	private static class MessageItemCsvUploadResponseInfo extends UploadResponseInfo {

		private static final long serialVersionUID = 7956890400420917332L;

		public MessageItemCsvUploadResponseInfo() {
			super();
		}
	}

	private String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
