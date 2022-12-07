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
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCCredential;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCRuntimeException;
import org.iplass.mtp.impl.auth.authenticate.oidc.OIDCState;
import org.iplass.mtp.impl.auth.authenticate.oidc.OpenIdConnectService;
import org.iplass.mtp.impl.auth.oauth.util.OAuthEndpointConstants;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCallbackCommand implements Command {
	public static final String PARAM_DEFINITION_NAME = "defName";
	public static final String STAT_SUCCESS = "SUCCESS";

	public static final String REQUEST_ERROR_TEMPLATE = "org.iplass.mtp.oidc.errorTemplate";
	
	private static Logger logger = LoggerFactory.getLogger(AbstractCallbackCommand.class);
	
	private OpenIdConnectService service = ServiceRegistry.getRegistry().getService(OpenIdConnectService.class);
	
	private String sessionOidStateKey;
	
	public AbstractCallbackCommand(String sessionOidStateKey) {
		this.sessionOidStateKey = sessionOidStateKey;
	}

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
		
		OIDCState state = null;
		SessionContext session = request.getSession(false);
		if (session != null) {
			state = (OIDCState) session.getAttribute(sessionOidStateKey);
			if (state != null) {
				session.removeAttribute(sessionOidStateKey);
			}
		}
		setErrorTemplate(request, state);

		if (error != null) {
			String errorDesc = StringUtil.stripToEmpty(request.getParam(OAuthEndpointConstants.PARAM_ERROR_DESCRIPTION));
			String errorUri = StringUtil.stripToEmpty(request.getParam(OAuthEndpointConstants.PARAM_ERROR_URI));
			logger.error("oidc error: error=" + error +", error_desc=" + errorDesc + ", error_uri=" + errorUri);
			throw new ApplicationException(resourceString("impl.auth.authenticate.oidc.command.AbstractCallbackCommand.error", StringUtil.stripToEmpty(errorDesc), error));
		}
		
		
		if (state == null) {
			//invalid state
			if (logger.isDebugEnabled()) {
				logger.debug("invalid state:" + state);
			}
			logger.error("oidc error: error=invalid client state");
			throw new ApplicationException(resourceString("impl.auth.authenticate.oidc.command.AbstractCallbackCommand.error", "", "invalid_client_state"));
		}
		
		OIDCCredential cre = new OIDCCredential(defName, code, stateToken, createRedirectUri(oidp, request), iss, state);
		executeImpl(oidp, request, cre);
		
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
	
	protected abstract void executeImpl(OpenIdConnectRuntime oidp, RequestContext request, OIDCCredential cre);
	protected abstract String createRedirectUri(OpenIdConnectRuntime oidp, RequestContext request);
	
	void setErrorTemplate(RequestContext request, OIDCState state) {
		String errorTmpl = null;
		if (state != null) {
			errorTmpl = state.getErrorTemplateName();
		}
		if (errorTmpl == null) {
			errorTmpl = MetaDataRefs.TMPL_OIDC_ERROR;
		}
		request.setAttribute(REQUEST_ERROR_TEMPLATE, errorTmpl);
	}

}
