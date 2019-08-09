/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc.template;

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
import org.iplass.adminconsole.shared.metadata.dto.template.ReportTemplateUploadProperty;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinitionManager;
import org.iplass.mtp.web.template.report.definition.GroovyReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.JasperReportType;
import org.iplass.mtp.web.template.report.definition.JavaClassReportOutputLogicDefinition;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;
import org.iplass.mtp.web.template.report.definition.PoiReportType;
import org.iplass.mtp.web.template.report.definition.ReportParamMapDefinition;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;
import org.iplass.mtp.web.template.report.definition.ReportType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gwtupload.server.exceptions.UploadActionException;

@SuppressWarnings("serial")
public class ReportTemplateUploadServiceImpl extends AdminUploadAction {

	private static final Logger logger = LoggerFactory.getLogger(ReportTemplateUploadServiceImpl.class);

	/* (非 Javadoc)
	 * @see gwtupload.server.UploadAction#executeAction(javax.servlet.http.HttpServletRequest, java.util.List)
	 */
	@Override
	public String executeAction(final HttpServletRequest request,
			final List<FileItem> sessionFiles) throws UploadActionException {

		final ReportTemplateUploadResponseInfo result = new ReportTemplateUploadResponseInfo();
		final HashMap<String,Object> args = new HashMap<String,Object>();
		try {
			//リクエスト情報の取得
			readRequest(request, sessionFiles, args);

			//セッションからファイルを削除
		    super.removeSessionFileItems(request);

		    //リクエスト情報の検証
			validateRequest(args);

			//テナントIDの取得
			final int tenantId = Integer.parseInt((String)args.get(ReportTemplateUploadProperty.TENANT_ID));
			final String defName = (String)args.get(ReportTemplateUploadProperty.DEF_NAME);

			final int currentVersion = Integer.parseInt((String)args.get(ReportTemplateUploadProperty.VERSION));
			final boolean checkVersion = Boolean.parseBoolean((String)args.get(ReportTemplateUploadProperty.CHECK_VERSION));

			//ここでトランザクションを開始
			DefinitionModifyResult ret = AuthUtil.authCheckAndInvoke(getServletContext(), request, null, tenantId, new AuthUtil.Callable<DefinitionModifyResult>() {

				@Override
				public DefinitionModifyResult call() {

					DefinitionModifyResult result = null;
					try {
						// バージョンの最新チェック
						MetaDataVersionCheckUtil.versionCheck(checkVersion, ReportTemplateDefinition.class, defName, currentVersion);

						//Definition生成
						ReportTemplateDefinition definition = convertDefinition(args);

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
					File file = (File)arg;
					if (!file.delete()) {
						logger.warn("Fail to delete temporary resource:" + file.getPath());
					}
				}
			}
		}

		//ResultをJSON形式に変換
	    try {
	    	String jsonResult = UploadUtil.toJsonResponse(result);
			return jsonResult;
		} catch (UploadRuntimeException e) {
			throw new UploadActionException(e);
		}
	}

	private void readRequest(final HttpServletRequest request, List<FileItem> sessionFiles, HashMap<String,Object> args) {

		//リクエスト情報の取得
		try {
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
						if (fieldName.equals(ReportTemplateUploadProperty.LOCALE_BEFORE)) {
							localeData.storedLocale = UploadUtil.getValueAsString(item);
						} else {
							localeData.values.put(fieldName, UploadUtil.getValueAsString(item));
						}
					} else {
						String strValue = UploadUtil.getValueAsString(item);
				        args.put(item.getFieldName(), strValue);
					}

				} else {
					//未選択の場合は除外
					if (item.getSize() == 0) {
						continue;
					}
					//Fileの場合、tempに書きだし
					File tempFile = UploadUtil.writeFileToTemporary(item, getContextTempDir());
					args.put(ReportTemplateUploadProperty.FILE_CONTENT_TYPE, item.getContentType());

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
						localeData.values.put(ReportTemplateUploadProperty.UPLOAD_FILE_NAME, FilenameUtils.getName(item.getName()));
					} else {
						//通常データ
						args.put(ReportTemplateUploadProperty.UPLOAD_FILE, tempFile);
						args.put(ReportTemplateUploadProperty.UPLOAD_FILE_NAME, FilenameUtils.getName(item.getName()));
					}
				}
			}

			args.put(ReportTemplateUploadProperty.LOCALE_PREFIX, localeMap);

		} catch (UploadRuntimeException e) {
			throw new UploadRuntimeException(resourceString("errReadingRequestInfo"), e);
		}
	}

	private void validateRequest(HashMap<String,Object> args) {
		if (args.get(ReportTemplateUploadProperty.TENANT_ID) == null) {
			throw new UploadRuntimeException(resourceString("canNotGetTenantInfo"));
		}
		if (args.get(ReportTemplateUploadProperty.DEF_NAME) == null) {
			throw new UploadRuntimeException(resourceString("canNotGetUpdateTarget"));
		}
//		if (args.get(ReportTemplateUploadProperty.UPLOAD_FILE) == null) {
//			throw new UploadRuntimeException(getRS("canNotGetImportFile"));
//		}
	}

	private String[] splitLocaleFieldName(String fieldName) {

		String[] ret = new String[2];
		if (fieldName != null && fieldName.startsWith(ReportTemplateUploadProperty.LOCALE_PREFIX)) {
			fieldName = fieldName.substring(ReportTemplateUploadProperty.LOCALE_PREFIX.length());
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

	private ReportTemplateDefinition convertDefinition(HashMap<String,Object> args) {

		TemplateDefinitionManager tdm = ManagerLocator.getInstance().getManager(TemplateDefinitionManager.class);
		TemplateDefinition oldTemplate = tdm.get((String)args.get(ReportTemplateUploadProperty.DEF_NAME));

		ReportTemplateDefinition definition;
		List<LocalizedReportDefinition> oldLocaleList = null;
		if (oldTemplate == null || !(oldTemplate instanceof ReportTemplateDefinition)) {
			//登録済のTemplateが存在しないか、Reportではない場合は新規で作成しなおし
			//（更新に必要なIDはTemplateDefinitionManagerで同一名のIDが設定される）
			definition = new ReportTemplateDefinition();
		} else {
			definition = (ReportTemplateDefinition) oldTemplate;
			oldLocaleList = definition.getLocalizedReportList();
		}

		definition.setName((String)args.get(ReportTemplateUploadProperty.DEF_NAME));
		definition.setDisplayName((String)args.get(ReportTemplateUploadProperty.DISPLAY_NAME));
		definition.setDescription((String)args.get(ReportTemplateUploadProperty.DESCRIPTION));

		String contentType = (String)args.get(ReportTemplateUploadProperty.CONTENT_TYPE);
		File binaryFile = (File)args.get(ReportTemplateUploadProperty.UPLOAD_FILE);
		if (contentType != null && !contentType.isEmpty()) {
			definition.setContentType(contentType);
		} else if (binaryFile != null){
			//未指定の場合は、アップロードファイルから設定
			definition.setContentType((String)args.get(ReportTemplateUploadProperty.FILE_CONTENT_TYPE));
		}
		//ファイルがアップロードされている場合は置き換え
		if (binaryFile != null) {
			definition.setBinary(convertFileToByte(binaryFile));
			definition.setFileName((String)args.get(ReportTemplateUploadProperty.UPLOAD_FILE_NAME));
		}

		ReportType reportType = createReportType(args);
		definition.setReportType(reportType);

		// 多言語用の処理
		List<LocalizedReportDefinition> newLocaleList = null;
		@SuppressWarnings("unchecked")
		Map<String, LocaleInfo> localeMap = (Map<String, LocaleInfo>)args.get(ReportTemplateUploadProperty.LOCALE_PREFIX);
		if (localeMap != null && !localeMap.isEmpty()) {
			newLocaleList = new ArrayList<LocalizedReportDefinition>();
			for(LocaleInfo value : localeMap.values()) {
				LocalizedReportDefinition localeDef = null;
				if (StringUtil.isEmpty(value.storedLocale)) {
					//新規追加
					localeDef = new LocalizedReportDefinition();
				} else {
					//更新
					if (oldLocaleList != null) {
						for (LocalizedReportDefinition old : oldLocaleList) {
							if (old.getLocaleName().equals(value.storedLocale)) {
								localeDef = old;
								break;
							}
						}
						if (localeDef == null) {
							//不整合だが新規で追加する(上書きで不整合かもしれないので)
							localeDef = new LocalizedReportDefinition();
						} else {
							//変更
						}
					} else {
						//不整合だが新規で追加する(上書きで不整合かもしれないので)
						localeDef = new LocalizedReportDefinition();
					}
				}
				localeDef.setLocaleName(value.newLocale);
				if (value.file != null) {
					localeDef.setBinary(convertFileToByte(value.file));
					localeDef.setFileName((String)value.values.get(ReportTemplateUploadProperty.UPLOAD_FILE_NAME));
				}

				ReportType localReportType = createReportType(value.values);
				localeDef.setReportType(localReportType);

				newLocaleList.add(localeDef);
			}
		}
		definition.setLocalizedReportList(newLocaleList);

		return definition;
	}

	private ReportType createReportType(HashMap<String,Object> args) {

		String reportType = (String)args.get(ReportTemplateUploadProperty.REPORT_TYPE);

		if(PoiReportType.class.getName().equals(reportType)){
			PoiReportType poiTemplate = new PoiReportType();
			poiTemplate.setOutputFileType((String)args.get(ReportTemplateUploadProperty.OUTPUT_FILE_TYPE));

			String logicType = (String)(args.get(ReportTemplateUploadProperty.POI_LOGIC_NAME));
			if(ReportTemplateUploadProperty.POI_LOGIC_NAME_JAVA.equals(logicType)){
				JavaClassReportOutputLogicDefinition javaLogic = new JavaClassReportOutputLogicDefinition();
				javaLogic.setClassName((String)(args.get(ReportTemplateUploadProperty.POI_LOGIC_VALUE)));
				poiTemplate.setReportOutputLogicDefinition(javaLogic);
			}else if(ReportTemplateUploadProperty.POI_LOGIC_NAME_GROOVY.equals(logicType)){
				GroovyReportOutputLogicDefinition groovyLogic = new GroovyReportOutputLogicDefinition();
				groovyLogic.setScript((String)(args.get(ReportTemplateUploadProperty.POI_LOGIC_VALUE)));
				poiTemplate.setReportOutputLogicDefinition(groovyLogic);
			}

			String passwordAttributeName = (String)args.get(ReportTemplateUploadProperty.POI_PASSWORD_ATTRIBUTE_NAME);
			if (StringUtil.isNotEmpty(passwordAttributeName)) {
				poiTemplate.setPasswordAttributeName(passwordAttributeName);
			}

			return poiTemplate;
		}else{
			JasperReportType jasperTemplate = new JasperReportType();
			jasperTemplate.setOutputFileType((String)args.get(ReportTemplateUploadProperty.OUTPUT_FILE_TYPE));

			//パラメータマッピング設定
			int mapCnt = Integer.parseInt((String)(args.get(ReportTemplateUploadProperty.JASPER_PARAM_MAP_CNT)));
			ReportParamMapDefinition[] rpmds = new ReportParamMapDefinition[mapCnt];
			for( int i=0; i < mapCnt; i++){
				ReportParamMapDefinition rpmd = new ReportParamMapDefinition();
				rpmd.setName((String)args.get(ReportTemplateUploadProperty.JASPER_PARAM_MAP_NAME + "_" + i));
				rpmd.setMapFrom((String)args.get(ReportTemplateUploadProperty.JASPER_PARAM_MAP_FROM + "_" + i));
				rpmd.setParamType((String)args.get(ReportTemplateUploadProperty.JASPER_PARAM_TYPE + "_" + i));
				rpmds[i] = rpmd;
			}
			if(mapCnt > 0){
				jasperTemplate.setParamMap(rpmds);
			}

			String dataSourceAttributeName = (String)args.get(ReportTemplateUploadProperty.JASPER_DATASOURCE_ATTRIBUTE_NAME);
			if (StringUtil.isNotEmpty(dataSourceAttributeName)) {
				jasperTemplate.setDataSourceAttributeName(dataSourceAttributeName);
			}
			String passwordAttributeName = (String)args.get(ReportTemplateUploadProperty.JASPER_PASSWORD_ATTRIBUTE_NAME);
			if (StringUtil.isNotEmpty(passwordAttributeName)) {
				jasperTemplate.setPasswordAttributeName(passwordAttributeName);
			}

			return jasperTemplate;
		}
	}

	private DefinitionModifyResult update(ReportTemplateDefinition definition) {
		TemplateDefinitionManager tdm = ManagerLocator.getInstance().getManager(TemplateDefinitionManager.class);
		MetaDataAuditLogger.getLogger().logMetadata(MetaDataAction.UPDATE, ReportTemplateDefinition.class.getName(), "name:" + definition.getName());
		return tdm.update(definition);
	}

	private class ReportTemplateUploadResponseInfo extends UploadResponseInfo {

		public ReportTemplateUploadResponseInfo() {
			super();
		}

	}

	private static class LocaleInfo {
		private String newLocale;
		private String storedLocale;
		private HashMap<String,Object> values = new HashMap<String, Object>();
		private File file;

		public LocaleInfo(String locale) {
			this.newLocale = locale;
		}

	}

	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("metadata.template.ReportTemplateUploadServiceImpl." + suffix, arguments);
	}

}
