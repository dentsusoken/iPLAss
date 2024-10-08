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

package org.iplass.gem.command.generic.upload;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.detail.DetailCommandBase;
import org.iplass.gem.command.generic.detail.DetailCommandContext;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.action.TokenCheck;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.impl.entity.fileport.EntityCsvException;
import org.iplass.mtp.impl.fileport.EntityFileType;
import org.iplass.mtp.impl.fileport.EntityFileUploadOption;
import org.iplass.mtp.impl.fileport.EntityFileUploadService;
import org.iplass.mtp.impl.fileport.EntityFileUploadStatus;
import org.iplass.mtp.impl.fileport.TransactionType;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormCsvUploadInterrupter;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.CsvUploadTransactionType;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.FileSupportType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CSVアップロードアップロード用コマンド
 *
 * @author lis3wg
 */
@ActionMapping(name = EntityFileUploadCommand.ACTION_NAME, displayName = "ファイルアップロード", paramMapping = {
		@ParamMapping(name = Constants.DEF_NAME, mapFrom = "${0}", condition = "subPath.length==1"),
		@ParamMapping(name = Constants.VIEW_NAME, mapFrom = "${0}", condition = "subPath.length==2"),
		@ParamMapping(name = Constants.DEF_NAME, mapFrom = "${1}", condition = "subPath.length==2"), }, result = {
				@Result(status = Constants.CMD_EXEC_SUCCESS, type = Type.JSP, value = Constants.CMD_RSLT_JSP_FILE_UPLOAD_RESULT, templateName = "gem/generic/upload/fileUploadResult", layoutActionName = Constants.LAYOUT_NORMAL_ACTION),
				@Result(status = Constants.CMD_EXEC_SUCCESS_ASYNC, type = Type.TEMPLATE, value = Constants.TEMPLATE_FILE_UPLOAD),
				@Result(status = Constants.CMD_EXEC_FAILURE, type = Type.TEMPLATE, value = Constants.TEMPLATE_FILE_UPLOAD),
				@Result(status = Constants.CMD_EXEC_ERROR_VIEW, type = Type.TEMPLATE, value = Constants.TEMPLATE_COMMON_ERROR, layoutActionName = Constants.LAYOUT_NORMAL_ACTION) }, tokenCheck = @TokenCheck)
@CommandClass(name = "gem/generic/upload/EntityFileUploadCommand", displayName = "ファイルアップロード")
public final class EntityFileUploadCommand extends DetailCommandBase {

	private static Logger logger = LoggerFactory.getLogger(EntityFileUploadCommand.class);

	public static final String ACTION_NAME = "gem/generic/upload/bin";

	private GemConfigService gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);

	@Override
	public String execute(final RequestContext request) {

		DetailCommandContext context = getContext(request);

		String uniqueKey = request.getParam("uniqueKey");
		String updateTargetVersion = request.getParam("updateTargetVersion");
		UploadFileHandle file = request.getParamAsFile("filePath");

		EntityDefinition ed = context.getEntityDefinition();
		String viewName = context.getViewName();

		// viewNameから一括ロールバックか件数単位でコミットかを取得(csvUploadTransactionType)
		String defName = ed.getName();
		EntityView view = context.getEntityView();
		SearchFormView searchFormView = FormViewUtil.getSearchFormView(ed, view, viewName);

		if (searchFormView == null) {
			request.setAttribute(Constants.MESSAGE,
					GemResourceBundleUtil.resourceString("command.generic.upload.CSVUploadCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		FileSupportType fileSupportType = null;
		if (searchFormView != null) {
			fileSupportType = searchFormView.getCondSection().getFileSupportType();
		}
		// 未指定の場合は、GemConfigServiceから取得
		if (fileSupportType == null) {
			GemConfigService gemConfigService = ServiceRegistry.getRegistry().getService(GemConfigService.class);
			fileSupportType = gemConfigService.getFileSupportType();
		}

		// エラーの場合の再表示のためリクエストに設定
		request.setAttribute(Constants.ENTITY_DEFINITION, ed);
		request.setAttribute("detailFormView", context.getView());
		request.setAttribute("searchFormView", searchFormView);
		request.setAttribute(Constants.SEARCH_COND, context.getSearchCond());
		request.setAttribute("fileSupportType", fileSupportType);
		request.setAttribute("requiredProperties", CsvUploadUtil.getRequiredProperties(ed));
		request.setAttribute("customColumnNameMap", getCustomColumnNameMap(ed, searchFormView));

		int commitLimit = gcs.getCsvUploadCommitCount();
		boolean isDenyInsert = searchFormView.getCondSection().isCsvUploadDenyInsert();
		boolean isDenyUpdate = searchFormView.getCondSection().isCsvUploadDenyUpdate();
		boolean isDenyDelete = searchFormView.getCondSection().isCsvUploadDenyDelete();
		Set<String> insertProperties = searchFormView.getCondSection().getCsvUploadInsertPropertiesSet();
		Set<String> updateProperties = searchFormView.getCondSection().getCsvUploadUpdatePropertiesSet();
		CsvUploadTransactionType csvUploadTransactionType = searchFormView.getCondSection()
				.getCsvUploadTransactionType();
		TargetVersion targetVersion = null;
		if (ed.getVersionControlType() == VersionControlType.NONE) {
			// バージョン管理対象外のEntityの場合
			if (searchFormView.getCondSection().isCanCsvUploadTargetVersionSelectForNoneVersionedEntity()) {
				// 画面から指定
				targetVersion = StringUtil.isNotEmpty(updateTargetVersion)
						&& updateTargetVersion.equals(TargetVersion.SPECIFIC.name()) ? TargetVersion.SPECIFIC
								: TargetVersion.CURRENT_VALID;
			} else {
				// 設定されていたら指定
				targetVersion = searchFormView.getCondSection().getCsvUploadTargetVersionForNoneVersionedEntity();
			}
		}
		EntityFileType entityFileType = getEntityFileType(request, searchFormView.getCondSection());

		EntityFileUploadService service = ServiceRegistry.getRegistry().getService(EntityFileUploadService.class);

		if (file == null) {
			request.setAttribute(Constants.MESSAGE,
					GemResourceBundleUtil.resourceString("command.generic.upload.CSVUploadCommand.selectFileMsg"));
			return Constants.CMD_EXEC_FAILURE;
		}

		if (gcs.isCsvUploadAsync()) {
			// 非同期アップロード(パラメータとしてView名を設定)

			try (InputStream is = file.getInputStream()) {
				EntityFileUploadOption option = new EntityFileUploadOption().entityFileType(entityFileType)
						.uniqueKey(uniqueKey).denyInsert(isDenyInsert).denyUpdate(isDenyUpdate).denyDelete(isDenyDelete)
						.insertProperties(insertProperties).updateProperties(updateProperties)
						.transactionType(toTransactionType(csvUploadTransactionType)).commitLimit(commitLimit)
						.withReferenceVersion(gcs.isCsvDownloadReferenceVersion())
						.deleteSpecificVersion(searchFormView.isDeleteSpecificVersion())
						.updateTargetVersionForNoneVersionedEntity(targetVersion)
						.interrupterClassName(searchFormView.getCondSection().getCsvUploadInterrupterName());

				service.asyncUpload(is, file.getFileName(), defName, viewName, option);

				return Constants.CMD_EXEC_SUCCESS_ASYNC;

			} catch (ApplicationException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				request.setAttribute(Constants.MESSAGE, StringUtil.escapeHtml(e.getMessage()).replace("\n", "<br/>"));
				return Constants.CMD_EXEC_FAILURE;
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				request.setAttribute(Constants.MESSAGE,
						GemResourceBundleUtil.resourceString("command.generic.upload.CSVUploadCommand.errReadFile"));
				return Constants.CMD_EXEC_FAILURE;
			}

		} else {
			// 同期アップロード

			try (InputStream is = file.getInputStream()) {
				service.validate(is, entityFileType, defName, gcs.isCsvDownloadReferenceVersion(),
						searchFormView.getCondSection().getCsvUploadInterrupterName());
			} catch (EntityCsvException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				request.setAttribute(Constants.MESSAGE, StringUtil.escapeHtml(e.getMessage()).replace("\n", "<br/>"));
				return Constants.CMD_EXEC_FAILURE;
			} catch (ApplicationException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				request.setAttribute(Constants.MESSAGE, StringUtil.escapeHtml(e.getMessage()).replace("\n", "<br/>"));
				return Constants.CMD_EXEC_FAILURE;
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				request.setAttribute(Constants.MESSAGE,
						GemResourceBundleUtil.resourceString("command.generic.upload.CSVUploadCommand.errReadFile"));
				return Constants.CMD_EXEC_FAILURE;
			}

			try (InputStream is = file.getInputStream()) {
				EntityFileUploadOption option = new EntityFileUploadOption().entityFileType(entityFileType)
						.uniqueKey(uniqueKey).denyInsert(isDenyInsert).denyUpdate(isDenyUpdate).denyDelete(isDenyDelete)
						.insertProperties(insertProperties).updateProperties(updateProperties)
						.transactionType(toTransactionType(csvUploadTransactionType)).commitLimit(commitLimit)
						.withReferenceVersion(gcs.isCsvDownloadReferenceVersion())
						.deleteSpecificVersion(searchFormView.isDeleteSpecificVersion())
						.updateTargetVersionForNoneVersionedEntity(targetVersion)
						.interrupterClassName(searchFormView.getCondSection().getCsvUploadInterrupterName());

				EntityFileUploadStatus result = service.upload(is, defName, option);

				request.setAttribute(Constants.MESSAGE,
						result.getMessage() != null ? StringUtil.escapeHtml(result.getMessage()).replace("\n", "<br/>")
								: null);
				request.setAttribute("insertCount", result.getInsertCount());
				request.setAttribute("updateCount", result.getUpdateCount());
				request.setAttribute("deleteCount", result.getDeleteCount());

				if (result.getStatus() == TaskStatus.COMPLETED) {
					return Constants.CMD_EXEC_SUCCESS;
				} else {
					return Constants.CMD_EXEC_FAILURE;
				}

			} catch (ApplicationException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				request.setAttribute(Constants.MESSAGE, StringUtil.escapeHtml(e.getMessage()).replace("\n", "<br/>"));
				return Constants.CMD_EXEC_FAILURE;
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				request.setAttribute(Constants.MESSAGE,
						GemResourceBundleUtil.resourceString("command.generic.upload.CSVUploadCommand.errReadFile"));
				return Constants.CMD_EXEC_FAILURE;
			}

		}
	}

	private EntityFileType getEntityFileType(final RequestContext request, SearchConditionSection condition) {
		FileSupportType fileSupportType = null;
		if (condition != null) {
			fileSupportType = condition.getFileSupportType();
		}

		// 未指定の場合は、GemConfigServiceから取得
		if (fileSupportType == null) {
			fileSupportType = gcs.getFileSupportType();
		}

		if (fileSupportType == FileSupportType.SPECIFY) {
			String specifyType = request.getParam(Constants.FILE_SUPPORT_TYPE);
			if (FileSupportType.CSV.name().equals(specifyType)) {
				fileSupportType = FileSupportType.CSV;
			} else if (FileSupportType.EXCEL.name().equals(specifyType)) {
				fileSupportType = FileSupportType.EXCEL;
			} else {
				fileSupportType = FileSupportType.CSV;
			}
		}

		if (fileSupportType == FileSupportType.CSV) {
			return EntityFileType.CSV;
		} else {
			return EntityFileType.EXCEL;
		}
	}

	private TransactionType toTransactionType(CsvUploadTransactionType tt) {
		if (tt == null) {
			return null;
		}
		switch (tt) {
		case ONCE:
			return TransactionType.ONCE;
		case DIVISION:
			return TransactionType.DIVISION;
		default:
			return null;
		}
	}

	private Map<String, String> getCustomColumnNameMap(EntityDefinition ed, SearchFormView searchFormView) {

		if (searchFormView != null) {
			if (StringUtil.isNotEmpty(searchFormView.getCondSection().getCsvUploadInterrupterName())) {
				UtilityClassDefinitionManager ucdm = ManagerLocator.getInstance()
						.getManager(UtilityClassDefinitionManager.class);
				SearchFormCsvUploadInterrupter interrupter = null;
				try {
					interrupter = ucdm.createInstanceAs(SearchFormCsvUploadInterrupter.class,
							searchFormView.getCondSection().getCsvUploadInterrupterName());
					return interrupter.columnNameMap(ed);
				} catch (ClassNotFoundException e) {
					logger.error(
							searchFormView.getCondSection().getCsvUploadInterrupterName() + " can not instantiate.", e);
					throw new ApplicationException(GemResourceBundleUtil
							.resourceString("command.generic.upload.CSVUploadCommand.internalErr"));
				}

			}
		}

		return null;
	}
}
