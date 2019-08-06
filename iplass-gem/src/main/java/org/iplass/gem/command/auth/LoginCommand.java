/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.auth.login.Credential;
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
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.command.annotation.template.Templates;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.token.TokenStore;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.tenant.web.TenantWebInfo;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMappings({
	@ActionMapping(name=LoginCommand.ACTION_VIEW_LOGIN,
			clientCacheType=ClientCacheType.NO_CACHE,
			privilaged=true,
			command={},
			result=@Result(type=Type.JSP, value=Constants.CMD_RSLT_JSP_LOGIN, templateName="gem/auth/Login")),
	@ActionMapping(name=LoginCommand.ACTION_LOGIN,
			allowMethod=HttpMethodType.POST,
			clientCacheType=ClientCacheType.NO_CACHE,
			privilaged=true,
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.REDIRECT, value="mtp.auth.redirectPath"),
				@Result(status=LoginCommand.CMD_EXEC_EXPIRE, type=Type.TEMPLATE, value=Constants.TEMPLATE_PASSWORD_EXPIRE),
				@Result(status=LoginCommand.CMD_EXEC_TWOSTEP, type=Type.JSP, value="/jsp/gem/auth/Verify2nd.jsp", templateName="gem/auth/Verify2nd"),
				@Result(status=Constants.CMD_EXEC_ERROR, type=Type.JSP, value=Constants.CMD_RSLT_JSP_LOGIN, templateName="gem/auth/Login"),
				@Result(exception=ApplicationException.class, type=Type.JSP, value=Constants.CMD_RSLT_JSP_LOGIN, templateName="gem/auth/Login")
				}
	)
})
@Templates({
	@Template(name=Constants.TEMPLATE_PASSWORD_EXPIRE, path=Constants.CMD_RSLT_JSP_PASSWORD_EXPIRE),
	@Template(name="gem/auth/LastLoginParts", displayName="最終ログイン日時パーツ", path="/jsp/gem/auth/lastLoginParts.jsp"),
	@Template(name="gem/generic/editor/EntitySelectPropertyEditorJsp", displayName="エンティティ選択プロパティエディタ",
		path="/jsp/gem/generic/editor/EntitySelectPropertyEditor.jsp"),
	@Template(name="gem/generic/editor/AuthenticationPolicySelectPropertyEditorJsp", displayName="認証ポリシー選択プロパティエディタ",
		path="/jsp/gem/generic/editor/AuthenticationPolicySelectPropertyEditor.jsp")
})
@CommandClass(name="gem/auth/LoginCommand", displayName="ログイン処理")
public final class LoginCommand implements Command, AuthCommandConstants {

	private static Logger logger = LoggerFactory.getLogger(LoginCommand.class);

	public static final String ACTION_VIEW_LOGIN = "gem/auth/login";
	public static final String ACTION_LOGIN = "gem/auth/dologin";

	public static final String CMD_EXEC_EXPIRE = "EXPIRE";
	public static final String CMD_EXEC_TWOSTEP = "TWOSTEP";

	private boolean checkLoginToken = true;

	private AuthManager auth = ManagerLocator.getInstance().getManager(AuthManager.class);

	public LoginCommand() {
	}

	public boolean isCheckLoginToken() {
		return checkLoginToken;
	}
	public void setCheckLoginToken(boolean checkLoginToken) {
		this.checkLoginToken = checkLoginToken;
	}

	private boolean isRememberMe(RequestContext request) {
		String val = request.getParam(PARAM_REMEMBER_ME);
		if (val != null && val.equals("1")) {
			return true;
		}
		return false;
	}

	@Override
	public String execute(RequestContext request) {
		//Login CSRFチェック
		//TODO 通常のCSRFと違い、referrerのチェックも必要か？？
		if (checkLoginToken) {
			TokenStore ts = TokenStore.getTokenStore(request.getSession());
			if (ts == null || !ts.isValid(request.getParam(TokenStore.TOKEN_PARAM_NAME), true)) {

				//操作が行われなかったため、ログインセッションがタイムアウトになりました。 もう一度やり直してください。
				request.setAttribute(RESULT_ERROR, new ApplicationException(resourceString("auth.Login.invalidToken")));

				return Constants.CMD_EXEC_ERROR;
			}
		}

		String id = request.getParam(PARAM_USER_ID);
		String pass = request.getParam(PARAM_PASSWORD);
		String redirectPath = request.getParam(PARAM_BACK_URL);
		boolean rememberMe = isRememberMe(request);

		try {
			IdPasswordCredential cre = new IdPasswordCredential(id, pass);
			if (ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantAuthInfo.class).isUseRememberMe()) {
				cre.setAuthenticationFactor(RememberMeConstants.FACTOR_REMEMBER_ME_FLAG, rememberMe);
			}
			auth.login(cre);
			setRedirectPathAfterLogin(request, redirectPath);
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
			return handleCredentialExpiredException(request, id, null, null, redirectPath, rememberMe, e);
		}
	}

	static void setRedirectPathAfterLogin(RequestContext request, String redirectPath) {
		if (checkRedirectPath(redirectPath)) {
			//ログイン後画面として設定されている画面へ遷移
			request.setAttribute(RESULT_REDIRECT_PATH, redirectPath);
		} else {
			//トップ画面へ遷移
			Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
//			String menuUrl = tenant.getTenantDisplayInfo().getMenuUrl();
			String menuUrl = (tenant.getTenantConfig(TenantWebInfo.class) != null ?
					tenant.getTenantConfig(TenantWebInfo.class).getHomeUrl() : null);
			if (menuUrl != null && menuUrl.length() != 0) {
				request.setAttribute(RESULT_REDIRECT_PATH, TemplateUtil.getTenantContextPath() + menuUrl);
			} else {
				request.setAttribute(RESULT_REDIRECT_PATH, TemplateUtil.getTenantContextPath() + "/gem/");
			}
		}
	}

	static String handleCredentialExpiredException(RequestContext request, String id, String token, Credential secondaryCredential, String redirectPath, boolean rememberMe, CredentialExpiredException e) {
		if (ManagerLocator.getInstance().getManager(AuthManager.class).canUpdateCredential(e.getPolicyName())) {
			//パスワード変更画面へ

			//認証情報を一旦待避
			CredentialExpiredState state = new CredentialExpiredState(id, token, secondaryCredential,
					rememberMe && ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantAuthInfo.class).isUseRememberMe(),
					secondaryCredential != null, redirectPath, e.getPolicyName());
			request.getSession().setAttribute(SESSION_CREDENTIAL_EXPIRE_STATE, state);

			//パスワード有効期限切れの場合にエラーメッセージをセット
			if (!e.isInitialLogin()) {
				request.setAttribute(RESULT_ERROR, e);
			}

			//onetimeCodeの消費などをrollbackするため
			Transaction.getCurrent().setRollbackOnly();

			return CMD_EXEC_EXPIRE;
		} else {
			throw e;
		}
	}

	static boolean checkRedirectPath(String redirectPath) {
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

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
