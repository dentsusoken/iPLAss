/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.common;

import org.iplass.gem.command.generic.HasDisplayScriptBindings;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(
		name=GetAutocompletionValueCommand.WEBAPI_NAME,
		displayName="自動補完値取得",
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="params", parameterType=AutocompletionParam.class),
		results={"value"},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/common/GetAutocompletionValueCommand", displayName="自動補完値取得")
public class GetAutocompletionValueCommand implements Command, HasDisplayScriptBindings {

	public static final String WEBAPI_NAME = "gem/generic/common/getAutocompletionValue";

	private EntityViewManager evm;

	public GetAutocompletionValueCommand() {
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
	}

	@Override
	public String execute(RequestContext request) {
		AutocompletionParam param = (AutocompletionParam) request.getAttribute("params");

		Entity entity = getBindingEntity(request);
		Object val = evm.getAutocompletionValue(param.getDefName(), param.getViewName(), param.getViewType(),
				param.getPropName(), param.getAutocompletionKey(), param.getReferenceSectionIndex(), param.getParams(), param.getCurrentValue(), entity);

		request.setAttribute("value", val);
		return null;
	}

	@Override
	public Entity getBindingEntity(RequestContext request) {
		AutocompletionParam param = (AutocompletionParam) request.getAttribute("params");
		AutocompletionEntityParam entity = param.getEntity();
		if (entity != null) {
			Entity e = new GenericEntity(param.getDefName());
			e.setOid(entity.getOid());
			e.setVersion(Long.valueOf(entity.getVersion()));
			return e;
		}
		return null;
	}
}
