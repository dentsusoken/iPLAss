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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.message.MessageService;
import org.iplass.mtp.spi.ServiceRegistry;

public class ResourceBundleWrapper {

	private static final String RESOURCE_FILE_NAME = "Application";
	private static MessageService ms = ServiceRegistry.getRegistry().getService(MessageService.class);
	private static UTF8ResourceBundleControl utf8ctrl = new UTF8ResourceBundleControl();

	public static String getString(String key) {
		return getString(key, (Object[]) null);
	}

	/**
	 * 現在の実行コンテキストで最適な言語のStringを返却
	 *
	 * @param key
	 * @param arguments
	 * @return
	 */
	public static String getString(String key, Object... arguments) {
		return getString(ExecuteContext.getCurrentContext().getLangLocale(), key, arguments);
	}

	/**
	 * 指定のLocaleで表現される言語のStringを返却
	 *
	 * @param langLocale
	 * @param key
	 * @param arguments
	 * @return
	 */
	public static String getString(Locale langLocale, String key, Object... arguments) {
		ResourceBundle resource = getResourceBundle(RESOURCE_FILE_NAME, langLocale);
		String resourceString = resource.getString(key);

		if (resourceString != null && arguments != null && arguments.length > 0) {
			return MessageFormat.format(resourceString, arguments);
		}

		return resourceString;
	}

	public static String getString(ResourceBundle resource, String key, Object... arguments) {
		String resourceString = resource.getString(key);

		if (resourceString != null && arguments != null && arguments.length > 0) {
			return MessageFormat.format(resourceString, arguments);
		}

		return resourceString;
	}

	public static ResourceBundle getResourceBundle(String baseName) {
		return getResourceBundle(baseName, ExecuteContext.getCurrentContext().getLangLocale());
	}

	public static ResourceBundle getResourceBundle(String baseName, Locale langLocale) {
		ResourceBundle.Control ctrl = ms.getResourceBundleControl(baseName);
		if (ctrl == null) {
			return ResourceBundle.getBundle(baseName, langLocale, utf8ctrl);
		} else {
			return ResourceBundle.getBundle(baseName, langLocale, ctrl);
		}
	}

	/**
	 * 固定のResourceBundle以外のlocalStringを取得するためのユーティリティメソッド。
	 *
	 * @param baseName ResourceBundleのbaseName
	 * @param key
	 * @param args
	 * @return
	 */
	public static String localString(String baseName, String key, Object... args) {
		//メソッド名getStringだと引数が、
		//(String key, Object... arguments)
		//と
		//(String baseName, String key, Object... arguments)
		//となり、可変引数の問題で区別しづらいので、、、、

		ResourceBundle localStrings = getResourceBundle(baseName);
		if (localStrings == null) {
			return null;
		}
		return getString(localStrings, key, args);
	}
}
