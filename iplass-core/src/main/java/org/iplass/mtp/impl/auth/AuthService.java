/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth;

import static org.iplass.mtp.impl.util.CoreResourceBundleUtil.resourceString;

import java.sql.Timestamp;
import java.util.List;
import java.util.function.Supplier;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.AuthenticationProcessType;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialExpiredException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.login.LoginException;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.auth.policy.AccountNotificationListener;
import org.iplass.mtp.auth.policy.LoginNotification;
import org.iplass.mtp.auth.policy.definition.NotificationType;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AnonymousUserContext;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.DefaultUserSessionStore;
import org.iplass.mtp.impl.auth.authenticate.TemporaryUserContext;
import org.iplass.mtp.impl.auth.authenticate.UserSessionStore;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.internal.InternalAuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.trust.TrustedAuthValidateResult;
import org.iplass.mtp.impl.auth.authenticate.trust.TrustedAuthValidator;
import org.iplass.mtp.impl.auth.authorize.AuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.AuthorizationProvider;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService implements Service {
	private static Logger logger = LoggerFactory.getLogger(AuthService.class);

	public static final String MDC_USER = "user";
	public static final String USER_HANDLE_NAME = "mtp.auth.UserHandle";
	static final String HOLDER_NAME = "mtp.auth.authCotnextHolder";

	private static final int DEFAULT_AUTH_PROVIDER = 0;

	//認証プロバイダ名称 Accounthandle設定用
	private static final String PROVIDER_NAME = "providerName";

	private AuthenticationProvider[] authenticationProviders;
//	private AccountManagementModule[] amm;
	private AuthorizationProvider authorizationProvider;
	private UserSessionStore userSessionStore;

	private AuthenticationPolicyService authPolicyService;
	private AccountManagementModule allAmm;

	public AuthenticationProvider getAuthenticationProvider() {
		if (authenticationProviders.length == 1) {
			return authenticationProviders[DEFAULT_AUTH_PROVIDER];
		} else {
			//複数AuthenticationProviderの場合
			UserContext user = userSessionStore.getUserContext();
			if (user == null) {
				return authenticationProviders[DEFAULT_AUTH_PROVIDER];
			}
			return authenticationProviders[user.getAccount().getAuthenticationProviderIndex()];
		}
	}

	public AuthenticationProvider[] getAuthenticationProviders() {
		return authenticationProviders;
	}

	public AuthorizationProvider getAuthorizationProvider() {
		return authorizationProvider;
	}

	public UserSessionStore getUserSessionStore() {
		return userSessionStore;
	}

	public UserContext getCurrentSessionUserContext() {
		UserContext userContext = userSessionStore.getUserContext();
		return userContext;
	}

	public boolean isAuthenticate() {
		return getCurrentSessionUserContext() != null;
	}

	public final <T> T doSecuredAction(UserContext userContext, Supplier<T> action) {
		AuthContextHolder userAuthContext = null;
		if (userContext != null) {
			userAuthContext = newAuthContextHolder(userContext);
		}
		return doSecuredAction(userAuthContext, action);
	}

	public final <T> T doSecuredAction(AuthContextHolder userAuthContext, Supplier<T> action) {
		ExecuteContext exec = ExecuteContext.getCurrentContext();
		if (userAuthContext == null) {
			userAuthContext = newAuthContextHolder(new AnonymousUserContext());
		}
		boolean prevSecuredAction = userAuthContext.isSecuredAction();
		AuthContextHolder prev = null;
		try {
			prev = doSecuredActionPre(userAuthContext, exec);
			return action.get();
		} finally {
			doSecuredActionPost(userAuthContext, prevSecuredAction, prev, exec);
		}
	}
	
	public AuthContextHolder doSecuredActionPre(AuthContextHolder doAuthContext, ExecuteContext ec) {
		//AuthTagと処理を共有したいがためだけに、メソッド切り出し＆public
		AuthContextHolder prev = (AuthContextHolder) ec.getAttribute(HOLDER_NAME);
		ec.setAttribute(HOLDER_NAME, doAuthContext, false);
		ec.setClientId(doAuthContext.getUserContext().getIdForLog());
		ec.mdcPut(MDC_USER, ec.getClientId());
		doAuthContext.setSecuredAction(true);
		return prev;
	}
	
	public void doSecuredActionPost(AuthContextHolder doAuthContext, boolean prevSecuredAction, AuthContextHolder prev, ExecuteContext ec) {
		//AuthTagと処理を共有したいがためだけに、メソッド切り出し＆public
		if (doAuthContext != null) {
			doAuthContext.setSecuredAction(prevSecuredAction);
		}
		ec.setAttribute(HOLDER_NAME, prev, false);
		if (prev != null) {
			ec.setClientId(prev.getUserContext().getIdForLog());
		} else {
			ec.setClientId(null);
		}
		ec.mdcPut(MDC_USER, ec.getClientId());
	}

	public final void reloadUserEntity() {
		UserContext current = getCurrentSessionUserContext();

		if (current != null && current.getUser() != null) {
			User userEntity = authenticationProviders[current.getAccount().getAuthenticationProviderIndex()].getUserEntityResolver().searchUser(current.getAccount());
			if (userEntity == null) {
				throw new LoginFailedException(resourceString("impl.auth.AuthService.removed"));
			}
			current.resetUserEntity(userEntity);

			userSessionStore.setUserContext(current, false);
		}
	}

	/**
	 * セッションに紐付けない。認証のみ。成功した場合は、そのUserContextを返却。
	 *
	 * @param credential
	 * @return
	 */
	public UserContext authenticate(Credential credential) throws LoginFailedException, CredentialExpiredException {
		long time = 0L;
		if (logger.isDebugEnabled()) {
			time = System.currentTimeMillis();
		}

		AccountHandle account = null;
		User userEntity = null;
		int providerIndex = 0;
		while (userEntity == null) {
			//アカウントの存在チェック
			account = searchAccount(credential, providerIndex);
			providerIndex = account.getAuthenticationProviderIndex();

			//ユーザーエンティティ存在チェック
			userEntity = authenticationProviders[providerIndex].getUserEntityResolver().searchUser(account);
			if (userEntity != null && !isValidAuthProviderOnAuthPolicy(userEntity, providerIndex)) {
				userEntity = null;
			}
			if (userEntity == null && providerIndex + 1 == authenticationProviders.length) {
				//最後までチェックしたら
				break;
			}
			providerIndex++;
		}
		userEntity = validateUser(account.getCredential(), account, userEntity);

//		//認証プロバイダー決定のため設定
//		amm = new LoggingAccountManagementModule(authenticationProviders[account.getAuthenticationProviderIndex()], this);

//		//認証プロバイダー名設定
//		account.getAttributeMap().put(PROVIDER_NAME, authenticationProviders[account.getAuthenticationProviderIndex()].getProviderName());

		//UserContext生成
		UserContext user;
		if (userEntity.isTemporary()) {
			user = new TemporaryUserContext(account, userEntity);
		} else {
			user = new UserContextImpl(account, userEntity);
		}

		//ログイン成功の通知＆ログ出力
		authenticationProviders[account.getAuthenticationProviderIndex()].afterLoginSuccess(account);
		authenticationProviders[account.getAuthenticationProviderIndex()].getAuthLogger().loginSuccess(user);

		if (logger.isDebugEnabled()) {
			logger.debug("login process time:" + (System.currentTimeMillis() - time) + "ms.");
		}

		return user;
	}

	private boolean isValidAuthProviderOnAuthPolicy(User userEntity,
			int providerIndex) {

		String authPolicyName = userEntity.getAccountPolicy();
		AuthenticationPolicyRuntime apr = authPolicyService.getOrDefault(authPolicyName);
		if (apr == null) {
			return true;
		}
		if (apr.getMetaData().getAuthenticationProvider() == null || apr.getMetaData().getAuthenticationProvider().size() == 0) {
			return true;
		}
		for (String pn: apr.getMetaData().getAuthenticationProvider()) {
			if (pn.equals(authenticationProviders[providerIndex].getProviderName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ログインしてセッションを初期化する。
	 *
	 * @param credential
	 * @throws LoginFailedException
	 * @throws CredentialExpiredException
	 */
	public void login(Credential credential) throws LoginFailedException, CredentialExpiredException {//TODO LoginExceptionでよいか？？

		try {
			if (credential.getAuthenticationFactor(Credential.FACTOR_AUTHENTICATION_PROCESS_TYPE) == null) {
				credential.setAuthenticationFactor(Credential.FACTOR_AUTHENTICATION_PROCESS_TYPE, AuthenticationProcessType.LOGIN);
			}
			UserContext user = authenticate(credential);
			//セッション情報初期化
			initializeSession(user, true);

			AuthenticationPolicyRuntime policy = AuthContextHolder.getAuthContext().getPolicy();
			if (policy != null && policy.getListeners() != null) {
				LoginNotification notification = new LoginNotification(NotificationType.LOGIN_SUCCESS, user.getUser().getOid(), credential, null);
				for (AccountNotificationListener l: policy.getListeners()) {
					l.loginSuccess(notification);
				}
			}
		} catch (LoginFailedException e) {
			AuthenticationPolicyRuntime policy = authPolicyService.getOrDefault(null);
			if (policy != null) {
				if (policy != null && policy.getListeners() != null) {
					LoginNotification notification = new LoginNotification(NotificationType.LOGIN_FAILED, null, credential, e);
					for (AccountNotificationListener l: policy.getListeners()) {
						l.loginFailed(notification);
					}
				}
			}
			
			throw e;
		}
	}

	/**
	 * 現在のセッションが信頼された（当該セッション内にてID/passなどの信頼できるCredentialで認証された）ものかどうかを返す。
	 *
	 * @return
	 */
	public TrustedAuthValidateResult checkCurrentSessionTrusted() {
		UserContext user = getCurrentSessionUserContext();
		if (user == null) {
			user = new AnonymousUserContext();
		}
		TrustedAuthValidator tav = getAuthenticationProvider().getTrustedAuthValidator();
		if (tav == null) {
			if (user instanceof AnonymousUserContext) {
				//未ログインの場合は、信頼されてないとする
				//TODO 未指定の場合のデフォルトのCredentialは？？
				return new TrustedAuthValidateResult(false, IdPasswordCredential.class);
			} else {
				//ログイン済みでTrustedAuthValidator未指定の場合は、信頼済みとする
				return new TrustedAuthValidateResult(true, null);
			}
		}
		return tav.checkTrusted(user);
	}

	/**
	 * 再認証して、セッション上のユーザー情報を更新する（セッション自体は破棄しない）。
	 * 同一ユーザーではない（AccountHandleのunmodifiableUniqueKeyで判断）場合は、LoginFailedExceptionがスローされる。
	 *
	 * @param credential
	 * @throws LoginFailedException
	 * @throws CredentialExpiredException
	 */
	public void reAuth(Credential credential) throws LoginFailedException, CredentialExpiredException {
		UserContext pre = getCurrentSessionUserContext();
		if (pre == null || pre instanceof AnonymousUserContext) {
			throw new LoginFailedException(resourceString("impl.auth.AuthService.checkIdPass"));
		}
		credential.setAuthenticationFactor(Credential.FACTOR_AUTHENTICATION_PROCESS_TYPE, AuthenticationProcessType.TRUSTED_LOGIN);
		UserContext user = authenticate(credential);
		if (!pre.getAccount().getUnmodifiableUniqueKey().equals(user.getAccount().getUnmodifiableUniqueKey())) {
			authenticationProviders[pre.getAccount().getAuthenticationProviderIndex()].getAuthLogger().loginFail(credential, null);
			throw new LoginFailedException(resourceString("impl.auth.AuthService.checkIdPass"));
		}

		initializeSession(user, false);
	}

	private AccountHandle searchAccount(Credential credential, int startIndex) throws LoginFailedException, CredentialExpiredException {//TODO LoginExceptionでよいか？？
		AccountHandle account = null;
		try {
			for (int i = startIndex; i < authenticationProviders.length; i++) {
				account = authenticationProviders[i].login(credential);
				if (account != null) {
					account.setAuthenticationProviderIndex(i);
					//認証プロバイダー名設定
					account.getAttributeMap().put(PROVIDER_NAME, authenticationProviders[i].getProviderName());
					break;
				}
			}
		} catch (LoginException e) {
			getAuthenticationProvider().getAuthLogger().loginFail(credential, e);
			throw e;
		}

		if (account == null) {
			getAuthenticationProvider().getAuthLogger().loginFail(credential, null);
			throw new LoginFailedException(resourceString("impl.auth.AuthService.checkIdPass"));
		}

		if (account.isAccountLocked()) {
			authenticationProviders[account.getAuthenticationProviderIndex()].getAuthLogger().loginLocked(credential);
			throw new LoginFailedException(resourceString("impl.auth.AuthService.checkIdPass"));
		}
		return account;
	}

	private User validateUser(Credential credential, AccountHandle account, User userEntity) {
		//ユーザーエンティティ存在チェック
		if (userEntity == null) {
			authenticationProviders[account.getAuthenticationProviderIndex()].getAuthLogger().loginFail(credential, null);
			throw new LoginFailedException(resourceString("impl.auth.AuthService.checkIdPass"));
		//有効期間のチェック
		} else {
			Timestamp now = new Timestamp(InternalDateUtil.getNow().getTime());
			if (userEntity.getStartDate() != null) {
				if (now.compareTo((Timestamp) userEntity.getStartDate()) < 0) {
					authenticationProviders[account.getAuthenticationProviderIndex()].getAuthLogger().loginLocked(credential);
					throw new LoginFailedException(resourceString("impl.auth.AuthService.checkIdPass"));
				}
			}
			if (userEntity.getEndDate() != null) {
				if (now.compareTo((Timestamp) userEntity.getEndDate()) >= 0) {
					authenticationProviders[account.getAuthenticationProviderIndex()].getAuthLogger().loginLocked(credential);
					throw new LoginFailedException(resourceString("impl.auth.AuthService.checkIdPass"));
				}
			}
		}
		if (account.isExpired()) {
			authenticationProviders[account.getAuthenticationProviderIndex()].getAuthLogger().loginPasswordExpired(credential);
			CredentialExpiredException cee = new CredentialExpiredException(resourceString("impl.auth.AuthService.expired"));
			if (account.isInitialLogin()) {
				cee.setInitialLogin(true);
			}
			cee.setPolicyName(userEntity.getAccountPolicy());
			throw cee;
		}
		return userEntity;
	}

	void initializeSession(UserContext user, boolean withSessionInit) {
		if (withSessionInit && isAuthenticate()) {
			//SessionFixationProtectionMethod.CHANGE_SESSION_IDの場合の考慮。
			//既にログイン済みだった場合はSessionをクリア
			logout();
		}
		//既存セッションの破棄と生成
		userSessionStore.setUserContext(user, withSessionInit);

		//ExecuteContextから、login前のAuthContextをクリア
		AuthContextHolder.reflesh();

		//ExecuteContext/LogのユーザーID更新
		ExecuteContext exec = ExecuteContext.getCurrentContext();
		UserContext uc = AuthContextHolder.getAuthContext().getUserContext();
		exec.setClientId(uc.getIdForLog());
		exec.mdcPut(MDC_USER, exec.getClientId());
	}

	public void resetCredential(Credential credential) {
		getAccountManagementModule().resetCredential(credential);
	}

	public void resetCredential(Credential credential, String policyName) {
		getAccountManagementModule(policyName).resetCredential(credential);
	}

	public void updateCredential(Credential oldCredential, Credential newCredential) {
		getAccountManagementModule().updateCredential(oldCredential, newCredential);
	}

	public void updateCredential(Credential oldCredential, Credential newCredential, String policyName) {
		getAccountManagementModule(policyName).updateCredential(oldCredential, newCredential);
	}

	public AccountManagementModule getAccountManagementModule(String policyName) {
		AuthenticationPolicyRuntime apr = authPolicyService.getOrDefault(policyName);
		if (apr == null) {
			return AuthenticationProviderBase.NO_UPDATABLE_AMM;
		}
		AccountManagementModule ret = apr.getAccountManagementModule();
		if (ret == null) {
			ret = allAmm;
		}
		return ret;
	}

	public AccountManagementModule getAccountManagementModule() {
		return allAmm;
	}

	public void logout() {
		//既存セッションの破棄
		UserContext user = getCurrentSessionUserContext();
		if (user != null) {
			for (AuthenticationProvider ap: authenticationProviders) {
				ap.logout(user.getAccount());
			}
		}
		userSessionStore.invalidateUserSession();

		if (user != null) {
			for (AuthenticationProvider ap: authenticationProviders) {
				ap.afterLogout(user.getAccount());
			}
		}
	}

	public AuthorizationContext getAuthorizationContext(Permission permission) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		int tenantId = ec.getClientTenantId();
		if (authorizationProvider.useSharedPermission(permission)) {
			TenantContextService tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
			tenantId = tcService.getSharedTenantId();
		}
		return authorizationProvider.getAuthorizationContext(tenantId, permission);
	}

	AuthContextHolder newAuthContextHolder(UserContext userContext) {
		return new AuthContextHolder(userContext, this);
	}

	AuthContextHolder newAuthContextHolder() {
		//sessionから取得
		UserContext account = getCurrentSessionUserContext();
		if (account != null) {
			return newAuthContextHolder(account);
		} else {
			//anonymous
			return newAuthContextHolder(new AnonymousUserContext());
		}
	}

	@Override
	public void init(Config config) {

		userSessionStore = (UserSessionStore) config.getBean("userSessionStore");
		if (userSessionStore == null) {
			userSessionStore = new DefaultUserSessionStore();
			userSessionStore.inited(this, config);
		}

		List<?> authenticationProviderList = config.getBeans("authenticationProvider");
		if (authenticationProviderList != null) {
			authenticationProviders = authenticationProviderList.toArray(new AuthenticationProvider[authenticationProviderList.size() + 1]);
			//プログラム内部からの利用用途のAuthenticationProvider
			InternalAuthenticationProvider internal = new InternalAuthenticationProvider();
			internal.inited(this, config);
			authenticationProviders[authenticationProviders.length - 1] = internal;
		}

		AccountManagementModuleWrapper ammr = new AccountManagementModuleWrapper();
		for (int i = 0; i < authenticationProviders.length; i++) {
			ammr.add(authenticationProviders[i].getAccountManagementModule());
		}
		allAmm = new LoggingAccountManagementModule(ammr.stripOrThis());

		authorizationProvider = (AuthorizationProvider) config.getBean("authorizationProvider");
		authPolicyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
	}

	@Override
	public void destroy() {
	}

}
