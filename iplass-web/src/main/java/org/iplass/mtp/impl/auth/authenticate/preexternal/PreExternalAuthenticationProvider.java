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
package org.iplass.mtp.impl.auth.authenticate.preexternal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginInstruction;
import org.iplass.mtp.impl.auth.authenticate.DefaultUserEntityResolver;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginHandler;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.interceptors.AuthInterceptor;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.web.WebRequestConstants;

public class PreExternalAuthenticationProvider extends AuthenticationProviderBase implements AutoLoginHandler {
	
	public enum SourceType {
		HEADER,
		REQUEST,
		SESSION
	}
	
	private SourceType sourceType;
	private String accountIdAttribute;
	private String uniqueKeyAttribute;
	private String[] userAttribute;
	private boolean validateOnlyLogin;
	private String logoutUrl;
	
	public boolean isValidateOnlyLogin() {
		return validateOnlyLogin;
	}
	public void setValidateOnlyLogin(boolean validateOnlyLogin) {
		this.validateOnlyLogin = validateOnlyLogin;
	}
	public String getLogoutUrl() {
		return logoutUrl;
	}
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	public SourceType getSourceType() {
		return sourceType;
	}
	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}
	public String getAccountIdAttribute() {
		return accountIdAttribute;
	}
	public void setAccountIdAttribute(String accountIdAttribute) {
		this.accountIdAttribute = accountIdAttribute;
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
	
	@Override
	public void inited(AuthService s, Config config) {
		boolean userEntityResolverIsNull = getUserEntityResolver() == null;
		
		super.inited(s, config);
		
		if (userEntityResolverIsNull) {
			if (uniqueKeyAttribute == null) {
				//accountIdで一致させる
				((DefaultUserEntityResolver) getUserEntityResolver()).setUnmodifiableUniqueKeyProperty(User.ACCOUNT_ID);
			}
		}
		
	}
	
	private Object getAttribute(String attrName, HttpServletRequest req) {
		switch (sourceType) {
		case HEADER:
			return req.getHeader(attrName);
		case REQUEST:
			return req.getAttribute(attrName);
		case SESSION:
			HttpSession sess = req.getSession(false);
			if (sess == null) {
				return null;
			} else {
				return sess.getAttribute(attrName);
			}
		default:
			return null;
		}
	}

	@Override
	public AccountHandle login(Credential credential) {
		if (!(credential instanceof PreExternalCredential)) {
			return null;
		}
		
		WebRequestStack reqStack = WebRequestStack.getCurrent();
		HttpServletRequest req = reqStack.getRequest();
		
		Object accountIdObj = getAttribute(accountIdAttribute, req);
		if (accountIdObj == null) {
			return null;
		}
		if (!accountIdObj.toString().equals(credential.getId())) {
			return null;
		}
		
		String uniqueKey;
		if (uniqueKeyAttribute != null) {
			uniqueKey = getAttribute(uniqueKeyAttribute, req).toString();
		} else {
			uniqueKey = credential.getId();
		}
		
		Map<String, Object> attributes = null;
		if (userAttribute != null && userAttribute.length > 0) {
			attributes = new HashMap<>();
			for (String ua: userAttribute) {
				attributes.put(ua, getAttribute(ua, req));
			}
		}
		
		return new PreExternalAccountHandle(credential.getId(), uniqueKey, attributes);
	}

	@Override
	public void logout(AccountHandle user) {
		if (user instanceof PreExternalAccountHandle) {
			WebRequestStack reqStack = WebRequestStack.getCurrent();
			if (reqStack != null && logoutUrl != null) {
				reqStack.getRequestContext().setAttribute(AuthInterceptor.LOGOUT_FLAG, Boolean.TRUE);
				reqStack.getRequestContext().setAttribute(AuthInterceptor.REDIRECT_PATH_AFTER_LOGOUT, logoutUrl);
			}
		}
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return NO_UPDATABLE_AMM;
	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return PreExternalCredential.class;
	}

	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return PreExternalAccountHandle.class;
	}

	@Override
	public AutoLoginHandler getAutoLoginHandler() {
		return this;
	}
	
	@Override
	public AutoLoginInstruction handle(RequestContext req, boolean isLogined, UserContext user) {
		
		if (isLogined) {
			if (!validateOnlyLogin) {
				HttpServletRequest httpReq = (HttpServletRequest) req.getAttribute(WebRequestConstants.SERVLET_REQUEST);
				Object accountIdObj = getAttribute(accountIdAttribute, httpReq);
				if (accountIdObj == null) {
					return AutoLoginInstruction.LOGOUT;
				}
				if (!accountIdObj.toString().equals(user.getAccount().getCredential().getId())) {
					return new AutoLoginInstruction(new PreExternalCredential(accountIdObj.toString()));
				}
			}
		} else {
			HttpServletRequest httpReq = (HttpServletRequest) req.getAttribute(WebRequestConstants.SERVLET_REQUEST);
			Object accountIdObj = getAttribute(accountIdAttribute, httpReq);
			if (accountIdObj != null) {
				return new AutoLoginInstruction(new PreExternalCredential(accountIdObj.toString()));
			}
		}
		
		return AutoLoginInstruction.THROUGH;
	}
	
}
