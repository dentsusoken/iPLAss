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
package org.iplass.mtp.impl.auth.oauth;

import java.util.List;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.UserEntityResolver;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;

public class AccessTokenAuthenticationProvider extends AuthenticationProviderBase {
	
	private AccessTokenAccountManagementModule amm = new AccessTokenAccountManagementModule();
	private OAuthAuthorizationService authorizationService = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
	private AuthenticationPolicyService authPolicyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
	
	private Class<? extends Credential> credentialTypeForTrust;
	private Class<? extends AccountHandle> accountHandleClassForTrust;
	

	public void setCredentialTypeForTrust(Class<? extends Credential> credentialTypeForTrust) {
		this.credentialTypeForTrust = credentialTypeForTrust;
	}

	public void setAccountHandleClassForTrust(Class<? extends AccountHandle> accountHandleClassForTrust) {
		this.accountHandleClassForTrust = accountHandleClassForTrust;
	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return AccessTokenCredential.class;
	}

	@Override
	protected Class<? extends Credential> getCredentialTypeForTrust() {
		return credentialTypeForTrust;
	}
	
	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return accountHandleClassForTrust;
	}
	
	@Override
	public void inited(AuthService service, Config config) {
		super.inited(service, config);
		
		setUserEntityResolver(new AccessTokenUserEntityResolver(getUserEntityResolver()));
	}

	@Override
	public void destroyed() {
	}

	@Override
	public void cleanupData() {
	}

	@Override
	public AccountHandle login(final Credential credential) {
		if (credential instanceof AccessTokenCredential) {
			AccessTokenCredential cre = (AccessTokenCredential) credential;
			if (cre.getToken() == null) {
				throw new IllegalArgumentException("specify token");
			}
			
			AccessToken token = authorizationService.getAccessTokenStore().getAccessToken(cre.getToken());
			if (token == null) {
				return null;
			}
			
			if (token.getExpiresIn() <= 0) {
				return null;
			}
			
			UserEntityResolver uer = getUserEntityResolver();
			User user = token.getUser();
			String oid = user.getValue(uer.getUnmodifiableUniqueKeyProperty()).toString();
			AuthenticationPolicyRuntime pol = authPolicyService.getOrDefault(user.getAccountPolicy());
			if (pol == null) {
				return null;
			}
			
			return new AccessTokenAccountHandle(oid, token, pol.getMetaData().getName());
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

	private class AccessTokenAccountManagementModule implements AccountManagementModule {

		private AccessTokenAccountManagementModule() {
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
			authorizationService.getAccessTokenStore().revokeTokenByUserOid(user.getValue(uer.getUnmodifiableUniqueKeyProperty()).toString());
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

	public class AccessTokenUserEntityResolver implements UserEntityResolver {
		
		private UserEntityResolver actual;
		
		private AccessTokenUserEntityResolver(UserEntityResolver actual) {
			this.actual = actual;
		}

		@Override
		public void inited(AuthService service, AuthenticationProvider provider) {
		}

		@Override
		public User searchUser(AccountHandle account) {
			AccessTokenAccountHandle atah = (AccessTokenAccountHandle) account;
			return atah.getAccessToken().getUser();
		}

		@Override
		public String getUnmodifiableUniqueKeyProperty() {
			return actual.getUnmodifiableUniqueKeyProperty();
		}
		
		public UserEntityResolver getActual() {
			return actual;
		}
		
	}
}
