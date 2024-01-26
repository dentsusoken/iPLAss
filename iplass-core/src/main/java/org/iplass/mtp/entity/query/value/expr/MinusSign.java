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

package org.iplass.mtp.entity.query.value.expr;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.ParenValue;

/**
 * -を表す。
 * 
 * @author K.Higuchi
 *
 */
public class MinusSign extends ValueExpression {
	private static final long serialVersionUID = 6263851229554606319L;

	private ValueExpression value;
	
	public MinusSign() {
	}
	
	public MinusSign(ValueExpression value) {
		setValue(value);
	}

	public ValueExpression getValue() {
		return value;
	}

	public void setValue(ValueExpression value) {
		//Polynomial(+,-)の場合は()を付ける
		if (value instanceof Polynomial) {
			this.value = new ParenValue(value);
		} else {
			this.value = value;
		}
	}

	@Override
	public String toString() {
		if (value == null) {
			return "-null";
		}
		return "-" + value.toString();
	}

	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (value != null) {
				value.accept(visitor);
			}
		}
	}
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		MinusSign other = (MinusSign) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
