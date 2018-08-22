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

package org.iplass.mtp.entity.query.condition.expr;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.ConditionVisitor;

/**
 * (　)を表す。
 * 
 * @author K.Higuchi
 *
 */
public class Paren extends Condition {
	private static final long serialVersionUID = 2119445900243861643L;

	private Condition nestedExpression;
	
	public Paren() {
	}
	
	public Paren(Condition nestedExpression) {
		this.nestedExpression = nestedExpression;
	}
	
	public void setNestedExpression(Condition nestedExpression) {
		this.nestedExpression = nestedExpression;
	}
	
	public Condition getNestedExpression() {
		return nestedExpression;
	}

	public void accept(ConditionVisitor visitor) {
		if (visitor.visit(this)) {
			if (nestedExpression != null) {
				nestedExpression.accept(visitor);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((nestedExpression == null) ? 0 : nestedExpression.hashCode());
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
		Paren other = (Paren) obj;
		if (nestedExpression == null) {
			if (other.nestedExpression != null)
				return false;
		} else if (!nestedExpression.equals(other.nestedExpression))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(nestedExpression).append(")");
		return sb.toString();
	}
	
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}
	
}
