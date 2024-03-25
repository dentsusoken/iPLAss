/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.web.actionmapping;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

//Servlet実装を参考に
class NoBodyResponse extends HttpServletResponseWrapper {
	private NoBodyOutputStream noBody;
	private PrintWriter writer;
	private boolean didSetContentLength;

	NoBodyResponse(HttpServletResponse r) {
		super(r);
		noBody = new NoBodyOutputStream();
	}
	
	void setContentLength() {
		if (!didSetContentLength) {
			  super.setContentLength(noBody.getContentLength());
		}
	}
	
	@Override
	public void setContentLength(int len) {
		super.setContentLength(len);
		didSetContentLength = true;
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(name, value);
		checkHeader(name);
	}

	@Override
	public void addHeader(String name, String value) {
		super.addHeader(name, value);
		checkHeader(name);
	}

	@Override
	public void setIntHeader(String name, int value) {
		super.setIntHeader(name, value);
		checkHeader(name);
	}

	@Override
	public void addIntHeader(String name, int value) {
		super.addIntHeader(name, value);
		checkHeader(name);
	}
	
	private void checkHeader(String name) {
		if ("content-length".equalsIgnoreCase(name)) {
			didSetContentLength = true;
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return noBody;
	}
	
	@Override
	public PrintWriter getWriter() throws UnsupportedEncodingException {
		if (writer == null) {
			OutputStreamWriter w = new OutputStreamWriter(noBody, getCharacterEncoding());
			writer = new PrintWriter(w);
		}
		return writer;
	}
	
	class NoBodyOutputStream extends ServletOutputStream {
		private int contentLength = 0;

		NoBodyOutputStream() {
		}

		int getContentLength() {
			return contentLength;
		}

		@Override
		public void write(int b) {
			contentLength++;
		}
		
		@Override
		public synchronized void write(byte[] buf, int offset, int len) throws IOException {
			if (buf == null) {
				throw new NullPointerException("buf is null");
			}
			
			if (offset < 0 || len < 0 || offset+len > buf.length) {
				String msg = "index out of bounds: offset={0}, len={1}, buf.length={2}";
				Object[] msgArgs = new Object[3];
				msgArgs[0] = Integer.valueOf(offset);
				msgArgs[1] = Integer.valueOf(len);
				msgArgs[2] = Integer.valueOf(buf.length);
				msg = MessageFormat.format(msg, msgArgs);
				throw new IndexOutOfBoundsException(msg);
			}
			
			contentLength += len;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			//do anything?
		}
	}
	
}
