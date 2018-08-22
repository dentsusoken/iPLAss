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

package org.iplass.mtp.impl.auth.log;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jAuthLogger extends AuthLoggerBase {
	
	public static final String DEFAULT_LOGGER_NAME ="mtp.auth";
	
	protected Logger auditLog;
	
	private String slf4LoggerName;

	public String getSlf4LoggerName() {
		return slf4LoggerName;
	}

	public void setSlf4LoggerName(String slf4LoggerName) {
		this.slf4LoggerName = slf4LoggerName;
	}

	@Override
	public void inited(AuthLoggerService service, Config config) {
		if (slf4LoggerName == null) {
			slf4LoggerName = DEFAULT_LOGGER_NAME;
		}
		
		auditLog = LoggerFactory.getLogger(slf4LoggerName);
	}

	@Override
	public void destroyed() {
	}

	@Override
	public void loginFail(Credential credential, Exception e) {
		if (auditLog.isWarnEnabled()) {
			if (e == null) {
				auditLog.warn(credential.getId() + ",login,fail");
			} else {
				auditLog.warn(credential.getId() + ",login,fail," + e);
			}
		}
	}

	@Override
	public void loginLocked(Credential credential) {
		if (auditLog.isWarnEnabled()) {
			auditLog.warn(credential.getId() + ",login,locked");
		}
	}

	@Override
	public void loginSuccess(UserContext user) {
		if (auditLog.isInfoEnabled()) {
			auditLog.info(user.getUser().getAccountId() + ",login,success");
		}
	}

	@Override
	public void loginPasswordExpired(Credential credential) {
		if (auditLog.isInfoEnabled()) {
			auditLog.info(credential.getId() + ",login,passExpired");
		}
	}

	@Override
	public void updatePasswordSuccess(Credential oldCredential) {
		if (auditLog.isInfoEnabled()) {
			auditLog.info(oldCredential.getId() + ",updatePass,success");
		}
	}

	@Override
	public void updatePasswordFail(Credential oldCredential, CredentialUpdateException e) {
		if (auditLog.isWarnEnabled()) {
			auditLog.warn(oldCredential.getId() + ",updatePass,fail," + e.toString());
		}
	}

	@Override
	public void resetPasswordSuccess(Credential credential) {
		if (auditLog.isInfoEnabled()) {
			auditLog.info(credential.getId() + ",resetPass,success");
		}
	}

}
