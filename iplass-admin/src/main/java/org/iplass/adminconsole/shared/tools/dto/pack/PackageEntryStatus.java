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

package org.iplass.adminconsole.shared.tools.dto.pack;

import org.iplass.mtp.entity.SelectValue;

public enum PackageEntryStatus {

	READY("00","Ready"),
	ACTIVE("10","Active"),
	COMPLETED("20","Completed"),
	ERROR("90","Error");

	private String value;
	private String displayName;

	private PackageEntryStatus(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public String displayName() {
		return displayName;
	}

	public String getStyleName() {
		if (READY.value.equals(value)) {
			return "";
		}
		//Statusが増えた場合はmtpadmin.cssに追加が必要
		return "pack" + displayName() + "GridRow";

	}

	public static PackageEntryStatus valueOf(SelectValue value) {
		for (PackageEntryStatus status : values()) {
			if (status.value.equals(value.getValue())) {
				return status;
			}
		}
		return null;
	}
}
