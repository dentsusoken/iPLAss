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

package org.iplass.mtp.impl.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.EntityManager;

public class BinaryReferenceDataSource implements DataSource {
	
	private BinaryReference bin;
	
	public BinaryReferenceDataSource(BinaryReference bin) {
		this.bin = bin;
	}

	@Override
	public String getContentType() {
		String type = bin.getType();
		if (type == null || type.length() == 0) {
			return "application/octet-stream";
		}
		return bin.getType();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return ManagerLocator.getInstance().getManager(EntityManager.class).getInputStream(bin);
	}

	@Override
	public String getName() {
		return bin.getName();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new IOException("unsupported");
	}

}
