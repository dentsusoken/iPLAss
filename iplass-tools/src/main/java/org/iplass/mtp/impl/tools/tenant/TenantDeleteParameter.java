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

package org.iplass.mtp.impl.tools.tenant;

/**
 * テナント削除用パラメータ情報
 *
 */
public class TenantDeleteParameter {

	// テナントID
	private int tenantId;
	// テナント名
	private String tenantName;
	//パーティション削除 (MySQL時のみ利用)
	private boolean isMySqlDropPartition = true;

	//ログ出力時のLang
	private String loggerLanguage;

	public TenantDeleteParameter() {
	}

	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public boolean isMySqlDropPartition() {
		return isMySqlDropPartition;
	}

	public void setMySqlDropPartition(boolean isMySqlDropPartition) {
		this.isMySqlDropPartition = isMySqlDropPartition;
	}

	public String getLoggerLanguage() {
		return loggerLanguage;
	}

	public void setLoggerLanguage(String loggerLanguage) {
		this.loggerLanguage = loggerLanguage;
	}

}
