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

package org.iplass.gem.command.generic.detail;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

/**
 * データアンロックコマンド
 * @author lis3wg
 */
@WebApi(
	name=UnlockCommand.WEBAPI_NAME,
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	tokenCheck=@WebApiTokenCheck(consume=false, useFixedToken=true),
	results={Constants.LOCK_RESULT},
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/detail/UnlockCommand", displayName="データアンロック")
public final class UnlockCommand extends DetailCommandBase {

	public static final String WEBAPI_NAME = "gem/generic/detail/unlock";

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String oid = request.getParam(Constants.OID);

		boolean lockResult = em.unlockByUser(oid, defName);

		request.setAttribute(Constants.LOCK_RESULT, lockResult);
		return Constants.CMD_EXEC_SUCCESS;
	}

}
