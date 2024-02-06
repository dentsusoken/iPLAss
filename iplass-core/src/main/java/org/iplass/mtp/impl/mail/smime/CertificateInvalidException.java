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
package org.iplass.mtp.impl.mail.smime;

import org.iplass.mtp.SystemException;

public class CertificateInvalidException extends SystemException {
	private static final long serialVersionUID = 6778862881001125031L;

	public CertificateInvalidException() {
		super();
	}

	public CertificateInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	public CertificateInvalidException(String message) {
		super(message);
	}

	public CertificateInvalidException(Throwable cause) {
		super(cause);
	}

}
