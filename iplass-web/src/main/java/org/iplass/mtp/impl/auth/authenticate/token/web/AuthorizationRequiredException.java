/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.token.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AuthorizationRequiredException extends WebApplicationException {
	private static final long serialVersionUID = 2602027263698869909L;

	public static final String CODE_NONE = "no_error_code_response";
	public static final String CODE_INVALID_REQUEST = "invalid_request";
	public static final String CODE_INVALID_TOKEN = "invalid_token";
	public static final String CODE_INSUFFICIENT_SCOPE = "insufficient_scope";
	
	public AuthorizationRequiredException(String scheme, String realm, String errorCode, String errorDescription) {
		super(errorDescription, buildResponse(scheme, realm, errorCode, errorDescription));
	}
	
	private static Response buildResponse(String scheme, String realm, String errorCode, String errorDescription) {
		int status;
		switch (errorCode) {
		case CODE_INVALID_REQUEST:
			status = 400;
			break;
		case CODE_NONE:
		case CODE_INVALID_TOKEN:
			status = 401;
			break;
		case CODE_INSUFFICIENT_SCOPE:
			status = 403;
			break;
		default:
			status = 500;
			break;
		}
		
		StringBuilder resMsg = new StringBuilder();
		resMsg.append(scheme);
		resMsg.append(" ");
		if (CODE_NONE.equals(errorCode)) {
			resMsg.append("realm=\"\"");
		} else {
			boolean appended = false;
			if (realm != null) {
				resMsg.append("realm=\"").append(realm).append("\"");
				appended = true;
			}
			if (errorCode != null) {
				if (appended) {
					resMsg.append(",");
				}
				resMsg.append("error=\"").append(errorCode).append("\"");
				appended = true;
			}
			if (errorDescription != null) {
				if (appended) {
					resMsg.append(",");
				}
				resMsg.append("error_description=\"").append(errorDescription).append("\"");
				appended = true;
			}
		}
		
		if (errorDescription == null) {
			errorDescription = "";
		}
		
		return Response.status(status).header("WWW-Authenticate", resMsg.toString()).entity(errorDescription).build();
	}

}
