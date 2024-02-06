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
package org.iplass.mtp.entity.query.value;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.primary.Literal;

/**
 * 行の項目のリストを表す。
 * 複数項目でのin条件指定の際に利用する。<br>
 * 例：<br>
 * (a, b) in ((1,'x'),(2,'y'),...)<br>
 * の(1,'x')を表すValueExpression。
 * 
 * @author K.Higuchi
 *
 */
public class RowValueList extends ValueExpression {
	private static final long serialVersionUID = -7414534715211409579L;

	private List<ValueExpression> rowValues;
	
	public RowValueList() {
	}
	
	public RowValueList(List<ValueExpression> rowValues) {
		this.rowValues = rowValues;
	}
	
	public RowValueList(ValueExpression... rowValue) {
		if (rowValue != null) {
			rowValues = new ArrayList<ValueExpression>();
			for (ValueExpression v: rowValue) {
				rowValues.add(v);
			}
		}
	}
	
	public RowValueList(Object... literalRowValue) {
		if (literalRowValue != null) {
			rowValues = new ArrayList<ValueExpression>();
			for (Object o: literalRowValue) {
				if (o instanceof Literal) {
					rowValues.add((ValueExpression) o);
				} else {
					rowValues.add(new Literal(o));
				}
			}
		}
	}
	
	public List<ValueExpression> getRowValues() {
		return rowValues;
	}

	public void setRowValues(List<ValueExpression> rowValues) {
		this.rowValues = rowValues;
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (rowValues != null) {
				for (ValueExpression v: rowValues) {
					v.accept(visitor);
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((rowValues == null) ? 0 : rowValues.hashCode());
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
		RowValueList other = (RowValueList) obj;
		if (rowValues == null) {
			if (other.rowValues != null)
				return false;
		} else if (!rowValues.equals(other.rowValues))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (rowValues == null) {
			return "()";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for (int i = 0; i < rowValues.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(rowValues.get(i));
			}
			sb.append(")");
			return sb.toString();
		}
	}

}
