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

import java.io.InputStream;

import org.iplass.mtp.definition.binary.ArchiveBinaryDefinition;

class RefArchiveBinaryDefinition implements ArchiveBinaryDefinition {
	private static final long serialVersionUID = -5719000121510382495L;

	private ArchiveBinaryMetaData bin;
	
	RefArchiveBinaryDefinition() {
	}
	
	RefArchiveBinaryDefinition(ArchiveBinaryMetaData bin) {
		this.bin = bin;
	}

	@Override
	public long getSize() {
		return bin.getSize();
	}

	@Override
	public InputStream getInputStream() {
		return bin.getInputStream();
	}

	@Override
	public InputStream getEntryAsStream(String path) {
		return bin.getEntryAsStream(path);
	}

	@Override
	public long getEntrySize(String path) {
		return bin.getEntrySize(path);
	}

	@Override
	public boolean hasEntry(String path) {
		return bin.hasEntry(path);
	}

	@Override
	public String getName() {
		return bin.getName();
	}

}
