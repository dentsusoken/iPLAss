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

package org.iplass.gem;

import org.iplass.gem.command.auth.LoginCommand;
import org.iplass.gem.command.auth.ReAuthCommand;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.web.LoginUrlSelector;

public class GemLoginUrlSelector implements LoginUrlSelector {

	@Override
	public String getLoginActionName(RequestContext request, String path) {
		return LoginCommand.ACTION_VIEW_LOGIN;
	}

	@Override
	public String getReAuthActionName(RequestContext request, String path) {
		return ReAuthCommand.ACTION_VIEW_RE_AUTH;
	}

}
