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

package org.iplass.gem.command.language;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.web.i18n.LangSelector;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

@CommandClass(name="gem/webapi/SelectLanguageCommand", displayName="言語選択")
@WebApi(name="gem/webapi/SelectLanguageCommand",
	accepts={RequestType.REST_JSON},
	methods={MethodType.GET, MethodType.POST},
	restJson=@RestJson(parameterName="param"),
	checkXRequestedWithHeader=true
)
public final class SelectLanguageCommand implements Command {

	@Override
	public String execute(RequestContext request) {

		String language = request.getParam("language");
		request.getSession().setAttribute(LangSelector.LANG_ATTRIBUTE_NAME, language);
		request.setAttribute(WebApiRequestConstants.DEFAULT_RESULT, language);

		return "SUCCESS";
	}
}
