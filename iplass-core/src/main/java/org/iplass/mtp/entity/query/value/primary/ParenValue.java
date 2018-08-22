/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;

/**
 * ValueExpression上での(　)を表す。
 * 
 * @author K.Higuchi
 *
 */
public class ParenValue extends PrimaryValue {
	private static final long serialVersionUID = -3526585666272234663L;

	private ValueExpression nestedValue;
	
	public ParenValue() {
	}
	
	public ParenValue(ValueExpression nestedValue) {
		this.nestedValue = nestedValue;
	}

	public ValueExpression getNestedValue() {
		return nestedValue;
	}

	public void setNestedValue(ValueExpression nestedValue) {
		this.nestedValue = nestedValue;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(nestedValue).append(")");
		return sb.toString();
	}


	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (nestedValue != null) {
				nestedValue.accept(visitor);
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
		result = prime * result
				+ ((nestedValue == null) ? 0 : nestedValue.hashCode());
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
		ParenValue other = (ParenValue) obj;
		if (nestedValue == null) {
			if (other.nestedValue != null)
				return false;
		} else if (!nestedValue.equals(other.nestedValue))
			return false;
		return true;
	}
	
}
