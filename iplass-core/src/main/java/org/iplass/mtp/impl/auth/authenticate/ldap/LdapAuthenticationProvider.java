/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.Map;

import javax.naming.Context;

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

/**
 * LDAP使用時の認証プロバイダー<br>
 * LDAPで保持しているユーザ、パスワード情報を利用して認証する
 *
 * @author 藤田　義弘
 *
 */
public class LdapAuthenticationProvider extends AuthenticationProviderBase {
	
	public static final String USERNAME_TOKEN = "${userName}";
	public static final String TENANTNAME_TOKEN = "${tenantName}";
	public static final String USERDN_TOKEN = "${userDn}";
    
	private static Logger logger = LoggerFactory.getLogger(LdapAuthenticationProvider.class);
	
	private Map<String, Object> jndiEnv;
	
	private boolean getUser;
	private boolean getGroup;
	private boolean groupAsTenant;
	
	private String userBaseDn;
	private String groupBaseDn;
	
	private String userDn;
	private String userFilter;
	private String uniqueKeyAttribute;
	private String[] userAttribute;
	
	private String groupFilter;
	private String groupCodeAttribute;
	private String tenantGroupCode;

	private LdapAuthStrategy actual;

	@Override
	public AccountHandle login(Credential credential) {
		return actual.login(credential);
	}

	@Override
	public void logout(AccountHandle user) {
		actual.logout(user);
	}
	
	@Override
	public Class<? extends Credential> getCredentialType() {
		return IdPasswordCredential.class;
	}

	@Override
	public void destroyed() {
		actual.destroyed();
	}
	
	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return LdapAccountHandle.class;
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
		
		if (jndiEnv != null) {
			if (jndiEnv.get(Context.INITIAL_CONTEXT_FACTORY) == null) {
				jndiEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			}
		}
		
		actual = new NeoLdapAuthStrategy(this);
		
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return NO_UPDATABLE_AMM;
	}

    public Map<String, Object> getJndiEnv() {
		return jndiEnv;
	}

	public void setJndiEnv(Map<String, Object> jndiEnv) {
		this.jndiEnv = jndiEnv;
	}

	public String getUserBaseDn() {
		return userBaseDn;
	}

	public void setUserBaseDn(String userBaseDn) {
		this.userBaseDn = userBaseDn;
	}

	public String getGroupBaseDn() {
		return groupBaseDn;
	}

	public void setGroupBaseDn(String groupBaseDn) {
		this.groupBaseDn = groupBaseDn;
	}

	public boolean isGetUser() {
		return getUser;
	}

	public void setGetUser(boolean getUser) {
		this.getUser = getUser;
	}

	public boolean isGetGroup() {
		return getGroup;
	}

	public void setGetGroup(boolean getGroup) {
		this.getGroup = getGroup;
	}

	public String getUserDn() {
		return userDn;
	}

	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}

	public String getUserFilter() {
		return userFilter;
	}

	public void setUserFilter(String userFilter) {
		this.userFilter = userFilter;
	}

	public String getUniqueKeyAttribute() {
		return uniqueKeyAttribute;
	}

	public void setUniqueKeyAttribute(String uniqueKeyAttribute) {
		this.uniqueKeyAttribute = uniqueKeyAttribute;
	}

	public String[] getUserAttribute() {
		return userAttribute;
	}

	public void setUserAttribute(String[] userAttribute) {
		this.userAttribute = userAttribute;
	}

	public String getGroupFilter() {
		return groupFilter;
	}

	public void setGroupFilter(String groupFilter) {
		this.groupFilter = groupFilter;
	}

	public String getGroupCodeAttribute() {
		return groupCodeAttribute;
	}

	public void setGroupCodeAttribute(String groupCodeAttribute) {
		this.groupCodeAttribute = groupCodeAttribute;
	}

	public boolean isGroupAsTenant() {
		return groupAsTenant;
	}

	public void setGroupAsTenant(boolean groupAsTenant) {
		this.groupAsTenant = groupAsTenant;
	}

	public String getTenantGroupCode() {
		return tenantGroupCode;
	}

	public void setTenantGroupCode(String tenantGroupCode) {
		this.tenantGroupCode = tenantGroupCode;
	}

}
