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
import org.iplass.gem.command.generic.detail.DetailCommandBase;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;

/**
 * お知らせ詳細表示用のコマンド
 * @author lis3wg
 */
@ActionMapping(
	name=InformationViewCommand.ACTION_NAME,
	displayName="お知らせ詳細表示",
	result=@Result(type=Type.JSP,
			value=Constants.CMD_RSLT_JSP_INFO_VIEW,
			templateName="gem/information/view",
			layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
)
@CommandClass(name="gem/information/InformationDetailViewCommand", displayName="お知らせ詳細表示")
public final class InformationViewCommand extends DetailCommandBase {

	public static final String ACTION_NAME = "gem/information/view";

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

		//画面で利用するデータ設定
		request.setAttribute(Constants.DATA_ENTITY, loadEntityWithoutReference(oid, version, InformationListCommand.INFORMATION_ENTITY));
		return Constants.CMD_EXEC_SUCCESS;
	}

}
