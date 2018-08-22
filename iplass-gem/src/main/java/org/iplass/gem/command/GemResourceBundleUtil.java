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

package org.iplass.gem.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.util.ResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.TemplateUtil;

public class GemResourceBundleUtil {

	/** Gem用リソースファイル名 */
	public static final String RESOURCE_NAME = "mtp-gem-messages";

	private GemResourceBundleUtil(){}

	public static List<LocalizedStringDefinition> resourceList(String key, Object... arguments) {
		if (TemplateUtil.getEnableLanguages() != null) {
			List<LocalizedStringDefinition> list = new ArrayList<LocalizedStringDefinition>();
			for (String lang : TemplateUtil.getEnableLanguages().keySet()) {
				String msg = resourceString(lang, key, arguments);
				if (StringUtil.isNotEmpty(msg)) {
					LocalizedStringDefinition trueDef = new LocalizedStringDefinition();
					trueDef.setLocaleName(lang);
					trueDef.setStringValue(msg);
					list.add(trueDef);
				}
			}
			return list;
		}

		return null;
	}

	public static String resourceString(String key, Object... arguments) {
		return resourceString(null, key, arguments);
	}

	private static String resourceString(String lang, String key, Object... arguments) {
		Locale langLocale = null;
		if (StringUtil.isNotEmpty(lang)) {
			langLocale = Locale.forLanguageTag(lang);
		} else {
			langLocale = ExecuteContext.getCurrentContext().getLangLocale();
		}

		return ResourceBundleUtil.resourceString(RESOURCE_NAME, langLocale, key, arguments);
	}
}
