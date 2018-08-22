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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;

/**
 * PARTITION BY句を表す。
 * 
 * @author K.Higuchi
 *
 */
public class PartitionBy implements ASTNode {
	private static final long serialVersionUID = 8515685165315099514L;

	private List<ValueExpression> partitionFieldList = new ArrayList<ValueExpression>();
	
	public List<ValueExpression> getPartitionFieldList() {
		return partitionFieldList;
	}

	public void setPartitionFieldList(List<ValueExpression> partitionFieldList) {
		this.partitionFieldList = partitionFieldList;
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (partitionFieldList != null) {
				for (ValueExpression s: partitionFieldList) {
					s.accept(visitor);
				}
			}
		}
	}

	public PartitionBy add(ValueExpression partitionField) {
		if (partitionField == null) {
			throw new NullPointerException("partitionField is null");
		}
		if (partitionFieldList == null) {
			partitionFieldList = new ArrayList<ValueExpression>();
		}
		partitionFieldList.add(partitionField);
		return this;
	}
	
	public PartitionBy add(Object partitionField) {
		if (partitionField == null) {
			throw new NullPointerException("partitionField is null");
		}
		if (partitionFieldList == null) {
			partitionFieldList = new ArrayList<ValueExpression>();
		}
		ValueExpression v = null;
		if (partitionField instanceof ValueExpression) {
			v = (ValueExpression) partitionField;
		} else if (partitionField instanceof String) {
			v = new EntityField((String) partitionField);
		} else {
			throw new QueryException("partitionField is ValueExpression or String type required.");
		}
		
		return add(v);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("partition by ");
		if (partitionFieldList != null) {
			for (int i = 0; i < partitionFieldList.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(partitionFieldList.get(i));
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((partitionFieldList == null) ? 0 : partitionFieldList
						.hashCode());
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
		PartitionBy other = (PartitionBy) obj;
		if (partitionFieldList == null) {
			if (other.partitionFieldList != null)
				return false;
		} else if (!partitionFieldList.equals(other.partitionFieldList))
			return false;
		return true;
	}

}
