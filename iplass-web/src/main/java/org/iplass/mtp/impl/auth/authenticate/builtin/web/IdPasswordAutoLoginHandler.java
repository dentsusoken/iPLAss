/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.builtin.web;

import jakarta.servlet.http.HttpServletRequest;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginHandler;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginInstruction;
import org.iplass.mtp.impl.webapi.rest.RestRequestContext;
import org.iplass.mtp.web.WebRequestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ID/PasswordベースのAutoLoginHandler。
 * 
 * @author K.Higuchi
 *
 */
public class IdPasswordAutoLoginHandler implements AutoLoginHandler {
	
	private static Logger logger = LoggerFactory.getLogger(IdPasswordAutoLoginHandler.class);
	
	public static final String AUTH_ID_HEADER = "X-Auth-Id";
	public static final String AUTH_PASSWORD_HEADER = "X-Auth-Password";
	
	private boolean enableBasicAuthentication;
	private boolean rejectAmbiguousRequest;
	
	public boolean isRejectAmbiguousRequest() {
		return rejectAmbiguousRequest;
	}

	public void setRejectAmbiguousRequest(boolean rejectAmbiguousRequest) {
		this.rejectAmbiguousRequest = rejectAmbiguousRequest;
	}

	public boolean isEnableBasicAuthentication() {
		return enableBasicAuthentication;
	}

	public void setEnableBasicAuthentication(boolean enableBasicAuthentication) {
		this.enableBasicAuthentication = enableBasicAuthentication;
	}

	private IdPasswordCredential idPassFromHeader(RequestContext req) {
		HttpServletRequest hr = (HttpServletRequest) req.getAttribute(WebRequestConstants.SERVLET_REQUEST);
		String id = null;
		String pass = null;
		
		//カスタムヘッダーによる認証処理
		id = hr.getHeader(AUTH_ID_HEADER);
		if (id != null && id.length() > 0) {
			logger.debug("handle custom header authentication");
			pass = hr.getHeader(AUTH_PASSWORD_HEADER);
			return new IdPasswordCredential(id, pass);
		}
		
		//Basic認証
		if (enableBasicAuthentication) {
			IdPasswordCredential cre = BasicAuthUtil.decodeFromHeader(req);
			if (cre != null) {
				logger.debug("handle basic authentication");
				return cre;
			}
		}
		
		return null;
	}
	
	@Override
	public AutoLoginInstruction handle(RequestContext req, boolean isLogined, UserContext user) {
		if (!(req instanceof RestRequestContext)) {
			return AutoLoginInstruction.THROUGH;
		}
		
		IdPasswordCredential cre = idPassFromHeader(req);
		if (cre == null) {
			return AutoLoginInstruction.THROUGH;
		}
		
		if (isLogined) {
			if (!cre.getId().equals(user.getAccount().getCredential().getId())) {
				if (rejectAmbiguousRequest) {
					//セッション上のUserと、HeaderのIDが等しくないならエラー
					throw new LoginFailedException("another login session is avaliable");
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("login session is avaliable, but another id/pass is specified. current id:" + user.getAccount().getCredential().getId() + ", request id:" + cre.getId());
					} else {
						logger.warn("login session is avaliable, but another id/pass is specified.");
					}
				}
			}
			
			return AutoLoginInstruction.THROUGH;
		} else {
			return new AutoLoginInstruction(cre);
		}
	}

	@Override
	public void handleSuccess(AutoLoginInstruction ali, RequestContext req, UserContext user) {
	}

	@Override
	public Exception handleException(AutoLoginInstruction ali, ApplicationException e, RequestContext req, boolean isLogined,
			UserContext user) {
		if (isBasicAuth(req)) {
			throw new WWWAuthenticateException(BasicAuthUtil.AUTH_SCHEME_BASIC, null, "Login with BASIC Authentication failed.");
		} else {
			throw e;
		}
	}

	private boolean isBasicAuth(RequestContext req) {
		if (!enableBasicAuthentication) {
			return false;
		}
		
		HttpServletRequest hr = (HttpServletRequest) req.getAttribute(WebRequestConstants.SERVLET_REQUEST);
		if (hr.getHeader(AUTH_ID_HEADER) != null) {
			return false;
		}
		
		return true;
	}

}
