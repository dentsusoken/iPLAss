/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.oidc.command;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCState;
import org.iplass.mtp.impl.auth.authenticate.oidc.OpenIdConnectService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMapping(name=AuthCommand.ACTION_NAME,
	clientCacheType=ClientCacheType.NO_CACHE,
	publicAction=true,
	paramMapping={
			@ParamMapping(name=AuthCommand.PARAM_DEFINITION_NAME, mapFrom=ParamMapping.PATHS)
	},
	result={
		@Result(status=AuthCommand.STAT_SUCCESS,
				type=Type.REDIRECT,
				allowExternalLocation=true,
				value=WebRequestConstants.REDIRECT_PATH)
	}
)
@CommandClass(name="mtp/oidc/AuthCommand", displayName="OpenID Connect Login processing")
public class AuthCommand implements Command {
	
	static final String ACTION_NAME = "oidc/auth";
	static final String PARAM_DEFINITION_NAME = "defName";
	static final String STAT_SUCCESS = "SUCCESS";
	static final String SESSION_OIDC_STATE = "org.iplass.mtp.oidc.state";
	
	private static Logger logger = LoggerFactory.getLogger(AuthCommand.class);
	
	private OpenIdConnectService service = ServiceRegistry.getRegistry().getService(OpenIdConnectService.class);

	@Override
	public String execute(RequestContext request) {
		
		//他人でログインされることはないのでLogin CSRF対策は必要ないと考える
		
		String defName = StringUtil.stripToNull(request.getParam(PARAM_DEFINITION_NAME));
		OpenIdConnectRuntime oidp = service.getOrDefault(defName);
		if (oidp == null) {
			throw new OIDCRuntimeException("no OpenIdProvider Definition:" + defName);
		}
		String backUrlAfterAuth = oidp.backUrlAfterAuth(request);
		if (backUrlAfterAuth == null) {
			backUrlAfterAuth = (String) request.getAttribute(WebRequestConstants.REDIRECT_PATH);
		}
		
		OIDCState state = oidp.newOIDCState(backUrlAfterAuth, oidp.createRedirectUri(request, AuthCallbackCommand.ACTION_NAME));
		request.getSession().setAttribute(SESSION_OIDC_STATE, state);

		String redirect = oidp.authorizeUrl(state);

		if (logger.isDebugEnabled()) {
			logger.debug("redirect to OP:" + redirect);
		}

		request.setAttribute(WebRequestConstants.REDIRECT_PATH, redirect);

		return STAT_SUCCESS;
	}
}
