/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;

/**
 * WITHIN GROUP句を表す。
 * 
 * @author K.Higuchi
 *
 */
public class WithinGroup implements ASTNode {
	private static final long serialVersionUID = -4852397116891135710L;

	private List<WithinGroupSortSpec> sortSpecList = new ArrayList<WithinGroupSortSpec>();

	public List<WithinGroupSortSpec> getSortSpecList() {
		return sortSpecList;
	}

	public void setSortSpecList(List<WithinGroupSortSpec> sortSpecList) {
		this.sortSpecList = sortSpecList;
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (sortSpecList != null) {
				for (WithinGroupSortSpec s: sortSpecList) {
					s.accept(visitor);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("within group(order by ");
		if (sortSpecList != null) {
			for (int i = 0; i < sortSpecList.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(sortSpecList.get(i));
			}
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sortSpecList == null) ? 0 : sortSpecList.hashCode());
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
		WithinGroup other = (WithinGroup) obj;
		if (sortSpecList == null) {
			if (other.sortSpecList != null)
				return false;
		} else if (!sortSpecList.equals(other.sortSpecList))
			return false;
		return true;
	}

	public WithinGroup add(WithinGroupSortSpec sortSpec) {
		if (sortSpec == null) {
			throw new NullPointerException("sortSpec is null");
		}
		if (sortSpecList == null) {
			sortSpecList = new ArrayList<WithinGroupSortSpec>();
		}
		sortSpecList.add(sortSpec);
		return this;
	}
	
	public WithinGroup add(Object value, SortType type) {
		return add(value, type, null);
	}
	
	public WithinGroup add(Object value, SortType type, NullOrderingSpec nullOrderingSpec) {
		if (value == null) {
			throw new NullPointerException("value is null");
		}
		if (sortSpecList == null) {
			sortSpecList = new ArrayList<WithinGroupSortSpec>();
		}
		ValueExpression v = null;
		if (value instanceof ValueExpression) {
			v = (ValueExpression) value;
		} else if (value instanceof String) {
			v = new EntityField((String) value);
		} else {
			throw new QueryException("value is ValueExpression or String type required.");
		}
		
		return add(new WithinGroupSortSpec(v, type, nullOrderingSpec));
	}
}
