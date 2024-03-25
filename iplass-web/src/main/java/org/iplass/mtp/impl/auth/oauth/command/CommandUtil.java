/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth.command;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.authenticate.builtin.web.BasicAuthUtil;
import org.iplass.mtp.impl.auth.authenticate.builtin.web.WWWAuthenticateException;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthClientService;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthEndpointConstants;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CommandUtil {
	//TODO 以下そのうち対応
	//client_assertion
	//client_assertion_type
	

	private static Logger logger = LoggerFactory.getLogger(CommandUtil.class);
	private static OAuthClientService clientService = ServiceRegistry.getRegistry().getService(OAuthClientService.class);
	
	static IdPasswordCredential clientCredential(RequestContext request) {
		String clientId = StringUtil.stripToNull(request.getParam(OAuthEndpointConstants.PARAM_CLIENT_ID));
		String clientSecret = null;
		if (clientId != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("get client credential from parameter. clientId:" + clientId);
			}
			clientSecret = StringUtil.stripToNull(request.getParam(OAuthEndpointConstants.PARAM_CLIENT_SECRET));
			return new IdPasswordCredential(clientId, clientSecret);
		}
		
		//from header
		IdPasswordCredential cre = BasicAuthUtil.decodeFromHeader(request);
		if (cre != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("get client credential from header. clientId:" + cre.getId());
			}
		}
		cre.setAuthenticationFactor(BasicAuthUtil.AUTH_SCHEME_BASIC, true);
		return cre;
	}
	
	static OAuthClientRuntime validateClient(RequestContext request, boolean allowPublicClient) {
		IdPasswordCredential clientCredential = clientCredential(request);
		if (clientCredential == null) {
			throw new WebApplicationException(buildErrorResponse(OAuthConstants.ERROR_INVALID_CLIENT, null, null));
		}
		
		OAuthClientRuntime clientRuntime = clientService.getRuntimeByName(clientCredential.getId());
		if (clientRuntime == null
				|| clientRuntime.getAuthorizationServer() == null
				|| !clientRuntime.validateCredential(clientCredential, allowPublicClient)) {
			if (clientCredential.getAuthenticationFactor(BasicAuthUtil.AUTH_SCHEME_BASIC) != null) {
				throw new WWWAuthenticateException(BasicAuthUtil.AUTH_SCHEME_BASIC, null, errorMsg(OAuthConstants.ERROR_INVALID_CLIENT, null, null));
			} else {
				throw new WebApplicationException(buildErrorResponse(OAuthConstants.ERROR_INVALID_CLIENT, null, null));
			}
		}
		
		return clientRuntime;

	}

	static Response buildErrorResponse(String error, String errorDescription, String errorUri) {
		Object msg = errorMsg(error, errorDescription, errorUri);
		int statCode = 400;
		if (OAuthConstants.ERROR_INVALID_CLIENT.equals(error)) {
			statCode= 401;
		}
		
		return Response.status(statCode).header("Content-Type", "application/json").entity(msg).build();
	}
	
	static String errorMsg(String error, String errorDescription, String errorUri) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"error\":");
		sb.append("\"").append(StringUtil.escapeJavaScript(error)).append("\"");
		if (errorDescription != null) {
			sb.append(",\"error_description\":");
			sb.append("\"").append(StringUtil.escapeJavaScript(errorDescription)).append("\"");
		}
		if (errorUri != null) {
			sb.append(",\"errorUri\":");
			sb.append("\"").append(StringUtil.escapeJavaScript(errorUri)).append("\"");
		}
		sb.append("}");
		return sb.toString();
	}

}
