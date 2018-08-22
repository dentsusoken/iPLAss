/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.iplass.mtp.async.ExceptionHandlingMode;

/**
 * SelectItemなどに設定するValueMap形式のデータソース用Util。
 */
public class ValueMapUtil {

	private static final ValueMapUtil instance = new ValueMapUtil();

	private ValueMapUtil() {
	}

	private Map<String, LinkedHashMap<String, String>> chacheBlank = new HashMap<String, LinkedHashMap<String, String>>();
	private Map<String, LinkedHashMap<String, String>> chache = new HashMap<String, LinkedHashMap<String, String>>();

	public static LinkedHashMap<String, String> getExceptionHandlingModeValueMap(boolean hasBlank) {

		if (hasBlank && instance.chacheBlank.containsKey("exceptionHandlingMode")) {
			return instance.chacheBlank.get("exceptionHandlingMode");
		}
		if (!hasBlank && instance.chache.containsKey("exceptionHandlingMode")) {
			return instance.chache.get("exceptionHandlingMode");
		}

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (hasBlank) {
			instance.chacheBlank.put("exceptionHandlingMode", map);
			map.put("", "");
		} else {
			instance.chache.put("exceptionHandlingMode", map);
		}

		map.put(ExceptionHandlingMode.RESTART.name(), "Restart");
		map.put(ExceptionHandlingMode.ABORT.name(), "Abort");
		map.put(ExceptionHandlingMode.ABORT_LOG_FATAL.name(), "Abort Log Fatal");
		return map;
	}

}
