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
package org.iplass.mtp.impl.mail.smime;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class CertificateKeyPair {
	private final PrivateKey key;
	private final X509Certificate certificate;
	
	CertificateKeyPair(X509Certificate certificate, PrivateKey key) {
		this.certificate = certificate;
		this.key = key;
	}

	public PrivateKey getKey() {
		return key;
	}

	public X509Certificate getCertificate() {
		return certificate;
	}

}
