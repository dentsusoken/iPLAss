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

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformer;
import org.iplass.mtp.entity.query.condition.ConditionVisitor;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.ValueExpressionVisitor;
import org.iplass.mtp.entity.query.value.primary.EntityField;

/**
 * IS NULL条件文を表す。
 * 
 * @author K.Higuchi
 *
 */
public class IsNull extends Predicate {
	private static final long serialVersionUID = -1655365124604984488L;

	private ValueExpression property;
	
	public IsNull() {
	}
	
	public IsNull(String propertyName) {
		setPropertyName(propertyName);
	}

	public IsNull(ValueExpression property) {
		setProperty(property);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPropertyName()).append(" is null");
		return sb.toString();
	}

	public void accept(ConditionVisitor visitor) {
		if (visitor.visit(this)) {
			if (visitor instanceof ValueExpressionVisitor) {
				if (getProperty() != null) {
					getProperty().accept((ValueExpressionVisitor) visitor);
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
				+ ((property == null) ? 0 : property.hashCode());
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
		IsNull other = (IsNull) obj;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		return true;
	}

}
