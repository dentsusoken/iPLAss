/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
 * @deprecated use {@link GetReferenceComboSettingCommand}
 */
@WebApi(
		name=GetEditorCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={"editor"},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/refcombo/GetEditorCommand", displayName="プロパティエディタ取得")
@Deprecated
public final class GetEditorCommand implements Command, HasDisplayScriptBindings{

	public static final String CMD_NAME = "gem/generic/refcombo/GetEditorCommand";
	public static final String WEBAPI_NAME = "gem/generic/refcombo/getEditor";

	private EntityViewManager evm = null;

	public GetEditorCommand() {
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

		request.setAttribute("editor", rpe);
		return "OK";
	}

}
