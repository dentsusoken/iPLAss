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

import org.apache.commons.codec.binary.Hex;

public class MessageDigestChecksum implements Checksum {
	private MessageDigest md;
	
	public MessageDigestChecksum(MessageDigest md) {
		this.md = md;
	}

	@Override
	public void update(int b) {
		md.update((byte) b);
	}

	@Override
	public void update(byte[] b, int off, int len) {
		md.update(b, off, len);
	}

	@Override
	public String getValue() {
		return Hex.encodeHexString(md.digest());
	}
	

	@Override
	public void reset() {
		md.reset();
	}

}
