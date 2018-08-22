/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.jaas;

import java.io.IOException;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.DefaultUserEntityResolver;
import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaasAuthenticationProvider extends AuthenticationProviderBase {

	public static final String DEFAULT_JAAS_CONFIG_ENTRY_NAME = "mtplogin";

	private static Logger logger = LoggerFactory.getLogger(JaasAuthenticationProvider.class);
	
	private String entryName;
	private Class<? extends Principal> uniquePrincipalType;

	public Class<? extends Principal> getUniquePrincipalType() {
		return uniquePrincipalType;
	}

	public void setUniquePrincipalType(Class<? extends Principal> uniquePrincipalType) {
		this.uniquePrincipalType = uniquePrincipalType;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	@Override
	public AccountHandle login(Credential credential) {

		if (!(credential instanceof IdPasswordCredential)) {
			return null;
//			throw new SystemException("JaasAuthenticationProvider supports only IdPasswordCredential");
		}
		final IdPasswordCredential idPass = (IdPasswordCredential) credential;

		//ログインコンテキストの生成
		Subject tSubject = new Subject();
		LoginContext tLoginContext = null;
		try {
			tLoginContext = new LoginContext(entryName, tSubject, new CallbackHandler() {
				public void handle(Callback[] aCallbacks) throws IOException, UnsupportedCallbackException {
					for (Callback tCallback : aCallbacks) {
						if (tCallback instanceof NameCallback) {
							NameCallback tNameCallback = (NameCallback) tCallback;
							tNameCallback.setName(idPass.getId());
						} else if (tCallback instanceof PasswordCallback) {
							PasswordCallback tPasswordCallback = (PasswordCallback) tCallback;
							tPasswordCallback.setPassword(idPass.getPassword().toCharArray());
						} else {
							throw new UnsupportedCallbackException(tCallback, "Unrecognized callback");
						}
					}
				}
			});
		} catch (LoginException e) {
			throw new SystemException("fail to create JAAS LoginContext", e);
		}

		try {
			tLoginContext.login();
		} catch (LoginException e) {
			logger.debug("login failed.", e);
			return null;
		}

		return new JaasAccountHandle(idPass.getId(), uniquePrincipalType, tLoginContext);
	}

	@Override
	public void logout(AccountHandle user) {
		if (user instanceof JaasAccountHandle) {
			JaasAccountHandle jaasUser = (JaasAccountHandle) user;
			if (jaasUser.getLoginContext() != null) {
				try {
					jaasUser.getLoginContext().logout();
				} catch (LoginException e) {
					logger.warn("fail logout process of " + user.getCredential().getId() + " cause " + e.toString());
				}
			} else {
				logger.warn("fail logout process of " + user.getCredential().getId() + " cause no LoginContext. Maybe session failover occured.");
			}
		}
	}
	
	@Override
	public Class<? extends Credential> getCredentialType() {
		return IdPasswordCredential.class;
	}

	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return JaasAccountHandle.class;
	}

	@Override
	public void destroyed() {
		super.destroyed();
	}

	@Override
	public void inited(AuthService s, Config config) {
		boolean userEntityResolverIsNull = getUserEntityResolver() == null;
		
		super.inited(s, config);
		
		if (userEntityResolverIsNull) {
			//DefaultUserEntityResolverのkeyをaccountIdに。
			logger.warn("userEntityResolver not specified, so use DefaultUserEntityResolver and User's accountId as unmodifiableUniqueKeyProperty");
			((DefaultUserEntityResolver) getUserEntityResolver()).setUnmodifiableUniqueKeyProperty(User.ACCOUNT_ID);
		}
		
		if (entryName == null) {
			logger.debug("entryName not specified. use default entryName:" + DEFAULT_JAAS_CONFIG_ENTRY_NAME);
			entryName = DEFAULT_JAAS_CONFIG_ENTRY_NAME;
		}
		
		if (uniquePrincipalType == null) {
			logger.warn("uniquePrincipalType not specified, so use login id as uniqueKey");
		}
		
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return NO_UPDATABLE_AMM;
	}

}
