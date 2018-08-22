/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashSet;

import org.iplass.mtp.impl.script.MetaUtilityClass.UtilityClassRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.GroovyResourceLoader;

public class UtilitiyClassResourceLoader implements GroovyResourceLoader {
	private static Logger fatalLog = LoggerFactory.getLogger("mtp.fatal.classloader");

	private final GroovyResourceLoader parentResourceLoader;
	private final GroovyScriptService service;
	private HashSet<String> utilityClassNames;


	public UtilitiyClassResourceLoader(GroovyResourceLoader parentResourceLoader, GroovyScriptService service) {
		this.parentResourceLoader = parentResourceLoader;
		this.service = service;
	}

	@Override
	public URL loadGroovySource(String name) throws MalformedURLException {

		boolean isMeta = false;
		synchronized (this) {
			if (utilityClassNames == null) {
				try {
					utilityClassNames = new HashSet<String>(service.nameList());
				} catch (Exception e) {
					fatalLog.error("utility class list loading process faild... " + e, e);
				} catch (Error e) {
					fatalLog.error("utility class list loading process faild... " + e, e);
					throw e;
				}
			}
			isMeta = utilityClassNames != null && utilityClassNames.contains(name);
		}

		if (!isMeta) {
			return parentResourceLoader.loadGroovySource(name);
		}

		//GroovyClassLoaderとの兼ね合いで、事前にMetaDataの中身を取得しておく。
		//openConnectionのタイミングでMetaData取得した場合、MetaDataの取得時のロックと、GroovyClassLoader内でのコンパイル時のロックでデッドロックが発生する。

//		return new URL(null, "metadata://utilityclass/" + name, new StreamHandler(service));
		return new URL(null, "metadata://utilityclass/" + name, new PreLoadStreamHandler(service, name));
	}

	public void clearClassNameList() {
		synchronized (this) {
			utilityClassNames = null;
		}
	}

	private static class PreLoadStreamHandler extends URLStreamHandler {
		private String source;
		private Throwable error;

		public PreLoadStreamHandler(GroovyScriptService service, String name) {
			UtilityClassRuntime ucr = null;
			try {
				ucr = service.getRuntimeByName(name);
			} catch (Exception e) {
				fatalLog.error("utility class:" + name + " cant load. maybe need Reload or restart." + e, e);
				error = new IOException(name + " cant read from UtilityMetaData:" + e, e);
			} catch (Error e) {
				fatalLog.error("utility class:" + name + " cant load. maybe need Reload or restart." + e, e);
				error = e;
			}
			if (ucr == null || ucr.getMetaData() == null || ucr.getMetaData().getScript() == null) {
				fatalLog.error("utility class:" + name + " cant load(metaData or script is null...). maybe need Reload or restart.");
				error = new IOException(name + " not found or no script...");
			}

			source = ucr.getMetaData().getScript();
		}

		protected URLConnection openConnection(URL u) throws IOException {
			return new PreLoadConnection(u);
		}

		private class PreLoadConnection extends URLConnection {
			PreLoadConnection(URL u) throws IOException {
				super(u);
			}

			public void connect() throws IOException {
			}

			public InputStream getInputStream() throws IOException {
				if (error != null) {
					if (error instanceof IOException) {
						throw (IOException) error;
					} else if (error instanceof Error) {
						throw (Error) error;
					} else {
						throw new IOException(error);
					}
				}
				return new ByteArrayInputStream(source.getBytes("UTF-8"));
			}

		}
	}

	@Deprecated
	private static class StreamHandler extends URLStreamHandler {
		private final GroovyScriptService service;

		public StreamHandler(GroovyScriptService service) {
			this.service = service;
		}

		protected URLConnection openConnection(URL u) throws IOException {
			return new Connection(u, service);
		}
	}

	@Deprecated
	private static class Connection extends URLConnection {
		private final GroovyScriptService service;

		public Connection(URL u, GroovyScriptService service)
				throws IOException {
			super(u);
			this.service = service;
		}

		public void connect() throws IOException {
		}

		public InputStream getInputStream() throws IOException {

			UtilityClassRuntime ucr;
			try {
				ucr = service.getRuntimeByName(getURL().getPath().substring(1));
			} catch (Exception e) {
				fatalLog.error("utility class:" + getURL().getPath() + " cant load. maybe need Reload or restart." + e, e);
				throw new IOException(getURL().getPath() + " cant read from UtilityMetaData:" + e, e);
			} catch (Error e) {
				fatalLog.error("utility class:" + getURL().getPath() + " cant load. maybe need Reload or restart." + e, e);
				throw e;
			}
			if (ucr == null || ucr.getMetaData() == null || ucr.getMetaData().getScript() == null) {
				fatalLog.error("utility class:" + getURL().getPath() + " cant load(metaData or script is null...). maybe need Reload or restart.");
				throw new IOException(getURL().getPath() + " not found or no script...");
			}
			return new ByteArrayInputStream(ucr.getMetaData().getScript().getBytes("UTF-8"));
		}

	}

}
