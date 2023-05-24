/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;

public interface AutoLoginHandler {
	
	public AutoLoginInstruction handle(RequestContext req, boolean isLogined, UserContext user);
	
	public default Exception handleException(AutoLoginInstruction ali, ApplicationException e, RequestContext req, boolean isLogined, UserContext user) {
		return null;
	}
	
	public default void handleSuccess(AutoLoginInstruction ali, RequestContext req, UserContext user) {
	}
	
	public default void inited(AuthService service, AuthenticationProvider provider) {
	}
}
