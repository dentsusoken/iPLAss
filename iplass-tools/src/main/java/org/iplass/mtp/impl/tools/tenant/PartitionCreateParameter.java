/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.impl.tools.tenant.rdb.TenantRdbConstants;

public class PartitionCreateParameter {

	//作成するテナントのID
	private int tenantId;

	//サブパーティション数
	private int subPartitionSize = TenantRdbConstants.MAX_SUBPARTITION;

	//パーティション作成のみ
	private boolean onlyPartitionCreate;

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

	public int getSubPartitionSize() {
		return subPartitionSize;
	}

	public void setSubPartitionSize(int subPartitionSize) {
		this.subPartitionSize = subPartitionSize;
	}

	public boolean isOnlyPartitionCreate() {
		return onlyPartitionCreate;
	}

	public void setOnlyPartitionCreate(boolean onlyPartitionCreate) {
		this.onlyPartitionCreate = onlyPartitionCreate;
	}

	public String getLoggerLanguage() {
		return loggerLanguage;
	}

	public void setLoggerLanguage(String loggerLanguage) {
		this.loggerLanguage = loggerLanguage;
	}

}
