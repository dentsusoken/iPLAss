/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantI18nInfo;

public class MetaTenantI18nInfo extends MetaTenantConfig<TenantI18nInfo> {

	private static final long serialVersionUID = -7077921989197723776L;

	/** 多言語利用するか */
	private boolean useMultilingual = false;
	/** テナントで利用する言語リスト */
	private List<String> useLanguageList = new ArrayList<String>();

	/** ロケール */
	private String locale;
	/** タイムゾーン */
	private String timezone;
	/** Dateの出力用Format */
	private String outputDateFormat;
	/** Dateのブラウザ入力用Format */
	private String browserInputDateFormat;

	/**
	 * Constractor
	 */
	public MetaTenantI18nInfo() {
	}

	public boolean isUseMultilingual() {
		return useMultilingual;
	}

	public void setUseMultilingual(boolean useMultilingual) {
		this.useMultilingual = useMultilingual;
	}

	public List<String> getUseLanguageList() {
		return useLanguageList;
	}

	public void setUseLanguageList(List<String> useLanguageList) {
		this.useLanguageList = useLanguageList;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getOutputDateFormat() {
		return outputDateFormat;
	}

	public void setOutputDateFormat(String outputDateFormat) {
		this.outputDateFormat = outputDateFormat;
	}

	public String getBrowserInputDateFormat() {
		return browserInputDateFormat;
	}

	public void setBrowserInputDateFormat(String browserInputDateFormat) {
		this.browserInputDateFormat = browserInputDateFormat;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TenantI18nInfo definition) {
		setUseMultilingual(definition.isUseMultilingual());
		setUseLanguageList(definition.getUseLanguageList());
		setLocale(definition.getLocale());
		setTimezone(definition.getTimezone());
		setOutputDateFormat(definition.getOutputDateFormat());
		setBrowserInputDateFormat(definition.getBrowserInputDateFormat());
	}

	@Override
	public TenantI18nInfo currentConfig() {
		TenantI18nInfo definition = new TenantI18nInfo();
		definition.setLocale(locale);
		definition.setTimezone(timezone);
		definition.setOutputDateFormat(outputDateFormat);
		definition.setBrowserInputDateFormat(browserInputDateFormat);
		definition.setUseMultilingual(useMultilingual);
		definition.setUseLanguageList(useLanguageList);
		return definition;
	}

	@Override
	public MetaTenantI18nInfoRuntime createRuntime(MetaTenantHandler tenantRuntime) {
		return new MetaTenantI18nInfoRuntime();
	}

	public class MetaTenantI18nInfoRuntime extends MetaTenantConfigRuntime {

		private Locale localeRuntime;
		private TimeZone timeZoneRuntime;
		private Locale langLocaleRuntime;

		public MetaTenantI18nInfoRuntime() {

			try {
				I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);
				if (locale != null) {
					localeRuntime = I18nUtil.getLocale(locale);
				} else {
					//vm default
					localeRuntime = i18n.getLocale();
				}

				//lang用Localeの設定（存在するlangセットから選択。ない場合はen）
				langLocaleRuntime = i18n.selectLangLocale(localeRuntime);

				if (timezone != null) {
					timeZoneRuntime = TimeZone.getTimeZone(timezone);
				} else {
					//vm default
					timeZoneRuntime = i18n.getTimezone();
				}

			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}

		@Override
		public MetaData getMetaData() {
			return MetaTenantI18nInfo.this;
		}

		@Override
		public void applyMetaDataToTenant(Tenant tenant) {
		}

		public Locale getLocale() {
			return localeRuntime;
		}

		public Locale getLangLocale() {
			return langLocaleRuntime;
		}

		public TimeZone getTimeZone() {
			return timeZoneRuntime;
		}
	}

}
