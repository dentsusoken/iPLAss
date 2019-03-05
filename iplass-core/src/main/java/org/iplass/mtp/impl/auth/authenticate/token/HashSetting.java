/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.token;

import java.security.NoSuchAlgorithmException;

import org.iplass.mtp.impl.util.HashUtil;

public class HashSetting {

	private String version;
	private String hashAlgorithm;
	private int stretchCount = 1;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getHashAlgorithm() {
		return hashAlgorithm;
	}
	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}
	public int getStretchCount() {
		return stretchCount;
	}
	public void setStretchCount(int stretchCount) {
		this.stretchCount = stretchCount;
	}
	
	protected String hash(String token) {
		try {
			
			String hashTarget = token;
			for (int i = 0; i < stretchCount; i++) {
				hashTarget = HashUtil.digest(hashTarget, hashAlgorithm);
			}
			return hashTarget;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
