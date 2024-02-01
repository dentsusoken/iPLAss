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

package org.iplass.mtp.entity.query;

import org.iplass.mtp.entity.query.value.primary.Literal;

/**
 * LIMIT句を表す。
 * 
 * @author K.Higuchi
 *
 */
public class Limit implements ASTNode {
	private static final long serialVersionUID = 2893574448831149966L;

	//LimitはメインQueryのみ指定可能とする。

	public static final int UNSPECIFIED = -1;
	
	private int limit = UNSPECIFIED;
	private int offset = UNSPECIFIED;
	private boolean bindable = true;
	
	public Limit() {
	}
	
	public Limit(int limit) {
		this.limit = limit;
	}
	
	public Limit(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
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

	public boolean isBindable() {
		return bindable;
	}

	public void setBindable(boolean bindable) {
		this.bindable = bindable;
	}
	
	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
	
	public void accept(QueryVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("limit ");
		if (!bindable) {
			sb.append(Literal.NO_BIND_HINT);
		}
		sb.append(limit);
		if (offset != UNSPECIFIED) {
			sb.append(" offset ");
			sb.append(offset);
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bindable ? 1231 : 1237);
		result = prime * result + limit;
		result = prime * result + offset;
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
		Limit other = (Limit) obj;
		if (bindable != other.bindable)
			return false;
		if (limit != other.limit)
			return false;
		if (offset != other.offset)
			return false;
		return true;
	}

}
