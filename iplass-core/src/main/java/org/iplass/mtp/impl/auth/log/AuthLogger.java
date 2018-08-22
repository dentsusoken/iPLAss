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
import org.iplass.mtp.spi.ServiceInitListener;

public interface AuthLogger extends ServiceInitListener<AuthLoggerService> {
	//TODO AccountNotificationListenerとの統合

	public String getLoggerName();

	public void loginFail(Credential credential, Exception e);

	public void loginLocked(Credential credential);

	public void loginSuccess(UserContext user);

	public void loginPasswordExpired(Credential credential);

	public void updatePasswordSuccess(Credential credential);

	public void updatePasswordFail(Credential credential, CredentialUpdateException e);

	public void resetPasswordSuccess(Credential credential);

}
