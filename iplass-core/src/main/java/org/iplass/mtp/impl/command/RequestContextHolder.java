/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.command;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.core.ExecuteContext;

public class RequestContextHolder {

	public static final String EXEC_CONTEXT_NAME ="requestContext";
	
	public static RequestContext getCurrent() {
		return (RequestContext) ExecuteContext.getCurrentContext().getAttribute(EXEC_CONTEXT_NAME);
	}
	
	public static void setCurrent(RequestContext requestContext) {
		if (requestContext == null) {
			ExecuteContext.getCurrentContext().removeAttribute(EXEC_CONTEXT_NAME);
		} else {
			ExecuteContext.getCurrentContext().setAttribute(EXEC_CONTEXT_NAME, requestContext, true);
		}
	}

}
