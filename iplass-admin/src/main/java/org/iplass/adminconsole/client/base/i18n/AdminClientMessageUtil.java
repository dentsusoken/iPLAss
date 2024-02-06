/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.i18n;

import com.google.gwt.i18n.client.Dictionary;

public class AdminClientMessageUtil {

	private static AdminClientMessageUtil instance = new AdminClientMessageUtil();

	private Dictionary localeInfo;

	private AdminClientMessageUtil() {
		localeInfo = Dictionary.getDictionary("LocaleInfo");
	}

	/**
	 *
	 * 指定されたキーのメッセージを返却します。
	 *
	 * @param key キー
	 * @param arguments 引数
	 * @return メッセージ
	 */
	public static String getString(String key, Object... arguments) {

		try {
			String message = instance.localeInfo.get(key);
			return MessageFormat.format(message, arguments);
		} catch (Throwable t) {
			return "";
		}
	}

	private static class MessageFormat {

		public static String format(String pattern, Object... arguments) {
			return doFormat(pattern, arguments);
		}

		private static String doFormat(String pattern, Object[] arguments) {
			// A very simple implementation of format
			if (pattern != null && arguments != null && arguments.length > 0) {

				int i = 0;
				while (i < arguments.length) {
					String delimiter = "{" + i + "}";
					while (pattern.contains(delimiter)) {
						pattern = pattern.replace(delimiter, String.valueOf(arguments[i]));
					}
					i++;
				}
			}
			return pattern;
		}

	}

}
