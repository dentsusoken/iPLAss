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



/**
 * 多項式 （加減算）を表す。
 *
 * @author K.Higuchi
 *
 */
public class Polynomial extends ValueExpression {
	private static final long serialVersionUID = 819825303377880827L;

	//TODO baseValue（第一引数）は別だしのほうが良いか？？

	private List<ValueExpression> addValues;
	private List<ValueExpression> subValues;

	public Polynomial() {
	}

	public Polynomial(ValueExpression baseValue) {
		add(baseValue);
	}

	public Polynomial(List<ValueExpression> addValues,
			List<ValueExpression> subValues) {
		this.addValues = addValues;
		this.subValues = subValues;
	}

	public List<ValueExpression> getAddValues() {
		return addValues;
	}

	public void setAddValues(List<ValueExpression> addValues) {
		this.addValues = addValues;
	}

	public List<ValueExpression> getSubValues() {
		return subValues;
	}

	public void setSubValues(List<ValueExpression> subValues) {
		this.subValues = subValues;
	}

	public Polynomial add(ValueExpression value) {
		if (addValues == null) {
			addValues = new ArrayList<ValueExpression>();
		}
		addValues.add(value);
		return this;
	}

	public Polynomial sub(ValueExpression value) {
		if (subValues == null) {
			subValues = new ArrayList<ValueExpression>();
		}
		subValues.add(value);
		return this;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		if (addValues != null) {
			for (int i = 0; i < addValues.size(); i++) {
				if (i != 0) {
					sb.append("+");
				}
				sb.append(addValues.get(i));
			}
		}
		if (subValues != null) {
			for (int i = 0; i < subValues.size(); i++) {
				sb.append("-");
				sb.append(subValues.get(i));
			}
		}

		return sb.toString();
	}

	public void accept(ValueExpressionVisitor visitor) {
		if (visitor.visit(this)) {
			if (addValues != null) {
				for (ValueExpression v: addValues) {
					v.accept(visitor);
				}
			}
			if (subValues != null) {
				for (ValueExpression v: subValues) {
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
				+ ((addValues == null) ? 0 : addValues.hashCode());
		result = prime * result
				+ ((subValues == null) ? 0 : subValues.hashCode());
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
		Polynomial other = (Polynomial) obj;
		if (addValues == null) {
			if (other.addValues != null)
				return false;
		} else if (!addValues.equals(other.addValues))
			return false;
		if (subValues == null) {
			if (other.subValues != null)
				return false;
		} else if (!subValues.equals(other.subValues))
			return false;
		return true;
	}

}
