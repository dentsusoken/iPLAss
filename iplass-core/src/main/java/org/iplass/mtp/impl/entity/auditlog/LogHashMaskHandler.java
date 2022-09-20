/* 
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.auditlog;

import java.security.NoSuchAlgorithmException;

import org.iplass.mtp.impl.util.HashUtil;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class LogHashMaskHandler implements LogMaskHandler {

	private String hashAlgorithm;

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	@Override
	public String mask(String value) {
		try {
			// ハッシュ化する
			return HashUtil.digest(value, hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceConfigrationException("invalid PasswordHashAlgorithm", e);
		}
	}
}
