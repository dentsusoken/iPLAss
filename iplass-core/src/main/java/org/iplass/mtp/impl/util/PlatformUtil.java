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

package org.iplass.mtp.impl.util;

import java.io.IOException;
import java.io.Serializable;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.iplass.mtp.definition.Definition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformUtil {

	private static final Logger logger = LoggerFactory.getLogger(PlatformUtil.class);

	public static final String TITLE_KEY 	= "Implementation-Title";
	public static final String VERSION_KEY	= "Implementation-Version";
	public static final String VENDOR_KEY 	= "Implementation-Vendor";
	public static final String BUILD_KEY 	= "Build-Date";

	private static final PlatformInfo platformInfo;
	static {
		platformInfo = new PlatformInfo();
		URLConnection conn = null;
		try {
			//mtp-apiのバージョンを取得するため、Definitionクラスを起点に取得
			ClassLoader classLoader = Definition.class.getClassLoader();
			URL classURL = classLoader.getResource(Definition.class.getName().replace(".", "/") + ".class");
			conn = classURL.openConnection();
			if (conn == null) {
				logger.debug("platform information source is null.");
				platformInfo.setError(true);
				platformInfo.setErrorMessage("platform information source is null.");
			} else {
				logger.debug("platform information source is " + conn.getClass().getName());

				if (conn instanceof JarURLConnection) {
					//jar
					JarURLConnection jarConn = (JarURLConnection)conn;
					Manifest manifest = jarConn.getManifest();
					if (manifest == null || manifest.getMainAttributes().isEmpty()) {
						logger.debug("platform information is empty.");

						platformInfo.setError(true);
						platformInfo.setErrorMessage("platform information is empty.");
					} else {
						Attributes attributes = manifest.getMainAttributes();
						addInfo(TITLE_KEY, attributes);
						addInfo(VERSION_KEY, attributes, null);
						addInfo(VENDOR_KEY, attributes);
						addInfo(BUILD_KEY, attributes);

						String version = platformInfo.getVersion();
						if (version != null) {
							//バージョン番号のハッシュ値を求めてセット
							try {
								platformInfo.setVersionHash(HashUtil.digest(version, "SHA-256"));
							} catch (NoSuchAlgorithmException e) {
								platformInfo.setVersionHash(version);
							}
							logger.info("platform version is " + version + ". version hash=" + platformInfo.getVersionHash());
						} else {
							platformInfo.setError(true);
							platformInfo.setErrorMessage("platform version information read failed.");
						}
					}
				} else {
					logger.debug("platform information source is direct or not support type. type=" + conn.getClass().getName());

					platformInfo.setError(true);
					platformInfo.setErrorMessage("platform information source is direct or not support type. type=" + conn.getClass().getName());
				}
			}
		} catch (IOException e) {
			logger.debug("get platform information failed.", e);

			platformInfo.setError(true);
			platformInfo.setErrorMessage("platform information read failed.");
		}

		if (platformInfo.isError) {
			logger.warn(platformInfo.errorMessage);
		}
	}

	/**
	 * <p>バージョンのハッシュ値を返します。</p>
	 *
	 * @return バージョンのハッシュ値
	 */
	public static String getAPIVersion() {
		if (platformInfo.isError || platformInfo.getVersionHash() == null) {
			//開発時
			return "devMode_" + System.currentTimeMillis();
		}
		return platformInfo.getVersionHash();
	}

	/**
	 * <p>プラットフォーム情報を返します。<p>
	 *
	 * <p>バージョンが取得したい場合は、以下のコードで取得できます。</p>
	 * <pre>
	 * PlatformUtil.getPlatformInformation().getVersion()
	 * </pre>
	 *
	 * @return プラットフォーム情報
	 */
	public static PlatformInfo getPlatformInformation() {
		return platformInfo;
	}

	private static void addInfo(String key, Attributes attributes) {
		addInfo(key, attributes, "not defined");
	}
	private static void addInfo(String key, Attributes attributes, String nullValue) {
		//attributes.containsKeyだと一致しない（Attribute.Name(key)なので）
		//if (attributes.containsKey(key)) {
		if (attributes.getValue(key) != null) {
			platformInfo.addInfomation(key, attributes.getValue(key));
		} else {
			platformInfo.addInfomation(key, nullValue);
		}
	}


	public static class PlatformInfo implements Serializable {

		private static final long serialVersionUID = 8594045462747319422L;

		private boolean isError = false;

		private String errorMessage = null;

		private Map<String, String> infomations;

		private String versionHash;

		public PlatformInfo() {
		}

		public boolean isError() {
			return isError;
		}

		public void setError(boolean isError) {
			this.isError = isError;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public Map<String, String> getInfomations() {
			return infomations;
		}

		public void setInfomations(Map<String, String> infomations) {
			this.infomations = infomations;
		}

		public void addInfomation(String key, String value) {
			if (infomations == null) {
				infomations = new LinkedHashMap<String, String>();
			}
			infomations.put(key, value);
		}

		public String getVersionHash() {
			return versionHash;
		}

		public void setVersionHash(String versionHash) {
			this.versionHash = versionHash;
		}

		public String getVersion() {
			if (infomations != null) {
				return infomations.get(VERSION_KEY);
			}
			return null;
		}
	}

}
