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

package org.iplass.mtp.tenant;

import java.util.ArrayList;
import java.util.List;

public class TenantI18nInfo extends TenantConfig {

	private static final long serialVersionUID = 5545610349508861218L;

	/** 多言語利用するか */
	private boolean useMultilingual;

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

	public TenantI18nInfo() {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (useMultilingual ? 1231 : 1237);
		result = prime * result
				+ ((useLanguageList == null) ? 0 : useLanguageList.hashCode());
		result = prime * result
				+ ((locale == null) ? 0 : locale.hashCode());
		result = prime * result
				+ ((timezone == null) ? 0 : timezone.hashCode());
		result = prime * result
				+ ((outputDateFormat == null) ? 0 : outputDateFormat.hashCode());
		result = prime * result
				+ ((browserInputDateFormat == null) ? 0 : browserInputDateFormat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		TenantI18nInfo other = (TenantI18nInfo) obj;

		if (useLanguageList != other.useLanguageList)
			return false;

		if (useLanguageList == null) {
			if (other.useLanguageList != null)
				return false;
		} else if (!useLanguageList.equals(other.useLanguageList))
			return false;

		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;

		if (timezone == null) {
			if (other.timezone != null)
				return false;
		} else if (!timezone.equals(other.timezone))
			return false;

		if (outputDateFormat == null) {
			if (other.outputDateFormat != null)
				return false;
		} else if (!outputDateFormat.equals(other.outputDateFormat))
			return false;

		if (browserInputDateFormat == null) {
			if (other.browserInputDateFormat != null)
				return false;
		} else if (!browserInputDateFormat.equals(other.browserInputDateFormat))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "TenantI18nInfo ["
				+ "useMultilingual=" + useMultilingual
				+ ", useLanguageList=" + useLanguageList
				+ ", locale=" + locale
				+ ", timezone=" + timezone
				+ ", outputDateFormat=" + outputDateFormat
				+ ", browserInputDateFormat=" + browserInputDateFormat
				+ "]";
	}

}
