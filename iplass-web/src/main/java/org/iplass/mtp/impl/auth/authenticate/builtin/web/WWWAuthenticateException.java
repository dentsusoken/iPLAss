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
package org.iplass.mtp.impl.auth.authenticate.builtin.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WWWAuthenticateException extends WebApplicationException {
	private static final long serialVersionUID = -5100314518573818114L;

	public WWWAuthenticateException(String scheme, String realm, String errorDescription) {
		super(errorDescription, buildResponse(scheme, realm, errorDescription));
	}
	
	private static Response buildResponse(String scheme, String realm, String errorDescription) {
		StringBuilder resMsg = new StringBuilder();
		resMsg.append(scheme);
		resMsg.append(" ");
		if (realm == null) {
			resMsg.append("realm=\"\"");
		} else {
			resMsg.append("realm=\"").append(realm).append("\"");
		}
		
		if (errorDescription == null) {
			errorDescription = "";
		}

		return Response.status(401).header("WWW-Authenticate", resMsg.toString()).entity(errorDescription).build();
	}

}
