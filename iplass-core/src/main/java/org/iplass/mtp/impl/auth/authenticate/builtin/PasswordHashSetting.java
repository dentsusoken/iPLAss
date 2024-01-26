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

package org.iplass.mtp.impl.auth.authenticate.builtin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.iplass.mtp.impl.util.HashUtil;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class PasswordHashSetting {

	private String version;
	private String passwordHashAlgorithm;
	private String systemSalt;
	private int stretchCount = 1000;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPasswordHashAlgorithm() {
		return passwordHashAlgorithm;
	}
	public void setPasswordHashAlgorithm(String passwordHashAlgorithm) {
		this.passwordHashAlgorithm = passwordHashAlgorithm;
	}
	public String getSystemSalt() {
		return systemSalt;
	}
	public void setSystemSalt(String systemSalt) {
		this.systemSalt = systemSalt;
	}
	public int getStretchCount() {
		return stretchCount;
	}
	public void setStretchCount(int stretchCount) {
		this.stretchCount = stretchCount;
	}
	
	public void checkValidConfiguration() {
		try {
			MessageDigest.getInstance(getPasswordHashAlgorithm());
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceConfigrationException("invalid PasswordHashAlgorithm", e);
		}
	}
	
	protected String hash(String password, String salt) {
		try {
			String hashTarget = password;
			for (int i = 0; i < stretchCount; i++) {
				hashTarget = HashUtil.digest(hashTarget + systemSalt + salt, passwordHashAlgorithm);
			}
			return hashTarget;
		} catch (NoSuchAlgorithmException e) {
			// NOP 初期化時にチェックしているので、ここでは例外にならない。
			throw new RuntimeException(e);
		}
	}
}
