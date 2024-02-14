/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.login.rememberme.RememberMeConstants;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.token.TokenStore;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMapping(name=UpdateExpirePasswordCommand.ACTION_UPDATE_EXP_PASSWORD,
		allowMethod=HttpMethodType.POST,
		clientCacheType=ClientCacheType.NO_CACHE,
		privileged=true,
		command=@CommandConfig("cmd.checkLoginToken=true"),
		result={
			@Result(status="SUCCESS", type=Type.REDIRECT, value="mtp.auth.redirectPath"),
			@Result(status="ERROR", type=Type.TEMPLATE, value=Constants.TEMPLATE_PASSWORD_EXPIRE),
			@Result(status="TOKEN_ERROR", type=Type.TEMPLATE, value=Constants.TEMPLATE_LOGIN)
			}
)
@CommandClass(name="gem/auth/UpdateExpirePasswordCommand", displayName="有効期限切れパスワード更新")
public final class UpdateExpirePasswordCommand implements Command, AuthCommandConstants {

	private static Logger logger = LoggerFactory.getLogger(UpdateExpirePasswordCommand.class);

	public static final String ACTION_UPDATE_EXP_PASSWORD = "gem/auth/expiredpassword/update";

	private boolean checkLoginToken;

	private AuthManager am = ManagerLocator.getInstance().getManager(AuthManager.class);

	public boolean isCheckLoginToken() {
		return checkLoginToken;
	}
	public void setCheckLoginToken(boolean checkLoginToken) {
		this.checkLoginToken = checkLoginToken;
	}

	@Override
	public String execute(RequestContext request) {
		//Login CSRFチェック
		//TODO 通常のCSRFと違い、referrerのチェックも必要か？？
		if (checkLoginToken) {
			TokenStore ts = TokenStore.getTokenStore(request.getSession());
			if (ts == null || !ts.isValid(request.getParam(TokenStore.TOKEN_PARAM_NAME), true)) {
				return "TOKEN_ERROR";
			}
		}

		CredentialExpiredState state = (CredentialExpiredState) request.getSession().getAttribute(SESSION_CREDENTIAL_EXPIRE_STATE);
		if (state == null) {
			throw new SystemException("CredentialExpiredState is null");
		}

		String oldPass = request.getParam(PARAM_PASSWORD);
		String newPass1 = request.getParam(PARAM_NEW_PASSWORD);
		String newPass2 = request.getParam(PARAM_CONFIRM_PASSWORD);
		if (!checkSame(newPass1, newPass2)) {
			request.setAttribute(RESULT_ERROR, new ApplicationException(resourceString("command.auth.UpdatePasswordCommand.notMatch")));
			return "ERROR";
		}

		if (!checkNotSame(oldPass, newPass1)) {
			request.setAttribute(RESULT_ERROR, new ApplicationException(resourceString("command.auth.UpdatePasswordCommand.notSame")));
			return "ERROR";
		}

		IdPasswordCredential oldCredential = new IdPasswordCredential(state.getId(), oldPass);
		IdPasswordCredential newCredential = new IdPasswordCredential(state.getId(), newPass1);

		try {
			am.updateCredential(oldCredential, newCredential, state.getPolicyName());
			Credential cre = newCredential;
			if (state.isRememberMe()) {
				cre.setAuthenticationFactor(RememberMeConstants.FACTOR_REMEMBER_ME_FLAG, Boolean.TRUE);
			}

			am.login(cre);

			String redirectPath = state.getRedirectPath();
			if (LoginCommand.checkRedirectPath(redirectPath)) {
				//ログイン後画面として設定されている画面へ遷移
				request.setAttribute(RESULT_REDIRECT_PATH, redirectPath);
			} else {
				//トップ画面へ遷移
				Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
				String menuUrl = WebUtil.getTenantWebInfo(tenant).getHomeUrl();
				if (menuUrl != null && menuUrl.length() != 0) {
					request.setAttribute(RESULT_REDIRECT_PATH, TemplateUtil.getTenantContextPath() + menuUrl);
				} else {
					request.setAttribute(RESULT_REDIRECT_PATH, TemplateUtil.getTenantContextPath() + "/gem/");
				}
			}
			return "SUCCESS";
		} catch (CredentialUpdateException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			request.setAttribute(RESULT_ERROR, e);
			return "ERROR";
		}
	}

	private boolean checkSame(String pass1, String pass2) {
		if (pass1 == null) {
			return false;
		}
		if (pass2 == null) {
			return false;
		}
		return pass1.equals(pass2);
	}

	private boolean checkNotSame(String oldPass, String newPass) {
		if (oldPass == null) {
			return false;
		}
		if (newPass == null) {
			return false;
		}
		return !oldPass.equals(newPass);
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
