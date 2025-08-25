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
package org.iplass.mtp.impl.auth.authenticate.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapName;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NeoLdapAuthStrategy implements LdapAuthStrategy {
	private static final String DN_ATTRIBUTE_NAME = "dn";
	private static final String DEFAULT_INIT_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	private static final String LDAP_POOLING_FLAG = "com.sun.jndi.ldap.connect.pool";

	private static Logger logger = LoggerFactory.getLogger(NeoLdapAuthStrategy.class);
	
	private LdapAuthenticationProvider p;
	
	private Pattern userNamePattern = Pattern.compile("\\$\\{userName\\}");
	private Pattern tenantNamePattern = Pattern.compile("\\$\\{tenantName\\}");
	private Pattern userDnPattern = Pattern.compile("\\$\\{userDn\\}");
	
	
	private boolean providerUrlHasDn;
	private String baseDn;

	private boolean userDnPatternHasUserName;
	private boolean userDnPatternHasTenantName;
	private boolean userBaseDnHasTenantName;
	private boolean userFilterPatternHasUserName;
	private boolean userFilterPatternHasTenantName;
	
	private boolean groupBaseDnHasTenantName;
	private boolean groupFilterPatternHasTenantName;
	private boolean tenantGroupCnPatternHasTenantName;

	
	private String[] attrIds;
	private String[] groupAttrIds;
	
	private boolean hasAdminIdPass;

	NeoLdapAuthStrategy(LdapAuthenticationProvider provider) {
		this.p = provider;
		
		String purl = (String) p.getJndiEnv().get(Context.PROVIDER_URL);
		if (purl == null) {
			throw new ServiceConfigrationException(Context.PROVIDER_URL + " not specified");
		}
		purl = purl.substring(purl.indexOf("://") + 3);
		int index = purl.lastIndexOf('/');
		if (index >= 0 && index < purl.length() - 1) {
			providerUrlHasDn = true;
			try {
				//normalize
				baseDn = new LdapName(purl.substring(index + 1)).toString();
			} catch (InvalidNameException e) {
				throw new ServiceConfigrationException(e);
			}
		}
		
		userDnPatternHasUserName = (p.getUserDn() != null && p.getUserDn().contains(LdapAuthenticationProvider.USERNAME_TOKEN));
		userDnPatternHasTenantName = (p.getUserDn() != null && p.getUserDn().contains(LdapAuthenticationProvider.TENANTNAME_TOKEN));
		userBaseDnHasTenantName = (p.getUserBaseDn() != null && p.getUserBaseDn().contains(LdapAuthenticationProvider.TENANTNAME_TOKEN));
		userFilterPatternHasUserName = (p.getUserFilter() != null && p.getUserFilter().contains(LdapAuthenticationProvider.USERNAME_TOKEN));
		userFilterPatternHasTenantName = (p.getUserFilter() != null && p.getUserFilter().contains(LdapAuthenticationProvider.TENANTNAME_TOKEN));
		groupBaseDnHasTenantName = (p.getGroupBaseDn() != null && p.getGroupBaseDn().contains(LdapAuthenticationProvider.TENANTNAME_TOKEN));
		groupFilterPatternHasTenantName = (p.getGroupFilter() != null && p.getGroupFilter().contains(LdapAuthenticationProvider.TENANTNAME_TOKEN));
		tenantGroupCnPatternHasTenantName = (p.getTenantGroupCode() != null && p.getTenantGroupCode().contains(LdapAuthenticationProvider.TENANTNAME_TOKEN));
		
		if (p.getUserAttribute() != null) {
			ArrayList<String> tmp = new ArrayList<>();
			for (String s: p.getUserAttribute()) {
				tmp.add(s);
			}
			if (p.getUniqueKeyAttribute() != null) {
				if (!tmp.contains(p.getUniqueKeyAttribute())) {
					tmp.add(p.getUniqueKeyAttribute());
				}
			}
			attrIds = tmp.toArray(new String[tmp.size()]);
		}
		
		groupAttrIds = new String[]{p.getGroupCodeAttribute()};
		
		hasAdminIdPass = (p.getJndiEnv().get(Context.SECURITY_PRINCIPAL) != null);
	}
	
	private Hashtable<Object, Object> createBaseEnv() {
		Hashtable<Object, Object> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, DEFAULT_INIT_CONTEXT_FACTORY);
		
		if (p.getJndiEnv() != null) {
			env.putAll(p.getJndiEnv());
		}
		return env;
		
	}
	
	private InitialLdapContext initContext() throws NamingException {
		Hashtable<Object, Object> env = createBaseEnv();
		return new InitialLdapContext(env, null);
	}
	
	private String userDnStr(IdPasswordCredential idPass) {
		String userDn = p.getUserDn();
		if (userDnPatternHasTenantName) {
			Tenant t = ExecuteContext.getCurrentContext().getCurrentTenant();
			userDn = tenantNamePattern.matcher(userDn).replaceAll(LdapUtil.escapeForDN(t.getName()));
		}
		if (userDnPatternHasUserName) {
			userDn = userNamePattern.matcher(userDn).replaceAll(LdapUtil.escapeForDN(idPass.getId()));
		}
		return userDn;
	}
	
	private String searchFilterStr(IdPasswordCredential idPass) {
		String filter = p.getUserFilter();
		if (userFilterPatternHasTenantName) {
			Tenant t = ExecuteContext.getCurrentContext().getCurrentTenant();
			filter = tenantNamePattern.matcher(filter).replaceAll(LdapUtil.escapeForFilter(t.getName()));
		}
		if (userFilterPatternHasUserName) {
			filter = userNamePattern.matcher(filter).replaceAll(LdapUtil.escapeForFilter(idPass.getId()));
		}
		return filter;
	}
	
	private String userBaseDnStr() {
		String baseDn = p.getUserBaseDn();
		if (userBaseDnHasTenantName) {
			Tenant t = ExecuteContext.getCurrentContext().getCurrentTenant();
			baseDn = tenantNamePattern.matcher(baseDn).replaceAll(LdapUtil.escapeForDN(t.getName()));
		}
		return baseDn;
	}
	
	private String groupBaseDnStr() {
		String baseDn = p.getGroupBaseDn();
		if (groupBaseDnHasTenantName) {
			Tenant t = ExecuteContext.getCurrentContext().getCurrentTenant();
			baseDn = tenantNamePattern.matcher(baseDn).replaceAll(LdapUtil.escapeForDN(t.getName()));
		}
		return baseDn;
	}
	
	private String groupFilterStr(String userDn) {
		String filter = p.getGroupFilter();
		if (groupFilterPatternHasTenantName) {
			Tenant t = ExecuteContext.getCurrentContext().getCurrentTenant();
			filter = tenantNamePattern.matcher(filter).replaceAll(LdapUtil.escapeForFilter(t.getName()));
		}
		filter = userDnPattern.matcher(filter).replaceAll(LdapUtil.escapeForFilter(userDn));
		return filter;
	}
	
	private Map<String, Object> searchUserByFilter(String filter, InitialLdapContext ctx) throws NamingException {
		if (logger.isDebugEnabled()) {
			logger.debug("search User by filter:" + filter);
		}
		
		SearchControls cons = new SearchControls();
		cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
		if (attrIds != null) {
			cons.setReturningAttributes(attrIds);
		}
		
		String baseDn = "";
		if (p.getUserBaseDn() != null) {
			baseDn = userBaseDnStr();
		}
		
		NamingEnumeration<SearchResult> results = null;
		try {
			results = ctx.search(baseDn, filter, cons);
			if (results.hasMore()) {
				SearchResult entry = results.next();
				Map<String, Object> ret = new HashMap<>();
				ret.put(DN_ATTRIBUTE_NAME, entry.getNameInNamespace());
				
				for (NamingEnumeration<? extends Attribute> attrs = entry.getAttributes().getAll(); attrs.hasMore();) {
					Attribute attr = attrs.next();
					if (attr.size() > 1) {
						Object[] vals = new Object[attr.size()];
						for (int i = 0; i < vals.length; i++) {
							vals[i] = attr.get(i);
						}
						ret.put(attr.getID(), vals);
					} else {
						ret.put(attr.getID(), attr.get());
					}
				}
				
				if (attrIds != null) {
					//明示的にnullをセット
					for (String ai: attrIds) {
						if (!ret.containsKey(ai)) {
							ret.put(ai, null);
						}
					}
				}
				
				return ret;
			}
		} finally {
			if (results != null) {
				results.close();
			}
		}
		
		return null;
	}
	
	private Map<String, Object> searchUserByDn(String userDn, InitialLdapContext ctx) throws NamingException {
		
		String orgDn = userDn;
		
		if (providerUrlHasDn) {
			//normalize
			userDn = new LdapName(userDn).toString();
			if (userDn.endsWith(baseDn)) {
				userDn = userDn.substring(0, userDn.length() - baseDn.length() - 1);
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("search User by userDn:" + userDn);
		}
		
		Attributes attrs;
		if (attrIds != null) {
			attrs = ctx.getAttributes(userDn, attrIds);
		} else {
			attrs = ctx.getAttributes(userDn);
		}
		
		Map<String, Object> ret = new HashMap<>();
		ret.put(DN_ATTRIBUTE_NAME, orgDn);
		for (NamingEnumeration<? extends Attribute> ae = attrs.getAll(); ae.hasMore();) {
			Attribute attr = ae.next();
			if (attr.size() > 1) {
				Object[] vals = new Object[attr.size()];
				for (int i = 0; i < vals.length; i++) {
					vals[i] = attr.get(i);
				}
				ret.put(attr.getID(), vals);
			} else {
				ret.put(attr.getID(), attr.get());
			}
		}
		
		if (attrIds != null) {
			//明示的にnullをセット
			for (String ai: attrIds) {
				if (!ret.containsKey(ai)) {
					ret.put(ai, null);
				}
			}
		}
		
		
		return ret;
	}
	
	private List<String> searchGroupByFilter(String filter, InitialLdapContext ctx) throws NamingException {
		if (logger.isDebugEnabled()) {
			logger.debug("search Group by filter:" + filter);
		}
		
		SearchControls cons = new SearchControls();
		cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
		cons.setReturningAttributes(groupAttrIds);
		
		String baseDn = "";
		if (p.getGroupBaseDn() != null) {
			baseDn = groupBaseDnStr();
		}
		
		NamingEnumeration<SearchResult> results = null;
		try {
			results = ctx.search(baseDn, filter, cons);
			List<String> ret = new ArrayList<>();
			while (results.hasMore()) {
				SearchResult entry = results.next();
				Object gc = entry.getAttributes().get(p.getGroupCodeAttribute());
				if (gc != null) {
					ret.add(gc.toString());
				}
			}
			
			return ret;
		} finally {
			if (results != null) {
				results.close();
			}
		}
	}
	
	@Override
	public AccountHandle login(Credential credential) {
		if (!(credential instanceof IdPasswordCredential)) {
			return null;
		}
		final IdPasswordCredential idPass = (IdPasswordCredential) credential;
		
		InitialLdapContext forSearch = null;
		InitialLdapContext forAuth = null;
		LdapAccountHandle account = null;
		Map<String, Object> userAttributes = null;
		try {
			
			//getUserDn
			String userDn;
			if (p.getUserDn() == null) {
				forSearch = initContext();
				String filter = searchFilterStr(idPass);
				userAttributes = searchUserByFilter(filter, forSearch);
				if (userAttributes == null) {
					return null;
				}
				userDn = (String) userAttributes.get(DN_ATTRIBUTE_NAME);
			} else {
				userDn = userDnStr(idPass);
			}
			
			//connect as auth user
			Hashtable<Object, Object> env = createBaseEnv();
			env.remove(LDAP_POOLING_FLAG);
			env.put(Context.SECURITY_PRINCIPAL, userDn);
			env.put(Context.SECURITY_CREDENTIALS, idPass.getPassword());
			try {
				forAuth = new InitialLdapContext(env, null);
				//success auth
				account = new LdapAccountHandle(idPass.getId());
				
			} catch (NamingException e) {
				logger.debug("login failed.", e);
				return null;
			}
			
			//check and create InitialLdapContext for search if need
			if (p.isGetUser() && userAttributes == null || p.isGetGroup() || p.isGroupAsTenant()) {
				if (forSearch == null) {
					if (hasAdminIdPass) {
						forSearch = initContext();
						forAuth.close();
					} else {
						forSearch = forAuth;
					}
				} else {
					forAuth.close();
				}
			}
			forAuth = null;
			
			//search user attributes if need
			if (p.isGetUser() && userAttributes == null) {
				if (p.getUserFilter() != null) {
					String filter = searchFilterStr(idPass);
					userAttributes = searchUserByFilter(filter, forSearch);
					if (userAttributes == null) {
						throw new NamingException("Can't search user attributes, maybe search filter is wrong or has no permission:filter=" + filter);
					}
				} else {
					userAttributes = searchUserByDn(userDn, forSearch);
					if (userAttributes == null) {
						throw new NamingException("Can't search user attributes, maybe userDN is wrong or has no permission:userDN=" + userDn);
					}
				}
			}

			if (p.isGetUser() && userAttributes != null) {
				account.getAttributeMap().putAll(userAttributes);
				if (p.getUniqueKeyAttribute() != null) {
					account.setUnmodifiableUniqueKey(ConvertUtil.convert(String.class, userAttributes.get(p.getUniqueKeyAttribute())));
				}
			}
			
			//search group
			if (p.isGetGroup() || p.isGroupAsTenant()) {
				String userDnForGroupSearch = (String) account.getAttributeMap().get(DN_ATTRIBUTE_NAME);
				if (userDnForGroupSearch == null) {
					userDnForGroupSearch = userDn;
				}
				
				String filter = groupFilterStr(userDnForGroupSearch);
				List<String> gcodes = searchGroupByFilter(filter, forSearch);
				
				if (p.isGroupAsTenant()) {
					if (!checkTenantValid(gcodes)) {
						return null;
					}
				}
				
				if (gcodes != null && gcodes.size() > 0) {
					account.getAttributeMap().put(AccountHandle.GROUP_CODE, gcodes.toArray(new String[gcodes.size()]));
				}
			}
			
			return account;
			
		} catch (NamingException e) {
			logger.error("Can't login because unexpected error occured on LDAP Access:" + e, e);
		} finally {
			if (forSearch != null) {
				try {
					forSearch.close();
				} catch (NamingException e) {
					logger.error("Fail to close InitialLdapContext. Check whether resource is leak or not.", e);
				}
			}
			if (forAuth != null) {
				try {
					forAuth.close();
				} catch (NamingException e) {
					logger.error("Fail to close InitialLdapContext. Check whether resource is leak or not.", e);
				}
			}
		}
		
		return null;
	}

	private boolean checkTenantValid(List<String> gcodes) {
		
		String tenantGroupName = ExecuteContext.getCurrentContext().getCurrentTenant().getName();
		
		if (tenantGroupCnPatternHasTenantName) {
			tenantGroupName = tenantNamePattern.matcher(p.getTenantGroupCode()).replaceAll(tenantGroupName);
		}
		
		if (gcodes.remove(tenantGroupName)) {
			return true;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Can't resolve tenant as group:" + tenantGroupName);
			}
			return false;
		}
	}

	@Override
	public void logout(AccountHandle user) {
	}

	@Override
	public void destroyed() {
	}

}
