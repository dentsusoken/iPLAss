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

import static org.iplass.mtp.impl.web.WebResourceBundleUtil.resourceString;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCCredential;
import org.iplass.mtp.web.WebRequestConstants;

@ActionMapping(name=AuthCallbackCommand.ACTION_NAME,
	clientCacheType=ClientCacheType.NO_CACHE,
	publicAction=true,
	privilaged=true,
	paramMapping={
			@ParamMapping(name=AuthCallbackCommand.PARAM_DEFINITION_NAME, mapFrom=ParamMapping.PATHS)
	},
	result={
			@Result(status=AuthCallbackCommand.STAT_SUCCESS, type=Type.REDIRECT, value=WebRequestConstants.REDIRECT_PATH),
			@Result(exception=ApplicationException.class, type=Type.DYNAMIC, value=AuthCommand.REQUEST_ERROR_TEMPLATE)
	}
)
@CommandClass(name="mtp/oidc/AuthCallbackCommand", displayName="OpenID Connect Auth Callback processing")
public class AuthCallbackCommand extends  AbstractCallbackCommand {
	public static final String ACTION_NAME = "oidc/authcb";
	public static final String PARAM_DEFINITION_NAME = "defName";
	public static final String STAT_SUCCESS = "SUCCESS";

	private AuthManager auth = ManagerLocator.getInstance().getManager(AuthManager.class);

	public AuthCallbackCommand() {
		super(AuthCommand.SESSION_OIDC_STATE);
	}

	@Override
	public String execute(RequestContext request) {
		try {
			return super.execute(request);
		} catch (LoginFailedException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new LoginFailedException(resourceString("impl.auth.authenticate.oidc.command.AuthCallbackCommand.error") + " " + e.getMessage(), e);
		}
	}

	@Override
	protected void executeImpl(OpenIdConnectRuntime oidp, RequestContext request, OIDCCredential cre) {
		auth.login(cre);
	}

	@Override
	protected String createRedirectUri(OpenIdConnectRuntime oidp, RequestContext request) {
		return oidp.createRedirectUri(request, ACTION_NAME);
	}
}
