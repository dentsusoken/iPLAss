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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.metadata.binary.ArchiveBinaryMetaData;
import org.iplass.mtp.impl.metadata.binary.BinaryMetaData;
import org.iplass.mtp.impl.metadata.binary.BinaryMetaDataService;
import org.iplass.mtp.impl.metadata.binary.SimpleBinaryMetaData;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * MetaDataのXMLにbyte[]をマーシャルする際に、
 * byte[]を複数のチャンクに分割して保存する為のクラス。
 * JAXBでマーシャル・アンマーシャルする際に、チャンク単位でしかメモリを消費しない。
 * 
 * @author K.Higuchi
 *
 */
public class ChunkedBinary {
	
	static final String TYPE_ARCHIVE = "archive";
	
	private String name;
	private String type;
	private List<Chunk> chunk;
	
	public ChunkedBinary(BinaryMetaData binMeta) {
		
		if (binMeta instanceof ArchiveBinaryMetaData) {
			type = TYPE_ARCHIVE;
		}
		this.name= binMeta.getName();
		BinaryMetaDataService service = ServiceRegistry.getRegistry().getService(BinaryMetaDataService.class);
		chunk = new ArrayList<>();
		long offset = 0;
		while (offset < binMeta.getSize()) {
			int length = (int) Math.min((long) service.getXmlBinaryChunkSize(), binMeta.getSize() - offset);
			chunk.add(new Chunk(offset, length, binMeta));
			offset += service.getXmlBinaryChunkSize();
		}
	}
	
	public ChunkedBinary() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Chunk> getChunk() {
		return chunk;
	}
	public void setChunk(List<Chunk> chunk) {
		this.chunk = chunk;
	}
	
	public BinaryMetaData toBinaryMetaData() throws IOException {
		
		BinaryMetaData binMeta;
		if (TYPE_ARCHIVE.equals(type)) {
			binMeta = new ArchiveBinaryMetaData(name);
		} else {
			binMeta = new SimpleBinaryMetaData(name);
		}
		if (chunk != null && chunk.size() > 0) {
			try (OutputStream os = binMeta.getOutputStream()) {
				for (Chunk c: chunk) {
					os.write(c.getBin());
				}
			}
		}
		return binMeta;
	}
	
	public void dispose() {
		if (chunk != null) {
			for (Chunk c: chunk) {
				c.dispose();
			}
		}
	}
	
}
