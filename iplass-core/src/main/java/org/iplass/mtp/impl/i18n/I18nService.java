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

package org.iplass.mtp.impl.i18n;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.util.StringUtil;

public class I18nService implements Service {

	/** 利用可能言語List */
	private List<EnableLanguages> enableLanguages;
	/** 利用可能言語Map */
	private Map<String, String> enableLanguagesMap;

	/** EnableLanguageに定義されるメイン言語以外の言語名のlowerCaseと、メイン言語へのマップ（IETF言語タグは大文字小文字区別しないので） */
	private Map<String, String> otherLanguagesMap;

	/** locale */
	private Locale locale;

	/** Timazone */
	private TimeZone timezone;

	/** Locale Format */
	private List<LocaleFormat> localeFormat;
	/** Locale Format Map */
	private Map<String, LocaleFormat> localeFormatMap;
	/** defaultLocaleFormat */
	private LocaleFormat defaultLocaleFormat;

	private List<LanguageFonts> languageFonts;

	public Locale selectLangLocale(Locale tenantLocale) {
		String langTag = tenantLocale.toLanguageTag();
		langTag = toValidLanguageTag(langTag);
		if (langTag == null) {
			return Locale.ENGLISH;
		} else {
			return Locale.forLanguageTag(langTag);
		}
	}

	public String toValidLanguageTag(String lang) {
		String lowerLang = lang.toLowerCase();
		String ret = otherLanguagesMap.get(lowerLang);
		while (ret == null) {
			int i = lowerLang.indexOf('-');
			if (i <= 0) {
				return null;
			}
			lowerLang = lowerLang.substring(0, i);
			ret = otherLanguagesMap.get(lowerLang);
		}
		return ret;
	}

	public Map<String, String> getEnableLanguagesMap() {
		return enableLanguagesMap;
	}
	public void setEnableLanguagesMap(Map<String, String> enableLanguagesMap) {
		this.enableLanguagesMap = enableLanguagesMap;
	}
	public List<EnableLanguages> getEnableLanguages() {
		return enableLanguages;
	}
	public void setEnableLanguages(List<EnableLanguages> enableLanguages) {
		this.enableLanguages = enableLanguages;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public TimeZone getTimezone() {
		return timezone;
	}
	public void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
	}
	public List<LocaleFormat> getLocaleFormat() {
		return localeFormat;
	}
	public void setLocaleFormat(List<LocaleFormat> localeFormat) {
		this.localeFormat = localeFormat;
	}
	public Map<String, LocaleFormat> getLocaleFormatMap() {
		return localeFormatMap;
	}
	public void setLocaleFormatMap(Map<String, LocaleFormat> localeFormatMap) {
		this.localeFormatMap = localeFormatMap;
	}
	public LocaleFormat getDefaultLocaleFormat() {
		return defaultLocaleFormat;
	}
	public void setDefaultLocaleFormat(LocaleFormat defaultLocaleFormat) {
		this.defaultLocaleFormat = defaultLocaleFormat;
	}
	public List<LanguageFonts> getLanguageFonts() {
		return languageFonts;
	}
	public void setLanguageFonts(List<LanguageFonts> languageFonts) {
		this.languageFonts = languageFonts;
	}

	public LocaleFormat getLocaleFormat(String locale) {
		if (localeFormatMap.get(locale) == null) {
			return getDefaultLocaleFormat();
		} else {
			return localeFormatMap.get(locale);
		}
	}

	public LocaleFormat getLocaleFormat(String locale, Tenant tenant) {
		LocaleFormat format = getLocaleFormat(locale);
		if (tenant == null) {
			return format;
		}
		TenantI18nInfo i18n = tenant.getTenantConfig(TenantI18nInfo.class);
		if (StringUtil.isNotEmpty(i18n.getOutputDateFormat())
				|| StringUtil.isNotEmpty(i18n.getBrowserInputDateFormat())) {

			LocaleFormat customFormat = format.clone();
			if (StringUtil.isNotEmpty(i18n.getOutputDateFormat())) {
				customFormat.setOutputDateFormat(i18n.getOutputDateFormat());
			}
			if (StringUtil.isNotEmpty(i18n.getBrowserInputDateFormat())) {
				customFormat.setBrowserInputDateFormat(i18n.getBrowserInputDateFormat());
			}
			return customFormat;
		}
		return format;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		if (config.getBeans("enableLanguages") != null) {
			enableLanguages = (List<EnableLanguages>) config.getBeans("enableLanguages");
			enableLanguagesMap = new LinkedHashMap<String, String>();
			otherLanguagesMap = new HashMap<String, String>();
			for (EnableLanguages e : enableLanguages) {
				enableLanguagesMap.put(e.getLanguageKey(), e.getLanguageName());
				otherLanguagesMap.put(e.getLanguageKey().toLowerCase(), e.getLanguageKey());
				if (e.getOtherLanguageKey() != null) {
					for (String ol: e.getOtherLanguageKey()) {
						otherLanguagesMap.put(ol.toLowerCase(), e.getLanguageKey());
					}
				}
			}
		}

		locale = Locale.getDefault();
		timezone = TimeZone.getDefault();

		defaultLocaleFormat = (LocaleFormat) config.getBean("defaultLocaleFormat");

		if (config.getBeans("localeFormat") != null) {
			localeFormat = (List<LocaleFormat>) config.getBeans("localeFormat");
			localeFormatMap = new LinkedHashMap<String, LocaleFormat>();
			for (LocaleFormat lf : localeFormat) {
				List<String> localeList = lf.getLocale();
				for (String l: localeList) {
					localeFormatMap.put(l, lf);
				}
			}
		}

		languageFonts = (List<LanguageFonts>) config.getBeans("languageFonts");
	}

	@Override
	public void destroy() {
	}

}
