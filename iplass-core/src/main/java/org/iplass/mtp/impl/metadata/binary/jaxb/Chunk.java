/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.metadata.binary.jaxb;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.iplass.mtp.impl.metadata.binary.BinaryMetaData;
import org.iplass.mtp.impl.metadata.binary.BinaryMetaDataService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Chunk {
	private static Logger logger = LoggerFactory.getLogger(Chunk.class);
	
	private final boolean marshalMode;
	private int length;
	private long offset;
	private BinaryMetaData binMeta;
	
	private Path tempFile;
	private byte[] bin;
	
	Chunk(long offset, int length, BinaryMetaData binMeta) {
		marshalMode = true;
		this.offset = offset;
		this.length = length;
		this.binMeta = binMeta;
	}
	
	public Chunk() {
		marshalMode = false;
	}
	
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) throws IOException {
		this.offset = offset;
		if (!marshalMode) {
			if (offset > 0 && bin != null) {
				toFile();
			}
		}
	}
	public byte[] getBin() throws IOException {
		if (marshalMode) {
			try (InputStream is = binMeta.getInputStream()) {
				is.skip(offset);
				byte[] retBin = new byte[length];
				IOUtils.readFully(is, retBin, 0, length);
				return retBin;
			}
		} else {
			if (tempFile != null) {
				return Files.readAllBytes(tempFile);
			}
			return bin;
		}
	}
	
	public void setBin(byte[] bin) throws IOException {
		this.bin = bin;
		if (!marshalMode) {
			if (offset > 0 && bin != null) {
				toFile();
			}
		}
	}
	
	private void toFile() throws IOException {
		BinaryMetaDataService service = ServiceRegistry.getRegistry().getService(BinaryMetaDataService.class);
		Path dir = service.getTempFileDir();
		tempFile = Files.createTempFile(dir, "cr_", ".tmp");
		Files.write(tempFile, bin);
		bin = null;
	}
	
	void dispose() {
		if (tempFile != null) {
			try {
				Files.deleteIfExists(tempFile);
			} catch (IOException e) {
				logger.warn("can't delete temp file:" + tempFile);
			}
			tempFile = null;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		dispose();
	}
}
