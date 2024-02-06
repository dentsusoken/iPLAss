/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.query.value.aggregate;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.PrimaryValue;

/**
 * 集計関数を表す抽象クラス。
 * 
 * @author K.Higuchi
 *
 */
public abstract class Aggregate extends PrimaryValue {
	private static final long serialVersionUID = -203649942259922206L;

	private ValueExpression value;

	public ValueExpression getValue() {
		return value;
	}

	public void setValue(ValueExpression value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return getFuncName() + "(" + getValue() + ")";
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getFuncName() == null) ? 0 : getFuncName().hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aggregate other = (Aggregate) obj;
		if (getFuncName() == null) {
			if (other.getFuncName() != null)
				return false;
		} else if (!getFuncName().equals(other.getFuncName()))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	protected abstract String getFuncName();


}
