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

package org.iplass.mtp.impl.lob.checksum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class ChecksumFactory {
	
	public static final String DEFAULT_ALGORITHM = "Adler-32";
	
	private static ChecksumFactory factory = new ChecksumFactory();
	
	public static ChecksumFactory getFactory() {
		return factory;
	}
	
	/**
	 * Adler-32/CRC-32/MD5/SHA-1/SHA-256
	 * 
	 * @param checksumAlgorithm
	 * @return
	 */
	public Checksum newChecksum(String checksumAlgorithm) {
		if (checksumAlgorithm == null) {
			checksumAlgorithm = DEFAULT_ALGORITHM;
		}
		switch (checksumAlgorithm) {
		case "Adler-32":
			return new ZipChecksum(new Adler32());
		case "CRC-32":
			return new ZipChecksum(new CRC32());
		default:
			try {
				return new MessageDigestChecksum(MessageDigest.getInstance(checksumAlgorithm));
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

}
