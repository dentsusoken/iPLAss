/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.gem.command.generic.refcombo;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.HasDisplayScriptBindings;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * 参照コンボ設定取得処理
 */
@WebApi(
		name=GetReferenceComboSettingCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={GetReferenceComboSettingCommand.RESULT_DATA_NAME},
		checkXRequestedWithHeader=true
	)
@CommandClass(name=GetReferenceComboSettingCommand.CMD_NAME, displayName="参照コンボ設定取得")
public class GetReferenceComboSettingCommand implements Command, HasDisplayScriptBindings{

	public static final String CMD_NAME = "gem/generic/refcombo/GetReferenceComboSettingCommand";
	public static final String WEBAPI_NAME = "gem/generic/refcombo/getComboSetting";

	/** 返却値のKEY名 */
	public static final String RESULT_DATA_NAME = "setting";

	private EntityViewManager evm = null;

	public GetReferenceComboSettingCommand() {
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);
		String propName = request.getParam(Constants.PROP_NAME);
		String viewType = request.getParam(Constants.VIEW_TYPE);

		Entity entity = getBindingEntity(request);
		//Editor取得
		PropertyEditor editor = evm.getPropertyEditor(defName, viewType, viewName, propName, entity);
		ReferencePropertyEditor rpe = null;
		if (editor instanceof ReferencePropertyEditor) {
			rpe = (ReferencePropertyEditor)editor;
		}
		if (rpe == null) {
			return "NOT_DEFINED_EDITOR";
		}

		request.setAttribute(RESULT_DATA_NAME, rpe.getReferenceComboSetting());
		return Constants.CMD_EXEC_SUCCESS;
	}

}
