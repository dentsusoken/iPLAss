/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

/**
 * 順序指定がある集計関数を表す抽象クラス。
 * 
 * @author K.Higuchi
 *
 */
public abstract class OrderedSetFunction extends Aggregate {
	private static final long serialVersionUID = -4737895157161789974L;

	private WithinGroup withinGroup;

	public WithinGroup getWithinGroup() {
		return withinGroup;
	}

	public void setWithinGroup(WithinGroup withinGroup) {
		this.withinGroup = withinGroup;
	}

	@Override
	public String toString() {
		if (withinGroup == null) {
			return super.toString();
		} else {
			return getFuncName() + "(" + getValue() + ") " + withinGroup.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((withinGroup == null) ? 0 : withinGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderedSetFunction other = (OrderedSetFunction) obj;
		if (withinGroup == null) {
			if (other.withinGroup != null)
				return false;
		} else if (!withinGroup.equals(other.withinGroup))
			return false;
		return true;
	}

}
