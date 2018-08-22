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

package org.iplass.mtp.impl.auth.authenticate.trust;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;

/**
 * AccountHandleのインスタンスが指定のものと一致することで信頼するTrustedAuthValidator。
 * 
 * @author K.Higuchi
 *
 */
public class DefaultTrustedAuthValidator implements TrustedAuthValidator {
	
	private String accountHandleClass;
	private String requiredCredentialClass;
	

	public DefaultTrustedAuthValidator(String accountHandleClass,
			String requiredCredentialClass) {
		this.accountHandleClass = accountHandleClass;
		this.requiredCredentialClass = requiredCredentialClass;
	}

	public String getRequiredCredentialClass() {
		return requiredCredentialClass;
	}

	public void setRequiredCredentialClass(String requiredCredentialClass) {
		this.requiredCredentialClass = requiredCredentialClass;
	}

	public String getAccountHandleClass() {
		return accountHandleClass;
	}

	public void setAccountHandleClass(String accountHandleClass) {
		this.accountHandleClass = accountHandleClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TrustedAuthValidateResult checkTrusted(UserContext userContext) {
		AccountHandle account = userContext.getAccount();
		if (account.getClass().getName().equals(accountHandleClass)) {
			return new TrustedAuthValidateResult(true, null);
		} else {
			try {
				return new TrustedAuthValidateResult(false, (Class<? extends Credential>) Class.forName(requiredCredentialClass));
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(requiredCredentialClass + " cant resolve:" + e, e);
			}
		}
	}

	@Override
	public void inited(AuthService service, AuthenticationProvider provider) {
	}

}
