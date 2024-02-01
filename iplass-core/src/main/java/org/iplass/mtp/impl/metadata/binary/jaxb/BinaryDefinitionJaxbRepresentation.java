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
package org.iplass.mtp.impl.metadata.binary.jaxb;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.iplass.mtp.definition.binary.ArchiveBinaryDefinition;

@XmlType(name="binaryDefinition", namespace="http://mtp.iplass.org/xml/definition/binary")
public class BinaryDefinitionJaxbRepresentation implements ArchiveBinaryDefinition {
	private static final long serialVersionUID = 5419587740677970289L;

	private String name;
	private long size;
	
	public BinaryDefinitionJaxbRepresentation() {
	}

	public BinaryDefinitionJaxbRepresentation(String name, long size) {
		this.name = name;
		this.size = size;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getSize() {
		return size;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@XmlTransient
	@Override
	public InputStream getInputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getEntryAsStream(String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getEntrySize(String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasEntry(String path) {
		throw new UnsupportedOperationException();
	}

}
