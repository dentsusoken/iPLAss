/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.script;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.slf4j.Logger;

public class LoggerPrintWriter extends PrintWriter {
	
	public LoggerPrintWriter(Logger logger) {
		super(new LoggerWriter(logger), true);
	}
	
	@Override
	public void println() {
		//loggerでは自動的に改行されてしまうので、、、
		try {
        	synchronized (lock) {
        		if (out == null) {
        			throw new IOException("Stream closed");
        		}
        		out.flush();
            }
		} catch (InterruptedIOException x) {
			Thread.currentThread().interrupt();
		} catch (IOException x) {
			setError();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}
	
	private static class LoggerWriter extends Writer {
		private Logger logger;
		
		String bufStr = null;
		StringBuilder buf = null;
		
		private LoggerWriter(Logger logger) {
			this.logger = logger;
		}
		
		@Override
		public void write(int c) throws IOException {
			super.write(c);
		}

		@Override
		public void write(char[] cbuf) throws IOException {
			super.write(cbuf);
		}

		@Override
		public void write(String str) throws IOException {
			if (buf != null) {
				buf.append(str);
			} else if (bufStr == null) {
				bufStr = str;
			} else {
				buf = new StringBuilder();
				buf.append(bufStr);
				buf.append(str);
				bufStr = null;
			}
		}

		@Override
		public void write(String str, int off, int len) throws IOException {
			write(str.substring(off, off + len));
		}

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			if (buf != null) {
				buf.append(cbuf, off, len);
			} else if (bufStr == null) {
				bufStr = new String(cbuf, off, len);
			} else {
				buf = new StringBuilder();
				buf.append(bufStr);
				buf.append(cbuf, off, len);
				bufStr = null;
			}
		}
		
		@Override
		public Writer append(CharSequence csq) throws IOException {
			return super.append(csq);
		}

		@Override
		public Writer append(CharSequence csq, int start, int end)
				throws IOException {
			return super.append(csq, start, end);
		}

		@Override
		public Writer append(char c) throws IOException {
			return super.append(c);
		}

		@Override
		public void flush() throws IOException {
			if (buf != null) {
				logger.info(buf.toString());
			} else if (bufStr != null) {
				logger.info(bufStr);
			}
			buf = null;
			bufStr = null;
		}

		@Override
		public void close() throws IOException {
			flush();
		}
	}

}
