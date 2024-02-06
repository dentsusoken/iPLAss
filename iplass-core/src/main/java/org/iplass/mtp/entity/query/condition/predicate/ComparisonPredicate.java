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

package org.iplass.mtp.entity.query.condition.predicate;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Literal;

/**
 * 比較条件文の抽象クラス。
 * 
 * @author K.Higuchi
 *
 */
public abstract class ComparisonPredicate extends Predicate {
	private static final long serialVersionUID = 2764393184976029446L;

	private ValueExpression property;
	private ValueExpression value;
	
	public ComparisonPredicate() {
	}
	
	public ComparisonPredicate(String propertyName, Object valueLiteral) {
		setPropertyName(propertyName);
		if (valueLiteral instanceof ValueExpression) {
			setValue((ValueExpression) valueLiteral);
		} else {
			setValue(new Literal(valueLiteral));
		}
	}
	
	public ComparisonPredicate(ValueExpression property, ValueExpression value) {
		setProperty(property);
		setValue(value);
	}
	
	public String getPropertyName() {
		if (property == null) {
			return null;
		}
		return property.toString();
	}
	
	public void setPropertyName(String propertyName) {
		property = new EntityField(propertyName);
	}
	
	public void setProperty(ValueExpression property) {
		this.property = property;
	}
	
	public ValueExpression getProperty() {
		return property;
	}
	
	public ValueExpression getValue() {
		return value;
	}
	
	public void setValue(ValueExpression value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPropertyName()).append(getOpString()).append(value);
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getOpString() == null) ? 0 : getOpString().hashCode());
		result = prime * result
				+ ((property == null) ? 0 : property.hashCode());
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
		ComparisonPredicate other = (ComparisonPredicate) obj;
		if (getOpString() == null) {
			if (other.getOpString() != null)
				return false;
		} else if (!getOpString().equals(other.getOpString()))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	protected abstract String getOpString();
	
}
