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
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCCredential;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCState;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCValidateResult;
import org.iplass.mtp.impl.auth.authenticate.oidc.OpenIdConnectService;
import org.iplass.mtp.impl.auth.oauth.util.OAuthEndpointConstants;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMapping(name=AccountConnectCallbackCommand.ACTION_NAME,
	clientCacheType=ClientCacheType.NO_CACHE,
	paramMapping={
			@ParamMapping(name=AccountConnectCallbackCommand.PARAM_DEFINITION_NAME, mapFrom=ParamMapping.PATHS)
	},
	result={
			@Result(status=AccountConnectCallbackCommand.STAT_SUCCESS, type=Type.REDIRECT, value=WebRequestConstants.REDIRECT_PATH),
			@Result(exception=ApplicationException.class, type=Type.TEMPLATE, value=MetaDataRefs.TMPL_OIDC_ERROR, resolveByName=true)
	}
)
@CommandClass(name="mtp/oidc/AccountConnectCallbackCommand", displayName="OpenID Connect Account Connect Callback processing")
public class AccountConnectCallbackCommand implements Command {
	static final String ACTION_NAME = "oidc/connectcb";
	static final String PARAM_DEFINITION_NAME = "defName";
	static final String STAT_SUCCESS = "SUCCESS";

	private static Logger logger = LoggerFactory.getLogger(AccountConnectCallbackCommand.class);
	
	private OpenIdConnectService service = ServiceRegistry.getRegistry().getService(OpenIdConnectService.class);
	private AuthenticationPolicyService policyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);

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
			logger.error("oidc connect error: error=" + error +", error_desc=" + errorDesc + ", error_uri=" + errorUri);
			throw new ApplicationException(resourceString("impl.auth.authenticate.oidc.command.AccountConnectCallbackCommand.error", StringUtil.stripToEmpty(errorDesc), error));
		}
		
		OIDCState state = null;
		SessionContext session = request.getSession(false);
		if (session != null) {
			state = (OIDCState) session.getAttribute(AccountConnectCommand.SESSION_OIDC_STATE);
			if (state != null) {
				session.removeAttribute(AccountConnectCommand.SESSION_OIDC_STATE);
			}
		}
		
		if (state == null) {
			//invalid state
			if (logger.isDebugEnabled()) {
				logger.debug("invalid state:" + state);
			}
			logger.error("oidc connect error: error=invalid client state");
			throw new ApplicationException(resourceString("impl.auth.authenticate.oidc.command.AccountConnectCallbackCommand.error", "", "invalid_client_state"));
		}
		
		OIDCCredential cre = new OIDCCredential(defName, code, stateToken, oidp.createRedirectUri(request, AccountConnectCallbackCommand.ACTION_NAME), iss, state);
		OIDCValidateResult vr = oidp.validate(cre);
		if (!vr.isValid()) {
			OIDCRuntimeException ore;
			if (vr.getRootCause() == null) {
				ore = new OIDCRuntimeException(vr.getError() + ":" + vr.getErrorDescription());
			} else {
				ore = new OIDCRuntimeException(vr.getError() + ":" + vr.getErrorDescription(), vr.getRootCause());
			}
			throw new ApplicationException(resourceString("impl.auth.authenticate.oidc.command.AccountConnectCallbackCommand.error", "Invalid response from OpenID Provider.", "invalid_response"), ore);
		}
		
		User user = AuthContext.getCurrentContext().getUser();
		AuthenticationPolicyRuntime userPolicy = policyService.getOrDefault(user.getAccountPolicy());
		if (!oidp.isAllowedOnPolicy(userPolicy)) {
			throw new OIDCRuntimeException("policy not allow OpenIdConnectDefinition:" + oidp.getMetaData().getName());
		}
		
		oidp.connect(user.getOid(), vr);
		
		if (state.getBackUrlAfterAuth() == null) {
			throw new OIDCRuntimeException("No redirect url");
		}
		if (!WebUtil.isValidInternalUrl(state.getBackUrlAfterAuth())) {
			if (logger.isDebugEnabled()) {
				logger.debug("invalid redirect url: " + state.getBackUrlAfterAuth());
			}
			throw new OIDCRuntimeException("Invalid redirect url");
		}
		
		request.setAttribute(WebRequestConstants.REDIRECT_PATH, state.getBackUrlAfterAuth());
		return STAT_SUCCESS;
	}

}
