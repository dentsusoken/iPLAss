/* 
 * Copyright 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.impl.entity.auditlog;

import java.security.NoSuchAlgorithmException;

import org.iplass.mtp.impl.util.HashUtil;
import org.iplass.mtp.spi.ServiceConfigrationException;

public class LogHashMaskHandler extends LogMaskHandler {

	private String hashAlgorithm;

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	@Override
	public String maskingProperty(String definitionName, String keyName, String value) {
		if (isTargetProperty(definitionName, keyName)) {
			try {
				// ハッシュ化する
				return HashUtil.digest(value, hashAlgorithm);
			} catch (NoSuchAlgorithmException e) {
				throw new ServiceConfigrationException("invalid PasswordHashAlgorithm", e);
			}
		}

		return value;
	}

}
