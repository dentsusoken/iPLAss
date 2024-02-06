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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * FileのPathを指定してArchiveBinaryDefinitionを指し示す為のクラス。
 * 
 * 
 * @author K.Higuchi
 *
 */
public class FileArchiveBinaryDefinition extends FileBinaryDefinition implements ArchiveBinaryDefinition {
	private static final long serialVersionUID = -4391803442328436515L;

	public FileArchiveBinaryDefinition() {
		super();
	}
	
	public FileArchiveBinaryDefinition(String name, Path file) {
		super(name, file);
	}

	@Override
	public InputStream getEntryAsStream(String path) {
		try (ZipFile zip = new ZipFile(file.toFile())) {
			ZipEntry e = zip.getEntry(path);
			if (e == null) {
				return null;
			} else {
				return new ZipInputStreamPerZipFile(zip.getInputStream(e), zip);
			}
		} catch (IOException e) {
			throw new IllegalStateException("can't open/read file:" + file, e);
		}
	}

	@Override
	public long getEntrySize(String path) {
		try (ZipFile zip = new ZipFile(file.toFile())) {
			ZipEntry e = zip.getEntry(path);
			if (e == null) {
				return -1;
			} else {
				return e.getSize();
			}
		} catch (IOException e) {
			throw new IllegalStateException("can't open/read file:" + file, e);
		}
	}

	@Override
	public boolean hasEntry(String path) {
		try (ZipFile zip = new ZipFile(file.toFile())) {
			ZipEntry e = zip.getEntry(path);
			if (e != null) {
				return true;
			}
		} catch (IOException e) {
			throw new IllegalStateException("can't open/read file:" + file, e);
		}
		return false;
	}
	
	
	private static class ZipInputStreamPerZipFile extends FilterInputStream {
		private ZipFile zip;

		protected ZipInputStreamPerZipFile(InputStream in, ZipFile zip) {
			super(in);
			this.zip = zip;
		}

		@Override
		public void close() throws IOException {
			try {
				super.close();
			} finally {
				zip.close();
			}
		}
	}

}
