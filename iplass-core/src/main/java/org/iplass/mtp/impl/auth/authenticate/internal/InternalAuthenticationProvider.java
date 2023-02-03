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

package org.iplass.mtp.impl.auth.authenticate.internal;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.DefaultUserEntityResolver;
import org.iplass.mtp.impl.auth.authenticate.UserEntityResolver;
import org.iplass.mtp.impl.auth.authenticate.trust.DefaultTrustedAuthValidator;
import org.iplass.mtp.impl.auth.authenticate.trust.TrustedAuthValidator;
import org.iplass.mtp.impl.auth.log.AuthLogger;
import org.iplass.mtp.impl.auth.log.AuthLoggerService;
import org.iplass.mtp.spi.Config;

/**
 * プログラム内部処理から、とあるユーザーとして認証したい場合に利用するAuthenticationProvider。
 * 
 * @author K.Higuchi
 *
 */
public class InternalAuthenticationProvider implements AuthenticationProvider {
	
	public static final String PROVIDER_NAME = "_internal";
	private AuthLogger authLogger;

	private String authLoggerName;
	
	private UserEntityResolver userEntityResolver;
	private TrustedAuthValidator trustedAuthValidator;

	@Override
	public UserEntityResolver getUserEntityResolver() {
		return userEntityResolver;
	}

	public String getAuthLoggerName() {
		return authLoggerName;
	}

	public void setAuthLoggerName(String authLoggerName) {
		this.authLoggerName = authLoggerName;
	}

	@Override
	public String getProviderName() {
	    return PROVIDER_NAME;
	}

	@Override
	public AuthLogger getAuthLogger() {
		return authLogger;
	}
	
	@Override
	public void inited(AuthService service, Config config) {
		authLogger = config.getDependentService(AuthLoggerService.class).getAuthLogger(authLoggerName);
		
		DefaultUserEntityResolver er = new DefaultUserEntityResolver();
		er.setEagerLoadReferenceProperty(DefaultUserEntityResolver.DEFAULT_EAGER_LOAD_REFERENCE_PROPERTY);
		er.setUnmodifiableUniqueKeyProperty(User.ACCOUNT_ID);
		er.inited(service, this);
		userEntityResolver = er;
		userEntityResolver.inited(service, this);
		trustedAuthValidator = new DefaultTrustedAuthValidator(InternalCreatedAccountHandle.class.getName(), InternalCredential.class.getName());
		trustedAuthValidator.inited(service, this);
	}

	@Override
	public void destroyed() {
	}

	@Override
	public AccountHandle login(Credential credential) {
		if (credential instanceof InternalCredential) {
			return new InternalCreatedAccountHandle(credential.getId());
		} else {
			return null;
		}
	}

	@Override
	public void logout(AccountHandle user) {
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return AuthenticationProviderBase.NO_UPDATABLE_AMM;
	}

	@Override
	public TrustedAuthValidator getTrustedAuthValidator() {
		return trustedAuthValidator;
	}

	@Override
	public void cleanupData() {
	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return InternalCredential.class;
	}

	@Override
	public boolean isSelectableOnAuthPolicy() {
		return false;
	}

}
