/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiParamMapping;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.oidc.OpenIdConnectService;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.CacheControlType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(name=AccountDisconnectCommand.WEBAPI_NAME,
	accepts=RequestType.REST_FORM,
	methods=MethodType.POST,
	cacheControlType=CacheControlType.NO_CACHE,
	tokenCheck=@WebApiTokenCheck(useFixedToken=true),
	paramMapping={
			@WebApiParamMapping(name=AccountDisconnectCommand.PARAM_DEFINITION_NAME, mapFrom=WebApiParamMapping.PATHS)
	}
)
@CommandClass(name="mtp/oidc/AccountDisconnectCommand", displayName="OpenID Connect Account Disconnect processing")
public class AccountDisconnectCommand implements Command {

	static final String WEBAPI_NAME = "oidc/disconnect";
	static final String PARAM_DEFINITION_NAME = "defName";
	static final String STAT_SUCCESS = "SUCCESS";

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
		
		oidp.disconnect(user.getOid());
		return STAT_SUCCESS;
	}
}
