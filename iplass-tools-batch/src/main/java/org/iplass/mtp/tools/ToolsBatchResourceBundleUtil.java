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

package org.iplass.mtp.tools;

import java.util.Locale;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.util.ResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

public class ToolsBatchResourceBundleUtil {

	/** Tool用リソースファイル名 */
	public static final String RESOURCE_NAME = "mtp-tools-batch-messages";

	/** リソースファイルの接頭語(Common) */
	private static final String RES_COMMON_PRE = "Common.";

	private ToolsBatchResourceBundleUtil(){}

	public static String resourceString(String lang, String key, Object... arguments) {

		Locale langLocale = null;
		if (StringUtil.isNotEmpty(lang)) {
			langLocale = Locale.forLanguageTag(lang);
		} else {
			langLocale = ExecuteContext.getCurrentContext().getLangLocale();
		}

		return ResourceBundleUtil.resourceString(RESOURCE_NAME, langLocale, key, arguments);
	}

	public static String commonResourceString(String lang, String subKey, Object... arguments) {
		return resourceString(lang, RES_COMMON_PRE + subKey, arguments);
	}

}
