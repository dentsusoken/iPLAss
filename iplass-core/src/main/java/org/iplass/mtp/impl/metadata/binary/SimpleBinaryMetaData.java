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
package org.iplass.mtp.impl.metadata.binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.Externalizable;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.iplass.mtp.definition.binary.BinaryDefinition;
import org.iplass.mtp.impl.metadata.MetaDataIllegalStateException;
import org.iplass.mtp.spi.ServiceRegistry;

public class SimpleBinaryMetaData implements BinaryMetaData, Externalizable {
	private String name;
	private long size;
	private byte[] inMemory;
	private Path tempPath;
	private volatile SoftReference<byte[]> cache;
	
	protected BinaryMetaDataService service = ServiceRegistry.getRegistry().getService(BinaryMetaDataService.class);
	
	public SimpleBinaryMetaData() {
	}
	
	public SimpleBinaryMetaData(String name) {
		this.name = name;
	}

	public SimpleBinaryMetaData(String name, Path source) {
		this.name = name;
		try (OutputStream os = getOutputStream()) {
			Files.copy(source, os);
		} catch (IOException e) {
			throw new MetaDataIllegalStateException("can't copy BinaryMetaData's source/tempFile:" + source + "/" + tempPath, e);
		}
	}
	
	public SimpleBinaryMetaData(BinaryDefinition def) {
		this.name = def.getName();
		try (OutputStream os = getOutputStream();
				InputStream is = def.getInputStream()) {
			IOUtils.copyLarge(is, os);
		} catch (IOException e) {
			throw new MetaDataIllegalStateException("can't copy BinaryMetaData's definition/tempFile:" + def.getName() + "/" + tempPath, e);
		}
	}
	
	@Override
	public BinaryDefinition currentConfig() {
		return new RefBinaryDefinition(this);
	}

	public Path getTempPath() {
		return tempPath;
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
		if (inMemory != null) {
			return new ByteArrayInputStream(inMemory);
		}
		
		SoftReference<byte[]> refCache = cache;
		if (refCache != null) {
			byte[] bd = refCache.get();
			if (bd != null) {
				return new ByteArrayInputStream(bd);
			}
		}
		
		if (tempPath == null) {
			return null;
		}
		
		try {
			if (size > service.getCacheMemoryThreshold()) {
				return Files.newInputStream(tempPath);
			} else {
				byte[] bd = new byte[(int) size];
				try (InputStream is = Files.newInputStream(tempPath)) {
					int len = IOUtils.read(is, bd);
					if (size != len) {
						throw new IOException("unmatch size:" + size + " != " + "Files.length:" + len);
					}
				}
				
				cache = new SoftReference<byte[]>(bd);
				return new ByteArrayInputStream(bd);
			}
		} catch (IOException e) {
			throw new MetaDataIllegalStateException("can't open/read BinaryMetaData's tempFile:" + tempPath, e);
		}
	}
	

	@Override
	public OutputStream getOutputStream() {
		try {
			return new SimpleBinaryMetaDataOutputStream();
		} catch (IOException e) {
			throw new MetaDataIllegalStateException("can't open/write BinaryMetaData's tempFile:" + tempPath, e);
		}
	}
	
	private Path initTempFile() throws IOException {
		if (tempPath != null) {
			Files.deleteIfExists(tempPath);
		}
		Path tempDir = service.getTempFileDir();
		tempPath = Files.createTempFile(tempDir, "sbmd_", ".tmp");
		return tempPath;
	}
	
	private class SimpleBinaryMetaDataOutputStream extends FilterOutputStream {
		
		private boolean isInMemory;
		private boolean flushed;
		
		private SimpleBinaryMetaDataOutputStream() throws IOException {
			super(service.getKeepInMemoryThreshold() > 0 ? new ByteArrayOutputStream(service.getKeepInMemoryThreshold()): Files.newOutputStream(initTempFile()));
			isInMemory = service.getKeepInMemoryThreshold() > 0;
		}

		@Override
		public void write(int b) throws IOException {
			flushed = false;
			size++;
			checkSize();
			out.write(b);
		}
		
		private void checkSize() throws IOException {
			flushed = false;
			if (isInMemory && size > service.getKeepInMemoryThreshold()) {
				ByteArrayOutputStream current = (ByteArrayOutputStream) out;
				out = Files.newOutputStream(initTempFile());
				current.writeTo(out);
				inMemory = null;
				isInMemory = false;
			}
		}

		@Override
		public void write(byte[] b) throws IOException {
			write(b, 0, b.length);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			if ((off | len | (b.length - (len + off)) | (off + len)) < 0) {
				throw new IndexOutOfBoundsException();
			}
			flushed = false;
			size += len;
			checkSize();
			out.write(b, off, len);
		}

		@Override
		public void flush() throws IOException {
			if (isInMemory && !flushed) {
				inMemory = ((ByteArrayOutputStream) out).toByteArray();
				flushed = true;
			}
			out.flush();
		}

		@Override
		public void close() throws IOException {
			if (isInMemory && !flushed) {
				inMemory = ((ByteArrayOutputStream) out).toByteArray();
				flushed = true;
			}
			super.close();
		}
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		try (InputStream is = getInputStream()) {
			if (is != null) {
				IOUtils.copy(is, out);
			}
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(name);
		out.writeLong(size);
		if (inMemory != null) {
			out.write(inMemory);
			if (size != inMemory.length) {
				throw new IOException("unmatch size:" + size + " != " + "inMemory.length:" + inMemory.length);
			}
		} else {
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
		}
		out.flush();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		service = ServiceRegistry.getRegistry().getService(BinaryMetaDataService.class);
		name = in.readUTF();
		size = in.readLong();
		if (service.getKeepInMemoryThreshold() < size) {
			try (OutputStream os = Files.newOutputStream(initTempFile())) {
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
		} else {
			byte[] dat = new byte[(int) size];
			long loaded = 0;
			while (loaded < size) {
				int ren = in.read(dat, (int) loaded, (int) (size - loaded));
				if (ren < 0) {
					throw new EOFException("cant read data correctly");
				}
				loaded += ren;
			}
			inMemory = dat;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (tempPath != null) {
			Files.deleteIfExists(tempPath);
		}
	}

}
