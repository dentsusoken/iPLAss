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

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.TokenCheck;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCState;
import org.iplass.mtp.impl.auth.authenticate.oidc.OpenIdConnectService;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCRuntimeException;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMapping(name=AccountConnectCommand.ACTION_NAME,
	clientCacheType=ClientCacheType.NO_CACHE,
	tokenCheck=@TokenCheck(useFixedToken=true),
	paramMapping={
			@ParamMapping(name=AccountConnectCommand.PARAM_DEFINITION_NAME, mapFrom=ParamMapping.PATHS)
	},
	result={
		@Result(status=AccountConnectCommand.STAT_SUCCESS,
				type=Type.REDIRECT,
				allowExternalLocation=true,
				value=WebRequestConstants.REDIRECT_PATH)
	}
)
@CommandClass(name="mtp/oidc/AccountConnectCommand", displayName="OpenID Connect Account Connect processing")
public class AccountConnectCommand implements Command {
	public static final String ACTION_NAME = "oidc/connect";
	public static final String PARAM_DEFINITION_NAME = "defName";
	public static final String STAT_SUCCESS = "SUCCESS";
	public static final String SESSION_OIDC_STATE = "org.iplass.mtp.oidc.connect.state";

	private static Logger logger = LoggerFactory.getLogger(AccountConnectCommand.class);
	
	private OpenIdConnectService service = ServiceRegistry.getRegistry().getService(OpenIdConnectService.class);
	private AuthenticationPolicyService policyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
	
	@Override
	public String execute(RequestContext request) {
		String defName = StringUtil.stripToNull(request.getParam(PARAM_DEFINITION_NAME));
		OpenIdConnectRuntime oidp = service.getOrDefault(defName);
		if (oidp == null) {
			throw new OIDCRuntimeException("no OpenIdProvider Definition:" + defName);
		}
		
		User user = AuthContext.getCurrentContext().getUser();
		AuthenticationPolicyRuntime userPolicy = policyService.getOrDefault(user.getAccountPolicy());
		if (!oidp.isAllowedOnPolicy(userPolicy)) {
			throw new OIDCRuntimeException("policy not allow OpenIdConnectDefinition:" + oidp.getMetaData().getName());
		}

		String backUrlAfterConnect = oidp.backUrlAfterConnect(request);
		if (backUrlAfterConnect == null) {
			backUrlAfterConnect = (String) request.getAttribute(WebRequestConstants.REDIRECT_PATH);
		}
		String errorTemplate = (String) request.getAttribute(AuthCommand.REQUEST_ERROR_TEMPLATE);
		
		OIDCState state = oidp.newOIDCState(backUrlAfterConnect, oidp.createRedirectUri(request, AccountConnectCallbackCommand.ACTION_NAME), errorTemplate);
		request.getSession().setAttribute(SESSION_OIDC_STATE, state);

		String redirect = oidp.authorizeUrl(state);

		if (logger.isDebugEnabled()) {
			logger.debug("redirect to OP:" + redirect);
		}

		request.setAttribute(WebRequestConstants.REDIRECT_PATH, redirect);

		return STAT_SUCCESS;
	}

}
