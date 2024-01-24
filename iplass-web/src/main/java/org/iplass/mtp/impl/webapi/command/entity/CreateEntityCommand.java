/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.command.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.RestXml;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.impl.csv.CsvUploadService;
import org.iplass.mtp.impl.csv.CsvUploadStatus;
import org.iplass.mtp.impl.csv.EntityCsvImportOption;
import org.iplass.mtp.impl.csv.EntityCsvImportResult;
import org.iplass.mtp.impl.csv.EntityCsvImportService;
import org.iplass.mtp.impl.csv.TransactionType;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.csv.EntityCsvException;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.fileupload.MultiPartParameterValueMap;
import org.iplass.mtp.impl.webapi.EntityWebApiService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(name="mtp/entity/POST",
		accepts={RequestType.REST_JSON, RequestType.REST_XML, RequestType.REST_FORM},
		methods={MethodType.POST},
		restJson=@RestJson(parameterName=CreateEntityCommand.PARAM_ENTITY, parameterType=Entity.class),
		restXml=@RestXml(parameterName=CreateEntityCommand.PARAM_ENTITY),
		results={CreateEntityCommand.RESULT_OID,
				CreateEntityCommand.RESULT_UPLOAD_INSERT, CreateEntityCommand.RESULT_UPLOAD_UPDATE,
				CreateEntityCommand.RESULT_UPLOAD_DELETE, CreateEntityCommand.RESULT_UPLOAD_ERROR},
		supportBearerToken = true,
		overwritable=false)
@CommandClass(name="mtp/entity/CreateEntityCommand", displayName="Entity Create Web API", overwritable=false)
public final class CreateEntityCommand extends AbstractEntityCommand {

	public static final String PARAM_ENTITY = "entity";
	public static final String RESULT_OID = "oid";

	// POSTのオプションパラメータ
	public static final String PARAM_POST_WITH_VALIDATION = "withValidation";
	public static final String PARAM_POST_NOTIFY_LISTENERS = "notifyListeners";
	public static final String PARAM_POST_ENABLE_AUDIT_PROPERTY_SPECIFICATION = "enableAuditPropertySpecification";
	public static final String PARAM_POST_REGENERATE_OID = "regenerateOid";
	public static final String PARAM_POST_REGENERATE_AUTO_NUMBER = "regenerateAutoNumber";
	public static final String PARAM_POST_VERSION_SPECIFIED = "versionSpecified";
	public static final String PARAM_POST_LOCALIZED = "localized";

	// POST(Upload)のオプションパラメータ
	public static final String PARAM_UPLOAD_FILE = "uploadFile";
	public static final String PARAM_UPLOAD_UNIQUE_KEY = "uniqueKey";
	public static final String PARAM_UPLOAD_ASYNC = "asyncUpload";
	public static final String PARAM_UPLOAD_TRUNCATE = "truncate";
	public static final String PARAM_UPLOAD_BULK_UPDATE = "bulkUpdate";
	public static final String PARAM_UPLOAD_ERROR_SKIP = "errorSkip";
	public static final String PARAM_UPLOAD_IGNORE_NOT_EXIST_PROPERTY = "ignoreNotExistsProperty";
	public static final String PARAM_UPLOAD_NOTIFY_LISTENERT = "notifyListeners";
	public static final String PARAM_UPLOAD_WITH_VALIDATION = "withValidation";
	public static final String PARAM_UPLOAD_UPDATE_DISUPDATABLE_PROPERTY = "updateDisupdatableProperty";
	public static final String PARAM_UPLOAD_INSERT_ENABLE_AUDIT_PROPERTY_SPECIFICATION = "insertEnableAuditPropertySpecification";
	public static final String PARAM_UPLOAD_PREFIX_OID = "prefixOid";
	public static final String PARAM_UPLOAD_COMMIT_LIMIT = "commitLimit";
	public static final String PARAM_UPLOAD_FORCE_UPDATE = "fourceUpdate";
	public static final String PARAM_UPLOAD_LOCALE = "locale";
	public static final String PARAM_UPLOAD_TIMEZONE = "timezone";

	public static final String RESULT_UPLOAD_INSERT = "insert";
	public static final String RESULT_UPLOAD_UPDATE = "update";
	public static final String RESULT_UPLOAD_DELETE = "delete";
	public static final String RESULT_UPLOAD_ERROR = "error";

	//entity/[defName]/
	//entity/[defName]/ multipart 一括アップロード（csv）

	@Override
	public String executeImpl(RequestContext request, String[] subPath) {
		if (subPath == null || subPath.length != 1) {
			throw new IllegalArgumentException("illegal path parameter:" + subPath);
		}

		if (isMultipart(request)) {
			doCsvUpload(request, subPath[0]);
		} else {
			doInsert(request, subPath[0]);
		}

		return CMD_EXEC_SUCCESS;
	}

	private boolean isMultipart(RequestContext request) {
		return (request instanceof WebRequestContext
				&& ((WebRequestContext)request).getValueMap() instanceof MultiPartParameterValueMap);
	}

	private void doInsert(RequestContext request, String defName) {

		checkPermission(defName, def -> def.getMetaData().isInsert());

		Entity e = (Entity) request.getAttribute(PARAM_ENTITY);
		e.setDefinitionName(defName);

		InsertOption option = new InsertOption();
		setInsertOptionWithParam(request, option);

		String oid = em.insert(e, option);
		request.setAttribute(RESULT_OID, oid);
	}

	/**
	 * リクエストパラメータに設定されたオプションをInsertOptionに設定
	 *
	 * @param request リクエスト
	 * @param option InsertOption
	 */
	private void setInsertOptionWithParam(RequestContext request, InsertOption option) {
		EntityWebApiService service = ServiceRegistry.getRegistry().getService(EntityWebApiService.class);

		if (service.isPermitEntityCrudApiOptionRole()) {
			String withValidation = request.getParam(PARAM_POST_WITH_VALIDATION);
			if (StringUtil.isNotBlank(withValidation)) {
				option.setWithValidation(Boolean.parseBoolean(withValidation));
			}
			String notifyListeners = request.getParam(PARAM_POST_NOTIFY_LISTENERS);
			if (StringUtil.isNotBlank(notifyListeners)) {
				option.setNotifyListeners(Boolean.parseBoolean(notifyListeners));
			}
			String enableAuditPropertySpecification = request.getParam(PARAM_POST_ENABLE_AUDIT_PROPERTY_SPECIFICATION);
			if (StringUtil.isNotBlank(enableAuditPropertySpecification)) {
				option.setEnableAuditPropertySpecification(Boolean.parseBoolean(enableAuditPropertySpecification));
			}
			String regenerateOid = request.getParam(PARAM_POST_REGENERATE_OID);
			if (StringUtil.isNotBlank(regenerateOid)) {
				option.setRegenerateOid(Boolean.parseBoolean(regenerateOid));
			}
			String regenerateAutoNumber = request.getParam(PARAM_POST_REGENERATE_AUTO_NUMBER);
			if (StringUtil.isNotBlank(regenerateAutoNumber)) {
				option.setRegenerateAutoNumber(Boolean.parseBoolean(regenerateAutoNumber));
			}
			String versionSpecified = request.getParam(PARAM_POST_VERSION_SPECIFIED);
			if (StringUtil.isNotBlank(versionSpecified)) {
				option.setVersionSpecified(Boolean.parseBoolean(versionSpecified));
			}
			String localized = request.getParam(PARAM_POST_LOCALIZED);
			if (StringUtil.isNotBlank(localized)) {
				option.setLocalized(Boolean.parseBoolean(localized));
			}
		}
	}

	private void doCsvUpload(RequestContext request, String defName) {

		checkPermission(defName, def -> def.getMetaData().isUpdate());

		UploadFileHandle file = request.getParamAsFile(PARAM_UPLOAD_FILE);
		String uniqueKey = StringUtil.stripToNull(request.getParam(PARAM_UPLOAD_UNIQUE_KEY));
		boolean async = request.getParam(PARAM_UPLOAD_ASYNC, Boolean.class, false);

		if (file == null) {
			throw new IllegalArgumentException("illegal parameter:" + PARAM_UPLOAD_FILE);
		}

		CsvUploadService service = ServiceRegistry.getRegistry().getService(CsvUploadService.class);
		EntityCsvImportService importService = ServiceRegistry.getRegistry().getService(EntityCsvImportService.class);
		EntityCsvImportOption option = new EntityCsvImportOption();
		setImportOptionWithParam(request, option);

		if (async) {
			if (option.isWithOption()) {
				//管理者用オプションあり
				try (InputStream is = file.getInputStream()) {
					List<String> excludeEntityNames = Arrays.asList("mtp.maintenance.Package", "mtp.maintenance.MetaDataTag");
					importService.asyncImportEntityData(defName, is, defName, option, excludeEntityNames);
				} catch (IOException e) {
					throw new SystemException(e);
				}
			} else {
				// 非同期アップロード(トランザクション分割なし)
				try (InputStream is = file.getInputStream()) {
					service.asyncUpload(is, file.getFileName(), defName, null, uniqueKey, false, false, false, null, null, TransactionType.ONCE, 0, true, true);
				} catch (IOException e) {
					throw new SystemException(e);
				}
			}
		} else {
			if (option.isWithOption()) {
				//管理者用オプションあり
				//MetaDataEntry
				String entityPath = EntityService.ENTITY_META_PATH + defName.replace(".", "/");
				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(entityPath);
				List<String> excludeEntityNames = Arrays.asList("mtp.maintenance.Package", "mtp.maintenance.MetaDataTag");
				try (InputStream is = file.getInputStream()) {
					EntityCsvImportResult ret = importService.importEntityData(defName, is, entry, option, null, null, excludeEntityNames);

					request.setAttribute(RESULT_UPLOAD_INSERT, ret.getInsertCount());
					request.setAttribute(RESULT_UPLOAD_UPDATE, ret.getUpdateCount());
					request.setAttribute(RESULT_UPLOAD_DELETE, ret.getDeleteCount());
					request.setAttribute(RESULT_UPLOAD_ERROR, ret.getErrorCount());
				} catch (IOException e) {
					throw new SystemException(e);
				}
			} else {
				// ファイルの整合性チェック
				try (InputStream is = file.getInputStream()) {
					service.validate(is, defName, true);
				} catch (IOException e) {
					throw new SystemException(e);
				}

				// 同期アップロード(トランザクション分割なし)
				try (InputStream is = file.getInputStream()) {
					CsvUploadStatus result = service.upload(is, defName, uniqueKey, false, false, false, null, null, TransactionType.ONCE, 0, true, true);
					if (result.getStatus() != TaskStatus.COMPLETED) {
						throw new EntityCsvException(result.getCode(), result.getMessage());
					}

					request.setAttribute(RESULT_UPLOAD_INSERT, result.getInsertCount());
					request.setAttribute(RESULT_UPLOAD_UPDATE, result.getUpdateCount());
					request.setAttribute(RESULT_UPLOAD_DELETE, result.getDeleteCount());
				} catch (IOException e) {
					throw new SystemException(e);
				}
			}
		}
	}

	/**
	 * リクエストパラメータに設定されたオプションをEntityCsvImportOptionに設定
	 *
	 * @param request リクエスト
	 * @param option EntityCsvImportOption
	 */
	private void setImportOptionWithParam(RequestContext request, EntityCsvImportOption option) {
		EntityWebApiService service = ServiceRegistry.getRegistry().getService(EntityWebApiService.class);

		if (service.isPermitEntityCrudApiOptionRole()) {
			String truncate = request.getParam(PARAM_UPLOAD_TRUNCATE);
			if (StringUtil.isNotBlank(truncate)) {
				option.setTruncate(Boolean.parseBoolean(truncate));
				option.withOption();
			}
			String bulkUpdate = request.getParam(PARAM_UPLOAD_BULK_UPDATE);
			if (StringUtil.isNotBlank(bulkUpdate)) {
				option.setBulkUpdate(Boolean.parseBoolean(bulkUpdate));
				option.withOption();
			}
			String errorSkip = request.getParam(PARAM_UPLOAD_ERROR_SKIP);
			if (StringUtil.isNotBlank(errorSkip)) {
				option.setErrorSkip(Boolean.parseBoolean(errorSkip));
				option.withOption();
			}
			String ignoreNotExistsProperty = request.getParam(PARAM_UPLOAD_IGNORE_NOT_EXIST_PROPERTY);
			if (StringUtil.isNotBlank(ignoreNotExistsProperty)) {
				option.setIgnoreNotExistsProperty(Boolean.parseBoolean(ignoreNotExistsProperty));
				option.withOption();
			}
			String notifyListeners = request.getParam(PARAM_UPLOAD_NOTIFY_LISTENERT);
			if (StringUtil.isNotBlank(notifyListeners)) {
				option.setNotifyListeners(Boolean.parseBoolean(notifyListeners));
				option.withOption();
			}
			String withValidation = request.getParam(PARAM_UPLOAD_WITH_VALIDATION);
			if (StringUtil.isNotBlank(withValidation)) {
				option.setWithValidation(Boolean.parseBoolean(withValidation));
				option.withOption();
			}
			String updateDisupdatableProperty = request.getParam(PARAM_UPLOAD_UPDATE_DISUPDATABLE_PROPERTY);
			if (StringUtil.isNotBlank(updateDisupdatableProperty)) {
				option.setUpdateDisupdatableProperty(Boolean.parseBoolean(updateDisupdatableProperty));
				option.withOption();
			}
			String insertEnableAuditPropertySpecification = request.getParam(PARAM_UPLOAD_INSERT_ENABLE_AUDIT_PROPERTY_SPECIFICATION);
			if (StringUtil.isNotBlank(insertEnableAuditPropertySpecification)) {
				option.setInsertEnableAuditPropertySpecification(Boolean.parseBoolean(insertEnableAuditPropertySpecification));
				option.withOption();
			}
			String prefixOid = request.getParam(PARAM_UPLOAD_PREFIX_OID);
			if (StringUtil.isNotBlank(prefixOid)) {
				option.setPrefixOid(prefixOid);
				option.withOption();
			}
			String commitLimit = request.getParam(PARAM_UPLOAD_COMMIT_LIMIT);
			if (StringUtil.isNotBlank(commitLimit)) {
				try {
					option.setCommitLimit(Integer.parseInt(commitLimit));
					option.withOption();
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("illegal parameter:" + PARAM_UPLOAD_COMMIT_LIMIT);
				}
			}
			String fourceUpdate = request.getParam(PARAM_UPLOAD_FORCE_UPDATE);
			if (StringUtil.isNotBlank(fourceUpdate)) {
				option.setFourceUpdate(Boolean.parseBoolean(fourceUpdate));
				option.withOption();
			}
			String uniqueKey = request.getParam(PARAM_UPLOAD_UNIQUE_KEY);
			if (StringUtil.isNotBlank(uniqueKey)) {
				option.setUniqueKey(uniqueKey);
				//option.withOption(); 既存のオプションでも有効なため、他の項目で判定
			}
			String locale = request.getParam(PARAM_UPLOAD_LOCALE);
			if (StringUtil.isNotBlank(locale)) {
				option.setLocale(locale);
				option.withOption();
			}
			String timezone = request.getParam(PARAM_UPLOAD_TIMEZONE);
			if (StringUtil.isNotBlank(timezone)) {
				option.setTimezone(timezone);
				option.withOption();
			}
		}
	}
}
