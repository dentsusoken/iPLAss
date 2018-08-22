/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.i18n;

import java.util.Locale;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.util.ResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

public class AdminResourceBundleUtil {

	/** AdminConsole用リソースファイル名 */
	public static final String RESOURCE_NAME = "adminconsole-messages";

	private AdminResourceBundleUtil(){}

	public static String resourceString(String key, Object... arguments) {
		return resourceString(null, key, arguments);
	}

	public static String resourceString(String lang, String key, Object... arguments) {

		Locale langLocale = null;
		if (StringUtil.isNotEmpty(lang)) {
			langLocale = Locale.forLanguageTag(lang);
		} else {
			langLocale = ExecuteContext.getCurrentContext().getLangLocale();
		}

		return ResourceBundleUtil.resourceString(RESOURCE_NAME, langLocale, key, arguments);
	}

}
