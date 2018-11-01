/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.simpletoken;

import static org.iplass.mtp.impl.util.CoreResourceBundleUtil.resourceString;

import java.util.List;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.auth.login.token.SimpleAuthTokenCredential;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.UserEntityResolver;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleAuthTokenAuthenticationProvider extends AuthenticationProviderBase  {

	private static Logger logger = LoggerFactory.getLogger(SimpleAuthTokenAuthenticationProvider.class);

	private SimpleAuthTokenAccountManagementModule amm = new SimpleAuthTokenAccountManagementModule();
	
	private SimpleAuthTokenHandler tokenHandler;
	private String authTokenType = SimpleAuthTokenHandler.TYPE_SIMPLE_DEFAULT;
	private Class<? extends Credential> credentialTypeForTrust;
	private Class<? extends AccountHandle> accountHandleClassForTrust;
	
	private AuthenticationPolicyService authPolicyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
	private AuthTokenService tokenService = ServiceRegistry.getRegistry().getService(AuthTokenService.class);

	public void setCredentialTypeForTrust(Class<? extends Credential> credentialTypeForTrust) {
		this.credentialTypeForTrust = credentialTypeForTrust;
	}

	public void setAccountHandleClassForTrust(Class<? extends AccountHandle> accountHandleClassForTrust) {
		this.accountHandleClassForTrust = accountHandleClassForTrust;
	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return SimpleAuthTokenCredential.class;
	}

	@Override
	protected Class<? extends Credential> getCredentialTypeForTrust() {
		return credentialTypeForTrust;
	}
	
	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return accountHandleClassForTrust;
	}
	
	public String getAuthTokenType() {
		return authTokenType;
	}

	public void setAuthTokenType(String authTokenType) {
		this.authTokenType = authTokenType;
	}

	@Override
	public void inited(AuthService service, Config config) {
		super.inited(service, config);
		tokenHandler = (SimpleAuthTokenHandler) tokenService.getHandler(authTokenType);
	}

	@Override
	public void destroyed() {
	}

	@Override
	public void cleanupData() {
	}

	@Override
	public AccountHandle login(final Credential credential) {
		if (credential instanceof SimpleAuthTokenCredential) {
			SimpleAuthTokenCredential cre = (SimpleAuthTokenCredential) credential;
			if (cre.getToken() == null) {
				throw new IllegalArgumentException("specify token");
			}
			
			int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
			if (cre.getToken() != null) {
				AuthToken crToken = new AuthToken();
				crToken.decodeToken(cre.getToken());
				if (!authTokenType.equals(crToken.getType())) {
					//another authProvider's token...
					if (logger.isDebugEnabled()) {
						logger.debug("no match token type:" + crToken.getType() + " so continue to another AuthProvider");
					}
					return null;
				}
				
				if (crToken.getToken() == null) {
					//illegal format
					throw new LoginFailedException(resourceString("impl.auth.authenticate.rememberme.failed"));
				}
				AuthToken stToken = tokenHandler.authTokenStore().getBySeries(tenantId, authTokenType, crToken.getSeries());
				if (stToken == null) {
					throw new LoginFailedException(resourceString("impl.auth.authenticate.rememberme.failed"));
				}
				if (!tokenHandler.checkTokenValid(crToken.getToken(), stToken)) {
					throw new LoginFailedException(resourceString("impl.auth.authenticate.rememberme.failed"));
				}
				
				AuthenticationPolicyRuntime pol = authPolicyService.getOrDefault(stToken.getPolicyName());
				if (pol == null) {
					throw new LoginFailedException(resourceString("impl.auth.authenticate.rememberme.failed"));
				}
				
				return new SimpleAuthTokenAccountHandle(stToken.getOwnerId(), stToken.getSeries(), stToken.getPolicyName());
			}
		}
		
		return null;
	}
	
	@Override
	public void logout(final AccountHandle user) {
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return amm;
	}

	private class SimpleAuthTokenAccountManagementModule implements AccountManagementModule {

		private SimpleAuthTokenAccountManagementModule() {
		}

		@Override
		public boolean canCreate() {
			return false;
		}

		@Override
		public boolean canUpdate() {
			return false;
		}

		@Override
		public boolean canRemove() {
			return true;
		}

		@Override
		public boolean canRestore() {
			return false;
		}

		@Override
		public boolean canPurge() {
			return false;
		}

		@Override
		public boolean canUpdateCredential() {
			return false;
		}

		@Override
		public boolean canResetCredential() {
			return false;
		}

		@Override
		public void create(User user) {
		}

		@Override
		public void afterCreate(User user) {
		}

		@Override
		public void update(User user, List<String> updateProperties) {
		}

		@Override
		public void remove(User user) {
			UserEntityResolver uer = getUserEntityResolver();
			tokenHandler.authTokenStore().delete(ExecuteContext.getCurrentContext().getClientTenantId(), authTokenType, user.getValue(uer.getUnmodifiableUniqueKeyProperty()).toString());
		}

		@Override
		public void restore(User user) {
		}

		@Override
		public void purge(User user) {
		}

		@Override
		public void updateCredential(Credential oldCredential,
				Credential newCredential) throws CredentialUpdateException {
		}

		@Override
		public void resetCredential(Credential credential)
				throws CredentialUpdateException {
		}

		@Override
		public boolean canResetLockoutStatus() {
			return false;
		}

		@Override
		public void resetLockoutStatus(String accountId) {
		}

	}

}
