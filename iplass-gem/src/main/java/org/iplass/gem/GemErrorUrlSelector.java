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

package org.iplass.gem;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.auth.NoPermissionException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.web.ErrorUrlSelector;

public class GemErrorUrlSelector implements ErrorUrlSelector {

	@Override
	public String getErrorTemplateName(Throwable exception, RequestContext request, String path) {
		if (exception instanceof NoPermissionException) {
			return Constants.TEMPLATE_PERMISSION_ERROR;
		}
		if (exception instanceof ApplicationException) {
			return Constants.TEMPLATE_ERROR;
		}

		return Constants.TEMPLATE_SYSTEM_ERROR;
	}

}
