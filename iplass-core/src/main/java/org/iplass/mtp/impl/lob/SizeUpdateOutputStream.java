/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.impl.lob.lobstore.LobData;
import org.iplass.mtp.impl.lob.lobstore.LobStoreRuntimeException;

public class SizeUpdateOutputStream extends FilterOutputStream {
	
	private int tenantId;
	private LobData lobData;
	private LobDao dao;

	private boolean closed;

	public SizeUpdateOutputStream(OutputStream out, int tenantId, LobData lobData, LobDao dao) {
		super(out);
		this.tenantId = tenantId;
		this.lobData  = lobData;
		this.dao = dao;
	}
	
	private void storeBlobSize() {
		if (!dao.updateLobStoreSize(tenantId, lobData.getLobDataId(), lobData.getSize())) {
			//プログラムから更新の場合、LOB_STOREでロックしてるので、発生しえないはず。なのでシステム例外。
			throw new LobStoreRuntimeException("Concurrent Update Occured.");
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
	}

	@Override
	public void close() throws IOException {
		if (!closed) {
			super.close();
			closed = true;
			storeBlobSize();
		}
	}
}
