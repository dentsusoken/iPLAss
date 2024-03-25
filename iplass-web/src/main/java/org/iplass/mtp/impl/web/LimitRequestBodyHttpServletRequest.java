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

package org.iplass.mtp.impl.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class LimitRequestBodyHttpServletRequest extends HttpServletRequestWrapper {
	
	private long maxBodySize;

	public LimitRequestBodyHttpServletRequest(HttpServletRequest request, long maxBodySize) {
		super(request);
		this.maxBodySize = maxBodySize;
	}
	
	public ServletInputStream getInputStream() throws IOException {
		return new LimitRequestBodyServletInputStream(getRequest().getInputStream(), getContentLengthLong());
	}

	public BufferedReader getReader() throws IOException {
		String charset = getCharacterEncoding();
		if (charset == null) {
			charset = "utf-8";
		}
		
		return new BufferedReader(new InputStreamReader(getInputStream(), charset));
	}
    
	private class LimitRequestBodyServletInputStream extends ServletInputStream {

		private long count;
		private ServletInputStream actual;
		private boolean contentLengthOver;
		
		private LimitRequestBodyServletInputStream(ServletInputStream actual, long contentLength) {
			if (contentLength >= 0 && contentLength > maxBodySize) {
				contentLengthOver = true;
			}
			
			this.actual = actual;
		}

		@Override
		public boolean isFinished() {
			return actual.isFinished();
		}

		@Override
		public boolean isReady() {
			return actual.isReady();
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			actual.setReadListener(readListener);
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

		@Override
		public int readLine(byte[] b, int off, int len) throws IOException {
			checkContentLength();
			int i = actual.readLine(b, off, len);
			if (i >= 0) {
				countup(i);
			}
			return i;
		}

		private void countup(int i) {
			count += i;
			if (count > maxBodySize) {
				RequestBodyTooLargeException ex = new RequestBodyTooLargeException("Request body too large. MaxBodySize:" + maxBodySize);
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
				RequestBodyTooLargeException ex = new RequestBodyTooLargeException("Request body too large. MaxBodySize:" + maxBodySize);
				try {
					actual.close();
				} catch (Exception e) {
					ex.addSuppressed(e);
				}
				throw ex;
			}
		}
		
	}

}
