/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;

public class JaasAccountHandle implements AccountHandle {
	private static final long serialVersionUID = -1085557232795815436L;

	private String id;
	private String uniqueKey;
	private boolean isAccountLocked;
	private boolean isPasswordExpired;
	private boolean isInitialLogin;
	private Map<String, Object> attributeMap;
	private int authenticationProviderIndex;
	private Subject subject;

	//LoginContextはSerializeできない。。。
	private transient LoginContext loginContext;

	public JaasAccountHandle() {
	}

	public JaasAccountHandle(String id, Class<? extends Principal> uniqueKeyPrincipalType, LoginContext loginContext) {
		this.id = id;
		this.loginContext = loginContext;
		this.subject = loginContext.getSubject();
		attributeMap = new HashMap<String, Object>();
		for (Principal p: subject.getPrincipals()) {
			if (p.getClass() == uniqueKeyPrincipalType) {
				uniqueKey = p.getName();
			}
			Object prev = attributeMap.get(p.getClass().getName());
			if (prev == null) {
				attributeMap.put(p.getClass().getName(), p.getName());
			} else if (prev instanceof String) {
				attributeMap.put(p.getClass().getName(), new String[]{(String) prev, p.getName()});
			} else {
				String[] prevArray = (String[]) prev;
				String[] newArray = Arrays.copyOf(prevArray, prevArray.length + 1);
				newArray[prevArray.length] = p.getName();
				attributeMap.put(p.getClass().getName(), newArray);
			}
		}
		
		if (uniqueKey == null) {
			uniqueKey = id;
		}
	}

	public Subject getSubject() {
		return subject;
	}
	
	public LoginContext getLoginContext() {
		return loginContext;
	}
	
	@Override
	public boolean isAccountLocked() {
		return isAccountLocked;
	}

	public void setAccountLocked(boolean isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}

	@Override
	public boolean isExpired() {
		return isPasswordExpired;
	}

	public void setPasswordExpired(boolean isPasswordExpired) {
		this.isPasswordExpired = isPasswordExpired;
	}

	@Override
	public boolean isInitialLogin() {
		return isInitialLogin;
	}

	public void setInitialLogin(boolean isInitialLogin) {
		this.isInitialLogin = isInitialLogin;
	}

	@Override
	public Map<String, Object> getAttributeMap() {
		return attributeMap;
	}

	@Override
	public Credential getCredential() {
		return new IdPasswordCredential(id, null);
	}

	public void setAuthenticationProviderIndex(int authenticationProviderIndex) {
		this.authenticationProviderIndex = authenticationProviderIndex;
	}

	@Override
	public int getAuthenticationProviderIndex() {
		return authenticationProviderIndex;
	}

	@Override
	public String getUnmodifiableUniqueKey() {
		return uniqueKey;
	}

}
