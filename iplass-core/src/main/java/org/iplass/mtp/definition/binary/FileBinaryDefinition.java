/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.definition.binary;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.iplass.mtp.impl.metadata.binary.BinaryMetaDataService;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * FileのPathを指定してBinaryDefinitionを指し示す為のクラス。
 * 
 * @author K.Higuchi
 *
 */
public class FileBinaryDefinition implements BinaryDefinition {
	private static final long serialVersionUID = -4061392972792605539L;

	transient Path file;
	private transient boolean isTemp;
	private long size;
	private String name;
	
	public FileBinaryDefinition() {
		isTemp = false;
	}
	
	public FileBinaryDefinition(String name, Path file) {
		this.name = name;
		this.file = file;
		isTemp = false;
		try {
			size = Files.size(file);
		} catch (IOException e) {
			throw new IllegalStateException("can't get file size: " + file, e);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public InputStream getInputStream() {
		try {
			return Files.newInputStream(file);
		} catch (IOException e) {
			throw new IllegalStateException("can't open file: " + file, e);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (isTemp) {
			Files.deleteIfExists(file);
		}
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeUTF(name);
		long size = getSize();
		out.writeLong(size);
		try (InputStream is = getInputStream()) {
			byte[] buff = new byte[8192];
			long count = 0;
			int n = 0;
			while (-1 != (n = is.read(buff))) {
				out.write(buff, 0, n);
				count += n;
			}
			if (size != count) {
				throw new IOException("unmatch size:" + size + " != " + "InputStream length:" + count);
			}
		}
		out.flush();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		BinaryMetaDataService service = ServiceRegistry.getRegistry().getService(BinaryMetaDataService.class);
		name = in.readUTF();
		size = in.readLong();
		
		Path tempDir = service.getTempFileDir();
		file = Files.createTempFile(tempDir, "fbd_", ".tmp");
		
		try (OutputStream os = Files.newOutputStream(file)) {
			byte[] buff = new byte[8192];
			long remain = size;
			while (remain > 0) {
				int ren;
				if (remain >= buff.length) {
					ren = in.read(buff);
				} else {
					ren = in.read(buff, 0, (int) remain);
				}
				if (ren < 0) {
					throw new EOFException("cant read data correctly");
				}
				os.write(buff, 0, ren);
				remain -= ren;
			}
		}
		isTemp = true;
	}

}
