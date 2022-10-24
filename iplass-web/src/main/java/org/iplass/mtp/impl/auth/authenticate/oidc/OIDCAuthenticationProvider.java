/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.oidc;

import static org.iplass.mtp.impl.web.WebResourceBundleUtil.resourceString;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.DefaultUserEntityResolver;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.MetaOpenIdConnect.OpenIdConnectRuntime;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OIDCAuthenticationProvider extends AuthenticationProviderBase {
	private static final Logger authLog = LoggerFactory.getLogger("mtp.auth.oidc");
	
	private OpenIdConnectService oidcService;
	private AuthenticationPolicyService policyService;

	@Override
	public void inited(AuthService service, Config config) {
		if (getUserEntityResolver() == null) {
			OIDCUserEntityResolver er = new OIDCUserEntityResolver();
			er.setEagerLoadReferenceProperty(DefaultUserEntityResolver.DEFAULT_EAGER_LOAD_REFERENCE_PROPERTY);
			setUserEntityResolver(er);
		}
		super.inited(service, config);
		oidcService = ServiceRegistry.getRegistry().getService(OpenIdConnectService.class);
		policyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return NO_UPDATABLE_AMM;
	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return OIDCCredential.class;
	}

	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return OIDCAccountHandle.class;
	}

	@Override
	public AccountHandle login(Credential credential) {
		if (!(credential instanceof OIDCCredential)) {
			return null;
		}
		
		OIDCCredential cre = (OIDCCredential) credential;
		OpenIdConnectRuntime oidcRuntime = oidcService.getOrDefault(cre.getOpenIdConnectDefinitionName());
		
		OIDCValidateResult vr = oidcRuntime.validate(cre);
		if (!vr.isValid()) {
			if (authLog.isDebugEnabled()) {
				if (vr.getRootCause() == null) {
					authLog.debug("OIDC failed:error=" + vr.getError() + ", errorDescription=" + vr.getErrorDescription());
				} else {
					authLog.debug("OIDC failed:error=" + vr.getError() + ", errorDescription=" + vr.getErrorDescription() + "exception=" + vr.getRootCause(), vr.getRootCause());
				}
			}
			
			OIDCRuntimeException ore;
			if (vr.getRootCause() == null) {
				ore = new OIDCRuntimeException(vr.getError() + ":" + vr.getErrorDescription());
			} else {
				ore = new OIDCRuntimeException(vr.getError() + ":" + vr.getErrorDescription(), vr.getRootCause());
			}
			throw new LoginFailedException(resourceString("impl.auth.authenticate.oidc.OIDCAuthenticationProvider.error", "Invalid response from OpenID Provider.", "invalid_response"), ore);
		}
		
		OIDCAccountHandle ah = new OIDCAccountHandle(vr.getSubjectId(), vr.getSubjectName(),
				oidcRuntime.getMetaData().getName(), vr.getClaims(), vr.getAccessToken(), vr.getExpiresIn(), vr.getRefreshToken(), vr.getScopes());

		User user = getUserEntityResolver().searchUser(ah);
		if (user == null) {
			if (oidcRuntime.getAutoUserProvisioningHandler() != null) {
				user = AuthContext.doPrivileged(() -> {
					String userOid = oidcRuntime.getAutoUserProvisioningHandler().createUser(vr.getSubjectId(), vr.getSubjectName(), ah.getAttributeMap());
					if (userOid != null) {
						oidcRuntime.connect(userOid, vr);
						return getUserEntityResolver().searchUser(ah);
					} else {
						return null;
					}
				});
			}
			
			if (user == null) {
				throw new LoginFailedException(resourceString("impl.auth.authenticate.oidc.OIDCAuthenticationProvider.error", "", "account_not_available"));
			}

		} else {
			if (oidcRuntime.getAutoUserProvisioningHandler() != null) {
				final User refUser = user;
				user = AuthContext.doPrivileged(() -> {
					oidcRuntime.getAutoUserProvisioningHandler().updateUser(refUser, vr.getSubjectId(), vr.getSubjectName(), ah.getAttributeMap());
					return getUserEntityResolver().searchUser(ah);
				});
			}
		}

		AuthenticationPolicyRuntime userPolicy = policyService.getOrDefault(user.getAccountPolicy());

		//check policy
		if (!oidcRuntime.isAllowedOnPolicy(userPolicy)) {
			throw new LoginFailedException(resourceString("impl.auth.authenticate.oidc.OIDCAuthenticationProvider.error", "", "account_policy_error"), new OIDCRuntimeException("policy not allow OpenIdConnectDefinition:" + oidcRuntime.getMetaData().getName()));
		}

		ah.setId(user.getAccountId());
		return ah;
	}

	@Override
	public void logout(AccountHandle user) {
	}

}
