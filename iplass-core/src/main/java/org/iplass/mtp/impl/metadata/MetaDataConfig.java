/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata;

public class MetaDataConfig {
	private boolean sharable;
	private boolean overwritable;
	private boolean dataSharable;
	private boolean permissionSharable;
	
	public MetaDataConfig() {
	}
	
	public MetaDataConfig(boolean sharable, boolean overwritable, boolean dataSharable, boolean permissionSharable) {
		this.sharable = sharable;
		this.overwritable = overwritable;
		this.dataSharable = dataSharable;
		this.permissionSharable = permissionSharable;
	}
	
	public boolean isPermissionSharable() {
		return permissionSharable;
	}

	public void setPermissionSharable(boolean permissionSharable) {
		this.permissionSharable = permissionSharable;
	}

	public boolean isDataSharable() {
		return dataSharable;
	}

	public void setDataSharable(boolean dataSharable) {
		this.dataSharable = dataSharable;
	}

	public boolean isOverwritable() {
		return overwritable;
	}
	
	public void setOverwritable(boolean overwritable) {
		this.overwritable = overwritable;
	}
	
	public boolean isSharable() {
		return sharable;
	}
	
	public void setSharable(boolean sharable) {
		this.sharable = sharable;
	}
}
