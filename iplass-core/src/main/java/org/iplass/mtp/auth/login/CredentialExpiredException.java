/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

/**
 *
 */
package org.iplass.mtp.auth.login;

/**
 * 認証情報有効期限切れ例外
 * @author 片野　博之
 *
 */
public class CredentialExpiredException extends LoginException {

	private static final long serialVersionUID = 5891584135962867247L;
	private boolean isInitialLogin;
	private String policyName;
	
	/**
	 *
	 */
	public CredentialExpiredException() {
	}

	/**
	 * @param message
	 */
	public CredentialExpiredException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CredentialExpiredException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CredentialExpiredException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean isInitialLogin() {
		return isInitialLogin;
	}
	
	public void setInitialLogin(boolean isInitialLogin) {
		this.isInitialLogin = isInitialLogin;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

}
