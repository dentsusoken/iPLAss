/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

public class PartitionCreateParameter {

	//作成するテナントのID
	private int tenantId;

	//サブパーティション利用有無 (MySQL時のみ利用)
	private boolean isMySqlUseSubPartition = true;

	//ログ出力時のLang
	private String loggerLanguage;

	public PartitionCreateParameter() {
	}

	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	public boolean isMySqlUseSubPartition() {
		return isMySqlUseSubPartition;
	}

	public void setMySqlUseSubPartition(boolean isMySqlUseSubPartition) {
		this.isMySqlUseSubPartition = isMySqlUseSubPartition;
	}

	public String getLoggerLanguage() {
		return loggerLanguage;
	}

	public void setLoggerLanguage(String loggerLanguage) {
		this.loggerLanguage = loggerLanguage;
	}

}
