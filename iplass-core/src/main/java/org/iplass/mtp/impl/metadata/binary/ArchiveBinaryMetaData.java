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
package org.iplass.mtp.impl.metadata.binary;

import java.io.EOFException;
import java.io.Externalizable;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.iplass.mtp.definition.binary.ArchiveBinaryDefinition;
import org.iplass.mtp.definition.binary.BinaryDefinition;
import org.iplass.mtp.impl.metadata.MetaDataIllegalStateException;
import org.iplass.mtp.spi.ServiceRegistry;

public class ArchiveBinaryMetaData implements BinaryMetaData, Externalizable {
	//zip or jar enabled
	
	private static class Entry {
		long size;
		SimpleBinaryMetaData binData;
	}
	
	private String name;
	private long size;
	private Path tempPath;

	protected BinaryMetaDataService service = ServiceRegistry.getRegistry().getService(BinaryMetaDataService.class);
	
	private final ConcurrentHashMap<String, Entry> entryCache;
	
	public ArchiveBinaryMetaData() {
		entryCache = new ConcurrentHashMap<>();
	}
	
	public ArchiveBinaryMetaData(String name) {
		this.name = name;
		entryCache = new ConcurrentHashMap<>();
	}
	
	public ArchiveBinaryMetaData(String name, Path source) {
		this.name = name;
		entryCache = new ConcurrentHashMap<>();
		try (OutputStream os = getOutputStream()) {
			Files.copy(source, os);
			readZipEntry();
		} catch (IOException e) {
			throw new MetaDataIllegalStateException("can't copy ArchiveBinaryMetaData's source/tempFile:" + source + "/" + tempPath, e);
		}
	}
	
	public ArchiveBinaryMetaData(ArchiveBinaryDefinition def) {
		this.name = def.getName();
		entryCache = new ConcurrentHashMap<>();
		try (OutputStream os = getOutputStream();
				InputStream is = def.getInputStream()) {
			IOUtils.copyLarge(is, os);
			readZipEntry();
		} catch (IOException e) {
			throw new MetaDataIllegalStateException("can't copy ArchiveBinaryMetaData's definition/tempFile:" + def.getName() + "/" + tempPath, e);
		}
	}
	
	@Override
	public BinaryDefinition currentConfig() {
		return new RefArchiveBinaryDefinition(this);
	}

	@Override
	public String getName() {
		return name;
	}

	public Path getTempPath() {
		return tempPath;
	}
	
	public boolean hasEntry(String path) {
		return entryCache.containsKey(path);
	}
	
	public InputStream getEntryAsStream(String path) {
		Entry entry = entryCache.get(path);
		if (entry == null) {
			return null;
		}
		if (entry.binData == null) {
			loadZipEntry(path);
			entry = entryCache.get(path);
			if (entry == null || entry.binData == null) {
				return null;
			}
		}
		return entry.binData.getInputStream();
	}
	
	public long getEntrySize(String path) {
		Entry entry = entryCache.get(path);
		if (entry == null) {
			return -1;
		}
		return entry.size;
	}
	
	private void loadZipEntry(String path) {
		entryCache.compute(path, (k, v) -> {
			if (v.binData == null) {
				SimpleBinaryMetaData newV = new SimpleBinaryMetaData(path);
				try (ZipFile zip = new ZipFile(tempPath.toFile())) {
					try (InputStream is = zip.getInputStream(zip.getEntry(path));
							OutputStream os = newV.getOutputStream()) {
						IOUtils.copy(is, os);
					}
					Entry newE = new Entry();
					newE.size = newV.getSize();
					newE.binData = newV;
					return newE;
				} catch (IOException e) {
					throw new MetaDataIllegalStateException("can't open/read ArchiveBinaryMetaData's tempFile:" + tempPath, e);
				}
			} else {
				return v;
			}
		});
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public InputStream getInputStream() {
		if (tempPath == null) {
			return null;
		}
		
		try {
			return Files.newInputStream(tempPath);
		} catch (IOException e) {
			throw new MetaDataIllegalStateException("can't open/read ArchiveBinaryMetaData's tempFile:" + tempPath, e);
		}
	}
	
	@Override
	public OutputStream getOutputStream() {
		try {
			return new ZipMetaDataOutputStream();
		} catch (IOException e) {
			throw new MetaDataIllegalStateException("can't open/write ArchiveBinaryMetaData's tempFile:" + tempPath, e);
		}
	}
	
	private Path initTempFile() throws IOException {
		if (tempPath != null) {
			Files.deleteIfExists(tempPath);
		}
		Path tempDir = service.getTempFileDir();
		tempPath = Files.createTempFile(tempDir, "abmd_", ".tmp");
		return tempPath;
	}
	
	private void readZipEntry() throws IOException {
		entryCache.clear();
		try (ZipFile zip = new ZipFile(tempPath.toFile())) {
				zip.stream().forEach(e -> {
					String name = e.getName();
					Entry entry = new Entry();
					entry.size = e.getSize();
					entryCache.put(name, entry);
				});
			};
	}
	
	private class ZipMetaDataOutputStream extends FilterOutputStream {
		
		private ZipMetaDataOutputStream() throws IOException {
			super(Files.newOutputStream(initTempFile()));
		}

		@Override
		public void write(int b) throws IOException {
			size++;
			out.write(b);
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
			size += len;
			out.write(b, off, len);
		}

		@Override
		public void flush() throws IOException {
			out.flush();
		}

		@Override
		public void close() throws IOException {
			super.close();
			//read zip entry
			readZipEntry();
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

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		service = ServiceRegistry.getRegistry().getService(BinaryMetaDataService.class);
		name = in.readUTF();
		size = in.readLong();
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
		readZipEntry();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (tempPath != null) {
			Files.deleteIfExists(tempPath);
		}
	}

}
