/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.webapi.rest;

import java.io.IOException;
import java.io.InputStream;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

class LimitRequestBodyInputStream extends InputStream {

	private long count;
	private InputStream actual;
	private long maxBodySize;
	private boolean contentLengthOver;
	
	LimitRequestBodyInputStream(InputStream actual, long maxBodySize, long contentLength) {
		if (contentLength >= 0 && contentLength > maxBodySize) {
			contentLengthOver = true;
		}
		
		this.maxBodySize = maxBodySize;
		this.actual = actual;
	}

	@Override
	public int read() throws IOException {
		checkContentLength();
		int i = actual.read();
		if (i >= 0) {
			countup(1);
		}
		return i;			
	}

	@Override
	public int read(final byte[] b) throws IOException {
		checkContentLength();
		int i = actual.read(b);
		if (i >= 0) {
			countup(i);
		}
		return i;
	}

	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
		checkContentLength();
		int i = actual.read(b, off, len);
		if (i >= 0) {
			countup(i);
		}
		return i;
	}

	private void countup(int i) {
		count += i;
		if (count > maxBodySize) {
			WebApplicationException ex = new WebApplicationException(Status.REQUEST_ENTITY_TOO_LARGE);
			try {
				actual.close();
			} catch (Exception e) {
				ex.addSuppressed(e);
			}
			throw ex;
		}
	}
	
	private void checkContentLength() {
		if (contentLengthOver) {
			WebApplicationException ex = new WebApplicationException(Status.REQUEST_ENTITY_TOO_LARGE);
			try {
				actual.close();
			} catch (Exception e) {
				ex.addSuppressed(e);
			}
			throw ex;
		}
	}
	
}
