/*
 * Copyright 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.gem.command.preview;

import java.sql.Timestamp;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.preview.PreviewHandler;
import org.iplass.mtp.tenant.web.TenantWebInfo;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.TopViewDefinitionManager;
import org.iplass.mtp.view.top.parts.PreviewDateParts;
import org.iplass.mtp.web.template.TemplateUtil;

@ActionMapping(
	name=PreviewDateViewCommand.ACTION_NAME,
	clientCacheType=ClientCacheType.NO_CACHE,
	displayName="プレビュー日付画面表示",
	result={
		@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
				value="/jsp/gem/layout/previewDate.jsp",
				templateName="gem/preview/previewDate",
				layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
	}
)
@CommandClass(name="gem/preview/PreviewDateViewCommand", displayName="プレビュー日付画面表示")
public final class PreviewDateViewCommand implements Command {

	public static final String ACTION_NAME = "gem/preview/index";

	public static final String TITLE = "previewDateTitle";
	public static final String PREVIEW_DATE = "previewDate";

	@Override
	public String execute(RequestContext request) {

		TenantWebInfo webInfo = WebUtil.getTenantWebInfo(TemplateUtil.getTenant());
		if (!webInfo.isUsePreview()) {
			throw new ApplicationException("not allowed to set preview date.");
		}

		String titlePreviewDate = null;

		TopViewDefinitionManager tvdm = ManagerLocator.manager(TopViewDefinitionManager.class);
		TopViewDefinition topView = tvdm.getRequestTopView();
		if (topView != null) {
			PreviewDateParts previewDateParts = tvdm.getTopViewParts(topView, PreviewDateParts.class);
			if (previewDateParts != null) {
				//パーツがある場合はパーツ設定
				if (!previewDateParts.isUsePreviewDate()) {
					throw new ApplicationException("not allowed to set preview date.");
				}

				titlePreviewDate = TemplateUtil.getMultilingualString(
						previewDateParts.getTitle(), previewDateParts.getLocalizedTitleList());
			}

		}

		//タイトルが未指定の場合はデフォルト
		if (titlePreviewDate == null) {
			titlePreviewDate = GemResourceBundleUtil.resourceString("layout.header.previewDate");
		}

		PreviewHandler handler = new PreviewHandler();
		Timestamp previewDate = handler.getPreviewDate(request);

		request.setAttribute(TITLE, titlePreviewDate);
		request.setAttribute(PREVIEW_DATE, previewDate);

		return Constants.CMD_EXEC_SUCCESS;

	}

}
