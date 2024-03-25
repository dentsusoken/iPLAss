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
package org.iplass.mtp.impl.auth.authenticate.jee;

import java.security.Principal;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginHandler;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginInstruction;
import org.iplass.mtp.impl.auth.authenticate.DefaultUserEntityResolver;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.web.WebRequestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeContainerManagedAuthenticationProvider extends AuthenticationProviderBase implements AutoLoginHandler {
	private static Logger logger = LoggerFactory.getLogger(JeeContainerManagedAuthenticationProvider.class);

	private String[] roleAsGroup;
	private boolean validateOnlyLogin;
	
	public boolean isValidateOnlyLogin() {
		return validateOnlyLogin;
	}

	public void setValidateOnlyLogin(boolean validateOnlyLogin) {
		this.validateOnlyLogin = validateOnlyLogin;
	}

	public String[] getRoleAsGroup() {
		return roleAsGroup;
	}

	public void setRoleAsGroup(String[] roleAsGroup) {
		this.roleAsGroup = roleAsGroup;
	}

	@Override
	public void inited(AuthService service, Config config) {
		boolean userEntityResolverIsNull = getUserEntityResolver() == null;
		
		super.inited(service, config);
		
		if (userEntityResolverIsNull) {
			//accountIdで一致させる
			((DefaultUserEntityResolver) getUserEntityResolver()).setUnmodifiableUniqueKeyProperty(User.ACCOUNT_ID);
		}
	}
	
	@Override
	public AccountHandle login(Credential credential) {
		if (!(credential instanceof JeeContainerManagedCredential)) {
			return null;
		}
		
		WebRequestStack reqStack = WebRequestStack.getCurrent();
		HttpServletRequest req = reqStack.getRequest();
		Principal up = req.getUserPrincipal();
		if (up == null) {
			return null;
		}
		
		if (!up.getName().equals(credential.getId())) {
			return null;
		}

		JeeContainerManagedAccountHandle ret = new JeeContainerManagedAccountHandle(credential.getId());
		if (roleAsGroup != null) {
			ArrayList<String> groups = new ArrayList<>();
			for (String r: roleAsGroup) {
				if (req.isUserInRole(r)) {
					groups.add(r);
				}
			}
			if (groups.size() > 0) {
				ret.getAttributeMap().put(AccountHandle.GROUP_CODE, groups.toArray(new String[groups.size()]));
			}
		}
		
		return ret;
	}

	@Override
	public void logout(AccountHandle user) {
		if (user instanceof JeeContainerManagedAccountHandle) {
			WebRequestStack reqStack = WebRequestStack.getCurrent();
			HttpServletRequest req = reqStack.getRequest();
			try {
				req.logout();
			} catch (ServletException e) {
				//silent log
				logger.debug("logout fail.", e);
			}
		}
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return NO_UPDATABLE_AMM;
	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return JeeContainerManagedCredential.class;
	}

	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return JeeContainerManagedAccountHandle.class;
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
				Principal up = httpReq.getUserPrincipal();
				if (up == null) {
					return AutoLoginInstruction.LOGOUT;
				}
				if (!up.getName().equals(user.getAccount().getCredential().getId())) {
					return new AutoLoginInstruction(new JeeContainerManagedCredential(up.getName()));
				}
			}
		} else {
			HttpServletRequest httpReq = (HttpServletRequest) req.getAttribute(WebRequestConstants.SERVLET_REQUEST);
			Principal up = httpReq.getUserPrincipal();
			if (up != null) {
				return new AutoLoginInstruction(new JeeContainerManagedCredential(up.getName()));
			}
		}
		
		return AutoLoginInstruction.THROUGH;
	}
	
}
