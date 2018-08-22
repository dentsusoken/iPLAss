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

package org.iplass.mtp.impl.tools.entityport;

import java.io.Serializable;

/**
 * EntityのExport条件
 */
public class EntityDataExportCondition implements Serializable {

	private static final long serialVersionUID = -4490981451667841415L;

	/** Where条件 */
	private String whereClause;
	/** Order By条件 */
	private String orderByClause;
	/** Versionデータ条件 */
	private boolean isVersioned;

	public EntityDataExportCondition() {
	}

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public boolean isVersioned() {
		return isVersioned;
	}

	public void setVersioned(boolean isVersioned) {
		this.isVersioned = isVersioned;
	}

	@Override
	public String toString() {
		return "EntityDataExportCondition [whereClause=" + whereClause + ", orderByClause=" + orderByClause
				+ ", isVersioned=" + isVersioned + "]";
	}

}
