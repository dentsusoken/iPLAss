/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.tenant;

import org.iplass.adminconsole.shared.base.dto.tenant.TenantEnv;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantI18nInfo;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

public class TenantInfoHolder {

	private static TenantEnv tenantEnv;

	private static String language;

	private static DateTimeFormat outputDateFormat;
	private static DateTimeFormat outputTimeFormat;
	private static DateTimeFormat outputDateTimeFormat;
	private static TimeZone tenantTimeZone;
	private static TimeZone serverTimeZone;

	public static void init(TenantEnv env, String lang) {
		tenantEnv = env;
		language = lang;
		outputDateFormat = null;
		outputTimeFormat = null;
		outputDateTimeFormat = null;
		tenantTimeZone = null;
		serverTimeZone = null;
	}

	public static void reload(TenantEnv env) {
		tenantEnv = env;
		outputDateFormat = null;
		outputTimeFormat = null;
		outputDateTimeFormat = null;
		tenantTimeZone = null;
		serverTimeZone = null;
	}

	public static Tenant getTenant() {
		return tenantEnv.getTenant();
	}

	public static int getId() {
		return tenantEnv.getTenant().getId();
	}

	public static String getLanguage() {
		return language;
	}

	public static boolean isUseMultilingual() {
		return tenantEnv.getTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual();
	}

	public static String getInputLocale() {
		return tenantEnv.getInputLocale();
	}

	public static String getOutputDateFormatString() {
		return tenantEnv.getOutputDateFormat();
	}

	public static String getOutputTimeSecFormatString() {
		return tenantEnv.getOutputTimeSecFormat();
	}

	public static String getOutputDateTimeSecFormatString() {
		return tenantEnv.getOutputDateTimeSecFormat();
	}

	public static DateTimeFormat getOutputDateFormat() {
		if (outputDateFormat == null) {
			outputDateFormat = DateTimeFormat.getFormat(getOutputDateFormatString());
		}
		return outputDateFormat;
	}

	public static DateTimeFormat getOutputTimeSecFormat() {
		if (outputTimeFormat == null) {
			outputTimeFormat = DateTimeFormat.getFormat(getOutputTimeSecFormatString());
		}
		return outputTimeFormat;
	}

	public static DateTimeFormat getOutputDateTimeSecFormat() {
		if (outputDateTimeFormat == null) {
			outputDateTimeFormat = DateTimeFormat.getFormat(getOutputDateTimeSecFormatString());
		}
		return outputDateTimeFormat;
	}

	public static TimeZone getTenantTimeZone() {
		if (tenantTimeZone == null) {
			//GWTのTimeZoneを生成
			if (tenantEnv.getTenantTimeZoneInfo() != null) {
				//TimeZone情報が見つかった場合
				tenantTimeZone = TimeZone.createTimeZone(tenantEnv.getTenantTimeZoneInfo());
			} else {
				//TimeZone情報が見つからなかった場合
				tenantTimeZone = TimeZone.createTimeZone(tenantEnv.getTenantTimeZoneOffsetInMinutes());
			}
		}
		return tenantTimeZone;
	}

	public static TimeZone getServerTimeZone() {
		if (serverTimeZone == null) {
			//GWTのTimeZoneを生成
			if (tenantEnv.getServerTimeZoneInfo() != null) {
				//TimeZone情報が見つかった場合
				serverTimeZone = TimeZone.createTimeZone(tenantEnv.getServerTimeZoneInfo());
			} else {
				//TimeZone情報が見つからなかった場合
				serverTimeZone = TimeZone.createTimeZone(tenantEnv.getServerTimeZoneOffsetInMinutes());
			}
		}
		return serverTimeZone;
	}

	public static boolean isMySql() {
		return tenantEnv.isMySql();
	}
}
