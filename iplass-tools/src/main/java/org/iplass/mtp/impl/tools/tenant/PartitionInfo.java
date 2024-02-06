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

import java.io.Serializable;
import java.util.Set;

public class PartitionInfo implements Serializable {

	private static final long serialVersionUID = 5858708032465988275L;

	private String tableName;
	private String postfix;
	private Integer maxTenantId;
	private Set<String> partitionNames;

	public PartitionInfo(final String tableName, final Set<String> partitionNames, final Integer maxTenantId) {
		this.tableName = tableName;
		this.postfix = null;
		this.partitionNames = partitionNames;
		this.maxTenantId = maxTenantId;
	}

	public PartitionInfo(final String tableName, final String postfix, final Set<String> partitionNames, final Integer maxTenantId) {
		this.tableName = tableName;
		this.postfix = postfix;
		this.partitionNames = partitionNames;
		this.maxTenantId = maxTenantId;
	}

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPostfix() {
		return postfix;
	}
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	public Integer getMaxTenantId() {
		return maxTenantId;
	}
	public void setMaxTenantId(Integer maxTenantId) {
		this.maxTenantId = maxTenantId;
	}

	public Set<String> getPartitionNames() {
		return partitionNames;
	}

	public void setPartitionNames(Set<String> partitionNames) {
		this.partitionNames = partitionNames;
	}

	public boolean exists(int tenantId) {
		return partitionNames != null ? partitionNames.contains(tableName + "_" + tenantId) : false;
	}

}
