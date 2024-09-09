/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

public class MultiAuthLogger extends AuthLoggerBase {
	
	private AuthLogger[] logger;

	public AuthLogger[] getLogger() {
		return logger;
	}

	public void setLogger(AuthLogger[] logger) {
		this.logger = logger;
	}

	@Override
	public void inited(AuthLoggerService service, Config config) {
	}

	@Override
	public void destroyed() {
	}

	@Override
	public void loginFail(Credential credential, Exception e) {
		if (logger != null) {
			for (AuthLogger l: logger) {
				l.loginFail(credential, e);
			}
		}
	}

	@Override
	public void loginLocked(Credential credential) {
		if (logger != null) {
			for (AuthLogger l: logger) {
				l.loginLocked(credential);
			}
		}
	}

	@Override
	public void loginSuccess(UserContext user) {
		if (logger != null) {
			for (AuthLogger l: logger) {
				l.loginSuccess(user);
			}
		}
	}

	@Override
	public void loginPasswordExpired(Credential credential) {
		if (logger != null) {
			for (AuthLogger l: logger) {
				l.loginPasswordExpired(credential);
			}
		}
	}

	@Override
	public void updatePasswordSuccess(Credential credential) {
		if (logger != null) {
			for (AuthLogger l: logger) {
				l.updatePasswordSuccess(credential);
			}
		}
	}

	@Override
	public void updatePasswordFail(Credential credential,
			CredentialUpdateException e) {
		if (logger != null) {
			for (AuthLogger l: logger) {
				l.updatePasswordFail(credential, e);
			}
		}
	}

	@Override
	public void resetPasswordSuccess(Credential credential) {
		if (logger != null) {
			for (AuthLogger l: logger) {
				l.resetPasswordSuccess(credential);
			}
		}
	}

}
