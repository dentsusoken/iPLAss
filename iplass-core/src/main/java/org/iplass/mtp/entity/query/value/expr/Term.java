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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.ParenValue;


/**
 * 項（乗除算）を表す。
 * 
 * @author K.Higuchi
 *
 */
public class Term extends ValueExpression {
	private static final long serialVersionUID = -322677503164182618L;

	//TODO baseValue（第一引数）は別だしのほうが良いか？？
	
	private List<ValueExpression> mulValues;
	private List<ValueExpression> divValues;
	
	public Term() {
	}
	
	public Term(ValueExpression baseValue) {
		mul(baseValue);
	}
	
	public Term(List<ValueExpression> mulValues,
			List<ValueExpression> divValues) {
		if (mulValues != null) {
			for (ValueExpression v: mulValues) {
				mul(v);
			}
		}
		if (divValues != null) {
			for (ValueExpression v: divValues) {
				div(v);
			}
		}
	}

	public List<ValueExpression> getMulValues() {
		return mulValues;
	}

	public void setMulValues(List<ValueExpression> mulValues) {
		this.mulValues = mulValues;
	}

	public List<ValueExpression> getDivValues() {
		return divValues;
	}

	public void setDivValues(List<ValueExpression> divValues) {
		this.divValues = divValues;
	}

	public Term mul(ValueExpression value) {
		if (mulValues == null) {
			mulValues = new ArrayList<ValueExpression>();
		}
		//Polynomial(+,-)の場合は()を付ける
		if (value instanceof Polynomial) {
			value = new ParenValue(value);
		}
		mulValues.add(value);
		return this;
	}
	
	public Term div(ValueExpression value) {
		if (divValues == null) {
			divValues = new ArrayList<ValueExpression>();
		}
		//Polynomial(+,-)の場合は()を付ける
		if (value instanceof Polynomial) {
			value = new ParenValue(value);
		}
		divValues.add(value);
		return this;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		if (mulValues != null) {
			for (int i = 0; i < mulValues.size(); i++) {
				if (i != 0) {
					sb.append("*");
				}
				sb.append(mulValues.get(i));
			}
		}
		if (divValues != null) {
			for (int i = 0; i < divValues.size(); i++) {
				sb.append("/");
				sb.append(divValues.get(i));
			}
		}
		
		return sb.toString();
	}

	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (mulValues != null) {
				for (ValueExpression v: mulValues) {
					v.accept(visitor);
				}
			}
			if (divValues != null) {
				for (ValueExpression v: divValues) {
					v.accept(visitor);
				}
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
				+ ((divValues == null) ? 0 : divValues.hashCode());
		result = prime * result
				+ ((mulValues == null) ? 0 : mulValues.hashCode());
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
		Term other = (Term) obj;
		if (divValues == null) {
			if (other.divValues != null)
				return false;
		} else if (!divValues.equals(other.divValues))
			return false;
		if (mulValues == null) {
			if (other.mulValues != null)
				return false;
		} else if (!mulValues.equals(other.mulValues))
			return false;
		return true;
	}

}
