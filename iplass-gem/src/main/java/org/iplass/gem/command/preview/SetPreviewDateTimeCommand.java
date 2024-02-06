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

package org.iplass.gem.command.preview;

import java.sql.Timestamp;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.preview.PreviewHandler;
import org.iplass.mtp.tenant.web.TenantWebInfo;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.TopViewDefinitionManager;
import org.iplass.mtp.view.top.parts.PreviewDateParts;
import org.iplass.mtp.web.template.TemplateUtil;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * プレビュー日付設定Command（WebAPI）。
 *
 * @author K.Higuchi
 *
 */
@WebApi(
		name=SetPreviewDateTimeCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results=SetPreviewDateTimeCommand.KEY_DATE_TIME,
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/preview/SetPreviewDateTimeCommand", displayName="プレビュー日時変更")
public final class SetPreviewDateTimeCommand implements Command {

	public static final String WEBAPI_NAME = "gem/preview/setPreviewDateTime";

	public static final String KEY_DATE_TIME = "dateTime";

	private PreviewHandler preview = new PreviewHandler();

	@Override
	public String execute(RequestContext request) {

		TenantWebInfo webInfo = WebUtil.getTenantWebInfo(TemplateUtil.getTenant());
		if (!webInfo.isUsePreview()) {
			throw new ApplicationException("not allowed to set preview date.");
		}

		TopViewDefinitionManager tvdm = ManagerLocator.manager(TopViewDefinitionManager.class);
		TopViewDefinition topView = tvdm.getRequestTopView();
		if (topView != null) {
			PreviewDateParts previewDateParts = tvdm.getTopViewParts(topView, PreviewDateParts.class);
			if (previewDateParts != null) {
				//パーツがある場合はパーツ設定
				if (!previewDateParts.isUsePreviewDate()) {
					throw new ApplicationException("not allowed to set preview date.");
				}
			}
		}

		Timestamp ts = request.getParamAsTimestamp(KEY_DATE_TIME, "yyyyMMddHHmmss");
		preview.setPreviewDate(ts, request);
		return "SUCCESS";
	}

}
