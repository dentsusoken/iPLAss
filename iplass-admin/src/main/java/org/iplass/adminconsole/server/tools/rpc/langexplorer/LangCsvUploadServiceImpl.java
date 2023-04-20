/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.tools.rpc.langexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.upload.AdminUploadAction;
import org.iplass.adminconsole.server.base.io.upload.UploadResponseInfo;
import org.iplass.adminconsole.server.base.io.upload.UploadRuntimeException;
import org.iplass.adminconsole.server.base.io.upload.UploadUtil;
import org.iplass.adminconsole.server.base.rpc.i18n.LangDataLogic;
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.rpc.util.TransactionUtil;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAction;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAuditLogger;
import org.iplass.adminconsole.shared.base.dto.i18n.MultiLangFieldInfo;
import org.iplass.adminconsole.shared.base.dto.io.upload.UploadProperty;
import org.iplass.adminconsole.shared.tools.dto.langexplorer.OutputMode;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.tools.lang.LangDataPortingInfo;
import org.iplass.mtp.impl.tools.lang.LangDataPortingService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gwtupload.server.exceptions.UploadActionException;

@SuppressWarnings("serial")
public class LangCsvUploadServiceImpl extends AdminUploadAction {

	private static final Logger logger = LoggerFactory.getLogger(LangCsvUploadServiceImpl.class);

	/** CSVの固定ヘッダ(定義のパス) */
	private static final String FIXED_HEADER_DEFINITION_PATH = "definitionPath";
	/** CSVの固定ヘッダ(アイテム名) */
	private static final String FIXED_HEADER_ITEM = "item";
	/** CSVの固定ヘッダ(デフォルト言語) */
	private static final String FIXED_HEADER_DEFAULT_LANG = "defaultLang";

	/* (非 Javadoc)
	 * @see gwtupload.server.UploadAction#executeAction(javax.servlet.http.HttpServletRequest, java.util.List)
	 */
	@Override
	public String executeAction(final HttpServletRequest request,
			final List<FileItem> sessionFiles) throws UploadActionException {

		final LangCsvUploadResponseInfo result = new LangCsvUploadResponseInfo();
		final HashMap<String,Object> args = new HashMap<String,Object>();
		try {

			//リクエスト情報の取得
			readRequest(request, sessionFiles, args);

		    //リクエスト情報の検証
			validateRequest(args);

			//テナントIDの取得
			int tenantId = Integer.parseInt((String)args.get(UploadProperty.TENANT_ID));
			final String mode = (String) args.get("mode");

			//ここでトランザクションを開始
			AuthUtil.authCheckAndInvoke(getServletContext(), request, null, tenantId, new AuthUtil.Callable<Void>() {

				@Override
				public Void call() {
					if (OutputMode.SINGLE.name().equals(mode)) {
						String path = (String) args.get("path");
						String definitionName = (String) args.get("definitionName");

						updateSingleMeta(result, args, path, definitionName);
					} else {
						updateMultiMeta(result, args);
					}

					return null;
				}
			});

		} catch (UploadRuntimeException e) {
			TransactionUtil.setRollback();
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
	private void readRequest(final HttpServletRequest request, List<FileItem> sessionFiles, HashMap<String,Object> args) {

		//リクエスト情報の取得
		try {
			for (FileItem item : sessionFiles) {
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
			throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.errReadingRequestInfo"), e);
		}
	}

	/**
	 * 入力チェック
	 * @param args
	 */
	private void validateRequest(HashMap<String,Object> args) {
		if (args.get(UploadProperty.UPLOAD_FILE) == null) {
			throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.canNotGetImportFile"));
		}

		if (args.get(UploadProperty.TENANT_ID) == null) {
			throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.tenantInfoCannotGet"));
		}
	}

	/**
	 * 単一メタデータの更新
	 * @param result
	 * @param args
	 * @param prefixPath
	 * @param definitionName
	 */
	private void updateSingleMeta(LangCsvUploadResponseInfo result, HashMap<String, Object> args, String path, String definitionName) {

		File file = (File)args.get(UploadProperty.UPLOAD_FILE);

		try (FileInputStream fis = new FileInputStream(file)){

			//更新対象の定義取得
			LangDataPortingService service = ServiceRegistry.getRegistry().getService(LangDataPortingService.class);
			LangDataPortingInfo info = service.getLangDataPortingInfo(path);

			if (info == null) {
				result.setStatusError();
				result.addMessage(rs("tools.langexplorer.LangCsvUploadServiceImpl.cannotGetTargetMetaData", path));
				return;
			}

			Definition definition = info.getDefinition();

			//CSV読込
			Map<String, MultiLangFieldInfo> localizedStringMap = createUpdateInfo(fis, path);

			//定義に更新後の多言語設定反映
			LangDataLogic logic = new LangDataLogic();
			logic.createDefinitionInfo(definition.getClass(), definition, localizedStringMap, null);

			//定義の更新
			updateDefinition(definition);

			Map<String, List<LocalizedStringDefinition>> currentLocalizedStringMap = new HashMap<String, List<LocalizedStringDefinition>>();
			logic.createMultiLangInfo(currentLocalizedStringMap, definition.getClass(), definition, null);

			List<String> noItemKeyList = new ArrayList<String>();

			for(Map.Entry<String, MultiLangFieldInfo> e : localizedStringMap.entrySet()) {
				if (!currentLocalizedStringMap.containsKey(e.getKey())) {
					noItemKeyList.add(e.getKey());
				}
			}

			//ステータスの書き込み
			if (noItemKeyList.isEmpty()) {
				result.setStatusSuccess();
			} else {
				result.setStatusWarn();
				result.addMessage(rs("tools.langexplorer.LangCsvUploadServiceImpl.noLangItemWarn"));
				for (String itemKey : noItemKeyList) {
					result.addMessage("This ItemKey is not found. : " + itemKey);
				}
			}

		} catch (IOException e) {
			throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.errReadFile"), e);
		}
	}

	private void updateMultiMeta(LangCsvUploadResponseInfo result, HashMap<String, Object> args) {
		LangDataPortingService service = ServiceRegistry.getRegistry().getService(LangDataPortingService.class);
		LangDataLogic logic = new LangDataLogic();

		File file = (File) args.get(UploadProperty.UPLOAD_FILE);

		try (FileInputStream fis = new FileInputStream(file)){


			//更新対象の定義取得
			Map<String, DefinitionLocalizedInfo> definitionInfoMap = getDefinitionLocalizedInfo(fis);
			for (String key : definitionInfoMap.keySet()) {
				LangDataPortingInfo info = service.getLangDataPortingInfo(key);

				if (info == null) {
					result.setStatusError();
					result.addMessage(rs("tools.langexplorer.LangCsvUploadServiceImpl.cannotGetTargetMetaData", key));
					continue;
				}

				Definition definition = info.getDefinition();

				//CSV読込
				DefinitionLocalizedInfo definitionInfo = definitionInfoMap.get(key);
				Map<String, MultiLangFieldInfo> localizedStringMap = definitionInfo.getFieldInfoList();

				//定義に更新後の多言語設定反映
				logic.createDefinitionInfo(definition.getClass(), definition, localizedStringMap, null);

				//定義の更新
				updateDefinition(definition);

				Map<String, List<LocalizedStringDefinition>> currentLocalizedStringMap = new HashMap<String, List<LocalizedStringDefinition>>();
				logic.createMultiLangInfo(currentLocalizedStringMap, definition.getClass(), definition, null);

				List<String> noItemKeyList = new ArrayList<String>();

				for(Map.Entry<String, MultiLangFieldInfo> e : localizedStringMap.entrySet()) {
					if (!currentLocalizedStringMap.containsKey(e.getKey())) {
						noItemKeyList.add(e.getKey());
					}
				}

				//ステータスの書き込み
				if (noItemKeyList.isEmpty()) {
					result.setStatusSuccess();
				} else {
					result.setStatusWarn();
					result.addMessage(rs("tools.langexplorer.LangCsvUploadServiceImpl.noLangItemWarn"));
					for (String itemKey : noItemKeyList) {
						result.addMessage("This ItemKey is not found. : " + key + "#" + itemKey);
					}
				}
			}
		} catch (IOException e) {
			throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.errReadFile"), e);
		}
	}

	/**
	 * 定義の更新
	 * @param definition
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <D extends Definition> void updateDefinition(D definition) {
		DefinitionService ds = ServiceRegistry.getRegistry().getService(DefinitionService.class);
		TypedDefinitionManager tdm = ds.getTypedDefinitionManager(definition.getClass());
		if (tdm != null) {
			MetaDataAuditLogger.getLogger().logMetadata(MetaDataAction.UPDATE, definition.getClass().getName(), "name:" + definition.getName());
			tdm.update(definition);
		}
	}

	/**
	 * アップロードファイルから多言語情報を取得
	 * @param is
	 * @param definitionPath
	 * @param defType
	 * @return
	 * @throws IOException
	 */
	private Map<String, MultiLangFieldInfo> createUpdateInfo(InputStream is, String definitionPath) throws IOException {
		Map<String, MultiLangFieldInfo> itemLocalizedInfoMap = new LinkedHashMap<String, MultiLangFieldInfo>();
		DefinitionService definitionService = ServiceRegistry.getRegistry().getService(DefinitionService.class);

		try (LangCsvReader reader = new LangCsvReader(is)){

			final Iterator<Map<String, Object>> iterator = reader.iterator();

			while (iterator.hasNext()) {
				//1行分のデータ
				Map<String, Object> map = iterator.next();

				// 引数のdefinitionPathはリポジトリのパス(/区切り)
				// csvから読み込んだcsvDefinitionPathはメタ固有のプレフィックス以外は.が混じることがある
				String csvDefinitionPath = (String) map.get(FIXED_HEADER_DEFINITION_PATH);
				String replacedCsvDefinitionPath = definitionService.getPath(csvDefinitionPath);

				if (!definitionPath.equals(replacedCsvDefinitionPath)) {
					throw new UploadRuntimeException(rs("tools.langexplorer.LangCsvUploadServiceImpl.errTargetMeta"));
				}

				String itemKey = (String) map.get(FIXED_HEADER_ITEM);
				MultiLangFieldInfo multiLangFieldInfo = new MultiLangFieldInfo();
				multiLangFieldInfo.setDefaultString((String) map.get(FIXED_HEADER_DEFAULT_LANG));

				List<LocalizedStringDefinition> localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
				for(Map.Entry<String, Object> entry : map.entrySet()) {
					//固定部分は入れない
					String key = entry.getKey();
					if (FIXED_HEADER_DEFINITION_PATH.equals(key)
							|| FIXED_HEADER_ITEM.equals(key)
							|| FIXED_HEADER_DEFAULT_LANG.equals(key)) {
						continue;
					}

					String value = (String) entry.getValue();
					if (value == null || value.isEmpty()) continue;//未設定の場合は多言語定義作らない

					LocalizedStringDefinition localizedDefinition = new LocalizedStringDefinition();
					localizedDefinition.setLocaleName(key);
					localizedDefinition.setStringValue(value);
					localizedDisplayNameList.add(localizedDefinition);
				}
				multiLangFieldInfo.setLocalizedStringList(localizedDisplayNameList);
				itemLocalizedInfoMap.put(itemKey, multiLangFieldInfo);
			}

		}

		return itemLocalizedInfoMap;
	}

	/**
	 * アップロードファイルから定義毎の多言語情報を取得
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private Map<String, DefinitionLocalizedInfo> getDefinitionLocalizedInfo(InputStream is) throws IOException {
		Map<String, DefinitionLocalizedInfo> definitionMap = new HashMap<String, DefinitionLocalizedInfo>();

		try (LangCsvReader reader = new LangCsvReader(is)){

			Iterator<Map<String, Object>> iterator = reader.iterator();

			while (iterator.hasNext()) {
				//1行分のデータ
				Map<String, Object> map = iterator.next();

				String definitionPath = (String) map.get(FIXED_HEADER_DEFINITION_PATH);
				DefinitionLocalizedInfo definitionInfo = definitionMap.get(definitionPath);
				if (definitionInfo == null) {
					//パスに一致する定義が無ければ入れ物作成
					definitionInfo = new DefinitionLocalizedInfo();
					definitionInfo.setPath(definitionPath);
					definitionMap.put(definitionPath, definitionInfo);
				}

				//項目のkey
				String itemKey = (String) map.get(FIXED_HEADER_ITEM);

				//デフォルトの設定
				MultiLangFieldInfo fieldInfo = new MultiLangFieldInfo();
				fieldInfo.setDefaultString((String) map.get(FIXED_HEADER_DEFAULT_LANG));

				//多言語毎の設定
				List<LocalizedStringDefinition> localizedStringList = new ArrayList<LocalizedStringDefinition>();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					//固定部分は入れない
					String key = entry.getKey();
					if (FIXED_HEADER_DEFINITION_PATH.equals(key)
							|| FIXED_HEADER_ITEM.equals(key)
							|| FIXED_HEADER_DEFAULT_LANG.equals(key)) {
						continue;
					}

					String value = (String) entry.getValue();
					if (value == null || value.isEmpty()) continue;//未設定の場合は多言語定義作らない

					LocalizedStringDefinition definition = new LocalizedStringDefinition();
					definition.setLocaleName(key);
					definition.setStringValue(value);
					localizedStringList.add(definition);
				}
				fieldInfo.setLocalizedStringList(localizedStringList);

				definitionInfo.addFieldInfo(itemKey, fieldInfo);
			}

		}

		return definitionMap;
	}

	/**
	 * 多言語情報アップロード結果
	 */
	private class LangCsvUploadResponseInfo extends UploadResponseInfo {

		public LangCsvUploadResponseInfo() {
			super();
		}
	}

	private String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
