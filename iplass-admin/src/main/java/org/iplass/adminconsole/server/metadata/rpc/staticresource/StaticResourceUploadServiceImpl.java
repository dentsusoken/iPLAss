/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc.staticresource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAction;
import org.iplass.adminconsole.server.base.service.auditlog.MetaDataAuditLogger;
import org.iplass.adminconsole.server.metadata.rpc.MetaDataVersionCheckUtil;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.EntryPathType;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.FileType;
import org.iplass.adminconsole.shared.metadata.dto.staticresource.StaticResourceUploadProperty;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.definition.binary.BinaryDefinition;
import org.iplass.mtp.definition.binary.FileArchiveBinaryDefinition;
import org.iplass.mtp.definition.binary.FileBinaryDefinition;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.staticresource.definition.EntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.JavaClassEntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.LocalizedStaticResourceDefinition;
import org.iplass.mtp.web.staticresource.definition.MimeTypeMappingDefinition;
import org.iplass.mtp.web.staticresource.definition.PrefixEntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.ScriptingEntryPathTranslatorDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gwtupload.server.exceptions.UploadActionException;

public class StaticResourceUploadServiceImpl extends AdminUploadAction {

	private static final long serialVersionUID = -4091333600499277716L;

	private static final Logger logger = LoggerFactory.getLogger(StaticResourceUploadServiceImpl.class);

	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		final StaticResourceUploadResponseInfo result = new StaticResourceUploadResponseInfo();
		final HashMap<String, Object> args = new HashMap<String, Object>();

		try {
			//リクエスト情報の取得
			readRequest(request, sessionFiles, args);

			//セッションからファイルを削除
		    super.removeSessionFileItems(request);

		    //リクエスト情報の検証
			validateRequest(args);

			//テナントIDの取得
			final int tenantId = Integer.parseInt((String) args.get(StaticResourceUploadProperty.TENANT_ID));
			final String defName = (String) args.get(StaticResourceUploadProperty.DEF_NAME);

			final int currentVersion = Integer.parseInt((String) args.get(StaticResourceUploadProperty.VERSION));
			final boolean checkVersion = Boolean.parseBoolean((String) args.get(StaticResourceUploadProperty.CHECK_VERSION));

			//ここでトランザクションを開始
			DefinitionModifyResult ret = AuthUtil.authCheckAndInvoke(getServletContext(), request, null, tenantId, new AuthUtil.Callable<DefinitionModifyResult>() {
				@Override
				public DefinitionModifyResult call() {
					DefinitionModifyResult result = null;

					try {
						//バージョンの最新チェック
						MetaDataVersionCheckUtil.versionCheck(checkVersion, StaticResourceDefinition.class, defName, currentVersion);

						//Definition生成
						StaticResourceDefinition definition = convertDefinition(args);

						//更新
						result = update(definition);
					} catch (Throwable e) {
						logger.error(e.getMessage(), e);
						result = new DefinitionModifyResult(false, e.getMessage());
					}

					return result;
				}
			});

			//ステータスの書き込み
			if (ret.isSuccess()) {
				result.setStatusSuccess();
			} else {
				result.setStatusError();
			}
			result.addMessage(ret.getMessage());

		} catch (Exception e) {
			throw new UploadActionException(e);
		} finally {
			//Tempファイルを削除
			for (Object arg : args.values()) {
				if (arg instanceof File) {
					File file = (File) arg;
					if (!file.delete()) {
						logger.warn("Fail to delete temporary resource:" + file.getPath());
					}
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

	private void readRequest(final HttpServletRequest request, List<FileItem> sessionFiles, HashMap<String, Object> args) {
		//リクエスト情報の取得
		try {
			List<LocaleDisplayNameInfo> displayNameList = new ArrayList<LocaleDisplayNameInfo>();
			List<MimeInfo> mimeList = new ArrayList<MimeInfo>();
			Map<String, LocaleInfo> localeMap = new LinkedHashMap<String, LocaleInfo>();

			for (FileItem item : sessionFiles) {
				if (item.isFormField()) {
			    	//File以外のもの
					String[] names = splitLocaleFieldName(item.getFieldName());
					String fieldName = names[0];
					if (names[1] != null) {
						//Locale用データ
						String locale = names[1];
						LocaleInfo localeData = null;
						if (localeMap.containsKey(locale)) {
							localeData = localeMap.get(locale);
						} else {
							localeData = new LocaleInfo(locale);
							localeMap.put(locale, localeData);
						}
						if (fieldName.equals(StaticResourceUploadProperty.LOCALE_BEFORE)) {
							localeData.storedLocale = UploadUtil.getValueAsString(item);
						}
						if (StaticResourceUploadProperty.BINARY_TYPE.equals(fieldName)) {
							localeData.fileType = FileType.valueOf(UploadUtil.getValueAsString(item));
						}
					} else {
						//通常データ
						if (fieldName.startsWith(StaticResourceUploadProperty.MIME_TYPE_MAPPING_PREFIX)) {
							mimeList.add(new MimeInfo(
								fieldName.substring(StaticResourceUploadProperty.MIME_TYPE_MAPPING_PREFIX.length()),
								UploadUtil.getValueAsString(item)));
						} else if (fieldName.startsWith(StaticResourceUploadProperty.DISPLAY_NAME_LOCALE_PREFIX)) {
							displayNameList.add(new LocaleDisplayNameInfo(
								fieldName.substring(StaticResourceUploadProperty.DISPLAY_NAME_LOCALE_PREFIX.length()),
								UploadUtil.getValueAsString(item)));
						} else {
							//通常データ
							String strValue = UploadUtil.getValueAsString(item);
					        args.put(item.getFieldName(), strValue);
						}
					}
				} else {
					//未選択の場合は除外
					if (item.getSize() == 0) {
						continue;
					}
					//Fileの場合、tempに書きだし
					File tempFile = UploadUtil.writeFileToTemporary(item, getContextTempDir());
					args.put(StaticResourceUploadProperty.FILE_CONTENT_TYPE, item.getContentType());

					//UploadServlet#parsePostRequestでFormFieldではないものには最後に連番(-xxxx)が付加されているので除去
					String fieldName = item.getFieldName();
					if (fieldName != null && fieldName.contains("-")) {
						fieldName = fieldName.substring(0, fieldName.lastIndexOf("-"));
					}

					String[] names = splitLocaleFieldName(fieldName);
					fieldName = names[0];
					if (names[1] != null) {
						//Locale用データ
						String locale = names[1];
						LocaleInfo localeData = null;
						if (localeMap.containsKey(locale)) {
							localeData = localeMap.get(locale);
						} else {
							localeData = new LocaleInfo(locale);
							localeMap.put(locale, localeData);
						}
						localeData.file = tempFile;
						localeData.binaryName = FilenameUtils.getName(item.getName());
					} else {
						//通常データ
						args.put(StaticResourceUploadProperty.UPLOAD_FILE, tempFile);
						args.put(StaticResourceUploadProperty.UPLOAD_FILE_NAME, FilenameUtils.getName(item.getName()));
					}
				}
			}

			args.put(StaticResourceUploadProperty.DISPLAY_NAME_LOCALE_PREFIX, displayNameList);
			args.put(StaticResourceUploadProperty.MIME_TYPE_MAPPING_PREFIX, mimeList);
			args.put(StaticResourceUploadProperty.LOCALE_PREFIX, localeMap);

		} catch (UploadRuntimeException e) {
			throw new UploadRuntimeException(resourceString("errReadingRequestInfo"), e);
		}
	}

	private void validateRequest(HashMap<String,Object> args) {
		if (args.get(StaticResourceUploadProperty.TENANT_ID) == null) {
			throw new UploadRuntimeException(resourceString("canNotGetTenantInfo"));
		}
		if (args.get(StaticResourceUploadProperty.DEF_NAME) == null) {
			throw new UploadRuntimeException(resourceString("canNotGetUpdateTarget"));
		}
	}

	private String[] splitLocaleFieldName(String fieldName) {
		String[] ret = new String[2];
		if (fieldName != null && fieldName.startsWith(StaticResourceUploadProperty.LOCALE_PREFIX)) {
			fieldName = fieldName.substring(StaticResourceUploadProperty.LOCALE_PREFIX.length());
			//次の_までがLocale値
			ret[1] = fieldName.substring(0, fieldName.indexOf("_"));
			//次の_以降がitemName
			ret[0] = fieldName.substring(fieldName.indexOf("_") + 1);
		} else {
			ret[0] = fieldName;
			ret[1] = null;
		}
		return ret;
	}

	private StaticResourceDefinition convertDefinition(HashMap<String, Object> args) {
		StaticResourceDefinitionManager dm = ManagerLocator.getInstance().getManager(StaticResourceDefinitionManager.class);
		StaticResourceDefinition oldDefinition = dm.get((String) args.get(StaticResourceUploadProperty.DEF_NAME));

		StaticResourceDefinition definition;
		List<LocalizedStaticResourceDefinition> oldLocaleList = null;
		if (oldDefinition == null) {
			//登録済のStaticResourceが存在しない場合は新規で作成しなおし
			//（更新に必要なIDはStaticResourceDefinitionManagerで同一名のIDが設定される）
			definition = new StaticResourceDefinition();
		} else {
			definition = (StaticResourceDefinition) oldDefinition;
			oldLocaleList = definition.getLocalizedResourceList();
		}

		definition.setName((String) args.get(StaticResourceUploadProperty.DEF_NAME));
		definition.setDisplayName((String) args.get(StaticResourceUploadProperty.DISPLAY_NAME));
		definition.setDescription((String) args.get(StaticResourceUploadProperty.DESCRIPTION));

		@SuppressWarnings("unchecked")
		final List<LocaleDisplayNameInfo> displayNameList = (List<LocaleDisplayNameInfo>) args.get(StaticResourceUploadProperty.DISPLAY_NAME_LOCALE_PREFIX);
		if (displayNameList != null) {
			List<LocalizedStringDefinition> localizedStringList = new ArrayList<LocalizedStringDefinition>();
			for (LocaleDisplayNameInfo info : displayNameList) {
				LocalizedStringDefinition localeDisplayNameDef = new LocalizedStringDefinition();
				localeDisplayNameDef.setLocaleName(info.localeName);
				localeDisplayNameDef.setStringValue(info.displayName);
				localizedStringList.add(localeDisplayNameDef);
			}
			definition.setLocalizedDisplayNameList(localizedStringList);
		}

		String contentType = (String) args.get(StaticResourceUploadProperty.CONTENT_TYPE);
		File binaryFile = (File) args.get(StaticResourceUploadProperty.UPLOAD_FILE);
		if (contentType != null && !contentType.isEmpty()) {
			definition.setContentType(contentType);
		} else if (binaryFile != null){
			//未指定の場合は、アップロードファイルから設定
			definition.setContentType((String) args.get(StaticResourceUploadProperty.FILE_CONTENT_TYPE));
		}
		//ファイルがアップロードされている場合は置き換え
		if (binaryFile != null) {
			BinaryDefinition resource = null;
			String binaryName = (String) args.get(StaticResourceUploadProperty.UPLOAD_FILE_NAME);
			String binaryType = (String) args.get(StaticResourceUploadProperty.BINARY_TYPE);
			if (FileType.ARCHIVE.toString().equals(binaryType)) {
				resource = new FileArchiveBinaryDefinition(binaryName, binaryFile.toPath());
			} else {
				resource = new FileBinaryDefinition(binaryName, binaryFile.toPath());
			}
			definition.setResource(resource);
		}

		@SuppressWarnings("unchecked")
		final List<MimeInfo> mimeList = (List<MimeInfo>) args.get(StaticResourceUploadProperty.MIME_TYPE_MAPPING_PREFIX);
		if (mimeList != null) {
			List<MimeTypeMappingDefinition> mimeTypeMapping = new ArrayList<MimeTypeMappingDefinition>();
			for (MimeInfo mimeInfo : mimeList) {
				MimeTypeMappingDefinition mimeDef = new MimeTypeMappingDefinition();
				mimeDef.setExtension(mimeInfo.extension);
				mimeDef.setMimeType(mimeInfo.type);
				mimeTypeMapping.add(mimeDef);
			}
			definition.setMimeTypeMapping(mimeTypeMapping);
		}

		definition.setEntryTextCharset((String) args.get(StaticResourceUploadProperty.ENTRY_TEXT_CHARSET));
		String entryPathTypeName = (String) args.get(StaticResourceUploadProperty.ENTRY_PATH_TYPE);
		String entryPathTypeContent = (String) args.get(StaticResourceUploadProperty.ENTRY_PATH_CONTENT);
		if (StringUtil.isNotEmpty(entryPathTypeName)) {
			EntryPathTranslatorDefinition entryPathDef = null;
			switch (EntryPathType.valueOf(entryPathTypeName)) {
			case JAVA:
				entryPathDef = new JavaClassEntryPathTranslatorDefinition();
				((JavaClassEntryPathTranslatorDefinition) entryPathDef).setClassName(entryPathTypeContent);
				break;
			case PREFIX:
				entryPathDef = new PrefixEntryPathTranslatorDefinition();
				((PrefixEntryPathTranslatorDefinition) entryPathDef).setPrefix(entryPathTypeContent);
				break;
			case SCRIPT:
				entryPathDef = new ScriptingEntryPathTranslatorDefinition();
				((ScriptingEntryPathTranslatorDefinition) entryPathDef).setScript(entryPathTypeContent);
				break;
			}
			definition.setEntryPathTranslator(entryPathDef);
		}

		List<LocalizedStaticResourceDefinition> newLocaleList = null;
		@SuppressWarnings("unchecked")
		Map<String, LocaleInfo> localeMap = (Map<String, LocaleInfo>) args.get(StaticResourceUploadProperty.LOCALE_PREFIX);
		if (localeMap != null) {
			newLocaleList = new ArrayList<LocalizedStaticResourceDefinition>();
			for (LocaleInfo localeInfo : localeMap.values()) {
				LocalizedStaticResourceDefinition localeDef = null;
				if (StringUtil.isEmpty(localeInfo.storedLocale)) {
					//新規追加
					localeDef = new LocalizedStaticResourceDefinition();
				} else {
					//更新
					if (oldLocaleList != null) {
						for (LocalizedStaticResourceDefinition old : oldLocaleList) {
							if (old.getLocaleName().equals(localeInfo.storedLocale)) {
								localeDef = old;
								break;
							}
						}
						if (localeDef == null) {
							//不整合だが新規で追加する(上書きで不整合かもしれないので)
							localeDef = new LocalizedStaticResourceDefinition();
						} else {
							//変更
						}
					} else {
						//不整合だが新規で追加する(上書きで不整合かもしれないので)
						localeDef = new LocalizedStaticResourceDefinition();
					}
				}
				localeDef.setLocaleName(localeInfo.newLocale);
				if (localeInfo.file != null) {
					BinaryDefinition resource = null;
					if (FileType.ARCHIVE.equals(localeInfo.fileType)) {
						resource = new FileArchiveBinaryDefinition(localeInfo.binaryName, localeInfo.file.toPath());
					} else {
						resource = new FileBinaryDefinition(localeInfo.binaryName, localeInfo.file.toPath());
					}
					localeDef.setResource(resource);
				}
				newLocaleList.add(localeDef);
			}
		}
		definition.setLocalizedResourceList(newLocaleList);

		return definition;
	}

	private DefinitionModifyResult update(StaticResourceDefinition definition) {
		StaticResourceDefinitionManager dm = ManagerLocator.getInstance().getManager(StaticResourceDefinitionManager.class);
		MetaDataAuditLogger.getLogger().logMetadata(MetaDataAction.UPDATE, StaticResourceDefinition.class.getName(), "name:" + definition.getName());
		return dm.update(definition);
	}

	private class StaticResourceUploadResponseInfo extends UploadResponseInfo {
		private static final long serialVersionUID = 4839453277142429762L;

		public StaticResourceUploadResponseInfo() {
			super();
		}
	}

	private static class LocaleDisplayNameInfo {
		private String localeName;
		private String displayName;

		public LocaleDisplayNameInfo(String locale, String displayName) {
			this.localeName = locale;
			this.displayName = displayName;
		}
	}

	private static class MimeInfo {
		private String extension;
		private String type;

		public MimeInfo(String extension, String type) {
			this.extension = extension;
			this.type = type;
		}
	}

	private static class LocaleInfo {
		private String newLocale;
		private String storedLocale;
		private File file;
		private FileType fileType;
		private String binaryName;

		public LocaleInfo(String locale) {
			this.newLocale = locale;
		}
	}

	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("metadata.staticresource.StaticResourceUploadServiceImpl." + suffix, arguments);
	}

}
