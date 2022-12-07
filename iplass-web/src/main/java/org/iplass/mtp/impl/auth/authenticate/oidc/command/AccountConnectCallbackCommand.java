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

import static org.iplass.mtp.impl.web.WebResourceBundleUtil.resourceString;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCCredential;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCValidateResult;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;

@ActionMapping(name=AccountConnectCallbackCommand.ACTION_NAME,
	clientCacheType=ClientCacheType.NO_CACHE,
	paramMapping={
			@ParamMapping(name=AccountConnectCallbackCommand.PARAM_DEFINITION_NAME, mapFrom=ParamMapping.PATHS)
	},
	result={
			@Result(status=AccountConnectCallbackCommand.STAT_SUCCESS, type=Type.REDIRECT, value=WebRequestConstants.REDIRECT_PATH),
			@Result(exception=ApplicationException.class, type=Type.DYNAMIC, value=AuthCommand.REQUEST_ERROR_TEMPLATE)
	}
)
@CommandClass(name="mtp/oidc/AccountConnectCallbackCommand", displayName="OpenID Connect Account Connect Callback processing")
public class AccountConnectCallbackCommand extends AbstractCallbackCommand {
	public static final String ACTION_NAME = "oidc/connectcb";
	public static final String PARAM_DEFINITION_NAME = "defName";
	public static final String STAT_SUCCESS = "SUCCESS";

	private AuthenticationPolicyService policyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);

	public AccountConnectCallbackCommand() {
		super(AccountConnectCommand.SESSION_OIDC_STATE);
	}

	@Override
	protected void executeImpl(OpenIdConnectRuntime oidp, RequestContext request, OIDCCredential cre) {
		OIDCValidateResult vr = oidp.validate(cre);
		if (!vr.isValid()) {
			OIDCRuntimeException ore;
			if (vr.getRootCause() == null) {
				ore = new OIDCRuntimeException(vr.getError() + ":" + vr.getErrorDescription());
			} else {
				ore = new OIDCRuntimeException(vr.getError() + ":" + vr.getErrorDescription(), vr.getRootCause());
			}
			throw new ApplicationException(resourceString("impl.auth.authenticate.oidc.command.AbstractCallbackCommand.error", "Invalid response from OpenID Provider.", "invalid_response"), ore);
		}
		
		User user = AuthContext.getCurrentContext().getUser();
		AuthenticationPolicyRuntime userPolicy = policyService.getOrDefault(user.getAccountPolicy());
		if (!oidp.isAllowedOnPolicy(userPolicy)) {
			throw new OIDCRuntimeException("policy not allow OpenIdConnectDefinition:" + oidp.getMetaData().getName());
		}
		
		oidp.connect(user.getOid(), vr);
	}

	@Override
	protected String createRedirectUri(OpenIdConnectRuntime oidp, RequestContext request) {
		return oidp.createRedirectUri(request, ACTION_NAME);
	}

}
