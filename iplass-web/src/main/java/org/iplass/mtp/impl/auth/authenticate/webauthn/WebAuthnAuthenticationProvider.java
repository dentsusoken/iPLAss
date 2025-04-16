/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.webauthn;

import static org.iplass.mtp.impl.web.WebResourceBundleUtil.resourceString;

import java.sql.Timestamp;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.builtin.BuiltinAccount;
import org.iplass.mtp.impl.auth.authenticate.builtin.RdbAccountStore;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.webauthn.MetaWebAuthn.WebAuthnRuntime;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebAuthnのDiscoverable Credentialを用いたパスワードレス、ネームレス認証を提供する認証プロバイダです。
 * 
 */
public class WebAuthnAuthenticationProvider extends AuthenticationProviderBase {
	private static final Logger logger = LoggerFactory.getLogger(WebAuthnAuthenticationProvider.class);
	
	private WebAuthnService webAuthnService;
	private AuthenticationPolicyService authPolicyService;
	private TenantContextService tenantContextService;
	private RdbAccountStore accountDao;

	@Override
	public void inited(AuthService service, Config config) {
		super.inited(service, config);
		webAuthnService = ServiceRegistry.getRegistry().getService(WebAuthnService.class);
		authPolicyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
		tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
		accountDao = new RdbAccountStore();
		accountDao.inited(this, config);

	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return NO_UPDATABLE_AMM;
	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return WebAuthnCredential.class;
	}

	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return WebAuthnAccountHandle.class;
	}

	@Override
	public AccountHandle login(Credential credential) {
		if (!(credential instanceof WebAuthnCredential)) {
			return null;
		}

		WebAuthnCredential cre = (WebAuthnCredential) credential;
		WebAuthnRuntime webauthn = webAuthnService.getOrDefault(cre.getWebAuthnDefinitionName());
		WebAuthnVerifyResult result = webauthn.verify(cre.getPublicKeyCredential(), cre.getServer(), true);
		
		if (!result.isVerified()) {
			if (logger.isDebugEnabled()) {
				if (result.getRootCause() == null) {
					logger.debug("WebAuthn failed:error=" + result.getError() + ", errorDescription=" + result.getErrorDescription());
				} else {
					logger.debug("WebAuthn failed:error=" + result.getError() + ", errorDescription=" + result.getErrorDescription() + "exception="
							+ result.getRootCause(), result.getRootCause());
				}
			}
			throw new LoginFailedException(resourceString("impl.auth.authenticate.webauthn.WebAuthnAuthenticationProvider.error", result.getError()), result.getRootCause());
		}
		
		//check id(UserHandle) if specified
		if (cre.getId() != null) {
			if (!cre.getId().equals(result.getUserHandle())) {
				if (logger.isDebugEnabled()) {
					logger.debug("WebAuthn failed: UserHandle mismatch.");
				}
				throw new LoginFailedException(resourceString("impl.auth.authenticate.webauthn.WebAuthnAuthenticationProvider.error", "authentication_failed"));
			}
		}
		
		//check Policy
		AuthenticationPolicyRuntime policy = getPolicy(result.getPolicyName());
		if (!webauthn.isAllowedOnPolicy(policy)) {
			if (logger.isDebugEnabled()) {
				logger.debug("WebAuthn failed: WebAuthn is not allowed by policy.");
			}
			throw new LoginFailedException(resourceString("impl.auth.authenticate.webauthn.WebAuthnAuthenticationProvider.error", "authentication_failed"));
		}

		WebAuthnAccountHandle ah = new WebAuthnAccountHandle(result.getUserHandle(), result.getUserOid(),
				webauthn.getMetaData().getName(), result.getCredentialId(), null);

		if (policy.getMetaData().isRecordLastLoginDate()) {
			//前回ログイン日時を取得
			BuiltinAccount ba = accountDao.getAccountFromOid(getTenantIdForAccountDao(), result.getUserOid());
			if (ba != null) {
				ah.getAttributeMap().put(AccountHandle.LAST_LOGIN_ON, ba.getLastLoginOn());
				policy.updateLastLoginOn(ba);
				accountDao.updateAccountLoginStatus(ba);
			}
		}
		ah.getAttributeMap().put(User.ACCOUNT_POLICY, result.getPolicyName());

		return ah;

	}

	private boolean isSharedLoginUser() {
		TenantContext shareTenantContext = tenantContextService.getSharedTenantContext();
		MetaDataEntry mde = shareTenantContext.getMetaDataContext()
				.getMetaDataEntry(DefinitionService.getInstance().getPath(EntityDefinition.class, User.DEFINITION_NAME));
		return mde.isDataSharable();
	}

	private int getTenantIdForAccountDao() {
		int tenantId;
		if (isSharedLoginUser()) {
			tenantId = tenantContextService.getSharedTenantId();
		} else {
			tenantId = ExecuteContext.getCurrentContext().getCurrentTenant().getId();
		}
		return tenantId;
	}

	private AuthenticationPolicyRuntime getPolicy(String policyName) {
		AuthenticationPolicyRuntime pol = authPolicyService.getOrDefault(policyName);
		if (pol == null) {
			throw new SystemException("policy:" + policyName + " not found.");
		}

		return pol;
	}

	@Override
	public void afterLoginSuccess(AccountHandle account) {
		Timestamp llo = (Timestamp) account.getAttributeMap().get(AccountHandle.LAST_LOGIN_ON);
		if (llo == null) {
			String policyName = (String) account.getAttributeMap().get(User.ACCOUNT_POLICY);
			AuthenticationPolicyRuntime pol = authPolicyService.getOrDefault(policyName);
			if (pol != null && pol.getMetaData().isRecordLastLoginDate()) {
				//前回ログイン日時を取得
				BuiltinAccount ba = accountDao.getAccountFromOid(getTenantIdForAccountDao(), account.getUnmodifiableUniqueKey());
				if (ba != null) {
					account.getAttributeMap().put(AccountHandle.LAST_LOGIN_ON, ba.getLastLoginOn());
				}
			}
		}
	}

	@Override
	public void logout(AccountHandle user) {
	}

}
