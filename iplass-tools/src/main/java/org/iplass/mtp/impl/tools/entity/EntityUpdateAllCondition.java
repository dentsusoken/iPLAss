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

package org.iplass.mtp.impl.tools.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntityUpdateAllCondition implements Serializable {

	/** シリアルバージョンNo */
	private static final long serialVersionUID = 6123091297872728806L;

	private String definitionName;

	private List<UpdateAllValue> values;

	private String whereClause;

	private boolean checkUpdatable;

	/**
	 * コンストラクタ
	 */
	public EntityUpdateAllCondition() {
		values = new ArrayList<UpdateAllValue>();
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setWhere(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getWhere() {
		return whereClause;
	}

	public void addValue(UpdateAllValue value) {
		values.add(value);
	}

	public void addValues(List<UpdateAllValue> values) {
		this.values.addAll(values);
	}

	public List<UpdateAllValue> getValues() {
		return values;
	}

	public boolean isCheckUpdatable() {
		return checkUpdatable;
	}

	public void setCheckUpdatable(boolean checkUpdatable) {
		this.checkUpdatable = checkUpdatable;
	}

}
