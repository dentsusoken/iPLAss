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

package org.iplass.adminconsole.shared.base.dto.tenant;

import java.io.Serializable;

import org.iplass.mtp.tenant.Tenant;

public class TenantEnv implements Serializable {

	private static final long serialVersionUID = -8782823650696971858L;

	/** テナント情報 */
	private Tenant tenant;

	/** テナントLocale */
	private String tenantLocale;

	/** テナントTimeZoneInfo */
	private String tenantTimeZoneInfo;

	/** テナントTimeZoneのオフセット(分) */
	private int tenantTimeZoneOffsetInMinutes;

	/** サーバデフォルトTimeZoneInfo */
	private String serverTimeZoneInfo;

	/** サーバデフォルトTimeZoneのオフセット(分) */
	private int serverTimeZoneOffsetInMinutes;

	/** Adminの入力用ロケール */
	private String inputLocale;

	/** Date出力用書式 */
	private String outputDateFormat;
	/** Time出力用書式 */
	private String outputTimeSecFormat;
	/** DateTime出力用書式 */
	private String outputDateTimeSecFormat;

	/** RdbAdapter名 */
	private String rdbAdapterName;

	public Tenant getTenant() {
		return tenant;
	}
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getTenantLocale() {
		return tenantLocale;
	}
	public void setTenantLocale(String tenantLocale) {
		this.tenantLocale = tenantLocale;
	}

	public String getTenantTimeZoneInfo() {
		return tenantTimeZoneInfo;
	}
	public void setTenantTimeZoneInfo(String tenantTimeZoneInfo) {
		this.tenantTimeZoneInfo = tenantTimeZoneInfo;
	}
	public int getTenantTimeZoneOffsetInMinutes() {
		return tenantTimeZoneOffsetInMinutes;
	}
	public void setTenantTimeZoneOffsetInMinutes(int tenantTimeZoneOffsetInMinutes) {
		this.tenantTimeZoneOffsetInMinutes = tenantTimeZoneOffsetInMinutes;
	}

	public String getServerTimeZoneInfo() {
		return serverTimeZoneInfo;
	}
	public void setServerTimeZoneInfo(String serverTimeZoneInfo) {
		this.serverTimeZoneInfo = serverTimeZoneInfo;
	}
	public int getServerTimeZoneOffsetInMinutes() {
		return serverTimeZoneOffsetInMinutes;
	}
	public void setServerTimeZoneOffsetInMinutes(int serverTimeZoneOffsetInMinutes) {
		this.serverTimeZoneOffsetInMinutes = serverTimeZoneOffsetInMinutes;
	}

	public String getInputLocale() {
		return inputLocale;
	}
	public void setInputLocale(String inputLocale) {
		this.inputLocale = inputLocale;
	}

	public String getOutputDateFormat() {
		return outputDateFormat;
	}
	public void setOutputDateFormat(String outputDateFormat) {
		this.outputDateFormat = outputDateFormat;
	}

	public String getOutputTimeSecFormat() {
		return outputTimeSecFormat;
	}
	public void setOutputTimeSecFormat(String outputTimeSecFormat) {
		this.outputTimeSecFormat = outputTimeSecFormat;
	}

	public String getOutputDateTimeSecFormat() {
		return outputDateTimeSecFormat;
	}
	public void setOutputDateTimeSecFormat(String outputDateTimeSecFormat) {
		this.outputDateTimeSecFormat = outputDateTimeSecFormat;
	}

	public String getRdbAdapterName() {
		return rdbAdapterName;
	}
	public void setRdbAdapterName(String rdbAdapterName) {
		this.rdbAdapterName = rdbAdapterName;
	}
}
