/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.web.actionmapping.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.Locale;

import org.iplass.mtp.impl.web.actionmapping.cache.Header.OpeType;
import org.iplass.mtp.impl.web.actionmapping.cache.Header.ValType;
import org.iplass.mtp.impl.web.actionmapping.cache.blocks.BinaryContentBlock;
import org.iplass.mtp.impl.web.actionmapping.cache.blocks.TextContentBlock;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class CachableHttpServletResponse extends HttpServletResponseWrapper {

	public static final String CONTENT_CACHE_NAME = "org.iplass.mtp.contentCache";
	public static final String CHSR_NAME = "org.iplass.mtp.cachableHttpServletResponse";
	public static final String ACTION_RUNTIME_NAME = "org.iplass.mtp.actionRuntime";
	private static final char[] LINE = System.getProperty("line.separator").toCharArray();

	private ByteArrayOutputStream binaryCache;
	private StringBuilder textCache;
	private boolean isError;
	private boolean isRedirect;

	private ServletOutputStreamWrapper os;
	private PrintWriter pr;

	private boolean doCache;
	private ContentCache currentContentCache;

	public CachableHttpServletResponse(HttpServletResponse response) {
		super(response);
	}

	public boolean isDoCache() {
		return doCache;
	}

	public void setDoCache(boolean doCache) {
		this.doCache = doCache;
	}

	public void setCurrentContentCache(ContentCache cc) {
		this.currentContentCache = cc;
		//		RequestStack.getCurrent().setAttribute(CONTENT_CACHE_NAME, cc);
	}

	public ContentCache getCurrentContentCache() {
		return currentContentCache;
		//		return (ContentCache) RequestStack.getCurrent().getAttribute(CONTENT_CACHE_NAME);
	}

	public void flushToContentCache() throws IOException {

		if (doCache) {
			ContentCache contentCache = getCurrentContentCache();
			if (binaryCache != null && binaryCache.size() > 0) {
				BinaryContentBlock bb = new BinaryContentBlock(binaryCache.toByteArray());
				contentCache.addContent(bb);
			}
			if (textCache != null && textCache.length() > 0) {
				TextContentBlock tb = new TextContentBlock(textCache.toString());
				contentCache.addContent(tb);
			}
		}


		binaryCache = null;
		textCache = null;
	}

	public boolean isError() {
		return isError;
	}

	public boolean isRedirect() {
		return isRedirect;
	}

	//TODO Cokkieをキャッシュに保存するかどうか検討
	@Override
	public void addCookie(Cookie cookie) {
		super.addCookie(cookie);
	}

	@Override
	public void addDateHeader(String name, long date) {
		super.addDateHeader(name, date);
		if (doCache) {
			getCurrentContentCache().addHeader(new Header(name, date, OpeType.ADD, ValType.DATE));
		}
	}

	@Override
	public void addHeader(String name, String value) {
		super.addHeader(name, value);
		if (doCache && !"Set-Cookie".equalsIgnoreCase(name)) {
			getCurrentContentCache().addHeader(new Header(name, value, OpeType.ADD, ValType.STRING));
		}
	}

	@Override
	public void addIntHeader(String name, int value) {
		super.addIntHeader(name, value);
		if (doCache) {
			getCurrentContentCache().addHeader(new Header(name, value, OpeType.ADD, ValType.INT));
		}
	}

	@Override
	public boolean containsHeader(String name) {
		return super.containsHeader(name);
	}

	@Override
	public String encodeRedirectURL(String url) {
		return super.encodeRedirectURL(url);
	}

	//	@SuppressWarnings("deprecation")
	//	@Override
	//	public String encodeRedirectUrl(String url) {
	//		return super.encodeRedirectUrl(url);
	//	}

	@Override
	public String encodeURL(String url) {
		return super.encodeURL(url);
	}

	//	@SuppressWarnings("deprecation")
	//	@Override
	//	public String encodeUrl(String url) {
	//		return super.encodeUrl(url);
	//	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		super.sendError(sc, msg);
		isError = true;
	}

	@Override
	public void sendError(int sc) throws IOException {
		super.sendError(sc);
		isError = true;
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		super.sendRedirect(location);
		isRedirect = true;
	}

	@Override
	public void setDateHeader(String name, long date) {
		super.setDateHeader(name, date);
		if (doCache) {
			getCurrentContentCache().addHeader(new Header(name, date, OpeType.SET, ValType.DATE));
		}
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(name, value);
		if (doCache && !"Set-Cookie".equalsIgnoreCase(name)) {
			getCurrentContentCache().addHeader(new Header(name, value, OpeType.SET, ValType.STRING));
		}
	}

	@Override
	public void setIntHeader(String name, int value) {
		super.setIntHeader(name, value);
		if (doCache) {
			getCurrentContentCache().addHeader(new Header(name, value, OpeType.SET, ValType.INT));
		}
	}

	//	@SuppressWarnings("deprecation")
	//	@Override
	//	public void setStatus(int sc, String sm) {
	//		super.setStatus(sc, sm);
	//		if (doCache) {
	//			getCurrentContentCache().setHttpStatus(sc);
	////			contentCache.setHttpStatusMessage(sm);//setStatus(int sc, String sm)は非推奨なので、smは保存しない
	//		}
	//	}

	@Override
	public void setStatus(int sc) {
		super.setStatus(sc);
		if (doCache) {
			getCurrentContentCache().setHttpStatus(sc);
		}
	}

	@Override
	public void flushBuffer() throws IOException {
		super.flushBuffer();
	}

	@Override
	public int getBufferSize() {
		return super.getBufferSize();
	}

	@Override
	public String getCharacterEncoding() {
		return super.getCharacterEncoding();
	}

	@Override
	public String getContentType() {
		return super.getContentType();
	}

	@Override
	public Locale getLocale() {
		return super.getLocale();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (os == null) {
			os = new ServletOutputStreamWrapper(super.getOutputStream());
		}
		return os;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (pr == null) {
			pr = new PrintWriterWrapper(super.getWriter());
		}
		return pr;
	}

	@Override
	public boolean isCommitted() {
		return super.isCommitted();
	}

	@Override
	public void reset() {
		super.reset();
		if (doCache) {
			getCurrentContentCache().resetResponseData();
		}
	}

	@Override
	public void resetBuffer() {
		super.resetBuffer();
		if (doCache) {
			getCurrentContentCache().resetContents();
		}
	}

	@Override
	public void setBufferSize(int size) {
		super.setBufferSize(size);
	}

	@Override
	public void setCharacterEncoding(String charset) {
		super.setCharacterEncoding(charset);
		//TODO contentType未指定の場合、上書きした場合、キャッシュ内のContentTypeを更新する必要あり。。。
	}

	@Override
	public void setContentLength(int len) {
		super.setContentLength(len);
	}

	@Override
	public void setContentType(String type) {
		super.setContentType(type);
		if (doCache) {
			//contentTypeでcharsetも指定されている想定
			getCurrentContentCache().setContentType(type);
		}
	}

	@Override
	public void setLocale(Locale loc) {
		super.setLocale(loc);
	}

	private class ServletOutputStreamWrapper extends ServletOutputStream {

		private ServletOutputStream actual;

		public ServletOutputStreamWrapper(ServletOutputStream actual) {
			this.actual = actual;
		}

		private OutputStream getOS() {
			if (binaryCache == null) {
				binaryCache = new ByteArrayOutputStream(1024*8);//8K
			}
			return binaryCache;
		}

		@Override
		public void write(int b) throws IOException {
			actual.write(b);
			if (doCache) {
				getOS().write(b);
			}
		}

		@Override
		public void write(byte[] b) throws IOException {
			//SRB対応-チェック済み
			actual.write(b);
			if (doCache) {
				//SRB対応-チェック済み
				getOS().write(b);
			}
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			//SRB対応-チェック済み
			actual.write(b, off, len);
			if (doCache) {
				//SRB対応-チェック済み
				getOS().write(b, off, len);
			}
		}

		@Override
		public void flush() throws IOException {
			actual.flush();
		}

		@Override
		public void close() throws IOException {
			actual.close();
		}

		@Override
		public boolean isReady() {
			return actual.isReady();
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			actual.setWriteListener(writeListener);
		}
	}

	private class PrintWriterWrapper extends PrintWriter {

		private PrintWriter actual;
		private Formatter formatter;

		private PrintWriterWrapper(PrintWriter actual) {
			super(actual);
			this.actual = actual;
		}

		private StringBuilder getSb() {
			if (textCache == null) {
				textCache = new StringBuilder(1024 * 8);//8K
			}
			return textCache;
		}


		@Override
		public void flush() {
			actual.flush();
		}


		@Override
		public void write(int c) {
			actual.write(c);
			if (doCache) {
				getSb().append(c);
			}
		}

		@Override
		public void write(char[] buf, int off, int len) {
			actual.write(buf, off, len);
			if (doCache) {
				getSb().append(buf, off, len);
			}
		}

		@Override
		public void write(char[] buf) {
			actual.write(buf);
			if (doCache) {
				getSb().append(buf);
			}
		}

		@Override
		public void write(String s, int off, int len) {
			actual.write(s, off, len);
			if (doCache) {
				getSb().append(s, off, off + len);
			}
		}

		@Override
		public void write(String s) {
			actual.write(s);
			if (doCache) {
				getSb().append(s);
			}
		}

		@Override
		public void print(boolean b) {
			actual.print(b);
			if (doCache) {
				getSb().append(b);
			}
		}

		@Override
		public void print(char c) {
			actual.print(c);
			if (doCache) {
				getSb().append(c);
			}
		}

		@Override
		public void print(int i) {
			actual.print(i);
			if (doCache) {
				getSb().append(i);
			}
		}

		@Override
		public void print(long l) {
			actual.print(l);
			if (doCache) {
				getSb().append(l);
			}
		}

		@Override
		public void print(float f) {
			actual.print(f);
			if (doCache) {
				getSb().append(f);
			}
		}

		@Override
		public void print(double d) {
			actual.print(d);
			if (doCache) {
				getSb().append(d);
			}
		}

		@Override
		public void print(char[] s) {
			actual.print(s);
			if (doCache) {
				getSb().append(s);
			}
		}

		@Override
		public void print(String s) {
			actual.print(s);
			if (doCache) {
				getSb().append(s);
			}
		}

		@Override
		public void print(Object obj) {
			actual.print(obj);
			if (doCache) {
				getSb().append(obj);
			}
		}

		@Override
		public void println() {
			actual.println();
			if (doCache) {
				getSb().append(LINE);
			}
		}

		@Override
		public void println(boolean x) {
			actual.println(x);
			if (doCache) {
				getSb().append(x).append(LINE);
			}
		}

		@Override
		public void println(char x) {
			actual.println(x);
			if (doCache) {
				getSb().append(x).append(LINE);
			}
		}

		@Override
		public void println(int x) {
			actual.println(x);
			if (doCache) {
				getSb().append(x).append(LINE);
			}
		}

		@Override
		public void println(long x) {
			actual.println(x);
			if (doCache) {
				getSb().append(x).append(LINE);
			}
		}

		@Override
		public void println(float x) {
			actual.println(x);
			if (doCache) {
				getSb().append(x).append(LINE);
			}
		}

		@Override
		public void println(double x) {
			actual.println(x);
			if (doCache) {
				getSb().append(x).append(LINE);
			}
		}

		@Override
		public void println(char[] x) {
			actual.println(x);
			if (doCache) {
				getSb().append(x).append(LINE);
			}
		}

		@Override
		public void println(String x) {
			actual.println(x);
			if (doCache) {
				getSb().append(x).append(LINE);
			}
		}

		@Override
		public void println(Object x) {
			actual.println(x);
			if (doCache) {
				getSb().append(x).append(LINE);
			}
		}

		@Override
		public PrintWriter printf(String format, Object... args) {
			actual.printf(format, args);

			if (doCache) {
				if ((formatter == null) || (formatter.locale() != Locale.getDefault())) {
					formatter = new Formatter(getSb());
				}
				formatter.format(Locale.getDefault(), format, args);
			}
			return this;
		}

		@Override
		public PrintWriter printf(Locale l, String format, Object... args) {
			actual.printf(l, format, args);

			if (doCache) {
				if ((formatter == null) || (formatter.locale() != l)) {
					formatter = new Formatter(getSb(), l);
				}
				formatter.format(l, format, args);
			}
			return this;
		}

		@Override
		public PrintWriter format(String format, Object... args) {
			actual.format(format, args);

			if (doCache) {
				if ((formatter == null) || (formatter.locale() != Locale.getDefault())) {
					formatter = new Formatter(getSb());
				}
				formatter.format(Locale.getDefault(), format, args);
			}
			return this;
		}

		@Override
		public PrintWriter format(Locale l, String format, Object... args) {
			actual.format(l, format, args);

			if (doCache) {
				if ((formatter == null) || (formatter.locale() != l)) {
					formatter = new Formatter(getSb(), l);
				}
				formatter.format(l, format, args);
			}
			return this;
		}

		@Override
		public PrintWriter append(CharSequence csq) {
			actual.append(csq);
			if (doCache) {
				getSb().append(csq);
			}
			return this;
		}

		@Override
		public PrintWriter append(CharSequence csq, int start, int end) {
			actual.append(csq, start, end);
			if (doCache) {
				getSb().append(csq, start, end);
			}
			return this;
		}

		@Override
		public PrintWriter append(char c) {
			actual.append(c);
			if (doCache) {
				getSb().append(c);
			}
			return this;
		}

		@Override
		public void close() {
			actual.close();
		}

		@Override
		public boolean checkError() {
			return actual.checkError();
		}
	}

}
