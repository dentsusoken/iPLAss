/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.tenant.MetaTenantI18nInfo.MetaTenantI18nInfoRuntime;
import org.iplass.mtp.impl.util.UTF8ResourceBundleControl;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;

public class ResourceBundleConfig implements ServiceInitListener<MessageService> {
	private String baseBundleNamePattern;
	private List<String> formats;
	private boolean fallbackToSystemLocale;

	private Pattern bbnPtn;
	private ResourceBundle.Control control;

	public String getBaseBundleNamePattern() {
		return baseBundleNamePattern;
	}
	public void setBaseBundleNamePattern(String baseBundleNamePattern) {
		this.baseBundleNamePattern = baseBundleNamePattern;
	}
	public List<String> getFormats() {
		return formats;
	}
	public void setFormats(List<String> formats) {
		this.formats = formats;
	}
	public boolean isFallbackToSystemLocale() {
		return fallbackToSystemLocale;
	}
	public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
		this.fallbackToSystemLocale = fallbackToSystemLocale;
	}

	@Override
	public void inited(MessageService service, Config config) {
		if (baseBundleNamePattern != null) {
			bbnPtn = Pattern.compile(baseBundleNamePattern);
		}

		if (formats == null) {
			formats = new ArrayList<>();
			formats.add(MessageResourceBundleControl.MESSAGE_FORMAT_TYPE);
			formats.addAll(ResourceBundle.Control.FORMAT_DEFAULT);
			formats = Collections.unmodifiableList(formats);
		}

		if (formats.contains(MessageResourceBundleControl.MESSAGE_FORMAT_TYPE)) {
			control = new MessageResourceBundleControl(fallbackToSystemLocale, formats);
		} else {
			control = new UTF8ResourceBundleControl() {
				@Override
				public List<String> getFormats(String baseName) {
					return formats;
				}
				@Override
				public Locale getFallbackLocale(String baseName, Locale locale) {
					Locale l = ExecuteContext.getCurrentContext().getTenantContext().getTenantRuntime()
							.getConfigRuntime(MetaTenantI18nInfoRuntime.class).getLangLocale();
					if (l != null && !locale.equals(l)) {
						return l;
					}
					if(fallbackToSystemLocale) {
						return super.getFallbackLocale(baseName, locale);
					} else {
						return null;
					}
				}
			};
		}
	}

	@Override
	public void destroyed() {
	}

	public ResourceBundle.Control getResourceBundleControl() {
		return control;
	}

	public boolean isMatch(String baseBundleName) {
		if (bbnPtn == null) {
			return true;
		} else {
			return bbnPtn.matcher(baseBundleName).matches();
		}
	}

}
