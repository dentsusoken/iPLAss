/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.ldap;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginContext;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;

/**
 * LDAP使用時のアカウントハンドラ<br>
 * LDAPで保持しているユーザー情報を保持する。
 *
 * @author 藤田　義弘
 *
 */
public class LdapAccountHandle implements AccountHandle {

	private static final long serialVersionUID = 7320347984611809914L;

	private String id;
	private String uniqueKey;
	private boolean isAccountLocked;
	private boolean isPasswordExpired;
	private boolean isInitialLogin;
	private Map<String, Object> attributeMap;
	private int authenticationProviderIndex;
	
	//LoginContextはSerializeできない。。。
	@Deprecated
	private transient LoginContext loginContext;

	public LdapAccountHandle() {
	}

	@Deprecated
	public LdapAccountHandle(String id, LoginContext loginContext) {
		this.id = id;
		this.uniqueKey = id;
		this.loginContext = loginContext;
	}
	
	public LdapAccountHandle(String id) {
		this.id = id;
		this.uniqueKey = id;
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
		if(attributeMap == null){
			attributeMap = new HashMap<String, Object>();
		}
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
	
	public void setUnmodifiableUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

}
