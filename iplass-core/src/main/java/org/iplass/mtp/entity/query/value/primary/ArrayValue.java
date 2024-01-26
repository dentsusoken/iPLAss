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

package org.iplass.mtp.entity.query.value.primary;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;


/**
 * 配列項目を表す。
 * 
 * @author K.Higuchi
 *
 */
public class ArrayValue extends PrimaryValue {
	private static final long serialVersionUID = -6737911797287905026L;

	private List<ValueExpression> values;
	
	public ArrayValue() {
	}

	public ArrayValue(List<ValueExpression> values) {
		this.values = values;
	}
	
	public ArrayValue(ValueExpression... value) {
		if (value != null) {
			values = new ArrayList<ValueExpression>();
			for (ValueExpression v: value) {
				values.add(v);
			}
		}
	}
	
	@Override
	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (values != null) {
				for (ValueExpression v: values) {
					v.accept(visitor);
				}
			}
		}
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public String toString() {
		if (values == null) {
			return "array[]";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("array[");
			for (int i = 0; i < values.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(values.get(i));
			}
			sb.append("]");
			return sb.toString();
		}
	}
	
	public List<ValueExpression> getValues() {
		return values;
	}

	public void setValues(List<ValueExpression> values) {
		this.values = values;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		ArrayValue other = (ArrayValue) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

}
