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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;


/**
 * ORDER BY句を表す。
 * 
 * @author K.Higuchi
 *
 */
public class OrderBy implements ASTNode {
	private static final long serialVersionUID = -7797666558027532968L;

	private List<SortSpec> sortSpecList = new ArrayList<SortSpec>();

	public List<SortSpec> getSortSpecList() {
		return sortSpecList;
	}

	public void setSortSpecList(List<SortSpec> sortSpecList) {
		this.sortSpecList = sortSpecList;
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
	public void accept(QueryVisitor visitor) {
		if (visitor.visit(this)) {
			if (sortSpecList != null) {
				for (SortSpec s: sortSpecList) {
					s.accept(visitor);
				}
			}
		}
	}

	public OrderBy add(SortSpec sortSpec) {
		if (sortSpec == null) {
			throw new NullPointerException("sortSpec is null");
		}
		if (sortSpecList == null) {
			sortSpecList = new ArrayList<SortSpec>();
		}
		sortSpecList.add(sortSpec);
		return this;
	}
	
	public OrderBy add(Object value, SortType type) {
		return add(value, type, null);
	}
	
	public OrderBy add(Object value, SortType type, NullOrderingSpec nullOrderingSpec) {
		if (value == null) {
			throw new NullPointerException("value is null");
		}
		if (sortSpecList == null) {
			sortSpecList = new ArrayList<SortSpec>();
		}
		ValueExpression v = null;
		if (value instanceof ValueExpression) {
			v = (ValueExpression) value;
		} else if (value instanceof String) {
			v = new EntityField((String) value);
		} else {
			throw new QueryException("value is ValueExpression or String type required.");
		}
		
		return add(new SortSpec(v, type, nullOrderingSpec));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("order by ");
		if (sortSpecList != null) {
			for (int i = 0; i < sortSpecList.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(sortSpecList.get(i));
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sortSpecList == null) ? 0 : sortSpecList.hashCode());
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
		OrderBy other = (OrderBy) obj;
		if (sortSpecList == null) {
			if (other.sortSpecList != null)
				return false;
		} else if (!sortSpecList.equals(other.sortSpecList))
			return false;
		return true;
	}
}
