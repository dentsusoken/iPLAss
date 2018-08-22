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
package org.iplass.mtp.impl.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.tenant.MetaTenantI18nInfo.MetaTenantI18nInfoRuntime;
import org.iplass.mtp.impl.util.UTF8ResourceBundleControl;

public class MessageResourceBundleControl extends UTF8ResourceBundleControl {
	public static final String MESSAGE_FORMAT_TYPE = "mtp.message";

	private final boolean isFallbackToSystemLocale;
	private final List<String> formats;
	private final List<String> fallbackFormats;

	public MessageResourceBundleControl(boolean isFallbackToSystemLocale, List<String> formats) {
		this.isFallbackToSystemLocale = isFallbackToSystemLocale;
		this.formats = formats;
		fallbackFormats = new ArrayList<>(formats.size());
		boolean afterMsg = false;
		for (String f: formats) {
			if (f.equals(MESSAGE_FORMAT_TYPE)) {
				afterMsg = true;
			} else if (afterMsg) {
				fallbackFormats.add(f);
			}
		}
	}

	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {
		if (format.equals("mtp.message")) {
			ResourceBundle wrapped = null;
			if (fallbackFormats != null) {
				for (String f: fallbackFormats) {
					wrapped = super.newBundle(baseName, locale, f, loader, reload);
					if (wrapped != null) {
						break;
					}
				}
			}
			return new MessageResourceBundle(baseName, locale, wrapped);
		} else {
			return super.newBundle(baseName, locale, format, loader, reload);
		}
	}

	@Override
	public List<String> getFormats(String baseName) {
		return formats;
	}

	@Override
	public Locale getFallbackLocale(String baseName, Locale locale) {
//		Locale l = ExecuteContext.getCurrentContext().getTenantContext().getTenantRuntime().getLangLocale();
		Locale l = ExecuteContext.getCurrentContext().getTenantContext().getTenantRuntime()
				.getConfigRuntime(MetaTenantI18nInfoRuntime.class).getLangLocale();
		if (l != null && !locale.equals(l)) {
			return l;
		}

		if (isFallbackToSystemLocale) {
			super.getFallbackLocale(baseName, locale);
		}

		return null;
	}
}
