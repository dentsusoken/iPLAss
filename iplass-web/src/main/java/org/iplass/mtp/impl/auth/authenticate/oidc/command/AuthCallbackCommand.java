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
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCCredential;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCState;
import org.iplass.mtp.impl.auth.authenticate.oidc.OpenIdConnectService;
import org.iplass.mtp.impl.auth.oauth.util.OAuthEndpointConstants;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMapping(name=AuthCallbackCommand.ACTION_NAME,
	clientCacheType=ClientCacheType.NO_CACHE,
	publicAction=true,
	privilaged=true,
	paramMapping={
			@ParamMapping(name=AuthCallbackCommand.PARAM_DEFINITION_NAME, mapFrom=ParamMapping.PATHS)
	},
	result={
			@Result(status=AuthCallbackCommand.STAT_SUCCESS, type=Type.REDIRECT, value=WebRequestConstants.REDIRECT_PATH),
			@Result(exception=ApplicationException.class, type=Type.TEMPLATE, value=MetaDataRefs.TMPL_OIDC_ERROR, resolveByName=true)
	}
)
@CommandClass(name="mtp/oidc/AuthCallbackCommand", displayName="OpenID Connect Auth Callback processing")
public class AuthCallbackCommand implements Command {
	static final String ACTION_NAME = "oidc/authcb";
	static final String PARAM_DEFINITION_NAME = "defName";
	static final String STAT_SUCCESS = "SUCCESS";

	private static Logger logger = LoggerFactory.getLogger(AuthCallbackCommand.class);
	
	private AuthManager auth = ManagerLocator.getInstance().getManager(AuthManager.class);
	private OpenIdConnectService service = ServiceRegistry.getRegistry().getService(OpenIdConnectService.class);

	@Override
	public String execute(RequestContext request) {
		String error = StringUtil.stripToNull(request.getParam(OAuthEndpointConstants.PARAM_ERROR));
		String defName = StringUtil.stripToNull(request.getParam(PARAM_DEFINITION_NAME));
		String stateToken = request.getParam(OAuthEndpointConstants.PARAM_STATE);
		String code = request.getParam(OAuthEndpointConstants.PARAM_CODE);
		String iss = request.getParam(OAuthEndpointConstants.PARAM_ISS);
		
		OpenIdConnectRuntime oidp = service.getOrDefault(defName);
		if (oidp == null) {
			throw new OIDCRuntimeException("no OpenIdProvider Definition:" + defName);
		}
		
		if (error != null) {
			String errorDesc = StringUtil.stripToEmpty(request.getParam(OAuthEndpointConstants.PARAM_ERROR_DESCRIPTION));
			String errorUri = StringUtil.stripToEmpty(request.getParam(OAuthEndpointConstants.PARAM_ERROR_URI));
			logger.error("authentication error: error=" + error +", error_desc=" + errorDesc + ", error_uri=" + errorUri);
			throw new LoginFailedException(resourceString("impl.auth.authenticate.oidc.command.AuthCallbackCommand.error", StringUtil.stripToEmpty(errorDesc), error));
		}
		
		OIDCState state = null;
		SessionContext session = request.getSession(false);
		if (session != null) {
			state = (OIDCState) session.getAttribute(AuthCommand.SESSION_OIDC_STATE);
			if (state != null) {
				session.removeAttribute(AuthCommand.SESSION_OIDC_STATE);
			}
		}
		
		if (state == null) {
			//invalid state
			if (logger.isDebugEnabled()) {
				logger.debug("invalid state:" + state);
			}
			logger.error("authentication error: error=invalid client state");
			throw new LoginFailedException(resourceString("impl.auth.authenticate.oidc.command.AuthCallbackCommand.error", "", "invalid_client_state"));
		}
		
		OIDCCredential cre = new OIDCCredential(defName, code, stateToken, oidp.createRedirectUri(request, AuthCallbackCommand.ACTION_NAME), iss, state);
		auth.login(cre);

		if (checkRedirectPath(state.getBackUrlAfterAuth())) {
			//ログイン後画面として設定されている画面へ遷移
			request.setAttribute(WebRequestConstants.REDIRECT_PATH, state.getBackUrlAfterAuth());
		} else {
			//トップ画面へ遷移
			Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
			String menuUrl = WebUtil.getTenantWebInfo(tenant).getHomeUrl();
			if (menuUrl != null && menuUrl.length() != 0) {
				request.setAttribute(WebRequestConstants.REDIRECT_PATH, TemplateUtil.getTenantContextPath() + menuUrl);
			} else {
				request.setAttribute(WebRequestConstants.REDIRECT_PATH, TemplateUtil.getTenantContextPath() + "/");
			}
		}
		
		return STAT_SUCCESS;
	}
	
	private boolean checkRedirectPath(String redirectPath) {
		//リダイレクトは（現状）同一ドメインへのものに限る
		if (redirectPath == null) {
			return false;
		}
		if (!WebUtil.isValidInternalUrl(redirectPath)) {
			if (logger.isDebugEnabled()) {
				logger.debug("invalid redirect url: " + redirectPath);
			}
			return false;
		}
		return true;
	}

}
