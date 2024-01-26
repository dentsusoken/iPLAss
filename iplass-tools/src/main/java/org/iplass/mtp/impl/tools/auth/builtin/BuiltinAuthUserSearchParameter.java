/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.auth.builtin;

import java.io.Serializable;

import org.iplass.mtp.impl.tools.auth.builtin.cond.BuiltinAuthUserSearchCondition;

public class BuiltinAuthUserSearchParameter implements Serializable {

	private static final long serialVersionUID = 2421949247398885224L;

	private BuiltinAuthUserSearchCondition condition;

	private int limit;

	private int offset;

	public BuiltinAuthUserSearchCondition getCondition() {
		return condition;
	}

	public void setCondition(BuiltinAuthUserSearchCondition condition) {
		this.condition = condition;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
