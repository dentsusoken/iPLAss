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

package org.iplass.mtp.impl.auth.authenticate;

import java.util.List;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.trust.DefaultTrustedAuthValidator;
import org.iplass.mtp.impl.auth.authenticate.trust.TrustedAuthValidator;
import org.iplass.mtp.impl.auth.log.AuthLogger;
import org.iplass.mtp.impl.auth.log.AuthLoggerService;
import org.iplass.mtp.spi.Config;

public abstract class AuthenticationProviderBase implements AuthenticationProvider {
	
	public static AccountManagementModule NO_UPDATABLE_AMM = new AccountManagementModule() {
		@Override
		public void updateCredential(Credential oldCredential,
				Credential newCredential) throws CredentialUpdateException {
		}
		@Override
		public void update(User user, List<String> updateProperties) {
		}
		@Override
		public void restore(User user) {
		}
		@Override
		public void resetCredential(Credential credential)
				throws CredentialUpdateException {
		}
		@Override
		public void remove(User user) {
		}
		@Override
		public void purge(User user) {
		}
		@Override
		public void create(User user) {
		}
		@Override
		public boolean canUpdateCredential() {
			return false;
		}
		@Override
		public boolean canUpdate() {
			return false;
		}
		@Override
		public boolean canRestore() {
			return false;
		}
		@Override
		public boolean canResetCredential() {
			return false;
		}
		@Override
		public boolean canRemove() {
			return false;
		}
		@Override
		public boolean canPurge() {
			return false;
		}
		@Override
		public boolean canCreate() {
			return false;
		}
		@Override
		public void afterCreate(User user) {
		}
		@Override
		public boolean canResetLockoutStatus() {
			return false;
		}
		@Override
		public void resetLockoutStatus(String accountId) {
		}
		@Override
		public void afterUpdate(User user, String policyName, List<String> updateProperties) {
		}
	};
	
	private AuthLogger authLogger;

	/** アカウント管理モジュール使用フラグ */
	private boolean selectableOnAuthPolicy = true;

	/** プロバイダ名称 */
	private String providerName;
	
	private String authLoggerName;
	
	private UserEntityResolver userEntityResolver;
	private TrustedAuthValidator trustedAuthValidator;
	private AutoLoginHandler autoLoginHandler;
	
	public AuthenticationProviderBase() {
	}

	@Override
	public AutoLoginHandler getAutoLoginHandler() {
		return autoLoginHandler;
	}

	public void setAutoLoginHandler(AutoLoginHandler autoLoginHandler) {
		this.autoLoginHandler = autoLoginHandler;
	}

	public TrustedAuthValidator getTrustedAuthValidator() {
		return trustedAuthValidator;
	}

	public void setTrustedAuthValidator(TrustedAuthValidator trustedAuthValidator) {
		this.trustedAuthValidator = trustedAuthValidator;
	}

	@Override
	public UserEntityResolver getUserEntityResolver() {
		return userEntityResolver;
	}

	public void setUserEntityResolver(UserEntityResolver userEntityResolver) {
		this.userEntityResolver = userEntityResolver;
	}

	public String getAuthLoggerName() {
		return authLoggerName;
	}

	public void setAuthLoggerName(String authLoggerName) {
		this.authLoggerName = authLoggerName;
	}

	@Override
	public boolean isSelectableOnAuthPolicy() {
		return selectableOnAuthPolicy;
	}

	public void setSelectableOnAuthPolicy(boolean selectableOnAuthPolicy) {
		this.selectableOnAuthPolicy = selectableOnAuthPolicy;
	}
	
	@Override
	public String getProviderName() {
	    return providerName;
	}

	public void setProviderName(String providerName) {
	    this.providerName = providerName;
	}

	@Override
	public AuthLogger getAuthLogger() {
		return authLogger;
	}
	
	@Override
	public void cleanupData() {
	}

	@Override
	public void inited(AuthService service, Config config) {
		authLogger = config.getDependentService(AuthLoggerService.class).getAuthLogger(authLoggerName);
		
		if (userEntityResolver == null) {
			DefaultUserEntityResolver er = new DefaultUserEntityResolver();
			er.setEagerLoadReferenceProperty(DefaultUserEntityResolver.DEFAULT_EAGER_LOAD_REFERENCE_PROPERTY);
			er.setUnmodifiableUniqueKeyProperty(User.OID);
			userEntityResolver = er;
		}
		userEntityResolver.inited(service, this);
		
		if (trustedAuthValidator == null) {
			trustedAuthValidator = new DefaultTrustedAuthValidator(getAccountHandleClassForTrust().getName(), getCredentialTypeForTrust().getName());
		}
		trustedAuthValidator.inited(service, this);
	}
	
	protected abstract Class<? extends AccountHandle> getAccountHandleClassForTrust();
	protected Class<? extends Credential> getCredentialTypeForTrust() {
		return getCredentialType();
	}

	@Override
	public void destroyed() {
	}

}
