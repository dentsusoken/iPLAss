/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.rememberme;

import static org.iplass.mtp.impl.util.CoreResourceBundleUtil.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.auth.login.rememberme.RememberMeConstants;
import org.iplass.mtp.auth.login.rememberme.RememberMeTokenCredential;
import org.iplass.mtp.auth.login.rememberme.RememberMeTokenStolenException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginHandler;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginInstruction;
import org.iplass.mtp.impl.auth.authenticate.UserEntityResolver;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaRememberMePolicy;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenClientStore;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.auth.authenticate.trust.TrustedAuthValidator;
import org.iplass.mtp.impl.auth.log.AuthLogger;
import org.iplass.mtp.impl.command.RequestContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RememberMeTokenAuthenticationProvider implements AuthenticationProvider, AutoLoginHandler {

	private static final String REMMEFLAG = "mtp.auth.RememberMeTokenAuthenticationProvider.rememberMe";

	private static Logger logger = LoggerFactory.getLogger(RememberMeTokenAuthenticationProvider.class);

	private String providerName;
	private Boolean selectableOnAuthPolicy;

	private RememberMeTokenHandler tokenHandler;
	private boolean deleteTokenOnFailure = true;
	private String authTokenType = RememberMeTokenHandler.TYPE_REMME_DEFAULT;
//	private boolean autoRefreshToken;
//	private boolean regenerateTokenOnSuccess = true;
//	private long cleanupDelayPeriod;

	private AuthTokenClientStore clientStore;

	private RememberMeTokenAccountManagementModule amm = new RememberMeTokenAccountManagementModule();

	private AuthenticationProvider authenticationProvider;

	private TrustedAuthValidator trustedAuthValidator;

	private AutoLoginHandler autoLoginHandler;

	private AuthService service;

	private AuthenticationPolicyService authPolicyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
	private AuthTokenService tokenService = ServiceRegistry.getRegistry().getService(AuthTokenService.class);

	public AuthenticationProvider getAuthenticationProvider() {
		return authenticationProvider;
	}

	public void setAuthenticationProvider(
			AuthenticationProvider baseAuthenticationProvider) {
		this.authenticationProvider = baseAuthenticationProvider;
	}

	public boolean isDeleteTokenOnFailure() {
		return deleteTokenOnFailure;
	}

	public void setDeleteTokenOnFailure(boolean deleteTokenOnFailure) {
		this.deleteTokenOnFailure = deleteTokenOnFailure;
	}

	public AuthTokenClientStore getClientStore() {
		return clientStore;
	}
	public void setClientStore(AuthTokenClientStore clientStore) {
		this.clientStore = clientStore;
	}

	public String getAuthTokenType() {
		return authTokenType;
	}

	public void setAuthTokenType(String authTokenType) {
		this.authTokenType = authTokenType;
	}

	@Override
	public void inited(AuthService service, Config config) {
		if (trustedAuthValidator != null) {
			trustedAuthValidator.inited(service, this);
		}
		tokenHandler = (RememberMeTokenHandler) tokenService.getHandler(authTokenType);
		
		if (autoLoginHandler != null) {
			autoLoginHandler.inited(service, this);
		}

		this.service = service;
	}

	@Override
	public void destroyed() {
	}

	@Override
	public void cleanupData() {
		ExecuteContext ec = ExecuteContext.getCurrentContext();

		long now = ec.getCurrentTimestamp().getTime();

		if (logger.isDebugEnabled()) {
			logger.debug("creanup timeup token of tenant:" + ec.getClientTenantId());
		}

		List<String> names = authPolicyService.nameList();
		for (String name: names) {
			AuthenticationPolicyRuntime apr = authPolicyService.getRuntimeByName(name);
			if (apr.getMetaData().getRememberMePolicy() != null) {
				if (apr.getMetaData().getRememberMePolicy().getLifetimeMinutes() > 0) {
					long timeoutMillis = TimeUnit.MINUTES.toMillis(apr.getMetaData().getRememberMePolicy().getLifetimeMinutes());
					tokenHandler.authTokenStore().deleteByDate(ec.getClientTenantId(), authTokenType, new Timestamp(now - timeoutMillis));
				}
			}
		}
		authenticationProvider.cleanupData();
	}

	@Override
	public AccountHandle login(final Credential credential) {
		if (credential instanceof RememberMeTokenCredential) {
			RememberMeTokenCredential rmtc = (RememberMeTokenCredential) credential;
			AccountHandle account = null;
			int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
			if (rmtc.getToken() != null) {
				AuthToken crToken = new AuthToken();
				crToken.decodeToken(rmtc.getToken());
				if (!authTokenType.equals(crToken.getType())) {
					//another authProvider's token...
					return null;
				}

				if (crToken.getToken() == null) {
					if (deleteTokenOnFailure) {
						clientStore.clearToken();
					}
					//illegal format
					throw new LoginFailedException(resourceString("impl.auth.authenticate.rememberme.failed"));
				}
				AuthToken rmtoken = tokenHandler.authTokenStore().getBySeries(tenantId, authTokenType, crToken.getSeries());
				if (rmtoken == null) {
					if (deleteTokenOnFailure) {
						clientStore.clearToken();
					}
					throw new LoginFailedException(resourceString("impl.auth.authenticate.rememberme.failed"));
				}
				if (!tokenHandler.checkTokenValid(crToken.getToken(), rmtoken)) {
					//There is a possibility that token was stolen.
					if (deleteTokenOnFailure) {
						tokenHandler.authTokenStore().delete(tenantId, authTokenType, rmtoken.getOwnerId());
						clientStore.clearToken();
					}
					throw new RememberMeTokenStolenException(resourceString("impl.auth.authenticate.rememberme.tokenStolen"));
				}

				MetaRememberMePolicy pol = rememberMePolicy(rmtoken.getPolicyName());
				if (pol == null) {
					if (deleteTokenOnFailure) {
						tokenHandler.authTokenStore().delete(tenantId, authTokenType, rmtoken.getOwnerId());
						clientStore.clearToken();
					}
//					return null;
					throw new LoginFailedException(resourceString("impl.auth.authenticate.rememberme.failed"));
				}
				//check timeout
				int maxAgeSeconds = maxAgeSeconds(rmtoken, pol.getLifetimeMinutes());
				if (maxAgeSeconds <= 0) {
					if (deleteTokenOnFailure) {
						clientStore.clearToken();
					}
					throw new LoginFailedException(resourceString("impl.auth.authenticate.rememberme.expired"));
				}

				AuthToken newToken = nextToken(rmtoken, !pol.isAbsoluteLifetime());
				final int newMaxAgeSeconds = maxAgeSeconds(newToken, pol.getLifetimeMinutes());

				//for sync db and cookie, require new transaction
				Transaction.requiresNew(t -> {
					tokenHandler.authTokenStore().update(newToken, rmtoken);
					t.afterCommit(() -> {
						clientStore.setToken(newToken.encodeToken(), newMaxAgeSeconds);
					});
				});
				account = new RememberMeTokenAccountHandle(newToken.getOwnerId(), newToken.getSeries(), newToken.getPolicyName());
			} else {
				throw new IllegalArgumentException("specify token");
			}

			return account;

		} else {
			final AccountHandle account = authenticationProvider.login(credential);
			if (account != null) {
				Boolean remMeFlag = (Boolean) credential.getAuthenticationFactor(RememberMeConstants.FACTOR_REMEMBER_ME_FLAG);
				if (remMeFlag != null && remMeFlag.booleanValue()) {
					account.getAttributeMap().put(REMMEFLAG, true);
				}
			}
			return account;
		}
	}

	@Override
	public void afterLoginSuccess(AccountHandle account) {
		Boolean remMeFlag = (Boolean) account.getAttributeMap().get(REMMEFLAG);
		if (remMeFlag != null && remMeFlag.booleanValue()) {
			String policyName = policyName(account);
			MetaRememberMePolicy rememberMePolicy = rememberMePolicy(policyName);
			if (rememberMePolicy != null && rememberMePolicy.getLifetimeMinutes() > 0) {
				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();

				//for sync db and cookie, require new transaction
				Transaction.requiresNew(t -> {
					if (!deleteTokenOnFailure) {
						//delete old series token
						RequestContext req = RequestContextHolder.getCurrent();
						if (req != null) {
							AuthToken seriesToken = new AuthToken(clientStore.getToken());
							if (seriesToken != null) {
								tokenHandler.authTokenStore().deleteBySeries(tenantId, authTokenType, seriesToken.getSeries());
							}
						}
					}

					//create RememberMeToken
					AuthToken newToken = tokenHandler.newAuthToken(account.getUnmodifiableUniqueKey(), policyName, null);
					tokenHandler.authTokenStore().create(newToken);
					t.afterCommit(() -> {
						clientStore.setToken(newToken.encodeToken(), maxAgeSeconds(newToken, rememberMePolicy.getLifetimeMinutes()));
					});
				});

			} else {
				clientStore.clearToken();
			}
		}
		
		authenticationProvider.afterLoginSuccess(account);
		
	}

	private int maxAgeSeconds(AuthToken token, long lifetimeMinutes) {
		if (lifetimeMinutes > 0) {
			return (int) ((token.getStartDate().getTime() + TimeUnit.MINUTES.toMillis(lifetimeMinutes) - System.currentTimeMillis()) / 1000);
		} else {
			return Integer.MAX_VALUE;
		}
	}

	@Override
	public void afterLogout(AccountHandle user) {
		authenticationProvider.afterLogout(user);
	}

	private String policyName(AccountHandle account) {
		return (String) account.getAttributeMap().get(User.ACCOUNT_POLICY);
	}

	private MetaRememberMePolicy rememberMePolicy(String policyName) {
		AuthenticationPolicyRuntime pol = authPolicyService.getOrDefault(policyName);
		if (pol == null) {
			return null;
		}
		return pol.getMetaData().getRememberMePolicy();
	}

	@Override
	public void logout(final AccountHandle user) {
		Transaction.requiresNew(t -> {
			clientStore.clearToken();
			tokenHandler.authTokenStore().delete(ExecuteContext.getCurrentContext().getClientTenantId(), authTokenType, user.getUnmodifiableUniqueKey());
		});
		
		authenticationProvider.logout(user);
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return amm;
	}

	@Override
	public boolean isSelectableOnAuthPolicy() {
		if (selectableOnAuthPolicy == null) {
			return authenticationProvider.isSelectableOnAuthPolicy();
		} else {
			return selectableOnAuthPolicy;
		}
	}

	public void setSelectableOnAuthPolicy(boolean selectableOnAuthPolicy) {
		this.selectableOnAuthPolicy = selectableOnAuthPolicy;
	}

	@Override
	public String getProviderName() {
		if (providerName == null) {
			return authenticationProvider.getProviderName();
		} else {
			return providerName;
		}
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	@Override
	public AuthLogger getAuthLogger() {
		return authenticationProvider.getAuthLogger();
	}

	@Override
	public UserEntityResolver getUserEntityResolver() {
		return authenticationProvider.getUserEntityResolver();
	}

	public void setTrustedAuthValidator(TrustedAuthValidator trustedAuthValidator) {
		this.trustedAuthValidator = trustedAuthValidator;
	}

	@Override
	public TrustedAuthValidator getTrustedAuthValidator() {
		if (trustedAuthValidator == null && authenticationProvider != null) {
			return authenticationProvider.getTrustedAuthValidator();
		}
		return trustedAuthValidator;
	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return authenticationProvider.getCredentialType();
	}

	private class RememberMeTokenAccountManagementModule implements AccountManagementModule {

		private RememberMeTokenAccountManagementModule() {
		}

		private AccountManagementModule am() {
			AccountManagementModule am = authenticationProvider.getAccountManagementModule();
			if (am == null) {
				//TODO isOK?
				logger.warn("No AccountManagementModule is specified, so use NO_UPDATABLE_AMM");
				am = AuthenticationProviderBase.NO_UPDATABLE_AMM;
			}
			return am;
		}

		@Override
		public boolean canCreate() {
			return am().canCreate();
		}

		@Override
		public boolean canUpdate() {
			return am().canUpdate();
		}

		@Override
		public boolean canRemove() {
			return true;
		}

		@Override
		public boolean canRestore() {
			return am().canRestore();
		}

		@Override
		public boolean canPurge() {
			return am().canPurge();
		}

		@Override
		public boolean canUpdateCredential() {
			return am().canUpdateCredential();
		}

		@Override
		public boolean canResetCredential() {
			return am().canResetCredential();
		}

		@Override
		public void create(User user) {
			am().create(user);
		}

		@Override
		public void afterCreate(User user) {
			am().afterCreate(user);
		}

		@Override
		public void update(User user, List<String> updateProperties) {
			am().update(user, updateProperties);
		}

		@Override
		public void afterUpdate(User user, String policyName, List<String> updateProperties) {
			am().afterUpdate(user, policyName, updateProperties);
		}

		@Override
		public void remove(User user) {
			if (am().canRemove()) {
				am().remove(user);
			}
			UserEntityResolver uer = getUserEntityResolver();
			tokenHandler.authTokenStore().delete(ExecuteContext.getCurrentContext().getClientTenantId(), authTokenType, user.getValue(uer.getUnmodifiableUniqueKeyProperty()).toString());
		}

		@Override
		public void restore(User user) {
			am().restore(user);
		}

		@Override
		public void purge(User user) {
			am().purge(user);
		}

		@Override
		public void updateCredential(Credential oldCredential,
				Credential newCredential) throws CredentialUpdateException {
			am().updateCredential(oldCredential, newCredential);
		}

		@Override
		public void resetCredential(Credential credential)
				throws CredentialUpdateException {
			am().resetCredential(credential);
		}

		@Override
		public boolean canResetLockoutStatus() {
			return am().canResetLockoutStatus();
		}

		@Override
		public void resetLockoutStatus(String accountId) {
			am().resetLockoutStatus(accountId);
		}

	}

	@Override
	public AutoLoginHandler getAutoLoginHandler() {
		if (autoLoginHandler == null) {
			return this;
		} else {
			return autoLoginHandler;
		}
	}

	public void setAutoLoginHandler(AutoLoginHandler autoLoginHandler) {
		this.autoLoginHandler = autoLoginHandler;
	}

	@Override
	public Exception handleException(AutoLoginInstruction ali, ApplicationException e, RequestContext req, boolean isLogined, UserContext user) {
		//cookie削除されない場合、エラーを通知後、再ログインしようとしても、またログイン失敗する。なので、エラー表示せずそのまま

		if (deleteTokenOnFailure && e instanceof RememberMeTokenStolenException) {
			return e;
		}
		
		if (ali.getCredential() instanceof RememberMeTokenCredential) {
			return null;
		}

		if (authenticationProvider.getAutoLoginHandler() != null) {
			return authenticationProvider.getAutoLoginHandler().handleException(ali, e, req, isLogined, user);
		} else {
			return null;
		}
	}
	
	@Override
	public void handleSuccess(AutoLoginInstruction ali, RequestContext req, UserContext user) {
		if (!(ali.getCredential() instanceof RememberMeTokenCredential)) {
			if (authenticationProvider.getAutoLoginHandler() != null) {
				authenticationProvider.getAutoLoginHandler().handleSuccess(ali, req, user);
			}
		}
	}

	@Override
	public AutoLoginInstruction handle(RequestContext req, boolean isLogined, UserContext user) {
		if (!isLogined) {
			Tenant t = ExecuteContext.getCurrentContext().getCurrentTenant();
			if (t != null && t.getTenantConfig(TenantAuthInfo.class) != null 
					&& t.getTenantConfig(TenantAuthInfo.class).isUseRememberMe()) {
				AuthToken token = new AuthToken(clientStore.getToken());
				if (authTokenType.equals(token.getType()) && token.getSeries() != null) {
					return new AutoLoginInstruction(new RememberMeTokenCredential(token.encodeToken()));
				}
			}
		}

		if (authenticationProvider.getAutoLoginHandler() != null) {
			return authenticationProvider.getAutoLoginHandler().handle(req, isLogined, user);
		} else {
			return AutoLoginInstruction.THROUGH;
		}
	}

	private AuthToken nextToken(AuthToken previousToken, boolean updateDate) {
		Timestamp startDate;
		if (updateDate) {
			startDate = new Timestamp(System.currentTimeMillis());
		} else {
			startDate = previousToken.getStartDate();
		}
		return new AuthToken(previousToken.getTenantId(), previousToken.getType(), previousToken.getOwnerId(), previousToken.getSeries(), tokenHandler.newTokenString(null), previousToken.getPolicyName(), startDate, previousToken.getDetails());
	}
}
