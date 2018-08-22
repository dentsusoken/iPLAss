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

package org.iplass.mtp.entity.query.value.window;


/**
 * Windowランク関数を表す抽象クラス。
 * 
 * @author K.Higuchi
 *
 */
public abstract class WindowRankFunction extends WindowFunction {
	private static final long serialVersionUID = 2769025316332782925L;

	@Override
	protected void writeWindowFunctionType(StringBuilder sb) {
		sb.append(getFuncName()).append("()");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getFuncName() == null) ? 0 : getFuncName().hashCode());
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
		WindowRankFunction other = (WindowRankFunction) obj;
		if (getFuncName() == null) {
			if (other.getFuncName() != null)
				return false;
		} else if (!getFuncName().equals(other.getFuncName()))
			return false;
		return true;
	}

	protected abstract String getFuncName();
}
