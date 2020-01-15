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

package org.iplass.mtp.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.iplass.mtp.impl.util.ResourceBundleWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ResourceBundle取得用のユーティリティ。
 */
public class ResourceBundleUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ResourceBundleUtil.class);

	/**
	 * ResourceBundleに定義された文字列を返します。
	 *
	 * @param key キー
	 * @param arguments パラメータ
	 * @return 文字列
	 */
	public static String resourceString(String key, Object... arguments) {

		try {
			return ResourceBundleWrapper.getString(key, arguments);
		} catch (Exception e) {
			logger.error("cant get resource string of key: " + key + ". cause:" + e);
			return "";
		}
	}

	/**
	 * langLocaleで指定されるLocaleに適したResourceBundleに定義された文字列を返します。
	 *
	 * @param langLocale
	 * @param key
	 * @param arguments
	 * @return
	 */
	public static String resourceString(Locale langLocale, String key, Object... arguments) {
		try {
			return ResourceBundleWrapper.getString(langLocale, key, arguments);
		} catch (Exception e) {
			logger.error("cant get resource string of key: " + key + ". cause:" + e);
			return "";
		}
	}

	/**
	 * 現在の実行コンテキストに最適な言語のResourceBundleを取得
	 *
	 * @param baseName ResourceBundleのbaseName
	 * @return
	 */
	public static ResourceBundle getResourceBundle(String baseName) {
		return ResourceBundleWrapper.getResourceBundle(baseName);
	}

	/**
	 * 指定のlangLocaleのResourceBundleを取得。
	 * ResourceBundle.getBundle(baseName, langLocale)を呼び出すのと同等。
	 *
	 * @param baseName ResourceBundleのbaseName
	 * @param langLocale 言語をあらわすLocale
	 * @return
	 */
	public static ResourceBundle getResourceBundle(String baseName, Locale langLocale) {
		return ResourceBundleWrapper.getResourceBundle(baseName, langLocale);
	}

	/**
	 * ResourceBundleに定義された文字列を返します。
	 *
	 * @param resource ResourceBundle
	 * @param key キー
	 * @param arguments パラメータ
	 * @return 文字列
	 */
	public static String resourceString(ResourceBundle resource, String key, Object... arguments) {
		try {
			return ResourceBundleWrapper.getString(resource, key, arguments);
		} catch (Exception e) {
			logger.error("cant get resource string of bundleName: " + resource.getBaseBundleName() + " key: " + key + ". cause:" + e);
			return "";
		}
	}
	
	/**
	 * bundleName, langLocaleで指定されるResourceBundleに定義された文字列を返します。
	 *
	 * @param bundleBaseName
	 * @param langLocale
	 * @param key
	 * @param arguments
	 * @return
	 */
	public static String resourceString(String bundleBaseName, Locale langLocale, String key, Object... arguments) {
		try {
			ResourceBundle bundle = getResourceBundle(bundleBaseName, langLocale);
			return resourceString(bundle, key, arguments);
		} catch (Exception e) {
			logger.error("cant get resource string of bundleName: " + bundleBaseName + " key: " + key + ". cause:" + e);
			return "";
		}
	}
	
}
