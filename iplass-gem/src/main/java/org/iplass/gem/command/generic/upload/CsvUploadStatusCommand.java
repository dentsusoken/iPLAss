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

package org.iplass.gem.command.generic.upload;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.ViewUtil;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.csv.CsvUploadService;
import org.iplass.mtp.impl.csv.CsvUploadStatus;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.FormView;
import org.iplass.mtp.web.template.TemplateUtil;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * CSVアップロードステータス用コマンド
 * @author lis9cb
 *
 */
@WebApi(
		name=CsvUploadStatusCommand.WEBAPI_NAME,
		displayName=" CSVアップロードステータス確認",
		accepts={RequestType.REST_FORM, RequestType.REST_JSON, RequestType.REST_XML},
		methods=MethodType.POST,
		results={WebApiRequestConstants.DEFAULT_RESULT}
	)
@CommandClass(
	name = "gem/generic/upload/CsvUploadStatusCommand",
	displayName = "CSVアップロードステータス確認"
)
public final class CsvUploadStatusCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/upload/status";

	private EntityDefinitionManager edm = null;

	public CsvUploadStatusCommand() {
		edm = ManagerLocator.manager(EntityDefinitionManager.class);
	}

	@Override
	public String execute(RequestContext request) {

		LocaleFormat lf = ExecuteContext.getCurrentContext().getLocaleFormat();
		SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(lf.getOutputDatetimeSecFormat(), true);

		// CSVアップロードステータスサービス呼び出し
		CsvUploadService service = ServiceRegistry.getRegistry().getService(CsvUploadService.class);
		List<CsvUploadStatus> statusList = service.getStatus();

		List<CsvUploadStatusData> resultList = new ArrayList<CsvUploadStatusData>();
		Map<String, String> dispNames = new HashMap<>();
		for (CsvUploadStatus status : statusList) {
			CsvUploadStatusData result = new CsvUploadStatusData();

			result.setInsertCount(status.getInsertCount());
			result.setUpdateCount(status.getUpdateCount());
			result.setDeleteCount(status.getDeleteCount());
			result.setFileName(status.getFileName());
			result.setUploadDate(sdf.format(status.getUploadDateTime()));
			result.setStatus(status.getStatus());
			result.setStatusLabel(GemResourceBundleUtil.resourceString("generic.csvUploadAsyncResult.status." + status.getStatus().name()));
			result.setMessage(status.getMessage() != null ? StringUtil.escapeHtml(status.getMessage()).replace("\n", "<br/>") : null);

			String defName = status.getDefName();
			String viewName = status.getParameter();	//parameterにViewNameを保持している

			String dispNameKey = defName + "_" + (viewName != null ? viewName : "");
			if (dispNames.containsKey(dispNameKey)) {
				result.setTargetDisplayName(dispNames.get(dispNameKey));
			} else {
				String dispName = getDisplayName(defName, viewName);
				dispNames.put(dispNameKey, dispName);
				result.setTargetDisplayName(dispName);
			}

			resultList.add(result);
		}

		request.setAttribute(WebApiRequestConstants.DEFAULT_RESULT, resultList);

		return Constants.CMD_EXEC_SUCCESS;
	}

	private String getDisplayName(String defName, String viewName) {

		EntityDefinition ed = edm.get(defName);
		if (ed == null) {
			return "";
		}

		FormView formView = ViewUtil.getFormView(defName, viewName, true);

		if (formView != null) {
			return TemplateUtil.getMultilingualString(formView.getTitle(), formView.getLocalizedTitleList(),
					ed.getDisplayName(), ed.getLocalizedDisplayNameList());
		} else {
			return TemplateUtil.getMultilingualString(ed.getDisplayName(), ed.getLocalizedDisplayNameList());
		}
	}

}
