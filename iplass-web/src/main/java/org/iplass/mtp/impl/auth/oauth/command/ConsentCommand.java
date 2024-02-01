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
package org.iplass.mtp.impl.auth.oauth.command;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationCode;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationRequest;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;

@ActionMapping(name="oauth/consent",
	clientCacheType=ClientCacheType.NO_CACHE,
	//requestIdがCSRF Token代替になるので、Tokenチェックは行わない
	//tokenCheck=@TokenCheck(),
	synchronizeOnSession=true,
	result={
		@Result(status=AuthorizeCommand.STAT_SUCCESS_REDIRECT, type=Type.REDIRECT, allowExternalLocation=true, value=WebRequestConstants.REDIRECT_PATH),
		@Result(status=AuthorizeCommand.STAT_SUCCESS_POST, type=Type.TEMPLATE, value=AuthorizeCommand.TMPL_POST),
		@Result(status=AuthorizeCommand.STAT_ERROR_REDIRECT, type=Type.REDIRECT, allowExternalLocation=true, value=WebRequestConstants.REDIRECT_PATH)
	}
)
@CommandClass(name="mtp/oauth/ConsentCommand", displayName="OAuth2.0 Consent Processing")
public class ConsentCommand implements Command {
	public static final String PARAM_REQUEST_ID = "requestId";
	public static final String PARAM_SUBMIT = "submit";
	public static final String SUBMIT_CANCEL = "cancel";
	public static final String SUBMIT_ACCEPT = "accept";
	
	private AuthorizeCommand authorizeCommand = new AuthorizeCommand();
	private OAuthAuthorizationService authorizationService = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
	
	@Override
	public String execute(RequestContext request) {
		AuthorizationRequest authReq = null;
		String requestId = request.getParam(PARAM_REQUEST_ID);
		String submit = request.getParam(PARAM_SUBMIT);
		try {
			authReq = (AuthorizationRequest) request.getSession().getAttribute(AuthorizeCommand.SESSION_AUTHORIZATION_REQUEST);
			if (authReq == null) {
				//session timeout or ?
				//TODO 多言語化
				throw new ApplicationException("Invalid OAuth authorization flow.");
			}
			if (!authReq.getRequestId().equals(requestId)) {
				throw new ApplicationException("Invalid OAuth authorization flow.");
			}
			
		} finally {
			request.getSession().removeAttribute(AuthorizeCommand.SESSION_AUTHORIZATION_REQUEST);
		}
		
		OAuthAuthorizationRuntime authRuntime = authorizationService.getRuntimeByName(authReq.getAuthorizationServerId());
		if (SUBMIT_ACCEPT.equals(submit)) {
			AuthorizationCode code = authRuntime.generateCode(authReq);
			return authorizeCommand.success(request, code, authRuntime.issuerId(request));
		} else {
			return authorizeCommand.error(request, OAuthConstants.ERROR_ACCESS_DENIED, "User canceled OAuth request.", authReq, authRuntime.issuerId(request));
		}
	}

}
