/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity.query.value.controlflow;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.ConditionVisitor;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.Literal;

/**
 * CASE文のWHEN ~ THEN ~句を表す。
 * 
 * @author K.Higuchi
 *
 */
public class When implements ASTNode {
	private static final long serialVersionUID = 3718712397451605139L;

	private Condition condition;
	private ValueExpression result;
	
	public When() {
	}

	public When(Condition condition, ValueExpression result) {
		this.condition = condition;
		this.result = result;
	}
	
	public When(Condition condition, Object resultLiteral) {
		this.condition = condition;
		if (resultLiteral instanceof ValueExpression) {
			this.result = (ValueExpression) resultLiteral;
		} else {
			this.result = new Literal(resultLiteral);
		}
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public ValueExpression getResult() {
		return result;
	}

	public void setResult(ValueExpression result) {
		this.result = result;
	}

	@Override
	public ASTNode accept(ASTTransformer transformer) {
		return transformer.visit(this);
	}

	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (condition != null && visitor instanceof ConditionVisitor) {
				condition.accept((ConditionVisitor) visitor);
			}
			if (result != null) {
				result.accept(visitor);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("when ");
		sb.append(condition);
		sb.append(" then ");
		sb.append(result);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
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
		When other = (When) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		return true;
	}

}
