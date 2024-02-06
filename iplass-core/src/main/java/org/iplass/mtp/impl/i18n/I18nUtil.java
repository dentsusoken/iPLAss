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

package org.iplass.mtp.impl.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.util.StringUtil;

public final class I18nUtil {

	public static String stringDef(String defaultString, List<LocalizedStringDefinition> localizedStringList) {
		String multilingualString = defaultString;
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		if (ec.getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()) {
			String lang = ec.getLanguage();
			if (lang != null && localizedStringList != null) {
				for (LocalizedStringDefinition lsd: localizedStringList) {
					if (lang.equals(lsd.getLocaleName())) {
						multilingualString = lsd.getStringValue();
						break;
					}
				}
			}
		}
		return multilingualString;
	}

	public static String stringMeta(String defaultString, List<MetaLocalizedString> localizedStringList) {
		String multilingualString = defaultString;
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		if (ec.getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()) {
			String lang = ec.getLanguage();
			if (lang != null && localizedStringList != null) {
				for (MetaLocalizedString mls: localizedStringList) {
					if (lang.equals(mls.getLocaleName())) {
						multilingualString = mls.getStringValue();
						break;
					}
				}
			}
		}
		return multilingualString;
	}

	/**
	 * 文字列表現のロケールから{@link Locale} インスタンスを生成します。
	 *
	 * @param localeStr ロケール文字列
	 * @return {@link Locale}
	 */
	public static Locale getLocale(String localeStr) {
		String[] tmp = localeStr.split("_");

		Locale locale = null;
		if (tmp.length == 1) {
			locale = new Locale(tmp[0]);
		} else 	if (tmp.length == 2) {
			locale = new Locale(tmp[0], tmp[1]);
		} else 	if (tmp.length == 3) {
			locale = new Locale(tmp[0], tmp[1], tmp[2]);
		}

		return locale;
	}

	/**
	 * ロケールの{@link Locale} 文字列表現を返します。
	 *
	 * @param locale ロケール
	 * @return ロケール文字列表現
	 */
	public static String getLocaleString(Locale locale) {

		String localeName = locale.getLanguage();
		if (StringUtil.isNotEmpty(locale.getCountry())) {
			localeName += "_" + locale.getCountry();

			if (StringUtil.isNotEmpty(locale.getVariant())) {
				localeName += "_" + locale.getVariant();
			}
		}

		return localeName;
	}

	public static List<MetaLocalizedString> toMeta(List<LocalizedStringDefinition> def) {
		if (def == null) {
			return null;
		}

		List<MetaLocalizedString> meta = new ArrayList<>();
		for (LocalizedStringDefinition ed: def) {

			MetaLocalizedString mls = new MetaLocalizedString();
			mls.setLocaleName(ed.getLocaleName());
			mls.setStringValue(ed.getStringValue());

			meta.add(mls);
		}
		return meta;
	}

	public static List<LocalizedStringDefinition> toDef(List<MetaLocalizedString> meta) {
		if (meta == null) {
			return null;
		}

		List<LocalizedStringDefinition> def = new ArrayList<>();
		for (MetaLocalizedString m: meta) {
			def.add(m.currentConfig());
		}
		return def;
	}
	
	public static String getLanguageIfUseMultilingual() {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		if (ec.getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()) {
			return ec.getLanguage();
		}
		return null;
	}
}
