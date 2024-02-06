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

package org.iplass.mtp.entity.query.value.window;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;

/**
 * Window関数のORDER BYのソート仕様を表す。
 * 
 * @author K.Higuchi
 *
 */
public class WindowSortSpec implements ASTNode {
	//TODO v3.2でAbstractSortSpec継承に
	private static final long serialVersionUID = 5226124289545051191L;

	private ValueExpression sortKey;
	private SortType type;
	private NullOrderingSpec nullOrderingSpec;
	
	public WindowSortSpec() {
	}
	
	public WindowSortSpec(String sortKeyField, SortType type) {
		super();
		this.sortKey = new EntityField(sortKeyField);
		this.type = type;
	}
	
	public WindowSortSpec(ValueExpression sortKey, SortType type) {
		super();
		this.sortKey = sortKey;
		this.type = type;
	}
	
	public WindowSortSpec(ValueExpression sortKey, SortType type, NullOrderingSpec nullOrderingSpec) {
		super();
		this.sortKey = sortKey;
		this.type = type;
		this.nullOrderingSpec = nullOrderingSpec;
	}
	
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			sortKey.accept(visitor);
		}
	}

	public ValueExpression getSortKey() {
		return sortKey;
	}

	public void setSortKey(ValueExpression sortKey) {
		this.sortKey = sortKey;
	}

	public SortType getType() {
		return type;
	}

	public void setType(SortType type) {
		this.type = type;
	}
	
	public NullOrderingSpec getNullOrderingSpec() {
		return nullOrderingSpec;
	}

	public void setNullOrderingSpec(NullOrderingSpec nullOrderingSpec) {
		this.nullOrderingSpec = nullOrderingSpec;
	}
	
	public WindowSortSpec nulls(NullOrderingSpec nullOrderingSpec) {
		this.nullOrderingSpec = nullOrderingSpec;
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(sortKey);
		if (type != null) {
			sb.append(" ");
			sb.append(type);
		}
		if (nullOrderingSpec != null) {
			sb.append(" NULLS ");
			sb.append(nullOrderingSpec);
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((nullOrderingSpec == null) ? 0 : nullOrderingSpec.hashCode());
		result = prime * result + ((sortKey == null) ? 0 : sortKey.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		WindowSortSpec other = (WindowSortSpec) obj;
		if (nullOrderingSpec != other.nullOrderingSpec)
			return false;
		if (sortKey == null) {
			if (other.sortKey != null)
				return false;
		} else if (!sortKey.equals(other.sortKey))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
