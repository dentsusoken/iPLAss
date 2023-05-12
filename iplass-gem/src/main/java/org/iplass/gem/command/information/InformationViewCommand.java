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

package org.iplass.gem.command.information;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.impl.view.top.parts.MetaInformationParts.InformationPartsHandler;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.top.TopViewDefinitionManager;
import org.iplass.mtp.view.top.parts.InformationParts;

/**
 * お知らせ詳細表示用のコマンド
 * @author lis3wg
 */
@ActionMapping(
	name=InformationViewCommand.ACTION_NAME,
	displayName="お知らせ詳細表示",
	result= {
		@Result(status=Constants.CMD_EXEC_SUCCESS,
			type=Type.JSP,
			value=Constants.CMD_RSLT_JSP_INFO_VIEW,
			templateName="gem/information/view",
			layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
		@Result(status=Constants.CMD_EXEC_ERROR_NODATA, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
			layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
		@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
			layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
	}
)
@CommandClass(name="gem/information/InformationDetailViewCommand", displayName="お知らせ詳細表示")
public final class InformationViewCommand implements Command {

	public static final String ACTION_NAME = "gem/information/view";

	/** EntityManager */
	private EntityManager em = ManagerLocator.manager(EntityManager.class);
	/** TopViewDefinitionManager */
	private TopViewDefinitionManager tvdm = ManagerLocator.manager(TopViewDefinitionManager.class);

	/**
	 * コンストラクタ
	 */
	public InformationViewCommand() {
		super();
	}

	@Override
	public String execute(RequestContext request) {

		//必要なパラメータ取得
		String oid = request.getParam(Constants.OID);
		Long version = request.getParamAsLong(Constants.VERSION);

		//検索
		Entity entity = null;
		if (oid != null) {
			entity = em.load(oid, version ,InformationListCommand.INFORMATION_ENTITY, new LoadOption(false, false));
		}
		if (entity == null) {
			request.setAttribute(Constants.MESSAGE, GemResourceBundleUtil.resourceString("information.view.noPermission"));
			return Constants.CMD_EXEC_ERROR_NODATA;
		}

		//パーツ
		InformationParts infoParts = tvdm.getRequestTopViewParts(InformationParts.class);
		if (infoParts == null) {
			request.setAttribute(Constants.MESSAGE, GemResourceBundleUtil.resourceString("information.view.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		//カスタムスタイル
		String customStyle = null;
		if (StringUtil.isNotEmpty(infoParts.getDetailCustomStyle())) {
			customStyle = tvdm.getRequestTopViewPartsHandler(InformationPartsHandler.class).getDetailCustomStyle(entity);
		} else {
			customStyle = "";
		}

		request.setAttribute(Constants.DATA_ENTITY, entity);
		request.setAttribute(Constants.INFO_SETTING, infoParts);
		request.setAttribute(Constants.INFO_DETAIL_CUSTOM_STYLE, customStyle);

		return Constants.CMD_EXEC_SUCCESS;
	}

}
