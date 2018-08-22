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

package org.iplass.mtp.impl.lob;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.iplass.mtp.impl.lob.checksum.Checksum;
import org.iplass.mtp.impl.lob.checksum.ChecksumFactory;
import org.iplass.mtp.impl.lob.lobstore.LobValidator;

public class LobValidatedOutputStream extends FilterOutputStream {
	
	private Checksum checksum;
	private Lob lob;
	private LobValidator lobValidator;
	
	private boolean closed;

	public LobValidatedOutputStream(OutputStream out, Lob lob, LobValidator lobValidator) {
		super(out);
		this.checksum = ChecksumFactory.getFactory().newChecksum(lobValidator.getChecksumAlgorithm());
		this.lob = lob;
		this.lobValidator = lobValidator;
	}
	
	public void write(int b) throws IOException {
		out.write(b);
		checksum.update(b);
	}
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
		checksum.update(b, off, len);
	}
	public Checksum getChecksum() {
		return checksum;
	}

	@Override
	public void close() throws IOException {
		if (!closed) {
			super.close();
			lobValidator.stored(lob, getChecksum().getValue());
			closed = true;
		}
	}
}
