/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.auth;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.auth.login.CredentialExpiredException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.auth.login.rememberme.RememberMeConstants;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMappings({
	@ActionMapping(name=ReAuthCommand.ACTION_VIEW_RE_AUTH,
			clientCacheType=ClientCacheType.NO_CACHE,
			command={},
			result=@Result(type=Type.JSP, value=Constants.CMD_RSLT_JSP_REAUTH, templateName="gem/auth/ReAuth")),
	@ActionMapping(name=ReAuthCommand.ACTION_RE_AUTH,
			allowMethod=HttpMethodType.POST,
			clientCacheType=ClientCacheType.NO_CACHE,
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.REDIRECT, value="mtp.auth.redirectPath"),
				@Result(status=ReAuthCommand.CMD_EXEC_EXPIRE, type=Type.DYNAMIC, value="expirePath"),
				@Result(status=ReAuthCommand.CMD_EXEC_TWOSTEP, type=Type.JSP, value="/jsp/gem/auth/Verify2nd.jsp", templateName="gem/auth/Verify2nd"),
				@Result(status=Constants.CMD_EXEC_ERROR, type=Type.JSP, value=Constants.CMD_RSLT_JSP_REAUTH, templateName="gem/auth/ReAuth"),
				@Result(exception=ApplicationException.class, type=Type.JSP, value=Constants.CMD_RSLT_JSP_REAUTH, templateName="gem/auth/ReAuth")
				}
	)
})
@CommandClass(name="gem/auth/ReAuthCommand", displayName="再認証処理")
public final class ReAuthCommand implements Command, AuthCommandConstants {

	private static Logger logger = LoggerFactory.getLogger(ReAuthCommand.class);

	public static final String ACTION_VIEW_RE_AUTH = "gem/auth/reAuth";
	public static final String ACTION_RE_AUTH = "gem/auth/doReAuth";

	public static final String CMD_EXEC_EXPIRE = "EXPIRE";
	public static final String CMD_EXEC_TWOSTEP = "TWOSTEP";

	private AuthManager am = ManagerLocator.getInstance().getManager(AuthManager.class);

	private boolean rememberMe(AuthContext authContext) {
		return false;
	}

	@Override
	public String execute(RequestContext request) {
		AuthContext authContext = AuthContext.getCurrentContext();
		String accountId = authContext.getUser().getAccountId();
		String redirectPath = request.getParam(PARAM_BACK_URL);

		boolean remMe = rememberMe(authContext);

		try {
			if (!authContext.isAuthenticated()) {
				throw new ApplicationException(resourceString("auth.Login.noLogin"));
			}

			IdPasswordCredential cre = new IdPasswordCredential(accountId, request.getParam(PARAM_PASSWORD));
			if (remMe) {
				cre.setAuthenticationFactor(RememberMeConstants.FACTOR_REMEMBER_ME_FLAG, remMe);
			}

			am.reAuth(cre);
			LoginCommand.setRedirectPathAfterLogin(request, redirectPath);

			return Constants.CMD_EXEC_SUCCESS;

		} catch (LoginFailedException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}
			request.setAttribute(RESULT_ERROR, e);
			if (redirectPath != null) {
				request.setAttribute(WebRequestConstants.REDIRECT_PATH, redirectPath);
			}
			return Constants.CMD_EXEC_ERROR;
		} catch (CredentialExpiredException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			return LoginCommand.handleCredentialExpiredException(request, accountId, null, null, redirectPath, remMe, e);
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
